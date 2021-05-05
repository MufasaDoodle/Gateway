package main.java.com.sep4.lorawanwebsocket;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MSSQLDatabase
{
    String connectionUrl;

    private static MSSQLDatabase instance;

    private MSSQLDatabase() throws SQLException{
        DriverManager.registerDriver(new com.microsoft.sqlserver.jdbc.SQLServerDriver());
        //connectionUrl = "jdbc:sqlserver://localhost;databaseName=SEP4DB;integratedSecurity=true;";
        connectionUrl = "jdbc:sqlserver://sep4db.ckcr1bq2ybz9.us-east-1.rds.amazonaws.com";
    }

    public static synchronized MSSQLDatabase getInstance() throws SQLException{
        if(instance == null){
            instance = new MSSQLDatabase();
        }


        return instance;
    }

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(connectionUrl);
    }

    void insertMeasurement(Measurement measurement){
        CallableStatement cstmt;
        System.out.println(measurement.toString());
        System.out.println(new Timestamp(1620145112));
        System.out.println(new Time(1620145112));
        System.out.println(new Date(1620145112));

        try{
            cstmt = getConnection().prepareCall("{call dbo.spInsert_Measurement(?,?,?,?,?,?)}");

            cstmt.setFloat("Temperature", measurement.getTemperature());
            cstmt.setFloat("Humidity", measurement.getHumidity());
            cstmt.setFloat("CO2Level", measurement.getCO2Level());
            cstmt.setInt("Gym_ID", measurement.getGym_Id());
            cstmt.setDate("Date", measurement.getDate());
            cstmt.setTime("Time", measurement.getTime());

            cstmt.execute();
        }
        catch (SQLException throwables)
        {
            Logger.getLogger(MSSQLDatabase.class.getName()).log(Level.SEVERE, null, throwables);
        }
    }

    void insertTemp(Temperature temp){

        CallableStatement cstmt;

        try{
            cstmt = getConnection().prepareCall("{call dbo.spInsert_Temperature}");

            cstmt.setFloat("Temperature", temp.getTemp());
            cstmt.setDate("Date", temp.getDate());
            cstmt.setTimestamp("Time", temp.getTime());
            cstmt.setInt("Gym_ID", temp.getGymId());

            cstmt.execute();
        }
        catch (SQLException throwables)
        {
            Logger.getLogger(MSSQLDatabase.class.getName()).log(Level.SEVERE, null, throwables);
        }
    }
}
