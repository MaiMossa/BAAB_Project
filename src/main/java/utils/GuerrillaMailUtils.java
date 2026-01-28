package utils;

import io.qameta.allure.Step;
import io.restassured.filter.cookie.CookieFilter;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;

public class GuerrillaMailUtils {

    private static final String BASE_URL = "https://api.guerrillamail.com/ajax.php";

    // Extract href values from HTML
    private static final Pattern HREF_PATTERN =
            Pattern.compile("href\\s*=\\s*\"(https?://[^\"]+)\"", Pattern.CASE_INSENSITIVE);

    // Fallback: first https in text
    private static final Pattern FIRST_URL_PATTERN =
            Pattern.compile("(https?://[^\\s\"'<>]+)", Pattern.CASE_INSENSITIVE);

    private final CookieFilter cookieFilter = new CookieFilter();
    private boolean debug = false;

    public GuerrillaMailUtils enableDebug(boolean enabled) {
        this.debug = false;
        return this;
    }

    public static class GuerrillaInbox {
        private final String emailAddress;
        private final String sidToken;

        public GuerrillaInbox(String emailAddress, String sidToken) {
            this.emailAddress = emailAddress;
            this.sidToken = sidToken;
        }

        public String getEmailAddress() { return emailAddress; }
        public String getSidToken() { return sidToken; }
    }

    @Step("GuerrillaMail: Create inbox (get_email_address)")
    public GuerrillaInbox createInbox() {
        Response res = given()
                .filter(cookieFilter)
                .queryParam("f", "get_email_address")
                .when().get(BASE_URL)
                .then().statusCode(200)
                .extract().response();

        JsonPath jp = res.jsonPath();

        String email = jp.getString("email_addr");
        String sid = jp.getString("sid_token");

        if (debug) {
            System.out.println("createInbox response: " + res.asString());
            System.out.println("Generated email: " + email);
            System.out.println("sid_token: " + sid);
        }

        if (email == null || email.isBlank() || sid == null || sid.isBlank()) {
            throw new RuntimeException("Failed to create Guerrilla inbox. Response: " + res.asString());
        }

        return new GuerrillaInbox(email, sid);
    }

    @Step("GuerrillaMail: Try wait for verification email id (by excerpt/from)")
    public Integer tryWaitForVerificationEmailId(String sidToken,
                                                 long timeoutMillis,
                                                 long pollEveryMillis) {

        Instant end = Instant.now().plusMillis(timeoutMillis);

        String lastSeenTopMailId = null;

        while (Instant.now().isBefore(end)) {

            Response res = given()
                    .filter(cookieFilter)
                    .queryParam("f", "get_email_list")
                    .queryParam("offset", 0)
                    .queryParam("sid_token", sidToken)
                    .when().get(BASE_URL)
                    .then().statusCode(200)
                    .extract().response();

            JsonPath jp = res.jsonPath();
            List<Object> emails = jp.getList("list");

            if (debug) {
                String topMailId = (emails == null || emails.isEmpty())
                        ? "EMPTY"
                        : String.valueOf(jp.getString("list[0].mail_id"));

                if (!topMailId.equals(lastSeenTopMailId)) {
                    System.out.println("get_email_list response (changed): " + res.asString());
                    lastSeenTopMailId = topMailId;
                }
            }

            if (emails != null && !emails.isEmpty()) {
                for (int i = 0; i < emails.size(); i++) {

                    String from = jp.getString("list[" + i + "].mail_from");
                    String subject = jp.getString("list[" + i + "].mail_subject");
                    String excerpt = jp.getString("list[" + i + "].mail_excerpt");

                    // Ignore welcome mail
                    if (subject != null && subject.equalsIgnoreCase("Welcome to Guerrilla Mail")) {
                        continue;
                    }

                    boolean looksLikeVerify =
                            (excerpt != null && excerpt.toLowerCase().contains("confirm")) ||
                                    (excerpt != null && excerpt.toLowerCase().contains("verify")) ||
                                    (from != null && !from.toLowerCase().contains("guerrillamail.com"));

                    if (looksLikeVerify) {
                        String mailIdStr = jp.getString("list[" + i + "].mail_id");
                        if (mailIdStr != null && !mailIdStr.isBlank()) return Integer.parseInt(mailIdStr);

                        Integer mailIdInt = jp.getInt("list[" + i + "].mail_id");
                        if (mailIdInt != null) return mailIdInt;
                    }
                }
            }

            try { Thread.sleep(pollEveryMillis); } catch (InterruptedException ignored) {}
        }

        return null;
    }



    @Step("GuerrillaMail: Fetch email body (fetch_email) id={emailId}")
    public String fetchEmailBody(String sidToken, int emailId) {
        Response res = given()
                .filter(cookieFilter)
                .queryParam("f", "fetch_email")
                .queryParam("email_id", emailId)
                .queryParam("sid_token", sidToken)
                .when().get(BASE_URL)
                .then().statusCode(200)
                .extract().response();

        if (debug) System.out.println("fetch_email response: " + res.asString());

        JsonPath jp = res.jsonPath();

        String body = jp.getString("mail_body");
        if (body != null && !body.trim().isEmpty()) return body;

        String source = jp.getString("mail_source");
        return source;
    }

    @Step("Extract confirmation link (prefer ConfirmEmail, else first href, else first URL)")
    public String extractConfirmEmailLink(String body) {
        if (body == null) return null;

        // Prefer ConfirmEmail link
        Matcher m = HREF_PATTERN.matcher(body);
        while (m.find()) {
            String url = m.group(1);
            if (url != null && url.contains("ConfirmEmail")) {
                return url;
            }
        }

        // Fallback: first href
        m = HREF_PATTERN.matcher(body);
        if (m.find()) return m.group(1);

        // Fallback: first https in body
        Matcher u = FIRST_URL_PATTERN.matcher(body);
        return u.find() ? u.group(1) : null;
    }
}
