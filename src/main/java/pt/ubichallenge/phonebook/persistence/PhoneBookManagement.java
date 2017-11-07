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
}
