package pl.edu.pg.student.lsea.lab.song;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Represents a released song in the streaming service.
 * @author Jakub Górniak
 */
public class Song {
	
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
	            return s1.getArtistID().compareTo(s2.getArtistID());
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

	/** song id generator */
	private static AtomicLong ID_GENERATOR = new AtomicLong();
	
	/** the song id */
	private Long songID;
	
	/** the name of the song */
	private String name;
	
	/** the name of the song album */
	private String album;
	
	/** the id of an artist who released the song (e.g. band, single musician) */
	private Long artistID;
	
	/** the release date of the song */
	private LocalDate releaseDate;
	
	/** the genre of the song */
	private String genre;
	
	/** the length of the song in seconds */
	private Short length;

	/**
	 * Default constructor.
	 * Creates a new song without setting parameters.
	 */
	public Song() {
		this.songID = ID_GENERATOR.getAndIncrement();
	}
	
	/**
	 * Creates a new song with given parameters.
	 * @param name the name of the song
	 * @param album the name of the song album
	 * @param artistID the id of an artist who released the song (e.g. band, single musician)
	 * @param releaseDate the release date of the song
	 * @param genre the genre of the song
	 * @param length the length of the song in seconds
	 */
	public Song(String name, String album, Long artistID, LocalDate releaseDate, String genre, Short length) {
		this.songID = ID_GENERATOR.getAndIncrement();
		this.name = name;
		this.album = album;
		this.artistID = artistID;
		this.releaseDate = releaseDate;
		this.genre = genre;
		this.length = length;
	}

	/**
	 * @return the id of the song
	 */
	public Long getSongID() {
		return songID;
	}

	/**
	 * @return the name of the song
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name of the song to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * @return the name of the song album
	 */
	public String getAlbum() {
		return album;
	}

	/**
	 * @param album the name of the song album to set
	 */
	public void setAlbum(String album) {
		this.album = album;
	}

	/**
	 * @return the id of an artist who released the song (e.g. band, single musician)
	 */
	public Long getArtistID() {
		return artistID;
	}

	/**
	 * @param artist the id of an artist who released the song (e.g. band, single musician) to set
	 */
	public void setArtistID(Long artistID) {
		this.artistID = artistID;
	}
	
	/**
	 * @return the release date of the song
	 */
	public LocalDate getReleaseDate() {
		return releaseDate;
	}

	/**
	 * @param releaseDate the release date of the song 
	 */
	public void setReleaseDate(LocalDate releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * @return the genre of the song
	 */
	public String getGenre() {
		return genre;
	}

	/**
	 * @param genre the genre of the song to set
	 */
	public void setGenre(String genre) {
		this.genre = genre;
	}

	/**
	 * @return the length of the song in seconds
	 */
	public Short getLength() {
		return length;
	}

	/**
	 * @param length the length of the song to set in seconds
	 */
	public void setLength(Short length) {
		this.length = length;
	}
	
	/**
	 * Generate a string representation of a song.
	 * @return string representation of a song
	 */
	public String toString() {
		return "\nSong [id = " + songID + ", name = " + name + ", album = " + album + ", artist id = " + artistID + ", release date = " + releaseDate
				+ ", genre = " + genre + ", length = " + length + "]";
	}
	
}
