/**
 * 
 */
package pl.edu.pg.student.lsea.lab.analysis;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.MultiKeyMap;
import org.apache.commons.math3.stat.inference.OneWayAnova;

import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.configuration.DataInitializer;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;
import pl.edu.pg.student.lsea.lab.user.listening.Listening;
import weka.core.ContingencyTables;

/**
 * Class for analyzing a particular year of the data in the database
 * @author Jakub Górniak
 */
public class AnalysisYear {

	/** database object with data for analysis */
	private DataInitializer data;
	
	/** year which is analyzed */
	private Integer analysisYear;
	
	/** list of listened song IDs */
	private List<Long> songIDs = new ArrayList<Long>();
	
	/** map of genres of listened songs, 
	 * genre of the song is the key, 
	 * count of listened songs of this genre is the value
	 */
	private Map<String, Long> genres = new HashMap<String, Long>();
	
	/** map of genres of listened artists, 
	 * artist is the key, 
	 * count of listened songs of this artist is the value
	 */
	private Map<Long, Long> artistCounts = new HashMap<Long, Long>();
	
	/** multi key map of genres of listened songs and countries of the users listening, 
	 * genre of the song and user's country is the key, 
	 * count of listened songs of this genre by users from certain country is the value
	 */
	private MultiKeyMap<String,Long> countryGenre = new MultiKeyMap<String, Long>();
	
	/** multi key map of artists of listened songs and ages of the users listening, 
	 * artist of the song and user's age is the key, 
	 * count of listened songs of this artist by users with certain age is the value
	 */
	private MultiKeyMap<Long,Long> ageArtist = new MultiKeyMap<Long, Long>();
	
	/** map of countries of listening users, 
	 * user's country is the key, 
	 * count of listened songs by user's from this country is the value
	 */
	private Map<String, Long> countries = new HashMap<String, Long>();
	
	/** age interval of the users in the database */
	private final int ageInterval = 66 - 17;
	
	/** count matrix of user's countries and song genres */
	private double[][] matrixCountryGenre;
	
	/** count matrix of user's ages and song artists */
	private double[][] matrixAgeArtist;
	
	/** map of song occurrences,
	 * id of the song is the key,
	 * count of the song listening is the value
	 */
	private Map<Long, Long> occurrences;
	
	/**
	 * Constructor of the analysis object.
	 * @param data database object with data for analysis
	 * @param analysisYear year which is analyzed
	 */
	public AnalysisYear(DataInitializer data, Integer analysisYear) {
		this.data = data;
		this.analysisYear = analysisYear;
	}
	
	/**
	 * Method creating artistCount map
	 */
	private void createArtistCount ()
	{
		for(Artist a : data.getArtists())
		{
			artistCounts.put(a.getArtistID(), Long.valueOf(0));
		}
	}
	
	/**
	 * Method creating genres map
	 */
	private void createGenres ()
	{
		for(String[] arr : data.getGenre())
		{
			for(String s : arr)
			{
				genres.put(s, Long.valueOf(0));
			}
		}
	}
	
	/**
	 * Method creating songIDs list and countries map
	 */
	private void createSongIDsAndCountries ()
	{
		for (User u : data.getUsers())
		{
			songIDs.addAll(
					u.getListenings().stream()
					.filter(l -> analysisYear.equals(l.getListeningTime().getYear()))
					.map(Listening::getSongID)
					.collect(Collectors.toList()));
			countries.put(u.getCountry(),Long.valueOf(0));
		}
	}
	
	/**
	 * Method creating countryGenre map
	 */
	private void createCountryGenre ()
	{
		for(String[] arr : data.getGenre())
		{
			for(String s : arr)
			{
				for (Map.Entry<String, Long> entry : countries.entrySet()) {
					countryGenre.put(s, entry.getKey(), Long.valueOf(0));
			    }
			}
		}
	}
	
	/**
	 * Method creating ageArtist map
	 */
	private void createAgeArtist ()
	{
		for(int i=17;i<66;i++)
		{
			for (Map.Entry<Long, Long> entry : artistCounts.entrySet()) {
				ageArtist.put(Long.valueOf(i), entry.getKey(), Long.valueOf(0));
		    }
		}
	}
	
