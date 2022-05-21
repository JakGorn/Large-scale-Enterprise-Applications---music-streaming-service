package pl.edu.pg.student.lsea.lab.user.playlist.config;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.student.lsea.lab.user.playlist.Playlist;

/**
 * Represents configuration of the user playlist.
 * @author Jakub Górniak
 */
@Entity
@Table(name = "playlist_configs")
public class PlaylistConfig implements Cloneable, Serializable {

	/** serialization identifier */
	private static final long serialVersionUID = 1L;

	/** the id of the playlist config */
	@Getter
	@Id
	@GeneratedValue
	@Column(name = "playlist_id")
	private Long playlistId;
	
	/** playlist to which config belongs */
	@Getter @Setter
	@OneToOne
	private Playlist playlist;
	
	/** name of the playlist */
	@Getter @Setter
	private String name;
	
	/** time of creation of the playlist */
	@Getter @Setter
	@Column(name = "creation_time")
	private LocalDateTime creationTime;
	
	/** whether playlist plays in shuffle mode */
	@Getter @Setter
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
	public PlaylistConfig(Playlist playlist, String name, boolean shuffle) {
		this.playlist = playlist;
		this.name = name;
		this.creationTime = LocalDateTime.now();
		this.shuffle = shuffle;
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
	        config = new PlaylistConfig(this.getPlaylist(), this.getName(), this.isShuffle());
	    }
	    config.setCreationTime(LocalDateTime.now());
	    return config;
	}

}
