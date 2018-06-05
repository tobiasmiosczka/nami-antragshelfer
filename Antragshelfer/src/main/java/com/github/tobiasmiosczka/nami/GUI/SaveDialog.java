package com.github.tobiasmiosczka.nami.GUI;

import javax.swing.JFileChooser;
import java.io.File;

/**
 * Created by Tobias on 13.10.2016.
 * Class for selecting a path to save a file.
 */
public class SaveDialog{
    public static String getFilePath(String previewFileName) {
        JFileChooser fc = new JFileChooser();
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setSelectedFile(new File(previewFileName));
        if  (fc.showDialog(null, "Speichern") == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile().getAbsolutePath();
        } else {
            return null;
        }
    }
}
