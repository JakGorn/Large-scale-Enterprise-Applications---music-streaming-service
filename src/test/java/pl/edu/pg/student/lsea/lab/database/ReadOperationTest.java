package pl.edu.pg.student.lsea.lab.database;

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
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;

import java.time.LocalDate;
import java.time.Year;
import java.util.LinkedList;
import java.util.List;

/**
 * Test of operations connected with reading data from database
 * @author Piotr Cichacki
 */
@RunWith(MockitoJUnitRunner.class)
public class ReadOperationTest {

    @Mock
    /** mock database handler for user objects */
    DatabaseHandler<User> userHandler = Mockito.mock(DatabaseHandler.class);
    /** mock database handler for artist objects */
    DatabaseHandler<Artist> artistHandler = Mockito.mock(DatabaseHandler.class);
    /** mock database handler for song objects */
    DatabaseHandler<Song> songHandler = Mockito.mock(DatabaseHandler.class);

    /** Testing if the method for getting all users was called and if the proper users' data was received */
    @Test
    public void testGetAllUsers() {
        List<User> users = new LinkedList<>();
        users.add(new User("New User 1", LocalDate.now(), "User 1's Country"));
        users.add(new User("New User 2", LocalDate.now(), "User 2's Country"));

        Mockito.when(userHandler.get_from_database("user", "all")).thenReturn(users);
        List<User> result = userHandler.get_from_database("user", "all");

        Mockito.verify(userHandler).get_from_database("user", "all");
        Assert.assertEquals(result.size(), 2);
    }

    /** Testing if empty list is received if no users' data is found in the database */
    @Test
    public void testEmptyListReturnIfNoUsersFound() {
        List<User> users = new LinkedList<>();

        Mockito.when(userHandler.get_from_database("user", "all")).thenReturn(users);
        List<User> result = userHandler.get_from_database("user", "all");

        Assert.assertTrue(result.isEmpty());
    }

    /** Testing if the method for getting user with given username was called and if the proper user data was received */
    @Test
    public void testGetUserByUsername() {
        List<User> users = new LinkedList<>();
        users.add(new User("user nickname", LocalDate.now(), "User's Country"));

        Mockito.when(userHandler.get_from_database("user", "user nickname")).thenReturn(users);
        List<User> result = userHandler.get_from_database("user", "user nickname");

        Mockito.verify(userHandler).get_from_database("user", "user nickname");
        Assert.assertEquals(result.size(), 1);
    }

    /** Testing if empty list is received if no user with given username data is found in the database */
    @Test
    public void testEmptyListReturnIfNoUserWithGivenUsernameFound() {
        List<User> users = new LinkedList<>();

        Mockito.when(userHandler.get_from_database("user", "user nickname")).thenReturn(users);
        List<User> result = userHandler.get_from_database("user", "user nickname");

        Assert.assertTrue(result.isEmpty());
    }

    /** Testing if the method for getting all artists was called and if the proper artists' data was received */
    @Test
    public void testGetAllArtists() {
        List<Artist> artists = new LinkedList<>();
        artists.add(new Musician());
        artists.add(new Band());

        Mockito.when(artistHandler.get_from_database("artist", "all")).thenReturn(artists);
        List<Artist> result = artistHandler.get_from_database("artist", "all");

        Mockito.verify(artistHandler).get_from_database("artist", "all");
        Assert.assertEquals(result.size(), 2);
    }

    /** Testing if empty list is received if no artists' data is found in the database */
    @Test
    public void testEmptyListReturnIfNoArtistsFound() {
        List<Artist> artists = new LinkedList<>();

        Mockito.when(artistHandler.get_from_database("artist", "all")).thenReturn(artists);
        List<Artist> result = artistHandler.get_from_database("artist", "all");

        Assert.assertTrue(result.isEmpty());
    }

    /** Testing if the method for getting artist with given stage name was called and if the proper artist data was received */
    @Test
    public void testGetArtistByStageName() {
        List<Artist> artists = new LinkedList<>();
        artists.add(new Musician("stage name", "country", "genre", Year.now(), Year.now(), new LinkedList<Song>(), new Band(), "name", "surname"));

        Mockito.when(artistHandler.get_from_database("artist", "stage name")).thenReturn(artists);
        List<Artist> result = artistHandler.get_from_database("artist", "stage name");

        Mockito.verify(artistHandler).get_from_database("artist", "stage name");
        Assert.assertEquals(result.size(), 1);
    }

    /** Testing if empty list is received if no artist with given stage name data is found in the database */
    @Test
    public void testEmptyListReturnIfNoArtistWithGivenStageNameFound() {
        List<Artist> artists = new LinkedList<>();

        Mockito.when(artistHandler.get_from_database("artist", "stage name")).thenReturn(artists);
        List<Artist> result = artistHandler.get_from_database("artist", "stage name");

        Assert.assertTrue(result.isEmpty());
    }

    /** Testing if the method for getting all songs was called and if the proper songs' data was received */
    @Test
    public void testGetAllSongs() {
        List<Song> songs = new LinkedList<>();
        songs.add(new Song());
        songs.add(new Song());

        Mockito.when(songHandler.get_from_database("song", "all")).thenReturn(songs);
        List<Song> result = songHandler.get_from_database("song", "all");

        Mockito.verify(songHandler).get_from_database("song", "all");
        Assert.assertEquals(result.size(), 2);
    }

    /** Testing if empty list is received if no songs' data is found in the database */
    @Test
    public void testEmptyListReturnIfNoSongsFound() {
        List<Song> songs = new LinkedList<>();

        Mockito.when(songHandler.get_from_database("song", "all")).thenReturn(songs);
        List<Song> result = songHandler.get_from_database("song", "all");

        Assert.assertTrue(result.isEmpty());
    }

    /** Testing if the method for getting song with given name was called and if the proper song data was received */
    @Test
    public void testGetSongsBySongName() {
        List<Song> songs = new LinkedList<>();
        songs.add(new Song("song name", "album", new Musician(), LocalDate.now(), "genre", (short) 1));

        Mockito.when(songHandler.get_from_database("song", "song name")).thenReturn(songs);
        List<Song> result = songHandler.get_from_database("song", "song name");

        Mockito.verify(songHandler).get_from_database("song", "song name");
        Assert.assertEquals(result.size(), 1);
    }

    /** Testing if empty list is received if no song with given name data is found in the database */
    @Test
    public void testEmptyListReturnIfNoSongWithGivenSongNameFound() {
        List<Song> songs = new LinkedList<>();

        Mockito.when(songHandler.get_from_database("song", "song name")).thenReturn(songs);
        List<Song> result = songHandler.get_from_database("song", "song name");

        Assert.assertTrue(result.isEmpty());
    }

}
