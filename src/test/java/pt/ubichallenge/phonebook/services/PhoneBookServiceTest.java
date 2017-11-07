package pt.ubichallenge.phonebook.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.deploy.config.ClientConfig;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.ubichallenge.phonebook.UbiChallengeApplication;
import pt.ubichallenge.phonebook.model.Address;
import pt.ubichallenge.phonebook.model.Contact;
import pt.ubichallenge.phonebook.model.Phone;
import pt.ubichallenge.phonebook.persistence.PhoneBookManagement;
import pt.ubichallenge.phonebook.util.PhoneBookUtils;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.net.URI;
import java.net.URL;

@RunWith(Arquillian.class)
public class PhoneBookServiceTest {

    private static final Logger logger = LoggerFactory.getLogger(PhoneBookServiceTest.class);

    private static WebTarget target;
    private Client client;

    @ArquillianResource
    private URL base;

    private String endpoint = "ubi/phonebook";
    private ObjectMapper mapper;
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test-phonebook.war")
                .addClasses(UbiChallengeApplication.class, PhoneBookService.class, Contact.class, Address.class, Phone.class, PhoneBookManagement.class, PhoneBookUtils.class)
                .addAsResource("META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    @Before
    public void setUp() throws Exception{
        client = ClientBuilder.newClient();

        mapper = new ObjectMapper();
    }

    @Test
    public void getAllContacts() throws Exception {
        target = client.target(URI.create(new URL(base, endpoint).toExternalForm()));

        String r = target.request().get(String.class);
        logger.info("DB: " + r);
    }
    @Test
    public void getContact() throws Exception {
        String id = "4";
        target = client.target(URI.create(new URL(base, endpoint + "/" + id).toExternalForm()));

        String response = target.request().accept(MediaType.APPLICATION_JSON).get(String.class);
        if(response != null) {
            Contact contact = mapper.readValue(response, Contact.class);
            logger.info(contact.toString());
        }
        logger.info("1: " + response);
    }

    @Test
    public void deleteContact() throws Exception {
        String id = "1";
        target = client.target(URI.create(new URL(base, endpoint + "/" + id).toExternalForm()));

        String r = target.request().delete(String.class);
        logger.info("DB: " + r);
    }

    @Test
    public void updateContact() throws Exception {
        String id = "3";
        target = client.target(URI.create(new URL(base, endpoint + "/" + id).toExternalForm()));
        Contact contact = PhoneBookUtils.createSampleContact();
        String contactJson = mapper.writeValueAsString(contact);
        String r = target.request().put(Entity.json(contactJson)).readEntity(String.class);
        logger.info("DB: " + r);
    }

    @Test
    public void createNewContact() throws Exception {
        target = client.target(URI.create(new URL(base, endpoint).toExternalForm()));
        Contact contact = PhoneBookUtils.createSampleContact();
        String contactJson = mapper.writeValueAsString(contact);

        String r = target.request().post(Entity.entity(contactJson,MediaType.APPLICATION_JSON)).readEntity(String.class);
        logger.info("DB: " + r);
    }
}
