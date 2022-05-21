/**
 * 
 */
package pl.edu.pg.student.lsea.lab.user.playlist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;
import pl.edu.pg.student.lsea.lab.user.playlist.config.PlaylistConfig;

/**
 * Represents the playlist of the user with certain songs added.
 * @author Jakub Górniak
 */
@Entity
@Table(name = "playlists")
public class Playlist implements Cloneable, Serializable {

	/** serialization identifier */
	private static final long serialVersionUID = 1L;
	
	/** the playlist id */
	@Getter
	@Id
	@GeneratedValue
	private Long playlistID;
	
	/** user which the playlist belongs to*/
	@Getter @Setter
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	/** playlist configuration */
	@Getter @Setter
	@OneToOne(mappedBy = "playlist", cascade = CascadeType.ALL)
	private PlaylistConfig config;
	
	/** the songs in the playlist */
	@Getter @Setter
	@ManyToMany
	private List<Song> songs;
	
	/**
	 * Default constructor.
	 * Creates a new playlist without setting the parameters.
	 */
	public Playlist() {
		this.config = new PlaylistConfig();
		this.songs = new ArrayList<Song>();
	}

	/**
	 * Creates a new playlist with given parameters.
	 * @param name name of the playlist
	 * @param songs songs in the playlist
	 */
	public Playlist(User user, PlaylistConfig config, List<Song> songs) {
		this.user = user;
		this.config = config;
		this.songs = songs;
	}
	
	/**
	 * @param song song to be added to playlist
	 */
	public void addSong(Song song) {
		this.songs.add(song);
	}
	
	/**
	 * @param song to be removed from playlist
	 */
	public void removeSong(Song song) {
		this.songs.remove(song);
	}
	
	/**
	 * Generate a string representation of a playlist.
	 * @return string representation of the playlist
	 */
	@Override
	public String toString() {
		return "\nPlaylist [playlistID=" + playlistID + ", config=" + config + ", songs=" + songs + "]";
	}

	/**
	 * Deep clone the playlist.
	 * @return deep cloned playlist
	 */
	@Override
	public Object clone() {
	    Playlist playlist = null;
	    try {
	        playlist = (Playlist) super.clone();
	    } catch (CloneNotSupportedException e) {
	        playlist = new Playlist(this.getUser(), this.getConfig(), this.getSongs());
	    }
	    playlist.config = (PlaylistConfig) this.config.clone();
	    return playlist;
	}
}
