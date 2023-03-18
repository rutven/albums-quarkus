package name.legkodymov.resource;

import java.net.URI;
import java.util.List;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import io.quarkus.panache.common.Sort;
import name.legkodymov.model.Person;
import name.legkodymov.repository.PersonRepository;

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
    public Response create(Person person) {
        repository.persist(person);
        return Response.created(URI.create("/persons/" + person.getId())).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Person update(Long id, Person person) {
        Person entity = repository.findById(id);

        if (entity == null) {
            throw new NotFoundException();
        }

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
        Person entity = repository.findById(id);

        if (entity == null) {
            throw new NotFoundException();
        }

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
}
