package gui.jobScheduling;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.*;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.stage.*;

import java.net.URL;
import java.util.*;

import models.jobScheduling.*;

public class MainController implements Initializable
{
    @FXML protected MenuItem closeMenuItem;
    @FXML protected MenuItem removeJobMenuItem;
    @FXML protected MenuItem clearJobMenuItem;

    @FXML protected TableView<Job> jobTableView;

    @FXML protected TableColumn<Job, Integer> idColumn;
    @FXML protected TableColumn<Job, Integer> arrivalTimeColumn;
    @FXML protected TableColumn<Job, Integer> burstTimeColumn;
    @FXML protected TableColumn<Job, Integer> priorityColumn;

    @FXML protected TextField arrivalTimeField;
    @FXML protected TextField burstTimeField;
    @FXML protected TextField priorityField;
    // @FXML protected TextField cstField;

    @FXML protected Button addJobButton;

    @FXML protected ChoiceBox algorithmChoiceBox;

    ObservableList algorithms = FXCollections.observableArrayList(
            "First Come First Serve",
            "Shortest Job First",
            "Priority (Non-preemptive)",
            new Separator(),
            "Shortest Remaining Job First",
            "Round Robin"
    );

    @Override @SuppressWarnings("unchecked")
    public void initialize(URL location, ResourceBundle resources)
    {
        /* initialize menu items */
        closeMenuItem.setOnAction(event -> Platform.exit());

        removeJobMenuItem.setOnAction(this::removeSelectedJob);
        clearJobMenuItem.setOnAction(event -> jobTableView.getItems().clear());


        /* initialize table related components */
        jobTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));
        burstTimeColumn.setCellValueFactory(new PropertyValueFactory<>("burstTime"));
        priorityColumn.setCellValueFactory(new PropertyValueFactory<>("priority"));

        /* initialize text fields and button */
        arrivalTimeField.textProperty().addListener((observable, oldValue, newValue) ->
                validateInteger(arrivalTimeField, oldValue, newValue, 0));
        burstTimeField.textProperty().addListener((observable, oldValue, newValue) ->
                validateInteger(burstTimeField, oldValue, newValue, 1));
        priorityField.textProperty().addListener((observable, oldValue, newValue) ->
                validateInteger(priorityField, oldValue, newValue, 1));
        priorityField.setText("1");

        /* cstField.textProperty().addListener(((observable, oldValue, newValue) ->
        *       validateInteger(cstField, oldValue, newValue, 0)));
        */

        addJobButton.setOnAction(this::addJob);
        addJobButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER)
                addJob(event);
        });

        /* initialize components of last HBox */
        algorithmChoiceBox.getItems().addAll(algorithms);
        algorithmChoiceBox.setValue(algorithms.get(0));
    }

    @SuppressWarnings("unchecked")
    protected void addJob(Event event)
    {
        /* get the value of each of text field */
        String arrivalTimeText = arrivalTimeField.getText();
        String burstTimeText = burstTimeField.getText();
        String priorityText = priorityField.getText();

        /* check whether strings are empty */
        boolean flag = arrivalTimeText.compareTo("") != 0;
        flag = flag && burstTimeText.compareTo("") != 0;
        flag = flag && priorityText.compareTo("") != 0;

        if (flag)
        {
            ObservableList<Job> jobs = jobTableView.getItems();

            /* get all fields of a job */
            int id = jobs.size() + 1;
            int arrivalTime = Integer.parseInt(arrivalTimeText);
            int burstTime = Integer.parseInt(burstTimeText);
            int priority = Integer.parseInt(priorityText);

            /* add job to the table */
            jobs.add(new Job(id, arrivalTime, burstTime, priority));

            /* clear the text fields */
            arrivalTimeField.setText("");
            burstTimeField.setText("");
            priorityField.setText("1");

            /* set the focus to arrival time text field */
            arrivalTimeField.requestFocus();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Arrival time, Burst time and Priority should not be empty");
            alert.showAndWait();
        }
    }

    protected void validateInteger(TextField field, String oldValue, String newValue, int limit)
    {
        try
        {
            if (newValue.compareTo("") == 0 || Integer.parseInt(newValue) >= limit)
                field.setText(newValue);
            else
                throw new NumberFormatException("Value less than zero");
        }
        catch (NumberFormatException ex)
        {
            field.setText(oldValue);
        }
    }

    @SuppressWarnings("unchecked")
    protected void removeSelectedJob(ActionEvent event)
    {
        if (jobTableView.isFocused())
        {
            ObservableList<Job> selected = jobTableView.getSelectionModel().getSelectedItems();
            ObservableList<Job> jobs = jobTableView.getItems();

            jobs.removeAll(selected);

            int i = 1;
            for (Job job : jobs)
                job.setId(i++);
        }
    }

    public void showAbout()
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About");
        alert.setHeaderText("An application for illustrating job scheduling in CPU with various algorithms");
        alert.setContentText("This is application is created by Shreesha Prabhu K, UG student in "
                + "Computer Science branch of MANIT, Bhopal. This application is an assignment of "
                + "Operating System Lab."
        );

        alert.setWidth(480);
        alert.setHeight(360);
        alert.show();
    }

    @FXML
    protected void display() throws Exception
    {
        if (jobTableView.getItems().isEmpty())
        {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initModality(Modality.WINDOW_MODAL);
            alert.initOwner(jobTableView.getScene().getWindow());
            alert.setHeaderText(null);
            alert.setContentText("There are no jobs to display Gantt Chart");
            alert.showAndWait();
            return;
        }

        Set<Job> jobs = new HashSet<>();
        jobs.addAll(jobTableView.getItems());

        int ind = algorithmChoiceBox.getSelectionModel().getSelectedIndex();

        JobSchedule js;
        String title = (String) algorithms.get(ind);
        switch (ind)
        {
            case 0:
                js = new FCFS(jobs);
                break;
            case 1:
                js = new SJF(jobs);
                break;
            case 2:
                js = new PrioritySchedule(jobs);
                break;
            case 4:
                js = new SRJF(jobs);
                break;
            case 5:
                long timeQuantum = 1;
                TextInputDialog dialog = new TextInputDialog();

                dialog.setTitle("Time Quantum");
                dialog.setHeaderText(null);
                dialog.setContentText("Specify time quantum: ");

                Optional<String> result;
                while ((result = dialog.showAndWait()).isPresent())
                {
                    String text = result.get();
                    try
                    {
                        timeQuantum = Long.parseLong(text);
                        if (timeQuantum < 1)
                            throw new NumberFormatException();
                        break;
                    }
                    catch (NumberFormatException ex)
                    {
                        dialog.setHeaderText("Enter valid time quantum(> 0)");
                    }
                }
                if (!result.isPresent())
                    return;

                title += " (Time quantum: " + timeQuantum + ")";
                js = new RoundRobin(jobs, timeQuantum);
                break;
            default:
                return;
        }

        ResourceBundle rb = new ListResourceBundle()
        {
            @Override
            protected Object[][] getContents()
            {
                Object[][] contents = new Object[1][];
                contents[0] = new Object[]{"schedule", js};

                return contents;
            }
        };

        FXMLLoader loader = new FXMLLoader(getClass().getResource("Display.fxml"), rb);
        Parent displayRoot = loader.load();
        Scene scene = new Scene(displayRoot, 640, 480);

        Stage stage = new Stage(StageStyle.UTILITY);
        stage.initOwner(jobTableView.getScene().getWindow());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle(title);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.showAndWait();
    }
}
