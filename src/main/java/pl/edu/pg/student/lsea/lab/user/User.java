package pl.edu.pg.student.lsea.lab.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.listening.Listening;
import pl.edu.pg.student.lsea.lab.user.playlist.Playlist;


/**
 * Represents an user account in the streaming service.
 * @author Jakub Górniak
 */
public class User implements Serializable {
	
	/**
	 * Comparator for comparing 2 users.
	 * According to certain value of enum, comparator compares on chosen parameter.
	 */
	public enum UserComparator implements Comparator<User> {
	    USERNAME {
	        @Override
	        public int compare(User u1, User u2) {
	            return u1.getUsername().compareTo(u2.getUsername());
	        }
	    },
	    BIRTH_DATE {
	    	@Override
	        public int compare(User u1, User u2) {
	            return u1.getBirthDate().compareTo(u2.getBirthDate());
	        }
	    },
	    COUNTRY {
	    	@Override
	        public int compare(User u1, User u2) {
	            return u1.getCountry().compareTo(u2.getCountry());
	        }
	    },
		JOIN_DATE {
	    	@Override
	        public int compare(User u1, User u2) {
	            return u1.getJoinDate().compareTo(u2.getJoinDate());
	        }
	    };
	}
	
	/** serialization identifier */
	private static final long serialVersionUID = 1L;
	
	/** user id generator */
	private static AtomicLong ID_GENERATOR = new AtomicLong();
	
	/** the user id */
	private Long userID;
	
	/** the username of the user */
	private String username;
	
	/** the birth date of the user */
	private LocalDate birthDate;
	
	/** the country of the user */
	private String country;
	
	/** the date of joining the service by the user */
	private LocalDate joinDate;
	
	/** the listening history of the user */ 
	private List<Listening> listenings;
	
	/** the playlists of the user */ 
	private List<Playlist> playlists;

	/**
	 * Default constructor.
	 * Creates a new user without setting the parameters.
	 */
	public User() {
		this.userID = ID_GENERATOR.getAndIncrement();
		this.joinDate = LocalDate.now();
		this.listenings = new ArrayList<Listening>();
		this.playlists = new ArrayList<Playlist>();
	}

	/**
	 * Creates a new user with given parameters.
	 * @param username the username of the user
	 * @param birthDate the birth date of the user
	 * @param country the country of the user
	 * @param joinDate the date of joining the service by the user
	 * @param listening the listening history of the user
	 * @param playlists the playlists of the user
	 */
	public User(String username, LocalDate birthDate, String country, List<Listening> listenings, List<Playlist> playlists) {
		this.userID = ID_GENERATOR.getAndIncrement();
		this.username = username;
		this.birthDate = birthDate;
		this.country = country;
		this.joinDate = LocalDate.now();
		this.listenings = listenings;
		this.playlists = playlists;
	}

	/**
	 * @return the id of the user
	 */
	public Long getUserID() {
		return userID;
	}

	/**
	 * @return the username of the user
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username the username of the user to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return the birth date of the user
	 */
	public LocalDate getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate the birth date of the user to set
	 */
	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return the country of the user
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country of the user to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the date of joining the service by the user
	 */
	public LocalDate getJoinDate() {
		return joinDate;
	}

	/**
	 * @param joinDate the date of joining the service by the user to set
	 */
	public void setJoinDate(LocalDate joinDate) {
		this.joinDate = joinDate;
	}

	/**
	 * @return the listening history of the user
	 */
	public List<Listening> getListenings() {
		return listenings;
	}

	/**
	 * @param listening the listening history of the user to set
	 */
	public void setListenings(List<Listening> listenings) {
		this.listenings = listenings;
	}
	
	/**
	 * @return the playlists of the user
	 */
	public List<Playlist> getPlaylists() {
		return playlists;
	}

	/**
	 * @param playlists the playlists of the user to set
	 */
	public void setPlaylists(List<Playlist> playlists) {
		this.playlists = playlists;
	}

	/**
	 * Generate a string representation of an user.
	 * @return string representation of the user
	 */
	@Override
	public String toString() {
		return "\nUser [id = " + userID + ", username = " + username + ", birth date = " + birthDate + ", country = " + country + ", join date = "
				+ joinDate + "]";
	}

	/**
	 * Count how many times particular songs were listened by the user.
	 * Generate string representation of the result.
	 * @param songs list of the songs
	 * @return song names and listening counts
	 */
	public String showStatistics(List<Song> songs) {
		Map<String, Integer> map = new HashMap<String, Integer>();
        for(Listening l : this.listenings) {
        	Long id = l.getSongID();
        	Song song = songs.stream()
					  .filter(s -> id.equals(s.getSongID()))
					  .findAny()
					  .orElse(null);
        	String songName = song.getName();
            Integer value = map.get(songName);
            if(value == null) {
                map.put(songName, 1);
            }
            else {
                map.put(songName, value + 1);
            }
        }
		return map.toString();
	}
	
	/**
	 * Copy the chosen playlist to the list of user playlists.
	 * @param user the user which playlist is copied
	 * @param playlistID the id of the playlist to be copied
	 */
	public void copyPlaylist(User user, int playlistID) {
		this.playlists.add((Playlist) user.playlists.get(playlistID).clone());
	}
	
}
