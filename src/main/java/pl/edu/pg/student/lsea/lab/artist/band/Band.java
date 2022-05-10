package pl.edu.pg.student.lsea.lab.artist.band;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import pl.edu.pg.student.lsea.lab.artist.Artist;

/**
 * Represents a band consisting of at least 2 musicians who released a song in the streaming service. 
 * @author Jakub Górniak
 */
public class Band extends Artist {
	
	/** list of member id's of the band */
	private List<Long> membersID;
	
	/** list of former member id's of the band */
	private List<Long> formerMembersID;
	
	/**
	 * Default constructor.
	 * Creates a new band without setting parameters.
	 */
	public Band() {
		super();
		this.membersID = new ArrayList<Long>();
		this.formerMembersID = new ArrayList<Long>();
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
	public Band(String stageName, String country, String genre, Year activeSince, Year activeTill, List<Long> songs, List<Long> membersID, List<Long> formerMembersID) {
		super(stageName, country, genre, activeSince, activeTill, songs);
		this.membersID = membersID;
		this.formerMembersID = formerMembersID;
	}

	/**
	 * @return the list of id's of current band members
	 */
	public List<Long> getMembersID() {
		return membersID;
	}

	/**
	 * @param members the list of id's of current band members to set
	 */
	public void setMembersID(List<Long> membersID) {
		this.membersID = membersID;
	}

	/**
	 * @return the list of id's of former band members
	 */
	public List<Long> getFormerMembersID() {
		return formerMembersID;
	}

	/**
	 * @param formerMembers the list of id's of former band members to set
	 */
	public void setFormerMembersID(List<Long> formerMembersID) {
		this.formerMembersID = formerMembersID;
	}

	/**
	 * Generate a string representation of a band.
	 * @return string representation of a band.
	 */
	@Override
	public String toString() {
		return "\nBand [current members id = " + membersID + ", former members id = " + formerMembersID + ", stage name = " + getStageName()
				+ ", country = " + getCountry() + ", genre = " + getGenre() + ", active since = "
				+ getActiveSince() + ", active till = " + getActiveTill() + "]";
	}
	
}
