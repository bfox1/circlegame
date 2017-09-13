package io.github.bfox1.client.BallroomGame.events;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bfox1 on 9/1/2017.
 */
public class EventRegistry
{
    private final List<ActionEvent> actionEvents;


    public EventRegistry()
    {

        this.actionEvents = new ArrayList<ActionEvent>();
    }

    public synchronized List<ActionEvent> getActionEvents()
    {
        return actionEvents;
    }

    public synchronized void addEvent(ActionEvent event)
    {
        this.actionEvents.add(event);
    }

    public boolean hasEvents()
    {
        return actionEvents.size() != 0;
    }
}
