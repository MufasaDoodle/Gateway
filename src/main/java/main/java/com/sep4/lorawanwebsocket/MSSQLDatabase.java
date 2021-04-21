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
        connectionUrl = "jdbc:sqlserver://localhost>:<port>;databaseName=<name>;user=<user>;password=<password>";
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
