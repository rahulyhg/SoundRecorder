package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import javax.sound.sampled.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Controller {

    /*############### REFECENCES FOR GUI ###############*/
    public Button ButtonStart;
    public Button ButtonStop;
    public Button ButtonPlay;
    public TextField LabelFileSave;
    public TextField LabelFileRead;
    public Button ButtonConfirmTop;
    public Button ButtonConfirm;

    /*############### REFECENCES FOR RECORD AND PLAY ###############*/
    private AudioFormat audioFormat;
    private TargetDataLine targetDataLine;
    private String filenamePlay;
    private String filenameRecord;
    private PopOutWindow popOutWindow = new PopOutWindow();

    /*############### GUI METHODS ###############*/
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

    /* ############### RECORD AND PLAY METHODS AND ALSO INNER CLASS ###############*/

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

    private void captureAudio() {
        try {
            audioFormat = new AudioFormat(8000.0F, 16, 1, true, false);
            DataLine.Info dataLineInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            targetDataLine = (TargetDataLine) AudioSystem.getLine(dataLineInfo);
            CaptureThread captureThread = new CaptureThread();
            captureThread.start();
        } catch (NullPointerException e) {
            popOutWindow.messageBox("Error", "Nie wprowadzono lub nie zatwiedzono nazwy pliku", Alert.AlertType.ERROR);
        } catch (IllegalArgumentException | LineUnavailableException e) {
            popOutWindow.messageBox("Error", "Brak mikrofonu lub niezgodność ze standardem", Alert.AlertType.ERROR);
            System.exit(0);
        }
    }

    private void playRecord() {
        try {
            Clip clip = AudioSystem.getClip();
            File sound = new File(filenamePlay);
            clip.open(AudioSystem.getAudioInputStream(sound));
            clip.start();
            Thread.sleep(clip.getMicrosecondLength() / 1000);
        } catch (Exception exc) {
            popOutWindow.messageBox("Error", "Nie wprowadzono lub nie zatwiedzono nazwy pliku", Alert.AlertType.ERROR);
        }
    }
}





