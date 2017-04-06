package com.github.toasterguy.nami.GUI.Windows;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.UIManager;

/**
 * Window to display the help text
 * 
 * @author Tobias Miosczka
 */
public class WindowHelp extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5550615557031668397L;

	/**
	 * Constructor.
	 * Creates the frame.
	 */
	public WindowHelp() {
		setType(Type.UTILITY);
		setResizable(false);
		setTitle("Hilfe");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 555, 255);
		JPanel contentPane = new JPanel();
		contentPane.setToolTipText("Hilfe");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextPane txtpnBenutzungbenutzernamemitgliedsnummer = new JTextPane();
		txtpnBenutzungbenutzernamemitgliedsnummer.setBackground(UIManager.getColor("Panel.background"));
		txtpnBenutzungbenutzernamemitgliedsnummer.setEditable(false);
		txtpnBenutzungbenutzernamemitgliedsnummer.setText("Benutzung:\r\n\r\n1.Benutzername (Mitgliedsnummer) und Passwort eingeben.\r\n2.Login Knopf dr\u00FCcken.\r\n3.Warten bis alle daten geladen sind.(Die Progressbar ist dann voll)\r\n4.Filter einstellen.\r\n5.Alle Teilnehmer der Veranstaltung in der Mitgliederliste mit dem \"Hinzuf\u00FCgen =>\" Knopf hinzuf\u00FCgen.\r\n6.Antr\u00E4ge exportieren.(Men\u00FC->Antr\u00E4ge->...)\r\n7.Exportierte Daten \u00FCberpr\u00FCfen(!)\r\n\r\nWeitere Fragen sind zu stellen an: Tobias.Miosczka@stud.uni-due.de\r\n");
		contentPane.add(txtpnBenutzungbenutzernamemitgliedsnummer, BorderLayout.CENTER);
	}

}
