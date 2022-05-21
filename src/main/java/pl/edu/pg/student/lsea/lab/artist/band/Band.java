package pl.edu.pg.student.lsea.lab.artist.band;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.artist.musician.Musician;
import pl.edu.pg.student.lsea.lab.song.Song;

/**
 * Represents a band consisting of at least 2 musicians who released a song in the streaming service. 
 * @author Jakub Górniak
 */
@Getter
@Setter
@Entity
@Table(name = "bands")
public class Band extends Artist {
	
	/** serialization identifier */
	private static final long serialVersionUID = 1L;
	
	/** list of members of the band */
	@OneToMany(mappedBy = "band", cascade = CascadeType.ALL)
	private List<Musician> members;
	
	/**
	 * Default constructor.
	 * Creates a new band without setting parameters.
	 */
	public Band() {
		super();
		this.members = new ArrayList<Musician>();
	}
	
	/**
	 * Creates a new band with given parameters.
	 * Extends constructor of the superclass.
	 * @param stageName the stage name of the band
	 * @param country the country of origin of the band
	 * @param genre the genre of the band
	 * @param activeSince the year in which the band became active
	 * @param activeTill the year till which the band was active, null if band is still active
	 * @param members the list of id's of current band members
	 * @param formerMembers the list of id's of former band members
	 */
	public Band(String stageName, String country, String genre, Year activeSince, Year activeTill, List<Song> songs, List<Musician> members) {
		super(stageName, country, genre, activeSince, activeTill, songs);
		this.members = members;
	}

	/**
	 * Generate a string representation of a band.
	 * @return string representation of a band.
	 */
	@Override
	public String toString() {
		return "\nBand [current members id = " + members + ", stage name = " + getStageName()
				+ ", country = " + getCountry() + ", genre = " + getGenre() + ", active since = "
				+ getActiveSince() + ", active till = " + getActiveTill() + "]";
	}
	
}
