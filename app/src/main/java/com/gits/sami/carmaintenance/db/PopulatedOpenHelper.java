package com.gits.sami.carmaintenance.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gits.sami.carmaintenance.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static com.gits.sami.carmaintenance.others.Utility.DbName;

public class PopulatedOpenHelper extends SQLiteOpenHelper {
	private static PopulatedOpenHelper sInstant;
	private static String DbPath;
	private SQLiteDatabase database;
	private Context context;

	private PopulatedOpenHelper(Context context) {
		super(context, DbName, null, 1);
		this.context=context;
		String PackageName=context.getPackageName();
		DbPath= context.getString(R.string._data_data_)+PackageName+"/databases/";

		this.database = OpenDatabase();
		close();
	}

	public static PopulatedOpenHelper getInstance(Context context){
		if(sInstant==null){
			sInstant = new PopulatedOpenHelper(context);
		}
		return sInstant;
	}
	public SQLiteDatabase GetDatabase(){
		return this.database;
	}
	public SQLiteDatabase OpenDatabase(){
		String path = DbPath+DbName;
		if(database==null){
			CreateDatabase();
			SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
		}
		return database;
	}
	private void CreateDatabase() {
		boolean dbExist=CheckDB();
		if(!dbExist){
			this.getReadableDatabase();
			Log.e(getClass().getName(), "DataBase not Exits And Copy From Assets");
			CopyToDatabase();
		}
		else {
			Log.e(getClass().getName(), "Database Already exist");
		}
		
	}

	private void CopyToDatabase() {
		String path = DbPath+DbName;
		try {
			InputStream dbInputStream = context.getAssets().open(DbName);
			OutputStream dbOutputStream = new FileOutputStream(path);
			byte[] buffer = new byte[4096];
			int readCount=0;
			while((readCount= dbInputStream.read(buffer))>0){
				dbOutputStream.write(buffer, 0, readCount);
			}
			dbInputStream.close();
			dbOutputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private boolean CheckDB(){
		String path = DbPath+DbName;
		File file = new File(path);
		try {
			if (file.exists()) {
				return true;
			}
			return false;
		}catch (Exception ex){
			Log.w("File path Error"," Exception in file path checking"+ex.getMessage());
			return false;
		}
	}
	public void Close(){
		if(database!=null){
			database.close();
		}
	}
	@Override
	public void onCreate(SQLiteDatabase arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
