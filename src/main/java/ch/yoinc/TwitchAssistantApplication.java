package ch.yoinc;

import ch.yoinc.manager.SettingsManager;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Optional;
import java.util.ResourceBundle;

public class TwitchAssistantApplication extends Application {

    @Override   
    public void start(Stage stage) throws Exception {

        SettingsManager.getInstance();

        stage.setOnCloseRequest(windowEvent -> {
            SettingsManager.getInstance().saveSettingsOnClose();
        });

        FXMLLoader fxmlLoader = new FXMLLoader(TwitchAssistantApplication.class.getResource("dashboard.fxml"));
        fxmlLoader.setResources(ResourceBundle.getBundle("texts"));
        Scene scene = new Scene(fxmlLoader.load(), 1000, 1000);
        String font = getClass().getResource("/css/application.css").toExternalForm();
        scene.getStylesheets().add(font);

        Image icon = new Image(TwitchAssistantApplication.class.getResourceAsStream("/logo.jpg"));

        stage.setTitle("Twitch Assistant");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.getIcons().add(icon);

        stage.show();

        ButtonType confirm = new ButtonType(ResourceBundle.getBundle("texts").getString("button_confirm"), ButtonBar.ButtonData.OK_DONE);
        ButtonType cancel = ButtonType.CANCEL;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,ResourceBundle.getBundle("texts").getString("alert_instructions"), confirm, cancel);
        alert.setTitle(ResourceBundle.getBundle("texts").getString("alert_title"));
        alert.setHeaderText("");
        alert.getDialogPane().getScene().getStylesheets().add(font);
        ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(icon);

        Optional<ButtonType> result = alert.showAndWait();
        if(result.get() == ButtonType.CANCEL) {
            Platform.exit();
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
