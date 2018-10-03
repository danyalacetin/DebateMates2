
package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;



public class Database {
    
    
    String newTableName = "Players";
    
    String url="jdbc:derby://localhost:1527//home/pascal/derbyDBs/PlayersDB;create=true";

    String usernameDerby="pdc";
    String passwordDerby="pdc";
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
    
    public void createTable()
    {
        try {
            Statement statement = conn.createStatement();
            
            String sqlCreate = "create table " + newTableName + " (ID int) ";
            statement.executeUpdate(sqlCreate);
            System.out.println("Table created");
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void additem(int id) {
        try {
            Statement statement=conn.createStatement();
            
            String sqlUpdateTable = "insert into " + newTableName + " (ID) values (" + id + ")";
            statement.executeUpdate(sqlUpdateTable);
            
            //statement.close();
            System.out.println("Item Added");
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void getQuery()
    {
        ResultSet rs = null;
        
        try {

            System.out.println(" getting query ");
            Statement statement = conn.createStatement(
            ResultSet.TYPE_SCROLL_INSENSITIVE, 
            ResultSet.CONCUR_READ_ONLY);
            
            String sqlQuery = "select ID from cases";
            
            rs=statement.executeQuery(sqlQuery);
            rs.beforeFirst();
            while(rs.next())
            {
                int caseid = rs.getInt("ID");                
                System.out.println("Cases picked so far: ");              
                System.out.println(caseid + ",");        
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //return(rs);  
        
        //return(rs);  
    }
    
    public void closeConnections()
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
