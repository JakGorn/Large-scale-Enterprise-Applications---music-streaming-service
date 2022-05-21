package pl.edu.pg.student.lsea.lab.artist.musician;

import java.time.Year;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.artist.band.Band;
import pl.edu.pg.student.lsea.lab.song.Song;

/**
 * Represents a single musician or current/former member of a band with released song in the streaming service. 
 * @author Jakub Górniak
 */
@Getter
@Setter
@Entity
@Table(name = "musicians")
public class Musician extends Artist {

	/** serialization identifier */
	private static final long serialVersionUID = 1L;
	
	/** band in which the musician plays(null if none) */
	@ManyToOne(cascade = CascadeType.PERSIST)
	private Band band;
	
	/** the first name of the musician */
	private String name;
	
	/** the surname of the musician */
	private String surname;
	
	/**
	 * Default constructor.
	 * Creates a new musician without setting parameters.
	 */
	public Musician() {
		super();
		this.band = null;
	}
	
	/**
	 * Creates a new musician with given parameters.
	 * Extends constructor of the superclass.
	 * @param stageName the stage name of the musician
	 * @param country the country of origin of the musician
	 * @param genre the genre of the musician
	 * @param activeSince the year in which the musician became active
	 * @param activeTill the year till which the musician was active, null if musician is still active
	 * @param name the first name of the musician 
	 * @param surname the surname of the musician
	 */
	public Musician(String stageName, String country, String genre, Year activeSince, Year activeTill, List<Song> songs, Band band, String name, String surname) {
		super(stageName, country, genre, activeSince, activeTill, songs);
		this.band = band;
		this.name = name;
		this.surname = surname;
	}
	
	/**
	 * Generate a string representation of a musician.
	 * @return string representation of musician.
	 */
	@Override
	public String toString() {
		return "\nMusician [name = " + name + ", surname = " + surname + ", stage name = " + getStageName()
				+ ", country = " + getCountry() + ", genre = " + getGenre() + ", active since = "
				+ getActiveSince() + ", active till = " + getActiveTill() + "]";
	}

}
