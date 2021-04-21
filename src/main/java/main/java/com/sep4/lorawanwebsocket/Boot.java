package main.java.com.sep4.lorawanwebsocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

public class Boot
{
    static CountDownLatch latch;

    public static void main(String[] args) throws URISyntaxException
    {
        latch = new CountDownLatch(5);
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

