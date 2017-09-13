package io.github.bfox1.client.BallroomGame.frames;

import io.github.bfox1.client.BallroomGame.Ballroom;
import io.github.bfox1.client.BallroomGame.circles.Circle;
import io.github.bfox1.client.BallroomGame.circles.CircleColor;
import io.github.bfox1.client.BallroomGame.events.ActionEvent;
import io.github.bfox1.client.BallroomGame.events.BallActionEvent;
import io.github.bfox1.client.BallroomGame.utility.BallRoomUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bfox1 on 9/1/2017.
 */
public class PaintSurface extends JComponent
{
    public HashMap<Integer, Circle> balls = new HashMap<Integer, Circle>();

    private final Ballroom room;

    Rectangle2D r = new Rectangle2D.Float(0,0, 0, 0);

    public PaintSurface(final Ballroom room)
    {
        this.room = room;
        //Creating the Balls//
        for(int i = 0; i < 10; i++)
        {
            balls.put(i, Circle.generateCircle(BallRoomUtilities.randomInt(20,50), balls));
        }

        //Adding our Listener for mouse Clicks
        this.addMouseListener(new MouseListener()
        {
            //UNUSED//
            public void mouseClicked(MouseEvent e) {}

            public void mousePressed(MouseEvent e)
            {
                int clickedX = e.getX();
                int clickedY = e.getY();
                Rectangle2D rectangle2D = new Rectangle2D.Float(clickedX, clickedY, 5, 5);

                for(Map.Entry entry : balls.entrySet())
                {
                    int index = (Integer)entry.getKey();
                    Circle circle = (Circle)entry.getValue();

                    if(circle.intersects(rectangle2D))
                    {
                        room.getEventRegistry().addEvent(new BallActionEvent(circle, room, index));
                    }

                }
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


    /**
     * This method gets Invoked everytime JFrame.repaint() gets invoked.
     * This handles all final calculations of the balls and determines if they should get destroyed and moved.
     * @param g
     */
    public void paint(Graphics g)
    {

        Graphics2D g2 = (Graphics2D) g;


        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.RED);

        g2.draw(r);

        for(ActionEvent event : room.getEventRegistry().getActionEvents())
        {
            BallActionEvent ballActionEvent = ((BallActionEvent)event);
            int index = ballActionEvent.getHasmapIndex();
            ballActionEvent.performAction(g2);
            balls.remove(index);
            balls.put(index, Circle.generateCircle(BallRoomUtilities.randomInt(20, 50), balls));

            room.points++;
        }
        room.getEventRegistry().getActionEvents().clear();



        for(Circle ball : balls.values())
        {

            if(ball.getCircle() == CircleColor.BLUE)
            {
                ball.adjustColor(g2);
            }
            else
            {
                ball.move();

                g2.fill(ball);
            }
        }

        //If ball intersects player, will get removed, and a new one spawns.
        if(r.getX() != 0 && r.getY() != 0)
        {

            room.misses++;
        }
        r = new Rectangle2D.Float(0,0,0,0);
    }
}
