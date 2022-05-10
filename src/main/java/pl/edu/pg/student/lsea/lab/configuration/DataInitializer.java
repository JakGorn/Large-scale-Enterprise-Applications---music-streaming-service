/**
 * 
 */
package pl.edu.pg.student.lsea.lab.configuration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.lang3.StringUtils;

import com.github.javafaker.Faker;


import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.artist.band.Band;
import pl.edu.pg.student.lsea.lab.artist.musician.Musician;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;
import pl.edu.pg.student.lsea.lab.user.listening.Listening;
import pl.edu.pg.student.lsea.lab.user.playlist.Playlist;
import pl.edu.pg.student.lsea.lab.user.playlist.config.PlaylistConfig;

/**
 * Class responsible for data initialization.
 * @author Jakub Górniak
 */
public class DataInitializer {
	
	/** lock for synchronization in concurrent processing */
	ReadWriteLock lock = new ReentrantReadWriteLock();
    
	/** write lock for synchronization in concurrent processing */
    Lock writeLock = lock.writeLock();
    
    /** read lock for synchronization in concurrent processing */
    Lock readLock = lock.readLock();

	/** list of the artists */
	private List<Artist> artists = new ArrayList<Artist>();
	
	/** list of the songs */
	private List<Song> songs = new ArrayList<Song>();
	
	/** list of the users */
	private List<User> users = new ArrayList<User>();
	
	/** number of users in the database */
	private final int userNr = 10000;
	
	/** number of bands in the database */
	private final int bandNr = 100;
	
	/** number of musicians in the database */
	private final int musicianNr = 500;
	
	/** base number of listenings of the user in the database */
	private final int listeningNr = 100;
	
	/** stack containing past 10 years, for analysis purposes */
	private Stack<Integer> past10Years;
	
	/** list of all songs which were listened over 100 times in any year and list of years in which they did so */
	private Map<Long,List<Integer>> songsOver100 = new HashedMap<Long, List<Integer>>();
	
