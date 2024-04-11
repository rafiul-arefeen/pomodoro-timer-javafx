package org.example.pomodoro;

import java.util.Timer;
import java.util.TimerTask;

public class MyTimer {
    public MyTimer(TimerCallBack callBack) {
        this.callBack = callBack;
    }

    private int secondsLeft;
    private Timer timer = new Timer();
    private TimerCallBack callBack;

    private TimerTask task = new TimerTask() {
        @Override
        public void run() {
            int minute = secondsLeft / 60;
            int second = secondsLeft % 60;
            secondsLeft--;

            if (callBack != null) callBack.onUpdate(minute, second);
        }
    };

    public void setSecondsLeft(int secondsLeft) {
        this.secondsLeft = secondsLeft;
    }

    public void startSession() {
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public int stopSession() {
        timer.cancel();
        return secondsLeft;
    }

    public interface TimerCallBack {
        void onUpdate(int minutes, int seconds);
    }
}
