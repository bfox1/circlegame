package io.github.bfox1.client.BallroomGame;


import io.github.bfox1.client.BallroomGame.events.EventRegistry;
import io.github.bfox1.client.BallroomGame.frames.GameMenu;
import io.github.bfox1.client.BallroomGame.frames.PaintSurface;
import io.github.bfox1.client.BallroomGame.utility.BallRoomUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by bfox1 on 8/28/2017.
 * This game is poorly written. Was normally a testing project which turned into a mess of testing
 * and experimenting
 * I will document as much as possible
 */
public class Ballroom extends JFrame
{

    /*
    Initializing static variables
     */
    public static final int WIDTH = 515;

    public static final int HEIGHT = 540;

    public static Ballroom instance;
    /*
    End of static variables.
     */

    private PaintSurface canvas;
    private JLabel label;
    private Button restartButton;
    private GameMenu mainMenu;

    private GameState state;
    private static transient final int env = 1;

    private ScheduledThreadPoolExecutor executor;

    public int points = 0;

    public int misses = 0;

    private final CountDownTimer timer = new CountDownTimer();
    public final EventRegistry eventRegistry = new EventRegistry();

    public static void main(String[] args)
    {
        if(env == 1)
        {
            unloadDependencies();
        }
        instance = new Ballroom();
        instance.init();

    }

    /**
     * Initializes the Game and the BallRoomGame JFrame.
     * Also executes thread.
     */
    public void init()
    {

        this.setSize(WIDTH, HEIGHT);

        //Additional information being set here//
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setTitle("Circle Game Deluxe");
        initMainMenu();

        //Executing Thread//
        executor = new ScheduledThreadPoolExecutor(3);
        executor.scheduleAtFixedRate(new AnimationThread(this),0L, 20L, TimeUnit.MILLISECONDS);

    }

    private void initMainMenu()
    {
        state = GameState.MENU;
        this.mainMenu = new GameMenu();
        this.mainMenu.addMouseListener(new MouseListener()
        {
            public void mouseClicked(MouseEvent e)
            {

            }

            public void mousePressed(MouseEvent e)
            {
                int startMinX = 160;
                int startMinY = 207;
                int startMaxX = 346;
                int startMaxY = 292;

                if(startMinX <= e.getX() && startMaxX >= e.getX())
                {
                    if(startMinY <= e.getY() && startMaxY >= e.getY())
                    {
                        mainMenu.setStartButtonSelected(true);
                    }
                }

            }

            public void mouseReleased(MouseEvent e)
            {
                int startMinX = 160;
                int startMinY = 207;
                int startMaxX = 346;
                int startMaxY = 292;
                if(startMinX <= e.getX() && startMaxX >= e.getX() && mainMenu.getStartButtonSelected())
                {
                    if(startMinY <= e.getY() && startMaxY >= e.getY())
                    {
                        setVisible(false);
                        remove(mainMenu);
                        state = GameState.INACTIVETRANS;
                    }
                }
            }

            public void mouseEntered(MouseEvent e) {

            }

            public void mouseExited(MouseEvent e) {

            }
        });
        this.add(mainMenu, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void initGame()
    {
        this.state = GameState.ACTIVE;
        canvas = new PaintSurface(this);
        label = new JLabel();

        this.restartButton = new Button();

        this.restartButton.setLabel("Restart Game");
        //this.label.setText("Points: " + points );

        BallRoomUtilities.playBackgroundMusic();



        this.restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                state = GameState.RESTART;
            }
        });

        this.add(label, BorderLayout.NORTH);
        this.add(canvas, BorderLayout.CENTER);
        this.add(restartButton, BorderLayout.SOUTH);
        timer.timer();
        this.setVisible(true);
    }

    /**
     * Invokes Restart. Will determine if game can do a Soft reset or a Hard Reset.
     */
    private void restartGame()
    {
        if(timer.getInterval() == 0)
        {
            BallRoomUtilities.stopPlayer();
            restartGameI();
        }
        else
        {
            points = 0;
            misses = 0;
            timer.restart();
            this.state = GameState.ACTIVE;
        }
    }

    /**
     * Invokes Restart. This method should only be used to invoke a Hard Reset
     */
    private static void restartGameI()
    {

        instance.setVisible(false);
        instance.executor.shutdown();
        instance = null;
        instance = new Ballroom();
        instance.init();
    }

    public final CountDownTimer getTimer()
    {
        return timer;
    }

    public final EventRegistry getEventRegistry()
    {
        return eventRegistry;
    }

    /**
     * This is the Animation Thread.
     * Constantly gets executed by Thread and controls Painting.
     */
    class AnimationThread implements Runnable
    {

        JFrame c;

        public AnimationThread(JFrame c)
        {
            this.c = c;

        }
        public void run()
        {
            if(state == GameState.MENU)
            {
                c.repaint();
                try
                {
                Thread.sleep(25);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if(state == GameState.RESTART)
            {
                restartGame();
            }
            else if(state == GameState.INACTIVETRANS)
            {
                initGame();
                c.repaint();
            }
            else if(state == GameState.ACTIVE)
            {
                label.setText("Hits: " + points + " Misses: " + misses + " TimeLeft: " + timer.getInterval());

                if (timer.getInterval() != 0)
                {
                    c.repaint();
                }
                else
                {

                    c.remove(canvas);


                    label.setText("You hit a total of " + points + " Circles and Missed " + misses + " Lasting a total of " + timer.getTotalTime() + "!");

                    c.repaint();

                }
            }

        }
    }

    private static final void unloadJar(String targetLocation)
    {
        File target = new File("lib/"+targetLocation + ".jar");
        if(target.exists())
            return;

        try {
            FileOutputStream out = new FileOutputStream(target);
            ClassLoader loader = Ballroom.class.getClassLoader();
            InputStream in = loader.getResourceAsStream("lib/"+targetLocation+".jar");

            byte[] buf = new byte[8*1024];
            int len;
            while((len = in.read(buf)) != -1)
            {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void unloadDependencies()
    {
        //InputStream iStream = BallroomOG.class.getClassLoader().getResourceAsStream("lib");


        //unloadJar("lib/basicplayer.jar", "lib/basicplayer.jar");
        File path = new File("lib");
        path.mkdirs();
        path.mkdir();
        unloadJar("basicplayer3.0");
        unloadJar("commons-logging-api");
        unloadJar("jl1.0");
        unloadJar("jogg-0.0.7");
        unloadJar("jorbis-0.0.15");
        unloadJar("jspeex0.9.7");
        unloadJar("mp3spi1.9.4");
        unloadJar("tritonus_share");
        unloadJar("vorbisspi1.0.2");
    }

    enum GameState
    {
        MENU(0), ACTIVE(1), PAUSE(2), RESTART(3), CLOSE(4), INACTIVETRANS(5);

        private final int id;
        GameState(int id)
        {
            this.id = id;
        }

        public int getId()
        {
            return id;
        }

    }

}
