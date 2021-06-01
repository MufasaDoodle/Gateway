package main.java.com.sep4.lorawanwebsocket;

import java.util.concurrent.CountDownLatch;

public class Boot
{
    static CountDownLatch latch;

    public static void main(String[] args)
    {
        latch = new CountDownLatch(5);
        try
        {
            SocketClient client = new SocketClient("wss://iotnet.cibicom.dk/app?token=vnoTugAAABFpb3RuZXQuY2liaWNvbS5ka0q3npoy3giwU6BP_fKjb5U=");

            latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

