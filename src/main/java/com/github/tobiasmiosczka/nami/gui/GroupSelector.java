package com.github.tobiasmiosczka.nami.gui;

import nami.connector.namitypes.NamiGruppierung;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.util.Collection;

/**
 * JFrame to select either one group the user likes to work with or all groups
 * @author Tobias Miosczka
 */
public class GroupSelector{

    private final JDialog dialog;
    private final JComboBox<NamiGruppierung> cbGroupSelect;

    private final NamiGruppierung[] groups;

    private NamiGruppierung group = null;

    private GroupSelector(JFrame owner, Collection<NamiGruppierung> groups) {
        this.groups = groups.toArray(new NamiGruppierung[0]);
        dialog = new JDialog(owner, true);
        dialog.setTitle("Gruppierung");
        dialog.setResizable(false);
        dialog.setAlwaysOnTop(true);
        dialog.setLocation(owner.getLocation());
        dialog.setSize(420, 170);
        dialog.getContentPane().setLayout(null);

        JLabel lbDescription = new JLabel("Bitte wähle die Gruppierung aus, mit welcher du arbeiten möchtest:");
        lbDescription.setBounds(10, 10, 400, 30);
        dialog.getContentPane().add(lbDescription);

        cbGroupSelect = new JComboBox<>(this.groups);
        cbGroupSelect.setBounds(10, 50, 400, 30);
        dialog.getContentPane().add(cbGroupSelect);

        JButton btnOne = new JButton("Auswählen");
        btnOne.setBounds(10, 100, 180, 30);
        btnOne.addActionListener(e -> {
            group = this.groups[cbGroupSelect.getSelectedIndex()];
            dialog.setVisible(false);
        });
        dialog.getContentPane().add(btnOne);

        JButton btnAll = new JButton("Alle Auswählen");
        btnAll.setBounds(210, 100, 200, 30);
        btnAll.addActionListener(e -> {
            group = null;
            dialog.setVisible(false);
        });
        dialog.getContentPane().add(btnAll);
    }

    private NamiGruppierung showModal(){
        dialog.setVisible(true);
        return group;
    }

    public static NamiGruppierung selectGruppierung(JFrame owner, Collection<NamiGruppierung> gruppierungen) {
        GroupSelector groupSelector = new GroupSelector(owner, gruppierungen);
        return groupSelector.showModal();
    }
}
