package com.github.tobiasmiosczka.nami.GUI.Windows;

import com.github.tobiasmiosczka.nami.extendetjnami.namitypes.Gruppierung;

import javax.swing.*;
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
    private final JComboBox<Gruppierung> cbGroupSelect;

    private final Gruppierung[] groups;

    private Gruppierung group = null;

    public GroupSelector(JFrame owner, Collection<Gruppierung> groups) {
        this.groups = groups.toArray(new Gruppierung[groups.size()]);
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

    public Gruppierung showModal(){
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
