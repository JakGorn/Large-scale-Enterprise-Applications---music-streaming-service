package pl.edu.pg.student.lsea.lab.song.service;

import java.util.List;

import pl.edu.pg.student.lsea.lab.DatabaseHandler;
import pl.edu.pg.student.lsea.lab.song.Song;

/**
 * Class for Song object service
 * @author Jakub Górniak
 */
public class SongService {
	/** database handler for song objects */
	private DatabaseHandler<Song> songHandler;
	
	/**
	 * constructor
	 * @param songHandler database handler for song objects
	 */
	public SongService(DatabaseHandler<Song> songHandler) {
		this.songHandler = songHandler;
	}
    
    /**
     * method for getting song from the database with given name
     * @param name name of the song
     * @return list of all songs with given name
     */
    public List<Song> getSong(String name) {
    	return this.songHandler.get_from_database("song", name);
    }
    
}
