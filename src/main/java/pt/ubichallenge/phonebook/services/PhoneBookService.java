package pt.ubichallenge.phonebook.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ubichallenge.phonebook.model.Contact;
import pt.ubichallenge.phonebook.persistence.PhoneBookManagement;
import pt.ubichallenge.phonebook.util.PhoneBookUtils;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
        logger.info("Get all contacts request");
        return managePhoneBook.getContacts();
    }

    @GET
    @Path("{id}")
    @Produces({MediaType.APPLICATION_JSON})
    public Response getContact(@PathParam("id") long id){
        logger.info("Get contact request, id: " + id);
        Contact contact = managePhoneBook.getContact(id);
        if(contact == null) {
            logger.info("Contact does not exist, id: " + id);
            String jsonResponse = PhoneBookUtils.createReturnMessageJson("Contact with id: " + id + " not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(jsonResponse).build();
        }
        else {
            return Response.ok(contact, MediaType.APPLICATION_JSON).build();
        }
    }

    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteContact(@PathParam("id") long id){
        logger.info("Delete contact request, id: " + id);
        if(managePhoneBook.deleteContact(id)){
            return Response.noContent().build();
        }
        else {
            String jsonResponse = PhoneBookUtils.createReturnMessageJson("Contact with id: " + id + " not found and therefore not deleted.");
            return Response.status(Response.Status.NOT_FOUND).entity(jsonResponse).build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateContact(@PathParam("id") long id, Contact updatedContact){
        logger.info("Update contact request, id: " + id);
        if(updatedContact == null){
            String jsonResponse = PhoneBookUtils.createReturnMessageJson("No JSON Contact sent in the body message.");
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        Contact contact = managePhoneBook.updateContact(id, updatedContact);
        if(contact == null) {
            String jsonResponse = PhoneBookUtils.createReturnMessageJson("Contact with id: " + id + " not found and therefore not updated.");
            return Response.status(Response.Status.NOT_FOUND).entity(jsonResponse).build();
        }
        else{
            return Response.noContent().build();
        }
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewContact(Contact contact){
        logger.info("Create contact request");
        if(contact == null){
            logger.info("Contact not added, contact is null");
            String jsonResponse = PhoneBookUtils.createReturnMessageJson("No JSON Contact sent in the body message.");
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        if(contact.getName() == null){
            logger.info("Contact not added, contact does not have a name");
            String jsonResponse = PhoneBookUtils.createReturnMessageJson("The Contact needs to at least have a name.");
            return Response.status(Response.Status.BAD_REQUEST).entity(jsonResponse).build();
        }
        long id = managePhoneBook.addContact(contact);
        String jsonResponse = PhoneBookUtils.createReturnMessageJson("Contact created successfully.", "/PhoneBook/ubi/phonebook/" + id);
        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }
}
