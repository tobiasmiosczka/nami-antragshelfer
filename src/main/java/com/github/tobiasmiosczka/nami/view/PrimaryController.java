package com.github.tobiasmiosczka.nami.view;

import com.github.tobiasmiosczka.nami.service.NamiServiceListener;
import com.github.tobiasmiosczka.nami.service.NamiService;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import nami.connector.NamiServer;
import nami.connector.exception.NamiApiException;
import nami.connector.exception.NamiLoginException;
import nami.connector.namitypes.NamiGruppierung;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.enums.NamiGeschlecht;
import nami.connector.namitypes.enums.NamiMitgliedstyp;
import nami.connector.namitypes.enums.NamiStufe;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;

public class PrimaryController implements NamiServiceListener {

    private NamiService namiService = new NamiService(this);

    @FXML private Button fxIdBtLogin;
    @FXML private Button fxIdBtLogout;

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

    @FXML private TableView<NamiMitglied> fxIdTvMember;

    @FXML
    public void login(ActionEvent actionEvent) {
        this.login(fxIdTfUsername.getText(), fxIdPfPassword.getText());
    }

    private void login(String username, String password) {
        try {
            namiService.login(username, password, NamiServer.getLiveserver());
        } catch (NamiLoginException e) {
            fxIdPfPassword.setStyle("-fx-background-color: #510000");
            fxIdTfUsername.setStyle("-fx-background-color: #510000");
            e.printStackTrace();
            return;
        } catch (IOException e) {
            //TODO: implement exception handling
            e.printStackTrace();
            return;
        }
        fxIdTfUsername.setDisable(true);
        fxIdBtLogin.setDisable(true);
        fxIdBtLogin.setVisible(false);
        fxIdBtLogout.setVisible(true);
        fxIdBtLogout.setDisable(false);
        fxIdPfPassword.setText("");
        fxIdPfPassword.setDisable(true);
        fxIdPfPassword.setStyle("");
        fxIdTfUsername.setStyle("");
        try {
            namiService.loadData(true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NamiApiException e) {
            e.printStackTrace();
        }
    }

    private void updateMembersList() {
        List<NamiMitglied> member = namiService.getMember();
                
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
        return true;
    }

    @FXML
    private void logout() {
        fxIdTfUsername.setDisable(false);
        fxIdBtLogin.setDisable(false);
        fxIdBtLogin.setVisible(true);
        fxIdBtLogout.setVisible(false);
        fxIdBtLogout.setDisable(true);
        fxIdPfPassword.setText("");
        fxIdPfPassword.setDisable(false);
        this.namiService = new NamiService(this);
    }

    @Override
    public void onMemberLoaded(int current, int count, NamiMitglied member) {
        System.out.println(member.getVorname() + " " + member.getNachname());
        Platform.runLater(() -> {
            System.out.println(member.getVorname() + " " + member.getNachname());
            fxIdPbProgress.setProgress((double)current / (double)count);
            updateMembersList();
        });
    }

    @Override
    public void onDone(long timeMS) {
        //TODO: implement
    }

    @Override
    public void onException(String message, Exception e) {
        //TODO: implement
    }

    @Override
    public NamiGruppierung selectGroup(Collection<NamiGruppierung> groups) {
        //TODO: implement
        return null;
    }

    public void openUrl(String url) {
        try {
            Desktop desktop = java.awt.Desktop.getDesktop();
            URI oURL = new URI(url);
            desktop.browse(oURL);
        } catch (Exception e) {
            onException("Fehler beim Öffnen einer URL. ", e);
        }
    }

    @FXML
    public void openHomepage(ActionEvent actionEvent) {
        openUrl("https://github.com/TobiasMiosczka/NaMiAntragshelfer");
    }

    @FXML
    public void openLicence(ActionEvent actionEvent) {
        openUrl("https://github.com/TobiasMiosczka/NaMiAntragshelfer/blob/master/LICENSE");
    }

    @FXML
    public void openChangelog(ActionEvent actionEvent) {
        openUrl("https://github.com/TobiasMiosczka/NaMiAntragshelfer/blob/master/CHANGELOG.md");
    }
}
