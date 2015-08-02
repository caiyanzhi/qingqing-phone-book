package com.example.qingqing701;



import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

//HistoryLabel��Ϊһ������SQLite�������࣬�ṩ��������Ĺ��ܣ�
//��һ��getReadableDatabase(),getWritableDatabase()���Ի��SQLiteDatabse����ͨ���ö�����Զ����ݿ���в���
//�ڶ����ṩ��onCreate()��onUpgrade()�����ص����������������ڴ������������ݿ�ʱ�������Լ��Ĳ���

public class HistoryLabel extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	     
	//��SQLiteOepnHelper�����൱�У������иù��캯��
	public HistoryLabel(Context context, String name, CursorFactory factory,int version) {
		//����ͨ��super���ø��൱�еĹ��캯��
		super(context, name, factory, version);
	}
	
	public HistoryLabel(Context context,String name){
		this(context,name,VERSION);
	}
	
	public HistoryLabel(Context context,String name,int version){
		this(context, name,null,version);
	}

	//�ú������ڵ�һ�δ������ݿ��ʱ��ִ��,ʵ�������ڵ�һ�εõ�SQLiteDatabse�����ʱ�򣬲Ż�����������
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a Database");
		
		//execSQL��������ִ��SQL���
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
 		//����label��ѯ�б��ع��
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

