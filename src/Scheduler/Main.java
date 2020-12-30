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
import java.time.*;
import java.time.temporal.WeekFields;
import java.util.Calendar;
import java.util.Locale;
import java.util.ResourceBundle;
import java.time.format.*;
import java.util.Calendar.*;
import java.util.TimeZone;

public class Main extends Application {

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

        var months = Month.values();
        for (Month month: months) {
            System.out.println(month.getValue() + " : " + month.getDisplayName(TextStyle.FULL,defaultLocale));
        }

        Calendar calendar = Calendar.getInstance(TimeZone.getDefault(),defaultLocale);
        calendar.set(Calendar.DAY_OF_WEEK,1);
        var firstDate = calendar.getTime();
        calendar.set(Calendar.DAY_OF_WEEK,7);
        var LastDate = calendar.getTime();
        LocalDate firstDay = LocalDate.ofInstant(firstDate.toInstant(), ZoneId.systemDefault());
        LocalDate lastDay = LocalDate.ofInstant(LastDate.toInstant(),ZoneId.systemDefault());


    }
    public static void main(String[] args) throws SQLException {
        launch(args);
        DBConnector.closeConnection();
    }
}
