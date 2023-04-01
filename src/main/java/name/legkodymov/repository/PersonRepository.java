package name.legkodymov.repository;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import name.legkodymov.model.Person;
import name.legkodymov.model.Status;

@ApplicationScoped
public class PersonRepository implements PanacheRepository<Person> {

    public Person findByName(String name) {
        String search = "%" + name + "%";
        return find("name like ?1", search).firstResult();
    }

    public List<Person> findAlive() {
        return list("status", Status.Alive);
    }

}
