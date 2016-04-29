//package com.longney.ui.entry;
//
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//
//import com.avos.avoscloud.AVException;
//import com.avos.avoscloud.AVUser;
//import com.avos.avoscloud.UpdatePasswordCallback;
//import com.avoscloud.leanchatlib.utils.Logger;
//import com.boyue.chat.R;
//import com.longney.utils.Utils;
//
//public class EntryResetPasswordActivity extends EntryBaseActivity {
//	EditText resetPasswordEdit, ensureResetPasswordEdit;
//
//	// String mobilePhoneNumber = null;
//	Button resetPasswordBtn;
//	String smsCode;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.entry_reset_password_activity);
//		initActionBar("修改密码");
//		smsCode = getIntent().getStringExtra("smsCode");
//		resetPasswordEdit = (EditText) findViewById(R.id.resetPasswordEdit);
//		ensureResetPasswordEdit = (EditText) findViewById(R.id.ensureResetPasswordEdit);
//		resetPasswordBtn = (Button) findViewById(R.id.btn_complete);
//		resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				resetPassword();
//			}
//		});
//	}
//
//	private void resetPassword() {
//		final String password = resetPasswordEdit.getText().toString();
//		String againPassword = ensureResetPasswordEdit.getText().toString();
//
//		if (TextUtils.isEmpty(smsCode)) {
//			Utils.toast("修改失败，验证码错误");
//			finish();
//			return;
//		}
//
//		if (TextUtils.isEmpty(password)) {
//			Utils.toast(R.string.password_can_not_null);
//			return;
//		}
//		if (!againPassword.equals(password)) {
//			Utils.toast(R.string.password_not_consistent);
//			return;
//		}
//		if (password.length() < 6) {
//			Utils.toast("请输入6位以上密码");
//			return;
//		}
//		Logger.d("smsCOde:" + smsCode);
//		final ProgressDialog dialog = Utils.showSpinnerDialog(this);
//		AVUser.resetPasswordBySmsCodeInBackground(smsCode, password, new UpdatePasswordCallback() {
//			@Override
//			public void done(AVException e) {
//				dialog.dismiss();
//				if (filterException(e)) {
//					Utils.toast("密码更改成功");
//					// 密码更改成功了！
//					Utils.goActivityAndFinish(EntryResetPasswordActivity.this, EntryLoginActivity.class);
//				}
//			}
//		});
//	}
//
//	// private void queryUserByPhoneNum() {
//	// AVQuery<AVUser> query = AVUser.getQuery();
//	// query.whereEqualTo("mobilePhoneNumber", mobilePhoneNumber);
//	// query.findInBackground(new FindCallback<AVUser>() {
//	//
//	// @Override
//	// public void done(List<AVUser> users, AVException e) {
//	// if (e == null) {
//	// if (users != null && users.size() > 0) {
//	// user = users.get(0);
//	// requestSmsCode(phoneNum);
//	// }
//	// toast("此号码未注册");
//	// } else {
//	// Logger.d("查询用户失败");
//	// }
//	// }
//	// });
//	// }
//
//}
