/*
This class is for the client side of the java RMI implementation which allows users to choose various services
that can will be obtained using JAVA RMI and the server.  The services that will be provided are involved with the NBA
and more specifically, providing the user with services that would be useful when requiring supplements for a fantasy league.

@date modified October 25, 2019
@author Darron Singh 100584624
 */

import java.io.*;
import java.rmi.*;
import java.util.ArrayList;
import java.util.Scanner;

public class nbaClient {

   static boolean live;

   /*
   Main method will essentially provide users with the options and take in their input to process their requests.

   @param none
   @return nothing
    */
    public static void main(String argv[]) {

        if (argv.length != 1) {     //takes one argument from the terminal which is just the user's machine
            System.out.println("Usage: java nbaClient machineName");
            System.exit(0);
        }

        try {
            String name = "//" + argv[0] + "/nbaServer2";
            nbaInterface nba = (nbaInterface) Naming.lookup(name);
            while (true) {
                Scanner input = new Scanner(System.in);
                Scanner input2 = new Scanner(System.in);
                Scanner input3 = new Scanner(System.in);
                Scanner input4 = new Scanner(System.in);

                System.out.println("************************************************************");
                System.out.println("**              Welcome to NBA Stat HUB                   **");
                System.out.println("**             What do you wish to view?                  **");
                System.out.println("**                  1. Player Stat                        **");
                System.out.println("**         2. Compare two players for match-up odds       **");
                System.out.println("**            3. See season stats of team                 **");
                System.out.println("**       4. Get the top 5 players in a specific category  **");
                System.out.println("**      5. Get a score update of your favorite team!      **");
                System.out.println("**     6. Get a live score update of your favorite team!  **");
                System.out.println("************************************************************");
                String userInput = input.nextLine();

                if (userInput.equals("1")) {
                    System.out.println("Enter player name");
                    String selection = input2.nextLine();
                    String[] stat = nba.playerStat(selection);
                    printStatArray(stat);
                } else if (userInput.equals("2")) {
                    System.out.println("Enter player 1");
                    String player1 = input3.nextLine();
                    System.out.println("Enter player 2");
                    String player2 = input4.nextLine();
                    String[] stat1 = nba.playerStat(player1);
                    String[] stat2 = nba.playerStat(player2);
                    printStatArrays(stat1, stat2);

                } else if (userInput.equals("3")) {
                    System.out.println("Enter team name");
                    String teamName = input2.nextLine();
                    printTeamStats(nba.teamStats(teamName));

                } else if (userInput.equals("4")) {
                    System.out.println("Enter category");
                    String category = input2.nextLine();
                    String[] top5 = nba.top5Players(category);
                    printTop5Array(category, top5);

                }
                else if(userInput.equals("5")){
                    System.out.println("Enter Team Name");
                    String team = input2.nextLine();
                    System.out.println("   Away Team       Home Team    Score");
                    System.out.println(nba.scoreUpdate(team));


                }
                else if (userInput.equals("6")) {
                    System.out.println("Enter team name");
                    String team = input2.nextLine();

                    live = true;
                    Thread thread = new Thread(){   //uses multi threading to provide the user with live score updates and give them the chance to quit and return to menu if they wish
                        public void run(){
                            System.out.println("Press any key to stop live updates");
                            System.out.println("   Away Team       Home Team    Score");
                            input2.nextLine();
                            live = false;
                        }
                    };
                    thread.start(); //thread starts and continuously provides an updated score every second
                    while(live) {
                        System.out.println(nba.scoreUpdate(team));
                        Thread.sleep(1000);
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("nbaServer exception: " + e.getMessage());
            e.printStackTrace();

        }
    }

    /*
    Method prints the array for displaying stats for a player

    @return nothing
    @param takes in a String array
     */
    public static void printStatArray(String arr[]) {
        String[] statColumns = new String[]{"Player Name ---->", "Height (Inches) ----> ", "Weight (lbs) ---->    ", "Team ---->      ", "Age --->           ", "Avg Points ---->", "Avg Blocks ---->", "Avg Steals ---->", "Avg Assists ---->", "Avg Rebounds ---->", "Avg FT% ---->      ", "Avg 3% ---->      ", "FG % ---->      "};

        for (int x = 0; x < arr.length; x++) {
            System.out.println(statColumns[x] + '\t' + arr[x] + " ");
        }
    }
    /*
    This method prints two arrays when comparing players, displays it in a way that makes it easier to compare the player's stats

    @return nothing
    @param: takes two arrays and outputs them in an easy to read format
     */
    public static void printStatArrays(String arr1[], String arr2[]) {
        String[] statColumns = new String[]{"Player Name ---->", "Height (Inches) ----> ", "Weight (lbs) ---->    ", "Team ---->      ", "Age --->           ", "Avg Points ---->", "Avg Blocks ---->", "Avg Steals ---->", "Avg Assists ---->", "Avg Rebounds ---->", "Avg FT% ---->      ", "Avg 3% ---->      ", "FG % ---->      "};

        for (int x = 0; x < arr1.length; x++) {
            System.out.println(statColumns[x] + '\t' + arr1[x]  + '\t' + "             " + arr2[x]);
        }
    }
    /*
    Prints array to display top 5 players for the category that the user specifies.

    @param the string for the category name and the array to be printed out
    @return nothing
     */
    public static void printTop5Array(String category, String arr[]) {
        System.out.println("TOP 5 PLAYERS: " + category);
        for (int x = 0; x < arr.length; x++) {
            System.out.println(arr[x]);
        }
    }
    /*
    Prints the stats for the team that the user selects

    @param takes in an array of the stats
    @returns nothing
     */
    public static void printTeamStats(double arr[]){
        System.out.println("POINTS     REBOUNDS     ASSISTS");
        for (int x = 0; x<arr.length; x++){
            System.out.print(arr[x] + "        ");
        }
        System.out.println(" ");
    }
}
