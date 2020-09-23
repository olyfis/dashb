package com.olympus.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.w3c.dom.NodeList;

 
import com.olympus.olyutil.Olyutil;

	@WebServlet("/testsql")
	public class TestSql extends HttpServlet {
		

		static Statement stmt = null;
		static Connection con = null;
		static ResultSet res  = null;
		static NodeList  node  = null;
		static String s = null;
		static private PreparedStatement statement;
		/*
		static String propFile = "C:\\Java_Dev\\props\\unidata.prop";
		static String sqlFile = "C:\\Java_Dev\\props\\sql\\NBVAbuy\\NBVA_assetBuy_V4.sql";
		 static String hdrFile = "C:\\Java_Dev\\props\\headers\\NBVA\\NBVA_assetBuy_fullQueryHdr_v2.txt";
		*/
		static String propFile = "C:\\Java_Dev\\props\\Rapport.prop";
		static String sqlFile = "C:\\Java_Dev\\props\\sql\\dashboard\\dashb_V1.sql";
		static String hdrFile = "C:\\Java_Dev\\props\\headers\\dashboard\\dashb_Hrd_V1.txt";
		 
		
 
		
		/*****************************************************************************************************************************************************/
 
		public static ArrayList<String> getDbData(String id, String sqlQueryFile, String booked, String qType) throws IOException {
			FileInputStream fis = null;
			FileReader fr = null;
			String s = new String();
			String sep = "";
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

System.out.println( query);	
			//System.out.println("*** idVal=" + id + "--");
			try {
				con = Olyutil.getConnection(connectionProps);
				if (con != null) {
					//System.out.println("Connected to the database");
					statement = con.prepareStatement(query);
					//System.out.println("***^^^*** contractID=" + contractID);
					//statement.setString(1, id);
					sep = ";";	 
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
			return strArr;
		}
		/*****************************************************************************************************************************************************/
		public static void displayData(ArrayList<String> strArr) {
			ArrayList<String> hdrArr = new ArrayList<String>();
			System.out.println("** HDR=" + hdrFile);
			hdrArr = Olyutil.readInputFile(hdrFile);
			int k = 0;
			for (String str : strArr) { // iterating ArrayList
 	 			
 				//System.out.println("**** Str=" + str);
 				String[] items = str.split(";");
 				int sz = items.length;
 				
 				for (int i = 0; i < 22; i++) {
					
					//System.out.println("****Row="  + k + "--  i=" + i + "-- " + hdrArr.get(i)  + "=" + items[i]);
 					System.out.println(k + ";" + i + ";" + hdrArr.get(i)  + ";" + items[i]);
				}
				k++;
			}
			
		}
		
		/*****************************************************************************************************************************************************/

		
		
		@Override
		protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			String idVal = "001-0000090-001";
			ArrayList<String>strArr = new ArrayList<String>();
			strArr = getDbData(idVal, sqlFile, "", "Asset");
			int arrSZ = strArr.size();
			if (arrSZ > 0) {
				//Olyutil.printStrArray(strArr);			
				displayData(strArr);
				
				
			} else {
				System.out.println("*** RTN Arr SZ=" + arrSZ + "--");
			}
			 
			
		}
		
}
