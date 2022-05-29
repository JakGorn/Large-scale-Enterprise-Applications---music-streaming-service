package pl.edu.pg.student.lsea.lab.database;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import pl.edu.pg.student.lsea.lab.DatabaseHandler;
import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.artist.service.ArtistService;
import pl.edu.pg.student.lsea.lab.user.User;
import pl.edu.pg.student.lsea.lab.user.service.UserService;

/**
 * Test of operations connected with updating data in the database
 * @author Jakub Górniak
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateOperationTest {
	@Mock
    /** mock database handler for user objects */
    DatabaseHandler<User> userHandler = Mockito.mock(DatabaseHandler.class);
	/** mock database handler for artist objects */
	DatabaseHandler<Artist> artistHandler = Mockito.mock(DatabaseHandler.class);

    
    /**
     * Verifying the proper message is returned if not existing user is updated
     */
    @Test
    public void updatingNotExistingUserHandling() {
    	UserService userService = new UserService(userHandler);
    	List<User> users = new LinkedList<>();
    	String[] updateUserData = {"not existing username", "new username", "new user's Country"};

        Mockito.when(userHandler.get_from_database("user", "not existing username")).thenReturn(users);
    	
        String resultMessage = userService.updateUser(updateUserData);
        
        Assert.assertEquals(resultMessage, "\nUser not found");
    }
    
    /**
     * Verifying the proper message is returned if not existing artist is updated
     */
    @Test
    public void updatingNotExistingArtistHandling() {
    	ArtistService artistService = new ArtistService(artistHandler);
    	List<Artist> artists = new LinkedList<>();
    	String[] updateArtistData = {"not existing stage name", "new stage name", "new country", "new genre"};

        Mockito.when(artistHandler.get_from_database("artist", "not existing stage name")).thenReturn(artists);
    	
        String resultMessage = artistService.updateArtist(updateArtistData);
        
        Assert.assertEquals(resultMessage, "\nArtist not found");
    }
    
}
