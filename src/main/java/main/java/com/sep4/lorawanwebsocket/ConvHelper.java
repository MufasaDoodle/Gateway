package main.java.com.sep4.lorawanwebsocket;

public class ConvHelper
{
    public static String convertHexToDecimal(String hex){
        int decimal = Integer.parseInt(hex, 16);
        String string = Integer.toString(decimal);
        return string;
    }
}
