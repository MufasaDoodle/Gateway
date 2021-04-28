package main.java.com.sep4.lorawanwebsocket;

import java.sql.Date;
import java.sql.Timestamp;

public class Humidity
{
    int id;
    float percentage;
    Date date;
    Timestamp time;
    int gymId;

    public Humidity(float percentage, Date date, Timestamp time)
    {
        this.percentage = percentage;
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

    public float getPercentage()
    {
        return percentage;
    }

    public void setPercentage(float percentage)
    {
        this.percentage = percentage;
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

    @Override
    public String toString()
    {
        return "Humidity{" +
                "id=" + id +
                ", percentage=" + percentage +
                ", date=" + date.toString() +
                ", time=" + time.toString() +
                ", gymId=" + gymId +
                '}';
    }
}
