package com.blogspot.ozgwei.jaxb;


import java.util.*;
import javax.xml.bind.DatatypeConverter;

/**
 More information about this class in
 http://ozgwei.blogspot.com/2007/08/generating-developer-friendly-codes.html
*/
public class DateConverter {


   public static Date parseDate(String s) {
       return DatatypeConverter.parseDate(s).getTime();
   }


   public static Date parseTime(String s) {
       return DatatypeConverter.parseTime(s).getTime();
   }

   public static Date parseDateTime(String s) {
       return DatatypeConverter.parseDateTime(s).getTime();
   }

   public static String printDate(Date dt) {
       Calendar cal = new GregorianCalendar();
       cal.setTime(dt);
       return DatatypeConverter.printDate(cal);
   }

   public static String printTime(Date dt) {
       Calendar cal = new GregorianCalendar();
       cal.setTime(dt);
       return DatatypeConverter.printTime(cal);
   }


   public static String printDateTime(Date dt) {
       Calendar cal = new GregorianCalendar();
       cal.setTime(dt);
       return DatatypeConverter.printDateTime(cal);
   }

}
