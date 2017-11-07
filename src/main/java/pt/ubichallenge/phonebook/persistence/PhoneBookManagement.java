package pt.ubichallenge.phonebook.persistence;

import pt.ubichallenge.phonebook.model.Contact;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@Stateless
public class PhoneBookManagement {

    @PersistenceContext(unitName="PhoneBookService", type= PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public void addContact(Contact contact){
        entityManager.persist(contact);
    }

    public List<Contact> getContacts(){
        @SuppressWarnings("unchecked")
        List<Contact> contacts = entityManager.createNamedQuery("findAllContacts").getResultList();
        return contacts;
    }

    public Contact getContact(long id) {
        return entityManager.find(Contact.class, id);
    }

    public String deleteContact(long id) {
        Contact contact = getContact(id);
        if(contact != null) {
            entityManager.remove(contact);
            return "Successful deletion.";
        }
        else {
            return "Contact not found.";
        }

    }

    public String updateContact(long id, Contact updatedContact) {
        Contact contact = getContact(id);
        if(contact == null)
            return "No contact with the specified id.";
        if(updatedContact.getName() != null)
            contact.setName(updatedContact.getName());
        if(updatedContact.getAddress() != null)
            contact.setAddress(updatedContact.getAddress());
        if(updatedContact.getPhones() != null)
            contact.setPhones(updatedContact.getPhones());
        return "Update successful";
    }
}
