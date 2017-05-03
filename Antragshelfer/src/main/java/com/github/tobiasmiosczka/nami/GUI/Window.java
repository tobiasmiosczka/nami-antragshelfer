package com.github.tobiasmiosczka.nami.GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import com.github.tobiasmiosczka.nami.GUI.Windows.GroupSelector;
import com.github.tobiasmiosczka.nami.applicationForms.*;
import com.github.tobiasmiosczka.nami.extendetjnami.namitypes.Gruppierung;
import com.github.tobiasmiosczka.nami.extendetjnami.namitypes.SchulungenMap;
import com.github.tobiasmiosczka.nami.GUI.Windows.WindowHelp;
import com.github.tobiasmiosczka.nami.GUI.Windows.WindowLicence;
import com.github.tobiasmiosczka.nami.program.FileEncodingHelper;
import com.github.tobiasmiosczka.nami.program.Program;
import nami.connector.namitypes.enums.Geschlecht;
import nami.connector.namitypes.enums.Mitgliedstyp;
import nami.connector.namitypes.enums.Stufe;
import nami.connector.exception.NamiApiException;
import nami.connector.namitypes.NamiMitglied;
import com.github.tobiasmiosczka.nami.GUI.Windows.WindowChangelog;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

public class Window extends JFrame implements  ActionListener, Program.ProgramHandler {

	private static final int VERSION_MAJOR = 3;
	private static final int VERSION_MINOR = 1;
	private static final int lastUpdate = 2017;

	private static final Color colorSuccess = Color.decode("0x009900");
	private static final Color colorFailed = Color.decode("0xCC0000");

