package pt.ubichallenge.phonebook.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ubichallenge.phonebook.UbiChallengeApplication;
import pt.ubichallenge.phonebook.model.Address;
import pt.ubichallenge.phonebook.model.Contact;
import pt.ubichallenge.phonebook.model.Phone;
import pt.ubichallenge.phonebook.util.ReturnMessageJson;
import pt.ubichallenge.phonebook.persistence.PhoneBookManagement;
import pt.ubichallenge.phonebook.util.PhoneBookUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.List;

import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PhoneBookServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PhoneBookServiceTest.class);

    private static WebTarget target;
    private Client client;

    @ArquillianResource
    private URL base;

    private String endpoint = "ubi/phonebook/";
    private ObjectMapper mapper;
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test-phonebook.war")
                .addClasses(UbiChallengeApplication.class, PhoneBookService.class, Contact.class, Address.class, Phone.class, PhoneBookManagement.class, PhoneBookUtils.class, ReturnMessageJson.class)
                .addAsResource("test-persistence.xml","META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception{
        target = createNewWebTarget("1");
        mapper = new ObjectMapper();
    }

    // These tests are meant to be run with this specific method order since some
    // tests will be expecting changes performed by previous tests

    @Test
    public void test1GetAllContactsEmpty() throws Exception {
        target = client.target(URI.create(new URL(base, endpoint).toExternalForm()));

        Response response = target.request().get();

        //First execution is expected to return an empty list of contacts
        assertEquals("[]",response.readEntity(String.class));
    }

    @Test
    public void test2CreateNewContact() throws Exception {
        target = client.target(URI.create(new URL(base, endpoint).toExternalForm()));
        Contact contact = PhoneBookUtils.createSampleContact();
        String contactJson = mapper.writeValueAsString(contact);

        Response response = target.request().post(Entity.entity(contactJson,MediaType.APPLICATION_JSON));

        // Checks the Status code and the message/URI
        assertEquals(Response.Status.OK,response.getStatusInfo());
        String expectedJson = "{\"message\":\"Contact created successfully.\"," +
                "\"uri\":\"/PhoneBook/ubi/phonebook/1\"}";
        assertEquals(expectedJson, response.readEntity(String.class));
    }

    @Test
    public void test3GetContact() throws Exception {
        Response response = target.request().accept(MediaType.APPLICATION_JSON).get();
        if(response != null) {
            assertEquals(Response.Status.OK, response.getStatusInfo());
            Contact contact = mapper.readValue(response.readEntity(String.class), Contact.class);

            // Asserts that a contact is indeed returned in the response
            assertThat(contact, instanceOf(Contact.class));
        }
        else{
            fail("Response is null");
        }
    }

    @Test
    public void test4UpdateExistingContact() throws Exception {

        Contact contact = PhoneBookUtils.createSampleContact();

        contact.getAddress().setAddress("UpdatedAddress");

        // Check if the address of the soon to be updated contact is still the old one
        assertEquals("SampleAddress",getContactWithJerseyClient("1").getAddress().getAddress());

        String contactJson = mapper.writeValueAsString(contact);

        // Perform the update
        Response response = target.request().put(Entity.json(contactJson));

        // Check the updated address and status code
        assertEquals("UpdatedAddress",getContactWithJerseyClient("1").getAddress().getAddress());
        assertEquals(Response.Status.NO_CONTENT, response.getStatusInfo());
    }

    @Test
    public void test5UpdateNonExistingContact() throws Exception {
        String id = "20";
        target = client.target(URI.create(new URL(base, endpoint + "/" + id).toExternalForm()));

        Contact contact = PhoneBookUtils.createSampleContact();

        String contactJson = mapper.writeValueAsString(contact);

        Response response = target.request().put(Entity.json(contactJson));

        // Verifies the proper response code when trying to update a non-existing resource
        assertEquals(Response.Status.NOT_FOUND, response.getStatusInfo());
    }

    @Test
    public void test6DeleteContact() throws Exception {

        Response response = target.request().delete();

        // Verifies the 204 when deleting a contact successfully
        assertEquals(Response.Status.NO_CONTENT, response.getStatusInfo());

    }

    @Test
    public void test7CreateMultipleContactsAndReturnThemAll() throws Exception {
        int numberOfContactsToAdd = 3;
        String contactJson = mapper.writeValueAsString(PhoneBookUtils.createSampleContact());

        for(int i = 0; i < numberOfContactsToAdd; i++) {
            target = createNewWebTarget("");
            target.request().post(Entity.entity(contactJson, MediaType.APPLICATION_JSON));
        }

        target = createNewWebTarget("");

        Response response = target.request().get();
        List<Contact> contacts = mapper.readValue(response.readEntity(String.class), new TypeReference<List<Contact>>(){});

        // Asserts that the correct number of contacts are added
        assertEquals(numberOfContactsToAdd, contacts.size());
    }

    //I need this method to facilitate the creation of WebTargets between requests
    public WebTarget createNewWebTarget(String id) throws MalformedURLException {

        client = ClientBuilder.newClient();
        target = client.target(URI.create(new URL(base, endpoint + id).toExternalForm()));
        return target;
    }

    public Contact getContactWithJerseyClient(String id) throws IOException {
        target = createNewWebTarget(""+id);

        String response = target.request().accept(MediaType.APPLICATION_JSON).get(String.class);
        if(response != null) {
            return mapper.readValue(response, Contact.class);
        }
        else{
            return null;
        }
    }
}
