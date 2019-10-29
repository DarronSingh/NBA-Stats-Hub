/*
This class handles all the users requests. When they call the service this class will be calling and running the appropriate methods. This class uses
Hashmaps, arrays and ArrayLists to help get access to the csv and sort the data in a way that makes it easier to find what the user wants.  At the start of the program
this hashmap is created so that hashmap can be accessed whenever the user is requesting data.

@date modified October 24 2019
@author Darron Singh


 */
import java.io.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class nbaImpl extends UnicastRemoteObject implements nbaInterface {
    private String name;
    HashMap<String, String[]> stats;
    String[] rawStats;
    String csvFile = "nbastats.csv";

    /*
    This method is run at the beginning and automatically loads the hashmap for the csv file

     */
    public nbaImpl(String s) throws RemoteException {
        super();
        name = s;
        loadHashMap();
    }

    /*
    This method is what loads the csv file into the hashmap.  By using a buffered reader the method reads line by line and loads it
    into the hashmap and uses the first element (player name) as the key. line.split is used to seperate the csv file by commas

    @return nothing
    @param nothing
     */
    public void loadHashMap() {
        stats = new HashMap<>(); //creating the hashmap


        BufferedReader br = null;
        String line = "";

        try {
            br = new BufferedReader(new FileReader(csvFile));   //buffered reader takes in the csvFile
            while ((line = br.readLine()) != null) {    //as long as line is not null, place each line to the stats hashmap
                stats.put(line.split(",")[0], line.split(","));
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
    This method uses webscraping and the jsoup jar file to get live score updates for whichever game that the user requests

    @return ArrayList of the updated score
    @param the team name that the user requests
     */
    public ArrayList<String> scoreUpdate(String team){
        ArrayList<String> items = new ArrayList<String>();  //creates ArrayList to store the game score

        final String url = "https://www.google.com/search?q=nba+" + team;

        try {
            final Document document = Jsoup.connect(url).get(); //uses Jsoup to scrape the web for the correct information

            for (Element row : document.select("div.imso_mh__wl.imso-ani.imso_mh__tas")) {  //cssQuery for the team name and score
                if (row.select(".ellipsisize.liveresults-sports-immersive__team-name-width.kno-fb-ctx").text()
                        .equals("")) {
                    continue;
                } else {
                    String teamName = row
                            .select(".ellipsisize.liveresults-sports-immersive__team-name-width.kno-fb-ctx").text();
                    String teamScore1 = row.select(".kno-fb-ctx.imso_mh__ma-sc-cont").text();
                    items.add(teamName);
                    items.add(teamScore1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return items;
    }

    /*
    This method is used to get the top 5 players of a specific category that the user requests.  A temporary hashmap is created to use the category
    as the value to make it easier to sort through.  A temporary array is also used to store the 5 player names

    @return String[] of the player names
    @param a String value for the category that the user wants
     */
    public String[] top5Players(String category){
        BufferedReader br = null;
        String line = "";
        HashMap<String, Double> tempStats = new HashMap<>();    //creates temporary hashmap
        String [] tempArray = new String[5];
        int statSection =0;


        if (category.equals("points")){ // looks at user's input and what they are requesting
            statSection = 5;
        }
        else if (category.equals("rebounds")){
            statSection =  9;
        }
        else if (category.equals("assists")){
            statSection = 8;
        }
        else if (category.equals("blocks")){
            statSection = 6;
        }

        try {
            br = new BufferedReader(new FileReader(csvFile));   //creates buffered reader to make temp hashmap
            while ((line = br.readLine()) != null) {
                tempStats.put(line.split(",")[0], Double.valueOf(line.split(",")[statSection]));
            }

            Object[] sortedValue = tempStats.entrySet().toArray();
            Arrays.sort(sortedValue, new Comparator<Object>() {     //used to compare the elements and sort them in ascending order
                public  int compare(Object o1, Object o2){
                    return ((Map.Entry<String, Double>) o2). getValue().compareTo(((Map.Entry<String, Double>) o1).getValue());
                }
            });

            for (int i = 0; i<5; i++){  //gets the top 5 elements in the hashmap and stores them in the temporary array thats created
                tempArray[i] =(((Map.Entry<String, Integer>) sortedValue[i]).getKey() + " : "
                        + ((Map.Entry<String, Integer>) sortedValue[i]).getValue());
            }

            return tempArray;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempArray;
    }

    /*
    This method takes the teamname from the user and gets the stats for the team for the year. a temp array is created to store the stats
     for the team and the method takes all the categories and calculates all the stats for the team

    @return double array of stats
    @param String of the teams name
     */
    public double[] teamStats(String teamName){
        double [] teamStats = new double[3];    //array of the 3 elements (stats) for the team
        BufferedReader br = null;
        String line = "";
        double teamPoints = 0;
        double teamRebounds =0;
        double teamAssists =0;

        int counter=0;

        try {
            br = new BufferedReader(new FileReader(csvFile));
            while ((line = br.readLine()) != null) {
                rawStats = line.split(",");

                if (rawStats[3].equals(teamName)){
                    double points = Double.parseDouble(rawStats[5]);
                    double rebounds = Double.parseDouble(rawStats[9]);
                    double assists = Double.parseDouble(rawStats[8]);
                    teamPoints += points;
                    teamRebounds += rebounds;
                    teamAssists += assists;
                    counter++;
                }
            }

            teamPoints = Math.round(teamPoints*100);
            teamPoints = teamPoints/100;

            teamRebounds = Math.round(teamRebounds*100);
            teamRebounds = teamRebounds/100;

            teamAssists = Math.round(teamAssists*100);
            teamAssists= teamAssists/100;

            teamStats[0] =teamPoints;
            teamStats[1] =teamRebounds;
            teamStats[2] = teamAssists;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return teamStats;
    }

/*
THis method simply searches the hashmap for the player name that the user selects and sends back the stats for that player

@return String[] of the player stats
@param String of the players name
 */
    public String[] playerStat(String player) {
        String[] playerStat = (stats.get(player));
        return playerStat;

    }
}