	/**
	 * Method filling the ageArtist and countryGenre maps with data
	 */
	private void fillCountryGenreAndAgeArtist ()
	{
		for(User u : data.getUsers())
		{
			Long userAge = Long.valueOf(Period.between(u.getBirthDate(), LocalDate.now()).getYears());
			List <Listening> userListeningsYear = u.getListenings().stream().filter(l -> analysisYear.equals(l.getListeningTime().getYear())).collect(Collectors.toList());
			for(Listening l : userListeningsYear)
			{
				Song song = data.getSongs().stream()
						  .filter(s -> l.getSongID().equals(s.getSongID()))
						  .findAny()
						  .orElse(null);
				countryGenre.put(song.getGenre(), u.getCountry(), countryGenre.get(song.getGenre(), u.getCountry()) + 1);
				ageArtist.put(userAge, song.getArtistID(), ageArtist.get(userAge, song.getArtistID()) + 1);
			}
		}
	}
	
	/**
	 * Method creating matrixCountryGenre 2d matrix
	 */
	private void createMatrixCountryGenre()
	{
		matrixCountryGenre = new double[data.getGenre().length*data.getGenre()[0].length][countries.size()];
	}
	
	/**
	 * Method filling the 2d matrix matrixCountryGenre with data
	 */
	private void fillMatrixCountryGenre() {
		for(int i=0; i<data.getGenre().length;i++)
		{
			for(int j=0;j<data.getGenre()[0].length;j++)
			{
				int k = 0;
				for(Map.Entry<String, Long> entry : countries.entrySet())
				{
					matrixCountryGenre[i*5 + j][k] = (double)countryGenre.get(data.getGenre()[i][j], entry.getKey());
					k++;
				}
			}
		}	
	}
	
	/**
	 * Method creating matrixAgeArtist 2d matrix
	 */
	private void createMatrixAgeArtist()
	{
		matrixAgeArtist = new double[ageInterval][artistCounts.size()];
	}
	
	/**
	 * Method filling the 2d matrix matrixAgeArtist with data 
	 */
	private void fillMatrixAgeArtist() {
		for(int i=0; i<ageInterval;i++)
		{
			int j=0;
			for(Map.Entry<Long, Long> entry : artistCounts.entrySet())
			{
				matrixAgeArtist[i][j] = (double)ageArtist.get(Long.valueOf(i+17), entry.getKey());
				j++;
			}
		}	
	}
	
	/**
	 * Method creating occurrences map
	 */
	private void createOccurrences () {
		occurrences = songIDs.stream().collect(Collectors.groupingBy(s -> s, Collectors.counting()));
	}
	
	/**
	 * Method processing occurrences map.
	 * Fills the genres and artistCount maps and adds songs which had over 100 listenings to the database
	 */
	private void processOccurrences () {
		for (Map.Entry<Long, Long> entry : occurrences.entrySet()) {
			Song song = data.getSongs().stream()
					  .filter(s -> entry.getKey().equals(s.getSongID()))
					  .findAny()
					  .orElse(null);
			genres.put(song.getGenre(), genres.get(song.getGenre()) + entry.getValue());
			artistCounts.put(song.getArtistID(), artistCounts.get(song.getArtistID()) + entry.getValue());
			if(entry.getValue() > 100)
			{
				if(data.getSongsOver100().get(entry.getKey()) != null)
				{
					if(!data.getSongsOver100().get(entry.getKey()).contains(analysisYear))
					{
						data.addYearSongsOver100(entry.getKey(), analysisYear);
					}
				}
				else {
					data.addSongsOver100(entry.getKey());
					data.addYearSongsOver100(entry.getKey(), analysisYear);
				}
			}
		}
	}
	
	/**
	 * Method finding the top song listened in analyzed year
	 * @param printWriter print writer object for writing the result to the file
	 */
	private void findTopSong(PrintWriter printWriter)
	{
		Optional<Entry<Long, Long>> maxEntrySong = occurrences.entrySet().stream()
				.max(Comparator.comparing(Map.Entry::getValue));
		Long maxSongID = maxEntrySong.get().getKey();
		Long maxSongCount = maxEntrySong.get().getValue();
		Song maxSong = data.getSongs().stream()
				  .filter(s -> maxSongID.equals(s.getSongID()))
				  .findAny()
				  .orElse(null);
		Artist maxSongArtist = data.getArtists().stream()
				  .filter(a -> maxSong.getArtistID().equals(a.getArtistID()))
				  .findAny()
				  .orElse(null);
		
		printWriter.println(String.format("Top song: %s (genre: %s, artist: %s) with %d listenings.", maxSong.getName(), maxSong.getGenre(), maxSongArtist.getStageName(), maxSongCount));
	}
	
