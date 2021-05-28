package main.java.com.sep4.lorawanwebsocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.concurrent.CountDownLatch;

public class Boot
{
    static CountDownLatch latch;

    public static void main(String[] args) throws URISyntaxException
    {
        latch = new CountDownLatch(5);
        try
        {
            SocketClient client = new SocketClient("wss://iotnet.cibicom.dk/app?token=vnoTugAAABFpb3RuZXQuY2liaWNvbS5ka0q3npoy3giwU6BP_fKjb5U=");
            //Measurement test = new Measurement(1, new Time(1324234L), new Date(1324234L), 20f, 46f, 690f);
            //client.testThingy(test);
            //MSSQLDatabase.getInstance().insertMeasurement(test);

            //MSSQLDatabase.getInstance().getStatesHEX();

            //TODO remove above comments

            latch.await();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}

