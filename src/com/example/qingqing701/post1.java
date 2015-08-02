package com.example.qingqing701;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;

public class post1 {
	
	
	static public String post(String pathToOurFile,String urlServer) throws ClientProtocolException, IOException, JSONException {
	      HttpClient httpclient = new DefaultHttpClient();
	      //设置通信协议版本
	      httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
	       
	      //File path= Environment.getExternalStorageDirectory(); //取得SD卡的路径
	       
	      //String pathToOurFile = path.getPath()+File.separator+"ak.txt"; //uploadfile
	      //String urlServer = "http://192.168.1.88/test/upload.php"; 
	       
	      HttpPost httppost = new HttpPost(urlServer);
	      File file = new File(pathToOurFile);
	     
	      MultipartEntity mpEntity = new MultipartEntity(); //文件传输
	      ContentBody cbFile = new FileBody(file);

	      mpEntity.addPart("file", cbFile); // <input type="file" name="userfile" />  对应的

	      
	      httppost.setEntity(mpEntity);
	      System.out.println("executing request " + httppost.getRequestLine());
	       
	      HttpResponse response = httpclient.execute(httppost);
	      
	      HttpEntity resEntity = response.getEntity();
	   
	      System.out.println(response.getStatusLine());//通信Ok
	      String json="";
	      String path="";
	      if (resEntity != null) {
	        //System.out.println(EntityUtils.toString(resEntity,"utf-8"));
	        json=EntityUtils.toString(resEntity,"utf-8");
	        JSONObject p=null;
	        try{
	            p=new JSONObject(json);
	            path=(String) p.get("path");
	        }catch(Exception e){
	            e.printStackTrace();
	        }
	      }
	      if (resEntity != null) {
	        resEntity.consumeContent();
	      }
	      
	      httpclient.getConnectionManager().shutdown();
	      
	     
	      return path;
	    }
}
