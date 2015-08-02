
package com.example.qingqing701;

import java.util.ArrayList;

import com.example.contactersss.R;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CheckContacter extends Activity{
	  private static final int PHONE_NUMBER = 100;
	private Button bt_back,bt_edit,bt_delete;
	TextView name,email,labeltest;
	ImageView userImage,star;
	private TextView editPhone[] = new TextView[PHONE_NUMBER ];
	private Button deletePhone[] = new Button[PHONE_NUMBER];
	private Button callBtn[] = new Button[PHONE_NUMBER];
	private Button sendSMSBtn[] = new Button[PHONE_NUMBER];
	private View phoneList[] = new View[PHONE_NUMBER ];
	
	private RelativeLayout phoneRL[] = new RelativeLayout[PHONE_NUMBER];

    private LinearLayout phoneset ;
	String userName,phone,mail,addedlabels;
	String[] phoneStr = new String[PHONE_NUMBER];
	String photoURI;
	int count = 0; //��¼�ж��ٸ��绰
	boolean hasMail = false;//�ж���û���ʼ�
	DatabaseHelper dbHelper1 = new DatabaseHelper(CheckContacter.this,"test_mars_db");
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.checklayout);
		//��ʼ�������ļ�
		initView();
		phoneset = (LinearLayout) findViewById(R.id.phoneset);
		//System.out.println("after init");

 		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();

		name =(TextView)findViewById(R.id.name);
		userImage = (ImageView)findViewById(R.id.addImage);
		star = (ImageView)findViewById(R.id.Star);
		//��ȡ��������name
		userName  = (String)getIntent().getSerializableExtra("name");
		
        //��������
		name.setText(userName);
		
 		String sql = "select * from user where name = '"+userName+"'";
 		//�������ֲ�ѯ�б��ع��
 		Cursor cursor = db1.rawQuery(sql, null);
 		if(cursor.equals(null)==false)
        if(cursor.moveToNext())
        {//����star��״̬
        	//System.out.println("start cursor");
        	if(cursor.getString(cursor.getColumnIndex("star")).equals("1")==true)
        		star.setBackgroundResource(R.drawable.star_yes);
        	else
        		star.setBackgroundResource(R.drawable.star_no);
        	java.lang.System.out.println("star ok");
 		 //������ϵ��ͷ�������ϵ��û��ͷ��Ļ����һĬ��ͷ��
        	photoURI = cursor.getString(cursor.getColumnIndex("photoURI"));
	        if(cursor.getString(cursor.getColumnIndex("photoURI")).equals("null") == true)
    	    userImage.setImageResource(R.drawable.headportrait_main);
    	    else
    	    userImage.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex("photoURI"))));
            
	        ringName = cursor.getString(cursor.getColumnIndex("ringURI"));
	        addedlabels =cursor.getString(cursor.getColumnIndex("groupp"));
 		java.lang.System.out.println("photo ok");
 		
 		//��ʾ�绰
 		phone = cursor.getString(cursor.getColumnIndex("phone"));
 		getPhones(phone);
 		showPhones(phoneStr);
 		
 		//��ʾ���䣺
 		mail = cursor.getString(cursor.getColumnIndex("mail"));
 		
 		if ( mail.equals("@") == false){
 			hasMail = true;
 			RelativeLayout line = (RelativeLayout) findViewById(R.id.mailline);
 			line.setVisibility(RelativeLayout.VISIBLE);
 			if (count % 2 == 0)
 				line.setBackgroundResource(R.drawable.background_row01);
 			else line.setBackgroundResource(R.drawable.background_row02);
 			email = (TextView) findViewById(R.id.mail);
		 	email.setText(mail);		
 		}
       }
 		 cursor.close();
 	    if (db1 != null && db1.isOpen()) {
             db1.close();
         }
 	    
 	   initRing();
 	   
 		//ɾ������
 		bt_delete = (Button) findViewById(R.id.deleteCon);
 		bt_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//new AlertDialog.Builder(CheckContacter.this)
				CustomDialog.Builder builder = new CustomDialog.Builder(CheckContacter.this);
			    builder.setMessage("ȷ��ɾ������ϵ����");
			    builder.setTitle("��ʾ");
			    builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						SQLiteDatabase db2 = dbHelper1.getWritableDatabase();
						db2.delete("user", "name=?", new String[]{userName});				
				    	delContact((Context)CheckContacter.this,userName);		
				        if (db2 != null && db2.isOpen()) {
				           db2.close();
				        }
				        if (dbHelper1 != null) {
				            dbHelper1.close();
				          }	
				        finish();
					}
				});
			    builder.setNegativeButton("ȡ��",
			    		new android.content.DialogInterface.OnClickListener() {
			    			public void onClick(DialogInterface dialog, int which) {
			    				dialog.dismiss();
			    			}
			    		});

				builder.create().show();	
				
				
				
			}
		});
 		if (dbHelper1 != null) {
            dbHelper1.close();
          }	
 	   
 		labeltest.setText(addedlabels);
	}
	
	private void initView() {
		bt_back = (Button)findViewById(R.id.back);
		bt_edit = (Button)findViewById(R.id.edit);
	
		//����������б�
		bt_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				 Intent intent = new Intent(CheckContacter.this,MainActivity.class);
				  setResult(RESULT_OK, intent);
					finish();
			}
		});
		
		//�������༭����
		bt_edit.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(CheckContacter.this,EditContacter.class);
				//���ϴ��Ķ���
				finish();
				intent.putExtra("name", userName);
				intent.putExtra("photoURI", photoURI); 
				intent.putExtra("phone", phone);
				intent.putExtra("mail", mail);
				intent.putExtra("ringURI",ringName);
				intent.putExtra("label", addedlabels);
				startActivity(intent);
			}
		});
		
		labeltest = (TextView) findViewById(R.id.label);
		
		labeltest.setFocusableInTouchMode(true);
		labeltest.requestFocus();
		
	}

	//��������
	String ringName="Ĭ������";
	TextView ring_name;
    private void initRing(){
    	RelativeLayout line = (RelativeLayout) findViewById(R.id.ring_line);
    	if (hasMail == false){
    		if (count % 2 == 0)
    			line.setBackgroundResource(R.drawable.background_row01);
    		else line.setBackgroundResource(R.drawable.background_row02);
    	}
    	else{
    		if (count % 2 == 0)
    			line.setBackgroundResource(R.drawable.background_row02);
    		else line.setBackgroundResource(R.drawable.background_row01);   		
    	}
    	ring_name = (TextView)findViewById(R.id.ring_name);
    	ring_name.setText(ringName);
    	
    }
    
    //���绰���������ֳ���������phoneStr[]
    public void getPhones(String phone){
    	int index[] = new int[20];
    	int indexcount = 0;
    	//System.out.println("Phone: " + phone);

    	//��phoneΪ�գ�����뷵�أ�����������
    	if(phone.length()==0)
        	return;
    	phone+="*";
    	for (int i = 0 ; i < phone.length() ; ++i){
    		String t = phone.substring(i, i+1);
    		if (phone.charAt(i) == '*'){
    			index[indexcount] = i;
    			indexcount ++;
    		}
    	}
    	//System.out.println("indexcount: " + String.valueOf(indexcount));

    	for (int i = 0 ; i < indexcount - 1; ++i){
    		int temp = index[i] + 1;
    		phoneStr[count] = phone.substring(temp, index[i+1]);    		
    		count++;
    	}
    	//System.out.println(String.valueOf(count));
    	//phoneStr[count] = phone.substring(index[indexcount - 1]+1);
    }
    
    //��ʾ����绰
    public void showPhones(String[] phoneStr){    	
    	for (int phoneCount = 0; phoneCount < count; ++phoneCount){
    		java.lang.System.out.println(" " + phoneCount);
			phoneList[phoneCount] = LayoutInflater.from(CheckContacter.this).inflate(R.layout.phonesetincheck, null);
			phoneset.addView(phoneList[phoneCount]);
			editPhone[phoneCount] = (TextView)phoneList[phoneCount].findViewById(R.id.phone); 
			editPhone[phoneCount].setText(phoneStr[phoneCount]);					
			phoneRL[phoneCount] = (RelativeLayout)phoneList[phoneCount]. findViewById(R.id.checkRL);
			if (phoneCount % 2 == 0)
				phoneRL[phoneCount].setBackgroundResource(R.drawable.background_row01);
			else 
				phoneRL[phoneCount].setBackgroundResource(R.drawable.background_row02);			
			callBtn[phoneCount] = (Button) phoneList[phoneCount].findViewById(R.id.call);
			sendSMSBtn[phoneCount] = (Button) phoneList[phoneCount].findViewById(R.id.sendSMS);	
			callBtn[phoneCount].setOnClickListener(new callClick(phoneCount));
			sendSMSBtn[phoneCount].setOnClickListener(new sendSMSClick(phoneCount));
    	}
    }
    
    
    public class callClick implements OnClickListener{
    	String telephone;
    	public callClick(int index) {
			// TODO Auto-generated constructor stub
    		this.telephone = phoneStr[index];
    		java.lang.System.out.println("call index "+String.valueOf(phoneStr[index]));
		}
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
		      //ϵͳĬ�ϵ�action��������Ĭ�ϵĵ绰����
  		  java.lang.System.out.println("call num "+String.valueOf(this.telephone));
		        intent.setAction(Intent.ACTION_CALL);
		      //��Ҫ����ĺ���
		        intent.setData(Uri.parse("tel:"+this.telephone));
		        CheckContacter.this.startActivity(intent);
			
		}
    }

    public class sendSMSClick implements OnClickListener{
    	String telephone;
    	public sendSMSClick(int index) {
			// TODO Auto-generated constructor stub
    		this.telephone = phoneStr[index];
		}
    	
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			java.lang.System.out.println("SMS"+String.valueOf(this.telephone));
			Intent intent = new Intent();
			//ϵͳĬ�ϵ�action��������Ĭ�ϵĶ��Ž���
			intent.setAction(Intent.ACTION_SENDTO);
			//��Ҫ����Ϣ�ĺ���
			intent.setData(Uri.parse("smsto:"+this.telephone));
			CheckContacter.this.startActivity(intent);
		}    	
    }
    
    //ɾ������ͨѶ¼����ϵ��
    private void delContact(Context context, String name) {    	 
        //��ת������ѯ��name�Ĺ�괦
    	Cursor cursor = getContentResolver().query(Data.CONTENT_URI,new String[] { 
    			Data.RAW_CONTACT_ID },ContactsContract.Contacts.DISPLAY_NAME + "=?",
    			new String[] { name }, null); 
    	ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();    	 
    	if (cursor.moveToFirst()) {    	 
    	do {    	 
    	long Id = cursor.getLong(cursor.getColumnIndex(Data.RAW_CONTACT_ID));  	 
    	ops.add(ContentProviderOperation.newDelete(    	 
    	ContentUris.withAppendedId(RawContacts.CONTENT_URI,Id)).build());    	 
    	try {
    	 	getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);    	 
    	} 
    	catch (Exception e){}    	 
    	} while (cursor.moveToNext());    	 
    	cursor.close();    	 
    	}    	 
   }
}
