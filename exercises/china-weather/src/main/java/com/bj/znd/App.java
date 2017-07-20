package com.bj.znd;
import com.bj.znd.weather.*;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        //System.out.println( "Hello World!" );
		String zipcode = "北京";
        try {
		  zipcode = args[0];
        } catch( Exception e ) {}

		
		WeatherWebService wws = new WeatherWebService();  
        WeatherWebServiceSoap wwsp = wws.getWeatherWebServiceSoap();  
          
        ArrayOfString aos = wwsp.getWeatherbyCityName(zipcode);  
          
        for (String s : aos.getString()) {     
            System.out.println(s);  
        }  
    }
}
