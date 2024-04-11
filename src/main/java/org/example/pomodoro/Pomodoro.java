package org.example.pomodoro;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Pomodoro implements MyTimer.TimerCallBack {
    //
    // Elements in Pomodoro tab
    //

    @FXML
    private AnchorPane pomodoroAnchorPane;
    @FXML
    private Label currentModeLabel;
    @FXML
    private Label motivationLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Button startButton;
    @FXML
    private Button pauseButton;
    @FXML
    private Button resumeButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button resetButton;
    @FXML
    private Button closeButton;
    @FXML
    private ChoiceBox<String> modeChoiceBox;

    //
    // Elements in Settings tab
    //

    @FXML
    private ToggleButton customToggleButton;
    @FXML
    private ToggleButton defaultToggleButton;
    @FXML
    private GridPane gridPane1;
    @FXML
    private GridPane gridPane2;
    @FXML
    private TextField focusTextField;
    @FXML
    private TextField shortBreakTextField;
    @FXML
    private TextField longBreakTextField;
    @FXML
    private TextField intervalTextField;
    @FXML
    private Button setFocusTimeButton;
    @FXML
    private Button setShortBreakTimeButton;
    @FXML
    private Button setLongBreakTimeButton;
    @FXML
    private Button setIntervalButton;
    @FXML
    private Button setAllButton;
    @FXML
    private Label confirmationLabel;

    //
    // Other items
    //

    private String currentMode;
    private int pomodoroSeconds;
    private int shortBreakSeconds;
    private int longBreakSeconds;
    private int interval;
    private int shortBreaksTaken;
    private int timeLeft;
    private Boolean timerRunning;
    private String[] modes = {"Pomodoro", "Short Break", "Long Break"};
    private MyTimer timer = new MyTimer(this);

    //
    // Methods
    //

    @FXML
    private void initialize() {
        modeChoiceBox.getItems().addAll(modes);
        modeChoiceBox.setOnAction(this::getMode);

        setDefaultTimes();
        timerRunning = true;

        pauseButton.setVisible(false);
        resumeButton.setVisible(false);
        gridPane1.setVisible(false);
        gridPane2.setVisible(false);
        setAllButton.setVisible(false);
        defaultToggleButton.setSelected(true);
    }

    @FXML
    private void getMode(ActionEvent e1) {
        currentMode = modeChoiceBox.getValue();
        setModeTimeLabel();
    }

    @FXML
    private void onStartButtonClick() {
        startTimer();
    }

    @FXML
    private void onPauseButtonClick() {
        stopTimer();
    }

    @FXML
    private void onResumeButtonClick() {
        resumeTimer();
    }

    @FXML
    private void onResetButtonClick() {
        resetTimer();
    }

    @FXML
    private void onCloseButtonClick(ActionEvent e2) {
        if (timerRunning) stopTimer();
        Node source = (Node) e2.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onNextButtonClick() {
        nextTimer();
        startButton.setVisible(true);
        pauseButton.setVisible(false);
        resumeButton.setVisible(false);
    }

    @FXML
    private void toggleButton(ActionEvent event) {
        if (event.getSource() == customToggleButton) {
            defaultToggleButton.setSelected(false);
            customToggleButton.setSelected(true);
            gridPane1.setVisible(true);
            gridPane2.setVisible(true);
            setAllButton.setVisible(true);
            setAllButtonClick();
            confirmationLabel.setText("Custom times enabled.");
        }
        if (event.getSource() == defaultToggleButton) {
            customToggleButton.setSelected(false);
            defaultToggleButton.setSelected(true);
            gridPane1.setVisible(false);
            gridPane2.setVisible(false);
            setAllButton.setVisible(false);
            setDefaultTimes();
            resetTimer();
            confirmationLabel.setText("Default times enabled");
        }
    }

    @FXML
    private void setFocusTimeButtonClick() {
        String str = focusTextField.getText();
        if (!str.isEmpty()) {
            pomodoroSeconds = Integer.parseInt(str) * 60;
            confirmationLabel.setText("Custom focus time set!");
        }
        setModeTimeLabel();
    }

    @FXML
    private void setShortBreakButtonClick() {
        String str = shortBreakTextField.getText();
        if (!str.isEmpty()) {
            shortBreakSeconds = Integer.parseInt(str) * 60;
            confirmationLabel.setText("Custom short break time set!");
        }
        setModeTimeLabel();
    }

    @FXML
    private void setLongBreakButtonClick() {
        String str = longBreakTextField.getText();
        if (!str.isEmpty()) {
            longBreakSeconds = Integer.parseInt(str) * 60;
            confirmationLabel.setText("Custom long break time set!");
        }
        setModeTimeLabel();
    }

    @FXML
    private void setIntervalButtonClick() {
        String str = intervalTextField.getText();
        if (!str.isEmpty()) {
            interval = Integer.parseInt(str) + 1;
            confirmationLabel.setText("Custom interval set!");
        }
        setModeTimeLabel();
    }

    @FXML
    private void setAllButtonClick() {
        String str = focusTextField.getText();
        if (!str.isEmpty()) pomodoroSeconds = Integer.parseInt(str) * 60;
        str = shortBreakTextField.getText();
        if (!str.isEmpty()) shortBreakSeconds = Integer.parseInt(str) * 60;
        str = longBreakTextField.getText();
        if (!str.isEmpty()) longBreakSeconds = Integer.parseInt(str) * 60;
        str = intervalTextField.getText();
        if (!str.isEmpty()) interval = Integer.parseInt(str) + 1;
        confirmationLabel.setText("Custom times set!");
        setModeTimeLabel();
    }

    private void startTimer() {
        if (timerRunning) {
            if (currentMode.equals("Pomodoro")) {
                timer.setSecondsLeft(pomodoroSeconds);
            } else if (currentMode.equals("Short Break")) {
                timer.setSecondsLeft(shortBreakSeconds);
            } else {
                timer.setSecondsLeft(longBreakSeconds);
            }
            timer.startSession();
        } else {
            timer = new MyTimer(this);
            if (currentMode.equals("Pomodoro")) {
                timer.setSecondsLeft(pomodoroSeconds);
            } else if (currentMode.equals("Short Break")) {
                timer.setSecondsLeft(shortBreakSeconds);
            } else {
                timer.setSecondsLeft(longBreakSeconds);
            }
            timer.startSession();
            timerRunning = true;
        }
        startButton.setVisible(false);
        pauseButton.setVisible(true);
        resumeButton.setVisible(false);
    }

    private void stopTimer() {
        timeLeft = timer.stopSession();
        timerRunning = false;
        pauseButton.setVisible(false);
        resumeButton.setVisible(true);
    }

    private void resumeTimer() {
        timer = new MyTimer(this);
        timer.setSecondsLeft(timeLeft + 1);
        timer.startSession();
        timerRunning = true;
        pauseButton.setVisible(true);
        resumeButton.setVisible(false);
    }

    private void resetTimer() {
        if (timerRunning) stopTimer();
        timeLeft = 0;
        shortBreaksTaken = 0;
        pauseButton.setVisible(false);
        resumeButton.setVisible(false);
        startButton.setVisible(true);
        setModeTimeLabel();
    }

    private void nextTimer() {
        if (timerRunning) stopTimer();
        timeLeft = 0;
        if (currentMode.equals("Short Break") || currentMode.equals("Long Break")) {
            currentMode = "Pomodoro";
            modeChoiceBox.setValue("Pomodoro");
        } else {
            if (shortBreaksTaken % interval == 0 && shortBreaksTaken > 0) {
                currentMode = "Long Break";
                modeChoiceBox.setValue("Long Break");
            } else {
                currentMode = "Short Break";
                modeChoiceBox.setValue("Short Break");
            }
        }
        setModeTimeLabel();
    }

    private void setDefaultTimes() {
        pomodoroSeconds = 1500;
        shortBreakSeconds = 300;
        longBreakSeconds = 900;
        interval = 5;
        timeLeft = 0;
        shortBreaksTaken = 0;

        currentMode = "Pomodoro";
        modeChoiceBox.setValue("Pomodoro");
    }

    private void setModeTimeLabel() {
        if (currentMode.equals("Pomodoro")) {
            timeLabel.setText(String.format("%02d:%02d", (pomodoroSeconds / 60), (pomodoroSeconds % 60)));
            currentModeLabel.setText(currentMode);
            setPomodoroAnchorPaneColour();
            motivationLabel.setText("Time to focus!");
        } else if (currentMode.equals("Short Break")) {
            timeLabel.setText(String.format("%02d:%02d", (shortBreakSeconds / 60), (shortBreakSeconds % 60)));
            currentModeLabel.setText(currentMode);
            setPomodoroAnchorPaneColour();
            motivationLabel.setText("Time for a break!");
        } else {
            timeLabel.setText(String.format("%02d:%02d", (longBreakSeconds / 60), (longBreakSeconds % 60)));
            currentModeLabel.setText(currentMode);
            setPomodoroAnchorPaneColour();
            motivationLabel.setText("Time for a break!");
        }
    }

    private void notifyWithSound() {
        String soundFilePath = getClass().getResource("/sounds/timerEnd.mp3").toString();
        Media sound = new Media(soundFilePath);
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    private void setPomodoroAnchorPaneColour(){
        if(currentMode.equals("Pomodoro"))
            pomodoroAnchorPane.setStyle("-fx-background-color: #AD504D;");
        else if(currentMode.equals("Short Break"))
            pomodoroAnchorPane.setStyle("-fx-background-color: #4D8389;");
        else
            pomodoroAnchorPane.setStyle("-fx-background-color: #476F94;");
    }

    private void setTransparentButtonText(Button button) {
        button.setStyle("-fx-background-color: white;");
        button.setTextFill(Color.rgb(0, 0, 0, 0.5));
    }

    @Override
    public void onUpdate(int minutes, int seconds) {
        Platform.runLater(() -> {
            timeLabel.setText(String.format("%02d:%02d", minutes, seconds));
            if (minutes == 0 && seconds == 0) {
                notifyWithSound();
                if (currentMode.equals("Pomodoro")) shortBreaksTaken++;
                nextTimer();
                startTimer();
            }
        });
    }
}