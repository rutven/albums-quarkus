package name.legkodymov;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import name.legkodymov.model.Person;
import name.legkodymov.model.Status;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class PersonResourceTest {

    private static final Logger LOG = Logger.getLogger(PersonResourceTest.class);

    private static void assertPerson(Person person1, Person person2) {
        assertEquals(person2.getName(), person1.getName());
        assertEquals(person2.getBirthDate(), person1.getBirthDate());
        assertEquals(person2.getStatus(), person1.getStatus());
    }

    @Test
    public void testCreateAndGetPersonEndpoints() {
        Person testPerson = new Person();
        testPerson.setName("Ivan Petrov");
        testPerson.setBirthDate(LocalDate.of(1970, Month.JANUARY, 10));
        testPerson.setStatus(Status.Alive);

        Person newPerson = given()
                .when()
                .contentType(ContentType.JSON)
                .body(testPerson)
                .post("/persons")
                .then()
                .contentType(ContentType.JSON)
                .statusCode(200)
                .extract().as(Person.class);

        LOG.info("Person created, id - : " + newPerson.getId());

        assertPerson(testPerson, newPerson);

        Person person = given()
                .when()
                .pathParam("id", newPerson.getId())
                .get("/persons/{id}")
                .then()
                .statusCode(200)
                .extract().as(Person.class);

        assertPerson(testPerson, person);
    }
}
