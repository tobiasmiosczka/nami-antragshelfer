module nami.antragshelfer {
    requires transitive nami.connector;
    requires transitive javafx.base;
    requires transitive javafx.graphics;
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
    requires transitive java.desktop;
    requires org.docx4j.core;

    opens com.github.tobiasmiosczka.nami.view;
    opens com.github.tobiasmiosczka.nami.updater;
    opens gui;
    opens forms;
}