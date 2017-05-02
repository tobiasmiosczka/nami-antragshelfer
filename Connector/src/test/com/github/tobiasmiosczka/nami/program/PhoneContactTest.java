package com.github.tobiasmiosczka.nami.program;

import org.junit.Before;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;

public class PhoneContactTest {

    PhoneContact[] contactsArray;

    @Before
    public void initialize() {
        String string = "01234 56789;" +
                "01234 56789 (Max);" +
                "01234 56789 (Max Mustermann);" +
                " 01234 56789  ( Max Mustermann ) ;" +
                "01234 56789 (Max Mustermann(Vater));" +
                "01234 56789 ()";

        Collection<PhoneContact> contactsCollection = PhoneContact.getPhoneContacts(string);
        contactsArray = contactsCollection.toArray(new PhoneContact[contactsCollection.size()]);
    }

    @Test
    public void getPhoneContacts() throws Exception {
        assertEquals("01234 56789", contactsArray[0].getPhoneNumber());
        assertEquals(null, contactsArray[0].getName());

        assertEquals("01234 56789", contactsArray[1].getPhoneNumber());
        assertEquals("Max", contactsArray[1].getName());

        assertEquals("01234 56789", contactsArray[2].getPhoneNumber());
        assertEquals("Max Mustermann", contactsArray[2].getName());

        assertEquals("01234 56789", contactsArray[3].getPhoneNumber());
        assertEquals("Max Mustermann", contactsArray[3].getName());

        assertEquals("01234 56789", contactsArray[4].getPhoneNumber());
        assertEquals("Max Mustermann(Vater)", contactsArray[4].getName());

        assertEquals("01234 56789", contactsArray[5].getPhoneNumber());
        assertEquals("", contactsArray[5].getName());
    }
}