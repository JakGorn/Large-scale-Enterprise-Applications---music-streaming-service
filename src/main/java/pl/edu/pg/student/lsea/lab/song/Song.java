package pl.edu.pg.student.lsea.lab.song;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Comparator;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.edu.pg.student.lsea.lab.artist.Artist;

/**
 * Represents a released song in the streaming service.
 * @author Jakub Górniak
 */
@NoArgsConstructor
@Entity
@Table(name = "songs")
public class Song implements Serializable{

	/**
	 * Comparator for comparing 2 songs.
	 * According to certain value of enum, comparator compares on chosen parameter.
	 */
	public enum SongComparator implements Comparator<Song> {
	    NAME {
	        @Override
	        public int compare(Song s1, Song s2) {
	            return s1.getName().compareTo(s2.getName());
	        }
	    },
	    ALBUM {
	    	@Override
	        public int compare(Song s1, Song s2) {
	            return s1.getAlbum().compareTo(s2.getAlbum());
	        }
	    },
	    ARTIST_ID {
	    	@Override
	        public int compare(Song s1, Song s2) {
	            return s1.getArtist().getArtistID().compareTo(s2.getArtist().getArtistID());
	        }
	    },
		RELEASE_DATE {
	    	@Override
	        public int compare(Song s1, Song s2) {
	            return s1.getReleaseDate().compareTo(s2.getReleaseDate());
	        }
	    },
		GENRE {
	    	@Override
	        public int compare(Song s1, Song s2) {
	            return s1.getGenre().compareTo(s2.getGenre());
	        }
	    },
		LENGTH {
	    	@Override
	        public int compare(Song s1, Song s2) {
	            return s1.getLength().compareTo(s2.getLength());
	        }
	    };
	}
	
	/** serialization identifier */
	private static final long serialVersionUID = 1L;
	
	/** the song id */
	@Getter 
	@Id
	@GeneratedValue
	@Column(name = "song_id")
	private Long songID;
	
	/** the name of the song */
	@Getter @Setter
	private String name;
	
	/** the name of the song album */
	@Getter @Setter
	private String album;
	
	/** the artist who released the song (e.g. band, single musician) */
	@Getter @Setter
	@ManyToOne
	@JoinColumn(name = "artist_id")
	private Artist artist;
	
	/** the release date of the song */
	@Getter @Setter
	@Column(name = "release_date")
	private LocalDate releaseDate;
	
	/** the genre of the song */
	@Getter @Setter
	private String genre;
	
	/** the length of the song in seconds */
	@Getter @Setter
	private Short length;
	
	/**
	 * Creates a new song with given parameters.
	 * @param name the name of the song
	 * @param album the name of the song album
	 * @param artistID the id of an artist who released the song (e.g. band, single musician)
	 * @param releaseDate the release date of the song
	 * @param genre the genre of the song
	 * @param length the length of the song in seconds
	 */
	public Song(String name, String album, Artist artist, LocalDate releaseDate, String genre, Short length) {
		this.name = name;
		this.album = album;
		this.artist = artist;
		this.releaseDate = releaseDate;
		this.genre = genre;
		this.length = length;
	}
	
	/**
	 * Generate a string representation of a song.
	 * @return string representation of a song
	 */
	public String toString() {
		return "\nSong [id = " + songID + ", name = " + name + ", album = " + album + ", artist id = " + artist.getArtistID() + ", release date = " + releaseDate
				+ ", genre = " + genre + ", length = " + length + "]";
	}
	
}
