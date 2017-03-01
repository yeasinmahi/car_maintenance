package com.gits.sami.carmaintenance.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gits.sami.carmaintenance.model.Bill;
import com.gits.sami.carmaintenance.others.Utility;

import java.util.ArrayList;
import java.util.Date;

import static com.gits.sami.carmaintenance.others.Utility.BillTableName;
import static com.gits.sami.carmaintenance.others.Utility.DbName;
import static com.gits.sami.carmaintenance.others.Utility.DbVersion;
public class DbHelper extends SQLiteOpenHelper {
	public DbHelper(Context context) {
		super(context, DbName, null, DbVersion);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public ArrayList<Bill> getBill(Date fromDate, Date toDate) {
		ArrayList<Bill> bills = new ArrayList<Bill>();
		// Rest Index Of Spinner from database
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from "+BillTableName+" where date between '"+Utility.getDateAsString(fromDate, Utility.myDateFormat.yyyy_MM_dd)+"' and '"+Utility.getDateAsString(toDate, Utility.myDateFormat.yyyy_MM_dd)+"'",null);

		if (cursor != null && cursor.getCount() > 0) {
			while (cursor.moveToNext()) {
				Bill bill = new Bill();
				bill.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex("id")));
				bill.jobName = cursor.getString(cursor.getColumnIndex("jobName"));
				bill.mileage = cursor.getString(cursor.getColumnIndex("mileage"));
				bill.description = cursor.getString(cursor.getColumnIndex("description"));
				bill.cost = Double.parseDouble(cursor.getString(cursor.getColumnIndex("cost")));
				bill.date = Utility.getDate(cursor.getString(cursor.getColumnIndex("date")),Utility.myDateFormat.yyyy_MM_dd);
				bill.garageName = cursor.getString(cursor.getColumnIndex("garageName"));
				bill.garageAddress = cursor.getString(cursor.getColumnIndex("garageAddress"));
				bills.add(bill);
			}
		}
		if (cursor != null) {
			cursor.close();
		}
		db.close();
		return bills;
	}
	public boolean insertBill(Bill bill) {
		ContentValues contentValues = new ContentValues();
		contentValues.put("jobName", bill.jobName);
		contentValues.put("mileage", bill.mileage);
		contentValues.put("description", bill.description);
		contentValues.put("cost", (Double) bill.cost);
		contentValues.put("date", Utility.getDateAsString(bill.date, Utility.myDateFormat.yyyy_MM_dd));
		contentValues.put("garageName", bill.garageName);
		contentValues.put("garageAddress", bill.garageAddress);
		SQLiteDatabase db = getReadableDatabase();
		long row = db.insert(BillTableName,null,contentValues);
		db.close();
		return row>0;
	}

}
