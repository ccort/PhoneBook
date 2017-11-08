package pt.ubichallenge.phonebook.util;

import pt.ubichallenge.phonebook.model.Address;
import pt.ubichallenge.phonebook.model.Contact;
import pt.ubichallenge.phonebook.model.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneBookUtils {
    static public Contact createSampleContact(){
        Contact contact = new Contact();
        contact.setName("FirstName LastName");

        Address address = new Address();
        address.setAddress("SampleAddress");

        contact.setAddress(address);

        Phone phone = new Phone();
        phone.setNumber("910000000");

        List<Phone> phones = new ArrayList<>();
        phones.add(phone);

        phone = new Phone();
        phone.setNumber("931112233");

        phones.add(phone);

        contact.setPhones(phones);

        return contact;
    }
}
