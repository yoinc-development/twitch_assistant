package ch.yoinc.service;

import com.google.cloud.speech.v1.*;
import com.google.protobuf.ByteString;

import javax.sound.sampled.*;
import java.io.File;
import java.nio.file.Files;

public class SpeechToTextService {

    private TargetDataLine targetDataLine;
    private File tempAudioFile = new File("streamer_message.wav");

    public void startRecording() {
        new Thread(() -> {
            try {
                AudioFormat audioFormat = new AudioFormat(16000, 16, 1, true, true);
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat);
                if(!AudioSystem.isLineSupported(info)) {
                    return;
                }

                targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
                targetDataLine.open(audioFormat);
                targetDataLine.start();

                AudioInputStream audioInputStream = new AudioInputStream(targetDataLine);
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, tempAudioFile);

            } catch (Exception e) {
                e.printStackTrace();

            }
        }).start();
    }

    public void stopRecording() {
        if(targetDataLine != null && targetDataLine.isOpen()) {
            targetDataLine.stop();
            targetDataLine.close();
        }
    }
    public String transcribeAudio() {
        try (SpeechClient speechClient = SpeechClient.create()) {
            byte[] audioBytes = Files.readAllBytes(tempAudioFile.toPath());
            RecognitionConfig config = RecognitionConfig.newBuilder()
                    .setEncoding(RecognitionConfig.AudioEncoding.LINEAR16)
                    .setSampleRateHertz(16000)
                    .setLanguageCode("en-US")
                    .build();

            RecognitionAudio audio = RecognitionAudio.newBuilder()
                    .setContent(ByteString.copyFrom(audioBytes))
                    .build();

            RecognizeResponse response = speechClient.recognize(config, audio);
            return response.getResultsList().stream()
                    .map(result -> result.getAlternatives(0).getTranscript())
                    .reduce("", String::concat);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
