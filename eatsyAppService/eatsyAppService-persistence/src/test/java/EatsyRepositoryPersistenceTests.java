import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

/**
 * Test class for the persistence context of the application.
 * <p>
 * Test cases will work with H2 in-memory database
 * to eliminates the need for configuring and starting an actual database.
 */
//The annotation will disable full auto-configuration applying only enable configuration relevant to JPA tests.
//tests annotated with @DataJpaTest are transactional and roll back at the end of each test
@DataJpaTest
public class EatsyRepositoryPersistenceTests {

    // TestEntityManager allows us to use EntityManager in DataJpaTests.
    // EntityManager is used by Spring to interact with the persistence context
    @Autowired
    private TestEntityManager testEntityManager;



}
