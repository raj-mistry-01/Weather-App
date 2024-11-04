import org.json.simple.JSONObject;
import javax.swing.*;
import java.awt.*;
import javax.imageio.ImageIO;
import javax.swing.table.DefaultTableModel;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WeatherAppGui extends JFrame {

    public JSONObject weatherData;
    private DefaultTableModel historyTableModel;
    private JPanel mainPanel;
    private CardLayout cardLayout;
    public List<String> searchHistory;
    private JTextArea historyTextArea;

    public WeatherAppGui() {
        super("Weather App");

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(450, 650);
        setLocationRelativeTo(null);
        setResizable(false);

        searchHistory = new ArrayList<>(); 

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createHomePage(), "Home");
        mainPanel.add(createHistoryPage(), "History");

        add(mainPanel);

        revalidate();
        repaint();
    }

    private JPanel createHomePage() {
        JPanel homePanel = new JPanel(null);

        JTextField searchTextField = new JTextField();
        searchTextField.setBounds(15, 15, 351, 45);
        searchTextField.setFont(new Font("Dialog", Font.PLAIN, 24));
        homePanel.add(searchTextField);

        JLabel cityName = new JLabel("City: Ahmedabad");
        cityName.setBounds(15, 72, 320, 30);
        cityName.setFont(new Font("Dialog", Font.BOLD, 24));
        homePanel.add(cityName);

        JLabel wCImage = new JLabel(loadImage("src/assets/cloudy.png"));
        wCImage.setBounds(0, 125, 450, 217);
        homePanel.add(wCImage);

        JLabel temperatureText = new JLabel("10 C");
        temperatureText.setBounds(0, 330, 450, 54);
        temperatureText.setFont(new Font("Dialog", Font.BOLD, 48));
        temperatureText.setHorizontalAlignment(SwingConstants.CENTER);
        homePanel.add(temperatureText);

        JLabel weatherConditionDesc = new JLabel("Cloudy");
        weatherConditionDesc.setBounds(0, 430, 450, 36);
        weatherConditionDesc.setFont(new Font("Arial", Font.PLAIN, 32));
        weatherConditionDesc.setHorizontalAlignment(SwingConstants.CENTER);
        homePanel.add(weatherConditionDesc);

        JLabel humidityImage = new JLabel(loadImage("src/assets/humidity.png"));
        humidityImage.setBounds(15, 500, 74, 66);
        homePanel.add(humidityImage);

        JLabel humidityText = new JLabel("<html><b>Humidity</b> 100%</html>");
        humidityText.setBounds(90, 510, 85, 55);
        humidityText.setFont(new Font("Dialog", Font.PLAIN, 16));
        homePanel.add(humidityText);

        JLabel windSpeedImage = new JLabel(loadImage("src/assets/windspeed.png"));
        windSpeedImage.setBounds(230, 510, 85, 55);
        homePanel.add(windSpeedImage);

        JLabel windSpeedText = new JLabel("<html><b>High</b> 10km/h</html>");
        windSpeedText.setBounds(330, 510, 85, 55);
        windSpeedText.setFont(new Font("Dialog", Font.PLAIN, 16));
        homePanel.add(windSpeedText);

        JButton searchButton = new JButton(loadImage("src/assets/search.png"));
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setBounds(375, 13, 47, 45);

        searchButton.addActionListener(e -> {
            String userInput = searchTextField.getText();
            if (userInput.trim().isEmpty()) {
                return;
            } else {
                cityName.setText("City: " + userInput);
                weatherData = WeatherApp.getWeatherData(userInput);

                // Update search history and refresh history display
                searchHistory.add(userInput);
                displayHistory();

                String weatherCondition = (String) weatherData.get("weatherCondition");
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

                double temperature = (double) weatherData.get("temperature");
                temperatureText.setText(temperature + " C");

                long humidity = (long) weatherData.get("relativeHumidity");
                humidityText.setText("<html><b>Humidity</b> " + humidity + "%</html>");

                double windSpeed = (double) weatherData.get("windSpeed");
                windSpeedText.setText("<html><b>WindSpeed</b> " + windSpeed + "km/h</html>");
            }
        });
        homePanel.add(searchButton);

        HistoryButton historyButton = new HistoryButton(this, mainPanel, cardLayout);
        historyButton.setBounds(30, 580, 100, 30);
        add(historyButton);

        return homePanel;
    }
private JPanel createHistoryPage() {
    JPanel historyPanel = new JPanel(new BorderLayout());

    JLabel historyLabel = new JLabel("Weather History", SwingConstants.CENTER);
    historyLabel.setFont(new Font("Dialog", Font.BOLD, 24));
    historyPanel.add(historyLabel, BorderLayout.NORTH);

    String[] columnNames = {"City", "Temperature (°C)", "Humidity (%)", "Condition", "Wind Speed (km/h)"};

    historyTableModel = new DefaultTableModel(columnNames, 0);
    JTable historyTable = new JTable(historyTableModel);
    historyTable.setFillsViewportHeight(true);

    JScrollPane scrollPane = new JScrollPane(historyTable);
    historyPanel.add(scrollPane, BorderLayout.CENTER);

    JButton backButton = new JButton("Back");
    backButton.addActionListener(e -> cardLayout.show(mainPanel, "Home"));
    backButton.setBounds(90,580,100,30);
    JPanel buttonPanel = new JPanel();
    buttonPanel.add(backButton);
    historyPanel.add(buttonPanel, BorderLayout.SOUTH);

    return historyPanel;
}

public void displayHistory() {
    if (weatherData != null && !searchHistory.isEmpty()) {
        String city = searchHistory.get(searchHistory.size() - 1);
        double temperature = (double) weatherData.get("temperature");
        long humidity = (long) weatherData.get("relativeHumidity");
        String condition = (String) weatherData.get("weatherCondition");
        double windSpeed = (double) weatherData.get("windSpeed");

        int rowCount = historyTableModel.getRowCount();
        if (rowCount > 0) {
            Object lastCity = historyTableModel.getValueAt(rowCount - 1, 0);
            Object lastTemp = historyTableModel.getValueAt(rowCount - 1, 1);
            Object lastHumidity = historyTableModel.getValueAt(rowCount - 1, 2);
            Object lastCondition = historyTableModel.getValueAt(rowCount - 1, 3);
            Object lastWindSpeed = historyTableModel.getValueAt(rowCount - 1, 4);

            if (lastCity.equals(city) && lastTemp.equals(temperature) &&
                    lastHumidity.equals(humidity) && lastCondition.equals(condition) &&
                    lastWindSpeed.equals(windSpeed)) {
                return;
            }
        }

        historyTableModel.addRow(new Object[]{city, temperature, humidity, condition, windSpeed});
    }
}

    public void addWeatherDataToHistory(String city, double temperature, long humidity, String condition, double windSpeed) {
        if (historyTableModel != null) {
            historyTableModel.addRow(new Object[]{city, temperature, humidity, condition, windSpeed});
        }
    }

    private ImageIcon loadImage(String srcPath) {
        try {
            BufferedImage image = ImageIO.read(new File(srcPath));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
