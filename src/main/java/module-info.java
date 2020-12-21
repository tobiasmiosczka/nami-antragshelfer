module nami.antragshelfer {
    requires nami.connector;
    requires org.docx4j.core;
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;

    requires jdk.crypto.ec;

    opens com.github.tobiasmiosczka.nami.view;
    opens com.github.tobiasmiosczka.nami.updater;
    opens gui;
    opens forms;
}