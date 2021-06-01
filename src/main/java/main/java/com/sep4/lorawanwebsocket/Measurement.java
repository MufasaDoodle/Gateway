package main.java.com.sep4.lorawanwebsocket;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class Measurement
{
    private int Gym_Id;
    private Time time;
    private Date date;
    private float Temperature;
    private float Humidity;
    private float CO2Level;

    public Measurement(int gym_Id, Time time, Date date, float temperature, float humidity, float CO2Level)
    {
        Gym_Id = gym_Id;
        this.time = time;
        this.date = date;
        Temperature = temperature;
        Humidity = humidity;
        this.CO2Level = CO2Level;
    }

    public int getGym_Id()
    {
        return Gym_Id;
    }

    public void setGym_Id(int gym_Id)
    {
        Gym_Id = gym_Id;
    }

    public Time getTime()
    {
        return time;
    }

    public void setTime(Time time)
    {
        this.time = time;
    }

    public Date getDate()
    {
        return date;
    }

    public void setDate(Date date)
    {
        this.date = date;
    }

    public float getTemperature()
    {
        return Temperature;
    }

    public void setTemperature(float temperature)
    {
        Temperature = temperature;
    }

    public float getHumidity()
    {
        return Humidity;
    }

    public void setHumidity(float humidity)
    {
        Humidity = humidity;
    }

    public float getCO2Level()
    {
        return CO2Level;
    }

    public void setCO2Level(float CO2Level)
    {
        this.CO2Level = CO2Level;
    }

    @Override
    public String toString()
    {
        return "Measurement{" +
                "Gym_Id=" + Gym_Id +
                ", time=" + time +
                ", date=" + date +
                ", Temperature=" + Temperature +
                ", Humidity=" + Humidity +
                ", CO2Level=" + CO2Level +
                '}';
    }
}
