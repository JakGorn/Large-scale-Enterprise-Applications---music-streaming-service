package pl.edu.pg.student.lsea.lab.database;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.times;

import pl.edu.pg.student.lsea.lab.DatabaseHandler;
import pl.edu.pg.student.lsea.lab.user.User;
import pl.edu.pg.student.lsea.lab.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import java.io.IOException;
import java.time.LocalDate;

/**
 * Test of operations connected with saving data to database
 * @author Piotr Cichacki, Jakub Górniak
 */
@RunWith(MockitoJUnitRunner.class)
public class CreateOperationTest {

    @Mock
    /** mock entity manager */
    EntityManager em;
    @Mock
    /** mock entity transaction */
    EntityTransaction transaction;

    /**
     * Verifying number of times persist was called when saving new user to database
     */
    @Test
    public void testSaveUserToDatabase_howManyTimesPersistWasCalled() {
        User newUser = new User("New User", LocalDate.now(), "User's Country");
        DatabaseHandler<User> userHandler = new DatabaseHandler<User>(em, User.class);

        Mockito.when(em.getTransaction()).thenReturn(transaction);
        userHandler.save_to_database(newUser);

        Mockito.verify(em, times(1)).persist(newUser);
    }
    
    /**
     * Verifying the proper message is returned if new user is correctly added to database
     */
    @Test
    public void testSaveUserToDatabase_returnMessage() throws IOException {
    	UserService userService = new UserService(new DatabaseHandler<User>(this.em, User.class));
    	String[] newUserData = {"New User", "01-01-1900", "User's Country"};
    	
    	Mockito.when(em.getTransaction()).thenReturn(transaction);
        String resultMessage = userService.addUser(newUserData);
        
        Assert.assertEquals(resultMessage, "\nNew user added.");
    }
    
    /**
     * Verifying the proper message is returned if wrong format of date is given while saving to the database
     */
    @Test
    public void testAddingUserWithWrongDateFormat_returnMessage() throws IOException {
    	UserService userService = new UserService(new DatabaseHandler<User>(this.em, User.class));
    	String[] newUserData = {"New User", "wrong date format", "User's Country"};
    	
        String resultMessage = userService.addUser(newUserData);
        
        Assert.assertEquals(resultMessage, "\nAdding user failed. Invalid date format provided.");
    }
}
