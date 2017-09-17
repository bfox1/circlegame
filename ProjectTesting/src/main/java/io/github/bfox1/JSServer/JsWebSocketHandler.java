package io.github.bfox1.JSServer;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.IOException;

/**
 * Created by bfox1 on 9/14/2017.
 */
@WebSocket
public class JsWebSocketHandler
{

    @OnWebSocketClose
    public void onClose(int statusCode, String reason)
    {
        System.out.println("Close: statusCode=" + statusCode + ", reason= " + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t)
    {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session)
    {
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());

        try
        {
            session.getRemote().sendString("Hello Browser");
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @OnWebSocketMessage
    public void onMessage(String message)
    {
        System.out.println("Message: " + message);
    }
}
