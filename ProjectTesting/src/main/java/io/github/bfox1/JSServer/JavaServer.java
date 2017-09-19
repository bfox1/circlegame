package io.github.bfox1.JSServer;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

/**
 * Created by bfox1 on 9/14/2017.
 */
public class JavaServer
{

    public static void main(String args[]) throws Exception {
        Server server = new Server(8080);
        WebSocketHandler handler = new WebSocketHandler()
        {
            @Override
            public void configure(WebSocketServletFactory webSocketServletFactory)
            {
                webSocketServletFactory.register(JsWebSocketHandler.class);
            }
        };

        server.setHandler(handler);
        server.start();
        server.join();
    }
}
