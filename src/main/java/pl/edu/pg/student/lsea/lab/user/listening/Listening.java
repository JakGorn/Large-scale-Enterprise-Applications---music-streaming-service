package pl.edu.pg.student.lsea.lab.user.listening;

import java.time.LocalDateTime;

/**
 * Represents a listening of the song by the user.
 * Contains id of the song and it's time of listening.
 * @author Jakub Górniak
 */
public class Listening {
	
	/** the id of the song */
    private Long songID;
    
    /** the time of listening of the song */
    private LocalDateTime listeningTime;
   
    /**
     * Default constructor.
     * Creates a new user without setting the parameters.
     */
    public Listening() {};
    
	/**
	 * Creates a new listening with given parameters.
	 * @param songID the id of the song
	 * @param listeningTime the time of listening of the song
	 */
	public Listening(Long songID, LocalDateTime listeningTime) {
	   this.setSongID(songID);
	   this.setListeningTime(listeningTime);
	}

	/**
	 * @return the id of the song
	 */
	public Long getSongID() {
		return songID;
	}

	/**
	 * @param songID the id of the song to set
	 */
	public void setSongID(Long songID) {
		this.songID = songID;
	}

	/**
	 * @return the time of listening of the song
	 */
	public LocalDateTime getListeningTime() {
		return listeningTime;
	}

	/**
	 * @param listeningTime the time of listening of the song to set
	 */
	public void setListeningTime(LocalDateTime listeningTime) {
		this.listeningTime = listeningTime;
	}
	
 }