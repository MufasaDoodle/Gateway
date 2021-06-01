package main.java.com.sep4.lorawanwebsocket;

import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.WebSocket;
import java.nio.ByteBuffer;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

public class SocketClient implements WebSocket.Listener
{
    private WebSocket server = null;

    // Send down-link message to device
    // Must be in Json format according to https://github.com/ihavn/IoT_Semester_project/blob/master/LORA_NETWORK_SERVER.md
    public void sendDownLink(String jsonTelegram) {
        server.sendText(jsonTelegram,true);
    }

    // E.g. url: "wss://iotnet.teracom.dk/app?token=??????????????????????????????????????????????="
    // Substitute ????????????????? with the token you have been given
    public SocketClient(String url) {
        HttpClient client = HttpClient.newHttpClient();
        CompletableFuture<WebSocket> ws = client.newWebSocketBuilder()
                .buildAsync(URI.create(url), this);

        server = ws.join();
    }

    //onOpen()
    public void onOpen(WebSocket webSocket) {
        // This WebSocket will invoke onText, onBinary, onPing, onPong or onClose methods on the associated listener (i.e. receive methods) up to n more times
        webSocket.request(1);
        System.out.println("WebSocket Listener has been opened for requests.");
    }

    //onError()
    public void onError(WebSocket webSocket, Throwable error) {
        System.out.println("A " + error.getCause() + " exception was thrown.");
        System.out.println("Message: " + error.getLocalizedMessage());
        webSocket.abort();
    }

    //onClose()
    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
        System.out.println("WebSocket closed!");
        System.out.println("Status:" + statusCode + " Reason: " + reason);
        return new CompletableFuture().completedFuture("onClose() completed.").thenAccept(System.out::println);
    }

    //onPing()
    public CompletionStage<?> onPing(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Ping: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Ping completed.").thenAccept(System.out::println);
    }

    //onPong()
    public CompletionStage<?> onPong(WebSocket webSocket, ByteBuffer message) {
        webSocket.request(1);
        System.out.println("Pong: Client ---> Server");
        System.out.println(message.asCharBuffer().toString());
        return new CompletableFuture().completedFuture("Pong completed.").thenAccept(System.out::println);
    }

    //onText()
    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last)
    {
        String indented = null;
        try
        {
            indented = (new JSONObject(data.toString())).toString(4);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        Message message = gson.fromJson(indented, Message.class);

        if(message.getCmd().equals("rx")){
            //grabbing the hexvalues of the different measurements
            String humHex = message.getData().substring(0,4);
            String tempHex = message.getData().substring(4,8);
            String co2Hex = message.getData().substring(8,12);

            //Getting the time given by the relay
            Time t = new Time(message.getTs());
            Time t2 = new Time(t.getHours(), t.getMinutes(), 0);
            //using deprecated methods because the other solutions are even worse
            //reason for doing this is that the data team doesn't want seconds or milliseconds

            Date date = new Date(message.getTs());

            //converting the hex values to decimal values
            float temp = Float.parseFloat(ConvHelper.convertHexToDecimal(tempHex));
            float hum = Float.parseFloat(ConvHelper.convertHexToDecimal(humHex));
            float co2 = Float.parseFloat(ConvHelper.convertHexToDecimal(co2Hex));

            Measurement measurement = new Measurement(1,t2,date,temp, hum, co2);

            try
            {
                //insert measurement into the database
                MSSQLDatabase.getInstance().insertMeasurement(measurement);

                //checks all the automation settings and changes states according to the measurement we just received
                handleAutomation(measurement);

                //Will try to transmit the states of the AC, humidifier, dehumidifier and window to the hardware
                sendStates();
            }
            catch (SQLException throwables)
            {
                throwables.printStackTrace();
            }
        }

        webSocket.request(1);
        return new CompletableFuture().completedFuture("onText() completed.").thenAccept(System.out::println);
    }

    private void handleAutomation(Measurement measurement)
    {
        try
        {
            MSSQLDatabase.getInstance().checkTemperature(measurement.getTemperature());
            MSSQLDatabase.getInstance().checkCO2(measurement.getCO2Level());
            MSSQLDatabase.getInstance().checkHumidity(measurement.getHumidity());
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }

    private void sendStates()
    {
        try
        {
            //grabs the states from the database as hexvalues
            String hex = MSSQLDatabase.getInstance().getStatesHEX();

            if(hex == null){
                //something went wrong with getting the states from database, so bail out
                return;
            }

            //initialize a downlinkmessage with the hex string
            DownLinkMessage message = new DownLinkMessage("tx", "0004A30B00219CB5", 2, false, hex);

            //network server wants json
            Gson gson = new Gson();
            String json = gson.toJson(message);

            //DEBUG
            System.out.println(json);

            sendDownLink(json);
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }
}
