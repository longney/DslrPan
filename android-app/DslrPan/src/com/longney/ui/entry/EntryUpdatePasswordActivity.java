//package com.longney.ui.entry;
//
//import android.app.ProgressDialog;
//import android.os.Bundle;
//import android.text.TextUtils;
//import android.view.View;
//import android.widget.EditText;
//
//import com.avos.avoscloud.AVException;
//import com.avos.avoscloud.AVUser;
//import com.avos.avoscloud.LogInCallback;
//import com.avos.avoscloud.UpdatePasswordCallback;
//import com.avoscloud.chat.service.UserService;
//import com.avoscloud.chat.ui.MainActivity;
//import com.avoscloud.chat.ui.boyue.data.MyUser;
//import com.avoscloud.leanchatlib.utils.Logger;
//import com.boyue.chat.R;
//import com.longney.model.avobject.User;
//import com.longney.utils.Utils;
//
//public class EntryUpdatePasswordActivity extends EntryBaseActivity {
//	EditText oldPasswordEdit;
//	EditText newPasswordEdit;
//	EditText ensureNewPasswordEdit;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		// TODO Auto-generated method stub
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.entry_update_password_activity);
//		initActionBar("修改密码");
//		oldPasswordEdit = (EditText) findViewById(R.id.old_password_edit);
//		newPasswordEdit = (EditText) findViewById(R.id.new_passwordEdit);
//		ensureNewPasswordEdit = (EditText) findViewById(R.id.ensure_new_PasswordEdit);
//		findViewById(R.id.btn_update_password).setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				// Utils.goActivity(ctx, EntryLoginActivity.class);
//				updatePassword();
//			}
//		});
//	}
//
//	private void updatePassword() {
//		final String oldPassword = oldPasswordEdit.getText().toString();
//		final String newPassword = newPasswordEdit.getText().toString();
//		final String ensureNewPassword = ensureNewPasswordEdit.getText().toString();
//
//		if (TextUtils.isEmpty(oldPassword)) {
//			Utils.toast("请输入正确的旧密码");
//			return;
//		}
//
//		if (TextUtils.isEmpty(newPassword)) {
//			Utils.toast(R.string.password_can_not_null);
//			return;
//		}
//		if (!ensureNewPassword.equals(newPassword)) {
//			Utils.toast(R.string.password_not_consistent);
//			return;
//		}
//		if (newPassword.length() < 6) {
//			Utils.toast("请输入6位以上新密码");
//			return;
//		}
//		final ProgressDialog dialog = Utils.showSpinnerDialog(this);
//		String name = AVUser.getCurrentUser(MyUser.class).getUsername();
//		AVUser.logInInBackground(name, oldPassword, new LogInCallback<AVUser>() {
//			@Override
//			public void done(AVUser avUser, AVException e) {
//				if (e == null) {
//					avUser.updatePasswordInBackground(oldPassword, newPassword,
//							new UpdatePasswordCallback() {
//
//								@Override
//								public void done(AVException e) {
//									dialog.dismiss();
//									if (e == null) {
//										toast("修改成功");
//										finish();
//									} else {
//										toast("修改失败");
//										Logger.d(e.getMessage());
//									}
//								}
//							});
//				} else {
//					dialog.dismiss();
//					Utils.toast("旧密码验证失败");
//				}
//			}
//		});
//	}
//}
