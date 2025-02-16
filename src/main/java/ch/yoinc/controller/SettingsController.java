package ch.yoinc.controller;

import ch.yoinc.manager.SettingsManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SettingsController {

    @FXML
    TextField ta_username;
    @FXML
    PasswordField ta_api_twitch;
    @FXML
    PasswordField ta_api_openai;
    @FXML
    CheckBox ta_google_credential_set;
    @FXML
    Button saveButton;
    public boolean closedProgrammatically = false;

    public void setData() {
        ta_username.setText(SettingsManager.getInstance().getSettings().getTa_username());
        ta_api_twitch.setText(SettingsManager.getInstance().getSettings().getTa_api_twitch());
        ta_api_openai.setText(SettingsManager.getInstance().getSettings().getTa_api_openai());
        ta_google_credential_set.setSelected(SettingsManager.getInstance().getSettings().isTa_google_credential_set());
    }

    @FXML
    public void saveSettings() {

        closedProgrammatically = true;

        SettingsManager.getInstance().getSettings().setTa_username(ta_username.getText());
        SettingsManager.getInstance().getSettings().setTa_api_twitch(ta_api_twitch.getText());
        SettingsManager.getInstance().getSettings().setTa_api_openai(ta_api_openai.getText());
        SettingsManager.getInstance().getSettings().setTa_google_credential_set(ta_google_credential_set.isSelected());

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }
}
