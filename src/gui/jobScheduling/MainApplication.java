package gui.jobScheduling;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApplication extends Application implements Runnable
{
    final private String[] args;

    public MainApplication()
    {
        args = new String[0];
    }

    public MainApplication(String... args)
    {
        this.args = args;
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        primaryStage.setTitle("Job Sequencing");

        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        Scene scene = new Scene(root);

        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
    }

    @Override
    public void run()
    {
        launch(args);
    }
}
