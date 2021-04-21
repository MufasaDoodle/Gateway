package main.java.com.sep4.lorawanwebsocket;

import org.glassfish.tyrus.client.ClientManager;

import javax.websocket.ClientEndpoint;
import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

public class Boot
{
    static CountDownLatch latch;

    public static void main(String[] args) throws URISyntaxException
    {
        latch = new CountDownLatch(1);

        try
        {
            LorawanSocket client = new LorawanSocket(new URI("ws://IP:XXXX"));
            client.connect();
            latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
