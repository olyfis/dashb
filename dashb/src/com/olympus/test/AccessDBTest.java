package com.olympus.test;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.olympus.olyutil.Olyutil;


public class AccessDBTest {
	
	//String databaseURL = "jdbc:ucanaccess://e://Java//JavaSE//MsAccess//Contacts.accdb";
	private static String dbName = "Commencement.accdb";
	private static String dbPath = "//D://Kettle//accessDB//";
	private static String dbPathName = dbPath + dbName;
	 
	/**************************************************************************************************************************************************************/

	public static void writeToFile(ArrayList<String> strArr, String fileName, String sep) throws IOException {
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
	    String[] strSplitArr = null;
	   
	    for (String str : strArr) { // iterating ArrayList
	    	strSplitArr = Olyutil.splitStr(str, sep);
	    	int i = 0;
	    	int sz = strSplitArr.length;
	    	for (String token : strSplitArr) {
	    		//writer.write(token.replaceAll("null", ""));
	    		if (i < sz -1) {
	    			writer.write(token + sep);
	    		} else {
	    			writer.write(token);
	    		}
	    		
	    		i++;
	    	}
	    	writer.newLine();
	    }
	    writer.close(); 
	   
	}
	
	/**************************************************************************************************************************************************************/

	
	
	/**************************************************************************************************************************************************************/
	/**************************************************************************************************************************************************************/
	public static ArrayList<String> getAccessDbData(String sqlQuery) throws IOException {
		
		String sep = ";";
	
		ArrayList<String> strArr = new ArrayList<String>();
		
		
    	
    	String databaseURL = "jdbc:ucanaccess://" + dbPathName;
    	
    	 
    	System.out.println("** URL=" + databaseURL);
        try (Connection connection = DriverManager.getConnection(databaseURL)) {
         
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(sqlQuery);
          
            while (result.next()) {
                int id = result.getInt("Credit App");
                String remainShipped = result.getString("Remaining To Be Shipped");
                
                String percentShipped = result.getString("Percent Shipped");
                //String comment = result.getString("Comment");
                //System.out.println(id + "," + percentShipped + "," + comment );
                //System.out.println(id + "," + percentShipped + "," + remainShipped );
                String rtnStr = id + sep  + remainShipped + sep + percentShipped;
                strArr.add(rtnStr);
            }
             
        } catch (SQLException ex) {
            ex.printStackTrace();
        }  
		
		
		return(strArr);
	}
	/**************************************************************************************************************************************************************/

	
	/**
	 * This program demonstrates how to use UCanAccess JDBC driver to read/write
	 * a Microsoft Access database.
	 * @author www.codejava.net
	 * @throws IOException 
	 *
	 */
	 
	    public static void main(String[] args) throws IOException {
	    /*	ArrayList<String> strArr = new ArrayList<String>();
	    	String sqlQuery = "SELECT * FROM Pentaho";
	    	 String fileName = "C:\\Java_Dev\\props\\accessDB\\adb.txt";
	       //String databaseURL = "jdbc:ucanaccess://e://Java//JavaSE//MsAccess//Contacts.accdb";
	    	
	    	String databaseURL = "jdbc:ucanaccess:" + dbPathName;
	    	System.out.println("** URL=" + databaseURL);
	    	strArr = getAccessDbData(sqlQuery);
	    	//Olyutil.printStrArray(strArr);
	    	writeToFile(strArr,fileName, ";");
	    	*/  
	    }
	}