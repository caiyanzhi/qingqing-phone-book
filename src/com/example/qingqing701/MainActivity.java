
package com.example.qingqing701;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import com.example.contactersss.R;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.PhoneLookup;
import android.provider.ContactsContract.RawContacts;
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
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	final private int LOVE = 1,ALL = 2,LABEL = 3,SOME = 4, SYNC = 5;
	//�ĸ�����ǩ�µĲ���,������Ҫ����mView[?]..
	private LinearLayout mLL;
	static Context mContext = null;
	private View mView[] = new View[5];
	
	//��ΪmView[?]�е�������mView[?]�Ĳ��ֶ�Ӧ
	private Some some = new Some();
	static private Sync syn = new Sync();
	private allContains all = new allContains();
	private LoveContains loveContains = new LoveContains();
	
	//������ǩ��ע��group������һ��
	private Button sync,group,love,allcontacter,label;

	//���ڱ���������ϵ���б�,���ղ��б�
	private ArrayList<HashMap<String, Object> > allList = null, loveList = null;
	
	//���ڴ��������ѡ�� ����������
	private ArrayList<Integer> selectedResult = null;
	
	//��ǩҳ�µĲ������
	ListView historyLabels;
	private ArrayList<String> labels;
	final private int MAXADDEDLABELS = 200; 
	private int labelsCount[] = new int[MAXADDEDLABELS];
	//private View [] hostoricLabelsList = new View[MAXADDEDLABELS];//���ڴ����ʷ��ǩ��itemView
	//private TextView [] textView2 = new TextView[MAXADDEDLABELS];//itemView�еĲ���
	//private TextView [] textView3 = new TextView[MAXADDEDLABELS];//itemView�еĲ���
	
	static public String url = "http://contactersss.sinaapp.com";
	
	static public LogInDialog.Builder login_builder = null;
	Bitmap photo = null;
	static public Handler login_handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			String message =null;
			if(msg.what == 2 || msg.what == 3){
				if(msg.obj.equals("0")){
					
					if(msg.what == 2)
					{
						UploadAndDownloadFile.saveAsLocalFile(mContext,login_builder.getUser().getText().toString());
				
						message = "��½�ɹ�,�����ϴ��У����Ժ�";
						Toast.makeText(mContext ,message, Toast.LENGTH_SHORT).show();
						login_builder.getDialog().dismiss();
						upload.uploadfile(mContext,login_builder.getUser().getText().toString(),1);
						upload.uploadfile(mContext,login_builder.getUser().getText().toString(),0);
					}
					else
					{
						message = "��½�ɹ�,���ڻָ��У����Ժ�";
						Toast.makeText(mContext ,message, Toast.LENGTH_SHORT).show();
						login_builder.getDialog().dismiss();
						download.downloadfile(mContext, login_builder.getUser().getText().toString(), url + '/' + "download",1);
						download.downloadfile(mContext, login_builder.getUser().getText().toString(), url + '/' + "download",0);
					}
				}
				if(msg.obj.equals("1")){
					message = "�������";
				}
				if(msg.obj.equals("2")){
					message = "����������";
				}
				if(msg.obj.equals("3")){
					message = "�û���������";
				}
				//Toast.makeText(mContext ,message, Toast.LENGTH_SHORT).show();
			}
			else if(msg.what == 0 || msg.what == 1){
				if(msg.obj.equals("0")){
					if(msg.what == 0)
					{
						UploadAndDownloadFile.saveAsLocalFile(mContext,login_builder.getUser().getText().toString());
						message = "ע��ɹ�,�����ϴ��У����Ժ�";
						Toast.makeText(mContext ,message, Toast.LENGTH_SHORT).show();
						login_builder.getDialog().dismiss();
						upload.uploadfile(mContext,login_builder.getUser().getText().toString(),1);
						upload.uploadfile(mContext,login_builder.getUser().getText().toString(),0);
					}
					else
					{
						message = "ע��ɹ�,���ڻָ��У����Ժ�";
						Toast.makeText(mContext ,message, Toast.LENGTH_SHORT).show();
						login_builder.getDialog().dismiss();
						download.downloadfile(mContext, login_builder.getUser().getText().toString(), url + '/' + "download",1);
						download.downloadfile(mContext, login_builder.getUser().getText().toString(), url + '/' + "download",0);
					}
				}
				if(msg.obj.equals("1")){
					message = "�û����Ѵ���";
				}
				if(msg.obj.equals("2")){
					message = "����������";
				}
				
				//Toast.makeText(mContext,message, Toast.LENGTH_SHORT).show();
			}
			else if(msg.what == 4)
			{
				message = "�ϴ��ɹ�";
				Toast.makeText(mContext,"�ϴ��ɹ�", Toast.LENGTH_SHORT).show();
			}
			else if(msg.what == 5)
			{
				message = "�ָ��ɹ�";
				Toast.makeText(mContext,"�ָ��ɹ�", Toast.LENGTH_SHORT).show();
			}
			else if(msg.what == 6)
			{
				message = (String)msg.obj;
				Toast.makeText(mContext,(String)msg.obj, Toast.LENGTH_SHORT).show();
			}
			else if(msg.what == 9)
			{
				download.syncContacters(mContext,login_builder.getUser().getText().toString());
			}
			syn.returnmessage.setText(message);
		}	
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.activity_main);
		//��ʼ�������ļ�
		initView();
		
		getContactInformation();
		
		setOnlistner();
		//�����ʵ�Ǵӱ�������������ǵ����ݿ�
        //�Ȳ�ѯ����list��������adapter��
        QueryAllList();
        if(allList.size()!=0)
			all.list.setAdapter(new MyListAdapter(MainActivity.this));
		else
			all.list.setAdapter(new simple(MainActivity.this,"���ͨѶ¼Ϊ��Ŷ���쵽�����Ӱ�ť����µ���ϵ�˰ɣ�"));

        mLL.addView(mView[1]);
	}
	
	private class simple extends BaseAdapter{

		Context mContext;
		String str;
		simple(Context context,String n){
			mContext = context;
            str = n;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 1;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			convertView = LayoutInflater.from(mContext).inflate(R.layout.simple, null);
			TextView text = (TextView)convertView.findViewById(R.id.text);
			text.setText(str);
			convertView.setTag(text);
			return convertView;
		}
		
	}
	private class allContains{
		Button addContacter; 
		Button searchImage;
		EditText searchText;
		TextView searchText2;
		ListView list;
	}
	
	private class Some{
		ListView selectToDelete;
		Button deleteSome,sentMessToSome;
	}
	
	private class LoveContains{
		ListView lovelistview;
	}

	static private class Sync{
		Button upload,download;
		TextView returnmessage;
	}
	
	private void initView() {

		selectedResult = new ArrayList<Integer >();
		allList = new ArrayList<HashMap<String,Object> >();
        loveList = new ArrayList<HashMap<String,Object>>();
		// TODO Auto-generated method stub
		mLL = (LinearLayout)findViewById(R.id.mLL);
	    
		label = (Button)findViewById(R.id.label);
		love =(Button)findViewById(R.id.love);
		group = (Button)findViewById(R.id.group);
		sync = (Button)findViewById(R.id.sync);
		allcontacter = (Button)findViewById(R.id.allContacters);
	
		//���������ĳ�ʼ����ɫ
	    allcontacter.setBackgroundResource(R.drawable.b9e086);
	    group.setBackgroundResource(R.drawable.a9777);
	    love.setBackgroundResource(R.drawable.a9777);
	    label.setBackgroundResource(R.drawable.a9777);
	    sync.setBackgroundResource(R.drawable.a9777);
	    
	    mView[0] = LayoutInflater.from(this).inflate(R.layout.operatorsome, null);
	    
	    some.deleteSome = (Button)mView[0].findViewById(R.id.deleteSome);
	    some.sentMessToSome = (Button)mView[0].findViewById(R.id.sentMessSome);
	    some.selectToDelete = (ListView)mView[0].findViewById(R.id.selectToDeleteListView);
	   // mView[0].setTag(some);
	    
        mView[1] = LayoutInflater.from(this).inflate(R.layout.alllist, null);
        all.list =(ListView)mView[1].findViewById(R.id.allListView);
      
		all.searchImage = (Button)mView[1].findViewById(R.id.searchImage);
		all.addContacter = (Button)mView[1].findViewById(R.id.addContacter);
		  all.searchText = (EditText)mView[1].findViewById(R.id.searchText);
		all.searchText2 = (TextView)mView[1].findViewById(R.id.searchText2);
		
		//mView[1].setTag(all);
		mView[2] = LayoutInflater.from(this).inflate(R.layout.love, null);
		loveContains.lovelistview =(ListView)mView[2].findViewById(R.id.loveListView);
		//mView[2].setTag(loveContains);
		mView[3] = LayoutInflater.from(this).inflate(R.layout.mainlabel, null);
		historyLabels = (ListView)mView[3].findViewById(R.id.lableList);
		//mView[3].setTag(historyLabels);
		
		mView[4] = LayoutInflater.from(this).inflate(R.layout.sync, null);
		
		syn.upload = (Button)mView[4].findViewById(R.id.upload);
	    syn.download = (Button)mView[4].findViewById(R.id.download);
	    syn.returnmessage = (TextView) mView[4].findViewById(R.id.returnmessage);
	}

	//��ʼ����ʷ��ǩ�Ĳ���
		private void initHistroyLabels() {
			// TODO Auto-generated method stub
			labels = HistoryLabel.QueryLabels(MainActivity.this);
			
			int size = labels.size();
		
			for(int i=0;i<size;i++){
				String tmp = labels.get(i);
				int count =  DatabaseHelper.QueryNum(MainActivity.this, tmp);
			    labelsCount[i] = count;
			}
			if(!historyLabels.equals(null))
				historyLabels.setAdapter(new LabelListAdapter(mContext));
		}
		
		 @Override
		    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		    // TODO Auto-generated method stub
			 //System.out.println("ALL");
		    switch (requestCode) {
		    case LABEL:
		    	mLL.removeAllViews();
		    	mLL.addView(mView[3]);
		    	initHistroyLabels();
		    	//System.out.println("Label");
		    	break;
		    case ALL:
		    	QueryAllList();
		    	//System.out.println("ALL");
				all.searchText.setText("");
				if(allList.size()!=0)
					all.list.setAdapter(new MyListAdapter(MainActivity.this));
				else
					all.list.setAdapter(new simple(MainActivity.this,"���ͨѶ¼Ϊ��Ŷ���쵽�����Ӱ�ť����µ���ϵ�˰ɣ�"));
				
		        mLL.removeAllViews();
		        mLL.addView(mView[1]);
		    	break;
		    case LOVE:
		    	 mLL.removeAllViews();
			   	 mLL.addView(mView[2]);
				 //�Ȳ�ѯ���Լ��ǵ�list��������adapter
				QueryLoveList();
				//System.out.println("Love");
				if(loveList.size()!=0)
				loveContains.lovelistview.setAdapter(new LoveListAdapter(MainActivity.this));
				else
				{
				    loveContains.lovelistview.setAdapter(new simple(MainActivity.this,"��û������κ��ղ�Ŷ���쵽�������б��鿴����Ӱɣ�"));
				}
				break;
		    case SOME:
		    	mLL.removeAllViews();
		   	    mLL.addView(mView[0]);
		   	   //QueryAllList();
		   	    //Toast.makeText(MainActivity.this,"ddddddddddddd",Toast.LENGTH_SHORT).show();
		   	    selectedResult.clear();
		   	    if(allList.size()!=0)
		   		    some.selectToDelete.setAdapter(new selectToDeleteAdapter(MainActivity.this,0));
				else
					some.selectToDelete.setAdapter(new simple(MainActivity.this,"���ͨѶ¼Ϊ��Ŷ!"));
		   	    break;
		    case SYNC:
		    	mLL.removeAllViews();
		    	mLL.addView(mView[4]);
	            
		    }
		    super.onActivityResult(requestCode, resultCode, data);

		    }

    private void setOnlistner(){
		//�л���������ϵ�˽���
		 allcontacter.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
					mLL.removeAllViews();
			   	    mLL.addView(mView[1]);
			   	    all.searchText.setText("");
			   	  //all.searchText.setVisibility(EditText.INVISIBLE);
					// TODO Auto-generated method stub
					 //�Ȳ�ѯ����list��������adapter��
					QueryAllList();
					if(allList.size()!=0)
						all.list.setAdapter(new MyListAdapter(MainActivity.this));
					else
						all.list.setAdapter(new simple(MainActivity.this,"���ͨѶ¼Ϊ��Ŷ���쵽�����Ӱ�ť����µ���ϵ�˰ɣ�"));
										
					allcontacter.setBackgroundResource(R.drawable.b9e086);
			   	    group.setBackgroundResource(R.drawable.a9777);
			   	    love.setBackgroundResource(R.drawable.a9777);
			   	    label.setBackgroundResource(R.drawable.a9777);
			   	    sync.setBackgroundResource(R.drawable.a9777);
				}
			});
		 
			
			 group.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
			
					   System.out.println("group �����");
			          
			   	    mLL.removeAllViews();
			   	    mLL.addView(mView[0]);
			   	 //QueryAllList();
			   	    selectedResult.clear();
			   	 if(allList.size()!=0)
			   		    some.selectToDelete.setAdapter(new selectToDeleteAdapter(MainActivity.this,0));
					else
						some.selectToDelete.setAdapter(new simple(MainActivity.this,"���ͨѶ¼Ϊ��Ŷ!"));
		            allcontacter.setBackgroundResource(R.drawable.a9777);
			   	    group.setBackgroundResource(R.drawable.b9e086);
			   	    love.setBackgroundResource(R.drawable.a9777);
			   	    label.setBackgroundResource(R.drawable.a9777);
			   	    sync.setBackgroundResource(R.drawable.a9777);
			         
			           some.deleteSome.setOnClickListener(new OnClickListener() {
					
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(selectedResult.size()!=0){
							CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
							builder.setTitle("��ʾ");
						    builder.setMessage("ȷ��ɾ����Щ��ϵ����");
							builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									// TODO Auto-generated method stub
									dialog.dismiss();								
									int size = selectedResult.size();
									DatabaseHelper dbHelper1 = new DatabaseHelper(MainActivity.this,"test_mars_db");
							        SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
							    
							        String [] nameToDelete = new String [size];
							        for(int i = 0; i < size; i++){
							        	nameToDelete[i] = (String)allList.get((Integer)selectedResult.get(i)).get("name");
							        	db1.delete("user", "name = ?", new String[]{nameToDelete[i]});
							        }
							        
							        //ɾ����ϵ��
							        delContact((Context)MainActivity.this,nameToDelete,size);
							        
							    	if (db1 != null && db1.isOpen()) {
						    	           db1.close();
						    	    }

						    	     if (dbHelper1 != null) {
						    	         dbHelper1.close();
						    	     }
						    	     Toast.makeText(MainActivity.this, "����ɾ���ɹ�",Toast.LENGTH_SHORT).show();
						    	     
						    	     QueryAllList();
						    	     if(allList.size()!=0)
								   		    some.selectToDelete.setAdapter(new selectToDeleteAdapter(MainActivity.this,0));
										else
											some.selectToDelete.setAdapter(new simple(MainActivity.this,"���ͨѶ¼Ϊ��Ŷ!"));
								}
							});
						    builder.setNegativeButton("ȡ��",
						    		new android.content.DialogInterface.OnClickListener() {
						    			public void onClick(DialogInterface dialog, int which) {
						    				   //QueryAllList();
						    				dialog.dismiss();
						    			}
						    		});

						    builder.create().show();
							}
							else
								Toast.makeText(MainActivity.this, "��ѡ����ϵ��",Toast.LENGTH_SHORT).show();
						}
					});
			          
			        some.sentMessToSome.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(selectedResult.size()==0)
								Toast.makeText(MainActivity.this, "��ѡ����ϵ��",Toast.LENGTH_SHORT).show();
							else{
							ArrayList<String> tmpPhones  = new ArrayList<String>();
							ArrayList<String> tmpNames  = new ArrayList<String>();
							int size = selectedResult.size();
							for(int i = 0; i < size; ++i){
								tmpPhones.add((String)allList.get((Integer)selectedResult.get(i)).get("phone"));
								tmpNames.add( (String)allList.get((Integer)selectedResult.get(i)).get("name"));
							}
							
							Intent intent = new Intent(MainActivity.this,sentMessToGroup.class);
							intent.putStringArrayListExtra("name", tmpNames);
							intent.putStringArrayListExtra("phone", tmpPhones);
							//System.out.r
							MainActivity.this.startActivityForResult(intent, SOME);
							//Toast.makeText(MainActivity.this,"Ⱥ�����ܻ�ûʵ�ְ�>-<",Toast.LENGTH_SHORT).show();
							}
						}
					});
			           
					}
				});
			 
			
			 //ת�����ղؽ���
			 love.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						System.out.println("love �����");
						
						allcontacter.setBackgroundResource(R.drawable.a9777);
				   	    group.setBackgroundResource(R.drawable.a9777);
				   	    love.setBackgroundResource(R.drawable.b9e086);
				   	    label.setBackgroundResource(R.drawable.a9777);
				   	    sync.setBackgroundResource(R.drawable.a9777);
				   	 
					   	 mLL.removeAllViews();
					   	 mLL.addView(mView[2]);
						 //�Ȳ�ѯ���Լ��ǵ�list��������adapter
						QueryLoveList();
						if(loveList.size()!=0)
						loveContains.lovelistview.setAdapter(new LoveListAdapter(MainActivity.this));
						else
						{
						    loveContains.lovelistview.setAdapter(new simple(MainActivity.this,"��û������κ��ղ�Ŷ���쵽�������б��鿴����Ӱɣ�"));
						}
					}
				});
		

			 label.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					allcontacter.setBackgroundResource(R.drawable.a9777);
			   	    group.setBackgroundResource(R.drawable.a9777);
			   	    label.setBackgroundResource(R.drawable.b9e086);
			   	    love.setBackgroundResource(R.drawable.a9777);
			   	    sync.setBackgroundResource(R.drawable.a9777);
			   	 initHistroyLabels();
			   	 mLL.removeAllViews();
			   	 mLL.addView(mView[3]);
			   	 
				}
			});
			 
			 sync.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//initHistroyLabels();
				   	 	mLL.removeAllViews();
				   	 	mLL.addView(mView[4]);
						allcontacter.setBackgroundResource(R.drawable.a9777);
				   	    group.setBackgroundResource(R.drawable.a9777);
				   	    label.setBackgroundResource(R.drawable.a9777);
				   	    love.setBackgroundResource(R.drawable.a9777);
				   	    sync.setBackgroundResource(R.drawable.b9e086);

				    	syn.returnmessage.setText("");
					}
				});
			 
			 syn.upload.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						login_builder = new LogInDialog.Builder(MainActivity.this);
						login_builder.setTitle("�ϴ�");
						login_builder.setMessage("Ϊ�˱�����ĸ�����Ϣ,���ȵ�¼��ע��");	
						
						
						login_builder.setsignupButton("ע��", new DialogInterface.OnClickListener() {									
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stubeditUser = builder.getUser();	
								//Toast.makeText(MainActivity.this, builder.getUser().getText().toString(),Toast.LENGTH_SHORT).show();
								String user_name = login_builder.getUser().getText().toString();
								String user_pass = login_builder.getPassword().getText().toString();
								
								UploadAndDownloadFile.login(user_name, user_pass, "register", url,true);
								dialog.dismiss();
							}
						});

						login_builder.setloginButton("��¼", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								String user_name = login_builder.getUser().getText().toString();
								String user_pass = login_builder.getPassword().getText().toString();
								
								UploadAndDownloadFile.login(user_name, user_pass, "login", url,true);
								dialog.dismiss();
							}
						});

						login_builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
						
						login_builder.create().show();
				        /*UploadAndDownloadFile.saveAsLocalFile(mContext,"test");
				    	upload.uploadfile(mContext.getFilesDir()+ "/test");*/
					}
				});
	   	     	
	  
	           syn.download.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						//String filename = "test";
						login_builder = new LogInDialog.Builder(MainActivity.this);
						login_builder.setTitle("ͬ�����ֻ�");
						login_builder.setMessage("Ϊ�˱�����ĸ�����Ϣ,���ȵ�¼��ע��");	
						login_builder.setsignupButton("ע��", new DialogInterface.OnClickListener() {									
						
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stubeditUser = builder.getUser();	
								//Toast.makeText(MainActivity.this, builder.getUser().getText().toString(),Toast.LENGTH_SHORT).show();
								String user_name = login_builder.getUser().getText().toString();
								String user_pass = login_builder.getPassword().getText().toString();
								
								UploadAndDownloadFile.login(user_name, user_pass, "register", url,false);
								dialog.dismiss();
							}
						});

						login_builder.setloginButton("��¼", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								//builder.setMessage("denglu");
								String user_name = login_builder.getUser().getText().toString();
								String user_pass = login_builder.getPassword().getText().toString();
								
								UploadAndDownloadFile.login(user_name, user_pass, "login", url,false);
								dialog.dismiss();
							}
						});

						login_builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								//builder.setMessage("zhuce");
								dialog.dismiss();
							}
						});
						
						login_builder.create().show();
						
						//download.downloadfile(mContext, "test", url + "/download/" + filename);	
						
						//Toast.makeText(MainActivity.this, "����ͨѶ¼",Toast.LENGTH_SHORT).show();
					}
				});
	           
			 //��ת�������ϵ�˽���
			all.addContacter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent mIntent = new Intent(MainActivity.this, AddContacter.class);
					mIntent.putExtra("name", all.searchText.getText().toString());
					Bundle bundle = new Bundle();
					bundle.putBoolean("from", true);
					mIntent.putExtras(bundle);
					MainActivity.this.startActivityForResult(mIntent,ALL);
				}
			});
				
			  //Ŀǰ����ɾ��,������ʾ
			all.list.setLongClickable(true);
			all.list.setOnItemLongClickListener(new OnItemLongClickListener() {
				@Override
				public boolean onItemLongClick(AdapterView<?> arg0, final View arg1,
						int arg2, long arg3) {
					// TODO Auto-generated method stub
							ViewClass tmp = (ViewClass)arg1.getTag();
							String tmpName = tmp.name.getText().toString();
							// TODO Auto-generated method stub
							tangkuang(tmpName);
                return false;
				}

				private void tangkuang(final String tmpName) {
					// TODO Auto-generated method stub
					CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
					builder.setTitle("��ʾ");
					builder.setMessage("ȷ��ɾ������ϵ�ˣ�");
					builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								DatabaseHelper dbHelper1 = new DatabaseHelper(MainActivity.this,"test_mars_db");
						        SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
						    	
						        db1.delete("user", "name=?", new String[]{tmpName});
						    		
						    	delContact((Context)MainActivity.this,new String[]{tmpName}, 1);
						    	
						    	if (db1 != null && db1.isOpen()) {
					    	           db1.close();
					    	    }

					    	     if (dbHelper1 != null) {
					    	         dbHelper1.close();
					    	     }
					    	     QueryAllList();
					    	     if(allList.size()!=0)
										all.list.setAdapter(new MyListAdapter(MainActivity.this));
									else
										all.list.setAdapter(new simple(MainActivity.this,"���ͨѶ¼Ϊ��Ŷ���쵽�����Ӱ�ť����µ���ϵ�˰ɣ�"));
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
		//all.searchText.setVisibility(EditText.GONE);
		//all.searchText2.setVisibility(TextView.GONE);
	    all.searchText2.setFocusableInTouchMode(true);
        all.searchText2.requestFocus();
		all.searchText.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
				String searchInfo = s.toString();
				//������������Ϊ���ǣ���ʾ������ϵ��
				 if(searchInfo.equals("")){
						//QueryAllList();
						if(allList.size()!=0)
							all.list.setAdapter(new MyListAdapter(MainActivity.this));
						else
							all.list.setAdapter(new simple(MainActivity.this,"���ͨѶ¼Ϊ��Ŷ���쵽�����Ӱ�ť����µ���ϵ�˰ɣ�"));
						
						return;
				}	
				search(searchInfo);
				
								
			    if(searchResultInfo.isEmpty()==false)
			    {
			    	all.list.setAdapter(new MySearchAdapter());
			    }
			    else{
			    	    all.list.setAdapter(new simple(MainActivity.this,"�Ҳ�����ϵ��"+searchInfo+"�������Ӱ�ť��ֱ����ӱ༭^_^" ));
			    }
			}
		});
	}
    //��������������adapter����ֻɾ��
    class selectToDeleteAdapter extends BaseAdapter{

    	int operator = 0;
    	public selectToDeleteAdapter(Context context,int n){
    		//selectedResult.clear();
    		operator = n;
    	}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return allList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.selecttodelete, null);
			convertView.setLongClickable(true);
	        convertView.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
				    builder.setMessage("�Ƿ�ȫѡ");
				    builder.setTitle("��ʾ");
				    builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							selectedResult.clear();
							 if(allList.size()!=0)
						   		    some.selectToDelete.setAdapter(new selectToDeleteAdapter(MainActivity.this,1));
								else
									some.selectToDelete.setAdapter(new simple(MainActivity.this,"���ͨѶ¼Ϊ��Ŷ!"));
						}
					});
				  /*  builder.setNeutralButton("��ѡ", new DialogInterface.OnClickListener() {
						
						@Override
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							dialog.dismiss();
		    				
							if(allList.size()!=0)
					   		    some.selectToDelete.setAdapter(new selectToDeleteAdapter(MainActivity.this,2));
							else
								some.selectToDelete.setAdapter(new simple(MainActivity.this,"���ͨѶ¼Ϊ��Ŷ!"));
						}
					});*/
				    builder.setNegativeButton("ȡ��",
				    		new android.content.DialogInterface.OnClickListener() {
				    			public void onClick(DialogInterface dialog, int which) {
				    				dialog.dismiss();
				    			}
				    		});

					builder.create().show();	
					return false;
				}
			});
			Holder holder = new Holder();
			holder.name = (TextView)convertView.findViewById(R.id.name);
			holder.check = (CheckBox)convertView.findViewById(R.id.check);
		
			convertView.setTag(holder);
			 //������ϵ�˵ı���
    	    if(position % 2==0)
    	    	convertView.setBackgroundResource(R.drawable.background_row01);
    	    else
    	    	convertView.setBackgroundResource(R.drawable.background_row02);
    	    
			
			holder.name.setText(allList.get(position).get("name").toString());
		   
			holder.check.setTag(position);
			holder.check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					Integer num = (Integer)buttonView.getTag();
						if(isChecked)
							selectedResult.add(num);
						else
						{
							selectedResult.remove(num);
						}
						
				}
			});
			if(operator == 1)
			{
				holder.check.setChecked(true);
				selectedResult.add(position);
			}
			else if(operator == 2){
				Integer num = (Integer)position;
				if(selectedResult.contains(num))
				{
					holder.check.setChecked(false);
					selectedResult.remove(num);
				}
				else
				{
					selectedResult.add(num);
					holder.check.setChecked(true);
				}
			}

			return convertView;
		}
    	class Holder{
    		TextView name;
    		CheckBox check;
    	}
    }
    
    //���ڴ���������
	ArrayList<HashMap<String, Object>> searchResultInfo =  new ArrayList<HashMap<String,Object>>();
	
	//�ڴ˴���EditText�� ��������Ϣ���д���
    private void search(String searchMess){
    	searchResultInfo.clear();
    	DatabaseHelper dbHelper1 = new DatabaseHelper(MainActivity.this,"test_mars_db");
		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
		if(searchMess.length()==0)
		{
			if (db1 != null && db1.isOpen()) {
	            db1.close();
	     }

	     if (dbHelper1 != null) {
	        dbHelper1.close();
	     }
			return;
		}
		//ѡ����䣬������������ϵ�����ֻ�绰��ģ������
		//String sql = "select * from user where phone like'%" + searchMess + "%' OR PY like '%"+searchMess +"%' OR name like '%"+searchMess +"%'";
		
		Cursor cursor = db1.query("user", new String[]{"name","groupp","phone"}, "phone like'%" + searchMess + "%' OR PY like '%"+searchMess +"%' OR name like '%"+searchMess +"%'",null, null, null, " name COLLATE LOCALIZED  ASC");
		//System.out.println(String.valueOf(cursor.getCount()));
		if(cursor!=null){
			while(cursor.moveToNext()){
				String phone = cursor.getString(cursor.getColumnIndex("phone"));
				String name = cursor.getString(cursor.getColumnIndex("name"));
				System.out.println(phone+"              "+name);
				if(phone.length()==0)
		    	{
		    		HashMap<String, Object> map = new HashMap<String, Object>();
		               map.put("name", name);
		               map.put("phone","");
		               searchResultInfo.add(map);
		    		   continue;
		    	}
				int index[] = new int[20];
				String []phoneStr = new String[20];
		    	int indexcount = 0;
		    	//System.out.println("Phone: " + phone);
		    	for (int i = 0 ; i < phone.length() ; ++i){
		    		if (phone.charAt(i) == '*'){
		    			index[indexcount] = i;
		    			indexcount ++;
		    		}
		    	}
		    	//System.out.println("indexcount: " + String.valueOf(indexcount));

		    	int count = 0;
		    	for (int i = 0 ; i < indexcount - 1; ++i){
		    		int temp = index[i] + 1;
		    		phoneStr[count] = phone.substring(temp, index[i+1]);
		    		//System.out.println(phoneStr[count]);
		    		count++;
		    	}
		    	//System.out.println(String.valueOf(count));
		    	if(index[indexcount-1]+1<phone.length())
		    	phoneStr[count++] = phone.substring(index[indexcount - 1]+1);
		    	//System.out.println(phoneStr[count]);
		    	if(phone.contains(searchMess)==false){
		    		for(int i=0;i<count;++i){
		               HashMap<String, Object> map = new HashMap<String, Object>();
		               map.put("name", name);
		               map.put("phone",phoneStr[i]);
		               searchResultInfo.add(map);
		    		}
		    	}
		    	else {
		    		for(int i=0;i<count;++i){
		    			if(phoneStr[i].contains(searchMess))
		    			{
		    				//System.out.println(phoneStr[i]+"              "+searchMess);
		    				 HashMap<String, Object> map = new HashMap<String, Object>();
				               map.put("name", name);
				               map.put("phone",phoneStr[i]);
				               searchResultInfo.add(map);
		    			}
		    		}
		    	}
			}
			
			for(int i=0;i<searchResultInfo.size();++i){
				System.out.println(searchResultInfo.get(i).get("name")+" "+searchResultInfo.get(i).get("phone"));
			}
		}

		 cursor.close();
		if (db1 != null && db1.isOpen()) {
	            db1.close();
	     }

	     if (dbHelper1 != null) {
	        dbHelper1.close();
	     }
    }
    
    //���ڴ�������ѳ��б��adapter,�����������searchResultInfo����
    class MySearchAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return searchResultInfo.size();
		}   

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int arg0, View convertView, ViewGroup arg2) {
			// TODO Auto-generated method stub
			
			convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.searchview, null);
			SearchItemClass searchItem = new SearchItemClass();
			searchItem.dail = (Button)convertView.findViewById(R.id.dial_bt);
			searchItem.name = (TextView)convertView.findViewById(R.id.name);
			searchItem.name.setText((String)searchResultInfo.get(arg0).get("name"));
			searchItem.phone = (TextView)convertView.findViewById(R.id.phone);
			searchItem.phone.setText((String)searchResultInfo.get(arg0).get("phone"));
			searchItem.sentMessage = (Button)convertView.findViewById(R.id.sentMess_bt);
			convertView.setTag(searchItem);
			
			 //���Ʊ����������������滻
    	    if(arg0 % 2==0)
    	    	convertView.setBackgroundResource(R.drawable.background_row01);
    	    else
    	    	convertView.setBackgroundResource(R.drawable.background_row02);
    	    
			//�����ת���鿴��ϵ��ҳ��
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					SearchItemClass searchItem = (SearchItemClass)v.getTag();
					int n = (Integer) searchItem.dail.getTag();
					Intent mIntent = new Intent(MainActivity.this,CheckContacter.class);
					mIntent.putExtra("name", (String)searchResultInfo.get(n).get("name"));
					Bundle bundle = new Bundle();
					bundle.putBoolean("from", true);
					mIntent.putExtras(bundle);
					MainActivity.this.startActivityForResult(mIntent,ALL);
				}
			});
			
			//��button���TAG = position���㶨λ
			searchItem.dail.setTag(arg0);
			searchItem.dail.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					int n = (Integer) v.getTag();
					String phone = (String)searchResultInfo.get(n).get("phone");
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_CALL);
					intent.setData(Uri.parse("tel:"+phone));
					MainActivity.this.startActivity(intent);
				}
			});
			
			searchItem.sentMessage.setTag(arg0);
			searchItem.sentMessage.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int n = (Integer) v.getTag();
					String phone = (String)searchResultInfo.get(n).get("phone");
					Intent intent = new Intent();
					//ϵͳĬ�ϵ�action��������Ĭ�ϵĶ��Ž���
					intent.setAction(Intent.ACTION_SENDTO);
					//��Ҫ����Ϣ�ĺ���
					intent.setData(Uri.parse("smsto:"+phone));
					MainActivity.this.startActivity(intent);

				}
				
			});
			return convertView;
		}
    	
		public class SearchItemClass{
			TextView name,phone;
			Button dail,sentMessage;
		}
    }
    //���ڴ��������ϵ����Ϣ��adpater,������Ϣ���ݱ����� allList���� 
    class MyListAdapter extends BaseAdapter {
    	
    	int size = 0;
    	public MyListAdapter(Context context) {
    	    mContext = context;
    	    size = allList.size();
    		//Toast.makeText(MainActivity.this, "length = "+size, Toast.LENGTH_LONG).show();
    	}
     
    	public int getCount() {
    	    //���û�������
    	    return size;
    	}
     
    	@Override
    	public boolean areAllItemsEnabled() {
    	    return false;
    	}
     
    	public Object getItem(int position) {
    	    return position;
    	}
    	public long getItemId(int position) {
    	    return position;
    	}
     
    	public View getView(final int position, View convertView, ViewGroup parent) {
    	    ViewClass view;
    	    
    		convertView = LayoutInflater.from(mContext).inflate(R.layout.adapterview, null);
    		
    	
    		view = new ViewClass();

    		      view.image = (ImageView) convertView.findViewById(R.id.img);
    		      view.name = (TextView) convertView.findViewById(R.id.title);
    		      view.star = (Button) convertView.findViewById(R.id.view_btn);
    		      view.label = (TextView) convertView.findViewById(R.id.label_in_main);
    		      view.label.setText(allList.get(position).get("groupp").toString());
    		     
    		      if(allList.get(position).get("star").toString().equals("1") == true){
    			       view.star.setBackgroundResource(R.drawable.star_yes);
    		      }
    		      else{
    		    	  view.star.setBackgroundResource(R.drawable.star_no);
    		       }
    		convertView.setTag(view);	    
    	    //���Ʊ����������������滻
    	    if(position%2==0)
    	    	convertView.setBackgroundResource(R.drawable.background_row01);
    	    else
    	    	convertView.setBackgroundResource(R.drawable.background_row02);
    	    
    	    convertView.setClickable(true);
    		convertView.setLongClickable(true);
    	    convertView.setOnClickListener(new OnClickListener() {	
				@Override
				public void onClick(View v) {
	                    ViewClass view1 = (ViewClass)v.getTag();
						int num =(Integer) view1.star.getTag();
						Intent mIntent = new Intent(MainActivity.this,CheckContacter.class);
						mIntent.putExtra("name",(String)allList.get(num).get("name"));
						Bundle bundle = new Bundle();
						bundle.putBoolean("from", true);
						mIntent.putExtras(bundle);
		                MainActivity.this.startActivityForResult(mIntent,ALL);
					}
			});
    	    
    	    view.name.setText((String) allList.get(position).get("name"));	
    	    //������ϵ��ͷ��
    	    //view.image.setImageBitmap(photo);
    	   if(allList.get(position).get("photoURI").toString().equals("null") == true)//++++++++++++++++++���������ַ������������ͼƬ�����null++++++++++++++++++++++++++
        	    view.image.setImageResource(R.drawable.headportrait_main);
    	   else
        	    //view.image.setImageURI(Uri.parse( (String) allList.get(position).get("photoURI")));
    	   {
    		   Uri uriStr = Uri.fromFile(new File((String) allList.get(position).get("photoURI")));
    		   Log.d("image", uriStr.toString());
    		   view.image.setImageURI(uriStr);
    	   }
    		
    	  
    	    int TAG = position;
    	  
    	  //Ϊstar���button����TAG,ֱ����position�ǻ��ҵģ���
    	  view.star.setTag(TAG);
    	  view.star.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					//��ȡ��Ӧ��tag��ΪҪ�����е�λ��
					int num = (Integer)v.getTag();//�ǵ���һ������
                	System.out.println(num);
                    DatabaseHelper dbHelper1 = new DatabaseHelper(MainActivity.this,"test_mars_db");
            		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
            		Cursor cursor = db1.query("user", new String[]{"name","star"}, null, null, null, null, " name COLLATE LOCALIZED  ASC");
            		cursor.moveToPosition(num);
            		
            		String star = cursor.getString(cursor.getColumnIndex("star")).toString();
            		String currentName = cursor.getString(cursor.getColumnIndex("name"));
            
            		//����star��ֵ���ı���ʵ״̬
            		if( star.equals("1")){
            			star = "0";
            			v.setBackgroundResource(R.drawable.star_no);
            		}
            		else if(star.equals("0")){
            			star = "1";
        			    v.setBackgroundResource(R.drawable.star_yes);
        		    } 
            	
            		//�������ݿ�
            		ContentValues values = new ContentValues();
            		if(star.equals("1"))
            	    values.put("star", "1");
                    else
                    values.put("star","0");
            		
            		db1.update("user", values, "name = ?", new String []{currentName});
            		
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
          
             return convertView;
    	}
               
     }
   
   
  //���ڴ���Ѳص���ϵ����Ϣ��adpater,������Ϣ���ݱ����� loveList���� 
    class LoveListAdapter extends BaseAdapter {
    	Context mContext;
    	
    	public LoveListAdapter(Context context) {
    	    mContext = context;
    	}

    	public int getCount() {
    	    //���û�������
    	    return loveList.size();
    	}
     
    	@Override
    	public boolean areAllItemsEnabled() {
    	    return false;
    	}
     
    	public Object getItem(int position) {
    	    return position;
    	}
     
    	public long getItemId(int position) {
    	    return position;
    	}
     
    	
    	public View getView(final int position, View convertView, ViewGroup parent) {
    	    ViewClass view;
    	  
    		convertView = LayoutInflater.from(mContext).inflate(R.layout.adapterview, null);
    		view = new ViewClass();
            
    		view.image = (ImageView) convertView.findViewById(R.id.img);
    		view.name = (TextView) convertView.findViewById(R.id.title);
    		view.star = (Button) convertView.findViewById(R.id.view_btn);
    		 view.label = (TextView) convertView.findViewById(R.id.label_in_main);
		      view.label.setText((String)loveList.get(position).get("groupp"));
		      
    		convertView.setTag(view);
    	   
    	    //������ϵ�˵ı���
    	    if(position%2==0)
    	    	convertView.setBackgroundResource(R.drawable.background_row01);
    	    else
    	    	convertView.setBackgroundResource(R.drawable.background_row02);
    	    
    	    //�̰���ʱ��鿴��ϵ�ˣ���һ��name
    		convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
                    ViewClass view1 = (ViewClass)v.getTag();
					int num =(Integer) view1.name.getTag();
					Intent i = new Intent(MainActivity.this,CheckContacter.class);
					i.putExtra("name",(String)loveList.get(num).get("name"));
	                MainActivity.this.startActivityForResult(i,LOVE);
				}
			});
    		view.name.setTag(position);
    	    view.name.setText((String)loveList.get(position).get("name"));	
    	    
    	    if(loveList.get(position).get("photoURI").toString().equals("null") == true)//++++++++++++++++++���������ַ������������ͼƬ�����null++++++++++++++++++++++++++
        	    view.image.setImageResource(R.drawable.headportrait_main);
        	    else
        	    {
        	    	Log.d("photo uri = ", (String)loveList.get(position).get("photoURI"));
        	    	view.image.setImageURI(Uri.parse((String) loveList.get(position).get("photoURI")));
        	    }
    	    
    	   //Ϊ���button����TAG
    	   int TAG = (Integer)loveList.get(position).get("position");
    	   view.star.setTag(TAG);
    	   
            view.star.setOnClickListener(new View.OnClickListener() {      
                @Override
                public void onClick(View v) {
                	
                	//��ȡ��Ӧ��tag��ΪҪ�����е�λ��
                	int num = (Integer)v.getTag();
                	//System.out.println(num);
                  
                    DatabaseHelper dbHelper1 = new DatabaseHelper(MainActivity.this,"test_mars_db");
            		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
            		Cursor cursor = db1.query("user", new String[]{"name","star"}, null, null, null, null, " name COLLATE LOCALIZED  ASC");
            		cursor.moveToPosition(num);
            		
            		String currentName = cursor.getString(cursor.getColumnIndex("name"));
            		
            		ContentValues values = new ContentValues();
            		
            		values.put("star", "0");
            		
            		db1.update("user", values, "name=?", new String[]{currentName});
                    Toast.makeText(MainActivity.this,"ȡ���ղسɹ���",Toast.LENGTH_SHORT).show();
            		//��������set��adapter�����б�
            		QueryLoveList();
            		if(loveList.size()!=0)
        				loveContains.lovelistview.setAdapter(new LoveListAdapter(MainActivity.this));
        				else
        				{
        				    loveContains.lovelistview.setAdapter(new simple(MainActivity.this,"��û������κ��ղ�Ŷ���쵽�������б��鿴����Ӱɣ�"));
        				}
            		
            		//Toast.makeText(MainActivity.this, cursor.getString(cursor.getColumnIndex("name"))+" " + currentName.toString()+' '+"dd"+num,Toast.LENGTH_SHORT).show();
            		cursor.close();
            		 if (db1 != null && db1.isOpen()) {
            	          db1.close();
            	     }
            	     if (dbHelper1 != null) {
            	         dbHelper1.close();
            	     }            	    
                }
            });  
    	    return convertView;
    	}     
     }

    class LabelListAdapter extends BaseAdapter {
    	Context mContext;
    	
    	public LabelListAdapter(Context context) {
    	    mContext = context;
    	}

    	public int getCount() {
    	    //���û�������
    	    return labels.size();
    	}
     
    	@Override
    	public boolean areAllItemsEnabled() {
    	    return false;
    	}
     
    	public Object getItem(int position) {
    	    return position;
    	}
     
    	public long getItemId(int position) {
    	    return position;
    	}
     
    	class subview
    	{
    		TextView label;
    		TextView count;
    	}
    	public View getView(final int position, View convertView, ViewGroup parent) {
    		subview view;
    	  
    		convertView = LayoutInflater.from(mContext).inflate(R.layout.addedlabel, null);
    		view = new subview();
            
    		view.label = (TextView) convertView.findViewById(R.id.text);
    		
    		view.count = (TextView) convertView.findViewById(R.id.text1);
    		
    		view.label.setText(labels.get(position));
    		view.count.setText(String.valueOf(labelsCount[position]));
    		
    		convertView.setTag(labels.get(position));
    		
    		if(position % 2==0)
    			convertView.setBackgroundResource(R.drawable.backgroundrowforlabel01);
	    	 else
	    	    convertView.setBackgroundResource(R.drawable.backgroundrowforlabel02);
    		
    		convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String n = (String)v.getTag();
	                Intent intent = new Intent(MainActivity.this,checkLabel.class);
	                intent.putExtra("label", n);
	                MainActivity.this.startActivityForResult(intent,LABEL);
				}
			 });
    		
    		convertView.setLongClickable(true);
    		convertView.setOnLongClickListener(new OnLongClickListener() {
					
				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					final String n = (String)v.getTag();
					CustomDialog.Builder builder = new CustomDialog.Builder(MainActivity.this);
					builder.setTitle("��ʾ");
					    builder.setMessage("�Ƿ�ɾ���ñ�ǩ��");
					    builder.setPositiveButton("��", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								HistoryLabel dbHelper2 = new HistoryLabel(MainActivity.this,"test_mars_db1");
						        SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
						    	DatabaseHelper dbHelper1 = new DatabaseHelper(mContext, "test_mars_db");
						    	SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
						    	
						        db2.delete("historyLabel", "label=?", new String[]{n});
						    	
						        Cursor cursor = db1.query("user", new String[]{"name","groupp"}, "groupp like'%" + n +"%'",null, null, null, " name COLLATE LOCALIZED  ASC");
								
						        if(!cursor.equals(null))
						        {
						        	while(cursor.moveToNext())
						        	{
						        		ContentValues values = new ContentValues();
						        		String groupp = cursor.getString(cursor.getColumnIndex("groupp"));
						        		values.put("groupp", groupp.replace(n,""));
						        		db1.update("user", values, "name=?",new String[]{cursor.getString(cursor.getColumnIndex("name"))});
						        	}
						        }
						    	if (db2 != null && db2.isOpen()) {
					    	           db2.close();
					    	    }

					    	     if (dbHelper2 != null) {
					    	         dbHelper2.close();
					    	     }
					    	     
					    	     
					    	     Toast.makeText(MainActivity.this, "ɾ����ǩ�ɹ�",Toast.LENGTH_SHORT).show();
					    	     initHistroyLabels();
							}
					    	
					    });
					    builder.setNegativeButton("��", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
					    builder.create().show();
					return false;
				}
			 });
			
				 
    	    return convertView;
    	}     
     }
    
    /*
     * ɾ�����ֵ���name�����������ϵ��
     * ����ɾ��
     */
    private void delContact(Context context, String [] name, int size) {
    	 
    	ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
   	 	
    	for(int i = 0; i < size; i++) //��ת������ѯ��name�Ĺ�괦
    	{
     	   Cursor cursor = getContentResolver().query(Data.CONTENT_URI,new String[] { Data.RAW_CONTACT_ID },ContactsContract.Contacts.DISPLAY_NAME + "=?",new String[]{name[i]}, null);
     
     	   if (cursor.moveToFirst()) {
     	 
     		   do {
     	 
     			   long Id = cursor.getLong(cursor.getColumnIndex(Data.RAW_CONTACT_ID));
     	 

     			   ops.add(ContentProviderOperation.newDelete(
     					   ContentUris.withAppendedId(RawContacts.CONTENT_URI,Id)).build());
     	 
     		   } while (cursor.moveToNext());
     		   cursor.close();	 
        	}
        }
    	if(ops.size() != 0)
    	{
    		try 
    		{
	    	 
				getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
	    	 
			} 
			catch (Exception e){}
		}	
    }
    	 
    //��ӱ���ͨѶ¼�����ݿ���û�еĶ���
    public void getContactInformation() {
    	
   	 //�õ�ContentResolver����   
       ContentResolver cr = getContentResolver();   
      //ȡ�õ绰���п�ʼһ��Ĺ��   
       Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, " display_name COLLATE LOCALIZED  ASC");   
       //cursor.moveToFirst();
       
       String imagePath = mContext.getFilesDir() + "/ContacterImage";   		
  		File dirImageFile=new File(imagePath);//��imageĿ¼
  		
  		if(!dirImageFile.exists())
  		{
  			//�ж��ļ���Ŀ¼�Ƿ����
  			dirImageFile.mkdir();
  			Log.d("file", "success");
  		}
  		

			
  	   SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'yyyyMMddHHmmss");
       while (cursor.moveToNext())   
       {   
           // ȡ����ϵ������   
           int nameFieldColumnIndex = cursor.getColumnIndex(PhoneLookup.DISPLAY_NAME);   
           String name = cursor.getString(nameFieldColumnIndex);   
           //���ж����ݿ��Ƿ���ڴ��ˣ�����������������ϵ�ˡ�
           String str =  QueryByKey(name);
          
          if (str != null) continue;
           
          
        //  mContactsName.add(name);
           // ȡ����ϵ��ID
           String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));   
           
           //��sort����key�ֶλ�ȡƴ��
           String PY1 = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.SORT_KEY_ALTERNATIVE));
           int size = PY1.length();
           String PY="";
          for(int i=0;i<size;i++){
        	  char ch = PY1.charAt(i);
        	  if(ch!=' ' && ch < 128)
        		  PY+=ch;
          }
           //String PY = JString.getPinyin(name);
           //System.out.println("*********************************"+PY);
           
           // ȡ�õ绰����(���ܴ��ڶ������)
           Cursor phone = cr.query(Phone.CONTENT_URI, null,  Phone.CONTACT_ID + " = " + contactId, 
           		null, null);   
           String strPhoneNumber = "";
           while (phone.moveToNext())   
           {
               String temp = phone.getString(phone.getColumnIndex(Phone.NUMBER));   
               if (!TextUtils.isEmpty(temp))
               {	
            	   strPhoneNumber += "*";
                    strPhoneNumber += temp;
               }
                			         
           }         
           phone.close();            
 
          //��ȱ��ϵ�˵�����
         String ringName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.CUSTOM_RINGTONE));

         if(ringName == null)
        	 ringName = "Ĭ������";
         else
         {
        	  Cursor cursor1 = getContentResolver().query(Uri.parse(ringName), null, null, null, null);
              if(cursor1!=null&&cursor1.moveToFirst())
              ringName = cursor1.getString(2);
         }
        // System.out.println(ringName);
         //�õ�����ϵ�˵������ַ
   		Cursor emails = getContentResolver().query(Email.CONTENT_URI,null,
   			   Email.CONTACT_ID + "=" + contactId,null,null);
   		  String emailAdd = "@";
   		while(emails.moveToNext()){
   			emailAdd =emails.getString(emails.getColumnIndex(Email.DATA));
   		}
   	
   	  	
   		emails.close();
   	
   		//�õ���ϵ��ͷ��ID
   		Long photoid = cursor.getLong(cursor.getColumnIndex(Phone.PHOTO_ID));
    	
   		//�õ���ϵ��ͷ��Bitamp
