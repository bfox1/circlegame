package io.github.bfox1.client.BallroomGame.frames;

import io.github.bfox1.BallRoomGameOG.BallroomOG;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

/**
 * Created by bfox1 on 9/6/2017.
 * This is the Main Menu for the Circle Game Deluxe
 */
public class GameMenu extends JComponent
{

    private boolean isStartClicked;
    private Image circleImage;

    /**
     * This is the Contructor
     */
    public GameMenu()
    {

        /*
        Here we are Loading the Main Menu Image from our Assets.
         */
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
