package io.github.bfox1.BallRoomGameOG;

import javazoom.jlgui.basicplayer.BasicPlayer;
import javazoom.jlgui.basicplayer.BasicPlayerException;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by bfox1 on 8/28/2017.
 */
public class BallroomOG extends JFrame
{
    public static final int WIDTH = 515;

    public static final int HEIGHT = 540;


    public static BallroomOG ballroom;

    private PaintSurface canvas;
    private GameMenu menu;
    private JLabel label;
    private Button restartButton;
    private String loc = "Pegboard_Nerds-Emergency.mp3";


    private JButton startButton;
    BasicPlayer bPlayer = new BasicPlayer();
    private static transient final int env = 0;

    private GameState state;

    private final CountDownTimer timer = new CountDownTimer();
    private ScheduledThreadPoolExecutor executor;

    private int points = 0;

    private int misses = 0;

    public static void main(String[] args)
    {
        if(env == 1)
        {
            unloadDependencies();
        }

        ballroom = new BallroomOG();
        ballroom.init();

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
    private static final void unloadJar(String targetLocation)
    {
        File target = new File("lib/"+targetLocation + ".jar");
        if(target.exists())
            return;

        try {
            FileOutputStream out = new FileOutputStream(target);
            ClassLoader loader = BallroomOG.class.getClassLoader();
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


    public void initMenuState()
    {


       state = GameState.MENU;
       this.menu = new GameMenu();
       this.menu.addMouseListener(new MouseListener()
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
                       menu.setStartButtonSelected(true);
                   }
               }

           }

           public void mouseReleased(MouseEvent e)
           {
               int startMinX = 160;
               int startMinY = 207;
               int startMaxX = 346;
               int startMaxY = 292;
               if(startMinX <= e.getX() && startMaxX >= e.getX() && menu.getStartButtonSelected())
               {
                   if(startMinY <= e.getY() && startMaxY >= e.getY())
                   {
                       setVisible(false);
                       remove(menu);
                       initGameState();
                   }
               }
           }

           public void mouseEntered(MouseEvent e) {

           }

           public void mouseExited(MouseEvent e) {

           }
       });
       this.add(menu, BorderLayout.CENTER);
        this.setVisible(true);
    }

    public void closeMenuState()
    {
        this.remove(startButton);
    }

    public void closeGameState()
    {
        this.remove(canvas);
        this.remove(label);
        this.remove(restartButton);
    }

    public void initGameState()
    {
        this.state = GameState.ACTIVE;
        canvas = new PaintSurface();
        label = new JLabel();
        this.restartButton = new Button();
        this.restartButton.setLabel("Restart Game");
        final Thread thread = new Thread()
        {

            @Override
            public void run()
            {



                try
                {
                    bPlayer.open(new BufferedInputStream(getClass().getClassLoader().getResourceAsStream("assets/CircleGame/sounds/music/" + loc)));
                    bPlayer.play();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        };
        thread.start();


        this.restartButton.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                state = GameState.RESTART;
            }
        });

        label.setText("Hits: " + points + " Misses: " + misses);

        this.add(label, BorderLayout.NORTH);
        this.add(canvas, BorderLayout.CENTER);
        this.add(restartButton, BorderLayout.SOUTH);
        timer.timer();
        this.setVisible(true);

    }

    public void init()
    {
        this.setSize(WIDTH, HEIGHT);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        initMenuState();

        executor = new ScheduledThreadPoolExecutor(3);
        executor.scheduleAtFixedRate(new AnimationThread(this),0L, 20L, TimeUnit.MILLISECONDS);

    }

    private void restartGame()
    {
        if(timer.getInterval() == 0)
        {
            restartGameI();
            try {
                bPlayer.stop();
            } catch (BasicPlayerException e) {
                e.printStackTrace();
            }
        }
        else
        {
            state = GameState.ACTIVE;
            points = 0;
            misses = 0;
            timer.restart();
        }
    }

    private static void restartGameI()
    {

        ballroom.setVisible(false);
        ballroom.executor.shutdown();
        ballroom = null;
        ballroom = new BallroomOG();
        ballroom.init();
    }

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
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else if(state == GameState.RESTART)
            {
                restartGame();


            }
            else if(state == GameState.ACTIVE)
            {
                label.setText("Hits: " + points + " Misses: " + misses + " TimeLeft: " + timer.getInterval());
                if (timer.getInterval() != 0)
                {
                    c.repaint();
                } else
                {

                    c.remove(canvas);
                    label.setText("You hit a total of " + points + " Circles and Missed " + misses + " Lasting a total of " + timer.getTotalTime() + "!");
                    c.repaint();

                }
            }


        }
    }

    class PaintSurface extends JComponent
    {

        public ArrayList<Ball> balls = new ArrayList<Ball>();

        Rectangle2D r = new Rectangle2D.Float(0,0, 0, 0);

        public PaintSurface()
        {
            for(int i = 0; i < 10; i++)
            {
                balls.add(new Ball(30, balls));
            }

            this.addMouseListener(new MouseListener()
            {
                //UNUSED//
                public void mouseClicked(MouseEvent e) {}

                public void mousePressed(MouseEvent e)
                {
                    int clickedX = e.getX();
                    int clickedY = e.getY();
                    Rectangle2D rectangle2D = new Rectangle2D.Float(clickedX, clickedY, 5, 5);
                    r = rectangle2D;
                }
                //UNUSED//
                public void mouseReleased(MouseEvent e) {}
                //UNUSED//
                public void mouseEntered(MouseEvent e) {}
                //UNUSED//
                public void mouseExited(MouseEvent e) {}
            });
        }



        public void paint(Graphics g)
        {

            Graphics2D g2 = (Graphics2D) g;


            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.RED);

            g2.draw(r);

            int i = 0;
            boolean flag = false;
            for(Ball ball : balls)
            {
                if(ball.intersects(r))
                {
                    if(ball.isBlue)
                    {
                        timer.addInterval(10);
                    }
                    flag = true;
                    r = new Rectangle2D.Float(0,0,0,0);
                    continue;
                }
                if(!flag)
                {
                    i++;
                }
                if(ball.blue())
                {
                    g2.setColor(Color.BLUE);
                    ball.move();
                    g2.fill(ball);
                    g2.setColor(Color.RED);
                }
                else
                    {
                    ball.move();

                    g2.fill(ball);
                }
            }

            if(flag)
            {
                balls.remove(i);
                balls.add(new Ball(new MathHelper().randomInt(20, 50), balls));
                points++;
            }
            else if(r.getX() != 0 && r.getY() != 0)
            {

                misses++;
            }
            r = new Rectangle2D.Float(0,0,0,0);
        }
    }

    class Ball extends Ellipse2D.Float
    {
        private int xSpeed, ySpeed;

        private int d;

        private int width = BallroomOG.WIDTH;
        private int height = BallroomOG.HEIGHT;

        private boolean isBlue = false;

        private boolean isGreen = false;

        private Ball prevSwap;

        private ArrayList<Ball> balls;

        public Ball(int d, ArrayList balls)
        {
            super((int)(Math.random() * (BallroomOG.WIDTH - d) +1), (int)(Math.random() *(BallroomOG.HEIGHT - d) + 1), d, d);
            this.balls = balls;
            this.d = d;

            this.xSpeed = (int)(Math.random() * 5 +1);
            this.ySpeed = (int)(Math.random() * 5 + 1);

            int b = new MathHelper().randomInt(5, 10);

            if(b == 7)
            {
               this.isBlue = true;
            }
            this.xSpeed += 2;
            this.ySpeed += 2;

        }

        public boolean blue()
        {
            return isBlue;
        }

        public void move()
        {

            //Detect collision with other balls
            Rectangle2D r = new Rectangle2D.Float(super.x, super.y, d, d);

            if(prevSwap != null)
            {
                if(this.prevSwap.intersects(r))
                {
                    if(this.xSpeed > 0) this.xSpeed = -xSpeed;
                    else if(this.xSpeed < 0) this.xSpeed = xSpeed*-1;
                    if(this.ySpeed > 0 ) this.ySpeed = -ySpeed;
                    else if(this.ySpeed < 0 ) this.ySpeed = ySpeed*-1;

                    if(this.prevSwap.xSpeed > 0) this.prevSwap.xSpeed = -prevSwap.xSpeed;
                    else if(this.prevSwap.xSpeed < 0) this.prevSwap.xSpeed = prevSwap.xSpeed*-1;
                    if(this.prevSwap.ySpeed > 0 ) this.prevSwap.ySpeed = -prevSwap.ySpeed;
                    else if(this.prevSwap.ySpeed < 0 ) this.prevSwap.ySpeed = prevSwap.ySpeed*-1;

                    this.prevSwap = null;
                }
            }

            for(Ball b : balls)
            {
                if(b != this && b.intersects(r))
                {
                        int tempX = xSpeed;
                        int tempY = ySpeed;
                        xSpeed = b.xSpeed;
                        ySpeed = b.ySpeed;
                        b.xSpeed = tempX;
                        b.ySpeed = tempY;

                        this.prevSwap = b;
                        break;

                }

            }
{
                if (super.x < 0) {
                    super.x = 0;
                    xSpeed = Math.abs(xSpeed);
                } else if (super.x > width - d - 12) {
                    super.x = width - d - 12;
                    xSpeed = -Math.abs(xSpeed);
                }
                if (super.y < 0) {
                    super.y = 0;
                    ySpeed = Math.abs(ySpeed);
                } else if (super.y > height - d - 75) {
                    super.y = height - d - 75;
                    ySpeed = -Math.abs(ySpeed);
                }
                super.x += xSpeed;
                super.y += ySpeed;
            }
        }
    }

    class MathHelper
    {
        public int randomInt(int min, int max)
        {
            int rand = (int) (Math.random() * max ) + 1;


            int total = rand + min;

            if(total > max) total -= total-max;
            if(total < min) total += min-total;

            return total;
        }
    }
}

class GameMenu extends JComponent
{

    private boolean isStartClicked;
    private Image circleImage;

    public GameMenu()
    {

        try
        {
            this.circleImage = ImageIO.read(getClass().getClassLoader().getResourceAsStream("assets/CircleGame/Menu/Full_Circle_Game_Menu.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;


        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.drawImage(circleImage, 0, 0, BallroomOG.ballroom);
    }

    public void setStartButtonSelected(boolean v)
    {
        this.isStartClicked = true;
    }

    public boolean getStartButtonSelected()
    {
        return isStartClicked;
    }
}

enum GameState
{
    MENU(0), ACTIVE(1), PAUSE(2), RESTART(3), CLOSE(4);

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
