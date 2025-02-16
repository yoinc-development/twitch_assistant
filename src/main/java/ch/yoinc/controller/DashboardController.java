package ch.yoinc.controller;

import ch.yoinc.TwitchAssistantApplication;
import ch.yoinc.manager.SettingsManager;
import ch.yoinc.model.Assistant;
import ch.yoinc.service.SpeechToTextService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class DashboardController {

    @FXML
    BorderPane contentPane;
    @FXML
    VBox northComponent;
    @FXML
    VBox southComponent;
    @FXML
    VBox westComponent;
    @FXML
    ScrollPane eastComponent;
    @FXML
    Button record_voice;
    @FXML
    Button read_chat;
    @FXML
    VBox chatbox;
    @FXML
    ComboBox assistantsComboBox;
    @FXML
    TextArea assistantDescriptionTextArea;

    SpeechToTextService speechToTextService;

    @FXML
    public void test() {

        eastComponent.setDisable(false);
        southComponent.setDisable(false);
        westComponent.setDisable(false);

        List<Assistant> assistants = new ArrayList<Assistant>();

        Assistant cowboy = new Assistant("Cowboy", "You are a cowboy. You like to tell stories and make jokes.");
        Assistant pirate = new Assistant("Pirate", "You are a pirate. You are funny and have fun.");
        Assistant corporate = new Assistant("Corporate", "You are a corporate. You are serious and don't make any jokes.");

        assistants.add(cowboy);
        assistants.add(pirate);
        assistants.add(corporate);

        assistantDescriptionTextArea.setText(SettingsManager.getInstance().getSettings().getTa_username());

        assistantsComboBox.getItems().addAll(assistants);

        assistantsComboBox.setConverter(new StringConverter<Assistant>() {
            @Override
            public String toString(Assistant assistant) {
                return assistant != null ? assistant.getTitle() : "";
            }

            @Override
            public Assistant fromString(String string) {
                return assistants.stream()
                        .filter(a -> a.getTitle().equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });

        assistantsComboBox.setOnAction(event -> {
            Assistant selectedAssistant = (Assistant) assistantsComboBox.getSelectionModel().getSelectedItem();
            if (selectedAssistant != null) {
                assistantDescriptionTextArea.setText(selectedAssistant.getDescription());
            }
        });

        //TODO move this
        speechToTextService = new SpeechToTextService();
    }

    @FXML
    public void handleVoiceRecording() {

        ResourceBundle bundle = ResourceBundle.getBundle("texts");

        if (record_voice.getText().equals(bundle.getString("button_start_recording"))) {
            speechToTextService.startRecording();
            record_voice.setText(bundle.getString("button_stop_recording"));

            northComponent.setDisable(true);
            westComponent.setDisable(true);

        } else {
            speechToTextService.stopRecording();
            String transcription = speechToTextService.transcribeAudio();
            displayMessageInChat(transcription);
            record_voice.setText(bundle.getString("button_start_recording"));

            northComponent.setDisable(false);
            westComponent.setDisable(false);
        }
    }

    private void displayMessageInChat(String message) {

        VBox messageBox = new VBox();
        messageBox.setPrefWidth(350);
        messageBox.setStyle("-fx-border-color: black; -fx-padding: 5;");
        messageBox.setSpacing(5);

        Label usernameLabel = new Label("System");
        usernameLabel.setStyle("-fx-font-weight: bold;");

        Label messageContentLabel = new Label(message);
        messageContentLabel.setWrapText(true);

        messageBox.getChildren().addAll(usernameLabel, messageContentLabel);
        chatbox.getChildren().add(messageBox);
    }

    @FXML
    public void initialize() {

        //handle all elements with dependencies from settings
        if (!SettingsManager.getInstance().getSettings().isTa_google_credential_set()) {
            record_voice.setDisable(true);
        }

        if (SettingsManager.getInstance().getSettings().getTa_api_twitch().isEmpty()) {
            read_chat.setDisable(true);
        }

        test();
    }

    @FXML
    public void handleSettings() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(TwitchAssistantApplication.class.getResource("settings.fxml"));
            Parent settingsRoot = (Parent) fxmlLoader.load();
            Image icon = new Image(TwitchAssistantApplication.class.getResourceAsStream("/logo.jpg"));

            SettingsController settingsController = fxmlLoader.getController();
            settingsController.setData();
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.setResizable(false);
            stage.setScene(new Scene(settingsRoot));
            stage.getIcons().add(icon);

            Stage currentStage = (Stage) contentPane.getScene().getWindow();
            stage.initOwner(currentStage);
            stage.showAndWait();

            if (!settingsController.closedProgrammatically) {
                String font = getClass().getResource("/css/application.css").toExternalForm();
                ButtonType confirm = new ButtonType(ResourceBundle.getBundle("texts").getString("button_confirm"), ButtonBar.ButtonData.OK_DONE);
                Alert alert = new Alert(Alert.AlertType.WARNING, ResourceBundle.getBundle("texts").getString("alert_settings_instructions"), confirm);
                alert.setTitle(ResourceBundle.getBundle("texts").getString("alert_settings_title"));
                alert.setHeaderText("");
                alert.getDialogPane().getScene().getStylesheets().add(font);
                ((Stage) alert.getDialogPane().getScene().getWindow()).getIcons().add(icon);

                Optional<ButtonType> result = alert.showAndWait();
            }

            initialize();

        } catch (Exception e) {
            System.out.println("Exception");
        }
    }
}
