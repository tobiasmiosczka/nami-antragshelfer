package com.github.tobiasmiosczka.nami.GUI.Windows;

import nami.connector.namitypes.NamiGruppierung;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

/**
 * JFrame to select either one group the user likes to work with or all groups
 * @author Tobias Miosczka
 */
public class GroupSelector extends JFrame implements ActionListener {

    private final JDialog dialog;
    private final JButton   btnOne,
                            btnAll;
    private final JComboBox<NamiGruppierung> cbGroupSelect;

    private final NamiGruppierung[] groups;

    private NamiGruppierung group = null;

    public GroupSelector(JFrame owner, Collection<NamiGruppierung> groups) {
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

        btnOne = new JButton("Auswählen");
        btnOne.setBounds(10, 100, 180, 30);
        btnOne.addActionListener(this);
        dialog.getContentPane().add(btnOne);

        btnAll = new JButton("Alle Auswählen");
        btnAll.setBounds(210, 100, 200, 30);
        btnAll.addActionListener(this);
        dialog.getContentPane().add(btnAll);
    }

    public NamiGruppierung showModal(){
        dialog.setVisible(true);
        return group;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == btnAll){
            group = null;
        }
        if(e.getSource() == btnOne){
            group = groups[cbGroupSelect.getSelectedIndex()];
        }
        dialog.setVisible(false);
    }
}
