package com.github.tobiasmiosczka.nami.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneContact {

    private static final Pattern NAME_PATTERN = Pattern.compile("\\((?<name>.*?)\\)");
    private final String phoneNumber;
    private final String name;

    public PhoneContact(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }

    public String getName() {
            return name;
        }

    public String getPhoneNumber() {
            return phoneNumber;
        }

    public String toString() {
        return ((name == null) ? "" : name + ": ") + phoneNumber;
    }

    private static PhoneContact getPhoneContact(String string) {
        Matcher matcher = NAME_PATTERN.matcher(string);
        if (matcher.find()) {
            String name = matcher.group("name").trim();
            String number = matcher.replaceFirst("").trim();
            return new PhoneContact(name, number);
        }
        return new PhoneContact(null, string.trim());
    }

    public static List<PhoneContact> getPhoneContacts(String string){
        if (string == null || string.isBlank())
            return new LinkedList<>();
        return Arrays.stream(string.split(";"))
                .filter(s -> s != null && !s.isBlank())
                .map(PhoneContact::getPhoneContact)
                .toList();
    }
}