<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="java.io.OutputStream"%>   
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="javax.servlet.*"%>
<%@ page import="java.time.*"%>
<%@ page import="java.time.temporal.ChronoUnit"%>

<%@ page import="java.text.*"%>
<%@ page import="com.olympus.olyutil.Olyutil"%>
<%   	String title =  "Olympus FIS Booking Details Report"; 
	 
	ArrayList<String> tokens = new ArrayList<String>();
	String formUrl =  (String) session.getAttribute("formUrl");
	
	String filePath = "C:\\Java_Dev\\props\\headers\\dashboard\\dashb_Hdr_V1.txt";
	
	ArrayList<String> headerArr = readHeader(filePath);
 
	HashMap<String, String> accessMap = new HashMap<String, String>();
	 
	ArrayList<String> strArr = new ArrayList<String>();
 	ArrayList<String> strArrMod = new ArrayList<String>();
	strArr = (ArrayList<String>) session.getAttribute("strArr");
	accessMap = (HashMap<String, String>) session.getAttribute("accessMap");
	//System.out.println("*** URL=" + formUrl +"--");
	
	
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title><%=title%></title>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="http://cdnjs.cloudflare.com/ajax/libs/jquery.tablesorter/2.9.1/jquery.tablesorter.min.js"></script>
<script type="text/javascript" src="includes/js/tableFilter.js"></script>
<script type="text/javascript" src="includes/js/multifilter.js"></script>


<script type="text/javascript">
$(document).ready(function() {
$('.filter').multifilter()
})
</script>


<style><%@include file="includes/css/header.css"%></style>
 
    <link rel="stylesheet" href="includes/css/calendar.css" /> 
<style><%@include file="includes/css/table.css"%></style>
<!--  
   <style><%@include file="includes/css/searchstyle2.css"%></style> 

-->

<!-- ******************************************************************************************************************************************************** -->


<style>

</style>
<!-- ******************************************************************************************************************************************************** -->
<script>

$(function() {

  // call the tablesorter plugin
  $("table").tablesorter({
    theme: 'blue',
    // initialize zebra striping of the table
    widgets: ["zebra"],
    // change the default striping class names
    // updated in v2.1 to use widgetOptions.zebra = ["even", "odd"]
    // widgetZebra: { css: [ "normal-row", "alt-row" ] } still works
    widgetOptions : {
      zebra : [ "normal-row", "alt-row" ]
    }
  });

});	
			
    </script>
    
   <script>
  function openWin(myID) {
  
  
   myID2 = document.getElementById(b_app).value;

  alert("ID" + myID2);
  //window.open("http://cvyhj3a27:8181/fisAssetServlet/readxml?appID=" + myID2);
	}
	
	
	var call = function(id){
		var myID = document.getElementById(id).value;
		//alert("****** myID=" + myID + " ID=" + id);
		//window.open("http://cvyhj3a27:8181/fisAssetServlet/readxml?appID=
		window.open("http://localhost:8181/webreport/getquote?appKey=" + myID);
				
				
	}



	var getExcel = function(urlValue){
		var formUrl = document.getElementById(urlValue).value;
		//alert("SD=" + startDate + "****** formUrl=" + formUrl + " \n***** urlValue=" + urlValue);
		//alert("in Quote" + myID + " --- id=" +id);
		window.open( formUrl, 'popUpWindow','height=500,width=800,left=100,top=100,resizable=yes,scrollbars=yes,toolbar=yes,menubar=no,location=no,directories=no, status=yes' );
	
	}
	
	
	
</script> 
</head>
<!-- ********************************************************************************************************************************************************* -->

<body>

 
<%@include  file="includes/header.html" %>

<h3><%=title%></h3>

