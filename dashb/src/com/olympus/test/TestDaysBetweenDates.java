package com.olympus.test;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


public class TestDaysBetweenDates {


	
	   public static void main(String[] args) {
		String dateBeforeString = "2020-03-13";
		String dateAfterString = "2020-09-18";
			
		//Parsing the date
		LocalDate dateBefore = LocalDate.parse(dateBeforeString);
		LocalDate dateAfter = LocalDate.parse(dateAfterString);
			
		//calculating number of days in between
		long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
			
		//displaying the number of days
		System.out.println(noOfDaysBetween);
	   }
}