	private final DocumentListener nameFilterListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent e) {
			updateLists();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			updateLists();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
			updateLists();
		}
	};

	private JPanel pLogin;

	private JTextField 	tfFirstName,
						tfLastName,
						tfUsername;
	private JLabel      lbUser;

	private JCheckBox 	cWoelflinge,
						cJungpfadfinder,
						cPfadfinder,
						cRover,
						cAndere,

						cMitglied,
						cSchnuppermitglied,
						cNichtmitglied,

						cMaennlich,
						cWeiblich;

	private JProgressBar progressBar;

	private JButton 	bAdd,
						bLogin,
						bRemove;

	private JMenuItem 	mntmExit,
						mntmHelp,
						mntmLicence,
						mntmAntragStadt,
						mntmAntragLand,
						mntmAntragLandLeiter,
						mntmNotfallliste,
						mntmChangelog;

	private JRadioButtonMenuItem 	rbmiSortByFirstname,
									rbmiSortByLastname,
									rbmiSortByAge;

	private JPasswordField	pfPassword;

	private JList<NamiMitglied>	listFiltered,
								listParticipants;

	private DefaultListModel<NamiMitglied> 	dlmFiltered,
											dlmParticipants;

	private final WindowHelp windowHelp = new WindowHelp();
	private final WindowLicence windowLicence = new WindowLicence();
	private final WindowChangelog windowChangelog = new WindowChangelog();

	private final Program program;

	public static void main(String[] args) {
		try {
			FileEncodingHelper.setFileEncoding("UTF-8");
		} catch (IllegalAccessException | NoSuchFieldException e) {
			e.printStackTrace();
		}
        Window window = new Window();
	}

	public Window() {
		this.program = new Program(this);
		initialize();
		setVisible(true);
	}

	private void loadWindowIcon() {
		try {
			InputStream s = Thread.currentThread().getContextClassLoader().getResourceAsStream("images/lilie.gif");
			Image icon = ImageIO.read(s);
			setIconImage(icon);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void initMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		/*Programm*/
		JMenu mnNewMenu = new JMenu("Programm");
		menuBar.add(mnNewMenu);

		mntmExit = new JMenuItem("Beenden");
		mntmExit.addActionListener(this);
		mnNewMenu.add(mntmExit);

		/*Anträge*/
		JMenu mAntrag = new JMenu("Anträge");
		menuBar.add(mAntrag);

		mntmAntragStadt = new JMenuItem("Antrag an Stadt");
		mntmAntragStadt.addActionListener(this);
		mAntrag.add(mntmAntragStadt);

		mntmAntragLand = new JMenuItem("Antrag an Land");
		mntmAntragLand.addActionListener(this);
		mAntrag.add(mntmAntragLand);

		mntmAntragLandLeiter = new JMenuItem("Antrag an Land (Leiter)");
		mntmAntragLandLeiter.addActionListener(this);
		mAntrag.add(mntmAntragLandLeiter);

		mntmNotfallliste = new JMenuItem("Notfallliste");
		mntmNotfallliste.addActionListener(this);
		mAntrag.add(mntmNotfallliste);

		/*Einstellungen*/
		JMenu mProperties = new JMenu("Einstellungen");
		menuBar.add(mProperties);

		JMenu mSortation = new JMenu("Sortierung");
		mProperties.add(mSortation);

		ButtonGroup sortByRadioButtonGroup = new ButtonGroup();

		rbmiSortByFirstname = new JRadioButtonMenuItem("Vorname");
		rbmiSortByFirstname.addActionListener(this);
		sortByRadioButtonGroup.add(rbmiSortByFirstname);
		mSortation.add(rbmiSortByFirstname);

		rbmiSortByLastname = new JRadioButtonMenuItem("Nachname");
		rbmiSortByLastname.addActionListener(this);
		sortByRadioButtonGroup.add(rbmiSortByLastname);
		mSortation.add(rbmiSortByLastname);

		rbmiSortByAge = new JRadioButtonMenuItem("Alter");
		rbmiSortByAge.addActionListener(this);
		sortByRadioButtonGroup.add(rbmiSortByAge);
		mSortation.add(rbmiSortByAge);

		rbmiSortByLastname.setSelected(true);

		/*Hilfe*/
		JMenu mHelp = new JMenu("Hilfe");
		menuBar.add(mHelp);

		mntmHelp = new JMenuItem("Hilfe");
		mntmHelp.addActionListener(this);
		mHelp.add(mntmHelp);

		mntmLicence = new JMenuItem("Lizenz");
		mntmLicence.addActionListener(this);
		mHelp.add(mntmLicence);

		mntmChangelog = new JMenuItem("Changelog");
		mntmChangelog.addActionListener(this);
		mHelp.add(mntmChangelog);
	}

	private void initLoginPane(JPanel pOptions) {
		pLogin = new JPanel();
		pLogin.setBounds(10, 10, 180, 100);
		pOptions.add(pLogin);
		pLogin.setLayout(null);

		JLabel lblNewLabel = new JLabel("Login", SwingConstants.CENTER);
		lblNewLabel.setBounds(0, 0, 180, 25);
		pLogin.add(lblNewLabel);

		JPanel pLoginForm = new JPanel();
		pLoginForm.setBounds(0, 25, 180, 50);
		pLogin.add(pLoginForm);
		pLoginForm.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lUsername = new JLabel("Benutzername:");
		pLoginForm.add(lUsername);

		tfUsername = new JTextField();
		tfUsername.addActionListener(this);
		tfUsername.setColumns(10);
		pLoginForm.add(tfUsername);

		JLabel lPassword = new JLabel("Passwort:");
		pLoginForm.add(lPassword);

		pfPassword = new JPasswordField();
		pfPassword.addActionListener(this);
		pLoginForm.add(pfPassword);

		bLogin = new JButton("Login");
		bLogin.addActionListener(this);
		bLogin.setBounds(49, 77, 89, 23);
		pLogin.add(bLogin);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(10, 114, 180, 14);
		pOptions.add(progressBar);

		lbUser = new JLabel("", SwingConstants.CENTER);
		lbUser.setBounds(10, 130, 180, 14);
		pOptions.add(lbUser);
	}

	private void initFilterOptionsPane(JPanel pOptions) {
		JPanel pFilterOptions = new JPanel();
		pFilterOptions.setLayout(null);
		pFilterOptions.setBounds(10, 150, 180, 500);
		pOptions.add(pFilterOptions);

		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setBounds(64, 0, 46, 25);
		pFilterOptions.add(lblFilter);

		/*Name*/
		JPanel pName = new JPanel();
		pName.setBorder(BorderFactory.createTitledBorder("Name"));
		pName.setBounds(0, 25, 180, 70);
		pFilterOptions.add(pName);
		pName.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lVorname = new JLabel("Vorname:");
		pName.add(lVorname);

		tfFirstName = new JTextField();
		pName.add(tfFirstName);
		tfFirstName.setColumns(10);
		tfFirstName.getDocument().addDocumentListener(nameFilterListener);

		JLabel lNachnahme = new JLabel("Nachnahme:");
		pName.add(lNachnahme);

		tfLastName = new JTextField();
		pName.add(tfLastName);
		tfLastName.setColumns(10);
		tfLastName.getDocument().addDocumentListener(nameFilterListener);

		/*Stufe*/
		JPanel pStufe = new JPanel();
		pStufe.setBorder(BorderFactory.createTitledBorder("Stufe"));
		pStufe.setBounds(0, 95, 180, 145);
		pFilterOptions.add(pStufe);
		pStufe.setLayout(new GridLayout(0, 1, 0, 0));

		cWoelflinge = new JCheckBox("Wölflinge");
		cWoelflinge.addActionListener(this);
		cWoelflinge.setSelected(true);
		pStufe.add(cWoelflinge);

		cJungpfadfinder = new JCheckBox("Jungpfadfinder");
		cJungpfadfinder.addActionListener(this);
		cJungpfadfinder.setSelected(true);
		pStufe.add(cJungpfadfinder);

		cPfadfinder = new JCheckBox("Pfadfinder");
		cPfadfinder.addActionListener(this);
		cPfadfinder.setSelected(true);
		pStufe.add(cPfadfinder);

		cRover = new JCheckBox("Rover");
		cRover.addActionListener(this);
		cRover.setSelected(true);
		pStufe.add(cRover);

		cAndere = new JCheckBox("Andere");
		cAndere.addActionListener(this);
		pStufe.add(cAndere);

		/*Mitgliedstyp*/
		JPanel pAktiv = new JPanel();
		pAktiv.setBounds(0, 240, 180, 95);
		pAktiv.setBorder(BorderFactory.createTitledBorder("Mitgliedstyp"));
		pFilterOptions.add(pAktiv);
		pAktiv.setLayout(new GridLayout(0, 1, 0, 0));

		cMitglied = new JCheckBox("Mitglieder");
		cMitglied.addActionListener(this);
		cMitglied.setSelected(true);
		pAktiv.add(cMitglied);

		cSchnuppermitglied = new JCheckBox("Schnuppermitglieder");
		cSchnuppermitglied.addActionListener(this);
		cSchnuppermitglied.setSelected(true);
		pAktiv.add(cSchnuppermitglied);

		cNichtmitglied = new JCheckBox("Nichtmitglieder");
		cNichtmitglied.addActionListener(this);
		pAktiv.add(cNichtmitglied);


		/*Geschlecht*/
		JPanel pGeschlecht = new JPanel();
		pGeschlecht.setBorder(BorderFactory.createTitledBorder("Geschlecht"));
		pGeschlecht.setBounds(0, 335, 180, 70);
		pFilterOptions.add(pGeschlecht);
		pGeschlecht.setLayout(new GridLayout(0, 1, 0, 0));

		cMaennlich = new JCheckBox("männlich");
		cMaennlich.addActionListener(this);
		cMaennlich.setSelected(true);
		pGeschlecht.add(cMaennlich);

		cWeiblich = new JCheckBox("weiblich");
		cWeiblich.addActionListener(this);
		cWeiblich.setSelected(true);
		pGeschlecht.add(cWeiblich);

		JLabel lblCopyRight = new JLabel("\u00a9 Tobias Miosczka 2013 - " + lastUpdate);
		lblCopyRight.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCopyRight.setBounds(0, 475, 180, 25);
		pFilterOptions.add(lblCopyRight);
	}

	private void initOptionsPane(JPanel panel) {
		JPanel pOptions = new JPanel();
		panel.add(pOptions);
		pOptions.setLayout(null);
		initLoginPane(pOptions);
		initFilterOptionsPane(pOptions);
	}

	private void initMemberPane(JPanel panel) {
		JPanel pListFiltered = new JPanel();
		panel.add(pListFiltered);
		pListFiltered.setLayout(null);

		JLabel lblMitglieder = new JLabel("Mitglieder", SwingConstants.CENTER);
		lblMitglieder.setBounds(0, 10, 200, 25);
		pListFiltered.add(lblMitglieder);

		listFiltered = new JList<>();
		listFiltered.setBounds(0, 35, 200, 525);

		JScrollPane spListFiltered = new JScrollPane();
		spListFiltered.setBounds(0, 35, 200, 575);
		spListFiltered.setViewportView(listFiltered);
		pListFiltered.add(spListFiltered);

		bAdd = new JButton("Hinzufügen =>");
		bAdd.addActionListener(this);
		bAdd.setBounds(10, 616, 178, 23);
		pListFiltered.add(bAdd);
	}

	private void initParticipantsPane(JPanel panel) {
		JPanel pParticipants = new JPanel();
		panel.add(pParticipants);
		pParticipants.setLayout(null);

		JLabel lblTeilnehmer = new JLabel("Teilnehmer", SwingConstants.CENTER);
		lblTeilnehmer.setBounds(0, 10, 200, 25);
		pParticipants.add(lblTeilnehmer);
		
		listParticipants = new JList<>();
		listParticipants.setBounds(0, 35, 200, 525);

		JScrollPane spListParticipants = new JScrollPane();
		spListParticipants.setBounds(0, 35, 200, 575);
		spListParticipants.setViewportView(listParticipants);
		pParticipants.add(spListParticipants);

		bRemove = new JButton("<= Entfernen");
		bRemove.addActionListener(this);
		bRemove.setBounds(10, 616, 178, 23);
		pParticipants.add(bRemove);
	}

	private void initialize() {
		setResizable(false);
		setTitle("Nami Antragshelfer " + String.valueOf(VERSION_MAJOR) + "." + String.valueOf(VERSION_MINOR));
		setSize(600, 700);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		loadWindowIcon();
		initMenuBar();

		JPanel pAll = new JPanel();
		getContentPane().add(pAll);
		pAll.setLayout(new GridLayout(1, 3, 0, 0));

		initOptionsPane(pAll);
		initMemberPane(pAll);
		initParticipantsPane(pAll);

		dlmFiltered = new DefaultListModel<>();
		dlmParticipants = new DefaultListModel<>();
		listFiltered.setModel(dlmFiltered);
		listParticipants.setModel(dlmParticipants);
	}

	private void showPassResult(boolean success, String text){
		if(success){
			tfUsername.setBackground(colorSuccess);
			pfPassword.setBackground(colorSuccess);
			progressBar.setString("");
			lbUser.setText(text);
			bLogin.setEnabled(false);
			tfUsername.setEnabled(false);
			pfPassword.setEnabled(false);
		}else{
			tfUsername.setBackground(colorFailed);
			pfPassword.setBackground(colorFailed);
			JOptionPane.showMessageDialog(this, text);
			progressBar.setString("");
		}
	}

	private boolean checkFilter(NamiMitglied m) {
		boolean bIsWlf = (m.getStufe() == Stufe.WOELFLING);
		boolean bIsJng = (m.getStufe() == Stufe.JUNGPFADFINDER);
		boolean bIsPfd = (m.getStufe() == Stufe.PFADFINDER);
		boolean bIsRvr = (m.getStufe() == Stufe.ROVER);
		boolean bIsNon = !(bIsWlf || bIsJng || bIsPfd || bIsRvr);
		//check stufe
		if(!((bIsWlf && cWoelflinge.isSelected()) ||
				(bIsJng && cJungpfadfinder.isSelected()) ||
				(bIsPfd && cPfadfinder.isSelected()) ||
				(bIsRvr && cRover.isSelected()) ||
				(bIsNon && cAndere.isSelected()))
				) {
			return false;
		}
		//check Aktiv
		if(!((cMitglied.isSelected()			 && m.getMitgliedstyp() == Mitgliedstyp.MITGLIED) ||
				(cSchnuppermitglied.isSelected() && m.getMitgliedstyp() == Mitgliedstyp.SCHNUPPER_MITGLIED) ||
				(cNichtmitglied.isSelected()	 && m.getMitgliedstyp() == Mitgliedstyp.NICHT_MITGLIED))) {
			return false;
		}
		//check gender
		if(!((cWeiblich.isSelected()  && m.getGeschlecht() == Geschlecht.WEIBLICH) ||
				(cMaennlich.isSelected() && m.getGeschlecht() == Geschlecht.MAENNLICH))) {
			return false;
		}
		//check Name
		if (!((m.getVorname().toLowerCase().contains(tfFirstName.getText().toLowerCase())) &&
				m.getNachname().toLowerCase().contains(tfLastName.getText().toLowerCase()))) {
			return false;
		}
		return true;
	}

	private void updateLists(){ //TODO: make it faster
		List<NamiMitglied> selected = listFiltered.getSelectedValuesList();

		dlmFiltered.removeAllElements();
		for(NamiMitglied m : program.getMember()){
			if (checkFilter(m)) {
				dlmFiltered.addElement(m);
			}
		}

		int[] selectedIndices = new int[selected.size()];
		for (int i = 0; i < selected.size(); ++i) {
			selectedIndices[i] = dlmFiltered.indexOf(selected.get(i));
		}
		listFiltered.setSelectedIndices(selectedIndices);

		selected = listParticipants.getSelectedValuesList();

		dlmParticipants.removeAllElements();
		for(NamiMitglied m : program.getParticipants()){
			dlmParticipants.addElement(m);
		}

		selectedIndices = new int[selected.size()];
		for(int i = 0; i < selected.size(); ++i) {
			selectedIndices[i] = dlmParticipants.indexOf(selected.get(i));
		}
		listParticipants.setSelectedIndices(selectedIndices);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if( source == cWoelflinge ||
				source == cJungpfadfinder ||
				source == cPfadfinder ||
				source == cRover ||
				source == cAndere ||
				source == cMitglied ||
				source == cSchnuppermitglied ||
				source == cNichtmitglied ||
				source == cMaennlich ||
				source == cWeiblich){
			updateLists();
		}
		if(source == bAdd){
			listFiltered.getSelectedValuesList().forEach(program::putMemberToParticipants);
			updateLists();
		}
		if(source == bRemove){
			listParticipants.getSelectedValuesList().forEach(program::putParticipantToMember);
			updateLists();
		}
		if(source == bLogin || source == tfUsername || source == pfPassword){
			this.login();
		}
		if(source == rbmiSortByLastname) {
			program.sortBy(Program.Sortation.SORT_BY_LASTNAME);
			updateLists();
		}
		if(source == rbmiSortByFirstname) {
			program.sortBy(Program.Sortation.SORT_BY_FIRSTNAME);
			updateLists();
		}
		if(source == rbmiSortByAge) {
			program.sortBy(Program.Sortation.SORT_BY_AGE);
			updateLists();
		}
		if(source == mntmExit){
			System.exit(0);
		}
		if(source == mntmHelp){
			windowHelp.setVisible(true);
		}
		if(source == mntmLicence){
			windowLicence.setVisible(true);
		}
		if(source == mntmChangelog){
			windowChangelog.setVisible(true);
		}
		if(source == mntmAntragLand){
			UserInput ui = new UserInput(this);
			ui.addStringOption("Mitgliedsverband" , "DPSG Diözesanverband Münster");
			ui.addStringOption("Träger", "Stamm DPSG St. Vincentius Dinslaken");
			ui.addBooleanOption("Datum freilassen", false);
			ui.addDateOption("Anfang (tt.mm.jjjj)", new Date());
			ui.addDateOption("Ende (tt.mm.jjjj)", new Date());
			ui.addStringOption("PLZ", "");
			ui.addStringOption("Ort", "");
			ui.addStringOption("Land", "Deutschland");
			if(!ui.showModal()) {
				return;
			}

			SaveDialog sd = new SaveDialog("Land Ausgefüllt.odt");
			if(!sd.showDialog()) {
				return;
			}

			try {
				new WriterAntragLand((String)ui.getOption(0), (String)ui.getOption(1), (Boolean)ui.getOption(2), (Date)ui.getOption(3), (Date)ui.getOption(4), (String)ui.getOption(5), (String)ui.getOption(6), (String)ui.getOption(7)).run(sd.getAbsolutePath(), program.getParticipants());
			} catch (Exception e1) {
				e1.printStackTrace();
			} catch (NoParticipantsException e1) {
				JOptionPane.showMessageDialog(null, "Es wurden keine Teilnehmer ausgewählt.");
				return;
			}
		}
		if(source == mntmAntragLandLeiter){
			UserInput ui = new UserInput(this);
			ui.addBooleanOption("Datum freilassen", false);
			ui.addDateOption("Datum (tt.mm.jjjj)", new Date());
			if(!ui.showModal()) {
				return;
			}

			// load courses of selected members
			List<SchulungenMap> schulungen;
			try {
				schulungen = program.loadSchulungen(program.getParticipants());
			} catch (Exception e1) {
				e1.printStackTrace();
				return;
			}

			SaveDialog sd = new SaveDialog("Land Leiter Ausgefüllt.odt");
			if(!sd.showDialog()) {
				return;
			}

			try {
				new WriterAntragLandLeiter(schulungen, (Boolean)ui.getOption(0), (Date)ui.getOption(1)).run(sd.getAbsolutePath(), program.getParticipants());
			} catch (Exception e1) {
				e1.printStackTrace();
			} catch (NoParticipantsException e1) {
			JOptionPane.showMessageDialog(null, "Es wurden keine Teilnehmer ausgewählt.");
			return;
			}
		}
		if(source == mntmAntragStadt){
			UserInput ui = new UserInput(this);
			ui.addStringOption("Maßnahme" , "");
			ui.addBooleanOption("Datum freilassen", false);
			ui.addDateOption("Anfang (tt.mm.jjjj)", new Date());
			ui.addDateOption("Ende (tt.mm.jjjj)", new Date());
			ui.addStringOption("Ort", "");
			if(!ui.showModal()) {
				return;
			}

			SaveDialog sd = new SaveDialog("Stadt Ausgefüllt.odt");
			if(!sd.showDialog()) {
				return;
			}

			try {
				new WriterAntragStadtDinslaken((String)ui.getOption(0), (Boolean)ui.getOption(1), (Date)ui.getOption(2), (Date)ui.getOption(3), (String)ui.getOption(4)).run(sd.getAbsolutePath(), program.getParticipants());
			} catch (Exception e1) {
				e1.printStackTrace();
			} catch (NoParticipantsException e1) {
				JOptionPane.showMessageDialog(null, "Es wurden keine Teilnehmer ausgewählt.");
				return;
			}
		}
		if(source == mntmNotfallliste){
			SaveDialog sd = new SaveDialog("applicationForms/Notfallliste.odt");
			if(!sd.showDialog()) {
				return;
			}
			try {
				new WriterEmergencyList().run(sd.getAbsolutePath(), program.getParticipants());
			} catch (Exception e1) {
				e1.printStackTrace();
			} catch (NoParticipantsException e1) {
			JOptionPane.showMessageDialog(null, "Es wurden keine Teilnehmer ausgewählt.");
			}
		}
	}

	private void login() {
		String user = tfUsername.getText();
		String pass = String.copyValueOf(pfPassword.getPassword());
		try{
			program.login(user, pass);
			program.loadData();
		} catch (IOException e) {
			this.showPassResult(false, "Keine Verbindung zum Server: " + e.getMessage());
			return;
		} catch (NamiApiException e) {
			this.showPassResult(false, e.getMessage());
			return;
		}
		showPassResult(true, "Angemeldet als " + user);
	}

	@Override
	public void onUpdate(int current, int count, NamiMitglied member) {
		EventQueue.invokeLater(() -> {
			progressBar.setMaximum(count);
			progressBar.setValue(current);
			String sb = "(" + current + "/" + count + ") " + member.getVorname() + " " + member.getNachname();
			progressBar.setString(sb);
			updateLists();
		});
	}

	@Override
	public void onException(Exception e) {
		//TODO: do something meaningfull, handle Exception, show it on GUI
		JOptionPane.showMessageDialog(this, "Beim laden der Mitgliedsdaten ist ein Fehler aufgetreten.");
		e.printStackTrace();
	}

	@Override
	public  void onDone(long timeMS) {
		EventQueue.invokeLater(() -> {
			progressBar.setMaximum(100);
			progressBar.setValue(100);
			progressBar.setString("Fertig nach " + timeMS / 1000 + "s.");
			updateLists();
		});
	}

	@Override
	public Gruppierung selectGruppierung(Collection<Gruppierung> gruppierungen) {
		GroupSelector gs = new GroupSelector(this, gruppierungen);
		return gs.showModal();
	}
}