package pt.ubichallenge.phonebook;

import pt.ubichallenge.phonebook.services.PhoneBookService;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.Set;

@ApplicationPath("ubi")
public class UbiChallengeApplication extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        resources.add(PhoneBookService.class);
        return resources;
    }
}
