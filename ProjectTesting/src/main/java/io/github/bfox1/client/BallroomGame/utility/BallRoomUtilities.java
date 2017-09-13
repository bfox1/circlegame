package io.github.bfox1.client.BallroomGame.utility;

import io.github.bfox1.client.BallroomGame.Ballroom;

import javax.sound.sampled.*;
import java.io.IOException;

/**
 * Created by bfox1 on 9/1/2017.
 */
public class BallRoomUtilities
{

    public static int randomInt(int min, int max)
    {
        int rand = (int) (Math.random() * max ) + 1;


        int total = rand + min;

        if(total > max) total -= total-max;
        if(total < min) total += min-total;

        return total;
    }

    public static void playSound(final String name)
    {
        new Thread()
        {

            public void run()
            {
                executeSound(name);
            }
        }.start();
    }


    private static final void executeSound(String name)
    {
        AudioInputStream stream = null;
        SourceDataLine line  = null;
        try
        {

            stream = AudioSystem.getAudioInputStream(Ballroom.class.getClassLoader().getResourceAsStream("assets/CircleGame/sounds/effects/" + name + ".wav"));


            AudioFormat format = stream.getFormat();
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            if(!AudioSystem.isLineSupported(info))
            {
                AudioFormat pcm = new AudioFormat(format.getSampleRate(), 16, format.getChannels(), true, false);
                stream = AudioSystem.getAudioInputStream(pcm, stream);
                format = stream.getFormat();
                info = new DataLine.Info(SourceDataLine.class, format);
            }
            line = (SourceDataLine)AudioSystem.getLine(info);
            line.open(format);

            int framsize = format.getFrameSize();
            byte[] buffer = new byte[4*1024*framsize];
            int numbytes = 0;

            boolean started = false;
            for(;;)
            {
                int bytesread = stream.read(buffer, numbytes, buffer.length - numbytes);

                if(bytesread == -1)
                    break;
                numbytes += bytesread;

                if(!started)
                {
                    line.start();
                    started = true;
                }

                int bytesToWrite = (numbytes/framsize) * framsize;

                line.write(buffer, 0, bytesToWrite);

                int remaining = numbytes - bytesToWrite;
                if(remaining > 0)
                    System.arraycopy(buffer, bytesToWrite, buffer, 0, remaining);
                numbytes = remaining;


            }

            line.drain();
           // Clip clip = AudioSystem.getClip();
            //clip.open(stream);
           // clip.start();
            //clip.close();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } finally {
            if(line != null)
            {
                line.close();
            }
            if(stream != null)
            {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
