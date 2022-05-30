package pl.edu.pg.student.lsea.lab.database;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.pg.student.lsea.lab.DatabaseHandler;
import pl.edu.pg.student.lsea.lab.user.User;
import pl.edu.pg.student.lsea.lab.user.service.UserService;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;

/**
 * Test of operations connected with removing data from database
 * @author Jakub Górniak, Jan Bogdziewicz
 */
@RunWith(MockitoJUnitRunner.class)
public class RemoveOperationTest {

    @Mock
    /** mock database handler for user objects */
    DatabaseHandler<User> userHandler = Mockito.mock(DatabaseHandler.class);
    
    /**
     * Verifying the proper message is returned if not existing user is removed
     */
    @Test
    public void removingNotExistingUserHandling() {
    	UserService userService = new UserService(userHandler);
    	List<User> users = new LinkedList<>();

        Mockito.when(userHandler.get_from_database("user", "not existing username")).thenReturn(users);
        String resultMessage = userService.removeUser("not existing username");
        Assert.assertEquals(resultMessage, "\nUser not found");
    }

    /**
     * Verifying the proper message is returned if user is successfully removed
     */
    @Test
    public void removingUserHandling() {
        UserService userService = new UserService(userHandler);
        List<User> users = new LinkedList<>();
        User user1 = new User("username", LocalDate.now(), "User's Country");
        users.add(user1);

        Mockito.when(userHandler.get_from_database("user", "username")).thenReturn(users);
        String resultMessage = userService.removeUser("username");
        Assert.assertEquals(resultMessage, "\nUser username removed");
    }

    /**
     * Verifying if the user removal query is successfully invoked
     */
    @Test
    public void testIfUserIsRemoved() {
        UserService userService = new UserService(userHandler);
        List<User> users = new LinkedList<>();
        User user1 = new User("username", LocalDate.now(), "User's Country");
        users.add(user1);

        Mockito.when(userHandler.get_from_database("user", "username")).thenReturn(users);
        userService.removeUser("username");
        Mockito.verify(userHandler).remove_from_database(user1);
    }
}
