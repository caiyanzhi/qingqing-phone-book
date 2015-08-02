package com.example.qingqing701;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.provider.ContactsContract.CommonDataKinds.Photo;
import android.util.Log;

public class download {  
	
	/*
	 * ���ܣ� ��urlStr���ر����ļ������ָ�ͨѶ¼
	 * 
	 */
    static public void downloadfile(final Context context, final String filename, final String url,final int type) {  
    	
        new Thread(new Runnable() {        
            @Override  
            public void run() {  

        		String line = null;
        		String urlStr = url + '/' + filename;
        		if(type == 0)
        			urlStr = urlStr + ".zip";
        		
        		BufferedReader buffer = null;
        		try {	
        			URL url = new URL(urlStr);
        			HttpURLConnection urlConn = (HttpURLConnection)url.openConnection();
        			
        			if(urlConn.getResponseCode() != 200)
        			{
        				Message mess = new Message();
        				mess.what = 6;
        				mess.obj = "���Ӵ���";
        				MainActivity.login_handler.sendMessage(mess);
        			}
        			if(urlConn.getResponseCode() == 200 && type == 0)
        			{
        				InputStream input = urlConn.getInputStream();
        				buffer = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
        				String zipFileName = context.getExternalCacheDir()+"/" +filename+".zip";
        				Log.d("zip file name", zipFileName);
        				File zipFile = new File(zipFileName);
    					zipFile.createNewFile();
        				OutputStream output = new BufferedOutputStream(new FileOutputStream(zipFile)); 
        				byte[] b = new byte[1024 * 5];   
                        int len;   
                        while ( (len = input.read(b)) != -1) {   
                            output.write(b, 0, len);   
                        }  
        				output.flush();
        				
        				output.close();
        				input.close();
        				Log.d("aim file path", context.getFilesDir().getAbsolutePath());
        				ZipUtil.unZipFolder(zipFileName, context.getFilesDir().getAbsolutePath());
        				Message msg = new Message();
        				msg.what = 9;
           				MainActivity.login_handler.sendMessage(msg);
        			}
        			else if(urlConn.getResponseCode() == 200 && type == 1)
        			{
        				InputStream input = urlConn.getInputStream();
        				
        				File file = new File(context.getExternalCacheDir()+"/" +filename+".txt");
        				file.createNewFile();
        				int BUFFER = 1024*4;
                        FileOutputStream fos = new FileOutputStream(file);
                        BufferedOutputStream bos = new BufferedOutputStream(fos,BUFFER);           
                        
                        byte data[] = new byte[BUFFER];
                        int count = -1;
                        while ((count = input.read(data, 0, BUFFER)) != -1)
                        {
                            bos.write(data, 0, count);
                        }
                        
                        bos.flush();
                        bos.close();
                        
            		    	//���²��õ�����������ֻ�ͨѶ¼���ݿ�,�ٶȡ�������Ǻǡ���
            		    /*	Uri uri = Uri.parse("content://com.android.contacts/raw_contacts");
            		    	  
            		    	ContentResolver resolver = context.getContentResolver();
            		    	ContentValues values1 = new ContentValues();
            		    	long contact_id = ContentUris.parseId(resolver.insert(uri, values1));
            		    	  // ����data��
            		    	uri = Uri.parse("content://com.android.contacts/data");
            		    	  // add Name
            		          
            		    	if (name.length() > 0) {
            		    		values1.put("raw_contact_id", contact_id);
            		    	   
            		    		values1.put(Data.MIMETYPE, "vnd.android.cursor.item/name");
            		    		// values.put("data2", "zs001");
            		    		values1.put("data1", name);
            		    		resolver.insert(uri, values1);
            		    		values1.clear();
            		    	}
            		    	
            		    	// add Phone Uri
            		    	if(!strs[3].equals("null"))
            				{
            				  	Bitmap bitmap = BitmapFactory.decodeFile(strs[3]);
            				  	
            					final ByteArrayOutputStream os = new ByteArrayOutputStream();
            					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            		            byte[] avatar = os.toByteArray();
            		            values1.put(Data.RAW_CONTACT_ID, contact_id);
            		            values1.put(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE);
            		            values1.put(Photo.PHOTO, avatar); 
            		        	resolver.insert(uri, values1);
            					values1.clear();
            				}
            		     	  // add Phone
            		    	index = 1;
            		    	if(phone.length() != 0)
            		    	{
            		    		phone+="*";
                		    	for (int i = 1 ; i < phone.length() ; ++i){
                		    		
                		    		if (phone.charAt(i) == '*'){
                		    			String subPhone = phone.substring(index, i);
                		    			if (subPhone.length() > 0) {
                    		    			values1.put("raw_contact_id", contact_id);
                    		    			values1.put(Data.MIMETYPE, "vnd.android.cursor.item/phone_v2");
                    		    			values1.put("data1", subPhone);
                    		    			resolver.insert(uri, values1);
                    		    			values1.clear();
                    		    		}
                		    			index = i+1;
                		    		}
                		    	}
                		    	
                		    	
            		    	}
            		    	
            		    	  // add email

            		    	if (email.length() > 0) {
            		    		values1.put("raw_contact_id", contact_id);
            		    		values1.put(Data.MIMETYPE, "vnd.android.cursor.item/email_v2");
            		    		// values1.put("data2", "2");
            		    		values1.put("data1", email);
            		    		resolver.insert(uri, values1);
            		    		values1.clear();
            		    	}  
            		    	*/
        			}
        		} 
        		catch (Exception e) {
        			e.printStackTrace();
        		}
            }
            
        }).start();  
    }   
    
