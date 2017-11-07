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
        //return "Get {id} user yet to be implemented. Debug: " + id;
    }

    @DELETE
    @Path("{id}")
    public String deleteContact(@PathParam("id") String id){

        return "Delete {id} user yet to be implemented. Debug: " + id;
    }

    @PUT
    @Path("{id}")
    public String updateContact(@PathParam("id") String id){

        return "Update {id} user yet to be implemented. Debug: " + id;
    }


    @POST
    //@Path("create_contact")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createNewContact(Contact contact){
        logger.info(contact.toString());
        logger.info("Inserting contact...");
        managePhoneBook.addContact(contact);
        logger.info("Inserted!");

        return "created...";
    }
}
