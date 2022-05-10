package pl.edu.pg.student.lsea.lab;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


import pl.edu.pg.student.lsea.lab.analysis.AnalysisThread;
import pl.edu.pg.student.lsea.lab.analysis.AnalysisYear;
import pl.edu.pg.student.lsea.lab.analysis.PerformanceAnalysis;
import pl.edu.pg.student.lsea.lab.artist.Artist;
import pl.edu.pg.student.lsea.lab.configuration.DataInitializer;
import pl.edu.pg.student.lsea.lab.user.User;
import pl.edu.pg.student.lsea.lab.user.playlist.Playlist;

/**
 * Class for running the application.
 * Based on the specified arguments it displays listening statistics about the user of the application.
 * @author Jakub Górniak
 */
public class Statistics
{
    public static void main(String[] args) throws IOException
    {
    	Scanner scanner = new Scanner(System.in);
    	DataInitializer data = new DataInitializer();
    	String input = "";
    	while(!input.equals("exit"))
    	{
    		System.out.println("Main menu");
    		System.out.println("You can perform actions on the song database, type help for more info.");
    		input = scanner.next();
    		switch (input) {
			case "user":
				System.out.println("Please specify username of the user which information would you like to view:");
				String use = scanner.next();
				String username = use + scanner.nextLine();	
				User user = data.getUsers().stream()
				  .filter(u -> username.equals(u.getUsername()))
				  .findAny()
				  .orElse(null);
				if(user == null)
				{
					System.out.println("User not found.");
					break;
				}
				String action = "";
				while(!action.equals("exit"))
				{
					System.out.println("User submenu");
					System.out.println("Please specify an action for the chosen user, type help for more info.");
					action = scanner.next();
					switch (action) {
					case "show":
						System.out.println(username + " listening statistics (song names and number of listenings):");
						System.out.println(user.showStatistics(data.getSongs()));
						break;
					case "playlist":
						System.out.println("Please specify playlist id of the playlist which information would you like to view or type all to display them all:");
						String playlistID = scanner.next();
						if(playlistID.equals("all"))
						{
							System.out.println(user.getPlaylists());
							break;
						}
						Playlist playlist = user.getPlaylists().stream()
						  .filter(p -> playlistID.equals(p.getPlaylistID().toString()))
						  .findAny()
						  .orElse(null);
						if(playlist == null)
						{
							System.out.println("Playlist not found.");
							break;
						}
						String actionPlaylist = "";
						while(!actionPlaylist.equals("exit"))
						{
							System.out.println("Playlist submenu");
							System.out.println("Please specify an action for the chosen playlist, type help for more info.");
							actionPlaylist = scanner.next();
							switch (actionPlaylist) {
							case "show":
								System.out.println(playlist.getConfig().getName() + " playlist info:");
								System.out.println(playlist);
								break;
							case "copy":
								System.out.println("Please specify username of the user which is the target for copying the playlist:");
								String tuse = scanner.next();
								String targetUsername = tuse + scanner.nextLine();
								User targetUser = data.getUsers().stream()
										  .filter(u -> targetUsername.equals(u.getUsername()))
										  .findAny()
										  .orElse(null);
								if(targetUser == null)
								{
									System.out.println("Target user not found.");
									break;
								}
								int id = Integer.parseInt(playlistID);
								targetUser.copyPlaylist(user, id);
								System.out.println("Playlist " + playlistID + " copied to " + targetUser.getUsername() + " from " + user.getUsername());
								break;
							case "change":
								String optionChange = "";
								while(!optionChange.equals("exit"))
								{
									System.out.println("Playlist changing submenu");
									System.out.println("Please specify what information you would like to change, type help for more info.");
									optionChange = scanner.next();
									switch (optionChange) {
									case "name":
										System.out.println("Please input the new name of the playlist.");
										String nam = scanner.next();
										String nameChange = nam + scanner.nextLine();
										playlist.getConfig().setName(nameChange);
										System.out.println("Playlist name changed to " + nameChange);
										break;
									case "shuffle":
										playlist.getConfig().switchShuffle();
										System.out.println("Playlist shuffle switched to " + playlist.getConfig().getShuffle());
										break;
									case "help":
										System.out.println("Arguments:");
										System.out.println("help - show help");
										System.out.println("name - change name of the playlist");
										System.out.println("shuffle - switch shuffle mode of the playlist");
										System.out.println("exit - exit back to the playlist submenu");
										break;
									case "exit":
										optionChange = "exit";
										break;
									default:
										System.out.println("Please input a valid argument. Use help for more information.");
										break;
									}
								}	
								break;
							case "help":
								System.out.println("Arguments:");
								System.out.println("help - show help");
								System.out.println("show - show the playlist information");
								System.out.println("copy - copy this playlist from this user to other user");
								System.out.println("change - enter playlist changing submenu");
								System.out.println("exit - exit back to the user submenu");
								break;
							case "exit":
								actionPlaylist = "exit";
								break;
							default:
								System.out.println("Please input a valid argument. Use help for more information.");
								break;
							}
						}	
						break;
					case "help":
						System.out.println("Arguments:");
						System.out.println("help - show help");
						System.out.println("show - show the listening information about user");
						System.out.println("playlist - enter playlist submenu");
						System.out.println("exit - exit back to the main menu");
						break;
					case "exit":
						action = "exit";
						break;
					default:
						System.out.println("Please input a valid argument. Use help for more information.");
						break;
					}
				}	
				break;
			case "artist":
				System.out.println("Please specify stage name of the artist which information would you like to view.");
				String sta = scanner.next();
				String stageName = sta + scanner.nextLine();
				Artist artist = data.getArtists().stream()
				  .filter(a -> stageName.equals(a.getStageName()))
				  .findAny()
				  .orElse(null);
				if(artist != null)
				{
					System.out.println(stageName + " song count:");
					System.out.println(artist.showStatistics());
				}
				else {
					System.out.println("Artist not found.");
				}
				break;
			case "analyse":
				String optionChange = "";
				while(!optionChange.equals("exit"))
				{
					System.out.println("Analysis submenu");
					System.out.println("Choose what type of analysis you want to perform. Type help for more info.");
					String ana = scanner.next();
					String analysis = ana + scanner.nextLine();
					long start, end;
					double duration;
					switch (analysis) {
					case "parallel":
						System.out.println("Please specify how many threads you want to run.");
						Integer threadNr;
						try {
							threadNr = scanner.nextInt();
						}
						catch (Exception e) {
							System.out.println("Invalid input. Please input a positive number in integer range.");
							break;
						}
						if(threadNr <= 0)
						{
							System.out.println("Invalid input. Please input a positive number in integer range.");
							break;
						}
						start = System.currentTimeMillis();
						PerformanceAnalysis p = new PerformanceAnalysis();
						ExecutorService es = Executors.newFixedThreadPool(threadNr);
						for(Integer y : data.getPast10Years())
						{
							AnalysisYear a = new AnalysisYear(data, y);
							es.execute(new AnalysisThread(a, p));
						}
						es.shutdown();
						try {
							if(!es.awaitTermination(2, TimeUnit.MINUTES)) {
								es.shutdownNow();
							};
						} catch (InterruptedException e) {
							es.shutdownNow();
						}
						System.out.println("All songs which had over 100 listenings in most years in last 10 years:");
						data.printTopSongsOver100();
						
						p.printResults(threadNr.toString() + "-thread analysis.txt");
						end = System.currentTimeMillis();
						duration = ((double)(end-start))/1000;
						System.out.println(String.format("Calculation time in seconds: %.2f.", duration));
						break;
					case "sequential":
						start = System.currentTimeMillis();
						PerformanceAnalysis p2 = new PerformanceAnalysis();
						for(Integer y : data.getPast10Years())
						{
							AnalysisYear a = new AnalysisYear(data, y);
							a.performAnalysis();
							p2.endNotification();
						}
						System.out.println("All songs which had over 100 listenings in most years in last 10 years:");
						data.printTopSongsOver100();
						
						p2.printResults("sequential analysis.txt");
						end = System.currentTimeMillis();
						duration = ((double)(end-start))/1000;
						System.out.println(String.format("Calculation time in seconds: %.2f.", duration));
						break;
					case "help":
						System.out.println("Arguments:");
						System.out.println("help - show help");
						System.out.println("parallel - perform parallel analysis");
						System.out.println("sequential - perform sequential analysis");
						System.out.println("exit - exit back to the main menu");
						break;
					case "exit":
						optionChange = "exit";
						break;
					default:
						System.out.println("Please input a valid argument. Use help for more information.");
						break;
					}
				}
				break;
			case "help":
				System.out.println("Arguments:");
				System.out.println("help - show help");
				System.out.println("artist - show count of songs of chosen artist");
				System.out.println("user - enter user submenu");
				System.out.println("analyse - analyse the listenng data");
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