    //ɾ�����б���ͨѶ¼����ϵ��
    static private void delAllContacters(Context context) 
    {  
    	
    	context.getContentResolver().delete(RawContacts.CONTENT_URI.buildUpon().appendQueryParameter(ContactsContract.CALLER_IS_SYNCADAPTER,"true").build(), null, null);
        Log.d("delete", "contacters");
    	//��ת������ѯ��name�Ĺ�괦
    	/*	Cursor cursor = context.getContentResolver().query(Data.CONTENT_URI,new String[] { Data.RAW_CONTACT_ID },null,null, null); 
    			ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();    	  
    				
    			if(cursor.equals(null) == false)
    				while (cursor.moveToNext())
    				{ 	 
    					long Id = cursor.getLong(cursor.getColumnIndex(Data.RAW_CONTACT_ID));  	 
    					ops.add(ContentProviderOperation.newDelete(ContentUris.withAppendedId(RawContacts.CONTENT_URI,Id)).build());    	 
    				
    					try {
    						context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);    	 
    					}	 
    					catch (Exception e){
    						continue;
    					}    	 
    				}
    			
    			cursor.close();	 
    			*/
    }
    
    static public void syncContacters(Context context, String filename)
    {
    	try
    	{

			DatabaseHelper dbHelper1 = new DatabaseHelper(context,"test_mars_db");
    		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
    		HistoryLabel dbHelper2 = new HistoryLabel(context, "test_mars_db1");
    		SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
    		
    		//ɾ��user �� historyLabel ��
    		db1.delete("user", null, null);
    		db2.delete("historyLabel",null,null);
    		delAllContacters(context);
    		Log.d("begin to decode", "begin");
    		File f = new File(context.getExternalCacheDir()+"/" +filename+".txt");
    		InputStream inputfile = new FileInputStream(f);
    		
    		BufferedReader buffer = new BufferedReader(new InputStreamReader(inputfile));
    		
    		//������ӱ��ص��ֻ�ͨѶ¼�Ĳ�������
        	ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
            int rawContactInsertIndex = 0;
            String line;
    		while( (line = buffer.readLine()) != null)
    		{
    			ContentValues values = new ContentValues();
    			//��ö����в����ֵ�ԣ����м���������ֵ��ϣ�����뵽��һ�е�ֵ��ֵ��������ݿ⵱�е���������һ��
    			//Log.d("", line);
    			String strs[] = new String[8];
    			int size = line.length();
    			int index = 0;
    			int start = 0;
    			for(int i = 0; i < size; i++)
    			{
    				char ch = line.charAt(i);
    				if(ch == ',')
    				{
    					if(start == (i))
    						strs[index++] = "";
    					else
    						strs[index++] = line.substring(start,i);
    					
    					start = i+1;
    					if(index == 6)
    					{
    						if(start == (size))
    							strs[index++] = "";
    						else
    							strs[index++] = line.substring(start,size);
    					}
    				}
    			}
    			
    			if(!strs[0].equals(null))
    				values.put("name",strs[0]);
    			else
    				values.put("name", "null");
    			
    			
    			if(!strs[1].equals(null))
    				values.put("mail",strs[1]);
    			else
    				values.put("mail", "null");

    			if(!strs[2].equals(null))
    				values.put("phone",strs[2]);
    			else
    				values.put("phone", "null");
    			
    			if(!strs[3].equals(null))
    				values.put("photoURI",strs[3]);
    			else
    				values.put("photoURI","null");
    			
    			if(!strs[4].equals(null))
    				values.put("PY",strs[4]);
    			else
    				values.put("PY", "null");
    			

    			if(!strs[5].equals(null))
    			{
    				values.put("groupp",strs[5]);
    				ArrayList<String> labelList = getLabels(strs[5]);
    				for(String item : labelList){
    					if(!HistoryLabel.isLabelExist(context,item))
        				{
        					ContentValues values2 = new ContentValues();
        		            values2.put("label",item);
        		 
        		            db2.insert("historyLabel", null, values2);
        				}
    				}
    			}
    			else
    				values.put("groupp", "");
    			
    			if(!strs[6].equals(null))
    				values.put("star",strs[6]);
    			else
    				values.put("star", "");		
    			
    			values.put("ringURI","");
    	        
    	        db1.insert("user",null,values);//�޸ı������ݿ�
    	        
    	        String name = strs[0];
    		    String email = strs[1];
    	    	String phone = strs[2];
    	    	
    	    	rawContactInsertIndex = ops.size();//������id
       		 
    	    	ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
                        .withValue(RawContacts.ACCOUNT_TYPE, null)
                        .withValue(RawContacts.ACCOUNT_NAME, null)
                        .withYieldAllowed(true).build());
                // �������
                ops.add(ContentProviderOperation
                        .newInsert(
                                android.provider.ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(Data.RAW_CONTACT_ID,
                                rawContactInsertIndex)
                        .withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
                        .withValue(StructuredName.DISPLAY_NAME, name)
                        .withYieldAllowed(true).build());
                // ��Ӻ���
                index = 1;
    	    	if(phone.length() != 0)
    	    	{
    	    		phone+="*";
    		    	for (int i = 1 ; i < phone.length() ; ++i){
    		    		
    		    		if (phone.charAt(i) == '*'){
    		    			String subPhone = phone.substring(index, i);
    		    			if (subPhone.length() > 0) {
    		    				ops.add(ContentProviderOperation
            		                    .newInsert(
            		                            android.provider.ContactsContract.Data.CONTENT_URI)
            		                    .withValueBackReference(Data.RAW_CONTACT_ID,
            		                            rawContactInsertIndex)
            		                    .withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
            		                    .withValue(Phone.NUMBER, subPhone)
            		                    .withValue(Phone.TYPE, Phone.TYPE_MOBILE)
            		                    .withValue(Phone.LABEL, "").withYieldAllowed(true).build());
        		    		}
    		    			index = i+1;
    		    		}
    		    	}
    	    	}
                
    	    	// add head photo
    	    	if(!strs[3].equals("null"))
    	    	try
    	    	{
    	    		Bitmap bitmap = BitmapFactory.decodeFile(strs[3]);
    	    		final ByteArrayOutputStream os = new ByteArrayOutputStream();
    				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
    	            byte[] avatar = os.toByteArray();
    	            ops.add(ContentProviderOperation
    	                    .newInsert(
    	                            android.provider.ContactsContract.Data.CONTENT_URI)
    	                    .withValueBackReference(Data.RAW_CONTACT_ID,
    	                            rawContactInsertIndex)
    	                    .withValue(Data.MIMETYPE, Photo.CONTENT_ITEM_TYPE)
    	                    .withValue(Photo.PHOTO, avatar).withYieldAllowed(true).build());
    	    	}
    	    	catch (Exception e) {
        			e.printStackTrace();
    	    	}
    		
    	       
    	    	ops.add(ContentProviderOperation
                        .newInsert(
                                android.provider.ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(Data.RAW_CONTACT_ID,
                                rawContactInsertIndex)
                        .withValue(Data.MIMETYPE, "vnd.android.cursor.item/email_v2")
                        .withValue(Photo.DATA1,email).withYieldAllowed(true).build());
    		}
    		
    		if (ops != null) {
	            // �������
    		//	Log.d("","opssss");
	            ContentProviderResult[] results = context.getContentResolver()
	                    .applyBatch(ContactsContract.AUTHORITY, ops);
	             /*for (ContentProviderResult result : results) 
	            	 {
	            		 Log.d("contacter", result.toString());
	            	 }
	            	 */
	             Log.d("","opssss");
	        }
    		Message msg = new Message();
    		msg.what = 5;
    
			MainActivity.login_handler.sendMessage(msg);
			inputfile.close();
			if (db1 != null && db1.isOpen()) {
	            db1.close();
	        }

			if (dbHelper1 != null) {
				dbHelper1.close();
			}
    	}
    	catch (Exception e) {
			e.printStackTrace();
		}
    }
    static private ArrayList<String> getLabels(String tmplabels) {
		// TODO Auto-generated method stub
		int size = tmplabels.length();
		ArrayList<String> tmp = new ArrayList<String>();
		if(size==0)
			return tmp;
	     tmp = new ArrayList<String>();
		tmplabels=" "+tmplabels;
		tmplabels+=' ';
		
		size = tmplabels.length();
		
		ArrayList<Integer> index = new ArrayList<Integer>();
		
		for(int j = 0; j < size; j++){
			if(tmplabels.charAt(j) == ' ')
				index.add(j);
		}
		
		for(int i= 0;i<index.size()-1;i++){
			int temp = index.get(i)+1;
			tmp.add(tmplabels.substring(temp, index.get(i+1)));
		}
		return tmp;
	}
}  