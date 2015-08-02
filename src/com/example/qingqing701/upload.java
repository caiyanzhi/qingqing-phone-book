package com.example.qingqing701;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;

import android.content.Context;
import android.os.Message;
import android.util.Log;

public class upload {  

	static public String accessKey = "jxmojw3l3w";
	static public String secretKey = "m32yzymxxkkj5lk0m11hx4wwxjy44hkkx4yimwxh";
	static public String appName = "contactersss";
	

    static public void uploadfile(final Context context,final String filename,final int type) {  
        new Thread(new Runnable() {           
            @Override  
            
            
            public void run() {  
           
					try {
						if(type == 1)
						{
							post1.post(context.getFilesDir() + "/" + filename, "http://contactersss.sinaapp.com" + "/upload");
							Message mess = new Message();
					    	mess.what = 4;
					    	MainActivity.login_handler.sendMessage(mess);
						}
						else if(type == 0)
						{
							ZipUtil.zipFolder(context.getFilesDir() + "/ContacterImage",context.getExternalCacheDir()+"/" +filename+".zip");
							post1.post(context.getExternalCacheDir()+"/" +filename+".zip", "http://contactersss.sinaapp.com" + "/upload");
						}
						
					} catch (ClientProtocolException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				
            	
            }
                     
        }).start();  
        
        Log.d("","thread");
    }     

}  