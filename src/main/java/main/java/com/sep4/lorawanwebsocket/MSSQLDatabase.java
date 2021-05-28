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
        //connectionUrl = "jdbc:sqlserver://localhost;databaseName=SEP4DB;integratedSecurity=true;"; use this if you want to test locally
        connectionUrl = "jdbc:sqlserver://sep4db.ckcr1bq2ybz9.us-east-1.rds.amazonaws.com:1433;databaseName=SEP4DB;"; //AWS web database server
    }

    public static synchronized MSSQLDatabase getInstance() throws SQLException{
        if(instance == null){
            instance = new MSSQLDatabase();
        }

        return instance;
    }

    private Connection getConnection() throws SQLException{
        return DriverManager.getConnection(connectionUrl, "admin", "sep4db2020");
    }

    void insertMeasurement(Measurement measurement){

        CallableStatement cstmt;
        System.out.println(measurement.toString()); //for debugging purposes TODO remove

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

    // This method returns the states of the AC, humidifier, dehumidifer and window from the database as hex
    String getStatesHEX(){
        CallableStatement cstmt;

        try{
            //AC
            cstmt = getConnection().prepareCall("{call dbo.spAC_GetByGymID(?)}");

            cstmt.setInt("Gym_ID", 1);

            ResultSet rs = cstmt.executeQuery();

            //in case an error happens, we start with a -1
            int acStateInt = -1;

            while(rs.next()){
                acStateInt = rs.getInt("state");
            }

            boolean acState;

            //java can't case an int to a boolean so we have to do this everytime we want to convert
            if(acStateInt == 0){
                acState = false;
            }
            else {
                acState = true;
            }

            //DEHUM
            cstmt = getConnection().prepareCall("{call dbo.spDehumidifier_GetByGymID(?)}");

            cstmt.setInt("Gym_ID", 1);

            rs = cstmt.executeQuery();

            int deHumStateInt = -1;

            while(rs.next()){
                deHumStateInt = rs.getInt("state");
            }

            boolean deHumState;

            if(deHumStateInt == 0){
                deHumState = false;
            }
            else {
                deHumState = true;
            }


            //HUM
            cstmt = getConnection().prepareCall("{call dbo.spHumidifier_GetByGymID(?)}");

            cstmt.setInt("Gym_ID", 1);

            rs = cstmt.executeQuery();

            int humStateInt = -1;

            while(rs.next()){
                humStateInt = rs.getInt("state");
            }

            boolean humState;

            if(humStateInt == 0){
                humState = false;
            }
            else {
                humState = true;
            }

            //WINDOW
            cstmt = getConnection().prepareCall("{call dbo.spWindow_GetByGymID(?)}");

            cstmt.setInt("Gym_ID", 1);

            rs = cstmt.executeQuery();

            int windowStateInt = -1;

            while(rs.next()){
                windowStateInt = rs.getInt("state");
            }

            boolean windowState;

            if(windowStateInt == 0){
                windowState = false;
            }
            else {
                windowState = true;
            }

            //creating HEX
            String hexString = "";

            if(acState){
                hexString += "01";
            }
            else {
                hexString += "00";
            }

            if(deHumState){
                hexString += "01";
            }
            else {
                hexString += "00";
            }

            if(humState){
                hexString += "01";
            }
            else {
                hexString += "00";
            }

            if(windowState){
                hexString += "01";
            }
            else {
                hexString += "00";
            }

            return hexString;
        }
        catch (SQLException throwables)
        {
            Logger.getLogger(MSSQLDatabase.class.getName()).log(Level.SEVERE, null, throwables);
        }

        return null;
    }
}
