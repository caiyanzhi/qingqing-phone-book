package com.example.qingqing701;



import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

//HistoryLabel作为一个访问SQLite的助手类，提供两个方面的功能，
//第一，getReadableDatabase(),getWritableDatabase()可以获得SQLiteDatabse对象，通过该对象可以对数据库进行操作
//第二，提供了onCreate()和onUpgrade()两个回调函数，允许我们在创建和升级数据库时，进行自己的操作

public class HistoryLabel extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	     
	//在SQLiteOepnHelper的子类当中，必须有该构造函数
	public HistoryLabel(Context context, String name, CursorFactory factory,int version) {
		//必须通过super调用父类当中的构造函数
		super(context, name, factory, version);
	}
	
	public HistoryLabel(Context context,String name){
		this(context,name,VERSION);
	}
	
	public HistoryLabel(Context context,String name,int version){
		this(context, name,null,version);
	}

	//该函数是在第一次创建数据库的时候执行,实际上是在第一次得到SQLiteDatabse对象的时候，才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a Database");
		
		//execSQL函数用于执行SQL语句
		db.execSQL("create table if not exists historyLabel(label text)");
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("update a Database");
		db.execSQL("DROP TABLE IF EXISTS notes");
		onCreate(db);
	}
	
	static public boolean isLabelExist(Context context, String label)
	{
		HistoryLabel dbHelper2 = new HistoryLabel(context, "test_mars_db1");
		SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
		String sql = "select * from historyLabel where label = '"+label+"'";
 		//根据label查询列表返回光标
 		Cursor cursor = db2.rawQuery(sql, null);
 		int count = cursor.getCount();
 		
 		Log.d("label count = ", String.valueOf(count));
 		cursor.close();
 		
 		if (db2 != null && db2.isOpen())
            db2.close();
            
        if (dbHelper2 != null) 
    		dbHelper2.close();
    	
 		if(count != 0)
 			return true;
		return false;
	}
	static public ArrayList<String> QueryLabels(Context context){
		ArrayList<String> tt = new ArrayList<String>();
		HistoryLabel dbHelper2 = new HistoryLabel(context, "test_mars_db1");
		SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
		Cursor cursor = db2.query("historyLabel", new String[]{"label"}, null, null, null, null,null);
		while(cursor.moveToNext()){
			String label = cursor.getString(cursor.getColumnIndex("label"));
			tt.add(label);
		}
		cursor.close();
		if (db2 != null && db2.isOpen()) {
            db2.close();
		}

		if (dbHelper2 != null) {
			dbHelper2.close();
		}
		return tt;
	}
/*	
	static public int QueryNum(Context context,String name){
		HistoryLabel dbHelper2 = new HistoryLabel(context, "test_mars_db1");
		SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
		String sql = "select * from historyLabel where label = '"+name+"'";
		Cursor cursor = db2.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		if (db2 != null && db2.isOpen()) {
            db2.close();
     }

     if (dbHelper2 != null) {
        dbHelper2.close();
     }
		return count;
	}
	*/
}

