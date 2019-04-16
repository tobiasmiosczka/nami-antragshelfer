package com.github.tobiasmiosczka.nami.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.github.tobiasmiosczka.nami.applicationforms.exception.NoParticipantsException;
import com.github.tobiasmiosczka.nami.applicationforms.WriterApplicationCityDinslaken;
import com.github.tobiasmiosczka.nami.applicationforms.WriterApplicationDioezeseMuenster;
import com.github.tobiasmiosczka.nami.applicationforms.WriterApplicationDioezeseMuensterGroupLeader;
import com.github.tobiasmiosczka.nami.applicationforms.WriterBankData;
import com.github.tobiasmiosczka.nami.applicationforms.WriterEmergencyList;
import com.github.tobiasmiosczka.nami.program.*;
import nami.connector.namitypes.NamiGruppierung;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSchulungenMap;
import nami.connector.NamiServer;
import nami.connector.namitypes.enums.NamiGeschlecht;
import nami.connector.namitypes.enums.NamiMitgliedstyp;
import nami.connector.namitypes.enums.NamiStufe;
import nami.connector.exception.NamiApiException;

public class Window extends JFrame implements IGui {

	private static final int lastUpdate = 2018;

	private static final Font fHeadline = new Font("Arial", Font.BOLD, 16);

	private static final Color colorSuccess = Color.decode("0x009900");
	private static final Color colorFailed = Color.decode("0xCC0000");

	private final DocumentListener nameFilterListener = new DocumentListener() {
		@Override
		public void insertUpdate(DocumentEvent e) {}

		@Override
		public void removeUpdate(DocumentEvent e) {}

		@Override
		public void changedUpdate(DocumentEvent e) {
			updateMembersList();
		}
	};

	private JTextField 	tfFirstName,
						tfLastName,
						tfUsername;

	private JPasswordField	pfPassword;

	private JCheckBox 	cWoelflinge,
						cJungpfadfinder,
						cPfadfinder,
						cRover,
						cAndere,
						cMitglied,
						cSchnuppermitglied,
						cNichtmitglied,
						cMaennlich,
						cWeiblich,
						cKeineAngabe;

	private JProgressBar progressBar;

	private JButton bLogin;

	private JList<NamiMitglied>	listFiltered,
								listParticipants;

	private final DefaultListModel<NamiMitglied> 	dlmFiltered = new DefaultListModel<>(),
													dlmParticipants = new DefaultListModel<>();

	private final Program program;
	private NamiServer server = NamiServer.LIVESERVER_WITH_API;

	private JCheckBoxMenuItem cbmiLadeInaktiv;

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

		JMenuItem mntmExit = new JMenuItem("Beenden");
		mntmExit.addActionListener(e -> System.exit(0));
		mnNewMenu.add(mntmExit);

		/*Anträge*/
		JMenu mAntrag = new JMenu("Anträge/Listen");
		menuBar.add(mAntrag);

		JMenuItem mntmAntragStadt = new JMenuItem("Antrag an Stadt");
		mntmAntragStadt.addActionListener(e -> applicationCityDinslaken());
		mAntrag.add(mntmAntragStadt);

		JMenuItem mntmAntragLand = new JMenuItem("Antrag an Diözese Münster");
		mntmAntragLand.addActionListener(e -> applicationDioezeseMuenster());
		mAntrag.add(mntmAntragLand);

		JMenuItem mntmAntragLandLeiter = new JMenuItem("Antrag an Diözese Münster (Leiter)");
		mntmAntragLandLeiter.addActionListener(e -> applicationDioezeseMuensterGroupLeader());
		mAntrag.add(mntmAntragLandLeiter);

		JMenuItem mntmNotfallliste = new JMenuItem("Notfallliste");
		mntmNotfallliste.addActionListener(e -> emergencyPhoneList());
		mAntrag.add(mntmNotfallliste);

		JMenuItem mntmBankData = new JMenuItem("Bankdaten");
		mntmBankData.addActionListener(e -> bankData());
		mAntrag.add(mntmBankData);

		/*Einstellungen*/
		JMenu mProperties = new JMenu("Einstellungen");
		menuBar.add(mProperties);

		/*Sortierung*/
		JMenu mSortation = new JMenu("Sortierung");
		mProperties.add(mSortation);

