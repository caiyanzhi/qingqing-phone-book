package com.example.qingqing701;


import com.example.contactersss.R;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LogInDialog extends Dialog{
	public LogInDialog(Context context){
		super(context);
	}
	
	public LogInDialog(Context context, int theme){
		super(context, theme);
	}
	
	public static class Builder{
		private Context context;
		private String title;
		private String message;
		private TextView messageView;
		private EditText editUser;
		private EditText editPassword;
		private String signupButtonText;
		private String loginButtonText;
		private String negativeButtonText;
		private View contentView;
		private DialogInterface.OnClickListener signupButtonClickListener;
		private DialogInterface.OnClickListener loginButtonClickListener;
		private DialogInterface.OnClickListener negativeButtonClickListener;
		private LogInDialog dialog;
		
		public Builder(Context context){
			this.context = context;
		}
		public Builder setMessage(String message) {
			this.message = message;
			return this;
		}
		/**
		 * Set the Dialog message from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setMessage(int message) {
			this.message = (String) context.getText(message);
			return this;
		}
		
		
		/**
		 * Set the Dialog title from resource
		 * 
		 * @param title
		 * @return
		 */
		public Builder setTitle(int title) {
			this.title = (String) context.getText(title);
			return this;
		}

		/**
		 * Set the Dialog title from String
		 * 
		 * @param title
		 * @return
		 */
		public EditText getUser(){
			return this.editUser;
		}
		
		public TextView getMessageView()
		{
			return this.messageView;
		}
		
		public EditText getPassword(){
			return this.editPassword;
		}
		public Builder setPassword(EditText editPassword){
			this.editPassword = editPassword;
			return this;
		}
		
		public Builder setTitle(String title) {
			this.title = title;
			return this;
		}
		
		public Builder setContentView(View v) {
			this.contentView = v;
			return this;
		}
		
		/**
		 * Set the sign up button resource and it's listener
		 * 
		 * @param sign up ButtonText
		 * @return
		 */
		public Builder setsignupButton(int signupButtonText,
				DialogInterface.OnClickListener listener) {
			this.signupButtonText = (String) context
					.getText(signupButtonText);
			this.signupButtonClickListener = listener;
			return this;
		}
		
		public Builder setsignupButton(String signupButtonText,
				DialogInterface.OnClickListener listener) {
			this.signupButtonText = signupButtonText;
			this.signupButtonClickListener = listener;
			return this;
		}
		public Builder setloginButton(int loginButtonText,
				DialogInterface.OnClickListener listener) {
			this.loginButtonText = (String) context
					.getText(loginButtonText);
			this.loginButtonClickListener = listener;
			return this;
		}
		public Builder setloginButton(String loginButtonText,
				DialogInterface.OnClickListener listener) {
			this.loginButtonText = loginButtonText;
			this.loginButtonClickListener = listener;
			return this;
		}
		
		public Builder setNegativeButton(int negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = (String) context
					.getText(negativeButtonText);
			this.negativeButtonClickListener = listener;
			return this;
		}

		public Builder setNegativeButton(String negativeButtonText,
				DialogInterface.OnClickListener listener) {
			this.negativeButtonText = negativeButtonText;
			this.negativeButtonClickListener = listener;
			return this;
		}
		
		public LogInDialog create(){
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog = new LogInDialog(context,R.style.Dialog);
			View layout = inflater.inflate(R.layout.dialog_login, null);
			dialog.addContentView(layout, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			// set the dialog title
			((TextView) layout.findViewById(R.id.title)).setText(title);
			editUser = (EditText)layout.findViewById(R.id.user);

			editPassword =  (EditText)layout.findViewById(R.id.password);
			messageView = (TextView)layout.findViewById(R.id.returnmessage);
			
			// set the confirm button
			if (signupButtonText != null) {
				((Button) layout.findViewById(R.id.signupButton))
						.setText(signupButtonText);
				if (signupButtonClickListener != null) {
					((Button) layout.findViewById(R.id.signupButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									signupButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_POSITIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.signupButton).setVisibility(
						View.GONE);
			}
			if (loginButtonText != null) {
				((Button) layout.findViewById(R.id.loginButton))
						.setText(loginButtonText);
				if (loginButtonClickListener != null) {
					((Button) layout.findViewById(R.id.loginButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									loginButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEUTRAL);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.loginButton).setVisibility(
						View.GONE);
			}
			// set the cancel button
			if (negativeButtonText != null) {
				((Button) layout.findViewById(R.id.negativeButton))
						.setText(negativeButtonText);
				if (negativeButtonClickListener != null) {
					((Button) layout.findViewById(R.id.negativeButton))
							.setOnClickListener(new View.OnClickListener() {
								public void onClick(View v) {
									negativeButtonClickListener.onClick(dialog,
											DialogInterface.BUTTON_NEGATIVE);
								}
							});
				}
			} else {
				// if no confirm button just set the visibility to GONE
				layout.findViewById(R.id.negativeButton).setVisibility(
						View.GONE);
			}
			// set the content message
			if (message != null) {
				((TextView) layout.findViewById(R.id.returnmessage)).setText(message);
			} else if (contentView != null) {
				// if no message set
				// add the contentView to the dialog body
				((LinearLayout) layout.findViewById(R.id.returnmessage))
						.removeAllViews();
				((LinearLayout) layout.findViewById(R.id.returnmessage)).addView(
						contentView, new LayoutParams(
								LayoutParams.WRAP_CONTENT,
								LayoutParams.WRAP_CONTENT));
			}
			
			dialog.setContentView(layout);
			return dialog;
		}
		
		LogInDialog getDialog()
		{
			return dialog;
		}
	}
}
