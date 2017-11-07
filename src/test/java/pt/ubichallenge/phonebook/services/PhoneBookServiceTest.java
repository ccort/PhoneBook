package pt.ubichallenge.phonebook.services;

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
    }

    @Test
    public void getAllContacts() throws Exception {
        target = client.target(URI.create(new URL(base, endpoint).toExternalForm()));

        String r = target.request().get(String.class);
        logger.info("DB: " + r);
    }
    @Test
    public void getContact() throws Exception {
        String id = "1";
        target = client.target(URI.create(new URL(base, endpoint + "/" + id).toExternalForm()));

        String r = target.request().get(String.class);
        logger.info("DB: " + r);
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
        String id = "1";
        target = client.target(URI.create(new URL(base, endpoint + "/" + id).toExternalForm()));

        String r = target.request().put(Entity.text("2")).readEntity(String.class);
        logger.info("DB: " + r);
    }

    @Test
    public void createNewContact() throws Exception {
        target = client.target(URI.create(new URL(base, endpoint).toExternalForm()));

        String r = target.request().post(Entity.text("1")).readEntity(String.class);
        logger.info("DB: " + r);
    }
}
