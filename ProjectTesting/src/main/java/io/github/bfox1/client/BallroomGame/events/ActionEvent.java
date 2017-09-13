package io.github.bfox1.client.BallroomGame.events;

import io.github.bfox1.client.BallroomGame.Ballroom;

import java.awt.*;

/**
 * Created by bfox1 on 9/1/2017.
 * This is to handle All Events to occur throughout the gameplay.
 */
public abstract class ActionEvent
{
    private final Ballroom ballroom;
    public ActionEvent(Ballroom ballroom)
    {
        this.ballroom = ballroom;
    }

    /**
     * This is to perform any action to which the event is being called.
     * @param graphics2D
     */
    public abstract void performAction(Graphics2D graphics2D);

    /**
     * To get a particular object of any Object from the event.
     * @return
     */
    public abstract Object getObject();
}
