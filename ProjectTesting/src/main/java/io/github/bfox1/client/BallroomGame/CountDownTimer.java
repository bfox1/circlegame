package io.github.bfox1.client.BallroomGame;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by bfox1 on 8/28/2017.
 */
public class CountDownTimer
{
     private int interval;
     private int totalTime;
     private Timer timer;

    public void timer() {

        int delay = 1000;
        int period = 1000;
        timer = new Timer();
        interval = 60;
        totalTime = interval;
        timer.scheduleAtFixedRate(new TimerTask() {

            public void run() {
                System.out.println(setInterval());

            }
        }, delay, period);
    }

    private final int setInterval() {
        if (interval == 1)
            timer.cancel();
        return --interval;
    }

    public int getInterval()
    {
        return interval;
    }

    public void addInterval(int i)
    {
        this.interval += i;
        this.totalTime += i;
    }

    public int getTotalTime() {
        return totalTime;
    }

    public final void restart()
    {
        this.interval = 60;
        this.totalTime = 60;
    }
}
