/**
 * 
 */
package pl.edu.pg.student.lsea.lab.user.playlist;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.playlist.config.PlaylistConfig;

/**
 * Represents the playlist of the user with certain songs added.
 * @author Jakub Górniak
 */
public class Playlist implements Cloneable, Serializable {

	/** serialization identifier */
	private static final long serialVersionUID = 1L;
	
	/** playlist id generator */
	private static AtomicLong ID_GENERATOR = new AtomicLong();
	
	/** the playlist id */
	private Long playlistID;

	/** playlist configuration */
	private PlaylistConfig config;
	
	/** the songs in the playlist */
	private List<Song> songs;
	
	/**
	 * Default constructor.
	 * Creates a new playlist without setting the parameters.
	 */
	public Playlist() {
		this.playlistID = ID_GENERATOR.getAndIncrement();
		this.setConfig(new PlaylistConfig());
		this.songs = new ArrayList<Song>();
	}

	/**
	 * Creates a new playlist with given parameters.
	 * @param name name of the playlist
	 * @param songs songs in the playlist
	 */
	public Playlist(PlaylistConfig config, List<Song> songs) {
		this.playlistID = ID_GENERATOR.getAndIncrement();
		this.setConfig(config);
		this.songs = songs;
	}

	/**
	 * @return the id of the playlist
	 */
	public Long getPlaylistID() {
		return playlistID;
	}

	/**
	 * @return the configuration of the playlist
	 */
	public PlaylistConfig getConfig() {
		return config;
	}

	/**
	 * @param config the configuration of the playlist to set
	 */
	public void setConfig(PlaylistConfig config) {
		this.config = config;
	}

	/**
	 * @return the songs in the playlist
	 */
	public List<Song> getSongs() {
		return songs;
	}

	/**
	 * @param songs the songs in the playlist to set
	 */
	public void setSongs(List<Song> songs) {
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
	        playlist = new Playlist(this.getConfig(), this.getSongs());
	    }
	    playlist.playlistID = ID_GENERATOR.getAndIncrement();
	    playlist.config = (PlaylistConfig) this.config.clone();
	    return playlist;
	}
}
