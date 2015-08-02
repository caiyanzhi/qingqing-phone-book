package com.example.qingqing701;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.example.contactersss.R;
import android.R.color;
import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.provider.MediaStore;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
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

public class EditContacter extends Activity{
	private Button back,save,addStar,addPhone,addlabelBtn;;
	private ImageButton editPhoto;
	private EditText editName, editEmail;
	private TextView label;
	String addedlabels="";
    private static final int PHOTO_REQUEST_TAKEPHOTO = 1;// 拍照
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private static final int PHONE_NUMBER = 100;
    
    private EditText editPhone[] = new EditText[PHONE_NUMBER ];
    private Button deletePhone[] = new Button[PHONE_NUMBER];
    private View phoneList[] = new View[PHONE_NUMBER ];

  //  private Vector<EditText> editPhone = new Vector<EditText>(PHONE_NUMBER);
  //  private Vector<Button> deletePhone = new Vector<Button>(PHONE_NUMBER);
 //   private Vector<View> phoneList = new Vector<View>(PHONE_NUMBER);
    private LinearLayout phoneset ;

    private int count = 0;//一共显示的电话框（原来的+添加的）
    
    // 创建一个以当前时间为名称的文件
    File tempFile = new File(Environment.getDownloadCacheDirectory(),getPhotoFileName());
    String PhotoUri,userName,phone,mail;
	ImageButton addImage;
	String[] phoneStr = new String[PHONE_NUMBER];

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editlayout);
		initView();
	}
	
	@SuppressWarnings("deprecation")
	private void initView() {
		// TODO Auto-generated method stub
		label = (TextView)findViewById(R.id.label);
		label.setFocusableInTouchMode(true);
		label.requestFocus();
		addlabelBtn = (Button)findViewById(R.id.addlabelBtn);
		addlabelBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(EditContacter.this,AddLabel.class);
				intent.putExtra("name", editName.getText().toString());
				intent.putExtra("is", "true");
				intent.putExtra("label", addedlabels);
				startActivityForResult(intent, 9);
			}
		});
		
		back = (Button)findViewById(R.id.back);

		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 finish();
				Intent mIntent = new Intent(EditContacter.this,CheckContacter.class);
				mIntent.putExtra("name", userName);
				EditContacter.this.startActivity(mIntent);				//*****************************************************要改成跳转到check页面************************************
			}
		});
		
		save = (Button)findViewById(R.id.save);
		save.setOnClickListener(new MyBtnOnClickListener());			
		//获取传过来的name
		userName  = (String)getIntent().getSerializableExtra("name");
		editName = (EditText) findViewById(R.id.name);
		ringName = (String)getIntent().getSerializableExtra("ringURI");
		addedlabels = (String)getIntent().getSerializableExtra("label");
		label.setText(addedlabels);
		editName.setText(userName);
		
		//头像
		editPhoto = (ImageButton) findViewById(R.id.addImage);
	    PhotoUri = (String)getIntent().getSerializableExtra("photoURI");
	    System.out.println("get PhotoUri " + PhotoUri);

	    if (PhotoUri.equals("null") == false){
	    	//editPhoto.setImageURI(Uri.parse(PhotoUri));
	
	    	Bitmap photo_bitmap = null ;
	    	photo_bitmap = BitmapFactory.decodeFile(PhotoUri);
			
	    	BitmapDrawable photo_bd = new BitmapDrawable(getResources(),photo_bitmap);
	    	editPhoto.setBackgroundDrawable(photo_bd);
	    }
	    
	    //编辑头像
		editPhoto.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ImgBtnshowDialog();
				}
			});
		
		
		phoneset = (LinearLayout) findViewById(R.id.phoneset);
		addPhone = (Button) findViewById(R.id.add_phone_btn);
		
		phone = (String)getIntent().getSerializableExtra("phone");
		ringName = (String)getIntent().getSerializableExtra("ringURI");
		getPhones(phone);
		showPhones(phoneStr);		
		
		//新添加的电话框
		addPhone.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				View t1 = LayoutInflater.from(EditContacter.this).inflate(R.layout.phonesetlayout, null);
				phoneList[count] = t1;			
				phoneset.addView(t1);
				editPhone[count] = (EditText)t1.findViewById(R.id.phone); 
				deletePhone[count] = (Button)t1.findViewById(R.id.delete_phone_btn) ;
				deletePhone[count].setOnClickListener(new deleteBtn(count));
				//System.out.println(String.valueOf(phoneCount));
				count ++;
				}
		});		
		
		//邮件
		editEmail = (EditText) findViewById(R.id.mail);
		mail = (String) getIntent().getSerializableExtra("mail");
		if ( mail.equals("@") == false)
			editEmail.setText(mail);		
 		
		Ring();
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

			for (int i = index; i < count-1; ++i)
				editPhone[i] =editPhone[i+1];
			if (count>0) count --;
		}
	}
     
	 Button add_ring;//铃声的按钮
	 Uri ring_uri; //铃声的uri
	 TextView ring_name;//铃声名
	 String ringName="默认铃声";
	 //初始化铃声
	 public void Ring(){
		 
		 ring_uri = Uri.parse("isNullcontext://");
		 ring_name  = (TextView)findViewById(R.id.ring_name);
		 ring_name.setText(ringName);
		 add_ring = (Button)findViewById(R.id.add_ring);
		 add_ring.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//Toast.makeText(EditContacter.this, "rui", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent( RingtoneManager.ACTION_RINGTONE_PICKER);
		        /*設定顯示鈴聲的資料夾*/
				
		        intent.putExtra( RingtoneManager.EXTRA_RINGTONE_TYPE,RingtoneManager.TYPE_RINGTONE);
		        /*設定顯示鈴聲開頭*/
		        intent.putExtra( RingtoneManager.EXTRA_RINGTONE_TITLE,"設定鈴聲");
		        
				startActivityForResult(intent,0);
			}
		});
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
    
    //更改铃声
    case 0:
        if(data!=null)
        {
        	RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM); 
            ring_uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        	//RingtoneManager.getActualDefaultRingtoneUri(AddClock.this, RingtoneManager.TYPE_ALARM);
       
        if(ring_uri!=null){
        Cursor cursor = getContentResolver().query(ring_uri, null, null, null, null);
        if(cursor.moveToFirst()){        
        ringName = cursor.getString(2);
        ring_name.setText(ringName);
        }
        else 
        	Toast.makeText(EditContacter.this, "获取文件出错", Toast.LENGTH_SHORT);
        }
        
        //Log.i("*", "" + ring_uri + "****"+ringName);
        }
        break;
        
    case 9:
    	String tmp = data.getCharSequenceExtra("label").toString();
    	addedlabels = tmp;
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
    if(!photo.equals(null))
    {
    	
    	PhotoUri = getFilesDir().getAbsolutePath() + "/ContacterImage/" + getPhotoFileName();
    	File file = new File(PhotoUri);
    	try {
			file.createNewFile();
			
			OutputStream output = new BufferedOutputStream(new FileOutputStream(file));
			photo.compress(Bitmap.CompressFormat.JPEG, 50, output);
			output.flush();
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			PhotoUri = "null";
			e.printStackTrace();
			Toast.makeText(EditContacter.this, "存储图片出错", Toast.LENGTH_SHORT).show();
		}
    	
    	Drawable drawable = new BitmapDrawable(photo);
    	editPhoto.setBackgroundDrawable(drawable);
    }
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
        for (int i = 0; i < count; ++i){
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
		String mail = editEmail.getText().toString();
		String name = editName.getText().toString();
		
			//判断联系人是否可以添加（如：姓名不能为空、电话要有效）
		 if(name.equals(""))    //没有输入名字不能添加
			  Toast.makeText(EditContacter.this, "请输入联系人姓名", 1).show();     
		  else if((mail.contains("@") == false || (mail.contains("@") == true && mail.length() == 1))&&mail.length()!=0)
			  Toast.makeText(EditContacter.this, "请输入有效邮箱地址", 1).show();	
		  else {
			  	delContact(EditContacter.this, userName);//将本地通讯录的之前的名字的联系人删除
			    
			    AddContacts();//添加到本地通讯录
			    insert_Contant();	  //将联系人信息保存到我们数据库,并且与手机通讯录同步
			    
			    SaveRingUri();
			    Toast.makeText(EditContacter.this, "编辑成功", 1).show();
			    finish();
			    //*********************看看有没有更高效的办法***********************************************
			    Intent intent = new Intent(EditContacter.this,CheckContacter.class);
			    intent.putExtra("name", editName.getText().toString());			    
			    startActivity(intent);
		  }
	     }
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
             EditContacter.this.getContentResolver().update(ContactsContract.Contacts.CONTENT_URI, values, ContactsContract.Contacts.DISPLAY_NAME + "=?", new String[] { editName.getText().toString() });
             //Toast.makeText(EditContacter.this,cursor.getString(cursor.getColumnIndex("ContactsContract.Contacts.DISPLAY_NAME")) ,Toast.LENGTH_SHORT).show();
             //System.out.println(cursor.getString(cursor.getColumnIndex("ContactsContract.Contacts.CUSTOM_RINGTONE")));
	      }
       //Toast.makeText(EditContacter.this,ring_uri.toString()+"设置铃声成功"+cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)), Toast.LENGTH_SHORT).show();
	 }
	 
    public void insert_Contant() {
   
      	 //---------------插入新数据-----------------
    	//生成ContentValues对象
		ContentValues values = new ContentValues();
		//想该对象当中插入键值对，其中键是列名，值是希望插入到这一列的值，值必须和数据库当中的数据类型一致\
		String nowname = editName.getText().toString();
		values.put("photoURI",PhotoUri);
		values.put("name",nowname);
		values.put("phone", getPhoneStr());
		values.put("groupp",addedlabels);
		
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
	      
		values.put("PY",mPY);
		//修改BY XL！！！
		if (editEmail.getText().toString().equals("") == false)
			values.put("mail",editEmail.getText().toString());
		else 
			values.put("mail", "@");
		values.put("ringURI",ringName);
		//群组选择功能目前未实现
		values.put("star", "0");

		DatabaseHelper dbHelper = new DatabaseHelper(EditContacter.this,"test_mars_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
	 	
		//****************************************************************************************************
		//****************************************************************************************************
		//把数据库原来这行删掉
    	db.delete("user", "name=?", new String[]{userName});
		
		//调用insert方法，就可以将数据插入到数据库当中
		db.insert("user", null, values);     
	    if (db != null && db.isOpen()) {
            db.close();
        }

       if (dbHelper != null) {
         dbHelper.close();
       }
    }
    
    
    //删除本地通讯录的联系人
    private void delContact(Context context, String name) {    	 
        //跳转到所查询的name的光标处
    		Cursor cursor = getContentResolver().query(Data.CONTENT_URI,new String[] { 
    			Data.RAW_CONTACT_ID },ContactsContract.Contacts.DISPLAY_NAME + "=?",
    			new String[] { name }, null); 
    		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();    	 
    		if (cursor.moveToFirst()) 
    		{    	 
    			do {    	 
    				long Id = cursor.getLong(cursor.getColumnIndex(Data.RAW_CONTACT_ID));  	 
    				ops.add(ContentProviderOperation.newDelete(    	 
    						ContentUris.withAppendedId(RawContacts.CONTENT_URI,Id)).build());    	 
    				
    				try {
    					getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);    	 
    				}	 
    				catch (Exception e){}    	 
    			}
    			
    			while (cursor.moveToNext());    	 
    			cursor.close();
    		}    	 
    	}
    
    //添加联系人到手机
    public void AddContacts(){ 
    	String name = editName.getText().toString();
	    String email = editEmail.getText().toString();
    	
    	 	// 插入raw_contacts表，并获取_id属性
    	Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
    	  
    	ContentResolver resolver = EditContacter.this.getContentResolver();
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
    	
    	if(!PhotoUri.equals("null"))
		{
		  	Bitmap bitmap = BitmapFactory.decodeFile(PhotoUri);
		  	
			final ByteArrayOutputStream os = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            byte[] avatar = os.toByteArray();
            values.put(Data.RAW_CONTACT_ID, contact_id);
            values.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
            values.put(Photo.PHOTO, avatar); 
        	resolver.insert(uri, values);
			values.clear();
		}
    	  // add Phone
    	for (int i = 0; i < count; ++i){
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

 
    
    //将电话号码逐个拆分出来，存入phoneStr[]
    public void getPhones(String phone){
    	int index[] = new int[20];

    	//若phone为空，则必须返回，否则会溢出；
    	if(phone.length()==0)
        	return;
    	phone+="*";
    	int indexcount = 0;
    	//System.out.println("Phone: " + phone);
    	for (int i = 0 ; i < phone.length() ; ++i){
    		String t = phone.substring(i, i+1);
    		if (phone.charAt(i) == '*'){
    			index[indexcount] = i;
    			indexcount ++; 
    		}
    	}
    	
    	for (int i = 0 ; i < indexcount - 1; ++i){
    		int temp = index[i] + 1;
    		phoneStr[count] = phone.substring(temp, index[i+1]);    		
    		count++;
    	}
    	//System.out.println(String.valueOf(count));
    	//phoneStr[count] = phone.substring(index[indexcount - 1]+1);
    	//count++;
    }
 
    //显示多个电话
    public void showPhones(String[] phoneStr){    

    	for (int phoneCount = 0; phoneCount < count; ++phoneCount){
    		//java.lang.System.out.println(" " + phoneCount);
			phoneList[phoneCount] = LayoutInflater.from(EditContacter.this).inflate(R.layout.phonesetlayout, null) ;		
			phoneset.addView(phoneList[phoneCount]);
			editPhone[phoneCount] = (EditText)phoneList[phoneCount].findViewById(R.id.phone); 			
			editPhone[phoneCount].setText(phoneStr[phoneCount]);	
			deletePhone[phoneCount] = (Button) phoneList[phoneCount].findViewById(R.id.delete_phone_btn);
			deletePhone[phoneCount].setOnClickListener(new deleteBtn(phoneCount));
			
			/*View t1 = LayoutInflater.from(EditContacter.this).inflate(R.layout.phonesetlayout, null);
			//phoneList[phoneCount] = LayoutInflater.from(AddContacter.this).inflate(R.layout.phonesetlayout, null);
			phoneList.add(t1);
			phoneset.addView(t1);
			EditText temp = (EditText)t1.findViewById(R.id.phone); 	
			editPhone.add(temp);
			editPhone.get(editPhone.size()-1).setText(phoneStr[phoneCount]);
			deletePhone.add( (Button)t1.findViewById(R.id.delete_phone_btn) );
			deletePhone.get(phoneCount).setOnClickListener(new deleteBtn(phoneCount));
    	*/
    	}
    } 
    
    @Override
	 public void onBackPressed() {
	 // TODO Auto-generated method stub
    	finish();
		Intent mIntent = new Intent(EditContacter.this,CheckContacter.class);
		mIntent.putExtra("name", userName);
		EditContacter.this.startActivity(mIntent);
	    super.onBackPressed(); 
	}

}
