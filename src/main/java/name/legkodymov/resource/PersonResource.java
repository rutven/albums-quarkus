package name.legkodymov.resource;

import io.quarkus.panache.common.Sort;
import name.legkodymov.model.Person;
import name.legkodymov.model.Status;
import name.legkodymov.repository.PersonRepository;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;

@Path("/persons")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PersonResource {

    @Inject
    PersonRepository repository;

    @GET
    public List<Person> list() {
        return repository.listAll(Sort.by("name"));
    }

    @GET
    @Path("/{id}")
    public Person get(Long id) {
        return repository.findById(id);
    }

    @POST
    @Transactional
    public Person create(Person person) {
        repository.persist(person);
        return person;
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Person update(Long id, Person person) {
        Person entity = getPersonById(id);

        entity.setName(person.getName());
        entity.setBirthDate(person.getBirthDate());
        entity.setStatus(person.getStatus());

        repository.persist(entity);

        return entity;
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response delete(Long id) {
        Person entity = getPersonById(id);

        repository.delete(entity);

        return Response.noContent().build();
    }

    @GET
    @Path("/search/{name}")
    public Person search(String name) {
        return repository.findByName(name);
    }

    @GET
    @Path("/count")
    public Long count() {
        return repository.count();
    }

    @GET
    @Path("/test")
    @Transactional
    public Person createTestPerson() {
        Person person = new Person();
        person.setName("Ivan Petrov");
        person.setBirthDate(LocalDate.of(1986, Month.FEBRUARY, 14));
        person.setStatus(Status.Alive);
        repository.persist(person);
        return person;
    }

    private Person getPersonById(Long id) {
        Person entity = repository.findById(id);

        if (entity == null) {
            throw new NotFoundException();
        }
        return entity;
    }

}
