package io.github.bfox1.client.BallroomGame.circles;

/**
 * Created by bfox1 on 9/1/2017.
 */
public enum CircleColor
{
    RED(0), BLUE(1), GREEN(2);

    private final int id;

    CircleColor(int id)
    {
        this.id = id;
    }


    public int getID()
    {
        return id;
    }


    public CircleColor getId(int i)
    {
        for(CircleColor circle : CircleColor.values())
        {
            if(circle.getID() == i)
            {
                return circle;
            }
        }
        return RED;
    }
}
