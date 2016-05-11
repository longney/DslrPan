package com.longney.ui.entry;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestMobileCodeCallback;
import com.avos.avoscloud.UpdatePasswordCallback;
import com.longney.dslrpan.R;
import com.longney.utils.Logger;
import com.longney.utils.Utils;

public class RetrievePasswordActivity extends EntryBaseActivity implements View.OnClickListener {
	EditText userphoneEdit, smsCodeEdit;
	Button nextStepBtn;
	Button requestSmsBtn;
	Button completeBtn;
	
	EditText resetPasswordEdit, ensureResetPasswordEdit;

	private TimeCount time;
	private AVUser user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_retrieve_password);
		initActionBar("找回密码");
		time = new TimeCount(60000, 1000);
		userphoneEdit = (EditText) findViewById(R.id.userphoneEdit);
		smsCodeEdit = (EditText) findViewById(R.id.sms_code);
		resetPasswordEdit = (EditText) findViewById(R.id.resetPasswordEdit);
		ensureResetPasswordEdit = (EditText) findViewById(R.id.ensureResetPasswordEdit);
		requestSmsBtn = (Button) findViewById(R.id.request_sms_code_btn);
		nextStepBtn = (Button) findViewById(R.id.btn_next);
		completeBtn = (Button) findViewById(R.id.btn_complete);
		completeBtn.setOnClickListener(this);
		nextStepBtn.setOnClickListener(this);
		requestSmsBtn.setOnClickListener(this);
	}

	private void requestSmsCode() {
		final String phoneNum = userphoneEdit.getText().toString();
		AVUser.requestPasswordResetBySmsCodeInBackground(phoneNum, new RequestMobileCodeCallback() {
			
			@Override
			public void done(AVException e) {
				if(e == null){
					toast("已发送");
					Logger.d("发送成功");
				}else {
					Logger.d("error" + e.getMessage());
				}
			}
		});
		time.start();
	}

	private void goResetPasswordActivity() {
		String smsCode = smsCodeEdit.getText().toString();
		if( TextUtils.isEmpty(smsCode)){
			toast("请输入6位数字验证码");
			return;
		}
		if(smsCode.length() != 6){
			toast("请输入6位数字验证码");
			return;
		}
		Intent intent = new Intent(this, EntryResetPasswordActivity.class);
		intent.putExtra("smsCode", smsCode);
		startActivity(intent);
//		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
//			verifySMSCode();
			goResetPasswordActivity();
			break;
		case R.id.request_sms_code_btn:
			requestSmsCode();
			break;

		case R.id.btn_complete:
			resetPassword();
			break;
		default:
			break;
		}
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
	
	private void resetPassword() {
		final String password = resetPasswordEdit.getText().toString();
		String againPassword = ensureResetPasswordEdit.getText().toString();

		String smsCode = smsCodeEdit.getText().toString();
		if (TextUtils.isEmpty(smsCode)) {
			Utils.toast("请输入6位数子验证码");
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
		Logger.d("smsCOde:" + smsCode);
		final ProgressDialog dialog = Utils.showSpinnerDialog(this);
		AVUser.resetPasswordBySmsCodeInBackground(smsCode, password, new UpdatePasswordCallback() {
			@Override
			public void done(AVException e) {
				dialog.dismiss();
				if (filterException(e)) {
					Utils.toast("密码更改成功");
					// 密码更改成功了！
					Utils.goActivityAndFinish(RetrievePasswordActivity.this, EntryLoginActivity.class);
				}
			}
		});
	}

}
