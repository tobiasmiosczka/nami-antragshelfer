module nami.antragshelfer {
    requires nami.connector;
    requires org.docx4j.core;
    requires javafx.controls;
    requires javafx.fxml;

    opens com.github.tobiasmiosczka.nami.view;
    opens com.github.tobiasmiosczka.nami.updater;
    opens gui;
    opens forms;
}