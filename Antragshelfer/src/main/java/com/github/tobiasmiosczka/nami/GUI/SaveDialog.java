package com.github.tobiasmiosczka.nami.GUI;

import javax.swing.*;
import java.io.File;

/**
 * Created by Tobias on 13.10.2016.
 * Class for selecting a path to save a file.
 */
public class SaveDialog{
    private final JFileChooser fc = new JFileChooser();

    public SaveDialog(String previewFileName) {
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setSelectedFile(new File(previewFileName));
    }
    public boolean showDialog(){
        return (fc.showDialog(null, "Speichern") == JFileChooser.APPROVE_OPTION);
    }

    public String getAbsolutePath() {
        return fc.getSelectedFile().getAbsolutePath();
    }
}
