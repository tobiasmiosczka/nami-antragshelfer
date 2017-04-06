package com.github.toasterguy.nami.GUI.Windows;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

/**
 * Window to display the change log
 * 
 * @author Tobias Miosczka
 */
public class WindowChangelog extends JFrame {
	
	private static final long serialVersionUID = 51999655259994672L;

	/**
	 * Constructor.
	 * Creates the frame.
	 */
	public WindowChangelog() {
		setType(Type.UTILITY);
		setResizable(false);
		setTitle("Hilfe");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 400, 700);
		JPanel contentPane = new JPanel();
		contentPane.setToolTipText("Changelog");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);		
		JTextPane txtpnBenutzungbenutzernamemitgliedsnummer = new JTextPane();
		txtpnBenutzungbenutzernamemitgliedsnummer.setBackground(UIManager.getColor("Panel.background"));
		txtpnBenutzungbenutzernamemitgliedsnummer.setEditable(false);
		txtpnBenutzungbenutzernamemitgliedsnummer.setText(
			  "Changelog                                            \r\n"
			+ "                                                     \r\n"
			+ "0.7 \t Erste version                                 \r\n"
			+ "1.0 \t Antrag Land hinzugefügt                       \r\n"
			+ "\t Antrag Stadt hinzugefügt                          \r\n"
			+ "1.1 \t Formatierungsfehler behoben (utf-8).          \r\n"
			+ "1.3 \t Antragsvorlagen in .jar gespeichert.          \r\n"
			+ "1.4 \t Antrag an die Stadt Dinslaken aktualisiert.   \r\n"
			+ "\t Unbenutzte externe Libraries entfernt.            \r\n"
			+ "\t ->Größe der .jar um 4/5 reduziert!                \r\n"
			+ "\t Benutzereingaben zu jedem Antrag hinzugefügt.     \r\n"
			+ "1.5 \t Bug durch Nami-Version 2.1 wurde behoben.     \r\n"
			+ "\t Fehler bei Benutzereingaben und Filter behoben.   \r\n"
			+ "1.6 \t Option hinzugefügt um das Datum               \r\n"
			+ "\t eines Antrags freizulassen.                       \r\n"
			+ "\t Speicherort eines Antrags kann nun selbst bestimmt\r\n"
			+ "\t werden.                                           \r\n"
			+ "\t                                                   \r\n"
			+ "2.0 \t Funktion zur Erstellung von Notfalllisten     \r\n"
			+ "\t hinzugefügt.                                      \r\n"
			+ "\t Formatierungsfehler beseitigt.                    \r\n"
			+ "\t Bugfixes.                                         \r\n"
			+ "2.1 \t Geburtsdaten und Alter werden richtig         \r\n"
			+ "\t angezeigt.                                        \r\n"
			+ "\t                                                   \r\n"
			+ "2.2 \t Fehler wurde behoben: Login-Fehlermeldungen   \r\n"
			+ "\t werden nun richtig angezeigt.                     \r\n"
			+ "\t                                                   \r\n"
			+ "3.0 \t Neues Formblatt für die Abrechnung mit dem    \r\n"
			+ "Land \t\"Land (Leiter)\" hinzugefügt.                \r\n"
			+ "\t Nurnoch eine Datei pro Formblatt.                 \r\n"
			+ "\t Es kann nun nach Vor- und Nachnamen sortiert      \r\n"
			+ "\t werden.                                           \r\n"
			+ "\t Vor dem Download können nun eine oder alle        \r\n"
			+ "\t Gruppierungen ausgewählt werden.                  \r\n");
		contentPane.add(txtpnBenutzungbenutzernamemitgliedsnummer, BorderLayout.CENTER);
	}

}
