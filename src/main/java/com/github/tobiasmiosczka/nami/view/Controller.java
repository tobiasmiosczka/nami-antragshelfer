package com.github.tobiasmiosczka.nami.view;

import com.github.tobiasmiosczka.nami.applicationforms.implemented.*;
import com.github.tobiasmiosczka.nami.service.NamiService;
import com.github.tobiasmiosczka.nami.util.BrowserUtil;
import com.github.tobiasmiosczka.nami.updater.Updater;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Menu;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import nami.connector.exception.NamiException;
import nami.connector.namitypes.NamiGruppierung;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiGeschlecht;
import nami.connector.namitypes.NamiMitgliedstyp;
import nami.connector.namitypes.NamiStufe;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.chrono.ChronoLocalDate;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable, NamiService.Listener {

    private static final  Logger logger = Logger.getLogger(Controller.class.getName());

    private NamiService namiService;

    @FXML private Button fxIdBtLogin;

    @FXML private TextField fxIdTfUsername;

    @FXML private PasswordField fxIdPfPassword;

    @FXML private ProgressBar fxIdPbProgress;

    @FXML private Menu fxIdMnApplicationForms;

    @FXML private TextField fxIdTfFilterFirstname;
    @FXML private TextField fxIdTfFilterLastname;

    @FXML private CheckBox fxIdCbFilterGroupBiber;
    @FXML private CheckBox fxIdCbFilterGroupWoelflinge;
    @FXML private CheckBox fxIdCbFilterGroupJungpfadfinder;
    @FXML private CheckBox fxIdCbFilterGroupPfadfinder;
    @FXML private CheckBox fxIdCbFilterGroupRover;
    @FXML private CheckBox fxIdCbFilterGroupOther;

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
        fxIdTvMember.getColumns().setAll(NamiMitgliedTableRowUtil.getCollumns());
        fxIdTvParticipants.getColumns().setAll(NamiMitgliedTableRowUtil.getCollumns());
        fxIdTfUsername.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) this.login(); });
        fxIdPfPassword.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.ENTER) this.login(); });
        TableViewDragAndDropUtil.init(
                fxIdTvMember,
                fxIdTvParticipants,
                namiService::putMembersToParticipants,
                namiService::putParticipantsToMembers);
        ApplicationFormsMenuUtil.init(fxIdMnApplicationForms, namiService, this::onException,
                List.of(
                    ApplicationBdkjDinslaken.class,
                    GemeindeDinslakenCoronaRaumnutzungung.class,
                    ApplicationDioezeseMuenster.class,
                    ApplicationDioezeseMuensterGroupLeader.class,
                    EmergencyList.class,
                    ParticipationList.class));
        checkForUpdates(true);
    }

    @FXML
    public void login() {
        if (namiService.isLoggedIn())
            logout();
        else
            login(fxIdTfUsername.getText(), fxIdPfPassword.getText());
    }

    private void login(String username, String password) {
        try {
            namiService.login(username, password);
        } catch (NamiException e) {
            onException("Fehler beim Login", e);
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
        } catch (InterruptedException | ExecutionException e) {
            onException("Fehler beim Laden der Mitgliedsdaten", e);
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
    private void updateMembersList() {
        List<NamiMitglied> filteredMember = namiService.getMember().stream()
                .filter(this::checkFilter)
                .toList();
        List<NamiMitglied> selected = new LinkedList<>(fxIdTvMember.getSelectionModel().getSelectedItems());
        fxIdTvMember.getItems().setAll(filteredMember);
        int[] indices = selected.stream().mapToInt(fxIdTvMember.getItems()::indexOf).toArray();
        fxIdTvMember.getSelectionModel().selectIndices(-1, indices);
        fxIdTvMember.sort();
    }

    @FXML
    private void updateParticipantsList() {
        List<NamiMitglied> selected = new LinkedList<>(fxIdTvParticipants.getSelectionModel().getSelectedItems());
        fxIdTvParticipants.getItems().setAll(namiService.getParticipants());
        int [] indices = selected.stream().mapToInt(fxIdTvParticipants.getItems()::indexOf).toArray();
        fxIdTvParticipants.getSelectionModel().selectIndices(-1, indices);
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

    private void checkForUpdates(boolean silent) {
        try {
            if (Updater.updateAvailable()) {
                if (DialogUtil.showChooseDialog(
                        "Update",
                        "Update verfügbar.",
                        "Soll das Update heruntergeladen werden?"))
                    Updater.downloadUpdate();
            } else if (!silent)
                DialogUtil.showMessage("Update", null, "Kein Update verfügbar.");
        } catch (URISyntaxException | IOException e) {
            onException("Fehler beim Updateüberprüfung.", e);
        }
    }

    @FXML
    private void checkForUpdates() {
        checkForUpdates(false);
    }

    private boolean checkFilter(NamiMitglied m) {
        boolean bIsBib = (m.getStufe() == NamiStufe.BIBER),
                bIsWlf = (m.getStufe() == NamiStufe.WOELFLING),
                bIsJng = (m.getStufe() == NamiStufe.JUNGPFADFINDER),
                bIsPfd = (m.getStufe() == NamiStufe.PFADFINDER),
                bIsRvr = (m.getStufe() == NamiStufe.ROVER),
                bIsNon = !(bIsWlf || bIsJng || bIsPfd || bIsRvr);
        if (!(  (bIsBib && fxIdCbFilterGroupBiber.isSelected()) ||
                (bIsWlf && fxIdCbFilterGroupWoelflinge.isSelected()) ||
                (bIsJng && fxIdCbFilterGroupJungpfadfinder.isSelected()) ||
                (bIsPfd && fxIdCbFilterGroupPfadfinder.isSelected()) ||
                (bIsRvr && fxIdCbFilterGroupRover.isSelected()) ||
                (bIsNon && fxIdCbFilterGroupOther.isSelected())))
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
        Platform.runLater(() -> fxIdPbProgress.setProgress((double) current / (double) count));
    }

    @Override
    public void onDone(long timeMS) {
        logger.log(Level.FINE, "Done loading after " + timeMS + "ms.");
    }

    @Override
    public void onException(String message, Throwable e) {
        Platform.runLater(() -> DialogUtil.showError(message, e));
        e.printStackTrace();
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
        return DialogUtil.showChoiceDialog(
                groups,
                "Gruppierung wählen",
                "Wählen sie eine Gruppierung:",
                null);
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
