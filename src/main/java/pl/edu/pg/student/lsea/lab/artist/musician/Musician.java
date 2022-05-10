package pl.edu.pg.student.lsea.lab.artist.musician;

import java.time.Year;
import java.util.List;

import pl.edu.pg.student.lsea.lab.artist.Artist;

/**
 * Represents a single musician or current/former member of a band with released song in the streaming service. 
 * @author Jakub Górniak
 */
public class Musician extends Artist {

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
	public Musician(String stageName, String country, String genre, Year activeSince, Year activeTill, List<Long> songs, String name, String surname) {
		super(stageName, country, genre, activeSince, activeTill, songs);
		this.name = name;
		this.surname = surname;
	}

	/**
	 * @return the first name of the musician
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the first name of the musician to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the surname of the musician
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname of the musician to set
	 */
	public void setSurname(String surname) {
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
