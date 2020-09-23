package com.olympus.excel;

	import java.io.File;
	import java.io.FileInputStream;
	import java.io.FileNotFoundException;
	import java.io.FileOutputStream;
	import java.io.IOException;
	import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
	import java.util.Date;
	import java.util.HashMap;
	import java.util.List;
	import java.util.Map;

	import javax.servlet.ServletException;
	import javax.servlet.annotation.WebServlet;
	import javax.servlet.http.HttpServlet;
	import javax.servlet.http.HttpServletRequest;
	import javax.servlet.http.HttpServletResponse;
	import javax.servlet.http.HttpSession;

	import org.apache.commons.lang3.tuple.Pair;
	import org.apache.poi.hssf.usermodel.HSSFWorkbook;
	import org.apache.poi.ss.usermodel.BorderStyle;
	import org.apache.poi.ss.usermodel.Cell;
	import org.apache.poi.ss.usermodel.CellStyle;
	import org.apache.poi.ss.usermodel.FillPatternType;
	import org.apache.poi.ss.usermodel.Font;
	import org.apache.poi.ss.usermodel.HorizontalAlignment;
	import org.apache.poi.ss.usermodel.IndexedColors;
	import org.apache.poi.ss.usermodel.Row;
	import org.apache.poi.ss.usermodel.VerticalAlignment;
	import org.apache.poi.ss.usermodel.Workbook;
	import org.apache.poi.ss.util.CellRangeAddress;
	import org.apache.poi.xssf.usermodel.XSSFCell;
	import org.apache.poi.xssf.usermodel.XSSFRow;
	import org.apache.poi.xssf.usermodel.XSSFSheet;
	import org.apache.poi.xssf.usermodel.XSSFWorkbook;
	import org.apache.poi.xssf.usermodel.*;

	import com.olympus.olyutil.Olyutil;

	@WebServlet("/dashbexcel")
	public class DashboardExcel extends HttpServlet {
		
		/***********************************************************************************************************************************/
	
		
		public static String formatDate(String dateVal ) throws IOException {
			
			 
			String dateMyFormat = "";
			
			if (Olyutil.isNullStr(dateVal)) {
				return("");
			}
	 
			//SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS"); 
	        //SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
	        
			SimpleDateFormat fromUser = new SimpleDateFormat("yyyy-MM-dd"); 
	        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
	        

	        try {
	            Date dateFromUser = fromUser.parse(dateVal); // Parse it to the exisitng date pattern and return Date type
	            dateMyFormat = myFormat.format(dateFromUser); // format it to the date pattern you prefer
	            //System.out.println("DF=" + dateMyFormat); // outputs : 2009-05-19

	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			
			
			
			return(dateMyFormat);
	 
		 
		}
		/***********************************************************************************************************************************/
		//   Map<String, CellStyle> styles = createStyles(workbook); // return styles to Hash
		// Ex. --> titleCell.setCellStyle(styles.get("title")); // deref title in hash and set cell
		public static Map<String, CellStyle> createStyles(Workbook wb){
	        Map<String, CellStyle> styles = new HashMap();
	        
	        CellStyle style;
	        Font titleFont = wb.createFont();
	        titleFont.setFontHeightInPoints((short)18);
	        titleFont.setBold(true);
	        style = wb.createCellStyle();
	        style.setAlignment(HorizontalAlignment.CENTER);
	        style.setVerticalAlignment(VerticalAlignment.CENTER);
	        style.setFont(titleFont);
	        style.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());  
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND); 
	        styles.put("title", style); // assign to Map
	        
	        Font monthFont = wb.createFont();
	        monthFont.setFontHeightInPoints((short)11);
	        monthFont.setColor(IndexedColors.WHITE.getIndex());
	        style = wb.createCellStyle();
	        style.setAlignment(HorizontalAlignment.CENTER);
	        style.setVerticalAlignment(VerticalAlignment.CENTER);
	        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        style.setFont(monthFont);
	        style.setWrapText(true);
	        styles.put("header", style); // assign to Map

	        style = wb.createCellStyle();
	        style.setAlignment(HorizontalAlignment.CENTER);
	        style.setWrapText(true);
	        style.setBorderRight(BorderStyle.THIN);
	        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
	        style.setBorderLeft(BorderStyle.THIN);
	        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
	        style.setBorderTop(BorderStyle.THIN);
	        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
	        style.setBorderBottom(BorderStyle.THIN);
	        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
	        styles.put("cell", style); // assign to Map

	        style = wb.createCellStyle();
	        style.setAlignment(HorizontalAlignment.CENTER);
	        style.setVerticalAlignment(VerticalAlignment.CENTER);
	        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
	        styles.put("formula", style); // assign to Map

	        style = wb.createCellStyle();
	        style.setAlignment(HorizontalAlignment.CENTER);
	        style.setVerticalAlignment(VerticalAlignment.CENTER);
	        style.setFillForegroundColor(IndexedColors.GREY_40_PERCENT.getIndex());
	        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
	        style.setDataFormat(wb.createDataFormat().getFormat("0.00"));
	        styles.put("formula_2", style); // assign to Map

	        return styles;
	    }
		
		/***********************************************************************************************************************************/
		public static XSSFSheet newWorkSheet(XSSFWorkbook workbook, String label) {

			XSSFSheet sheet = workbook.createSheet(label);
			return sheet;
		}
		/***********************************************************************************************************************************/
		public static XSSFWorkbook newWorkbook() {

			XSSFWorkbook workbook = new XSSFWorkbook();
			return workbook;
		}
		/****************************************************************************************************************************************************/
		
		/****************************************************************************************************************************************************/
		public static void loadHeader(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> headerArr) {
				
			Row row = sheet.createRow(0);
			int colNum = 0;
			 Font font = workbook.createFont();
	         font.setFontHeightInPoints((short) 12);
	         font.setFontName("Times New Roman");
	         font.setColor(IndexedColors.BLACK.getIndex());
	         font.setBold(true);
	         CellStyle style = workbook.createCellStyle();
	         style.setFont(font);
	         
	         
			style.setBorderRight(BorderStyle.THIN);
			style.setRightBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderBottom(BorderStyle.THIN);
			style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderLeft(BorderStyle.THIN);
			style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
			style.setBorderTop(BorderStyle.THIN);
			style.setTopBorderColor(IndexedColors.BLACK.getIndex());
		 
	         
	         style.setFillForegroundColor(IndexedColors.TURQUOISE.getIndex());  
	         style.setFillPattern(FillPatternType.SOLID_FOREGROUND);  
			
			for (Object field : headerArr) {
				Cell cell = row.createCell(colNum++);
				if (field instanceof String) {
					cell.setCellStyle(style);
					cell.setCellValue((String) field);
				}
			}
		}
		
		/****************************************************************************************************************************************************/
	
		/****************************************************************************************************************************************************/
		public static void loadWorkSheetCell(XSSFWorkbook workbook, XSSFSheet sheet, ArrayList<String> strArr, int rowNum, String sep) throws IOException {
			String[] strSplitArr = null;
			long assetID = 0;
			double equipCost = 0.0;
			double assetRes = 0.0;
			double accountRes = 0.0;
			double cogs = 0.0;
			String sn = "";
			
			//System.out.println("************* strArr=" + strArr.toString());
			int j = 0;
			for (String str : strArr) { // iterating ArrayList
					
				Row row = sheet.createRow(rowNum++);
				strSplitArr = Olyutil.splitStr(str, sep);	
				//System.out.println("****ItemSZ=" + strSplitArr.length);
				int colNum = 0;
				for (String token : strSplitArr) {
					if (token.equals("15342")  ) {
						
						System.out.println("*^* Str=" + strSplitArr[colNum]);
					}
					Cell cell = row.createCell(colNum);
					 if (colNum == 10 || colNum == 11) {
						String nDate = formatDate(token);
						//System.out.println("Col=" + colNum + " -- DF=" + nDate);
						if (token instanceof String) {
							cell.setCellValue((String) nDate);
						}	
											
						
					} else {			
						if (token instanceof String) {
							cell.setCellValue((String) token.replaceAll("null", ""));
						}
					}
					colNum++;
				 token = "";
					
				}
			}
			 
		}
		
		
	/****************************************************************************************************************************************************/

		
	// Service method
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// String excelTemplate =
		// "C:\\Java_Dev\\props\\nbvaupdate\\excelTemplates\\AssetList.xlsx";
		Date date = Olyutil.getCurrentDate();
		String dateStamp = date.toString();
		
		HttpSession session = req.getSession();
		String FILE_NAME = "Rapport_Booking Details_Report_" + dateStamp + ".xlsx";
	
		 
		ArrayList<String> hdrArr = new ArrayList<String>();
		ArrayList<String> strArr = new ArrayList<String>();
		
		strArr = (ArrayList<String>) session.getAttribute("strArr");
		ArrayList<String> strArrMod = new ArrayList<String>();
		strArrMod = (ArrayList<String>) session.getAttribute("strArrMod");
		
		String hdrFile = "C:\\Java_Dev\\props\\headers\\dashboard\\dashb_Hdr_V1.txt";

		XSSFWorkbook workbook = null;
		XSSFSheet sheet = null;
		workbook = new XSSFWorkbook();
		sheet = newWorkSheet(workbook, "Booking Data");
		hdrArr = Olyutil.readInputFile(hdrFile);
		// Olyutil.printStrArray(hdrArr);

		//String tab1 = "Asset List";
		// workbook = new XSSFWorkbook(new FileInputStream(excelTemplate));

		loadHeader(workbook, sheet, hdrArr);
		loadWorkSheetCell(workbook, sheet, strArr, 1, "~");



		try {
			// HttpServletResponse response = getResponse(); // get ServletResponse
			res.setContentType("application/vnd.ms-excel"); // Set up mime type
			res.addHeader("Content-Disposition", "attachment; filename=" + FILE_NAME);
			OutputStream out2 = res.getOutputStream();
			workbook.write(out2);
			out2.flush();

			// ********************************************************************************************************************************

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				workbook.close();
			} catch (Exception ee) {
				ee.printStackTrace();
			}
		}
	}

	} // End Class
