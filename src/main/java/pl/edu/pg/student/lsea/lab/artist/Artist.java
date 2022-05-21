package pl.edu.pg.student.lsea.lab.artist;

import java.io.Serializable;
import java.time.Year;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.student.lsea.lab.song.Song;


/**
 * An abstract class which represents an artist who released a song in the streaming service.
 * @author Jakub Górniak
 */
@Entity
@Table(name = "artists")
@Inheritance(strategy = InheritanceType.JOINED)
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
	
	/** the artist id */
	@Getter
	@Id
	@GeneratedValue
	@Column(name = "artist_id")
	private Long artistID;
	
	/** the stage name of the artist */
	@Getter @Setter
	@Column(name = "stage_name")
	private String stageName;
	
	/** the country of origin of the artist */
	@Getter @Setter
	private String country;
	
	/** the genre of the artist */
	@Getter @Setter
	private String genre;
	
	/** the year in which the artist became active */
	@Getter @Setter
	@Column(name = "active_since")
	private Year activeSince;

	/** the year till which the artist was active, null if still active*/
	@Getter @Setter
	@Column(name = "active_till")
	private Year activeTill;
	
	/** the list of id's of songs released by the artist */
	@Getter @Setter
	@OneToMany(mappedBy = "artist", cascade = CascadeType.ALL)
	private List<Song> songs;
	
	/**
	 * Default constructor.
	 * For invocation in subclass default constructors.
	 */
	protected Artist() {
		this.songs = new ArrayList<Song>();
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
	protected Artist(String stageName, String country, String genre, Year activeSince, Year activeTill, List<Song> songs) {
		this.stageName = stageName;
		this.country = country;
		this.genre = genre;
		this.activeSince = activeSince;
		this.activeTill = activeTill;
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
