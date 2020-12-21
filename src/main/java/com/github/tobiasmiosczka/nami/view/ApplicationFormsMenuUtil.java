package com.github.tobiasmiosczka.nami.view;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Training;
import com.github.tobiasmiosczka.nami.service.NamiService;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.util.List;

public class ApplicationFormsMenuUtil {

    public interface ErrorConsumer {
        void onException(String message, Exception e);
    }

    public static void init(Menu menu, NamiService service, ErrorConsumer consumer, List<Class<? extends DocumentWriter>> writers) {
        for (Class<? extends  DocumentWriter> dClass : writers)
            add(service, menu, consumer, dClass);
    }

    private static DocumentWriter genWriter(Constructor<?> constructor, NamiService service, List<Object> options) throws Exception {
        int iParameter = 0, iOption = 0;
        Object[] constructorParameters = new Object[constructor.getParameterCount()];
        for (Parameter c : constructor.getParameters()) {
            if (c.getAnnotation(Training.class) != null)
                constructorParameters[iParameter++] = service.loadSchulungen(service.getParticipants());
            if (c.getAnnotation(Option.class) != null)
                constructorParameters[iParameter++] = options.get(iOption++);
        }
        return (DocumentWriter) constructor.newInstance(constructorParameters);
    }

    private static CustomDialog getCustomDialog(Constructor<?> constructor) {
        CustomDialog customDialog = new CustomDialog()
                .setTitle("Optionen")
                .setHeaderText("Dokumentenoptionen:");
        boolean hasOption = false;
        for (Parameter parameter : constructor.getParameters()) {
            Option option = parameter.getAnnotation(Option.class);
            if (option != null) {
                hasOption = true;
                if (parameter.getType() == String.class)
                    customDialog.addStringOption(option.title());
                if (parameter.getType() == LocalDate.class)
                    customDialog.addDateOption(option.title());
                if (parameter.getType() == boolean.class)
                    customDialog.addBooleanOption(option.title());
            }
        }
        return hasOption ? customDialog : null;
    }

    public static void add(NamiService service, Menu menu, ErrorConsumer consumer, Class<? extends DocumentWriter> c) {
        String title = c.getAnnotation(Form.class).title();
        Constructor<?> constructor = c.getConstructors()[0];
        CustomDialog customDialog = getCustomDialog(constructor);
        MenuItem item = new MenuItem(title);
        item.setOnAction(event -> {
            List<Object> options = null;
            if (customDialog != null)
                options = customDialog.getResult();
            if (customDialog != null && options == null)
                return;
            File file = DialogUtil.showSaveDialog();
            if (file == null)
                return;
            try {
                genWriter(constructor, service, options).run(new FileOutputStream(file), service.getParticipants());
            } catch (Exception e) {
                consumer.onException("Fehler beim Generieren von \"" + title + "\"", e);
            }
        });
        menu.getItems().add(item);
    }

}