<%!/*****************************************************************************************************************************************************************/
//String formUrl = null;
/*************************************************************************************************************************************************************/
public ArrayList<String> readHeader(String filePath) throws IOException {
	
	ArrayList<String> strArr = new ArrayList<String>();
	String header = null;
	BufferedReader reader = null;
	StringBuilder sb = null;
	String line = null;
	try {
	 	reader = new BufferedReader(new FileReader(filePath));
    	 sb = new StringBuilder();
    
	} catch (FileNotFoundException fex) {
		fex.printStackTrace();	
	}
	try { 
	    while((line = reader.readLine())!= null){
	    	strArr.add(line);
	    }	   
		reader.close();
	
	} catch (IOException ioe) {
		ioe.printStackTrace();
	}
	
	return strArr;	
}
/*************************************************************************************************************************************************************/
public long  daysPending( String currDate, String releaseDate   ) throws IOException {
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

public String  buildHeader( JspWriter out2, ArrayList<String> dataArr   ) throws IOException {
	
	String header = "";
	String style = "b3";
	//System.out.println("** hdrArrSZ=" + dataArr.size());
	if (dataArr.size() > 0) {
		for (int k = 0; k < dataArr.size(); k++) {
			
			if (k == 1) {
				style = "b3a";
			} else {
				style = "b3";
			}
			 header += "<th class=\"b3\" >" + dataArr.get(k) + " </th>";
			 
			
		}
	}
	return header;
	
}
/*************************************************************************************************************************************************************/

/*************************************************************************************************************************************************************/
public String  buildCells(JspWriter out, ArrayList<String> dataArr, HashMap<String, String> hMap  ) throws IOException {
	String cells = "";
	String xDataItem = null;
	String color1 = "plum";
	String style1 = "font-family: sans-serif; color: white;";
	String rowEven = "#D7DBDD";
	String rowOdd = "#AEB6BF";
	String excel = null;
	String rowColor = null;
	String sep = "~";
	String relDate = "";
	String data = "";
	String remain = "";
	String pShip = "";
	String modStr = "";
	ArrayList<String> modArr = new ArrayList<String>();
	 
	long  daysPend = 0;
	
		if (dataArr.size() > 0) {
			for (int k = 0; k < dataArr.size(); k++) {

				rowColor = (k % 2 == 0) ? rowEven : rowOdd;
				//cells += "<tr class=\"alt-row\" bgcolor=" + rowColor + ">";
				cells = "<tr  bgcolor=" + rowColor + " >";
				out.println(cells);
				cells = "";
				xDataItem = dataArr.get(k);
				modStr = xDataItem;
				String token_list[] = xDataItem.split(sep);
				for (int x = 0; x < token_list.length; x++) {
					
					 if (x == 5) {
						//System.out.println("*** EC=" +  token_list[x] + "--");
						if (! Olyutil.isNullStr(token_list[x])) {
							double eCost = Olyutil.strToDouble(token_list[x]);
							cells += "<td class=\"b3\" >" + eCost + "</td>";
						} else {
							cells += "<td class=\"b3\" >" + token_list[x].replaceAll("null", "") + "</td>";
						}
						
					} else {

					//if (x == 3 || x == 53) {
						//System.out.println("*** Date:" + token_list[x] + "--" + "SZ=" + token_list.length );
					//}
					//cells += "<td class=\"b3\">" + token_list[x] + "</td>";
					cells += "<td class=\"b3\" >" + token_list[x].replaceAll("null", "") + "</td>";
					
					}
					if (! Olyutil.isNullStr(token_list[10])) {
						 relDate = Olyutil.formatDate(token_list[10], "yyyy-MM-dd hh:mm:ss.SSS", "yyyy-MM-dd");
						 daysPend = daysPending(token_list[11], relDate);
						 //System.out.println("*** DP=" + daysPend + "-- currDate=" + token_list[11]   + "-- RelDate=" +  relDate);
						 
						 
					}
					
					if (x == 0) {
						data = hMap.get(token_list[x]);
						if (! Olyutil.isNullStr(data)) {
						//System.out.println("***^^*** appID=" + token_list[x] + "-- data=" + data + "--");
							String[] items = data.split(";");
							DecimalFormat df = new DecimalFormat("#.##");
							remain = df.format(Olyutil.strToDouble(items[0]) );
							pShip = items[1];
						}
						
						
					} 
					
				 
					 
					
				}
				
				cells += "<td class=\"b3\" >" + daysPend + "</td>";
				cells += "<td class=\"b3\" >" + remain + "</td>";
				cells += "<td class=\"b3\" >" + pShip + "</td>";
				cells += "</tr >";
				out.println(cells);
				modStr += daysPend + "~" + remain  + "~" +  pShip;
				if (! Olyutil.isNullStr(pShip)) {
					System.out.println("** MS " + modStr);
				}
				cells = "";
				remain = "";
				pShip = "";
				
				modArr.add(modStr);
				modStr = "";
			}
		}


		return cells;
	}%>




 
<%  
/*****************************************************************************************************************************************************************/



 
	
	//out.println("strArrMod Size=" + strArrMod.size());
	if (strArr.size() > 0) {
		
		%>
	
   
   	<!-- 
  
			<div style="height: 450px; overflow: auto;">
				
 -->

 
   
<!--  
<input id="search" type="text" placeholder="Enter Text to Filter...">
	 -->			
		
		
		
<!-- ***************************************************************************************************************************************************************************-->
<!-- ***************************************************************************************************************************************************************************-->

	  </CENTER>
	  <!-- ***************************************************************************************************************************************************************************-->
		
		
	  <!-- ***************************************************************************************************************************************************************************-->
		
		
		

		<%  
		/**********************************************************************************************************************************************************/

		
	}
%>	
</div>

<!-- ***************************************************************************************************************************************************************************-->
			<table class="nb" border="0" cellpadding="1" cellspacing="1">
  <tr class="nb">
    <td  class="nb" >   
    <form name="excelForm" enctype="multipart/form-data" method="get" action="<%=formUrl%> " \>
    <input type="hidden" value="excel"  name="excel" />
    <input type="submit" value="Save Excel File" class="btn" /> 
    </form>
	</td>   
   </tr></table><BR>
   
<div class='container'>
<div class='filters'>



<div class='clearfix'></div>
<div class='container'>

<CENTER>

<table> 
	  <tr bgcolor="#5DADE2"  style="font-family: sans-serif; color: white;" >
	<th class="a" >Filter Data</th>  
	
	
</tr></table>
<h6></h6>
<div class='filter-container'>
<input autocomplete='off' class='filter' name='ProgramType' placeholder='ProgramType' data-col='ProgramType'/>
 
<input autocomplete='off' class='filter' name='Branch' placeholder='Branch' data-col='Branch'/>

<input autocomplete='off' class='filter' name='App_Status' placeholder='App_Status' data-col='App_Status'/>

<input autocomplete='off' class='filter' name='Rep2' placeholder='Rep2' data-col='Rep2'/>

  <input autocomplete='off' class='filter' name='App_Owner' placeholder='App_Owner' data-col='App_Owner'/>
  
  <input autocomplete='off' class='filter' name='Customer_Name' placeholder='Customer_Name' data-col='Customer_Name'/>
  
  
</div>
<h6></h6>
</CENTER>
<!-- ***************************************************************************************************************************************************************************-->
<%  
out.println("<table  border=\"1\"> <thead> <tr>");
String header = buildHeader(out, headerArr); // build header from file
out.println(header);
out.println("</tr></thead>");
out.println("<tbody >");
String cells =  buildCells(out, strArr, accessMap); // build data cells from file
out.println(cells);
out.println("</tbody></table>"); // Close Table

%>

</table>
<!-- ***************************************************************************************************************************************************************************-->




 



</body>
</html>