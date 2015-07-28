package gui.jobScheduling;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.*;

import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;

import models.jobScheduling.JobSchedule;
import models.jobScheduling.RunningProcess;

public class DisplayController implements Initializable
{
    @FXML protected VBox root;

    @FXML protected Canvas canvas;

    protected Color[] colors = {
            Color.web("#AB4642"), Color.web("#86C1B9"),
            Color.web("#A16946"), Color.web("#A1B56C"),
            Color.web("#BA8BAF"), Color.web("#F7CA88"),
            Color.web("#7CAFC2"), Color.web("#DC9656")
    };

    @FXML protected Label ttatLabel;
    @FXML protected Label atatLabel;
    @FXML protected Label twtLabel;
    @FXML protected Label awtLabel;
    @FXML protected Label trtLabel;
    @FXML protected Label artLabel;
    @FXML protected Label utilLabel;

    @Override
    @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources)
    {
        JobSchedule schedule = (JobSchedule) resources.getObject("schedule");
        long processTime = schedule.processTime();

        // paint the canvas
        GraphicsContext context = canvas.getGraphicsContext2D();

        // draw title
        context.setFill(Color.BLACK);
        context.setTextAlign(TextAlignment.CENTER);
        context.setTextBaseline(VPos.TOP);
        context.setFont(Font.font("System", FontWeight.BOLD, 20));
        context.fillText("Gantt Chart", 320.0, 5.0);

        // draw rectangle
        context.setFill(Color.web("#B8B8B8"));
        context.fillRect(20.0, 50.0, 600.0, 50.0);

        context.setFont(Font.font("System", FontWeight.NORMAL, 12));
        context.setFill(Color.BLACK);
        context.fillText("0", 20.0, 100.0);

        // draw rectangles corresponding to each process
        int colorIndex = 0;
        long lastTime = 0;
        double lastX = 20.0;
        HashMap<String, Color> colorMap = new HashMap<>();

        for (RunningProcess process : schedule)
        {
            String name = process.getName();
            long burstTime = process.getBurstTime();
            double width = burstTime * 600.0 / processTime;

            if (name.length() > 1 && name.matches("P(\\d)*"))
            {
                if (!colorMap.containsKey(name))
                {
                    colorMap.put(name, colors[colorIndex]);
                    colorIndex++;
                }

                context.setFill(colorMap.get(name));
                context.fillRect(lastX, 50.0, width, 50.0);
            }

            lastTime += burstTime;
            lastX += width;

            context.setFill(Color.BLACK);
            context.fillText(lastTime + "", lastX, 100.0);
        }

        ttatLabel.setText(schedule.totalTurnAroundTime() + " units");
        atatLabel.setText(schedule.averageTurnAroundTime() + " units");
        twtLabel.setText(schedule.totalWaitingTime() + " units");
        awtLabel.setText(schedule.averageWaitingTime() + " units");
        trtLabel.setText(schedule.totalResponseTime() + " units");
        artLabel.setText(schedule.averageResponseTime() + " units");
        utilLabel.setText(schedule.utilization() + "%");
    }
}
