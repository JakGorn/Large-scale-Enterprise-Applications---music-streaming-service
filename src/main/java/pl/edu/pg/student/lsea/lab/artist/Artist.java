package pl.edu.pg.student.lsea.lab.artist;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;


/**
 * An abstract class which represents an artist who released a song in the streaming service.
 * @author Jakub Górniak
 */
public abstract class Artist implements Serializable{
	
	/**
	 * Comparator for comparing 2 artists.
	 * According to certain value of enum, comparator compares on chosen parameter.
	 */
	public enum ArtistComparator implements Comparator<Artist> {
	    STAGE_NAME {
	        @Override
	        public int compare(Artist a1, Artist a2) {
	            return a1.getStageName().compareTo(a2.getStageName());
	        }
	    },
	    COUNTRY {
	    	@Override
	        public int compare(Artist a1, Artist a2) {
	            return a1.getCountry().compareTo(a2.getCountry());
	        }
	    },
	    GENRE {
	    	@Override
	        public int compare(Artist a1, Artist a2) {
	            return a1.getGenre().compareTo(a2.getGenre());
	        }
	    },
		ACTIVE_SINCE {
	    	@Override
	        public int compare(Artist a1, Artist a2) {
	            return a1.getActiveSince().compareTo(a2.getActiveSince());
	        }
	    };
	}
	
	/** serialization identifier */
	private static final long serialVersionUID = 1L;
	
	/** artist id generator */
	private static AtomicLong ID_GENERATOR = new AtomicLong();
	
	/** the artist id */
	private Long artistID;
	
	/** the stage name of the artist */
	private String stageName;
	
	/** the country of origin of the artist */
	private String country;
	
	/** the genre of the artist */
	private String genre;
	
	/** the year in which the artist became active */
	private Year activeSince;

	/** the year till which the artist was active, null if still active*/
	private Year activeTill;
	
	/** the list of id's of songs released by the artist */
	private List<Long> songs;
	
	/**
	 * Default constructor.
	 * For invocation in subclass default constructors.
	 */
	protected Artist() {
		this.artistID = ID_GENERATOR.getAndIncrement();
		this.songs = new ArrayList<Long>();
	}
	
	/**
	 * Abstract class constructor with parameter setting.
	 * For invocation in subclass constructors.
	 * @param stageName the stage name of the artist
	 * @param country the country of origin of the artist
	 * @param genre the genre of the artist
	 * @param activeSince the year in which the artist became active
	 * @param activeTill the year till which the artist was active, null if artist is still active
	 * @param songs the list of id's of songs released by the artist
	 */
	protected Artist(String stageName, String country, String genre, Year activeSince, Year activeTill, List<Long> songs) {
		this.artistID = ID_GENERATOR.getAndIncrement();
		this.stageName = stageName;
		this.country = country;
		this.genre = genre;
		this.activeSince = activeSince;
		this.activeTill = activeTill;
		this.songs = songs;
	}

	/**
	 * @return the id of the artist
	 */
	public Long getArtistID() {
		return artistID;
	}

	/**
	 * @return the stage name of the artist (e.g. band name, singer alias)
	 */
	public String getStageName() {
		return stageName;
	}

	/**
	 * @param stageName the stage name of the artist to set
	 */
	public void setStageName(String stageName) {
		this.stageName = stageName;
	}
	
	/**
	 * @return the country of origin of the artist
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country of origin of the artist to set 
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the genre of the artist
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * @param genre the genre of the artist to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}
	
	/**
	 * @return the year in which the artist became active
	 */
	public Year getActiveSince() {
		return activeSince;
	}

	/**
	 * @param activeSince the year in which the artist became active to set
	 */
	public void setActiveSince(Year activeSince) {
		this.activeSince = activeSince;
	}

	/**
	 * @return the year till which artist was active, null if artist is still active
	 */
	public Year getActiveTill() {
		return activeTill;
	}

	/**
	 * @param activeTill the year till which the artist was active to set, null if artist is still active
	 */
	public void setActiveTill(Year activeTill) {
		this.activeTill = activeTill;
	}

	/**
	 * @return the list of id's of songs released by the artist
	 */
	public List<Long> getSongs() {
		return songs;
	}

	/**
	 * @param songs the list of id's of songs released by the artist to set
	 */
	public void setSongs(List<Long> songs) {
		this.songs = songs;
	}

	/**
	 * Abstract method for generating string representation.
	 * Used in subclasses.
	 * @return string representation of an artist
	 */
	@Override
	public abstract String toString();
	
	/**
	 * Count how many songs were released by an artist.
	 * Generate string representation of the result.
	 * @return song count of this artist
	 */
	public String showStatistics() {
		return "song count = " + this.getSongs().size();
	}

}
