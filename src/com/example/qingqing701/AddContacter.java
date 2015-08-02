package com.example.qingqing701;


import java.io.File;
import java.sql.Date;
import java.text.SimpleDateFormat;
import com.example.contactersss.R;
import android.R.color;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Data;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddContacter extends Activity{
	private Button back,save,addPhone,addlabelBtn;
	private ImageButton editPhoto;
	private EditText editName, editEmail;
	private TextView label;
	
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int PHONE_NUMBER = 100;
    
    private EditText editPhone[] = new EditText[PHONE_NUMBER];
    private Button deletePhone[] = new Button[PHONE_NUMBER];
    private View phoneList[] = new View[PHONE_NUMBER];
    private LinearLayout phoneset;
    private int phoneCount = 0;//有多少个电话框
    int i=1;   
    
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());

	String PhotoUri = "null";
	ImageButton addImage;
	TextView title;

    EditText edittext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addlayout);
		initView();
	}
	
 
	 Button add_ring;//铃声的按钮
	 Uri ring_uri; //铃声的uri
	 String  ringName = "默认铃声";//铃声名
	 TextView ring_name;//铃声名
	 //初始化铃声
	 public void Ring(){
		 
		 ring_uri = Uri.parse("isNullcontext://");
		 add_ring = (Button)findViewById(R.id.add_ring);
		ring_name = (TextView)findViewById(R.id.ring_name);
		ring_name.setText(ringName);
		 add_ring.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent( RingtoneManager.ACTION_RINGTONE_PICKER);
		        /*設定顯示鈴聲的資料夾*/
				
		        intent.putExtra( RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_RINGTONE);
		        /*設定顯示鈴聲開頭*/
		        intent.putExtra( RingtoneManager.EXTRA_RINGTONE_TITLE,"設定鈴聲");
		        
				startActivityForResult(intent,0);
			}
		});
	 }
	 
	 
	 //保存铃声的URI
	 private void SaveRingUri(){
		 if(ring_uri == null)
		 {
			 ring_uri = Uri.parse("Context://");
		 }
		 Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,ContactsContract.Contacts.DISPLAY_NAME + "=?",new String[] { editName.getText().toString()}, null);
		 //Cursor dd = getContentResolver().query(Data.CONTENT_URI,new String[] { Data.RAW_CONTACT_ID },ContactsContract.Contacts.CUSTOM_RINGTONE + "=?",new String[] { editName.getText().toString() }, null);
		 
	      if(cursor.moveToNext()) 
	      {
	    	 // Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,cursor.getColumnIndex(ContactsContract.Contacts._ID));
	    	 // System.out.println(cursor.getString(cursor.getColumnIndex("ContactsContract.Contacts.CUSTOM_RINGTONE")));
		      ContentValues values = new ContentValues();
             values.put(ContactsContract.Contacts.CUSTOM_RINGTONE, ring_uri.toString());
             AddContacter.this.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, values, ContactsContract.Contacts.DISPLAY_NAME + "=?", new String[] { editName.getText().toString() });
             //Toast.makeText(EditContacter.this,cursor.getString(cursor.getColumnIndex("ContactsContract.Contacts.DISPLAY_NAME")) ,Toast.LENGTH_SHORT).show();
             //System.out.println(cursor.getString(cursor.getColumnIndex("ContactsContract.Contacts.CUSTOM_RINGTONE")));
	      }
       //Toast.makeText(EditContacter.this,ring_uri.toString()+"设置铃声成功"+cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), Toast.LENGTH_SHORT).show();
	 }
	 
	 
	private void initView() {
		// TODO Auto-generated method stub
		Ring();
		label = (TextView)findViewById(R.id.label);
		addlabelBtn = (Button)findViewById(R.id.addlabelBtn);
		addlabelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AddContacter.this,AddLabel.class);
				intent.putExtra("name", editName.getText().toString());
				intent.putExtra("is", "false");
				//*********************************
				intent.putExtra("label", label.getText().toString() );
				startActivityForResult(intent, 9);
			}
		});
		back = (Button)findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		save = (Button)findViewById(R.id.save);
		save.setOnClickListener(new MyBtnOnClickListener());			
			
		title = (TextView)findViewById(R.id.text_message);
		title.setText("添加联系人");
		
		//头像
		editPhoto = (ImageButton) findViewById(R.id.addImage);
		editPhoto.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ImgBtnshowDialog();
				}
			});
		
		editEmail = (EditText)findViewById(R.id.mail);
		editName = (EditText) findViewById(R.id.name);
		editName.setText((String)getIntent().getSerializableExtra("name"));
		phoneset = (LinearLayout) findViewById(R.id.phoneset);
		addPhone = (Button) findViewById(R.id.add_phone_btn);
		addPhone.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				View t1 = LayoutInflater.from(AddContacter.this).inflate(R.layout.phonesetlayout, null);
				//phoneList.add(t1);
				phoneList[phoneCount] = t1;
				phoneset.addView(t1);
				//EditText temp = (EditText)t1.findViewById(R.id.phone); 
				//editPhone.add(temp);				
				//deletePhone.add( (Button)t1.findViewById(R.id.delete_phone_btn) );
				editPhone[phoneCount] = (EditText)t1.findViewById(R.id.phone); 
				deletePhone[phoneCount] = (Button)t1.findViewById(R.id.delete_phone_btn) ;
				deletePhone[phoneCount].setOnClickListener(new deleteBtn(phoneCount));
				System.out.println("PC(before)="+String.valueOf(phoneCount));
				phoneCount ++;			
				}
		});		
	}
	
	public class deleteBtn implements OnClickListener{
		int index;
		public deleteBtn(int index) {
			// TODO Auto-generated constructor stub
			this.index = index;
		}
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			phoneset.removeView(phoneList[index]);

			for (int i = index; i < phoneCount-1; ++i)
				editPhone[i] =editPhone[i+1];
			if (phoneCount > 0) phoneCount --;
			System.out.println("PC(after)="+String.valueOf(phoneCount));
		}
	}
	
	
	//点击修改头像 提示对话框方法
	private void ImgBtnshowDialog() {
		     if (PhotoUri.equals("null") == false){
		        CustomDialogThree.Builder builder = new CustomDialogThree.Builder(this);
		        builder.setTitle("头像设置");
		        builder.setMessage("请选择操作");
		        builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        dialog.dismiss();
		        // 调用系统的拍照功能
		        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		        // 指定调用相机拍照后照片的储存路径
		        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
		        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
		        }
		        });
		        
		        builder.setNeutralButton("相册", new DialogInterface.OnClickListener() {

		        @Override
		        public void onClick(DialogInterface dialog, int which) {
		        // TODO Auto-generated method stub
		        	dialog.dismiss();
		            Intent intent = new Intent(Intent.ACTION_PICK, null);
		            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
		            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
		       
		        }
		        });
		        
		        builder.setNegativeButton("删除", new DialogInterface.OnClickListener() {
		    		
		    		@Override
		    		public void onClick(DialogInterface dialog, int which) {
		    			// TODO Auto-generated method stub
		    		 dialog.dismiss();
		    		 editPhoto.setBackgroundColor(color.transparent);//像素问题？不能覆盖？
		    		 PhotoUri = "null";
		    		}
		    	});
		        builder.create().show();
		        }
		     else{
		    	 	CustomDialog.Builder builder = new CustomDialog.Builder(this);
		            builder.setTitle("头像设置");
		            builder.setMessage("请选择操作");
		            builder.setPositiveButton("拍照", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            // TODO Auto-generated method stub
		            dialog.dismiss();
		            // 调用系统的拍照功能
		            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		            // 指定调用相机拍照后照片的储存路径
		            intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(tempFile));
		            startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
		            }
		            });		            
		            
		            builder.setNegativeButton("相册", new DialogInterface.OnClickListener() {

		            @Override
		            public void onClick(DialogInterface dialog, int which) {
		            // TODO Auto-generated method stub
		            dialog.dismiss();
		            Intent intent = new Intent(Intent.ACTION_PICK, null);
		            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*");
		            startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
		            }
		            });
		            builder.create().show();
		        }
		        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // TODO Auto-generated method stub

    switch (requestCode) {
    case PHOTO_REQUEST_TAKEPHOTO:
    startPhotoZoom(Uri.fromFile(tempFile), 216); //尺寸要改
    break;

    case PHOTO_REQUEST_GALLERY:
    if (data != null)
    startPhotoZoom(data.getData(), 216);//尺寸要改
    break;

    case PHOTO_REQUEST_CUT:
    if (data != null)
    setPicToView(data);
    break;
    case 0:
        if(data!=null)
        {
        	RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); 
            ring_uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        	//RingtoneManager.getActualDefaultRingtoneUri(AddClock.this, RingtoneManager.TYPE_ALARM);
       
        if(ring_uri!=null){
        Cursor cursor = getContentResolver().query(ring_uri, null, null, null, null);
        cursor.moveToFirst();
        ringName = cursor.getString(2);
        ring_name.setText(ringName);
        }
        
        Log.i("*", "" + ring_uri + "****"+ringName);
        }
        break;
       case 9:
    	String tmp = data.getCharSequenceExtra("label").toString();
    	label.setText(tmp);
    	break;
    }
    super.onActivityResult(requestCode, resultCode, data);

    }

    private void startPhotoZoom(Uri uri, int size) {
    Intent intent = new Intent("com.android.camera.action.CROP");
    intent.setDataAndType(uri, "image/*");
    // crop为true是设置在开启的intent中设置显示的view可以剪裁
    intent.putExtra("crop", "true");

    // aspectX aspectY 是宽高的比例
    intent.putExtra("aspectX", 1);
    intent.putExtra("aspectY", 1);

    // outputX,outputY 是剪裁图片的宽高
    intent.putExtra("outputX", size);
    intent.putExtra("outputY", size);
    intent.putExtra("return-data", true);

    startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    //将进行剪裁后的图片显示到UI界面上
    @SuppressWarnings("deprecation")
	private void setPicToView(Intent picdata) {
    	Bundle bundle = picdata.getExtras();
        if (bundle != null) {
        Bitmap photo = bundle.getParcelable("data");
        PhotoUri =String.valueOf( Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), photo, null,null)) );
        Drawable drawable = new BitmapDrawable(photo);
        editPhoto.setBackgroundDrawable(drawable);
    }
    }

    // 使用系统当前日期加以调整作为照片的名称
    private String getPhotoFileName() {
    Date date = new Date(System.currentTimeMillis());
    SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
    return dateFormat.format(date) + ".jpg";
    }
    
    //将几个电话输入框里的数据合起来为一个字符串，每个电话号码在前面用'*'标示
    public String getPhoneStr(){
        String PhoneStr = "";
        for (int i = 0; i < phoneCount; ++i){
        	String temp;
        	temp = editPhone[i].getText().toString();
        	if (temp.equals("") == false){
        		PhoneStr += '*';
        		PhoneStr += temp;
        	}
        }
        return PhoneStr;
    }

    //确定按钮的监听
	 class MyBtnOnClickListener implements OnClickListener {
		@Override
		public void onClick(View arg0) {		
			String Samename = isSame(editName.getText().toString());
			String mail = editEmail.getText().toString();
			//判断联系人是否可以添加（如：姓名不能为空、电话要有效）	,没有重复联系人存在
		  if(editName.getText().toString().equals(""))    //没有输入名字不能添加
			  Toast.makeText(AddContacter.this, "请输入联系人姓名", 1).show();	
		  else if((mail.contains("@") == false || (mail.contains("@") == true && mail.length() == 1))&&mail.length()!=0)
			  Toast.makeText(AddContacter.this, "请输入有效邮箱地址", 1).show();	
		  else if(Samename.equals("null") == false){
			  Toast.makeText(AddContacter.this, "该联系人已存在，请直接添加到原有联系人处", Toast.LENGTH_SHORT).show();
			  Intent intent = new Intent(AddContacter.this,MainActivity.class);
			  setResult(RESULT_OK, intent);
			  finish();
		  }			  
		  else{
			 
			  System.out.println("ready");
			  AddContacts();
			  insert_Contant();	  
			  //将联系人信息保存到数据库
			  SaveRingUri();
			  Toast.makeText(AddContacter.this, "添加成功", 1).show();
			  Intent intent = new Intent(AddContacter.this,MainActivity.class);
			  setResult(RESULT_OK, intent);
			  finish();
		  }
	     }
	 }    
    
	//---------------插入新数据到自己的数据库-----------------
    public void insert_Contant() {      	 
    	//生成ContentValues对象
		ContentValues values = new ContentValues();
		//想该对象当中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致
		values.put("photoURI",PhotoUri);
		values.put("name",editName.getText().toString());
		values.put("phone", getPhoneStr());
		values.put("ringURI",ringName);
		values.put("groupp",label.getText().toString());
		 
		Cursor cursor = getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,null,ContactsContract.Contacts.DISPLAY_NAME + "=?",new String[] {editName.getText().toString()}, null);
		 //Cursor dd = getContentResolver().query(Data.CONTENT_URI,new String[] { Data.RAW_CONTACT_ID },ContactsContract.Contacts.CUSTOM_RINGTONE + "=?",new String[] { editName.getText().toString() }, null);
		 
		 String mPY="";
	      if(cursor.moveToNext()) 
	      {
	    	  String PY = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.SORT_KEY_ALTERNATIVE));
	           int size = PY.length();
	          
	          for(int i=0;i<size;i++){
	        	  char ch = PY.charAt(i);
	        	  if(ch!=' ' && ch < 128)
	        		  mPY+=ch;
	          }
	      }
	      cursor.close();
		
		values.put("PY", mPY);
		if (editEmail.getText().toString().equals("") == false)
			values.put("mail",editEmail.getText().toString());
		else 
			values.put("mail", "@");
		//群组选择功能目前未实现
		values.put("star", "0");

		DatabaseHelper dbHelper = new DatabaseHelper(AddContacter.this,"test_mars_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		//调用insert方法，就可以将数据插入到数据库当中
		db.insert("user", null, values);     
	    if (db != null && db.isOpen()) {
            db.close();
        }

       if (dbHelper != null) {
         dbHelper.close();
       }
       
       //insert_to_phone();
    }
    
  //添加联系人到手机
    public void AddContacts(){ 
    	String name = editName.getText().toString();
	    String email = editEmail.getText().toString();
    	
    	 // 插入raw_contacts表，并获取_id属性
    	  Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
    	  ContentResolver resolver = AddContacter.this.getContentResolver();
    	  ContentValues values = new ContentValues();
    	  long contact_id = ContentUris.parseId(resolver.insert(uri, values));
    	  // 插入data表
    	  uri = Uri.parse("content://com.android.contacts/data");
    	  // add Name
          
    	  if (name.length() > 0) {
    	   values.put("raw_contact_id", contact_id);
    	   values.put(Data.MIMETYPE, "vnd.android.cursor.item/name");
    	   // values.put("data2", "zs001");
    	   values.put("data1", name);
    	   resolver.insert(uri, values);
    	   values.clear();
    	  }
    	  // add Phone
    	  for (int i = 0; i < phoneCount; ++i){
    	  String phone = editPhone[i].getText().toString();
    	  if (phone.length() > 0) {
    	   values.put("raw_contact_id", contact_id);
    	   values.put(Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
      	   values.put("data1", phone);
    	   resolver.insert(uri, values);
    	   values.clear();
    	  }
    	  }
    	  // add email

    	  if (email.length() > 0) {
    	   values.put("raw_contact_id", contact_id);
    	   values.put(Data.MIMETYPE, "vnd.android.cursor.item/email_v2");
    	   // values.put("data2", "2");
    	   values.put("data1", email);
    	   resolver.insert(uri, values);
    	   values.clear();
    	  }    	
    }


  //  ==================查询所有数据（根据名字查看是否存在此联系人）======================== 
    public String isSame(String name) {
    	
    	DatabaseHelper dbHelper1 = new DatabaseHelper(AddContacter.this,"test_mars_db");
		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
		Cursor cursor1 = db1.query("user", new String[]{"name"}, "name=?", new String[]{name}, null, null, null);
       
	    while(cursor1.moveToNext()){
			String name1 = cursor1.getString(cursor1.getColumnIndex("name"));
            if(name.equals(name1)) {      
                if (db1 != null && db1.isOpen()) {
                    db1.close();
                }

               if (dbHelper1 != null) {
                 dbHelper1.close();
               }
            	return name1;
            }
		}	     	   	
	    
	    db1.close();
	    if (db1 != null && db1.isOpen()) {
            db1.close();
        }

       if (dbHelper1 != null) {
         dbHelper1.close();
       }
    	return "null";
    } 
    
    

}
