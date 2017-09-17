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
        public void doActionWallCollision(Graphics2D graphics2D)
        {
            Ballroom.instance.getTimer().addInterval(10);
            this.setDeath(true);

        }

        public void moveSpecialWallCollision(Graphics2D g2)
        {
            g2.setColor(Color.BLUE);
            this.move();
            g2.fill(this);
            g2.setColor(Color.RED);
        }

        public void moveSpecialCircleCollision(Graphics2D g2, Circle circle)
        {
            g2.setColor(Color.BLUE);
            this.moveCircleCollision();
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

    public void moveCircleCollision()
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
        Rectangle2D r = new Rectangle2D.Float(super.x, super.y, d, d);
        for(Circle circle : balls.values())
        {
            if (circle != this && circle.intersects(r))
            {
                int tempX = xSpeed;
                int tempY = ySpeed;
                xSpeed = circle.xSpeed;
                ySpeed = circle.ySpeed;
                circle.xSpeed = tempX;
                circle.ySpeed = tempY;

                BallRoomUtilities.playSound("blop");
                super.x += xSpeed;
                super.y += ySpeed;

                circle.x += tempX;
                circle.y += tempY;
                break;
            }
        }
    }


    public void doActionWallCollision(Graphics2D graphics2D)
    {

    }

    public void doActionCircleCollision(Graphics2D g2)
    {}

    public boolean isDeath() {
        return death;
    }

    public void moveSpecialWallCollision(Graphics2D g2)
    {

    }

    public void moveSpecialCircleCollision(Graphics2D g2, Circle circle)
    {}

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
