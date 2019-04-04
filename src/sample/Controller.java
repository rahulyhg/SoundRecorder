package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.sound.sampled.*;
import java.io.File;

public class Controller {

    public Button ButtonStart;
    public Button ButtonStop;
    public Button ButtonPlay;
    public TextField LabelFileSave;
    public TextField LabelFileRead;
    public Button ButtonConfirmTop;
    public Button ButtonConfirm;

    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private String filenamePlay;
    private String filenameRecord;

    public void onActionButtonStart(ActionEvent actionEvent) {
        captureAudio();
    }

    public void onActionButtonStop(ActionEvent actionEvent) {
        targetDataLine.stop();
        targetDataLine.close();
    }

    public void onActionButtonPlay(ActionEvent actionEvent) {
        playRecord();
    }


    public void onActionButtonConfirmTop(ActionEvent actionEvent) {
        filenameRecord = LabelFileSave.getText();
    }

    public void onActionButtonConfirmBottom(ActionEvent actionEvent) {
        filenamePlay = LabelFileRead.getText();
    }

    void captureAudio() {
        try {
            audioFormat = new AudioFormat(8000.0F, 16, 1, true, false);
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            new CaptureThread().start();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    void playRecord() {
        try {
            Clip clip = AudioSystem.getClip();
            File sound = new File(filenamePlay);
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    class CaptureThread extends Thread {
        public void run() {
            AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;
            File audioFile = new File(filenameRecord);

            try {
                targetDataLine.open(audioFormat);
                targetDataLine.start();
                AudioSystem.write(new AudioInputStream(targetDataLine), fileType, audioFile);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}