		ButtonGroup sortByRadioButtonGroup = new ButtonGroup();

		JRadioButtonMenuItem rbmiSortByFirstname = new JRadioButtonMenuItem("Vorname");
		rbmiSortByFirstname.addActionListener(e -> program.sortBy(Sorting.SORT_BY_FIRSTNAME));
		sortByRadioButtonGroup.add(rbmiSortByFirstname);
		mSortation.add(rbmiSortByFirstname);

		JRadioButtonMenuItem rbmiSortByLastname = new JRadioButtonMenuItem("Nachname");
		rbmiSortByLastname.addActionListener(e -> program.sortBy(Sorting.SORT_BY_LASTNAME));
		sortByRadioButtonGroup.add(rbmiSortByLastname);
		mSortation.add(rbmiSortByLastname);

		JRadioButtonMenuItem rbmiSortByAge = new JRadioButtonMenuItem("Alter");
		rbmiSortByAge.addActionListener(e -> program.sortBy(Sorting.SORT_BY_AGE));
		sortByRadioButtonGroup.add(rbmiSortByAge);
		mSortation.add(rbmiSortByAge);

		JRadioButtonMenuItem rbmiSortById = new JRadioButtonMenuItem("ID");
		rbmiSortById.addActionListener(e -> program.sortBy(Sorting.SORT_BY_ID));
		sortByRadioButtonGroup.add(rbmiSortById);
		mSortation.add(rbmiSortById);

		rbmiSortByLastname.setSelected(true);

		/*Server*/
		JMenu mServer = new JMenu("Server");
		mProperties.add(mServer);

		ButtonGroup serverRadioButtonGroup = new ButtonGroup();

		JRadioButtonMenuItem rbmiServerLiveServer = new JRadioButtonMenuItem("Live-Server ohne API");
		rbmiServerLiveServer.addActionListener(e -> this.server = NamiServer.LIVESERVER);
		serverRadioButtonGroup.add(rbmiServerLiveServer);
		mServer.add(rbmiServerLiveServer);

		JRadioButtonMenuItem rbmiServerLiveServerWithApi = new JRadioButtonMenuItem("Live-Server mit API");
		rbmiServerLiveServerWithApi.addActionListener(e -> this.server = NamiServer.LIVESERVER_WITH_API);
		serverRadioButtonGroup.add(rbmiServerLiveServerWithApi);
		mServer.add(rbmiServerLiveServerWithApi);

		JRadioButtonMenuItem rbmiServerTestServer= new JRadioButtonMenuItem("Testserver");
		rbmiServerTestServer.addActionListener(e -> this.server = NamiServer.TESTSERVER);
		serverRadioButtonGroup.add(rbmiServerTestServer);
		mServer.add(rbmiServerTestServer);

		rbmiServerLiveServerWithApi.setSelected(true);

		/*Lade Inaktive*/
		cbmiLadeInaktiv = new JCheckBoxMenuItem("Lade inaktive Mitglieder");
		cbmiLadeInaktiv.setSelected(false);
		mProperties.add(cbmiLadeInaktiv);

		/*Hilfe*/
		JMenu mHelp = new JMenu("Hilfe");
		menuBar.add(mHelp);

		JMenuItem mntmHomepage = new JMenuItem("Homepage");
		mntmHomepage.addActionListener(e -> openUrl("https://github.com/TobiasMiosczka/NaMi"));
		mHelp.add(mntmHomepage);

		JMenuItem mntmLicence = new JMenuItem("Lizenz");
		mntmLicence.addActionListener(e -> openUrl("https://github.com/TobiasMiosczka/NaMi/blob/master/LICENSE"));
		mHelp.add(mntmLicence);

