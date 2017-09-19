package io.github.bfox1.SimpleShape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * Created by bfox1 on 8/28/2017.
 */
public class SimpleShape extends JFrame
{

    public static void main(String[] args)
    {
        new SimpleShape();
    }

    public SimpleShape()
    {
        this.setSize(300, 300);
        this.setTitle("A Simple Shape Program");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(new PaintSurface(), BorderLayout.CENTER);

        this.setVisible(true);
    }

    private class PaintSurface extends JComponent
    {

        public void paint(Graphics g)
        {
            Graphics2D g2 = (Graphics2D) g;

            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            Shape s = new Ellipse2D.Float(20,40,250,150);

            g2.setPaint(Color.BLACK);
            g2.draw(s);
        }
    }
}
