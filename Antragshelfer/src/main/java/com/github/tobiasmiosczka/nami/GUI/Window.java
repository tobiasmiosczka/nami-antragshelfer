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
import com.github.tobiasmiosczka.nami.program.Helper;
import com.github.tobiasmiosczka.nami.program.Program;
import com.github.tobiasmiosczka.nami.GUI.Windows.WindowHelp;
import com.github.tobiasmiosczka.nami.GUI.Windows.WindowLicence;
import nami.connector.Mitgliedstyp;
import nami.connector.Stufe;
import nami.connector.exception.NamiApiException;
import nami.connector.exception.NamiLoginException;
import nami.connector.namitypes.NamiMitglied;
import com.github.tobiasmiosczka.nami.GUI.Windows.WindowChangelog;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;


/**
 * Programs GUI class
 *
 * @author Tobias Miosczka
 *
 */
public class Window extends JFrame implements  ActionListener, DocumentListener, Program.ProgramHandler {

	private static final int VERSION_MAJOR = 3;
	private static final int VERSION_MINOR = 1;
	private static final int lastUpdate = 2017;

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
			cNichtmitglied;

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

	private JPasswordField	pfPassword;

	private JRadioButton 	rbSortByFirstname,
							rbSortByLastname;

	private JList<NamiMitglied>			listFiltered,
			listParticipants;

	private DefaultListModel<NamiMitglied> 	dlmFiltered,
			dlmParticipants;

	private final WindowHelp windowHelp = new WindowHelp();
	private final WindowLicence windowLicence = new WindowLicence();
	private final WindowChangelog windowChangelog = new WindowChangelog();

	private final Program program;

	/**
	 * returns the GUIs JProgressBar
	 *
	 * @return GUIs JProgressBar to display progress
	 */
	private JProgressBar getProgressBar(){
		return progressBar;
	}

	public static void main(String[] args) {
		Helper.setFileEncodingUTF8();
		Window window = new Window();
	}

	/**
	 * Constructor
	 */
	public Window() {
		this.program = new Program(this);
		initialize();
		setVisible(true);
	}

	private void loadImage() {
		try {
			InputStream s = Thread.currentThread().getContextClassLoader().getResourceAsStream("images/lilie.gif");
			Image icon = ImageIO.read(s);
			setIconImage(icon);
		} catch (IOException e) {
			System.out.println("Icon couldn't be loaded.");
			e.printStackTrace();
		}
	}

	private void initMenuBar(){
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnNewMenu = new JMenu("Programm");
		menuBar.add(mnNewMenu);

		mntmExit = new JMenuItem("Beenden");
		mntmExit.addActionListener(this);
		mnNewMenu.add(mntmExit);

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

	private void initSortByPane(JPanel pOptions) {
		JPanel pSortBy =new JPanel();

		pSortBy.setBounds(10, 450, 100, 100);
		pSortBy.setLayout(null);

		JLabel lbSortBy = new JLabel("Sortieren nach:");
		lbSortBy.setBounds(0, 0, 180, 25);
		pSortBy.add(lbSortBy);

		rbSortByFirstname = new JRadioButton("Vorname");
		rbSortByFirstname.setBounds(0, 25, 100, 25);
		rbSortByFirstname.setSelected(false);
		rbSortByFirstname.addActionListener(this);
		pSortBy.add(rbSortByFirstname);

		rbSortByLastname = new JRadioButton("Nachname");
		rbSortByLastname.setBounds(0, 50, 100, 25);
		rbSortByLastname.setSelected(true);
		rbSortByLastname.addActionListener(this);
		pSortBy.add(rbSortByLastname);

		pOptions.add(pSortBy);

		ButtonGroup sortByRadioButtonGroup = new ButtonGroup();
		sortByRadioButtonGroup.add(rbSortByFirstname);
		sortByRadioButtonGroup.add(rbSortByLastname);

	}

	private void initLoginPane(JPanel pOptions) {
		JPanel pLogin = new JPanel();
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
		pFilterOptions.setBounds(10, 150, 180, 275);
		pOptions.add(pFilterOptions);


		JPanel pStufe = new JPanel();
		pStufe.setBounds(0, 25, 180, 125);
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

		JPanel pName = new JPanel();
		pName.setBounds(0, 150, 180, 50);
		pFilterOptions.add(pName);
		pName.setLayout(new GridLayout(0, 2, 0, 0));

		JLabel lVorname = new JLabel("Vorname:");
		pName.add(lVorname);

		tfFirstName = new JTextField();
		pName.add(tfFirstName);
		tfFirstName.setColumns(10);
		tfFirstName.getDocument().addDocumentListener(this);

		JLabel lNachnahme = new JLabel("Nachnahme:");
		pName.add(lNachnahme);

		tfLastName = new JTextField();
		pName.add(tfLastName);
		tfLastName.setColumns(10);
		tfLastName.getDocument().addDocumentListener(this);

		JPanel pAktiv = new JPanel();
		pAktiv.setBounds(0, 200, 180, 75);
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

		JLabel lblFilter = new JLabel("Filter");
		lblFilter.setBounds(64, 0, 46, 25);
		pFilterOptions.add(lblFilter);
	}

	/**
	 * initializes the GUI
	 */
	private void initialize() {
		setResizable(false);
		setTitle("Nami Antragshelfer "+String.valueOf(VERSION_MAJOR)+"."+String.valueOf(VERSION_MINOR));
		setBounds(100, 100, 600, 650);
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));

