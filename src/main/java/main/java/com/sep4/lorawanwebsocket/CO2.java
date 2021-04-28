package main.java.com.sep4.lorawanwebsocket;

import java.sql.Date;
import java.sql.Timestamp;

public class CO2
{
    int id;
    float co2level;
    Date date;
    Timestamp time;
    int gymId;

    public CO2(float co2level, Date date, Timestamp time)
    {
        this.co2level = co2level;
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

    public float getCo2level()
    {
        return co2level;
    }

    public void setCo2level(float co2level)
    {
        this.co2level = co2level;
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
        return "CO2{" +
                "id=" + id +
                ", co2level=" + co2level +
                ", date=" + date.toString() +
                ", time=" + time.toString() +
                ", gymId=" + gymId +
                '}';
    }
}
