
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
            checkTableExisting(newTableName);
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
                         + "(FACEBOOKID INT ,"
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
    
    public void addItem(int id, String nickname, int wins, int loses, int rankscore, int onlinestatus) { //adds item (new user)
        try {
            statement = conn.createStatement();
            
            String sqlUpdateTable = "insert into " + newTableName + " VALUES ("
                    + id + ", '" 
                    + nickname + "', " 
                    + wins + ", " 
                    + loses + ", " 
                    + rankscore + ", "
                    + onlinestatus + ", "
                    + "NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL)"; //Question Values
            
            statement.executeUpdate(sqlUpdateTable);
            statement.close();
            statement = null;
            
            System.out.println("Item Added");
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateItem(int facebookID, String field, String value){
        try {
            statement = conn.createStatement();
            
            String sqlUpdateTable = "UPDATE " + newTableName 
                                 + " SET " + field + " = '" + value + "'"
                                 + " WHERE FACEBOOKID = " + facebookID + ";";

            statement.executeUpdate(sqlUpdateTable);
            statement.close();
            statement = null;
            
            System.out.println("Item Updated");
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String getQuery(int facebookID, String field) //returns a value 
    {
        try {
            rs = statement.executeQuery("SELECT " + field 
                                     + " FROM " + newTableName 
                                     + " WHERE FACEBOOKID = " + facebookID);
            return rs.getString(field);
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
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
