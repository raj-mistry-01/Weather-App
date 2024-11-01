import javax.swing.*;

public class AppLuancher {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WeatherAppGui().setVisible(true);
//                System.out.println(WeatherApp.getLocationData("Singapore"));
                System.out.println(WeatherApp.getWeatherData("Ahmedabad"));
            }
        });
    }
}
