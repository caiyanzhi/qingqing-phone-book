package com.example.qingqing701;

import java.util.ArrayList;

import com.example.contactersss.R;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class checkLabel extends Activity{
	private LinearLayout list;//
	ArrayList<String> contacts;
	
	//���´�ŵ���ÿһ����ǩ��Ӧ��ϵ�˵Ĳ�������
	final private int MAXADDEDLABELS = 500; 
	private View items[] = new View[MAXADDEDLABELS];
	private TextView name[] = new TextView[MAXADDEDLABELS];
	private ImageView img[] = new  ImageView[MAXADDEDLABELS];
	private TextView star[] = new TextView[MAXADDEDLABELS];
	private TextView label[] = new TextView[MAXADDEDLABELS];
	//��¼�Ƿ��д洢�绰������
    private boolean isRecorded = false;
	//���ڴ��label��Ӧ�����Ե绰�����֣���Ⱥ�����ŵĴ�ֵʹ��
	ArrayList<String> tmpPhones  = new ArrayList<String>();
	ArrayList<String> tmpNames  = new ArrayList<String>();
	
	private int count;
	@Override
	protected void onCreate(Bundle save){
		super.onCreate(save);
		setContentView(R.layout.checklabel);
		initView();
		initView2();
	}
	
	@Override 
	protected void onRestart(){
		super.onRestart();
		isRecorded = true;
	    initView2();
	}
	
	//��ʼ�������ļ�
	private void initView2() {
		// TODO Auto-generated method stub
		count = 0;
		list.removeAllViews();
		contacts = new ArrayList<String>();
		DatabaseHelper dbHelper1 = new DatabaseHelper(checkLabel.this,"test_mars_db");
		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
		Cursor cursor = db1.query("user", new String[]{"name","groupp","phone","photoURI","star"}, "groupp like'%" +(String)getIntent().getSerializableExtra("label") + "%'",null, null, null, " name COLLATE LOCALIZED  ASC");
		//��ѯ��ǩΪ��label��������ϵ��
		//String sql = "select * from user where groupp like'%" + (String)getIntent().getSerializableExtra("label") + "%'";
		
		//Cursor cursor = db1.rawQuery(sql, null);
		if(cursor==null){
			items[0] = LayoutInflater.from(checkLabel.this).inflate(R.layout.simple, null);
			name[0] = (TextView)items[0].findViewById(R.id.text);
			name[0].setText("�ñ�ǩ�޶�Ӧ����ϵ��");
			list.addView(items[count]);
		}
		else
		while(cursor.moveToNext()){
			String name1 = cursor.getString(cursor.getColumnIndex("name"));
			String star1 = cursor.getString(cursor.getColumnIndex("star"));
			String label1 = cursor.getString(cursor.getColumnIndex("groupp"));
			String phone1 =  cursor.getString(cursor.getColumnIndex("phone"));
			String img1= cursor.getString(cursor.getColumnIndex("photoURI"));
			
			items[count] = LayoutInflater.from(checkLabel.this).inflate(R.layout.adapterview, null);
			list.addView(items[count]);
			img[count] = (ImageView) items[count].findViewById(R.id.img);
  		    name[count] = (TextView) items[count].findViewById(R.id.title);
  		    label[count] = (TextView) items[count].findViewById(R.id.label_in_main);
  		    label[count].setText(label1);
			name[count].setText(name1);
			items[count].setTag(name1);
			
			if(img1.equals("null") == true)//++++++++++++++++++���������ַ������������ͼƬ�����null++++++++++++++++++++++++++
				img[count].setImageResource(R.drawable.headportrait_main);
    	    else
    	    	img[count].setImageURI(Uri.parse(img1));
  		      star[count] = (Button) items[count].findViewById(R.id.view_btn);
  		  //����star��ֵ���ı���ʵ״̬
      		if( star1.equals("1"))
      		    star[count].setBackgroundResource(R.drawable.star_yes);
      		else
      			star[count].setBackgroundResource(R.drawable.star_no);
      		
  		    star[count].setTag(name1);
            star[count].setOnClickListener(new OnClickListener() {

				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
                	String name = (String)v.getTag();
                    DatabaseHelper dbHelper1 = new DatabaseHelper(checkLabel.this,"test_mars_db");
            		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
            		Cursor cursor = db1.query("user", new String[]{"star"},"name = ?",new String[]{name},null,null,null);
                    if(cursor.moveToNext())
            		{
            			String star = cursor.getString(cursor.getColumnIndex("star")).toString();
            	        //����star��ֵ���ı���ʵ״̬
            		    if( star.equals("1")){
            			    star = "0";
            			    v.setBackgroundResource(R.drawable.star_no);
            		     }
            		    else{
            			     star = "1";
        			         v.setBackgroundResource(R.drawable.star_yes);
            		    }
        		    
            		
            		//�������ݿ�
            		ContentValues values = new ContentValues();
            		if(star.equals("1"))
            	    values.put("star", "1");
                    else
                    values.put("star","0");
            		
            		db1.update("user", values, "name = ?", new String []{name});
            		} 
            	   //	Toast.makeText(MainActivity.this,star+" "+' '+ currentName.toString()+' '+"dd"+num,Toast.LENGTH_SHORT).show();
            		cursor.close();
            		 if (db1 != null && db1.isOpen()) {
            	            db1.close();
            	     }

            	       if (dbHelper1 != null) {
            	         dbHelper1.close();
            	       }
				   }
				});
				
             if(count % 2==0)
		    	    	items[count].setBackgroundResource(R.drawable.background_row01);
		    	    else
		    	        items[count].setBackgroundResource(R.drawable.background_row02);
				 
				//��û�д洢����ô���
					if(isRecorded==false){
					    tmpNames.add(name1);
					    tmpPhones.add(phone1);
					}
		 
					//�����ת���鿴��ϵ��ҳ��
					items[count].setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							String tmp = (String)v.getTag();
							Intent mIntent = new Intent(checkLabel.this,CheckContacter.class);
							mIntent.putExtra("name", tmp);
							checkLabel.this.startActivity(mIntent);
						}
					});
		/*	
			items[count] = LayoutInflater.from(checkLabel.this).inflate(R.layout.searchview, null);
			name[count] = (TextView)items[count].findViewById(R.id.name);
			phone[count] = (TextView)items[count].findViewById(R.id.phone);
			dial_bts[count] = (Button)items[count].findViewById(R.id.dial_bt);
			sentMess_bts[count] = (Button)items[count].findViewById(R.id.sentMess_bt);
		    list.addView(items[count]);
		    String name1 = cursor.getString(cursor.getColumnIndex("name"));
		   // System.out.println(cursor.getString(cursor.getColumnIndex("name")));
			name[count].setText(name1);
			String phone1 = cursor.getString(cursor.getColumnIndex("phone"));
			
			//��û�д洢����ô���
			if(isRecorded==false){
			    tmpNames.add(name1);
			    tmpPhones.add(phone1);
			}
			//��ǩֻ��ʾ��һ���绰
			phone1= getFirstPhone(phone1);
			phone[count].setText(phone1);
			items[count].setTag(name1);//��Ϊ�˿�����ת���鿴ҳ�棬�˴����name��Ϊ��ѯ������			
			//���Ʊ����������������滻
    	    if(count % 2==0)
    	    	items[count].setBackgroundResource(R.drawable.background_row01);
    	    else
    	        items[count].setBackgroundResource(R.drawable.background_row02);
    	    
			//�����ת���鿴��ϵ��ҳ��
			items[count].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String tmp = (String)v.getTag();
					Intent mIntent = new Intent(checkLabel.this,CheckContacter.class);
					mIntent.putExtra("name", tmp);
					checkLabel.this.startActivity(mIntent);
				}
			});
			
			//��button���TAG = phone���ڴ�绰�ͷ�����
			dial_bts[count].setTag(phone1);
			dial_bts[count].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					String phone = (String) v.getTag();
					if(phone.equals(""))
					{
						Toast.makeText(checkLabel.this, "��û�洢����ϵ�˵ĵ绰��( �� o �� )��", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_CALL);
					
					intent.setData(Uri.parse("tel:"+phone));
					checkLabel.this.startActivity(intent);
				}
			});
			
			sentMess_bts[count].setTag(phone1);
			sentMess_bts[count].setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String phone = (String) v.getTag();
					if(phone.equals(""))
					{
						Toast.makeText(checkLabel.this, "��û�洢����ϵ�˵ĵ绰��( �� o �� )��", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent();
					//ϵͳĬ�ϵ�action��������Ĭ�ϵĶ��Ž���
					intent.setAction(Intent.ACTION_SENDTO);
					//��Ҫ����Ϣ�ĺ���
					intent.setData(Uri.parse("smsto:"+phone));
					checkLabel.this.startActivity(intent);
				}
			});*/
			count++;
			
		}
		
		cursor.close();
		if (db1 != null && db1.isOpen()) {
	           db1.close();
	    }

	     if (dbHelper1 != null) {
	         dbHelper1.close();
	     }
	}

	private Button bt_back,bt_GroupSM;

	TextView text;

	private void initView() {
		bt_GroupSM = (Button)findViewById(R.id.groupSentMess);
		bt_GroupSM.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(checkLabel.this,sentMessToGroup.class);
				intent.putStringArrayListExtra("name", tmpNames);
				intent.putStringArrayListExtra("phone", tmpPhones);
				checkLabel.this.startActivity(intent);
			}
		});
		
		bt_back = (Button)findViewById(R.id.back);
		bt_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				finish();
			}
		});
		
		text= (TextView)findViewById(R.id.text);
		text.setText((String)getIntent().getSerializableExtra("label"));
		list = (LinearLayout)findViewById(R.id.labelList);
		
	}

}
