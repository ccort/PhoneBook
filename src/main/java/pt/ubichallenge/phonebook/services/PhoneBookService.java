package pt.ubichallenge.phonebook.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ubichallenge.phonebook.model.Contact;
import pt.ubichallenge.phonebook.persistence.PhoneBookManagement;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Stateless
@LocalBean
@Path("/phonebook")
public class PhoneBookService {

    private static final Logger logger = LoggerFactory.getLogger(PhoneBookService.class);

    @EJB
    private PhoneBookManagement managePhoneBook;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Contact> getAllContacts(){
        return managePhoneBook.getContacts();
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Contact getContact(@PathParam("id") long id){
        return managePhoneBook.getContact(id);
    }

    @DELETE
    @Path("{id}")
    public String deleteContact(@PathParam("id") long id){
        return managePhoneBook.deleteContact(id);
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateContact(@PathParam("id") long id, Contact updatedContact){
        return managePhoneBook.updateContact(id, updatedContact);
    }


    @POST
    //@Path("create_contact")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createNewContact(Contact contact){
        managePhoneBook.addContact(contact);
        return "created...";
    }
}
