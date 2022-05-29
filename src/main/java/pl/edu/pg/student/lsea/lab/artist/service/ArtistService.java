package pl.edu.pg.student.lsea.lab.artist.service;

import java.util.List;

import pl.edu.pg.student.lsea.lab.DatabaseHandler;
import pl.edu.pg.student.lsea.lab.artist.Artist;

/**
 * Class for Artist object service
 * @author Jakub Górniak
 */
public class ArtistService {
	/** database handler for artist objects */
	private DatabaseHandler<Artist> artistHandler;
	
	/**
	 * constructor
	 * @param artistHandler database handler for artist objects
	 */
	public ArtistService(DatabaseHandler<Artist> artistHandler) {
		this.artistHandler = artistHandler;
	}
    
    /**
     * method for getting artist from the database with given stage name
     * @param stageName stage name of the artist
     * @return list of all artists with given stage name
     */
    public List<Artist> getArtist(String stageName) {
    	return this.artistHandler.get_from_database("artist", stageName);
    }
    
    /**
     * method for updating the chosen artist in the database
     * @param artistData updated data of the artist
     * @return result of the update operation
     */
    public String updateArtist(String[] artistData) {
    	String stageName = artistData[0];
    	List<Artist> artists = getArtist(stageName);
        if (!artists.isEmpty()) {
        	Artist artist = artists.get(0);
            artistHandler.update_to_database("artist", artistData, artist);
    		return ("\nartist " + stageName + " updated");
        } else {
        	return "\nArtist not found";
        }
    }
}
