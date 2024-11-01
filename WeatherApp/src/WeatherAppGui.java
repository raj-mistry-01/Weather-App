import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageFilter;
import java.io.*;

public class WeatherAppGui extends JFrame {


    private JSONObject weatherData;
    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450,650);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(null);
        addGuiComponents();
        revalidate();
        repaint();
    }
    public  void addGuiComponents(){
        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15,15,351,45);
        searchTextField.setFont(new Font("Dialog",Font.PLAIN,24));
        add(searchTextField);
        JLabel cityName = new JLabel("City : Ahmedabad");
        cityName.setBounds(15 , 72 , 320 , 30);
        cityName.setFont(new Font("Dialog" , Font.BOLD , 24));
        add(cityName);
        JLabel wCImage = new JLabel(loadImage("src/assets/cloudy.png"));
        wCImage.setBounds(0,125,450,217);
        add(wCImage);
        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0,330,450,54);
        temperatureText.setFont(new Font("Dialog",Font.BOLD,48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        add(temperatureText);
        JLabel weatherConitionDesp = new JLabel("Cloudy");
        weatherConitionDesp.setBounds(0,430,450,36);
        weatherConitionDesp.setFont(new Font("Arial",Font.PLAIN,32));
        weatherConitionDesp.setHorizontalAlignment(SwingConstants.CENTER);
        add(weatherConitionDesp);
        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15,500,74,66);
        add(humidityImage);
        JLabel humdityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humdityText.setBounds(90,510,85,55);
        humdityText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(humdityText);
        JLabel windSpeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windSpeedImage.setBounds(230,510,85,55);
        add(windSpeedImage);
        JLabel windSpeedText = new JLabel("<html><b>High</b> 10km/h</html>");
        windSpeedText.setBounds(330,510,85,55);
        windSpeedText.setFont(new Font("Dialog",Font.PLAIN,16));
        add(windSpeedText);

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375,13,47,45);

        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userInput = searchTextField.getText();

                if(userInput.replaceAll("\\s" ,  "").length() <= 0) {
                    return ;
                }
                else {

                    cityName.setText("City " + userInput);

                    weatherData = WeatherApp.getWeatherData(userInput);

                    System.out.println("Fetched Weather In Gui");

                    System.out.println(weatherData);

                    // gui update

                    String weatherCondition = (String)  weatherData.get("weatherCondition");

                    switch (weatherCondition) {
                        case "Clear":
                            wCImage.setIcon(loadImage("src/assets/clear.png"));
                            break;
                        case "Cloudy":
                            wCImage.setIcon(loadImage("src/assets/cloudy.png"));
                            break;
                        case "Rain":
                            wCImage.setIcon(loadImage("src/assets/rain.png"));
                            break;
                        case "Snow":
                            wCImage.setIcon(loadImage("src/assets/snow.png"));
                            break;
                    }

                    double temperature  = (double) weatherData.get("temperature");

                    temperatureText.setText(temperature + " C");

                    long humidity = (long) weatherData.get("relativeHumidity");

                    humdityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                    double windSpeed = (double) weatherData.get("windSpeed");

                    windSpeedText.setText("<html><b>WindSpeed</b> " + windSpeed + "km/h</html>");


                }
            }
        });
        add(searchButton);
    }
    private ImageIcon loadImage(String srcPath) {
        try {
            BufferedImage image = ImageIO.read(new File(srcPath));
            return new ImageIcon(image);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }
}
