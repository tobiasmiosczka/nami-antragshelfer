package com.github.tobiasmiosczka.nami.view;

import com.github.tobiasmiosczka.nami.applicationforms.WriterApplicationCityDinslaken;
import com.github.tobiasmiosczka.nami.applicationforms.WriterApplicationDioezeseMuenster;
import com.github.tobiasmiosczka.nami.applicationforms.WriterApplicationDioezeseMuensterGroupLeader;
import com.github.tobiasmiosczka.nami.applicationforms.WriterEmergencyList;
import com.github.tobiasmiosczka.nami.applicationforms.WriterGemeindeDinslakenCoronaRaumnutzungung;
import com.github.tobiasmiosczka.nami.applicationforms.WriterParticipationList;
import com.github.tobiasmiosczka.nami.service.NamiServiceListener;
import com.github.tobiasmiosczka.nami.service.NamiService;
import com.github.tobiasmiosczka.nami.util.BrowserUtil;
import com.github.tobiasmiosczka.nami.util.UpdateUtil;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import nami.connector.NamiServer;
import nami.connector.exception.NamiApiException;
import nami.connector.exception.NamiLoginException;
import nami.connector.namitypes.NamiGruppierung;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSchulungenMap;
import nami.connector.namitypes.enums.NamiGeschlecht;
import nami.connector.namitypes.enums.NamiMitgliedstyp;
import nami.connector.namitypes.enums.NamiStufe;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class Controller implements Initializable, NamiServiceListener {

    private NamiService namiService;

    @FXML private Button fxIdBtLogin;

    @FXML private TextField fxIdTfUsername;

    @FXML private PasswordField fxIdPfPassword;

    @FXML private ProgressBar fxIdPbProgress;

    @FXML private TextField fxIdTfFilterFirstname;
    @FXML private TextField fxIdTfFilterLastname;

    @FXML private CheckBox fxIdCbFilterStufeWoelflinge;
    @FXML private CheckBox fxIdCbFilterStufeJungpfadfinder;
    @FXML private CheckBox fxIdCbFilterStufePfadfinder;
    @FXML private CheckBox fxIdCbFilterStufeRover;
    @FXML private CheckBox fxIdCbFilterStufeOther;

    @FXML private CheckBox fxIdCbFilterGenderMale;
    @FXML private CheckBox fxIdCbFilterGenderFemale;
    @FXML private CheckBox fxIdCbFilterGenderOther;

    @FXML private CheckBox fxIdCbFilterMemberTypeMember;
    @FXML private CheckBox fxIdCbFilterMemberTypeTryout;
    @FXML private CheckBox fxIdCbFilterMemberTypeNonMember;

    @FXML private DatePicker fxIdDpFilterBirthdateFrom;
    @FXML private DatePicker fxIdDpFilterBirthdateTo;

    @FXML private TableView<NamiMitglied> fxIdTvMember;
    @FXML private TableView<NamiMitglied> fxIdTvParticipants;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        namiService = new NamiService(this);
        fxIdTvMember.getColumns().setAll(NamiMitgliedTableUtil.getCollumns());
        fxIdTvMember.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fxIdTvParticipants.getColumns().setAll(NamiMitgliedTableUtil.getCollumns());
        fxIdTvParticipants.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        fxIdTfUsername.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                login();
        });
        fxIdPfPassword.setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER)
                login();
        });
        checkForUpdatesSilent();
    }

    @FXML
    public void login() {
        if (namiService.isLoggedIn())
            logout();
        else
            this.login(fxIdTfUsername.getText(), fxIdPfPassword.getText());
    }

    private void login(String username, String password) {
        try {
            namiService.login(username, password, NamiServer.getLiveserver());
        } catch (NamiLoginException e) {
            onException("Fehler beim Login", e);
            e.printStackTrace();
            return;
        } catch (IOException e) {
            onException("Netzwerkfehler", e);
            e.printStackTrace();
            return;
        }
        fxIdTfUsername.setDisable(true);
        fxIdPfPassword.setText("");
        fxIdPfPassword.setDisable(true);
        fxIdPfPassword.setStyle("");
        fxIdTfUsername.setStyle("");
        fxIdBtLogin.setText("Logout");
        try {
            namiService.loadData(true);
        } catch (IOException | NamiApiException e) {
            onException("Fehler beim Laden der Mitgliedsdaten", e);
            e.printStackTrace();
        }
    }

    private void logout() {
        this.namiService = new NamiService(this);
        fxIdTfUsername.setDisable(false);
        fxIdBtLogin.setText("Login");
        fxIdPfPassword.setText("");
        fxIdPfPassword.setDisable(false);
    }

    @FXML
    private void genAppRaumnutzungsplanGemeindeDinslaken() {
        List<Object> options = new CustomDialog()
                .setTitle("Optionen")
                .addDateOption("Datum")
                .addStringOption("Von (HH:MM)")
                .addStringOption("Bis (HH:MM)")
                .getResult();
        if (options == null)
            return;
        File file = DialogUtil.showSaveDialog(null, null);
        if (file == null)
            return;
        WriterGemeindeDinslakenCoronaRaumnutzungung writer = new WriterGemeindeDinslakenCoronaRaumnutzungung(
                (LocalDate)options.get(0),
                (String)options.get(1),
                (String)options.get(2));
        try {
            writer.run(
                    new FileOutputStream(file),
                    fxIdTvParticipants.getItems());
        } catch (Exception e) {
            onException("Fehler beim Generieren", e);
            e.printStackTrace();
        }
    }

    @FXML
    private void genAppStadtDinslaken() {
        List<Object> options = new CustomDialog()
                .setTitle("Optionen")
                .addStringOption("Maßnahme")
                .addDateOption("Datum (von)")
                .addDateOption("Datum (bis)")
                .addStringOption("Ort")
                .getResult();
        if (options == null)
            return;
        File file = DialogUtil.showSaveDialog(null, null);
        if (file == null)
            return;
        WriterApplicationCityDinslaken writer = new WriterApplicationCityDinslaken(
                (String) options.get(0),
                (LocalDate) options.get(1),
                (LocalDate) options.get(2),
                (String) options.get(3));
        try {
            writer.run(
                    new FileOutputStream(file),
                    fxIdTvParticipants.getItems());
        } catch (Exception e) {
            onException("Fehler beim Generieren", e);
            e.printStackTrace();
        }
    }

    @FXML
    private void genAppDioezeseMuenster() {
        List<Object> options = new CustomDialog()
                .setTitle("Optionen")
                .addStringOption("Mitgliedsverband")
                .addStringOption("Träger")
                .addDateOption("Datum (von)")
                .addDateOption("Datum (bis)")
                .addStringOption("PLZ")
                .addStringOption("Ort")
                .addStringOption("Land")

                .addBooleanOption("Freizeit")
                .addBooleanOption("Bildung")
                .addBooleanOption("Aus- und Weiterbildung")
                .addBooleanOption("Qualitätssicherung")
                .addBooleanOption("Großveranstaltung")
                .getResult();
        if (options == null)
            return;
        File file = DialogUtil.showSaveDialog(null, null);
        if (file == null)
            return;
        WriterApplicationDioezeseMuenster writer = new WriterApplicationDioezeseMuenster(
                (String) options.get(0),
                (String) options.get(1),
                (LocalDate) options.get(2),
                (LocalDate) options.get(3),
                (String) options.get(4),
                (String) options.get(5),
                (String) options.get(6),
                (Boolean) options.get(7),
                (Boolean) options.get(8),
                (Boolean) options.get(9),
                (Boolean) options.get(10),
                (Boolean) options.get(11));
        try {
            writer.run(
                    new FileOutputStream(file),
                    fxIdTvParticipants.getItems());
        } catch (Exception e) {
            onException("Fehler beim Generieren", e);
            e.printStackTrace();
        }
    }

    @FXML
    private void genAppDioezeseMuensterLeader() {
        List<Object> options = new CustomDialog()
                .setTitle("Optionen")
                .addDateOption("Datum")
                .getResult();
        if (options == null)
            return;
        File file = DialogUtil.showSaveDialog(null, null);
        if (file == null)
            return;
        try {
            List<NamiSchulungenMap> training = namiService.loadSchulungen(fxIdTvParticipants.getItems());
            WriterApplicationDioezeseMuensterGroupLeader writer = new WriterApplicationDioezeseMuensterGroupLeader(
                    training,
                    (LocalDate) options.get(0));
            writer.run(
                    new FileOutputStream(file),
                    fxIdTvParticipants.getItems());
        } catch (Exception e) {
            onException("Fehler beim Generieren", e);
            e.printStackTrace();
        }
    }

    @FXML
    private void genEmergencyList() {
        WriterEmergencyList writer = new WriterEmergencyList();
        File file = DialogUtil.showSaveDialog(null, null);
        if (file == null)
            return;
        try {
            writer.run(
                    new FileOutputStream(file),
                    fxIdTvParticipants.getItems());
        } catch (Exception e) {
            onException("Fehler beim Generieren", e);
            e.printStackTrace();
        }
    }

    @FXML
    private void genParticipantsList() {
        WriterParticipationList writer = new WriterParticipationList();
        File file = DialogUtil.showSaveDialog(null, null);
        if (file == null)
            return;
        try {
            writer.run(
                    new FileOutputStream(file),
                    fxIdTvParticipants.getItems());
        } catch (Exception e) {
            onException("Fehler beim Generieren", e);
            e.printStackTrace();
        }
    }

    @FXML
    private void updateMembersList() {
        List<NamiMitglied> filteredMember = namiService.getMember().stream()
                .filter(this::checkFilter)
                .collect(Collectors.toList());
        List<NamiMitglied> selected = new LinkedList<>(fxIdTvMember.getSelectionModel().getSelectedItems());
        fxIdTvMember.getItems().setAll(filteredMember);
        int[] indices = selected.stream().mapToInt(fxIdTvMember.getItems()::indexOf).toArray();
        fxIdTvMember.getSelectionModel().selectIndices(-1, indices);
        fxIdTvMember.sort();
    }

    private void updateParticipantsList() {
        List<NamiMitglied> participants = namiService.getParticipants();
        fxIdTvParticipants.getItems().setAll(participants);
        fxIdTvParticipants.sort();
    }

    @FXML
    private void addParticipants() {
        namiService.putMembersToParticipants(fxIdTvMember.getSelectionModel().getSelectedItems());
    }

    @FXML
    private void removeParticipants() {
        namiService.putParticipantsToMembers(fxIdTvParticipants.getSelectionModel().getSelectedItems());
    }

    private void checkForUpdatesSilent() {
        try {
            if (UpdateUtil.updateAvailable()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION,
                        "Update verfügbar.",
                        ButtonType.OK,
                        ButtonType.CANCEL);
                alert.setTitle("Update");
                alert.setHeaderText("Update verfügbar.");
                alert.setContentText("Soll das Update heruntergeladen werden?");
                if (alert.showAndWait().orElse(ButtonType.CLOSE) == ButtonType.OK)
                    UpdateUtil.downloadUpdate();
            }
        } catch (URISyntaxException | IOException e) {
            onException("Fehler beim Updateüberprüfung.", e);
            e.printStackTrace();
        }
    }

    @FXML
    private void checkForUpdates() {
        try {
            if (UpdateUtil.updateAvailable())
                checkForUpdatesSilent();
            else
                DialogUtil.showMessage(Alert.AlertType.INFORMATION, "Update", null, "Kein Update verfügbar.");
        } catch (IOException e) {
            onException("Fehler beim Updateüberprüfung.", e);
            e.printStackTrace();
        }
    }

    private boolean checkFilter(NamiMitglied m) {
        boolean bIsWlf = (m.getStufe() == NamiStufe.WOELFLING),
                bIsJng = (m.getStufe() == NamiStufe.JUNGPFADFINDER),
                bIsPfd = (m.getStufe() == NamiStufe.PFADFINDER),
                bIsRvr = (m.getStufe() == NamiStufe.ROVER),
                bIsNon = !(bIsWlf || bIsJng || bIsPfd || bIsRvr);
        if (!(  (bIsWlf && fxIdCbFilterStufeWoelflinge.isSelected()) ||
                (bIsJng && fxIdCbFilterStufeJungpfadfinder.isSelected()) ||
                (bIsPfd && fxIdCbFilterStufePfadfinder.isSelected()) ||
                (bIsRvr && fxIdCbFilterStufeRover.isSelected()) ||
                (bIsNon && fxIdCbFilterStufeOther.isSelected())))
            return false;
        if (!(  (fxIdCbFilterMemberTypeMember.isSelected()	    && m.getMitgliedstyp() == NamiMitgliedstyp.MITGLIED) ||
                (fxIdCbFilterMemberTypeTryout.isSelected()      && m.getMitgliedstyp() == NamiMitgliedstyp.SCHNUPPER_MITGLIED) ||
                (fxIdCbFilterMemberTypeNonMember.isSelected()	&& m.getMitgliedstyp() == NamiMitgliedstyp.NICHT_MITGLIED)))
            return false;
        if (!(  (fxIdCbFilterGenderFemale.isSelected()  && m.getGeschlecht() == NamiGeschlecht.WEIBLICH) ||
                (fxIdCbFilterGenderMale.isSelected()    && m.getGeschlecht() == NamiGeschlecht.MAENNLICH) ||
                (fxIdCbFilterGenderOther.isSelected()   && m.getGeschlecht() == NamiGeschlecht.KEINE_ANGABE)))
            return false;
        if (    !(m.getVorname().toLowerCase().contains(fxIdTfFilterFirstname.getText().toLowerCase())) ||
                !(m.getNachname().toLowerCase().contains(fxIdTfFilterLastname.getText().toLowerCase())))
            return false;
        return (fxIdDpFilterBirthdateFrom.getValue() == null || !fxIdDpFilterBirthdateFrom.getValue().isAfter(ChronoLocalDate.from(m.getGeburtsDatum()))) &&
                (fxIdDpFilterBirthdateTo.getValue() == null || !fxIdDpFilterBirthdateTo.getValue().isBefore(ChronoLocalDate.from(m.getGeburtsDatum())));
    }

    @Override
    public void onMemberLoaded(int current, int count, NamiMitglied member) {
        Platform.runLater(() -> fxIdPbProgress.setProgress((double)current / (double)count));
    }

    @Override
    public void onDone(long timeMS) {
        //TODO: implement
    }

    @Override
    public void onException(String message, Exception e) {
        Platform.runLater(() -> DialogUtil.showError(message, e));
    }

    @Override
    public void onMemberListUpdated() {
        Platform.runLater(this::updateMembersList);
    }

    @Override
    public void onParticipantsListUpdated() {
        Platform.runLater(this::updateParticipantsList);
    }

    @Override
    public NamiGruppierung selectGroup(Collection<NamiGruppierung> groups) {
        ChoiceDialog<NamiGruppierung> dialog = new ChoiceDialog<>(null, groups);
        dialog.setTitle("Gruppierung");
        dialog.setHeaderText("Gruppierung auswählen");
        return dialog.showAndWait().orElse(null);
    }

    public void openUrl(String url) {
        try {
            BrowserUtil.openUrl(url);
        } catch (Exception e) {
            onException("Fehler beim Öffnen einer URL. ", e);
        }
    }

    @FXML
    public void openHomepage() {
        openUrl("https://github.com/TobiasMiosczka/NaMiAntragshelfer");
    }

    @FXML
    public void openLicence() {
        openUrl("https://github.com/TobiasMiosczka/NaMiAntragshelfer/blob/master/LICENSE");
    }

    @FXML
    public void openChangelog() {
        openUrl("https://github.com/TobiasMiosczka/NaMiAntragshelfer/blob/master/CHANGELOG.md");
    }
}
