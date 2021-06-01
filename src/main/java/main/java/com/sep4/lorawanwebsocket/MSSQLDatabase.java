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

    public void insertMeasurement(Measurement measurement){

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
    public String getStatesHEX(){
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

    public void checkTemperature(float temperature)
    {

        CallableStatement cstmt;

        try{
            cstmt = getConnection().prepareCall("{call dbo.spAC_GetByGymID(?)}");

            cstmt.setInt("Gym_ID", 1);

            ResultSet rs = cstmt.executeQuery();

            int isOn = 0;
            float target = -1f;

            while(rs.next()){
                isOn = rs.getInt("automation");
                target = rs.getFloat("TargetTemperature");
            }

            //check if automation settings are off and we just bail out
            if(isOn != 1){
                return;
            }

            if(temperature > target){
                System.out.println("Temperature is too high");
                changeACState(true);
            }
            else if(temperature < target){
                System.out.println("Temperature is fine");
                changeACState(false);
            }
        }
        catch (SQLException throwables)
        {
            Logger.getLogger(MSSQLDatabase.class.getName()).log(Level.SEVERE, null, throwables);
        }

    }

    public void checkCO2(float co2Level)
    {
        CallableStatement cstmt;

        try{
            cstmt = getConnection().prepareCall("{call dbo.spWindow_GetByGymID(?)}");

            cstmt.setInt("Gym_ID", 1);

            ResultSet rs = cstmt.executeQuery();

            int isOn = 0;
            float target = -1f;

            while(rs.next()){
                isOn = rs.getInt("automation");
                target = rs.getFloat("TargetCO2Level");
            }

            //check if automation settings are off and we just bail out
            if(isOn != 1){
                return;
            }

            if(co2Level > target){
                System.out.println("CO2 too high");
                changeWindowState(true);
            }
            else if(co2Level < target){
                System.out.println("CO2 is low");
                changeWindowState(false);
            }
        }
        catch (SQLException throwables)
        {
            Logger.getLogger(MSSQLDatabase.class.getName()).log(Level.SEVERE, null, throwables);
        }
    }

    public void checkHumidity(float humidity)
    {
        CallableStatement cstmt;

        try{
            cstmt = getConnection().prepareCall("{call dbo.spHumidifier_GetByGymID(?)}");

            cstmt.setInt("Gym_ID", 1);

            ResultSet rs = cstmt.executeQuery();

            int isOn = 0;
            float target = -1f;

            while(rs.next()){
                isOn = rs.getInt("automation");
                target = rs.getFloat("TargetHumidity");
            }

            //check if automation settings are off and we just bail out
            if(isOn != 1){
                return;
            }

            if(humidity < target){
                System.out.println("Humidity too low");
                changeHumState(true);
            }
            else {
                System.out.println("Humidity is fine");
                changeHumState(false);
            }
        }
        catch (SQLException throwables)
        {
            Logger.getLogger(MSSQLDatabase.class.getName()).log(Level.SEVERE, null, throwables);
        }
    }

    private void changeACState(boolean isOn){
        CallableStatement cstmt;

        try{
            if(isOn){
                System.out.println("Setting AC to on");
                cstmt = getConnection().prepareCall("{call dbo.spTurnOn_AC(?)}");
            }
            else {
                System.out.println("Setting AC to off");
                cstmt = getConnection().prepareCall("{call dbo.spTurnOff_AC(?)}");
            }

            cstmt.setInt("Gym_ID", 1);

            cstmt.execute();
        }
        catch (SQLException throwables)
        {
            Logger.getLogger(MSSQLDatabase.class.getName()).log(Level.SEVERE, null, throwables);
        }
    }

    private void changeHumState(boolean isOn){
        CallableStatement cstmt;

        try{
            if(isOn){
                System.out.println("Setting humidifier to on");
                cstmt = getConnection().prepareCall("{call dbo.spTurnOn_Humidifier(?)}");
            }
            else {
                System.out.println("Setting humidifier to off");
                cstmt = getConnection().prepareCall("{call dbo.spTurnOff_Humidifier(?)}");
            }

            cstmt.setInt("Gym_ID", 1);

            cstmt.execute();
        }
        catch (SQLException throwables)
        {
            Logger.getLogger(MSSQLDatabase.class.getName()).log(Level.SEVERE, null, throwables);
        }
    }

    private void changeWindowState(boolean isOpen){
        CallableStatement cstmt;

        try{
            if(isOpen){
                System.out.println("Setting window to on");
                cstmt = getConnection().prepareCall("{call dbo.spTurnOn_Window(?)}");
            }
            else {
                System.out.println("Setting window to off");
                cstmt = getConnection().prepareCall("{call dbo.spTurnOff_Window(?)}");
            }

            cstmt.setInt("Gym_ID", 1);

            cstmt.execute();
        }
        catch (SQLException throwables)
        {
            Logger.getLogger(MSSQLDatabase.class.getName()).log(Level.SEVERE, null, throwables);
        }
    }
}
