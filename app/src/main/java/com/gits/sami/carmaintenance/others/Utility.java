package com.gits.sami.carmaintenance.others;

import android.widget.EditText;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Arafat on 18/01/2017.
 */

public class Utility {
    public static Date ErrorDate = new Date();
    static {
        try {
            ErrorDate = (new SimpleDateFormat(myDateFormat.yyyy_MM_dd.toString())).parse("2000-01-01");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public static  final String DbName = "carMaintenance.sqlite";
    public static  final int DbVersion = 1;
    public static  final String BillTableName = "bill";
    public enum myDateFormat {
        dd_MMM_yyyy("dd-MMM-yyyy"),
        yyyy_MM_dd("yyyy-MM-dd");

        private final String text;

        private myDateFormat(final String text) {
            this.text = text;
        }
        @Override
        public String toString() {
            return text;
        }
    }
    public static Date getDate(String date, myDateFormat dateDFormat){
        DateFormat format = new SimpleDateFormat(dateDFormat.toString());
        try {
            return format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return ErrorDate;
    }
    public enum dateEnum{
        EntryDate,
        ReportFromDate,
        ReportToDate
    }
    public static String getDateAsString(Date date,Utility.myDateFormat dateFormat){
        return new SimpleDateFormat(dateFormat.toString()).format(date);
    }
    public static boolean isEmpty(EditText editText){
        return editText.getText().toString().trim().equals("");
    }
    public static String getMyText(EditText editText){
        return editText.getText().toString().trim();
    }
    public static String getErrorMsg(){
        return "This field is required";
    }
}
