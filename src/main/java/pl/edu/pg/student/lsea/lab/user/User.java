package pl.edu.pg.student.lsea.lab.user;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.hibernate.annotations.NamedQuery;

import lombok.Getter;
import lombok.Setter;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.listening.Listening;
import pl.edu.pg.student.lsea.lab.user.playlist.Playlist;


/**
 * Represents an user account in the streaming service.
 * @author Jakub Górniak
 */
@NamedQuery(name = "findAllUsers", query = "from User")
@NamedQuery(name = "findUser_byUsername", query = "from User u where u.username = :username")
@Entity
@Table(name = "users")
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
	
	/** the user id */
	@Getter 
	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Long userID;
	
	/** the username of the user */
	@Getter @Setter
	private String username;
	
	/** the birth date of the user */
	@Getter @Setter
	@Column(name = "birth_date")
	private LocalDate birthDate;
	
	/** the country of the user */
	@Getter @Setter
	private String country;
	
	/** the date of joining the service by the user */
	@Getter @Setter
	@Column(name = "join_date")
	private LocalDate joinDate;
	
	/** the listening history of the user */ 
	@Getter @Setter
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Listening> listenings;
	
	/** the playlists of the user */ 
	@Getter @Setter
	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	private List<Playlist> playlists;

	/**
	 * Default constructor.
	 * Creates a new user without setting the parameters.
	 */
	public User() {
		this.joinDate = LocalDate.now();
		this.listenings = new ArrayList<Listening>();
		this.playlists = new ArrayList<Playlist>();
	}

	/**
	 * Creates a new user with given parameters.
	 * @param username the username of the user
	 * @param birthDate the birth date of the user
	 * @param country the country of the user
	 */
	public User(String username, LocalDate birthDate, String country) {
		this.username = username;
		this.birthDate = birthDate;
		this.country = country;
		this.joinDate = LocalDate.now();
		this.listenings = new ArrayList<Listening>();
		this.playlists = new ArrayList<Playlist>();
	}

	/**
	 * Creates a new user with given parameters.
	 * @param username the username of the user
	 * @param birthDate the birth date of the user
	 * @param country the country of the user
	 * @param listenings the listening history of the user
	 * @param playlists the playlists of the user
	 */
	public User(String username, LocalDate birthDate, String country, List<Listening> listenings, List<Playlist> playlists) {
		this.username = username;
		this.birthDate = birthDate;
		this.country = country;
		this.joinDate = LocalDate.now();
		this.listenings = listenings;
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
        	Song song = l.getSong();
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
		Playlist playlist = (Playlist) user.playlists.get(playlistID).clone();
		playlist.setUser(this);
		this.playlists.add(playlist);
	}
	
}
