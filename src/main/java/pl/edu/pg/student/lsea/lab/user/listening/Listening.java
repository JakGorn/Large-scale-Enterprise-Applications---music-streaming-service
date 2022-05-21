package pl.edu.pg.student.lsea.lab.user.listening;

import java.io.Serializable;
import java.time.LocalDateTime;

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
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;

/**
 * Represents a listening of the song by the user.
 * Contains id of the song and it's time of listening.
 * @author Jakub Górniak
 */
@NoArgsConstructor
@Entity
@Table(name = "listenings")
public class Listening implements Serializable {

	/** serialization identifier */
	private static final long serialVersionUID = 1L;
	
	/** the id of the listening */
	@Getter
	@Id
	@GeneratedValue
	@Column(name = "listening_id")
	private Long listeningId;
	
	/** the song which was listened */
	@Getter @Setter
	@ManyToOne
	@JoinColumn(name = "song_id")
    private Song song;
	
	/** user to which the listening belongs */
	@Getter @Setter
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;
    
    /** the time of listening of the song */
	@Getter @Setter
	@Column(name = "listening_time")
    private LocalDateTime listeningTime;
    
	/**
	 * Creates a new listening with given parameters.
	 * @param songID the id of the song
	 * @param listeningTime the time of listening of the song
	 */
	public Listening(Song song, User user, LocalDateTime listeningTime) {
	   this.song = song;
	   this.user = user;
	   this.listeningTime = listeningTime;
	}
	
 }