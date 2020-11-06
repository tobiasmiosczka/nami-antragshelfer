package com.github.tobiasmiosczka.nami.view;

import javafx.scene.image.Image;

import java.util.Objects;

public class IconUtil {
    private static final Image ICON = new Image(Objects.requireNonNull(App.class.getClassLoader().getResourceAsStream("gui/icon.png")));

    public static Image getIcon() {
        return ICON;
    }
}
