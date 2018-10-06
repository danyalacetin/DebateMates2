
package database;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Database {
    
    
    String newTableName = "USERS";
    
    Connection conn = null;
    String url = "jdbc:derby:DebatemateDB;create=true";
    //String url="jdbc:derby://localhost:1527/Players;create=true";
    String usernameDerby="debatemates";
    String passwordDerby="mates";
    Statement statement;
    ResultSet rs;
    
    
    public Database() 
    {

    }
    
    public void establishConnection()
    {
        try {
            conn=DriverManager.getConnection(url, usernameDerby, passwordDerby);
            checkTableExisting(newTableName);
            System.out.println(url+"   connected....");
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
    
    public void createTable() // creates the table for the data
    {
        String sqlCreate = "CREATE TABLE " + newTableName + " "
                    + "(facebookID int,"
                    + "nickname varchar(20), "
                    + "wins int, "
                    + "loses int, "
                    + "rankscore int)";
        
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
    
    private void checkTableExisting(String newTableName) {
        try {
            //System.out.println("check existing tables.... ");
            String[] types = {"TABLE"};
            DatabaseMetaData dbmd = conn.getMetaData();
            ResultSet rsDBMeta = dbmd.getTables(null, null, null, null);//types);
            Statement dropStatement = null;

            while (rsDBMeta.next()) {
                String tableName = rsDBMeta.getString("TABLE_NAME");
                //System.out.println("found: " + tableName);
                if (tableName.compareToIgnoreCase(newTableName) == 0) {
                    //System.out.println(tableName + "  needs to be deleted");
                    String sqlDropTable = "DROP TABLE " + newTableName;
                    dropStatement = conn.createStatement();
                    dropStatement.executeUpdate(sqlDropTable);
                    //System.out.println("table deleted");
                }
            }
            if (rsDBMeta != null) {
                rsDBMeta.close();
            }
            if (dropStatement != null) {
                dropStatement.close();
            }

        } catch (SQLException ex) {
        }

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
