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
import pl.edu.pg.student.lsea.lab.artist.service.ArtistService;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.song.service.SongService;
import pl.edu.pg.student.lsea.lab.user.User;
import pl.edu.pg.student.lsea.lab.user.service.UserService;

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
    DatabaseHandler<User> userHandler;
	@Mock
    /** mock database handler for artist objects */
    DatabaseHandler<Artist> artistHandler;
	@Mock
    /** mock database handler for song objects */
    DatabaseHandler<Song> songHandler;

    /** Testing if the method for getting all users was called and if the proper users' data was received */
    @Test
    public void testGetAllUsers() {
        UserService userService = new UserService(userHandler);
        List<User> users = new LinkedList<>();
        users.add(new User("New User 1", LocalDate.now(), "User 1's Country"));
        users.add(new User("New User 2", LocalDate.now(), "User 2's Country"));

        Mockito.when(userHandler.get_from_database("user", "all")).thenReturn(users);
        List<User> result = userService.getUser("all");

        Mockito.verify(userHandler).get_from_database("user", "all");
        Assert.assertEquals(result.size(), 2);
    }

    /** Testing if empty list is received if no users' data is found in the database */
    @Test
    public void testEmptyListReturnIfNoUsersFound() {
        UserService userService = new UserService(userHandler);
        List<User> users = new LinkedList<>();

        Mockito.when(userHandler.get_from_database("user", "all")).thenReturn(users);
        List<User> result = userService.getUser("all");

        Assert.assertTrue(result.isEmpty());
    }

    /** Testing if the method for getting user with given username was called and if the proper user data was received */
    @Test
    public void testGetUserByUsername() {
        List<User> users = new LinkedList<>();
        List<User> correctUsers = new LinkedList<>();
        UserService userService = new UserService(userHandler);
        User user1 = new User("user nickname", LocalDate.now(), "User's Country");
        User user2 = new User("user nickname", LocalDate.now(), "User's Country");
        User user3 = new User("user nickname 3", LocalDate.now(), "User's Country");

        users.add(user1);
        users.add(user2);
        users.add(user3);

        correctUsers.add(user1);
        correctUsers.add(user2);

        Mockito.when(userHandler.get_from_database("user", "user nickname")).thenReturn(correctUsers);
        List<User> result = userService.getUser("user nickname");

        Mockito.verify(userHandler).get_from_database("user", "user nickname");
        Assert.assertEquals(result.size(), 2);
    }

    /** Testing if empty list is received if no user with given username data is found in the database */
    @Test
    public void testEmptyListReturnIfNoUserWithGivenUsernameFound() {
        UserService userService = new UserService(userHandler);
        List<User> users = new LinkedList<>();

        Mockito.when(userHandler.get_from_database("user", "user nickname")).thenReturn(users);
        List<User> result = userService.getUser("user nickname");

        Assert.assertTrue(result.isEmpty());
    }

    /** Testing if the method for getting all artists was called and if the proper artists' data was received */
    @Test
    public void testGetAllArtists() {
        ArtistService artistService = new ArtistService(artistHandler);
        List<Artist> artists = new LinkedList<>();
        artists.add(new Musician());
        artists.add(new Band());

        Mockito.when(artistHandler.get_from_database("artist", "all")).thenReturn(artists);
        List<Artist> result = artistService.getArtist("all");

        Mockito.verify(artistHandler).get_from_database("artist", "all");
        Assert.assertEquals(result.size(), 2);
    }

    /** Testing if empty list is received if no artists' data is found in the database */
    @Test
    public void testEmptyListReturnIfNoArtistsFound() {
        ArtistService artistService = new ArtistService(artistHandler);
        List<Artist> artists = new LinkedList<>();

        Mockito.when(artistHandler.get_from_database("artist", "all")).thenReturn(artists);
        List<Artist> result = artistService.getArtist("all");

        Assert.assertTrue(result.isEmpty());
    }

    /** Testing if the method for getting artist with given stage name was called and if the proper artist data was received */
    @Test
    public void testGetArtistByStageName() {
        ArtistService artistService = new ArtistService(artistHandler);
        List<Artist> artists = new LinkedList<>();
        artists.add(new Musician("stage name", "country", "genre", Year.now(), Year.now(), new LinkedList<Song>(), new Band(), "name", "surname"));

        Mockito.when(artistHandler.get_from_database("artist", "stage name")).thenReturn(artists);
        List<Artist> result = artistService.getArtist("stage name");

        Mockito.verify(artistHandler).get_from_database("artist", "stage name");
        Assert.assertEquals(result.size(), 1);
    }

    /** Testing if empty list is received if no artist with given stage name data is found in the database */
    @Test
    public void testEmptyListReturnIfNoArtistWithGivenStageNameFound() {
        ArtistService artistService = new ArtistService(artistHandler);
        List<Artist> artists = new LinkedList<>();

        Mockito.when(artistHandler.get_from_database("artist", "stage name")).thenReturn(artists);
        List<Artist> result = artistService.getArtist("stage name");

        Assert.assertTrue(result.isEmpty());
    }

    /** Testing if the method for getting all songs was called and if the proper songs' data was received */
    @Test
    public void testGetAllSongs() {
        SongService songService = new SongService(songHandler);
        List<Song> songs = new LinkedList<>();
        songs.add(new Song());
        songs.add(new Song());

        Mockito.when(songHandler.get_from_database("song", "all")).thenReturn(songs);
        List<Song> result = songService.getSong("all");

        Mockito.verify(songHandler).get_from_database("song", "all");
        Assert.assertEquals(result.size(), 2);
    }

    /** Testing if empty list is received if no songs' data is found in the database */
    @Test
    public void testEmptyListReturnIfNoSongsFound() {
        SongService songService = new SongService(songHandler);
        List<Song> songs = new LinkedList<>();

        Mockito.when(songHandler.get_from_database("song", "all")).thenReturn(songs);
        List<Song> result = songService.getSong("all");

        Assert.assertTrue(result.isEmpty());
    }

    /** Testing if the method for getting song with given name was called and if the proper song data was received */
    @Test
    public void testGetSongsBySongName() {
        SongService songService = new SongService(songHandler);
        List<Song> songs = new LinkedList<>();
        songs.add(new Song("song name", "album", new Musician(), LocalDate.now(), "genre", (short) 1));

        Mockito.when(songHandler.get_from_database("song", "song name")).thenReturn(songs);
        List<Song> result = songService.getSong("song name");

        Mockito.verify(songHandler).get_from_database("song", "song name");
        Assert.assertEquals(result.size(), 1);
    }

    /** Testing if empty list is received if no song with given name data is found in the database */
    @Test
    public void testEmptyListReturnIfNoSongWithGivenSongNameFound() {
        SongService songService = new SongService(songHandler);
        List<Song> songs = new LinkedList<>();

        Mockito.when(songHandler.get_from_database("song", "song name")).thenReturn(songs);
        List<Song> result = songService.getSong("song name");

        Assert.assertTrue(result.isEmpty());
    }

}
