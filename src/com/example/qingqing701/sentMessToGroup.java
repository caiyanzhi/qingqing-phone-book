package com.example.qingqing701;

import java.util.ArrayList;
import java.util.List;

import com.example.contactersss.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class sentMessToGroup extends Activity{

	private EditText contacts,textContent;
	private Button sent, cancel;
	String content = "",contactsStr="";
	ArrayList<String> phones,names;
	@Override
	protected void onCreate(Bundle save){
		super.onCreate(save);
		setContentView(R.layout.sentmesstosome);
		initView();
	}
	
	private void initView(){
		
		phones = new ArrayList<String>();
		names = new ArrayList<String>();
		phones = getIntent().getStringArrayListExtra("phone");
		names = getIntent().getStringArrayListExtra("name");
		contacts = (EditText)findViewById(R.id.contacts);
		
		textContent = (EditText)findViewById(R.id.textContent);
		
		initContactsStr();
		contacts.setText(contactsStr);
		textContent.setText(content);
		
		cancel = (Button)findViewById(R.id.cancel);
		sent = (Button)findViewById(R.id.sent);
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		sent.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				content = textContent.getText().toString();
				contactsStr = contacts.getText().toString();
				phones = getPhones(contactsStr);
				int size = phones.size();
				if(size==0)
				{
					Toast.makeText(sentMessToGroup.this, "请输入联系人的手机",Toast.LENGTH_SHORT).show();
				    return;
				}
				else if(content.equals(""))
				{
					Toast.makeText(sentMessToGroup.this, "不能发送空短信啊>-<",Toast.LENGTH_SHORT).show();
				    return;
				}
				String SENT_SMS_ACTION = "SENT_SMS_ACTION";
				Intent sentIntent = new Intent(SENT_SMS_ACTION);
				PendingIntent sentPI = PendingIntent.getBroadcast(sentMessToGroup.this, 0, sentIntent,
				        0);
			
				String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
				// create the deilverIntent parameter
				Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
				PendingIntent deliverPI = PendingIntent.getBroadcast(sentMessToGroup.this, 0,
				       deliverIntent, 0);
				
				SmsManager smsManager = SmsManager.getDefault();
                List<String> divideContents = smsManager.divideMessage(content);
                int count = 0;
				for(int i=0;i<size;i++){
					//System.out.println(phones.get(i));
					insert_messInfo(phones.get(i),content);
				    for (String text : divideContents) 
				    {
				    	if(phones.get(i).equals("")==false)
				    	{
				    		count++;
				            smsManager.sendTextMessage(phones.get(i), null, text, sentPI, deliverPI);
				    	}
				    }
				}
				// 注册广播 发送消息
				//registerReceiver(sendMessage, new IntentFilter(SENT_SMS_ACTION));
				//registerReceiver(receiver, new IntentFilter(DELIVERED_SMS_ACTION));
				Toast.makeText(sentMessToGroup.this,
				        "短信发送成功,共计"+count+"条", Toast.LENGTH_SHORT)
				        .show();
				//sentMessToGroup.this.unregisterReceiver(receiver);
			}
			

			private ArrayList<String> getPhones(String contactsStr) {
				// TODO Auto-generated method stub
				ArrayList<String> list = new ArrayList<String>();
				if(contactsStr.length()==0)
					return list;
				int count[] = new int[20];
				int counts = 0;
				contactsStr=";"+contactsStr+";";
				for(int i=0;i<contactsStr.length();i++){
					if(contactsStr.charAt(i)==';')
						count[counts++] = i;
				}
				for(int i=0;i<counts-1;i++){
					int tmp = count[i]+1;
					list.add(contactsStr.substring(tmp, count[i+1]));
				}
				return list;
			}
		});
	}

	private BroadcastReceiver sendMessage = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
		    //判断短信是否发送成功
		    switch (getResultCode()) {
		    case Activity.RESULT_OK:
			Toast.makeText(sentMessToGroup.this, "短信发送成功", Toast.LENGTH_SHORT).show();
			break;
		    default:
			Toast.makeText(sentMessToGroup.this, "发送失败", Toast.LENGTH_LONG).show();
			break;
		    }
		}
	    };
	    
	   
	    private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
		    //表示对方成功收到短信
		    Toast.makeText(sentMessToGroup.this, "对方接收成功",Toast.LENGTH_LONG).show();
		}
	    };
	    
	private void insert_messInfo(String phone,String content){
	    /**将发送的短信插入数据库**/
	    ContentValues values = new ContentValues();
	    //发送时间
	    values.put("date", System.currentTimeMillis());
	    //阅读状态
	    values.put("read", 0);
	    //1为收 2为发
	    values.put("type", 2);
	    //送达号码
	    values.put("address", phone);
	    //送达内容
	    values.put("body", content);
	    //插入短信库
	    getContentResolver().insert(Uri.parse("content://sms"),values);

	}
	    
	private void initContactsStr() {
		// TODO Auto-generated method stub
	     int size = phones.size();
	     for(int i=0;i<size;i++){
	    	 String tmp = phones.get(i);
	    	if(tmp.equals(""))
	    		continue;
	    	
	    	 for(int k=1;k<tmp.length();k++)
	    	 {   
	    		 if(tmp.charAt(k)!='*')
	    		 contactsStr+=tmp.charAt(k);
	    		 else
	    			 contactsStr+=";";
	    	     if(k==tmp.length()-1&&i!=size-1)
	    		 contactsStr+=';';
	    	 }
	     }
	}
}