		loadImage();

		initMenuBar();

		JPanel pAll = new JPanel();
		getContentPane().add(pAll);
		pAll.setLayout(new GridLayout(1, 3, 0, 0));

		JPanel pOptions = new JPanel();
		pAll.add(pOptions);
		pOptions.setLayout(null);

		initLoginPane(pOptions);

		initFilterOptionsPane(pOptions);

		initSortByPane(pOptions);

		JLabel lblCopyRight = new JLabel("(c) Tobias Miosczka 2013 - " + lastUpdate);
		lblCopyRight.setFont(new Font("Arial", Font.PLAIN, 11));
		lblCopyRight.setBounds(10, 576, 178, 14);
		pOptions.add(lblCopyRight);

		JPanel pListFiltered = new JPanel();
		pAll.add(pListFiltered);
		pListFiltered.setLayout(null);

		JLabel lblMitglieder = new JLabel("Mitglieder", SwingConstants.CENTER);
		lblMitglieder.setBounds(0, 10, 200, 25);
		pListFiltered.add(lblMitglieder);

		bAdd = new JButton("Hinzufügen =>");
		bAdd.addActionListener(this);
		bAdd.setBounds(10, 566, 178, 23);
		pListFiltered.add(bAdd);

		listFiltered = new JList<>();
		listFiltered.setBounds(0, 35, 200, 525);

		JScrollPane spListFiltered = new JScrollPane();
		spListFiltered.setBounds(0, 35, 200, 525);
		spListFiltered.setViewportView(listFiltered);
		pListFiltered.add(spListFiltered);

		JPanel panel_1 = new JPanel();
		pAll.add(panel_1);
		panel_1.setLayout(null);

		JLabel lblTeilnehmer = new JLabel("Teilnehmer", SwingConstants.CENTER);
		lblTeilnehmer.setBounds(0, 10, 200, 25);
		panel_1.add(lblTeilnehmer);

		bRemove = new JButton("<= Entfernen");
		bRemove.addActionListener(this);
		bRemove.setBounds(10, 566, 178, 23);
		panel_1.add(bRemove);

		listParticipants = new JList<>();
		listParticipants.setBounds(0, 35, 200, 525);

		JScrollPane spListParticipants = new JScrollPane();
		spListParticipants.setBounds(0, 35, 200, 525);
		spListParticipants.setViewportView(listParticipants);
		panel_1.add(spListParticipants);

