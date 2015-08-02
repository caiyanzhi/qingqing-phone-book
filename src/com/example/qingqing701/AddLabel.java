package com.example.qingqing701;

import java.util.ArrayList;

import com.example.contactersss.R;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AddLabel extends Activity{

	private Button bt_back,bt_save,editOk;
	TextView text,labelText2;
	
	EditText labelText;//编辑要添加的标签
	private ArrayList<String> labels;//存放历史标签
	private LinearLayout addSets,historyLabels;//要添加的标签的布局,已有的标签的布局
	private String name;//存放该标签对应的名字
	private String addedLabels;//存放所有标签
	final private int MAXADDEDLABELS = 200; 
	private View addedLabelsList[] = new View[MAXADDEDLABELS];//用于存放想添加的标签的itemView
	private TextView textView[] = new TextView[MAXADDEDLABELS];//itemView中的布局
	private Button button[] = new Button[MAXADDEDLABELS];//删除用的buttob
	private View hostoricLabelsList[] = new View[MAXADDEDLABELS];//用于存放历史标签的itemView
	private TextView textView2[] = new TextView[MAXADDEDLABELS];//itemView中的布局
	private int count = 0;//记录已添加的label的数量
	@Override
	protected void onCreate(Bundle save){
		super.onCreate(save);
		setContentView(R.layout.addlabel);
		initView();
	}
	
	void initView(){
	
		editOk = (Button)findViewById(R.id.ok);
		editOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(labelText.getText().toString().contains(" "))
				{
					Toast.makeText(AddLabel.this,"标签不能包含空格呀( ⊙ o ⊙ )！",Toast.LENGTH_SHORT).show();
					return;
				}
				String n = labelText.getText().toString();
				if(n.equals("")){
					return;
				}
				else if(labelsSets.contains(n)){
					Toast.makeText(AddLabel.this,"你已经选择了该标签呀( ⊙ o ⊙ )！",Toast.LENGTH_SHORT).show();
					return;
				}
				else if(!HistoryLabel.isLabelExist(AddLabel.this, n))
				{	
					HistoryLabel dbHelper2 = new HistoryLabel(AddLabel.this, "test_mars_db1");
		            SQLiteDatabase db2 = dbHelper2.getReadableDatabase();
		            ContentValues values2 = new ContentValues();
		            values2.put("label",n);
		 
		            db2.insert("historyLabel", null, values2);
		            if (dbHelper2 != null) {
    	               dbHelper2.close();
		            }
	    	        
	   				labels.add(n);
	   				AddHistroyLabels(historyLabalCount);
	   				historyLabalCount++;
	    	           
				}
				addedLabelsList[count] = LayoutInflater.from(AddLabel.this).inflate(R.layout.deletelabel, null);
				
				textView[count] = (TextView)addedLabelsList[count].findViewById(R.id.text);
			    textView[count].setText(n);
				addSets.addView(addedLabelsList[count]);
				addedLabelsList[count].setTag(n);
				labelsSets.add(n);
				button[count] = (Button)addedLabelsList[count].findViewById(R.id.deletebt);
				button[count].setTag(count);
				button[count].setOnClickListener(new OnClickListener() {			
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int i = (Integer)v.getTag();
						addSets.removeView(addedLabelsList[i]);
						labelsSets.remove((String)addedLabelsList[i].getTag());
					}
				});
				count++;
				labelText.setText("");
			}
		});
		labelText = (EditText)findViewById(R.id.labelText);
		
		labelText2 = (TextView)findViewById(R.id.labelText2);
		labelText2.setFocusableInTouchMode(true);
		labelText2.requestFocus();
		
		bt_save = (Button)findViewById(R.id.save);
		
		bt_save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 addedLabels = "";
				
					//System.out.println(labelText.getText().toString());
					//System.out.println(addedLabels);
				    int size = labelsSets.size();
					for(int i=0;i<size;i++)
					{
						
						if(i!=0)
							addedLabels+=' ';
						
						addedLabels+=labelsSets.get(i);
					}
					
		        Intent intent;
		        String is = (String)getIntent().getCharSequenceExtra("is");
		      
		        if(is.equals("false"))
		        {
		        	intent = new Intent(AddLabel.this,AddContacter.class);
		        	  intent.putExtra("label",addedLabels);
		        	   setResult(RESULT_OK, intent);
		        	   finish();
		        }
		        else 
		        {	
		        	intent = new Intent(AddLabel.this,EditContacter.class);
		             intent.putExtra("label",addedLabels);
		            //AddLabel.this.startActivity(intent); 
		             setResult(RESULT_OK, intent);
		             finish();
		        }
			}
		});
		addedLabels = (String)getIntent().getCharSequenceExtra("label");
		addSets = (LinearLayout)findViewById(R.id.addSets);
		addSets.requestFocus();
		initAddedLabels();
		
		historyLabels = (LinearLayout)findViewById(R.id.labelList);
		initHistroyLabels();
		
        //historyLabels.setAdapter(new MyBaseAdaper());
			
	    	
		if(((String)getIntent().getCharSequenceExtra("name")).equals(""))
			name="noName";
			else
			name = (String)getIntent().getCharSequenceExtra("name");
		bt_back = (Button)findViewById(R.id.back);
		bt_back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(AddLabel.this,AddContacter.class);
	        	  intent.putExtra("label",addedLabels);
	        	   setResult(RESULT_OK, intent);
	        	   finish();
			}
		});
		
		text= (TextView)findViewById(R.id.text);
		addSets.requestFocus();
	}

	@Override
	 public void onBackPressed() {
	 // TODO Auto-generated method stub
		Intent intent = new Intent(AddLabel.this,AddContacter.class);
  	    intent.putExtra("label",addedLabels);
  	    setResult(RESULT_OK, intent);
  	    finish();
	    super.onBackPressed(); 
	}

	private int historyLabalCount = 0;
	//初始化历史标签的布局
	private void initHistroyLabels() {
		// TODO Auto-generated method stub
		labels = HistoryLabel.QueryLabels(AddLabel.this);
		
		Log.d("历史标签数 = ", String.valueOf(labels.size()));
		
		historyLabalCount = labels.size();
		for(int i = 0; i < historyLabalCount; i++){
			AddHistroyLabels(i);
		}
	}
	
	private void AddHistroyLabels(int i) {
		// TODO Auto-generated method stub
		hostoricLabelsList[i] = LayoutInflater.from(AddLabel.this).inflate(R.layout.historylabel, null);
		textView2[i] = (TextView)hostoricLabelsList[i].findViewById(R.id.text);
		textView2[i].setText(labels.get(i));
		historyLabels.addView(hostoricLabelsList[i]);
		hostoricLabelsList[i].setTag(labels.get(i));
		hostoricLabelsList[i].setOnClickListener(new OnClickListener() {		
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String n = (String)v.getTag();
				System.out.println(n);
				
				if(labelsSets.contains(n))
				{
					    //System.out.println("*****************************"+n);
					    return;
				}
				else{
					addedLabelsList[count] = LayoutInflater.from(AddLabel.this).inflate(R.layout.deletelabel, null);
					textView[count] = (TextView)addedLabelsList[count].findViewById(R.id.text);
				    textView[count].setText(n);
					addSets.addView(addedLabelsList[count]);
					addedLabelsList[count].setTag(n);
					labelsSets.add(n);
					button[count] = (Button)addedLabelsList[count].findViewById(R.id.deletebt);
					button[count].setTag(count);
					button[count].setOnClickListener(new OnClickListener() {			
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							int i = (Integer)v.getTag();
							addSets.removeView(addedLabelsList[i]);
							labelsSets.remove((String)addedLabelsList[i].getTag());
						}
					});
					count++;
				}
			}
		});
	}

	ArrayList<String> labelsSets = new ArrayList<String>();
	//初始化已添加标签的布局
	private void initAddedLabels(){
		if(addedLabels.equals(""))
			return;
		
		labelsSets = getLabels(addedLabels);
		
		int size = labelsSets.size();
		for(int i = 0; i < size; i++){
			addedLabelsList[i] = LayoutInflater.from(AddLabel.this).inflate(R.layout.deletelabel, null);
			textView[i] = (TextView)addedLabelsList[i].findViewById(R.id.text);
			
			textView[i].setText(labelsSets.get(i));
			addSets.addView(addedLabelsList[i]);
			addedLabelsList[i].setTag(labelsSets.get(i));
			button[i] = (Button)addedLabelsList[i].findViewById(R.id.deletebt);
			button[i].setTag(i);
			button[i].setOnClickListener(new OnClickListener() {			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int i = (Integer)v.getTag();
					addSets.removeView(addedLabelsList[i]);
					labelsSets.remove((String)addedLabelsList[i].getTag());
				}
			});
		}
	}
	
	//将传过来的标签初始化
	private ArrayList<String> getLabels(String tmplabels) {
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
			count++;
		}
		return tmp;
	}
		
}
