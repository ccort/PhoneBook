package pt.ubichallenge.phonebook.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ubichallenge.phonebook.model.Contact;
import pt.ubichallenge.phonebook.services.PhoneBookService;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import java.util.List;

@Stateless
public class PhoneBookManagement {

    private static final Logger logger = LoggerFactory.getLogger(PhoneBookManagement.class);

    @PersistenceContext(unitName="PhoneBookService", type= PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public long addContact(Contact contact){
        logger.info("Adding new contact...");
        entityManager.persist(contact);
        entityManager.flush();
        logger.info("Contact added with id: " + contact.getId());
        return contact.getId();
    }

    public List<Contact> getContacts(){
        logger.info("Getting all contacts");
        @SuppressWarnings("unchecked")
        List<Contact> contacts = entityManager.createNamedQuery("findAllContacts").getResultList();
        return contacts;
    }

    public Contact getContact(long id) {
        logger.info("Getting contact with id: " + id);
        return entityManager.find(Contact.class, id);
    }

    public boolean deleteContact(long id) {
        Contact contact = getContact(id);
        if(contact != null) {
            logger.info("Deleting contact with id: " + id);
            entityManager.remove(contact);
            return true;
        }
        else {
            logger.info("Contact not found: " + id + " no deletion performed");
            return false;
        }
    }

    public Contact updateContact(long id, Contact updatedContact) {
        Contact contact = getContact(id);
        if(contact == null) {
            logger.info("Can't update, contact not found, id:" + id);
            return null;
        }
        if(updatedContact.getName() != null)
            contact.setName(updatedContact.getName());
        if(updatedContact.getAddress() != null)
            contact.setAddress(updatedContact.getAddress());
        if(updatedContact.getPhones() != null)
            contact.setPhones(updatedContact.getPhones());
        logger.info("Contact updated, id: " + id);
        return contact;
    }
}
