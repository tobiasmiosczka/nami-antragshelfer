package com.github.tobiasmiosczka.nami.view;

import com.github.tobiasmiosczka.nami.applicationforms.DocumentWriter;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Form;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Option;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Participants;
import com.github.tobiasmiosczka.nami.applicationforms.annotations.Training;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import nami.connector.namitypes.NamiBaustein;
import nami.connector.namitypes.NamiMitglied;
import nami.connector.namitypes.NamiSchulung;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class ApplicationFormsMenuUtil {

    public static void init(Menu menu, Supplier<List<NamiMitglied>> participantsSupplier, Supplier<Map<NamiMitglied, Map<NamiBaustein, NamiSchulung>>> trainingsSupplier, BiConsumer<String, Throwable> exceptionConsumer, List<Class<? extends DocumentWriter>> writers) {
        writers.stream()
                .map(e -> buildMenuItem(participantsSupplier, trainingsSupplier, exceptionConsumer, e))
                .forEach(item -> menu.getItems().add(item));
    }

    private static <T extends DocumentWriter> T genWriter(Constructor<T> constructor, Supplier<List<NamiMitglied>> participantsSupplier, Supplier<Map<NamiMitglied, Map<NamiBaustein, NamiSchulung>>> trainingsSupplier, List<Object> options) throws Exception {
        int iParameter = 0, iOption = 0;
        Object[] constructorParameters = new Object[constructor.getParameterCount()];
        for (Parameter c : constructor.getParameters()) {
            if (c.getAnnotation(Training.class) != null)
                constructorParameters[iParameter++] = trainingsSupplier.get();
            if (c.getAnnotation(Option.class) != null)
                constructorParameters[iParameter++] = options.get(iOption++);
            if (c.getAnnotation(Participants.class) != null)
                constructorParameters[iParameter++] = participantsSupplier.get();
        }
        return constructor.newInstance(constructorParameters);
    }

    private static <T extends DocumentWriter> CustomDialog buildCustomDialog(Constructor<T> constructor) {
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

    private static <T extends DocumentWriter> MenuItem buildMenuItem(Supplier<List<NamiMitglied>> participantsSupplier, Supplier<Map<NamiMitglied, Map<NamiBaustein, NamiSchulung>>> trainingsSupplier, BiConsumer<String, Throwable> consumer, Class<T> c) {
        String title = c.getAnnotation(Form.class).title();
        Constructor<T> constructor = (Constructor<T>) c.getConstructors()[0];
        CustomDialog customDialog = buildCustomDialog(constructor);
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
                genWriter(constructor, participantsSupplier, trainingsSupplier, options).run(new FileOutputStream(file));
            } catch (Exception e) {
                consumer.accept("Fehler beim Generieren von \"" + title + "\"", e);
            }
        });
        return item;
    }

}
