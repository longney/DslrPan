package com.longney.ui.entry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVMobilePhoneVerifyCallback;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.SignUpCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.longney.dslrpan.R;
import com.longney.model.avobject.MyUser;
import com.longney.service.PreferenceMap;
import com.longney.service.UserService;
import com.longney.ui.base.MyApplication;
import com.longney.utils.Logger;
import com.longney.utils.Utils;

public class EntryRegisterActivity extends EntryBaseActivity implements View.OnClickListener {
	View registerButton;
	EditText userphoneEdit, passwordEdit, ensurePasswordEdit, smsCodeEdit;
	Button requestSmsBtn;
	CheckBox acceptBox;
	TextView serviceTextView;// 定义了服务条款TextView

	private MyUser mUser = new MyUser();
	private boolean mGetSmsCode = false;
	private TimeCount time;

	// RadioGroup sexRadio;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		findView();
		time = new TimeCount(60000, 1000);
		initActionBar(R.string.register);
	}

	private void findView() {
		userphoneEdit = (EditText) findViewById(R.id.userphoneEdit);
		passwordEdit = (EditText) findViewById(R.id.passwordEdit);
		ensurePasswordEdit = (EditText) findViewById(R.id.ensurePasswordEdit);
		smsCodeEdit = (EditText) findViewById(R.id.sms_code);
		acceptBox = (CheckBox) findViewById(R.id.accept_provision_cb);
		serviceTextView = (TextView) findViewById(R.id.service_terms_textview);
		// serviceProvision = (TextView)
		// findViewById(R.id.service_terms_textview)
		serviceTextView.setOnClickListener(this);// 绑定点击事件
		registerButton = findViewById(R.id.btn_register);
		requestSmsBtn = (Button) findViewById(R.id.request_sms_code_btn);
		registerButton.setOnClickListener(this);
		requestSmsBtn.setOnClickListener(this);
		// sexRadio = (RadioGroup) findViewById(R.id.sexRadio);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_register:
			register();
			break;
		case R.id.request_sms_code_btn:
			if (mGetSmsCode) {
				requestSmsCode();
			} else {
				initRegister();
			}
			break;
		case R.id.service_terms_textview:// 响应点击服务条款事件
//			Intent intent = new Intent(EntryRegisterActivity.this, ServiceProvision.class);
//			startActivity(intent);
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			if (mGetSmsCode) {
				mUser.deleteInBackground();
				AVUser.logOut();
			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			if (mGetSmsCode) {
				mUser.deleteInBackground();
				AVUser.logOut();
			}
		}
		return super.onKeyDown(keyCode, event);

	}

	private void register() {
		final String phoneNum = userphoneEdit.getText().toString();
		final String smsCode = smsCodeEdit.getText().toString();
		final String password = passwordEdit.getText().toString();
		String againPassword = ensurePasswordEdit.getText().toString();

		if (TextUtils.isEmpty(phoneNum)) {
			Utils.toast(R.string.username_cannot_null);
			return;
		}

		if (TextUtils.isEmpty(smsCode)) {
			Utils.toast("请输入6位数字验证码");
			return;
		}

		if (TextUtils.isEmpty(password)) {
			Utils.toast(R.string.password_can_not_null);
			return;
		}
		if (!againPassword.equals(password)) {
			Utils.toast(R.string.password_not_consistent);
			return;
		}
		if (password.length() < 6) {
			Utils.toast("请输入6位以上密码");
			return;
		}

		if (!acceptBox.isChecked()) {
			toast("请阅读并同意相关服务条款");
			return;
		}
		Logger.d("smsCOde:" + smsCode);
		verifySmsCode(phoneNum, smsCode, password);
	}

	private void signup(final String phoneNum, final String password) {
//		mUser.setPassword(password);
		mUser.setInstallationID(AVInstallation.getCurrentInstallation().getInstallationId()); // 保存设备号
		mUser.updatePasswordInBackground("123456", password, new UpdatePasswordCallback() {
			
			@Override
			public void done(AVException e) {
				// TODO Auto-generated method stub
				if (filterException(e)) {
					Utils.toast(R.string.registerSucceed);
					Utils.goActivity(ctx, RegisterSuccessActivity.class);
					saveHistory(phoneNum);
					finish();
					UserService.updateUserInstallation();
					PreferenceMap preferenceMap = new PreferenceMap(EntryRegisterActivity.this);
//					preferenceMap.set(phoneNum);
					Logger.d("注册成功");
				}
			}
		});
	}

	private void requestSmsCode() {
		final String phoneNum = userphoneEdit.getText().toString();
		if (!Utils.isMobileNO(phoneNum)) {
			toast("请输入正确的手机号码");
			return;
		}
		AVUser.requestMobilePhoneVerifyInBackgroud(phoneNum, new RequestMobileCodeCallback() {
			
			@Override
			public void done(AVException e) {
				if (filterException(e)) {
					toast("已发送");
					smsCodeEdit.requestFocus();
					time.start();
				}
			}
		});
	}

	private void initRegister() {
		final String phoneNum = userphoneEdit.getText().toString();
		if (!Utils.isMobileNO(phoneNum)) {
			toast("请输入正确的手机号码");
			return;
		}
		final ProgressDialog dialog = showSpinnerDialog();
		mUser.setMobilePhoneNumber(phoneNum);
		mUser.setUsername(phoneNum);
		mUser.setPassword("123456");
		mUser.signUpInBackground(new SignUpCallback() {

			@Override
			public void done(AVException e) {
				dialog.dismiss();
				if (filterException(e)) {
					toast("已发送");
					smsCodeEdit.requestFocus();
					mGetSmsCode = true;
					time.start();
				}
			}
		});
	}

	private void verifySmsCode(final String phoneNum, final String smsCode, final String password) {
		mUser.verifyMobilePhoneInBackgroud(smsCode, new AVMobilePhoneVerifyCallback() {

			@Override
			public void done(AVException e) {
				if (filterException(e)) {
					signup(phoneNum, password);
				}
			}
		});
	}

	private void getVerificationCode() {
		final String phoneNum = userphoneEdit.getText().toString();
		AVUser.requestMobilePhoneVerifyInBackgroud(phoneNum, new RequestMobileCodeCallback() {

			@Override
			public void done(AVException e) {
				if (filterException(e)) {
					toast("已发送");
					time.start();
				}
			}
		});
	}

	class TimeCount extends CountDownTimer {
		public TimeCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
		}

		@Override
		public void onFinish() {// 计时完毕时触发
			requestSmsBtn.setText("重新验证");
			requestSmsBtn.setClickable(true);
		}

		@Override
		public void onTick(long millisUntilFinished) {// 计时过程显示
			requestSmsBtn.setClickable(false);
			requestSmsBtn.setText(millisUntilFinished / 1000 + "秒后重发");
		}
	}
	
	private void saveHistory(String userName) {  
		PreferenceMap preferenceMap = new PreferenceMap(this);
        String longhistory = preferenceMap.getHistoryLoginInfo();  
        StringBuilder sb = new StringBuilder(longhistory);  
        sb.insert(0, userName + ",");  
        preferenceMap.setHistoryLoginInfo(sb.toString());
	}
}
