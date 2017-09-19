package io.github.bfox1.helloworld;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by bfox1 on 8/28/2017.
 */
public class HelloWorld extends JFrame
{

    public static void main(String[] args)
    {
        new HelloWorld();
    }

    public HelloWorld()
    {

        this.setSize(300, 300);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("Hello World");

        ClickListener listener = new ClickListener();

        //This is to add the panel//
        HelloPanel panel = new HelloPanel();
        this.add(panel);

        JButton button = new JButton("Click me");
        button.addActionListener(listener);
        this.add(button);

        //Adding a Simple Label//
        JLabel label = new JLabel("Hello World");
        panel.add(label);

        this.setVisible(true);

        /*To place Frame on top left corner
        frame.setLocation(0.0);
         */
        this.setLocationRelativeTo(null);
    }

    public class HelloPanel extends JPanel
    {
        public HelloPanel()
        {

        }
    }

    public class ClickListener implements ActionListener
    {

        public void actionPerformed(ActionEvent e)
        {
            System.out.println("Ive been clicked.");
        }
    }
}
