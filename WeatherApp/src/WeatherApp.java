import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import javax.swing.plaf.IconUIResource;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class WeatherApp {
    public  static JSONObject getWeatherData(String locationName) {
        JSONArray locationData = getLocationData(locationName);
        JSONObject location = (JSONObject)  locationData.get(0);
//        double latitude = (double) location.get("latitude");
//        double longitude = (double) location.get("longitude");
          double latitude = Double.parseDouble((String) location.get("lat"));
          double longitude = Double.parseDouble((String) location.get("lon"));

          // debug
        System.out.println("Fetched the new Cords");

        System.out.println("Latitude "  + latitude);

        System.out.println("Latitude "  + longitude);

        //

        String urlString  =  "latitude and longitude api open meteo.com";

        try {
//            System.out.println("yes I am called");
            HttpURLConnection conn = fectchURLResponse(urlString);

            if(conn.getResponseCode() != 200) {
                System.out.println("Error : Could Not fetch Api");
                return null;
            }
            else {
                StringBuilder resultJson = new StringBuilder();
                Scanner input = new Scanner(conn.getInputStream());
                while(input.hasNext()) {
                    resultJson.append(input.nextLine());
                }
                input.close();

                conn.disconnect();

                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));

                JSONObject hourly = (JSONObject)  resultJsonObj.get("hourly");

                JSONArray time =  (JSONArray)  hourly.get("time");
                int index = findIndexOfCurrentTime(time);

                JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");

                double temperature = (double) temperatureData.get(index);

                JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
                String weatherCondition = convertWeatherCode((long) weatherCode.get(index));

                JSONArray relativeHumidity = (JSONArray) hourly.get("relative_humidity_2m");

                long humidity = (long) relativeHumidity.get(index);

                JSONArray windSpeedData = (JSONArray)  hourly.get("wind_speed_10m");
                double windSpeed = (double) windSpeedData.get(index);

                JSONObject weatherData = new JSONObject();

                weatherData.put("temperature" , temperature);

                weatherData.put("weatherCondition" , weatherCondition);

                weatherData.put("relativeHumidity" , humidity);

                weatherData.put("windSpeed" , windSpeed);

                return  weatherData;

            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public   static  JSONArray getLocationData(String locationName) {

        String urlString1 = "locationIq Api for city search";
        // String uelString optional ==> open meteo gecoding locationData api // use above for city
        locationName = locationName.replaceAll(" ","+");
        try {
            System.out.println("fetching new things ");
            HttpURLConnection conn = fectchURLResponse(urlString1);
            if(conn.getResponseCode() != 200) {
                System.out.println("Error : Not fetched!");
            }
            else {
                StringBuilder resJson  = new StringBuilder();
                Scanner input  = new Scanner(conn.getInputStream());
                while(input.hasNext()) {
                    resJson.append(input.nextLine());
                }
                input.close();
                conn.disconnect();
                JSONParser parser = new JSONParser();
//                JSONObject resJsonObj = (JSONObject) parser.parse(String.valueOf(resJson));
//                JSONArray locationData = (JSONArray) resJsonObj.get("results");
                JSONArray locationData = (JSONArray) parser.parse(resJson.toString());
                 return locationData;
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static HttpURLConnection fectchURLResponse(String urlString) {
        try {
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection)  url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            return conn;
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private  static  int  findIndexOfCurrentTime(JSONArray time) {
        String currentTime = getCurrentTime();
//        System.out.println("Printing the current time for searching");
//        System.out.println(currentTime);
        for(int i = 0 ; i < time.size() ; i++) {
            String timeStr = (String) time.get(i);
            if(timeStr.equals(currentTime)) {
                return i;
            }
        }

       return -1;
    }
    private    static  String getCurrentTime(){
        LocalDateTime currenDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        String formattedDate = currenDateTime.format(formatter);
        return  formattedDate;
    }
    private static  String convertWeatherCode(long  weatherCode) {
        String weatherCondition = "";
        if(weatherCode == 0L) {
            weatherCondition = "Clear";
        }
        else if(weatherCode <= 3L && weatherCode > 0L) {
            weatherCondition = "Cloudy";
        }
        else if((weatherCode >= 51L && weatherCode <= 67L) || (weatherCode >= 80L && weatherCode <= 99L)) {
            weatherCondition = "Rain";
        }
        else if(weatherCode >= 71L && weatherCode <= 77L){
            weatherCondition = "Snow";
        }
        return weatherCondition;
    }
}
