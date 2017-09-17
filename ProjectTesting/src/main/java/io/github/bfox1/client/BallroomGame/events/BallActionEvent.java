package io.github.bfox1.client.BallroomGame.events;

import io.github.bfox1.client.BallroomGame.Ballroom;
import io.github.bfox1.client.BallroomGame.circles.Circle;

import java.awt.*;

/**
 * Created by bfox1 on 9/1/2017.
 */
public class BallActionEvent extends ActionEvent
{

    private final Circle circle;

    private final int hasmapIndex;


    public BallActionEvent(Circle circle, Ballroom ballroom, int index)
    {
        super(ballroom);
        this.circle = circle;
        this.hasmapIndex = index;
    }

    public Circle getCircle()
    {
        return getObject();
    }

    public void performAction(Graphics2D graphics2D)
    {
        circle.doActionWallCollision(graphics2D);
    }

    @Override
    public final Circle getObject() {
        return null;
    }

    public int getHasmapIndex() {
        return hasmapIndex;
    }
}