		JMenuItem mntmChangelog = new JMenuItem("Changelog");
		mntmChangelog.addActionListener(e -> openUrl("https://github.com/TobiasMiosczka/NaMi/blob/master/CHANGELOG.md"));
		mHelp.add(mntmChangelog);
	}

	private void openUrl(String url) {
		try {
			Desktop desktop = java.awt.Desktop.getDesktop();
			URI oURL = new URI(url);
			desktop.browse(oURL);
		} catch (Exception e) {
			onException("Fehler beim Öffnen einer URL. ", e);
		}
	}

	private void initLoginPane(JPanel pOptions) {
		JPanel pLogin = new JPanel();
		pLogin.setBounds(10, 10, 180, 100);
		pOptions.add(pLogin);
		pLogin.setLayout(null);

		JLabel lblNewLabel = new JLabel("Login", SwingConstants.CENTER);
		lblNewLabel.setFont(fHeadline);
		lblNewLabel.setBounds(0, 0, 180, 25);
		pLogin.add(lblNewLabel);

		JPanel pLoginForm = new JPanel();
		pLoginForm.setBounds(0, 25, 180, 50);
		pLogin.add(pLoginForm);
		pLoginForm.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lUsername = new JLabel("Benutzername:");
		pLoginForm.add(lUsername);

		tfUsername = new JTextField();
		tfUsername.addActionListener(e -> login());
		tfUsername.setColumns(10);
		pLoginForm.add(tfUsername);

		JLabel lPassword = new JLabel("Passwort:");
		pLoginForm.add(lPassword);

		pfPassword = new JPasswordField();
		pfPassword.addActionListener(e -> login());
		pLoginForm.add(pfPassword);

		bLogin = new JButton("Login");
		bLogin.addActionListener(e -> login());
		bLogin.setBounds(0, 77, 180, 23);
		pLogin.add(bLogin);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		progressBar.setBounds(10, 114, 180, 23);
		pOptions.add(progressBar);
	}

	private void initFilterOptionsPane(JPanel pOptions) {
		JPanel pFilterOptions = new JPanel();
		pFilterOptions.setLayout(null);
		pFilterOptions.setBounds(10, 160, 180, 500);
		pOptions.add(pFilterOptions);

		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setFont(fHeadline);
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
		cWoelflinge.addActionListener(e -> updateMembersList());
		cWoelflinge.setSelected(true);
		pStufe.add(cWoelflinge);

		cJungpfadfinder = new JCheckBox("Jungpfadfinder");
		cJungpfadfinder.addActionListener(e -> updateMembersList());
		cJungpfadfinder.setSelected(true);
		pStufe.add(cJungpfadfinder);

		cPfadfinder = new JCheckBox("Pfadfinder");
		cPfadfinder.addActionListener(e -> updateMembersList());
		cPfadfinder.setSelected(true);
		pStufe.add(cPfadfinder);

		cRover = new JCheckBox("Rover");
		cRover.addActionListener(e -> updateMembersList());
		cRover.setSelected(true);
		pStufe.add(cRover);

		cAndere = new JCheckBox("Keine/Andere");
		cAndere.addActionListener(e -> updateMembersList());
		pStufe.add(cAndere);

		/*Mitgliedstyp*/
		JPanel pAktiv = new JPanel();
		pAktiv.setBounds(0, 240, 180, 95);
		pAktiv.setBorder(BorderFactory.createTitledBorder("Mitgliedstyp"));
		pFilterOptions.add(pAktiv);
		pAktiv.setLayout(new GridLayout(0, 1, 0, 0));

		cMitglied = new JCheckBox("Mitglieder");
		cMitglied.addActionListener(e -> updateMembersList());
		cMitglied.setSelected(true);
		pAktiv.add(cMitglied);

		cSchnuppermitglied = new JCheckBox("Schnuppermitglieder");
		cSchnuppermitglied.addActionListener(e -> updateMembersList());
		cSchnuppermitglied.setSelected(true);
		pAktiv.add(cSchnuppermitglied);

		cNichtmitglied = new JCheckBox("Nichtmitglieder");
		cNichtmitglied.addActionListener(e -> updateMembersList());
		pAktiv.add(cNichtmitglied);

		/*Geschlecht*/
		JPanel pGeschlecht = new JPanel();
		pGeschlecht.setBorder(BorderFactory.createTitledBorder("Geschlecht"));
		pGeschlecht.setBounds(0, 335, 180, 105);
		pFilterOptions.add(pGeschlecht);
		pGeschlecht.setLayout(new GridLayout(0, 1, 0, 0));

		cMaennlich = new JCheckBox("männlich");
		cMaennlich.addActionListener(e -> updateMembersList());
		cMaennlich.setSelected(true);
		pGeschlecht.add(cMaennlich);

		cWeiblich = new JCheckBox("weiblich");
		cWeiblich.addActionListener(e -> updateMembersList());
		cWeiblich.setSelected(true);
		pGeschlecht.add(cWeiblich);

		cKeineAngabe = new JCheckBox("Keine Angabe");
		cKeineAngabe.addActionListener(e -> updateMembersList());
		cKeineAngabe.setSelected(true);
		pGeschlecht.add(cKeineAngabe);

		JLabel lblCopyRight = new JLabel("\u00a9 Tobias Miosczka 2013 - " + lastUpdate);
		lblCopyRight.setFont(new Font("Arial", Font.PLAIN, 12));
		lblCopyRight.setBounds(0, 455, 180, 25);
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
		lblMitglieder.setFont(fHeadline);
		lblMitglieder.setBounds(0, 10, 200, 25);
		pListFiltered.add(lblMitglieder);

		listFiltered = new JList<>();
		listFiltered.setBounds(0, 35, 200, 525);

		JScrollPane spListFiltered = new JScrollPane();
		spListFiltered.setBounds(0, 35, 200, 575);
		spListFiltered.setViewportView(listFiltered);
		pListFiltered.add(spListFiltered);

		JButton bAdd = new JButton("Hinzufügen");
		bAdd.addActionListener(e -> {
			listFiltered.getSelectedValuesList().forEach(program::putMemberToParticipants);
			updateMembersList();
			updateParticipantsList();
		});
		bAdd.setBounds(10, 616, 178, 23);
		pListFiltered.add(bAdd);
	}

	private void initParticipantsPane(JPanel panel) {
		JPanel pParticipants = new JPanel();
		panel.add(pParticipants);
		pParticipants.setLayout(null);

		JLabel lblTeilnehmer = new JLabel("Teilnehmer", SwingConstants.CENTER);
		lblTeilnehmer.setFont(fHeadline);
		lblTeilnehmer.setBounds(0, 10, 200, 25);
		pParticipants.add(lblTeilnehmer);
		
		listParticipants = new JList<>();
		listParticipants.setBounds(0, 35, 200, 525);

		JScrollPane spListParticipants = new JScrollPane();
		spListParticipants.setBounds(0, 35, 200, 575);
		spListParticipants.setViewportView(listParticipants);
		pParticipants.add(spListParticipants);

		JButton bRemove = new JButton("Entfernen");
		bRemove.addActionListener(e -> {
			listParticipants.getSelectedValuesList().forEach(program::putParticipantToMember);
			updateMembersList();
			updateParticipantsList();
		});
		bRemove.setBounds(10, 616, 178, 23);
		pParticipants.add(bRemove);
	}

	private void initialize() {
		setResizable(false);
		setTitle("Nami Antragshelfer " + VersionHelper.VERSION);
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

		listFiltered.setModel(dlmFiltered);
		listParticipants.setModel(dlmParticipants);
	}

	private void showPassResult(boolean success, String text){
		if (success) {
			tfUsername.setBackground(colorSuccess);
			pfPassword.setBackground(colorSuccess);
			progressBar.setString("");
			bLogin.setEnabled(false);
			tfUsername.setEnabled(false);
			pfPassword.setEnabled(false);
		} else {
			tfUsername.setBackground(colorFailed);
			pfPassword.setBackground(colorFailed);
			JOptionPane.showMessageDialog(this, text);
			progressBar.setString("");
		}
	}

	private boolean checkFilter(NamiMitglied m) {
		boolean bIsWlf = (m.getStufe() == NamiStufe.WOELFLING),
		 		bIsJng = (m.getStufe() == NamiStufe.JUNGPFADFINDER),
		 		bIsPfd = (m.getStufe() == NamiStufe.PFADFINDER),
		 		bIsRvr = (m.getStufe() == NamiStufe.ROVER),
		 		bIsNon = !(bIsWlf || bIsJng || bIsPfd || bIsRvr);
		//check stufe
		if (!(  (bIsWlf && cWoelflinge.isSelected()) ||
				(bIsJng && cJungpfadfinder.isSelected()) ||
				(bIsPfd && cPfadfinder.isSelected()) ||
				(bIsRvr && cRover.isSelected()) ||
				(bIsNon && cAndere.isSelected()))
				) {
			return false;
		}
		//check Aktiv
		if (!(  (cMitglied.isSelected()			 && m.getMitgliedstyp() == NamiMitgliedstyp.MITGLIED) ||
				(cSchnuppermitglied.isSelected() && m.getMitgliedstyp() == NamiMitgliedstyp.SCHNUPPER_MITGLIED) ||
				(cNichtmitglied.isSelected()	 && m.getMitgliedstyp() == NamiMitgliedstyp.NICHT_MITGLIED))) {
			return false;
		}
		//check gender
		if (!(  (cWeiblich.isSelected()  && m.getGeschlecht() == NamiGeschlecht.WEIBLICH) ||
				(cMaennlich.isSelected() && m.getGeschlecht() == NamiGeschlecht.MAENNLICH) ||
				(cKeineAngabe.isSelected() && m.getGeschlecht() == NamiGeschlecht.KEINE_ANGABE))) {
			return false;
		}
		//check Name
		return m.getVorname().toLowerCase().contains(tfFirstName.getText().toLowerCase()) && m.getNachname().toLowerCase().contains(tfLastName.getText().toLowerCase());
	}

	private void updateParticipantsList() {
		List<NamiMitglied> selected = listParticipants.getSelectedValuesList();
		dlmParticipants.removeAllElements();
		program.getParticipants().forEach(dlmParticipants::addElement);
		int[] selectedIndices = new int[selected.size()];
		int i = 0;
		for(NamiMitglied member : selected) {
			selectedIndices[i++] = dlmParticipants.indexOf(member);
		}
		listParticipants.setSelectedIndices(selectedIndices);
	}

	private void updateMembersList(){
		List<NamiMitglied> selected = listFiltered.getSelectedValuesList();
		dlmFiltered.removeAllElements();
		program.getMember().stream().filter(this::checkFilter).forEach(dlmFiltered::addElement);
		int[] selectedIndices = new int[selected.size()];
		int i = 0;
		for (NamiMitglied member : selected) {
			selectedIndices[i++] = dlmFiltered.indexOf(member);
		}
		listFiltered.setSelectedIndices(selectedIndices);
	}

	private void applicationDioezeseMuenster() {
		UserInputDialog ui = new UserInputDialog(this);
		ui.addStringOption("Mitgliedsverband", "");
		ui.addStringOption("Träger", "");
		ui.addBooleanOption("Datum freilassen", false);
		ui.addDateOption("Datum von (tt.mm.jjjj)", new Date());
		ui.addDateOption("Datum bis (tt.mm.jjjj)", new Date());
		ui.addStringOption("PLZ","");
		ui.addStringOption("Ort","");
		ui.addStringOption("Land","");
		if(!ui.showModal()) {
			return;
		}
		String fileName = SaveDialog.getFilePath("Land Ausgefüllt.odt");
		if(fileName == null) {
			return;
		}
		try {
			new WriterApplicationDioezeseMuenster(
					(String)ui.getOption(0),
					(String)ui.getOption(1),
					(Boolean)ui.getOption(2),
					(Date)ui.getOption(3),
					(Date)ui.getOption(4),
					(String)ui.getOption(5),
					(String)ui.getOption(6),
					(String)ui.getOption(7)
				).run(fileName, program.getParticipants());
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch (NoParticipantsException e1) {
			JOptionPane.showMessageDialog(null, "Es wurden keine Teilnehmer ausgewählt.");
		}
	}

	private void applicationDioezeseMuensterGroupLeader() {
		UserInputDialog ui = new UserInputDialog(this);
		ui.addBooleanOption("Datum freilassen", false);
		ui.addDateOption("Datum (tt.mm.jjjj)", new Date());
		if(!ui.showModal()) {
			return;
		}
		// load courses of selected members
		List<NamiSchulungenMap> schulungen;
		try {
			schulungen = program.loadSchulungen(program.getParticipants());
		} catch (Exception e1) {
			e1.printStackTrace();
			return;
		}
		String fileName = SaveDialog.getFilePath("Land Leiter Ausgefüllt.odt");
		if(fileName == null) {
			return;
		}
		try {
			new WriterApplicationDioezeseMuensterGroupLeader(schulungen, (Boolean)ui.getOption(0), (Date)ui.getOption(1)).run(fileName, program.getParticipants());
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch (NoParticipantsException e1) {
			JOptionPane.showMessageDialog(null, "Es wurden keine Teilnehmer ausgewählt.");
		}
	}

	private void applicationCityDinslaken() {
		UserInputDialog ui = new UserInputDialog(this);
		ui.addStringOption("Maßnahme" , "");
		ui.addBooleanOption("Datum freilassen", false);
		ui.addDateOption("Anfang (tt.mm.jjjj)", new Date());
		ui.addDateOption("Ende (tt.mm.jjjj)", new Date());
		ui.addStringOption("Ort", "");
		if (!ui.showModal()) {
			return;
		}
		String fileName = SaveDialog.getFilePath("Stadt Ausgefüllt.odt");
		if (fileName == null) {
			return;
		}
		try {
			new WriterApplicationCityDinslaken((String)ui.getOption(0), (Boolean)ui.getOption(1), (Date)ui.getOption(2), (Date)ui.getOption(3), (String)ui.getOption(4)).run(fileName, program.getParticipants());
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch (NoParticipantsException e1) {
			JOptionPane.showMessageDialog(null, "Es wurden keine Teilnehmer ausgewählt.");
		}
	}

	private void emergencyPhoneList() {
		String fileName = SaveDialog.getFilePath("applicationForms/Notfallliste.odt");
		if (fileName == null) {
			return;
		}
		try {
			new WriterEmergencyList().run(fileName, program.getParticipants());
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch (NoParticipantsException e1) {
			JOptionPane.showMessageDialog(null, "Es wurden keine Teilnehmer ausgewählt.");
		}
	}

	private void bankData() {
		UserInputDialog ui = new UserInputDialog(this);
		ui.addFloatOption("Voller Beitrag [€](Dezimalpunkt)", 0.0f);
		ui.addFloatOption("Familienermäßigter Beitrag [€](Dezimalpunkt)", 0.0f);
		ui.addFloatOption("Sozialermäßigter Beitrag [€](Dezimalpunkt)", 0.0f);
		if (!ui.showModal()) {
			return;
		}
		String fileName = SaveDialog.getFilePath("applicationForms/Bankdaten.odt");
		if (fileName == null) {
			return;
		}
		try {
			new WriterBankData((Float)ui.getOption(0), (Float)ui.getOption(1), (Float)ui.getOption(2)).run(fileName, program.getParticipants());
		} catch (Exception e1) {
			e1.printStackTrace();
		} catch (NoParticipantsException e1) {
			JOptionPane.showMessageDialog(null, "Es wurden keine Teilnehmer ausgewählt.");
		}
	}

	private void login() {
		try {
			program.login(tfUsername.getText(), String.copyValueOf(pfPassword.getPassword()), server);
		} catch (IOException e) {
			this.showPassResult(false, "Keine Verbindung zum Server: " + e.getMessage());
			return;
		} catch (NamiApiException e) {
			this.showPassResult(false, e.getMessage());
			return;
		}
		showPassResult(true, "");
		try {
			program.loadData(cbmiLadeInaktiv.isSelected());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(this, "Keine Verbindung zum Server: " + e.getMessage());
		} catch (NamiApiException e) {
			JOptionPane.showMessageDialog(this, e.getMessage());
		}
	}

	@Override
	public void onUpdate(int current, int count, NamiMitglied member) {
		EventQueue.invokeLater(() -> {
			progressBar.setMaximum(count);
			progressBar.setValue(current);
			progressBar.setString("(" + current + "/" + count + ") " + member.getVorname() + " " + member.getNachname());
			updateMembersList();
		});
	}

	@Override
	public void onException(String message, Exception e) {
		JOptionPane.showMessageDialog(this, message + " " + e.getMessage());
	}

	@Override
	public void onDone(long timeMS) {
		EventQueue.invokeLater(() -> {
			progressBar.setMaximum(100);
			progressBar.setValue(100);
			progressBar.setString("Fertig nach " + timeMS / 1000 + "s.");
		});
	}

	@Override
	public NamiGruppierung selectGruppierung(Collection<NamiGruppierung> gruppierungen) {
		return GroupSelector.selectGruppierung(this, gruppierungen);
	}
}