		dlmFiltered = new DefaultListModel<>();
		dlmParticipants = new DefaultListModel<>();
		listFiltered.setModel(dlmFiltered);
		listParticipants.setModel(dlmParticipants);
	}

	/**
	 * displays login results
	 *
	 * @param success
	 * 				true if username/password is correct
	 * 				false if username/password wrong
	 * @param user
	 * 				username that will be displayed
	 */
	private void showPassResult(boolean success, String user){
		Color colorSuccess = Color.decode("0x009900");
		Color colorFailed = Color.decode("0xCC0000");
		if(success){ //TODO: simplify
			tfUsername.setBackground(colorSuccess);
			pfPassword.setBackground(colorSuccess);
			progressBar.setString("");
			lbUser.setText("Angemeldet als "+user);
		}else{
			tfUsername.setBackground(colorFailed);
			pfPassword.setBackground(colorFailed);
			progressBar.setString("Falscher Name/Passwort.");
		}
	}

	/**
	 * updates the member and participants lists and sorts their elements
	 */
	private void updateLists(){ //TODO: simplify
		dlmFiltered.removeAllElements();

		List<NamiMitglied> l = program.getMember();
		for(NamiMitglied d : l){
			boolean bIsWlf = (d.getStufe() == Stufe.WOELFLING);
			boolean bIsJng = (d.getStufe() == Stufe.JUNGPFADFINDER);
			boolean bIsPfd = (d.getStufe() == Stufe.PFADFINDER);
			boolean bIsRvr = (d.getStufe() == Stufe.ROVER);
			//check stufe
			if(((bIsWlf && cWoelflinge.isSelected()) ||
				(bIsJng && cJungpfadfinder.isSelected()) ||
				(bIsPfd && cPfadfinder.isSelected()) ||
				(bIsRvr && cRover.isSelected()) ||
				(!bIsWlf && !bIsJng && !bIsPfd && !bIsRvr && cAndere.isSelected()))
				){
				//check Aktiv
				if( (cMitglied.isSelected()			 && d.getMitgliedstyp() == Mitgliedstyp.MITGLIED)||
					(cSchnuppermitglied.isSelected() && d.getMitgliedstyp() == Mitgliedstyp.SCHNUPPER_MITGLIED)||
					(cNichtmitglied.isSelected()	 && d.getMitgliedstyp() == Mitgliedstyp.NICHT_MITGLIED)){
					//check Name
					if((d.getVorname().toLowerCase().contains(tfFirstName.getText().toLowerCase())) &&
							d.getNachname().toLowerCase().contains(tfLastName.getText().toLowerCase())){
						dlmFiltered.addElement(d);
					}
				}
			}
		}
		//listParticipants

		dlmParticipants.removeAllElements();
		for(NamiMitglied d : program.getParticipants()){
			dlmParticipants.addElement(d);
		}
	}

	/**
	 * Listener for all elements of the GUI
	 *
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
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
				source == cNichtmitglied){
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
		if(source == rbSortByLastname) {
			program.sortBy(Program.Sortation.SORT_BY_LASTNAME);
			updateLists();
		}
		if(source == rbSortByFirstname) {
			program.sortBy(Program.Sortation.SORT_BY_FIRSTNAME);
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
				new WriterNotfallliste().run(sd.getAbsolutePath(), program.getParticipants());
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
		} catch (NamiLoginException e) {
			this.showPassResult(false, "");
			return;
		} catch (IOException e) {
			this.getProgressBar().setString("Keine Verbindung zur NaMi.");
			return;
		} catch (NamiApiException e) {
			e.printStackTrace();
		}

		bLogin.setEnabled(false);
		tfUsername.setEnabled(false);
		pfPassword.setEnabled(false);
		showPassResult(true, user);
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		updateLists();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		updateLists();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		updateLists();
	}

	@Override
	public void loaderUpdate(int percents, String info) {
		EventQueue.invokeLater(() -> {
			progressBar.setValue(percents);
			progressBar.setString(info);
			updateLists();
		});
	}

	@Override
	public  void loaderDone(long timeMS) {
		EventQueue.invokeLater(() -> {
			progressBar.setValue(100);
			progressBar.setString("Fertig nach" + timeMS / 1000 + "s.");
			updateLists();
		});
	}

	@Override
	public Gruppierung selectGruppierung(Collection<Gruppierung> gruppierungen) {
		GroupSelector gs = new GroupSelector(this, gruppierungen);
		return gs.showModal();
	}
}
