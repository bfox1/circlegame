package io.github.bfox1.client.BallroomGame.circles;

import io.github.bfox1.client.BallroomGame.Ballroom;
import io.github.bfox1.client.BallroomGame.utility.BallRoomUtilities;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;

/**
 * Created by bfox1 on 9/1/2017.
 */
public class Circle extends Ellipse2D.Float
{

    protected static class RedCircle extends Circle
    {

        /**
         * Constructor determines what type of ball its going to be. It has a chance to be either Blue, Red and Green.
         *
         * @param d
         * @param balls
         */
        public RedCircle(int d, HashMap<Integer, Circle> balls)
        {
            super(d, balls, CircleColor.RED);
        }
    }

    protected static class BlueCircle extends Circle
    {

        /**
         * Constructor determines what type of ball its going to be. It has a chance to be either Blue, Red and Green.
         *
         * @param d
         * @param balls
         */
        public BlueCircle(int d, HashMap<Integer, Circle> balls) {
            super(d, balls, CircleColor.BLUE);
        }

        @Override
        public void doAction(Graphics2D graphics2D)
        {
            Ballroom.instance.getTimer().addInterval(10);
            this.setDeath(true);

        }

        public void adjustColor(Graphics2D g2)
        {
            g2.setColor(Color.BLUE);
            this.move();
            g2.fill(this);
            g2.setColor(Color.RED);
        }
    }
    private int xSpeed, ySpeed;

    private int d;

    private int z;

    private int i = 0;

    private int width = Ballroom.WIDTH;
    private int height = Ballroom.HEIGHT;

    private final CircleColor circle;

    private Circle prevSwap;

    private boolean death;

    private HashMap<Integer, Circle> balls;

    /**
     * Constructor determines what type of ball its going to be. It has a chance to be either Blue, Red and Green.
     * @param d
     * @param balls
     */
    public Circle(int d, HashMap<Integer, Circle> balls, CircleColor color)
    {
        super((int)(Math.random() * (Ballroom.WIDTH - d) +1), (int)(Math.random() *(Ballroom.HEIGHT - d) + 1), d, d);
        this.circle = color;
        this.balls = balls;
        this.d = d;

        this.death = false;
        this.xSpeed = (int)(Math.random() * 5 +1);
        this.ySpeed = (int)(Math.random() * 5 + 1);

        int b = BallRoomUtilities.randomInt(0,1);

        if(b == 0)
        {
            z = 1;
        }
        else
        {
            z = -1;
        }


        this.xSpeed += 2;
        this.ySpeed += 2;

    }

    /**
     * Actual movements take place here.
     */
    public void move()
    {
        if(i == 4)
        {
            if (d == 50) {
                this.z = -1;
            }
            if (d == 20) {
                this.z = 1;
            }

            this.d += z;

            super.height = d;
            super.width = d;
            i = 0;
        }
        i++;
        //Detect collision with other balls
        Rectangle2D r = new Rectangle2D.Float(super.x, super.y, d, d);

        /*
        Special Collision Check, checking if previous Hit circle is stuck to current Circle.
        If so, will perform an instant speed switch.
         */
        if(prevSwap != null)
        {
            if(this.prevSwap.intersects(r))
            {
                if(this.xSpeed > 0) {
                    this.xSpeed = -xSpeed;
                    this.x -= 3;
                }
                else if(this.xSpeed < 0) {
                    this.xSpeed = xSpeed*-1;
                    this.x += 3;
                }
                if(this.ySpeed > 0 ) {
                    this.ySpeed = -ySpeed;
                    this.y -= 3;
                }
                else if(this.ySpeed < 0 ) {
                    this.ySpeed = ySpeed*-1;
                    this.y += 3;
                }

                if(this.prevSwap.xSpeed > 0) this.prevSwap.xSpeed = -prevSwap.xSpeed;
                else if(this.prevSwap.xSpeed < 0) this.prevSwap.xSpeed = prevSwap.xSpeed*-1;
                if(this.prevSwap.ySpeed > 0 ) this.prevSwap.ySpeed = -prevSwap.ySpeed;
                else if(this.prevSwap.ySpeed < 0 ) this.prevSwap.ySpeed = prevSwap.ySpeed*-1;

                this.prevSwap = null;
            }
        }
        else
        {
            this.prevSwap = null;
        }
        for(Circle b : balls.values())
        {
            if(b != this && b.intersects(r))
            {
                    int tempX = xSpeed;
                    int tempY = ySpeed;
                    xSpeed = b.xSpeed;
                    ySpeed = b.ySpeed;
                    b.xSpeed = tempX;
                    b.ySpeed = tempY;

                    BallRoomUtilities.playSound("blop");
                    this.prevSwap = b;
                    break;
            }
        }

            if (super.x < 0)
            {
                super.x = 0;
                xSpeed = Math.abs(xSpeed);
                BallRoomUtilities.playSound("blop");
            } else if (super.x > width - d - 12)
            {
                super.x = width - d - 12;
                xSpeed = -Math.abs(xSpeed);
                BallRoomUtilities.playSound("blop");
            }
            if (super.y < 0)
            {
                super.y = 0;
                ySpeed = Math.abs(ySpeed);
                BallRoomUtilities.playSound("blop");
            } else if (super.y > height - d - 75)
            {
                super.y = height - d - 75;
                ySpeed = -Math.abs(ySpeed);
                BallRoomUtilities.playSound("blop");
            }
            super.x += xSpeed;
            super.y += ySpeed;
    }


    public void doAction(Graphics2D graphics2D)
    {

    }

    public boolean isDeath() {
        return death;
    }

    public void adjustColor(Graphics2D g2)
    {

    }

    public void setDeath(boolean death) {
        this.death = death;
    }

    public static Circle generateCircle(int diameter, HashMap<Integer, Circle> circles)
    {
        int b = BallRoomUtilities.randomInt(5, 20);
        if(b == 15)
        {
            return new BlueCircle(diameter, circles);
        }

        int g = BallRoomUtilities.randomInt(5, 50);
        if(g == 29)
        {
            //return new GreenCircle(diameter, circles);
        }

        return new RedCircle(diameter, circles);
    }

    public CircleColor getCircle() {
        return circle;
    }
}
