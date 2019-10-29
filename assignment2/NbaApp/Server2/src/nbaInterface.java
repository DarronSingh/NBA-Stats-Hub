import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;


public interface nbaInterface extends Remote{
    public String[] playerStat(String player) throws RemoteException;
    //public void comparePlayer(String player1, String player2) throws RemoteException;
    public double[] teamStats(String teamName) throws  RemoteException;
    public ArrayList<String> scoreUpdate(String name) throws  RemoteException;
    public String[] top5Players(String category) throws  RemoteException;

}