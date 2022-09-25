package com.github.tobiasmiosczka.nami.model;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PhoneContactTest {

    @Test
    public void testGetPhoneContactsWithSingleContactWithNameShouldReturnSingleContactWithName() {
        //arrange
        String input = "123456789 (Max Mustermann)";

        //act
        List<PhoneContact> result = PhoneContact.getPhoneContacts(input);

        //assert
        assertEquals(1, result.size());
        assertEquals("Max Mustermann", result.get(0).getName());
        assertEquals("123456789", result.get(0).getPhoneNumber());
    }

    @Test
    public void testGetPhoneContactsWithSingleContactWithoutNameShouldReturnSingleContactWithoutName() {
        //arrange
        String input = "123456789 ";

        //act
        List<PhoneContact> result = PhoneContact.getPhoneContacts(input);

        //assert
        assertEquals(1, result.size());
        assertNull(result.get(0).getName());
        assertEquals("123456789", result.get(0).getPhoneNumber());
    }

    @Test
    public void testGetPhoneContactsWithTwoContactWithNameShouldReturnTwoContactWithName() {
        //arrange
        String input = "123456789 (Max Mustermann); 987654321 (Maria Mustermann)";

        //act
        List<PhoneContact> result = PhoneContact.getPhoneContacts(input);

        //assert
        assertEquals(2, result.size());
        assertEquals("Max Mustermann", result.get(0).getName());
        assertEquals("123456789", result.get(0).getPhoneNumber());
        assertEquals("Maria Mustermann", result.get(1).getName());
        assertEquals("987654321", result.get(1).getPhoneNumber());
    }
}