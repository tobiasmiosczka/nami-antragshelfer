package com.github.tobiasmiosczka.nami.view;

import com.github.tobiasmiosczka.nami.util.FileEncodingUtil;
import com.github.tobiasmiosczka.nami.util.VersionUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {

    private static final String TITLE = "NaMi-Antragshelfer " + VersionUtil.VERSION;
    private static final double MIN_WIDTH = 500D;
    private static final double MIN_HEIGHT = 500D;

    private static Scene scene;

    public static void main(String[] args) {
        try {
            //TODO: change
            FileEncodingUtil.setFileEncoding("UTF-8");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("gui/primary.fxml"));

        stage.setTitle(TITLE);
        stage.setMinWidth(MIN_WIDTH);
        stage.setMinHeight(MIN_HEIGHT);
        stage.getIcons().add(new Image(App.class.getClassLoader().getResourceAsStream("gui/icon.png")));

        stage.setScene(scene);
        stage.show();
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getClassLoader().getResource(fxml));
        return fxmlLoader.load();
    }

}