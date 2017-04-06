package com.github.toasterguy.nami.GUI.Windows;

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.border.EmptyBorder;
import javax.swing.UIManager;
/**
 * Window to display the licence
 *
 * @author Tobias Miosczka
 */
public class WindowLicence extends JFrame {


	private static final long serialVersionUID = -3638721420122220527L;

	/**
	 * Constructor.
	 * Creates the frame.
	 */
	public WindowLicence() {
		setType(Type.UTILITY);
		setResizable(false);
		setTitle("Lizenz");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 512, 511);
		JPanel contentPane = new JPanel();
		contentPane.setToolTipText("Hilfe");
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JTextPane txtpnBenutzungbenutzernamemitgliedsnummer = new JTextPane();
		txtpnBenutzungbenutzernamemitgliedsnummer.setEditable(false);
		txtpnBenutzungbenutzernamemitgliedsnummer.setBackground(UIManager.getColor("Panel.background"));
		txtpnBenutzungbenutzernamemitgliedsnummer.setText("Der Code unterliegt der GNU GPL. Der vollst\u00E4ndige Lizenztext befindet sich in LICENSE.\r\n\r\n    Copyright (C) \t2013 - 2016 Tobias Miosczka\r\n\r\n    This program is free software: you can redistribute it and/or modify\r\n    it under the terms of the GNU General Public License as published by\r\n    the Free Software Foundation, either version 3 of the License, or\r\n    (at your option) any later version.\r\n\r\n    This program is distributed in the hope that it will be useful,\r\n    but WITHOUT ANY WARRANTY; without even the implied warranty of\r\n    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the\r\n    GNU General Public License for more details.\r\n\r\n    You should have received a copy of the GNU General Public License\r\n    along with this program. If not, see <http://www.gnu.org/licenses/>.\r\n\r\nDanke an Fabian Lipp, der die NamiConnector-Klassen programmiert und ver\u00F6ffentlicht hat.");
		contentPane.add(txtpnBenutzungbenutzernamemitgliedsnummer, BorderLayout.CENTER);
	}

}
