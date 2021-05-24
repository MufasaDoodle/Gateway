package main.java.com.sep4.lorawanwebsocket;

public class DownLinkAch
{
    private String cmd;
    private String EUI;
    private String success;
    private String error;
    private String data;

    public DownLinkAch(String cmd, String EUI, String error, String data)
    {
        this.cmd = cmd;
        this.EUI = EUI;
        this.error = error;
        this.data = data;
    }



    public DownLinkAch(String cmd, String EUI, String success, String error, String data)
    {
        this.cmd = cmd;
        this.EUI = EUI;
        this.success = success;
        this.error = error;
        this.data = data;
    }
}
