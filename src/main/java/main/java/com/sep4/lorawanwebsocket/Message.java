package main.java.com.sep4.lorawanwebsocket;

public class Message
{
    String cmd;
    String EUI;
    int ts;
    boolean ack;
    int fcnt;
    int port;
    String encdata;
    String data;

    public Message(String cmd, String EUI, int ts, boolean ack, int fcnt, int port, String encdata, String data)
    {
        this.cmd = cmd;
        this.EUI = EUI;
        this.ts = ts;
        this.ack = ack;
        this.fcnt = fcnt;
        this.port = port;
        this.encdata = encdata;
        this.data = data;
    }

    public String getCmd()
    {
        return cmd;
    }

    public void setCmd(String cmd)
    {
        this.cmd = cmd;
    }

    public String getEUI()
    {
        return EUI;
    }

    public void setEUI(String EUI)
    {
        this.EUI = EUI;
    }

    public int getTs()
    {
        return ts;
    }

    public void setTs(int ts)
    {
        this.ts = ts;
    }

    public boolean isAck()
    {
        return ack;
    }

    public void setAck(boolean ack)
    {
        this.ack = ack;
    }

    public int getFcnt()
    {
        return fcnt;
    }

    public void setFcnt(int fcnt)
    {
        this.fcnt = fcnt;
    }

    public int getPort()
    {
        return port;
    }

    public void setPort(int port)
    {
        this.port = port;
    }

    public String getEncdata()
    {
        return encdata;
    }

    public void setEncdata(String encdata)
    {
        this.encdata = encdata;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}
