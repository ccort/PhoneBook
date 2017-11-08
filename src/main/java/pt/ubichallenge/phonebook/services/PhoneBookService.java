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
            return Response.noContent().build();
        }
        else {
            return Response.ok(contact, MediaType.APPLICATION_JSON).build();
        }
    }

    @DELETE
    @Path("{id}")
    public Response deleteContact(@PathParam("id") long id){
        logger.info("Delete contact request, id: " + id);
        if(managePhoneBook.deleteContact(id)){
            return Response.noContent().build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).entity("Contact with id: " + id + " not found and therefore not deleted.").build();
        }
    }

    @PUT
    @Path("{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateContact(@PathParam("id") long id, Contact updatedContact){
        logger.info("Update contact request, id: " + id);
        Contact contact = managePhoneBook.updateContact(id, updatedContact);
        if(contact == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Contact with id: " + id + " not found and therefore not updated.").build();
        }
        else{
            return Response.noContent().build();
        }
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewContact(Contact contact){
        logger.info("Create contact request");
        long id = managePhoneBook.addContact(contact);
        String jsonResponse = "{\n" +
                "  \"message\": \"Contact created successfully\",\n" +
                "  \"uri\": \"/PhoneBook/ubi/phonebook/" + id + "\"\n" +
                "}";
        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }
}