	/** 2d array of music genres, grouped according to type */
	private final String[][] genre = {
			{"alternative country", "country pop", "country rock", "americana", "country blues"},
			{"african blues", "blues rock", "country blues", "soul blues", "punk blues"},
			{"art pop", "country pop", "disco polo", "indie pop", "pop rock"},
			{"alternative rock", "blues rock", "classic rock", "electronic rock", "pop rock"},
			{"alternative metal", "black metal", "death metal", "folk metal", "metalcore"}
	};

	
	/**
	 * Fills the given lists with data. 
	 * @param users list of users
	 * @param songs list of songs
	 * @param artists list of artists
	 */
	private void init(List<Artist> artists, List<Song> songs, List<User> users) {
		
		Random rand = new Random();
		Calendar cld = Calendar.getInstance(), cld2 = Calendar.getInstance();
		Faker faker = new Faker();
		
		Stack<Integer> stackMusicians = new Stack<Integer>();
        for (int i=0; i<musicianNr; i++) {
            stackMusicians.push(i);
        }
        Collections.shuffle(stackMusicians);

		for(int i=0; i<this.userNr; i++)
		{
			
			User u = new User();
			users.add(u);
			u.setBirthDate(faker.date().birthday().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			u.setCountry(faker.address().country());
			u.setJoinDate(faker.date().past(10*365,TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
			u.setUsername(faker.name().username());
		}
		
		for(int i=0;i<musicianNr; i++)
		{
			int activeTillChance = rand.nextInt(100);
			int stageNameChance = rand.nextInt(100);
			int genreTypeRandom = rand.nextInt(5);
			int genreRandom = rand.nextInt(5);
			Musician m = new Musician();
			artists.add(m);
			m.setActiveSince(Year.parse(String.valueOf(faker.number().numberBetween(1970, 2020))));
			if(activeTillChance < 80)
			{
				m.setActiveTill(null);
			}
			else
			{
				m.setActiveTill(Year.parse(String.valueOf(faker.number().numberBetween(m.getActiveSince().getValue(), 2022))));
			}
			m.setCountry(faker.address().country());
			m.setGenre(genre[genreTypeRandom][genreRandom]); 
			m.setName(faker.name().firstName());
			m.setSurname(faker.name().lastName());
			if(stageNameChance < 70)
			{
				m.setStageName(m.getName() + " " + m.getSurname());
			}
			else
			{
				int stageRandom = rand.nextInt(2) + 1;
				m.setStageName(String.join(" ", faker.lorem().words(stageRandom)));
			}
		}
		
		for(int i=0; i<this.bandNr; i++)
		{
			int activeTillChance = rand.nextInt(100);
			int genreTypeRandom = rand.nextInt(5);
			int genreRandom = rand.nextInt(5);
			Band b = new Band();
			artists.add(b);
			b.setActiveSince(Year.parse(String.valueOf(faker.number().numberBetween(1970, 2020))));
			if(activeTillChance < 80)
			{
				b.setActiveTill(null);
			}
			else
			{
				b.setActiveTill(Year.parse(String.valueOf(faker.number().numberBetween(b.getActiveSince().getValue(), 2022))));
			}
			b.setCountry(faker.address().country());
			b.setGenre(genre[genreTypeRandom][genreRandom]); 
			int stageRandom = rand.nextInt(2) + 1;
			b.setStageName(String.join(" ", faker.lorem().words(stageRandom)));
			int membersNr = rand.nextInt(4) + 2;
			for (int j=0;j<membersNr;j++)
			{
	        	genreRandom = rand.nextInt(5);
	        	Artist artist = artists.stream()
						  .filter(a -> Long.valueOf(stackMusicians.peek().toString()).equals(a.getArtistID()))
						  .findAny()
						  .orElse(null);
	        	artist.setGenre(genre[genreTypeRandom][genreRandom]);
	        	b.getMembersID().add(Long.valueOf(stackMusicians.pop()));
			}
			
			int albumNr = rand.nextInt(3) + 1;
			for(int j=0;j<albumNr;j++)
			{
				int songNr = rand.nextInt(5) + 5;
				int albumWordsNr = rand.nextInt(3) + 1;
				LocalDate albumDate;
				cld.set(Calendar.YEAR, b.getActiveSince().getValue());
				if(b.getActiveTill() != null)
				{
					cld2.set(Calendar.YEAR, b.getActiveTill().getValue());
					albumDate = faker.date().between(cld.getTime(), cld2.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				}
				else {
					albumDate = faker.date().between(cld.getTime(), new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				}
				String album = String.join(" ", faker.lorem().words(albumWordsNr));
				for (int k=0;k<songNr;k++)
				{
					int songWordsNr = rand.nextInt(3) + 1;
					genreRandom = rand.nextInt(5);
					Song s = new Song();
			        songs.add(s);
			        s.setAlbum(album);
			        s.setArtistID(b.getArtistID());
			        s.setGenre(genre[genreTypeRandom][genreRandom]);
			        s.setLength((short)faker.number().numberBetween(90, 420));
			        s.setName(String.join(" ", faker.lorem().words(songWordsNr)));
			        s.setReleaseDate(albumDate);
			        b.getSongs().add(s.getSongID());
				}
			}
		} 
		
		for(Integer m : stackMusicians)
		{
			Artist artist = artists.stream()
					  .filter(a -> Long.valueOf(m.toString()).equals(a.getArtistID()))
					  .findAny()
					  .orElse(null);
			int genreTypeRandom = rand.nextInt(5);
			int genreRandom = rand.nextInt(5);
			artist.setGenre(genre[genreTypeRandom][genreRandom]);
			int albumNr = rand.nextInt(3) + 1;
			for(int j=0;j<albumNr;j++)
			{
				int songNr = rand.nextInt(5) + 5;
				int albumWordsNr = rand.nextInt(3) + 1;
				LocalDate albumDate;
				cld.set(Calendar.YEAR, artist.getActiveSince().getValue());
				if(artist.getActiveTill() != null)
				{
					cld2.set(Calendar.YEAR, artist.getActiveTill().getValue());
					albumDate = faker.date().between(cld.getTime(), cld2.getTime()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				}
				else {
					albumDate = faker.date().between(cld.getTime(), new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				}
				String album = String.join(" ", faker.lorem().words(albumWordsNr));
				for (int k=0;k<songNr;k++)
				{
					int songWordsNr = rand.nextInt(3) + 1;
					genreRandom = rand.nextInt(5);
					Song s = new Song();
			        songs.add(s);
			        s.setAlbum(album);
			        s.setArtistID(artist.getArtistID());
			        s.setGenre(genre[genreTypeRandom][genreRandom]);
			        s.setLength((short)faker.number().numberBetween(90, 420));
			        s.setName(String.join(" ", faker.lorem().words(songWordsNr)));
			        s.setReleaseDate(albumDate);
			        artist.getSongs().add(s.getSongID());
				}
			}
		}
        
        int songNr = songs.size();
        
        for(User u : users)
        {
        	int lNr = rand.nextInt(400) + listeningNr;
        	int playlistNr = rand.nextInt(3);
        	for (int i=0;i<lNr;i++)
        	{
        		Long songRandom = Long.valueOf(rand.nextInt(songNr));
        		Song song = songs.stream()
  					  .filter(s -> songRandom.equals(s.getSongID()))
  					  .findAny()
  					  .orElse(null);
        		Listening l = new Listening();
        		l.setSongID(songRandom);
        		Date constraint = new GregorianCalendar(2013, 1, 1).getTime();
        		Date songDate = Date.from(song.getReleaseDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        		if(constraint.before(songDate))
        		{
        			constraint = songDate;
        		}
        		LocalDate randomDate = faker.date().between(constraint, new Date()).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        		int hour = rand.nextInt(24);
        		int minute = rand.nextInt(60);
        		LocalTime randomTime = LocalTime.of(hour, minute);
        		l.setListeningTime(LocalDateTime.of(randomDate, randomTime));
        		u.getListenings().add(l);
        	}
        	
        	for(int i=0;i<playlistNr;i++)
        	{
        		Playlist p = new Playlist();
    	        PlaylistConfig pConfig = new PlaylistConfig(faker.lorem().word(),rand.nextBoolean());
    	        u.getPlaylists().add(p);
    	        int playlistSongNr = rand.nextInt(20) + 5;
    	        for (int j=0;j<playlistSongNr;j++)
    	        {
    	        	Long songRandom = Long.valueOf(rand.nextInt(songNr));
    	        	Song song = songs.stream()
    	  					  .filter(s -> songRandom.equals(s.getSongID()))
    	  					  .findAny()
    	  					  .orElse(null);
    	        	p.addSong(song);
    	        }
    	        p.setConfig(pConfig);
        	}
        }
    }

	/**
	 * Initializes "database" with data.
	 */
	public DataInitializer() {
		this.past10Years = new Stack<Integer>();
        for (int i=2022; i>2012; i--) {
            this.past10Years.push(i);
        }
		init(this.artists, this.songs, this.users);
	}

	/**
	 * @return the list of artists
	 */
	public List<Artist> getArtists() {
		readLock.lock();
		try {
			return artists;
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * @return the list of songs
	 */
	public List<Song> getSongs() {
		readLock.lock();
		try {
			return songs;
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * @return the list of users
	 */
	public List<User> getUsers() {
		readLock.lock();
		try {
			return users;
		} finally {
			readLock.unlock();
		}
	}

	/**
	 * @return the stack containing past 10 years
	 */
	public Stack<Integer> getPast10Years() {
		return past10Years;
	}

	/**
	 * @return the list of songs which were listened over 100 times in any year and years in which they did so
	 */
	public Map<Long, List<Integer>> getSongsOver100() {
		readLock.lock();
		try {
			return songsOver100;
		} finally {
			readLock.unlock();
		}
	}
	
	/**
	 * Add the year to the song which had over 100 listenings
	 */
	public void addYearSongsOver100(Long key, Integer year) {
		writeLock.lock();
		try {
			this.songsOver100.get(key).add(year);
		}
		finally {
			writeLock.unlock();
		}
	}
	
	/**
	 * Add the new song to the map of songs which had over 100 listenings
	 */
	public void addSongsOver100(Long key) {
		writeLock.lock();
		try {
			this.songsOver100.put(key, new ArrayList<Integer>());
		}
		finally {
			writeLock.unlock();
		}
	}
	
	/**
	 * Prints songs which had over 100 listenings in most years
	 */
	public void printTopSongsOver100()
	{
		int mostYears = 0;
		for(Map.Entry<Long, List<Integer>> entry : this.songsOver100.entrySet())
		{
			if (entry.getValue().size() > mostYears)
			{
				mostYears = entry.getValue().size();
			}
		}
		for(Map.Entry<Long, List<Integer>> entry : this.songsOver100.entrySet())
		{
			Song song = this.songs.stream()
					  .filter(s -> entry.getKey().equals(s.getSongID()))
					  .findAny()
					  .orElse(null);
			if(entry.getValue().size() == mostYears)
			{
				String tmpString = StringUtils.join(entry.getValue(), ", ");
				System.out.println(String.format("%s in years %s", song.getName(), tmpString));
			}
		}
	}

	/**
	 * @return the 2d array of all genres
	 */
	public String[][] getGenre() {
		return genre;
	}

}
