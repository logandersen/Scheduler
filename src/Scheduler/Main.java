package Scheduler;

import Scheduler.DAO.DBService;
import Scheduler.DAO.DBConnector;
import Scheduler.controller.LoginController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.*;
import java.util.Locale;
import java.util.ResourceBundle;

/** Main application class for the scheduler */
public class Main extends Application {

    /** Starts the program by getting the local resource bundle and opening the login form */
    @Override
    public void start(Stage primaryStage) throws Exception{
        Locale defaultLocale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("Scheduler/Nat",defaultLocale);

        FXMLLoader loginLoader = new FXMLLoader();
        loginLoader.setLocation(getClass().getResource("view/Login.fxml"));
        loginLoader.setResources(rb);
        Parent loginParent = loginLoader.load();
        LoginController loginController = loginLoader.getController();

        Connection conn = DBConnector.connectToDB();
        loginController.setConn(conn);

        loginController.setLocale(defaultLocale);

        primaryStage.setTitle(rb.getString("primaryStage.title"));
        primaryStage.setScene(new Scene(loginParent));
        primaryStage.show();
    }
    /** Launches the application and closes the connection after the application closes */
    public static void main(String[] args) throws SQLException {
        launch(args);
        DBConnector.closeConnection();
    }
}
