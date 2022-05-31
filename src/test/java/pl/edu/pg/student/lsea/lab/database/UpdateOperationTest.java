package pl.edu.pg.student.lsea.lab.database;

import java.time.LocalDate;
import java.time.Year;
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
import pl.edu.pg.student.lsea.lab.artist.band.Band;
import pl.edu.pg.student.lsea.lab.artist.musician.Musician;
import pl.edu.pg.student.lsea.lab.artist.service.ArtistService;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;
import pl.edu.pg.student.lsea.lab.user.service.UserService;

/**
 * Test of operations connected with updating data in the database
 * @author Jakub Górniak, Jan Bogdziewicz
 */
@RunWith(MockitoJUnitRunner.class)
public class UpdateOperationTest {
	@Mock
    /** mock database handler for user objects */
    DatabaseHandler<User> userHandler;
	@Mock
	/** mock database handler for artist objects */
	DatabaseHandler<Artist> artistHandler;

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
     * Verifying the proper message is returned if user is successfully added
     */
    @Test
    public void testNewUserAdded() {
        UserService userService = new UserService(userHandler);
        List<User> users = new LinkedList<>();
        String[] updateUserData = {"username", "new username", "new user's Country"};
        User user1 = new User("username", LocalDate.now(), "User's Country");
        users.add(user1);

        Mockito.when(userHandler.get_from_database("user", "username")).thenReturn(users);
        String resultMessage = userService.updateUser(updateUserData);
        Assert.assertEquals(resultMessage, "\nUser username updated");
    }

    /**
     * Verifying if the user update query to the database was successfully invoked
     */
    @Test
    public void testIfUserUpdateIsSuccessful() {
        UserService userService = new UserService(userHandler);
        List<User> users = new LinkedList<>();
        String[] updateUserData = {"username", "new username", "new user's Country"};
        User user1 = new User("username", LocalDate.now(), "User's Country");
        users.add(user1);

        Mockito.when(userHandler.get_from_database("user", "username")).thenReturn(users);
        userService.updateUser(updateUserData);
        Mockito.verify(userHandler).update_to_database("user", updateUserData, user1);
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

    /**
     * Verifying the proper message is returned if artist is successfully added
     */
    @Test
    public void testNewArtistAdded() {
        ArtistService artistService = new ArtistService(artistHandler);
        List<Artist> artists = new LinkedList<>();
        String[] updateArtistData = {"stage name", "new stage name", "new country", "new genre"};

        artists.add(new Musician("stage name", "artist country", "artist genre",
                                           Year.of(2000), Year.of(2010), new LinkedList<Song>(),
                                           new Band(), "artist name", "artist surname"));

        Mockito.when(artistHandler.get_from_database("artist", "stage name")).thenReturn(artists);
        String resultMessage = artistService.updateArtist(updateArtistData);
        Assert.assertEquals(resultMessage, "\nArtist stage name updated");
    }

    /**
     * Verifying if the artist update query to the database was successfully invoked
     */
    @Test
    public void testIfArtistUpdateIsSuccessful() {
        ArtistService artistService = new ArtistService(artistHandler);
        List<Artist> artists = new LinkedList<>();
        String[] updateArtistData = {"stage name", "new stage name", "new country", "new genre"};
        Artist artist1 = new Musician("stage name", "artist country", "artist genre",
                Year.of(2000), Year.of(2010), new LinkedList<Song>(),
                new Band(), "artist name", "artist surname");
        artists.add(artist1);

        Mockito.when(artistHandler.get_from_database("artist", "stage name")).thenReturn(artists);
        artistService.updateArtist(updateArtistData);
        Mockito.verify(artistHandler).update_to_database("artist", updateArtistData, artist1);
    }
}
