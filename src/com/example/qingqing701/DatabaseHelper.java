package com.example.qingqing701;



import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

//DatabaseHelper��Ϊһ������SQLite�������࣬�ṩ��������Ĺ��ܣ�
//��һ��getReadableDatabase(),getWritableDatabase()���Ի��SQLiteDatabse����ͨ���ö�����Զ����ݿ���в���
//�ڶ����ṩ��onCreate()��onUpgrade()�����ص����������������ڴ������������ݿ�ʱ�������Լ��Ĳ���

public class DatabaseHelper extends SQLiteOpenHelper {
	
	private static final int VERSION = 1;
	     
	
	//��SQLiteOepnHelper�����൱�У������иù��캯��
	public DatabaseHelper(Context context, String name, CursorFactory factory,int version) {
		//����ͨ��super���ø��൱�еĹ��캯��
		super(context, name, factory, version);		
	}
	
	public DatabaseHelper(Context context,String name){
		this(context,name,VERSION);
	}
	
	public DatabaseHelper(Context context,String name,int version){
		this(context, name,null,version);
	}

	//�ú������ڵ�һ�δ������ݿ��ʱ��ִ��,ʵ�������ڵ�һ�εõ�SQLiteDatabse�����ʱ�򣬲Ż�����������
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create a Database");
		
		//execSQL��������ִ��SQL���
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
		//ѡ����䣬������������ϵ�����ֻ�绰��ģ������
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

