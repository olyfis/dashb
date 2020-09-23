package com.olympus.dashboard;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Handler;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.olympus.olyutil.Olyutil;
import com.olympus.olyutil.log.OlyLog;
/**************************************************************************************************************************************************************/
@WebServlet("/dashboard")
public class Dashboard extends HttpServlet {
 
	private final Logger logger = Logger.getLogger(Dashboard.class.getName()); // define logger
	static Statement stmt = null;
	static Connection con = null;
	static ResultSet res  = null;
	static private PreparedStatement statement;
	static String propFile = "C:\\Java_Dev\\props\\Rapport.prop";
	static String sqlFile = "C:\\Java_Dev\\props\\sql\\dashboard\\dashb_V1.sql";
	static String hdrFile = "C:\\Java_Dev\\props\\headers\\dashboard\\dashb_Hdr_V1.txt";
	static String sep = "~";	

	/**************************************************************************************************************************************************************/
	public static ArrayList<String> getAccessDbData(ServletContext context, String sqlQuery, String dbPathName) throws IOException {
		
		//System.out.println("*** Path=" + dbPathName);
		//System.out.println("*** File=" + sqlFile);
	
		ArrayList<String> strArr = new ArrayList<String>();
		
		//String databaseURL = "jdbc:ucanaccess://e://Java//JavaSE//MsAccess//Contacts.accdb";
    	
    	String databaseURL = "jdbc:ucanaccess://" + dbPathName;
    	
    	//String databaseURL = "jdbc:ucanaccess://" + context.getRealPath("DB/" + dbName);
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
                System.out.println(id + "," + percentShipped + "," + remainShipped );
            }
             
        } catch (SQLException ex) {
            ex.printStackTrace();
        }  
		
		
		return(strArr);
	}
	
	
	/****************************************************************************************************************************************************/

	public static ArrayList<String> getDbData( String sqlQueryFile) throws IOException {
		FileInputStream fis = null;
		FileReader fr = null;
		String s = new String();
		 
        StringBuffer sb = new StringBuffer();
        ArrayList<String> strArr = new ArrayList<String>();
		try {
			fis = new FileInputStream(propFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Properties connectionProps = new Properties();
		connectionProps.load(fis);
		fr = new FileReader(new File(sqlQueryFile));	
		// be sure to not have line starting with "--" or "/*" or any other non alphabetical character
		BufferedReader br = new BufferedReader(fr);
		while((s = br.readLine()) != null){
		      sb.append(s);       
		}
		br.close();
		//displayProps(connectionProps);
		String query = new String();
		query = sb.toString();	
		//System.out.println( query);	 
		try {
			con = Olyutil.getConnection(connectionProps);
			if (con != null) {
				//System.out.println("Connected to the database");
				statement = con.prepareStatement(query);
				//System.out.println("***^^^*** contractID=" + contractID);
				//statement.setString(1, id);
			
				res = Olyutil.getResultSetPS(statement);		 	 
				strArr = Olyutil.resultSetArray(res, sep);			
			}		
		} catch (SQLException se) {
			se.printStackTrace();
		} finally {
			try {
				if (stmt != null) {
					stmt.close();
				}
				if (con != null) {
					con.close();
				}
			} catch (SQLException se) {
				se.printStackTrace();
			}
		}
		return(strArr);
	}
	 
	/**************************************************************************************************************************************************************/
	public static void displayData(ArrayList<String> strArr, ArrayList<String> hdrArr) {
		 
		
		int k = 0;
		for (String str : strArr) { // iterating ArrayList
	 			
				//System.out.println("**** Str=" + str);
				String[] items = str.split("~");
				int sz = items.length;
			
				for (int i = 0; i < sz; i++) {
					
					System.out.println("**** SZ=" +  items.length + "-- Row="  + k + "--  i=" + i + "-- " + hdrArr.get(i)  + "=" + items[i] );
			
					//System.out.println(k + ";" + i + ";" + hdrArr.get(i)  + ";" + items[i]);
				}
				k++;
		}
		
	}
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
	public static HashMap<String, String> getAccessData( String fileName, String sep) {
		ArrayList<String> adbArr = new ArrayList<String>();
		HashMap<String, String> hMap = new HashMap<String, String>();
		String key = "";
		String data = "";
		
		
		//System.out.println("*** Path=" + fileName);
		adbArr = Olyutil.readInputFile(fileName);
		//Olyutil.printStrArray(adbArr);
		int k = 0;
		for (String str : adbArr) {
			String[] items = str.split(";");
			
			key = items[0];
			data = items[1] + ";" + items[2];
			
			/*
			int sz = items.length;
			for (int i = 0; i < sz; i++) {
				System.out.println("****Row=" + k + "--  i=" + i + "-- item"  + "=" + items[i]);
				// System.out.println(k + ";" + i + ";" + hdrArr.get(i) + ";" + items[i]);
			}
			*/
			
			hMap.put(key, data);
			k++;
		}
		return(hMap);
	}
	/*************************************************************************************************************************************************************/
	public static long  daysPending( String currDate, String releaseDate   ) throws IOException {
		long  pDays = 0;
		
		if(! Olyutil.isNullStr(releaseDate) ) {
			LocalDate dateBefore = LocalDate.parse(releaseDate);
			LocalDate dateAfter = LocalDate.parse(currDate);
				
			//calculating number of days in between
		 pDays = ChronoUnit.DAYS.between(dateBefore, dateAfter);
				
			//displaying the number of days
			//System.out.println("*** daysPending() -- DAYSP=" + noOfDaysBetween);
			
		} else {
			pDays = 0;
		}

		return(pDays);
	}

	/*************************************************************************************************************************************************************/

	/**************************************************************************************************************************************************************/
	public static ArrayList<String> parseData(ArrayList<String> dataArr, HashMap<String, String> hMap, ArrayList<String> hdrArr) throws IOException {
		String[] items = null;
		ArrayList<String> modArr = new ArrayList<String>();
		String sep = "~";
		String relDate = "";
		String data = "";
		String remain = "";
		String pShip = "";
		long  daysPend = 0;
		String xDataItem = null;
		String modStr = "";
		String tmpStr = "";
		 int k = 0;
		 int j = 0;
		for (String str : dataArr) { // iterating ArrayList
	    	items = Olyutil.splitStr(str, sep); 	
	    	int sz = items.length;
	    	modStr = str;
	    	for (int i = 0; i < sz; i++) {		
				//System.out.println("****Row="  + k + "--  i=" + i + "-- Item"  + "=" + items[i]);
		
	    		 
	    		if (! Olyutil.isNullStr(items[10])) {
					 relDate = Olyutil.formatDate(items[10], "yyyy-MM-dd hh:mm:ss.SSS", "yyyy-MM-dd");
					 daysPend = daysPending(items[11], relDate);
					 //System.out.println("*** DP=" + daysPend + "-- currDate=" + items[11]   + "-- RelDate=" +  relDate); 
				}
			 
				if (i == 0) {
					data = hMap.get(items[i]);
					if (! Olyutil.isNullStr(data)) {
					//System.out.println("***^^*** appID=" + token_list[x] + "-- data=" + data + "--");
						if (! Olyutil.isNullStr(items[10])) {
							 relDate = Olyutil.formatDate(items[10], "yyyy-MM-dd hh:mm:ss.SSS", "yyyy-MM-dd");
							 daysPend = daysPending(items[11], relDate);
							// System.out.println("*** DP=" + daysPend + "-- currDate=" + items[11]   + "-- RelDate=" +  relDate); 
						}
						
						
						
						String[] tok = data.split(";");
						DecimalFormat df = new DecimalFormat("#.##");
						remain = df.format(Olyutil.strToDouble(tok[0]) );
						pShip = tok[1];
						tmpStr += "~" + daysPend + "~" + remain  + "~" +  pShip;
					} else {
						tmpStr +=  "~" +  "~ " +  "~ ";
					}
				
	 
				}
			
				remain = "";
				pShip = "";
				
				
	    	k++;
	    }
	    	
	    	String nStr = modStr.concat(tmpStr);
	    	//if ( j++ < 40) {
	    		//System.out.println("** nSTR=" + nStr);
	    		
	    	//}
	    	modArr.add(nStr);
			modStr = "";
			tmpStr ="";
		}

		//displayData( modArr, hdrArr);
		return(modArr);
	}

	/**************************************************************************************************************************************************************/

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HashMap<String, String> accessMap = new HashMap<String, String>();
		String formUrl = "formUrl";
		String formUrlValue = "/dashb/dashbexcel";
		request.getSession().setAttribute(formUrl, formUrlValue);
		ArrayList<String> strArr = new ArrayList<String>();
		ArrayList<String> dataArr = new ArrayList<String>();
		ArrayList<String> hdrArr = new ArrayList<String>();
		String dbFileName = "accessdb.txt";

		String dbPath = "C:\\Java_Dev\\props\\accessDB\\";
	 
		String dbPathName = dbPath + dbFileName;
		
		ServletContext context = request.getServletContext();
		String path = context.getRealPath("DB");
		String dispatchJSP = "/dashdetail.jsp";
		String logFileName = "dashb.log";
		String directoryName = "D:/Kettle/logfiles/dashbApp";
		Handler fileHandler =  OlyLog.setAppendLog(directoryName, logFileName, logger );
		String dateFmt = "";
		String fileName = "c:/temp/dash.txt";
		//System.out.println("*** Path=" + path);
		
		
		String sqlQuery = "SELECT * FROM Pentaho";
		hdrArr = Olyutil.readInputFile(hdrFile);
		
		
		dataArr = getDbData(sqlFile);
		int arrSZ = strArr.size();
		//System.out.println("*** arrSz:" + arrSZ + "--");
		//
		//
		
		accessMap = getAccessData(dbPathName, ";");
		//Olyutil.printHashMap(accessMap);
		strArr = parseData(dataArr, accessMap, hdrArr); 
		
		
		
		
		//Olyutil.printStrArray(strArr);
		//displayData( sArr, hdrArr);
		 //writeToFile(strArr, fileName, "~");
		logger.info(dateFmt + ": " + "------------------Begin forward to: " + dispatchJSP);
		fileHandler.flush();
		fileHandler.close();
		//request.getSession().setAttribute("contractMap", contractMap);
		//Olyutil.printHashMap(contractMap);
		request.getSession().setAttribute("strArr", strArr);
		request.getSession().setAttribute("accessMap", accessMap);
		
		 request.getRequestDispatcher(dispatchJSP).forward(request, response);
		
	} // End doGet
	/**************************************************************************************************************************************************************/

} // End Class