//   		Bitmap contactPhoto = null;
   		Long contactid = Long.valueOf(contactId);
    	
   		//photoid ����0 ��ʾ��ϵ����ͷ�� ���û�и���������ͷ�������һ��Ĭ�ϵ�
   		
   		if(photoid > 0 ) {    		 
   			//Log.d("ContactsContract.Contacts.CONTENT_URI = ", String.valueOf(ContactsContract.Contacts.CONTENT_URI));
   			
   			Uri  uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactid);
   			InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(), uri);
   		//	photo = BitmapFactory.decodeStream(input);
   			Date curDate = new Date(System.currentTimeMillis());//��ȡ��ǰʱ��
   			try {
				if (input != null) 
                {
					String imageFileName = imagePath + "/" + dateFormat.format(curDate) + String.valueOf((Math.random() * 100))+ ".jpg";
					Log.d("name" , imageFileName);
					File imageFile = new File(imageFileName);
					imageFile.createNewFile();
					OutputStream output = new BufferedOutputStream(new FileOutputStream(imageFile));
					
					InputStreamReader inputreader = new InputStreamReader(input);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    byte[] data = new byte[input.available()];
                  
                  	input.read(data);
                  	output.write(data);
                  	
                    Log.d("line", "wawa2");
                    output.flush();
                    input.close();
                    output.close();
                    Log.d("line", "wawa3");
                    insert_Contant(imageFileName, name, strPhoneNumber, emailAdd,"0",ringName,PY);
                }
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d("Ioception", "puhahaha");
				insert_Contant("null", name, strPhoneNumber, emailAdd,"0",ringName,PY);
				e.printStackTrace();
			}
   			//insert_Contant(String.valueOf(uri), name, strPhoneNumber, emailAdd,"0");
   		}
   		else {
   			Log.d("Ioception", "puha");
   		   // contactPhoto = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
   			insert_Contant("null", name, strPhoneNumber, emailAdd,"0",ringName,PY);
   		}
   	//	mContactsPhoto.add(contactPhoto);
  
       }   
       cursor.close();   
    }
   //��ѯ���ݿ������������ϵ�˲����浽arrayList������
    public void QueryAllList() {
    	allList.clear();
	    DatabaseHelper dbHelper1 = new DatabaseHelper(MainActivity.this,"test_mars_db");
		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
		Cursor cursor = db1.query("user", new String[]{"photoURI","name","groupp","star","phone"}, null, null, null, null, " name COLLATE LOCALIZED  ASC");
		while(cursor.moveToNext()){
			HashMap<String, Object> map = new HashMap<String, Object>();		
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("photoURI", cursor.getString(cursor.getColumnIndex("photoURI")));
			map.put("groupp", cursor.getString(cursor.getColumnIndex("groupp")));
			map.put("star", cursor.getString(cursor.getColumnIndex("star")));
			map.put("phone",cursor.getString(cursor.getColumnIndex("phone")));
			allList.add(map);
		}
		cursor.close();
		 if (db1 != null && db1.isOpen()) {
	            db1.close();
	     }

	     if (dbHelper1 != null) {
	        dbHelper1.close();
	     }
    }
    
  //��ѯ���ݿ������������ϵ�˲����浽arrayList������
    public  void QueryLoveList() {
	    DatabaseHelper dbHelper1 = new DatabaseHelper(MainActivity.this,"test_mars_db");
		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
		Cursor cursor = db1.query("user", new String[]{"photoURI","name","groupp","star"}, null, null, null, null, " name COLLATE LOCALIZED  ASC");
		loveList.clear();
		while(cursor.moveToNext()){
			if(cursor.getString(cursor.getColumnIndex("star")).equals("0"))
				continue;
			HashMap<String, Object> map = new HashMap<String, Object>();		
			map.put("name", cursor.getString(cursor.getColumnIndex("name")));
			map.put("photoURI", cursor.getString(cursor.getColumnIndex("photoURI")));
			map.put("position", cursor.getPosition());
			loveList.add(map);
		}
		cursor.close();
		 if (db1 != null && db1.isOpen()) {
	            db1.close();
	     }

	     if (dbHelper1 != null) {
	        dbHelper1.close();
	     }
    }
	
    
    //==================��ѯ�������ݣ�����name���鿴�Ƿ���ڴ���ϵ�ˣ�======================== 
    public String QueryByKey(String name) {
    	DatabaseHelper dbHelper1 = new DatabaseHelper(MainActivity.this,"test_mars_db");
    	if(name==null)
    	return null;
    	
		SQLiteDatabase db1 = dbHelper1.getReadableDatabase();
		Cursor cursor1 = db1.query("user", new String[]{"name"}, "name=?", new String[]{name}, null, null, null);
	    //*********************************
		//*********************************
		if(cursor1.getCount()!=0)
		{
			 cursor1.close();
			    db1.close();
			    if (db1 != null && db1.isOpen()) {
		            db1.close();
		        }

		       if (dbHelper1 != null) {
		         dbHelper1.close();
		       }
			return "abc";
		}
		else
		{
	    cursor1.close();
	    db1.close();
	    if (db1 != null && db1.isOpen()) {
            db1.close();
        }

       if (dbHelper1 != null) {
         dbHelper1.close();
        }
    	return null;
    	}
    }     
    
    
    //�����ֻ�ͨѶ¼��ȡ������Ϣ�����Լ������ݿ�
    public void insert_Contant(String photoURI,String name,String strPhoneNumber,String strEmailAdd,String star,String ringName,String PY) {
    	
    	//����õ����֡��绰���ڼ�ֵ�������ٲ��뵽�ҵ���ϵ���б���
       
    	//---------------����������-----------------
    	//����ContentValues����
		ContentValues values = new ContentValues();
		//��ö����в����ֵ�ԣ����м���������ֵ��ϣ�����뵽��һ�е�ֵ��ֵ��������ݿ⵱�е���������һ��
		values.put("photoURI",photoURI);
		values.put("name",name);
		values.put("phone",strPhoneNumber);		
		values.put("mail",strEmailAdd);
		values.put("groupp","");
		values.put("star", star);
		values.put("ringURI", ringName);
        values.put("PY",PY);
		DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this,"test_mars_db");
		SQLiteDatabase db = dbHelper.getWritableDatabase();
		//����insert�������Ϳ��Խ����ݲ��뵽���ݿ⵱��
		db.insert("user", null, values); 
		//dbHelper.close();
		//db.close();
	    if (db != null && db.isOpen()) {
            db.close();
        }

       if (dbHelper != null) {
         dbHelper.close();
       }
    }
}
