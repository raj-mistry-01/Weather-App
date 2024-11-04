
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class HistoryButton extends JButton {

    public HistoryButton(WeatherAppGui appGui, JPanel mainPanel, CardLayout cardLayout) {
        super("History");

        addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardLayout.show(mainPanel, "History");
                appGui.displayHistory();
            }
        });
    }
}