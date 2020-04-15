package LibraryModule.UI.Settings;

import com.google.gson.Gson;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.*;

public class Preferences {
    public static final String CONFIG_FILE = "config.txt";

    private int nDays;
    private float fine;
    private String userName;
    private String password;

    public Preferences(){
        this.nDays = 0;
        this.fine = 1.5f;
        this.userName = "admin";
        setPassword("password");
    }

    public int getnDays() {
        return nDays;
    }

    public void setnDays(int nDays) {
        this.nDays = nDays;
    }

    public float getFine() {
        return fine;
    }

    public void setFine(float fine) {
        this.fine = fine;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        if(password.length() < 16)
            this.password = DigestUtils.sha1Hex(password);
        else
            this.password = password;
    }

    public static void initConfig(){
        Writer writer = null;
        Preferences preferences = new Preferences();
        Gson gson = new Gson();
        try {
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(preferences,writer);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void rewriteConfig(Preferences pref){
        Gson gson = new Gson();
        Writer writer = null;
        try {
            writer = new FileWriter(CONFIG_FILE);
            gson.toJson(pref,writer);
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setContentText("Settings Saved");
            alert.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Preferences getPreference(){
        Reader reader = null;
        Preferences pref = new Preferences();
        Gson gson = new Gson();
        try {
            reader = new FileReader(CONFIG_FILE);
            pref = gson.fromJson(reader,Preferences.class);
        } catch (FileNotFoundException e) {
            initConfig();
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return pref;
    }

}
