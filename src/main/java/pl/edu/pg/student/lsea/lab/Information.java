package pl.edu.pg.student.lsea.lab;

import java.util.Collections;
import java.util.Scanner;

import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.configuration.DataInitializer;
import pl.edu.pg.student.lsea.lab.song.Song;
import pl.edu.pg.student.lsea.lab.user.User;
/**
 * Class for running the application.
 * Based on the specified arguments it displays information about the data contained in the application.
 * @author Jakub Górniak
 */
public class Information
{
    public static void main(String[] args)
    {
    	Scanner scanner = new Scanner(System.in);
    	DataInitializer data = new DataInitializer();

    	String input = "";
    	while(!input.equals("exit"))
    	{
			System.out.println("Main menu");
    		System.out.println("You can find information on the song database, type help for more info.");
    		input = scanner.next();
	    	switch (input) {
			case "song":
				System.out.println("Please specify name of the song which information would you like to view.");
				System.out.println("Alternatively input 'all' to view all songs");
				String son = scanner.next();
				String songName = son + scanner.nextLine();
				if(songName.equals("all"))
				{
					System.out.println("Would you like to sort the songs?");
					System.out.println("Type yes or no for the songs in the current order. Wrong input results in no answer.");
					String sorting = scanner.next();
					if (sorting.equals("yes")){
						while(!sorting.equals("exit"))
						{
							System.out.println("Song sorting submenu");
							System.out.println("Please specify how would you like to sort the songs, type help for more info.");
							sorting = scanner.next();
							switch (sorting) {
							case "name":
								Collections.sort(data.getSongs(), Song.SongComparator.NAME);
								System.out.println(data.getSongs());
								sorting = "exit";
								break;
							case "album":
								Collections.sort(data.getSongs(), Song.SongComparator.ALBUM);
								System.out.println(data.getSongs());
								sorting = "exit";
								break;
							case "artist":
								Collections.sort(data.getSongs(), Song.SongComparator.ARTIST_ID);
								System.out.println(data.getSongs());
								sorting = "exit";
								break;
							case "release":
								Collections.sort(data.getSongs(), Song.SongComparator.RELEASE_DATE);
								System.out.println(data.getSongs());
								sorting = "exit";
								break;
							case "genre":
								Collections.sort(data.getSongs(), Song.SongComparator.GENRE);
								System.out.println(data.getSongs());
								sorting = "exit";
								break;
							case "length":
								Collections.sort(data.getSongs(), Song.SongComparator.LENGTH);
								System.out.println(data.getSongs());
								sorting = "exit";
								break;
							case "help":
								System.out.println("Arguments:");
								System.out.println("name - sort by song name");
								System.out.println("album - sort by song album name");
								System.out.println("artist - sort by an artist releasing a song");
								System.out.println("release - sort by song release date");
								System.out.println("genre - sort by song genre");
								System.out.println("length - sort by song length");
								System.out.println("exit - exit back to the main menu");
								break;
							case "exit":
								sorting = "exit";
								break;
							default:
								System.out.println("Please input a valid argument. Use help for more information.");
								break;
							}
						}
					}
					else {
						System.out.println(data.getSongs());
					}
				}
				else {
					Song song = data.getSongs().stream()
							  .filter(s -> songName.equals(s.getName()))
							  .findAny()
							  .orElse(null);
					if(song != null)
					{
						System.out.println(song);
					}
					else {
						System.out.println("Song not found.");
					}
				}
				break;
			case "artist":
				System.out.println("Please specify stage name of the artist which information would you like to view.");
				System.out.println("Alternatively input 'all' to view all artists");
				String sta = scanner.next();
				String stageName = sta + scanner.nextLine();
				if(stageName.equals("all"))
				{
					System.out.println("Would you like to sort the artists?");
					System.out.println("Type yes or no for the artists in the current order. Wrong input results in no answer.");
					String sorting = scanner.next();
						if (sorting.equals("yes")){
							while(!sorting.equals("exit"))
							{
								System.out.println("Artist sorting submenu");
								System.out.println("Please specify how would you like to sort the artists, type help for more info.");
								sorting = scanner.next();
								switch (sorting) {
								case "name":
									Collections.sort(data.getArtists(), Artist.ArtistComparator.STAGE_NAME);
									System.out.println(data.getArtists());
									sorting = "exit";
									break;
								case "country":
									Collections.sort(data.getArtists(), Artist.ArtistComparator.COUNTRY);
									System.out.println(data.getArtists());
									sorting = "exit";
									break;
								case "genre":
									Collections.sort(data.getArtists(), Artist.ArtistComparator.GENRE);
									System.out.println(data.getArtists());
									sorting = "exit";
									break;
								case "active":
									Collections.sort(data.getArtists(), Artist.ArtistComparator.ACTIVE_SINCE);
									System.out.println(data.getArtists());
									sorting = "exit";
									break;
								case "help":
									System.out.println("Arguments:");
									System.out.println("name - sort by artist stage name");
									System.out.println("country - sort by artist country");
									System.out.println("genre - sort by artist genre");
									System.out.println("active - sort by a year since which artist is active");
									System.out.println("exit - exit back to the main menu");
									break;
								case "exit":
									sorting = "exit";
									break;
								default:
									System.out.println("Please input a valid argument. Use help for more information.");
									break;
								}
							}
						}
						else {
							System.out.println(data.getArtists());
						}
					
				}
				else {
					Artist artist = data.getArtists().stream()
						  .filter(a -> stageName.equals(a.getStageName()))
						  .findAny()
						  .orElse(null);
					if(artist != null)
					{
						System.out.println(artist);
					}
					else {
						System.out.println("Artist not found.");
					}
				}		
				break;
			case "user":
				System.out.println("Please specify username of the user which information would you like to view.");
				System.out.println("Alternatively input 'all' to view all users");
				String use = scanner.next();
				String username = use + scanner.nextLine();
				if(username.equals("all"))
				{
					System.out.println("Would you like to sort the users?");
					System.out.println("Type yes or no for the users in the currrent order. Wrong input results in no answer.");
					String sorting = scanner.next();
						if (sorting.equals("yes")){
							while(!sorting.equals("exit"))
							{
								System.out.println("User sorting submenu");
								System.out.println("Please specify how would you like to sort the users, type help for more info.");
								sorting = scanner.next();
								switch (sorting) {
								case "name":
									Collections.sort(data.getUsers(), User.UserComparator.USERNAME);
									System.out.println(data.getUsers());
									sorting = "exit";
									break;
								case "birth":
									Collections.sort(data.getUsers(), User.UserComparator.BIRTH_DATE);
									System.out.println(data.getUsers());
									sorting = "exit";
									break;
								case "country":
									Collections.sort(data.getUsers(), User.UserComparator.COUNTRY);
									System.out.println(data.getUsers());
									sorting = "exit";
									break;
								case "join":
									Collections.sort(data.getUsers(), User.UserComparator.JOIN_DATE);
									System.out.println(data.getUsers());
									sorting = "exit";
									break;
								case "help":
									System.out.println("Arguments:");
									System.out.println("name - sort by user username");
									System.out.println("birth - sort by user birth date");
									System.out.println("country - sort by user country");
									System.out.println("join - sort by user join date");
									System.out.println("exit - exit back to the main menu");
									break;
								case "exit":
									sorting = "exit";
									break;
								default:
									System.out.println("Please input a valid argument. Use help for more information.");
									break;
								}
							}
						}
						else {
							System.out.println(data.getUsers());
						}
				}
				else {
					User user = data.getUsers().stream()
							  .filter(u -> username.equals(u.getUsername()))
							  .findAny()
							  .orElse(null);
					if(user != null)
					{
						System.out.println(user);
					}
					else {
						System.out.println("User not found.");
					}
				}
				break;
			case "help":
				System.out.println("Arguments:");
				System.out.println("help - show help with all possible arguments");
				System.out.println("song - show information about chosen or all songs");
				System.out.println("artist - show information about chosen or all artists");
				System.out.println("user - show information about chosen or all users");
				System.out.println("exit - exit the app");
				break;
			case "exit":
				input = "exit";
				break;
			default:
				System.out.println("Please input a valid argument. Use help for more information.");
				break;
			}
    	}
    	scanner.close();
    }
    
}
