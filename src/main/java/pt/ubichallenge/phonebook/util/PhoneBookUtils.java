package pt.ubichallenge.phonebook.util;

import pt.ubichallenge.phonebook.model.Address;
import pt.ubichallenge.phonebook.model.Contact;
import pt.ubichallenge.phonebook.model.Phone;

import java.util.ArrayList;
import java.util.List;

public class PhoneBookUtils {

    // Returns a message in JSON format
    static public String createReturnMessageJson(String message){
        ReturnMessageJson json = new ReturnMessageJson();
        json.setMessage(message);
        return json.toJson();
    }

    // Returns a message and URI in JSON format
    static public String createReturnMessageJson(String message, String uri){
        ReturnMessageJson json = new ReturnMessageJson();
        json.setMessage(message);
        json.setUri(uri);
        return json.toJson();
    }

    // Checks if the contact contains null values or empty fields
    static public boolean isContactValid(Contact contact){
        if(contact.getName() == null || contact.getAddress() == null || contact.getPhones() == null){
            return false;
        }
        if(contact.getAddress().getAddress() == null || contact.getPhones().size() == 0){
            return false;
        }
        if(isStringEmpty(contact.getName()) || isStringEmpty(contact.getAddress().getAddress())){
            return false;
        }

        if(!arePhonesValid(contact.getPhones())){
            return false;
        }

        return true;
    }

    // Checks if the phones contain null values or empty fields
    // I omitted the verification of the phone numbers content, e.g. if they are indeed numbers or not
    static public boolean arePhonesValid(List<Phone> phones){
        if(phones.size() == 0)
            return false;
        for (Phone phone: phones) {
            if(phone.getNumber() == null) {
                return false;
            }
            if(isStringEmpty(phone.getNumber())){
                return false;
            }
        }

        return true;
    }

    static public boolean isStringEmpty(String string){
        return string.compareTo("") == 0;
    }


    // Creation of a sample contact mainly to use in tests
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
