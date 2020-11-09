package com.github.tobiasmiosczka.nami.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PhoneContact {
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
        String name = null;
        int iOpenBracket = string.indexOf('(');
        int iCloseBracket = string.lastIndexOf(')');
        if(iOpenBracket >= 0 && iCloseBracket >= 0 && iCloseBracket > iOpenBracket) {
            name = string.substring(iOpenBracket + 1, iCloseBracket).trim();
            string = string.replace( '(' + name + ')', "");
        }
        return new PhoneContact(name, string.trim());
    }

    public static List<PhoneContact> getPhoneContacts(String string){
        if (string == null || string.isBlank())
            return new LinkedList<>();
        return Arrays.stream(string.split(";"))
                .filter(s -> s != null && !s.isBlank())
                .map(PhoneContact::getPhoneContact)
                .collect(Collectors.toList());
    }
}