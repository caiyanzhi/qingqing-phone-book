package com.example.qingqing701;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

//DatabaseHelper作为一个访问SQLite的助手类，提供两个方面的功能，
//第一，getReadableDatabase(),getWritableDatabase()可以获得SQLiteDatabse对象，通过该对象可以对数据库进行操作
//第二，提供了onCreate()和onUpgrade()两个回调函数，允许我们在创建和升级数据库时，进行自己的操作

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	     
	
	//在SQLiteOepnHelper的子类当中，必须有该构造函数
	public DatabaseHelper(Context context, String name, CursorFactory factory,int version) {
		//必须通过super调用父类当中的构造函数
		super(context, name, factory, version);		
	}
	
	public DatabaseHelper(Context context,String name){
		this(context,name,VERSION);
	}
	
	public DatabaseHelper(Context context,String name,int version){
		this(context, name,null,version);
	}

	//该函数是在第一次创建数据库的时候执行,实际上是在第一次得到SQLiteDatabse对象的时候，才会调用这个方法
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a Database");
		
		//execSQL函数用于执行SQL语句
		db.execSQL("create table if not exists user(photoURI text,name varchar(20),phone text,mail text,groupp text,star text,ringURI text,PY text)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("update a Database");
		db.execSQL("DROP TABLE IF EXISTS notes");
		onCreate(db);
	}
	
	static public int QueryNum(Context context, String tmpGroupp){
		DatabaseHelper dbHelper1 = new DatabaseHelper(context,"test_mars_db");
		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
		//选择语句，让搜索根据联系人名字或电话来模糊搜索
		String sql = "select * from user where groupp like'%" + tmpGroupp + "%'";
		
		Cursor cursor = db1.rawQuery(sql, null);
		int count = cursor.getCount();
		cursor.close();
		if (db1 != null && db1.isOpen()) {
            db1.close();
        }

        if (dbHelper1 != null) {
           dbHelper1.close();
        }
		return count;
	}
	
}

