package pl.edu.pg.student.lsea.lab.user.playlist.config;

import java.time.LocalDateTime;

/**
 * Represents configuration of the user playlist.
 * @author Jakub Górniak
 */
public class PlaylistConfig implements Cloneable{

	/** name of the playlist */
	private String name;
	
	/** time of creation of the playlist */
	private LocalDateTime creationTime;
	
	/** whether playlist plays in shuffle mode */
	private boolean shuffle;
	
	/**
	 * Default constructor.
	 * Creates a new playlist configuration without setting the parameters.
	 */
	public PlaylistConfig() {
		this.name = "Default name";
		this.creationTime = LocalDateTime.now();
		this.shuffle = false;
	}

	/**
	 * Creates a new playlist configuration without setting the parameters.
	 * @param name name of the playlist
	 * @param songs songs in the playlist
	 */
	public PlaylistConfig(String name, boolean shuffle) {
		this.name = name;
		this.creationTime = LocalDateTime.now();
		this.shuffle = shuffle;
	}

	/**
	 * @return the name of the playlist
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name of the playlist to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return time of creation of the playlist
	 */
	public LocalDateTime getCreationTime() {
		return creationTime;
	}

	/**
	 * @param creationTime time of creation of the playlist to set
	 */
	public void setCreationTime(LocalDateTime creationTime) {
		this.creationTime = creationTime;
	}

	/**
	 * @return whether playlist plays in shuffle mode
	 */
	public boolean getShuffle() {
		return shuffle;
	}

	/**
	 * Switching the mode of playing to shuffle or otherwise.
	 */
	public void switchShuffle() {
		this.shuffle = !this.shuffle;
	}
	
	/**
	 * Generate a string representation of a playlist configuration.
	 * @return string representation of the playlist configuration
	 */
	@Override
	public String toString() {
		return "PlaylistConfig [name=" + name + ", creationTime=" + creationTime + ", shuffle=" + shuffle + "]";
	}
	
	/**
	 * Clone the playlist configuration.
	 * @return cloned playlist configuration
	 */
	@Override
	public Object clone() {
		PlaylistConfig config = null;
	    try {
	        config = (PlaylistConfig) super.clone();
	    } catch (CloneNotSupportedException e) {
	        config = new PlaylistConfig(this.getName(), this.getShuffle());
	    }
	    config.setCreationTime(LocalDateTime.now());
	    return config;
	}

}
