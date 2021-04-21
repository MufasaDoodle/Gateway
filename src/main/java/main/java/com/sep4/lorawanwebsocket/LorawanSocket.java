package main.java.com.sep4.lorawanwebsocket;

import com.google.gson.Gson;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class LorawanSocket extends WebSocketClient
{
    Logger logger;

    public LorawanSocket(URI serverUri)
    {
        super(serverUri);
        logger = LoggerFactory.getLogger(LorawanSocket.class);
        logger.info("Connecting to " + serverUri.toString());
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake)
    {
        logger.info("Connection established...");
    }

    @Override
    public void onMessage(String message){
        logger.info("Received message: \n" + message);
    }

    @Override
    public void onClose(int i, String s, boolean b)
    {
        logger.info("Closed connection");
    }

    @Override
    public void onError(Exception e)
    {
        logger.error("Error in connection: ");
        e.printStackTrace();
    }
}
