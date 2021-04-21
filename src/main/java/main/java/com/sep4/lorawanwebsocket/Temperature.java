package main.java.com.sep4.lorawanwebsocket;

import java.sql.Date;
import java.sql.Timestamp;

public class Temperature
{
    int id;
    float temp;
    Date date;
    Timestamp time;
    int gymId;

    public Temperature(float temp, Date date, Timestamp time)
    {
        this.temp = temp;
        this.date = date;
        this.time = time;
        gymId = 0;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public float getTemp()
    {
        return temp;
    }

    public void setTemp(float temp)
    {
        this.temp = temp;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public Timestamp getTime()
    {
        return time;
    }

    public void setTime(Timestamp time)
    {
        this.time = time;
    }

    public int getGymId()
    {
        return gymId;
    }

    public void setGymId(int gymId)
    {
        this.gymId = gymId;
    }
}
