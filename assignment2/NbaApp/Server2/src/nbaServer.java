import java.io.*;
import java.rmi.*;

public class nbaServer {
    public static void main(String argv[]) {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManager());
        }
        try{
            nbaInterface nba = new nbaImpl("nbaServer2");
            Naming.rebind("//127.0.0.1/nbaServer2", nba);
        }
        catch(Exception e){
            System.out.println("nbaServer: "+e.getMessage());
        }
    }
}
