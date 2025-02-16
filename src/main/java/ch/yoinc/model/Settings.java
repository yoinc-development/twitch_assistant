package ch.yoinc.model;

import java.util.List;

public class Settings {

    String ta_username;
    String ta_api_twitch;
    String ta_api_openai;
    boolean ta_google_credential_set;
    List<Assistant> ta_assistants;

    public Settings(String ta_username, String ta_api_twitch, String ta_api_openai, boolean ta_google_credential_set, List<Assistant> ta_assistants) {
        this.ta_username = ta_username;
        this.ta_api_twitch = ta_api_twitch;
        this.ta_api_openai = ta_api_openai;
        this.ta_google_credential_set = ta_google_credential_set;
        this.ta_assistants = ta_assistants;
    }

    public String getTa_username() {
        return ta_username;
    }

    public String getTa_api_twitch() {
        return ta_api_twitch;
    }

    public String getTa_api_openai() {
        return ta_api_openai;
    }

    public boolean isTa_google_credential_set() {
        return ta_google_credential_set;
    }

    public List<Assistant> getTa_assistants() {
        return ta_assistants;
    }

    public void setTa_username(String ta_username) {
        this.ta_username = ta_username;
    }

    public void setTa_api_twitch(String ta_api_twitch) {
        this.ta_api_twitch = ta_api_twitch;
    }

    public void setTa_api_openai(String ta_api_openai) {
        this.ta_api_openai = ta_api_openai;
    }

    public void setTa_google_credential_set(boolean ta_google_credential_set) {
        this.ta_google_credential_set = ta_google_credential_set;
    }
}
