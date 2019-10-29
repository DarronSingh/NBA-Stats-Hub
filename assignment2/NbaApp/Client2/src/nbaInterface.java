/*
This interface contains all the methods that the user will be calling using java rmi

@author Darron Singh 100583624
@date modified October 24, 2019
 */
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface nbaInterface extends Remote{
    public String replyHello(String name) throws RemoteException;
    public String[] playerStat(String player) throws RemoteException;
    public void comparePlayer(String player1, String player2) throws RemoteException;
    public double[] teamStats(String teamName) throws  RemoteException;
    public ArrayList<String> scoreUpdate(String name) throws  RemoteException;
    public String[] top5Players(String category) throws  RemoteException;
}
