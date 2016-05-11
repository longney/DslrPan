package com.longney.ui.entry;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.longney.dslrpan.R;
import com.longney.service.PreferenceMap;
import com.longney.service.UserService;
import com.longney.ui.MainActivity;
import com.longney.ui.base.MyApplication;
import com.longney.utils.Utils;
public class EntryLoginActivity extends EntryBaseActivity implements OnClickListener {
	private AutoCompleteTextView usernameEdit;
	private EditText passwordEdit;
	private Button loginBtn;
	private TextView registerBtn, forgetPasswordBtn;

	private PreferenceMap mPreferenceMap;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mPreferenceMap = new PreferenceMap(this);
		init();
	}

	private void init() {
		usernameEdit = (AutoCompleteTextView) findViewById(R.id.et_username);
		passwordEdit = (EditText) findViewById(R.id.et_password);
		loginBtn = (Button) findViewById(R.id.btn_login);
		registerBtn = (TextView) findViewById(R.id.btn_register);
		forgetPasswordBtn = (TextView) findViewById(R.id.btn_forget_password);
		initActionBar("登录");
		loginBtn.setOnClickListener(this);
		registerBtn.setOnClickListener(this);
		forgetPasswordBtn.setOnClickListener(this);
		initAutoComplete(usernameEdit);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			Utils.goActivity(ctx, EntryRegisterActivity.class);
			break;
		case R.id.btn_login:
			login();
			break;
		case R.id.btn_forget_password:
			Utils.goActivity(ctx, RetrievePasswordActivity.class);
			break;
		default:
			break;
		}
	}

	private void login() {
	    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);  
	    imm.hideSoftInputFromWindow(passwordEdit.getWindowToken(),0);  
		final String name = usernameEdit.getText().toString();
		final String password = passwordEdit.getText().toString();

		if (TextUtils.isEmpty(name)) {
			Utils.toast(R.string.username_cannot_null);
			return;
		}

		if (TextUtils.isEmpty(password)) {
			Utils.toast(R.string.password_can_not_null);
			return;
		}

		final ProgressDialog spinner = showSpinnerDialog();
		AVUser.logInInBackground(name, password, new LogInCallback<AVUser>() {
			@Override
			public void done(AVUser avUser, AVException e) {
				spinner.dismiss();
				if (filterException(e)) {
					PreferenceMap.getCurUserPrefDao(EntryLoginActivity.this).setCurrentUserAvatarUrl(avUser.getString("avatar"));
					UserService.updateUserInstallation();
					//UserService.updateUserLocation();//SHARE_APP_TAG
					saveHistory();
//					Intent intent = new Intent(EntryLoginActivity.this,MainActivity.class);
//					startActivity(intent);
					MyApplication.goMainActivityFromActivity(EntryLoginActivity.this);
				}
			}
		});
	}
	
	private void initAutoComplete(AutoCompleteTextView auto) {  
        String longhistory = mPreferenceMap.getHistoryLoginInfo();  
        String[]  hisArrays = longhistory.split(",");  
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,  
                android.R.layout.simple_dropdown_item_1line, hisArrays);  
        if(hisArrays.length > 5){  
            String[] newArrays = new String[5];  
            System.arraycopy(hisArrays, 0, newArrays, 0, 5);  
            adapter = new ArrayAdapter<String>(this,  
                    android.R.layout.simple_dropdown_item_1line, newArrays);  
        }  
        auto.setAdapter(adapter);  
        auto.setThreshold(1);   
        if(hisArrays.length > 0 && !TextUtils.isEmpty(hisArrays[0])){
        	auto.setText(hisArrays[0]);
        	passwordEdit.requestFocus();
        }
    }  
	
	private void saveHistory() {  
        String text = usernameEdit.getText().toString();
        String longhistory = mPreferenceMap.getHistoryLoginInfo();  
        StringBuilder sb = new StringBuilder(longhistory); 
        if (longhistory.contains(text + ",")) {  
        	int index = sb.indexOf(text + ",");
            sb.delete(index, index + text.length() + 1);
        }
        sb.insert(0, text + ",");  
        mPreferenceMap.setHistoryLoginInfo(sb.toString());
	}
}