	/**
	 * Method finding the top artist listened in analyzed year
	 * @param printWriter print writer object for writing the result to the file
	 */
	private void findTopArtist(PrintWriter printWriter)
	{
		Optional<Entry<Long, Long>> maxEntryArtist = artistCounts.entrySet().stream()
				.max(Comparator.comparing(Map.Entry::getValue));
		Long maxArtistID = maxEntryArtist.get().getKey();
		Long maxArtistCount = maxEntryArtist.get().getValue();
		Artist maxArtist = data.getArtists().stream()
				  .filter(a -> maxArtistID.equals(a.getArtistID()))
				  .findAny()
				  .orElse(null);
		printWriter.println(String.format("Top artist: %s (genre: %s) with %d listenings.", maxArtist.getStageName(), maxArtist.getGenre(), maxArtistCount));
	}
	
	/**
	 * Method finding the top genre listened in analyzed year
	 * @param printWriter print writer object for writing the result to the file
	 */
	private void findTopGenre(PrintWriter printWriter)
	{
		Optional<Entry<String, Long>> maxEntryGenre = genres.entrySet().stream()
				.max(Comparator.comparing(Map.Entry::getValue));
		String maxGenre = maxEntryGenre.get().getKey();
		Long maxGenreCount = maxEntryGenre.get().getValue();
		
		printWriter.println(String.format("Top genre: %s with %d listenings.", maxGenre, maxGenreCount));
	}
	
	/**
	 * Method calculating Cramer's V correlation between genre of songs and countries of users in analyzed year
	 * @param printWriter print writer object for writing the result to the file
	 */
	private void calculateCramerV(PrintWriter printWriter) {
		double cramerVCorrelation = ContingencyTables.CramersV(matrixCountryGenre);
		printWriter.println(String.format("Correlation between genre of the listened songs and the country of the user(Cramer's V coefficient): %.3f", cramerVCorrelation));
	}

	/**
	 * Method calculating one way ANOVA between artist of songs and ages of users in analyzed year
	 * @param printWriter print writer object for writing the result to the file
	 */
	private void calculateOneWayANOVA(PrintWriter printWriter) {
		List<double[]> collectionAgeArtist = new ArrayList<double[]>();
		for (double[] array : matrixAgeArtist) {
		    collectionAgeArtist.add(array);
		}
		OneWayAnova anova = new OneWayAnova();
		printWriter.println(String.format("Correlation between age of the user and the artist listened (One Way ANOVA F value): %.3f", anova.anovaFValue(collectionAgeArtist)));
	}
	
	/**
	 * Method performing analysis
	 */
	public void performAnalysis()
	{
		System.out.println(String.format("Writing analysis data about year %d to the file.", analysisYear));
		File file = new File("Analysis - " + analysisYear.toString() +".txt");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    PrintWriter printWriter = new PrintWriter(fileWriter);
		printWriter.println(String.format("Year %d", analysisYear));
		
		createArtistCount();
		createGenres();
		createSongIDsAndCountries();
		createCountryGenre();
		createAgeArtist();
		
		fillCountryGenreAndAgeArtist();
		
		createMatrixCountryGenre();
		fillMatrixCountryGenre();
		
		createMatrixAgeArtist();
		fillMatrixAgeArtist();
		
		printWriter.println(String.format("All listenings: %d", songIDs.size()));
		
		createOccurrences();
	
		findTopSong(printWriter);

		processOccurrences();
		
		findTopArtist(printWriter);
		
		findTopGenre(printWriter);
		
		calculateCramerV(printWriter);
		
		calculateOneWayANOVA(printWriter);
		
		printWriter.close();
		System.out.println(String.format("Analysis data about year %d written to the file.", analysisYear));
	}

}
