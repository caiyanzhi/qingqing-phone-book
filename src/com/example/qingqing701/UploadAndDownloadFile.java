package com.example.qingqing701;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Message;
import android.util.Log;



public class UploadAndDownloadFile
{
	
	static class user_row
 	{
		public String name;
		public String photoURI;
		public String phone;
		public String mail;
		public String groupp;
		public String PY;
		public String star;
		public String label;
	}
	static Context mContext;
	static ArrayList<String> user_list = null;
	
	static public void login(final String user_name, final String password,final String opt, final String url, final boolean isUpload){
			final String message[] = {null};
			
			final String connectUrl = url + '/' + opt;
		new Thread(new Runnable(){
			@Override
			public void run(){
				String result = null; //����ȡ�÷��ص�String��

				//test
				System.out.println("username:"+user_name);
				System.out.println("password:"+password);
				System.out.println("operation"+opt);
				//����post����
				HttpPost httpRequest = new HttpPost(connectUrl);
				//Post�������ͱ���������NameValuePair[]���д���
				List<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("user",user_name));
				params.add(new BasicNameValuePair("password",password));
				//params.add(new BasicNameValuePair("message", message));
				
				try{
				//����HTTP����
				httpRequest.setEntity(new UrlEncodedFormEntity(params,HTTP.UTF_8));
				//ȡ��HTTP response
				HttpResponse httpResponse=new DefaultHttpClient().execute(httpRequest);
				//��״̬��Ϊ200������ɹ���ȡ����������
				if(httpResponse.getStatusLine().getStatusCode()==200){
				//ȡ���ַ���
				    result=EntityUtils.toString(httpResponse.getEntity(),"utf-8");
				    //mess.what = 0 upload/ 1 download for register 
				    if(opt.equals("register")){
				    	Message mess = new Message();
				    	if(isUpload)
				    		mess.what = 0;
				    	else
				    		mess.what = 1;
				    	
				    	mess.obj = result;
				    	MainActivity.login_handler.sendMessage(mess);				    					    	
				    }
				    //mess.what = 2 upload/ 3 download  for login
				    else if(opt.equals("login")){
				    	Message mess = new Message();
				    	if(isUpload)
				    		mess.what = 2;
				    	else
				    		mess.what = 3;
				    	mess.obj = result;
				    	MainActivity.login_handler.sendMessage(mess);
				    }
				    
				    System.out.println("result= "+result);
				}
				}catch(Exception e){
				e.printStackTrace();
				message[0] = "�������ӳ���";
				}
			}
		}).start();		
		Log.d("","threadconnect");
	}
	
	static public void saveAsLocalFile(Context context,String fileName)
	{		
		mContext = context;
		//Toast.makeText(context, "saving", Toast.LENGTH_LONG).show();
		
		DatabaseHelper dbHelper1 = new DatabaseHelper(context,"test_mars_db");
		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
		
		String sql = "select * from user";
		Cursor cursor = db1.rawQuery(sql,null);
		
		user_list = new ArrayList<String>();
		if(cursor.equals(null)==false)
	        while(cursor.moveToNext())
	        {
	        	user_row ur = new user_row();
	        	
	        	ur.name = cursor.getString(cursor.getColumnIndex("name"));
	        	ur.photoURI = cursor.getString(cursor.getColumnIndex("photoURI"));
	        	ur.mail = cursor.getString(cursor.getColumnIndex("mail"));
	        	ur.PY = cursor.getString(cursor.getColumnIndex("PY"));
	        	ur.groupp = cursor.getString(cursor.getColumnIndex("groupp"));
	        	ur.phone = cursor.getString(cursor.getColumnIndex("phone"));
	        	ur.star = cursor.getString(cursor.getColumnIndex("star"));
	        	String line = ur.name + "," + ur.mail + "," + ur.phone + "," + ur.photoURI + "," + ur.PY + "," + ur.groupp+"," + ur.star+"\n";
	        	
				Log.d("", line);
	        	user_list.add(line);
	        }
		
		cursor.close();
	 	if (db1 != null && db1.isOpen()) {
	             db1.close();
	    }
	 	
	 	//Toast.makeText(context, fileName, Toast.LENGTH_LONG).show();
		
	 	saveAsFile(user_list,fileName);
	}
	
	static public void saveAsFile(ArrayList<String> list, String fileName)
	{
	
		FileOutputStream output = null;
		try 
		{
			output = mContext.openFileOutput(fileName, mContext.MODE_PRIVATE);
			int count = list.size();
			//Log.d("","333save in " + fileName);
			for(int i = 0; i < count; i++)
			{
				String line = (String) list.get(i);
				byte [] b = line.getBytes("UTF-8"); 
				Log.d("", line);
				output.write(b);
			}
			
			Log.d("","222save in " + fileName);
		
			output.close();
		}
		catch (IOException e1) {
			   // TODO Auto-generated catch block
			   e1.printStackTrace();
		}finally{
			try{
				if( output != null ){
					output.close();
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	    
	}
	/*
	 * ���ܣ� ��ȡfilename�ļ����洢��ArrayList<user_row>����
	 * 
	 */
	static ArrayList<String> getUserListFromFile(Context context,String filename)
	{
		ArrayList<String>  list = new ArrayList<String>();
		
		File file = new File(context.getFilesDir()+ "/"+ filename);
		if(!file.exists())
		{
			return null;
		}
		InputStream inputStream = null;
		
		try
		{
			Log.d("",filename);
			inputStream = context.openFileInput(filename);
			if (inputStream != null) 
           	{
                InputStreamReader inputreader = new InputStreamReader(inputStream);
                BufferedReader buffreader = new BufferedReader(inputreader);
			
                String line;
                //���ж�ȡ
                while ((line = buffreader.readLine()) != null) {
               	     list.add(line);
                }
                
                Log.d("", "over");
            }               
            inputStream.close();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return list;
	}
	
	public static class FileService {
		 
		 /**
		  * ��������
		  * @param outStream
		  * @param content
		  * @throws Exception
		  */
	 		public static void save(OutputStream outStream, String content)
				 throws Exception {
	 			outStream.write(content.getBytes());
	 			outStream.close();
	 		}
		 /**
		  * ��ȡ����
		  * @param inStream
		  * @return ���
		  * @throws Exception
		  */
	 		public static String read(InputStream inStream) throws Exception {
			 	ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			 	byte[] buffer = new byte[1024];
			 	int len = -1;
			 	//=-1�Ѷ����ļ�ĩβ
			 	while ((len = inStream.read(buffer)) != -1) {
			 		outStream.write(buffer, 0, len);
			 	}
			 	byte[] data = outStream.toByteArray();  
			 	outStream.close();
			 	inStream.close();
			 	return new String (data);
		 	}
	 	}
}