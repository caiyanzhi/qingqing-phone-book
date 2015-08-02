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
	
	//以下存放的是每一个标签对应联系人的布局内容
	final private int MAXADDEDLABELS = 500; 
	private View items[] = new View[MAXADDEDLABELS];
	private TextView name[] = new TextView[MAXADDEDLABELS];
	private ImageView img[] = new  ImageView[MAXADDEDLABELS];
	private TextView star[] = new TextView[MAXADDEDLABELS];
	private TextView label[] = new TextView[MAXADDEDLABELS];
	//记录是否有存储电话和名字
    private boolean isRecorded = false;
	//用于存放label对应的所以电话和名字，供群发短信的传值使用
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
	
	//初始化布局文件
	private void initView2() {
		// TODO Auto-generated method stub
		count = 0;
		list.removeAllViews();
		contacts = new ArrayList<String>();
		DatabaseHelper dbHelper1 = new DatabaseHelper(checkLabel.this,"test_mars_db");
		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
		Cursor cursor = db1.query("user", new String[]{"name","groupp","phone","photoURI","star"}, "groupp like'%" +(String)getIntent().getSerializableExtra("label") + "%'",null, null, null, " name COLLATE LOCALIZED  ASC");
		//查询标签为此label的所以联系人
		//String sql = "select * from user where groupp like'%" + (String)getIntent().getSerializableExtra("label") + "%'";
		
		//Cursor cursor = db1.rawQuery(sql, null);
		if(cursor==null){
			items[0] = LayoutInflater.from(checkLabel.this).inflate(R.layout.simple, null);
			name[0] = (TextView)items[0].findViewById(R.id.text);
			name[0].setText("该标签无对应的联系人");
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
			
			if(img1.equals("null") == true)//++++++++++++++++++这里存的是字符串啊，如果无图片存的是null++++++++++++++++++++++++++
				img[count].setImageResource(R.drawable.headportrait_main);
    	    else
    	    	img[count].setImageURI(Uri.parse(img1));
  		      star[count] = (Button) items[count].findViewById(R.id.view_btn);
  		  //更新star的值并改变现实状态
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
            	        //更新star的值并改变现实状态
            		    if( star.equals("1")){
            			    star = "0";
            			    v.setBackgroundResource(R.drawable.star_no);
            		     }
            		    else{
            			     star = "1";
        			         v.setBackgroundResource(R.drawable.star_yes);
            		    }
        		    
            		
            		//更新数据库
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
				 
				//若没有存储，那么添加
					if(isRecorded==false){
					    tmpNames.add(name1);
					    tmpPhones.add(phone1);
					}
		 
					//点击挑转到查看联系人页面
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
			
			//若没有存储，那么添加
			if(isRecorded==false){
			    tmpNames.add(name1);
			    tmpPhones.add(phone1);
			}
			//标签只显示第一个电话
			phone1= getFirstPhone(phone1);
			phone[count].setText(phone1);
			items[count].setTag(name1);//是为了可以跳转到查看页面，此处添加name作为查询的条件			
			//绘制背景，让两个轮流替换
    	    if(count % 2==0)
    	    	items[count].setBackgroundResource(R.drawable.background_row01);
    	    else
    	        items[count].setBackgroundResource(R.drawable.background_row02);
    	    
			//点击挑转到查看联系人页面
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
			
			//给button添加TAG = phone便于打电话和发短信
			dial_bts[count].setTag(phone1);
			dial_bts[count].setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					String phone = (String) v.getTag();
					if(phone.equals(""))
					{
						Toast.makeText(checkLabel.this, "你没存储该联系人的电话啊( ⊙ o ⊙ )！", Toast.LENGTH_SHORT).show();
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
						Toast.makeText(checkLabel.this, "你没存储该联系人的电话啊( ⊙ o ⊙ )！", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent();
					//系统默认的action，用来打开默认的短信界面
					intent.setAction(Intent.ACTION_SENDTO);
					//需要发短息的号码
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
