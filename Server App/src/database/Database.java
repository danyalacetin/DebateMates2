
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
    String usernameDerby = "debatemates";
    String passwordDerby = "mates";
    Statement statement;
    ResultSet rs;
    
    
    public Database() 
    {

    }
    
    public void establishConnection()
    {
        try {
            conn = DriverManager.getConnection(url, usernameDerby, passwordDerby);
            createMainTable(newTableName);
            System.out.println(url+"   connected....");
            
        } catch (SQLException ex) {
            System.out.println("could not connect: " + ex.getMessage());
        }
    }
    
    public void closeConnection()
    {
        if(conn != null)
        {
            try
            {
                conn.close();
                conn = null;
            }
            catch (SQLException ex) {
                System.out.println("Could not close connection: " + ex.getMessage());
            }
        }
    }
    
    public void createTable() // creates the table for the data
    {
        String sqlCreate = "CREATE TABLE " + newTableName + " "
                         + "(FACEBOOKID VARCHAR(20),"
                         + "NICKNAME VARCHAR(20), "
                         + "WINS INT, "
                         + "LOSSES INT, "
                         + "RANKSCORE INT, "
                         + "ONLINESTATUS INT, "
                         + "QUESTION1 INT, QUESTION2 INT, QUESTION3 INT, QUESTION4 INT, QUESTION5 INT, QUESTION6 INT, QUESTION7 INT, QUESTION8 INT, "
                         + "QUESTION9 INT, QUESTION10 INT, QUESTION11 INT, QUESTION12 INT, QUESTION13 INT, QUESTION14 INT, QUESTION15 INT)";
        
        try {
            statement = conn.createStatement();
            statement.execute(sqlCreate);
            statement.close();
            statement = null;
            
            System.out.println("tablecreated");
            
        } catch (SQLException ex) {
            System.out.println("Problem creating table: " + ex.getMessage());
        }
    }
    
    public void addItem(String facebookID, String nickname, int wins, int loses, int rankscore, int onlinestatus) { //adds item (new user)
        try {
            statement = conn.createStatement();
            statement.executeUpdate("insert into " + newTableName + " VALUES ('"
                    + facebookID + "', '" 
                    + nickname + "', " 
                    + wins + ", " 
                    + loses + ", " 
                    + rankscore + ", "
                    + onlinestatus + ", "
                    + "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)"); //Question Values
            statement.close();
            statement = null;
            
            System.out.println("New User Added To Database");
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateItem(String facebookID, String field, String value){
        try {
            statement = conn.createStatement();
            statement.executeUpdate("UPDATE " + newTableName 
                                 + " SET " + field + " = " + value
                                 + " WHERE FACEBOOKID = '" + facebookID + "'");
            statement.close();
            statement = null;
            
            System.out.println("User " + facebookID + " Updated " + field + " With " + value);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getQuery(String facebookID, String field){
        String str = null;
        try {
            statement = conn.createStatement();
            rs = statement.executeQuery("SELECT " + field 
                                     + " FROM " + newTableName 
                                     + " WHERE FACEBOOKID = '" + facebookID + "'");
            while (rs.next()) {
            str = rs.getString(field);
            }
            statement.close();
            statement = null;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        return str;
    }
    
    //Testing purpose only!
    public void droptable(){
        try {
            statement = conn.createStatement();
            statement.executeUpdate("DROP TABLE " + newTableName);
            statement.close();
            statement = null;
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void createMainTable(String newTableName) throws SQLException {
        String[] types = {"TABLE"};
        DatabaseMetaData dbmd = conn.getMetaData();
        ResultSet rsDBMeta = dbmd.getTables(null, null, null, null);//types);
        boolean found = false;

        while (rsDBMeta.next()) {
            String tableName = rsDBMeta.getString("TABLE_NAME");
            //finds each table name
            
            if (tableName.compareToIgnoreCase(newTableName) == 0) {
                //finds table matching the newTableName
                //Don't make anotherone
                System.out.println("Table Found");
                found = true;
            }
        }
        if(!found){
            //table wasn't found
            //Make the table
            System.out.println("Table Wasn't Found.\nCreating Table Now");
            createTable();
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
