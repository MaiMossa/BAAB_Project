package utils;

import ws.schild.jave.Encoder;
import ws.schild.jave.EncoderException;
import ws.schild.jave.MultimediaObject;
import ws.schild.jave.encode.AudioAttributes;
import ws.schild.jave.encode.EncodingAttributes;
import ws.schild.jave.encode.VideoAttributes;


import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenRecorderUtils {

    public final static String RECORDINGS_PATH = "test-outputs/recordings/";

    private static File currentRecordingFile;

    /**
     * Start recording (here, just create a placeholder file name for demo)
     */
    public static void startRecording() {
        try {
            File recordingsDir = new File(RECORDINGS_PATH);
            if (!recordingsDir.exists()) recordingsDir.mkdirs();

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            currentRecordingFile = new File(RECORDINGS_PATH + "Recording_" + timestamp + ".avi");

            // TODO: Add real screen capture library here if needed
            System.out.println("Recording started. File will be: " + currentRecordingFile.getAbsolutePath());

        } catch (Exception e) {
            System.err.println("Failed to start recording: " + e.getMessage());
        }
    }

    /**
     * Stop recording and convert to mp4
     */
    public static File stopRecording(String name) {
        if (currentRecordingFile == null) return null;

        try {
            // TODO: Here you would stop the actual recording process

            // Convert to MP4 (even if AVI is empty, for demo)
            File mp4File = new File(currentRecordingFile.getParent(),
                    currentRecordingFile.getName().replace(".avi", ".mp4"));

            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("aac");

            VideoAttributes video = new VideoAttributes();
            video.setCodec("libx264");

            EncodingAttributes encodingAttributes = new EncodingAttributes();
            encodingAttributes.setOutputFormat("mp4");
            encodingAttributes.setAudioAttributes(audio);
            encodingAttributes.setVideoAttributes(video);

            Encoder encoder = new Encoder();
            encoder.encode(new MultimediaObject(currentRecordingFile), mp4File, encodingAttributes);

            // Delete original AVI
            if (mp4File.exists()) currentRecordingFile.delete();

            System.out.println("Recording stopped. Converted to MP4: " + mp4File.getAbsolutePath());
            return mp4File;

        } catch (EncoderException e) {
            System.err.println("Failed to convert video to MP4: " + e.getMessage());
        }
        return null;
    }
}
