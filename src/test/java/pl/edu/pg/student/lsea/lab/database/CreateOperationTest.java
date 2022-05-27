package pl.edu.pg.student.lsea.lab.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.times;
import pl.edu.pg.student.lsea.lab.DatabaseHandler;
import pl.edu.pg.student.lsea.lab.user.User;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.time.LocalDate;

/**
 * Test of operations connected with saving data to database
 * @author Piotr Cichacki
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateOperationTest {

    @Mock
    /** mock entity manager */
    EntityManager em;
    /** mock entity transaction */
    EntityTransaction transaction = Mockito.mock(EntityTransaction.class);

    /**
     * Verifying number of times persist was called when saving new user to database
     */
    @Test
    public void testSaveUserToDatabase_howManyTimesPersistWasCalled() {
        User newUser = new User("New User", LocalDate.now(), "User's Country");
        DatabaseHandler<User> userHandler = new DatabaseHandler(em, User.class);

        Mockito.when(em.getTransaction()).thenReturn(transaction);
        userHandler.save_to_database(newUser);

        Mockito.verify(em, times(1)).persist(newUser);
    }

}
