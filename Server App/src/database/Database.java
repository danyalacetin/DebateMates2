
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Database {
    
    
    String newTableName = "users";
    
    String url="jdbc:derby://localhost:1527/Players;"; // create=true

    String usernameDerby="debatemates";
    String passwordDerby="mates";
    Connection conn;
    
    public Database() 
    {

    }
    
    public void establishConnection()
    {
        try {
            conn=DriverManager.getConnection(url, usernameDerby, passwordDerby);
            System.out.println(url+"   connected....");
        
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void createTable() // creates the table for the data
    {
        String sqlCreate = "CREATE TABLE " + newTableName + " (\n"
                    + " facebookID int,\n"
                    + " nickname varchar(20),\n"
                    + " wins int,\n"
                    + " loses int,\n"
                    + " rankscore int\n"
                    + ")";
        
        try (Connection conn = DriverManager.getConnection(url);
            
            Statement stmt = conn.createStatement()) {
            stmt.execute(sqlCreate);
            
            System.out.println("tablecreated");
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void additem(int id, String nickname, int wins, int loses, int rankscore) { // adds item (new player)
        try {
            Statement statement = conn.createStatement();
            
            String sqlUpdateTable = "insert into " + newTableName + "(facebookID, nickname, wins, loses, rankscore) VALUES (" + id + "," + nickname + "," + wins + "," + loses + "," + rankscore + ")";
            
            statement.executeUpdate(sqlUpdateTable);
            
            //statement.close();
            System.out.println("Item Added");
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getQuery() //returns a value 
    {
        
    }
    
    public void closeConnections() //ends the connection
    {
        if(conn!=null)
        {
            try {
                conn.close();
            } catch (SQLException ex) {
                Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
