package com.longney.ui.base;


import com.avos.avoscloud.AVException;
import com.longney.dslrpan.R;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

public class BaseActivity extends FragmentActivity {
	public static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
	protected Context ctx;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ctx = this;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			super.onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	protected void initActionBar() {
		initActionBar(null, false, true);
	}

	protected void initActionBar(String title, boolean displayuserlog,
			boolean displayhome) {
		ActionBar actionBar = getActionBar();
		if (actionBar == null) {
			throw new NullPointerException("action bar is null");
		}
		if (title != null) {
			actionBar.setTitle(title);
		}
		actionBar.setDisplayUseLogoEnabled(displayuserlog);
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowHomeEnabled(displayhome);
	}

	protected void initActionBar(String title) {
		ActionBar actionBar = getActionBar();
		if (actionBar == null) {
			throw new NullPointerException("action bar is null");
		}
		if (title != null) {
			actionBar.setTitle(title);
		}
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	protected void initActionBar(int id) {
		initActionBar(getString(id), false, true);
	}

	protected void updateTitle(int id) {
		ActionBar actionBar = getActionBar();
		if (actionBar == null) {
			throw new NullPointerException("action bar is null");
		}
		String title = getString(id);
		if (title != null) {
			actionBar.setTitle(title);
		}
	}

	protected void updateTitle(String title) {
		ActionBar actionBar = getActionBar();
		if (actionBar == null) {
			throw new NullPointerException("action bar is null");
		}
		if (title != null) {
			actionBar.setTitle(title);
		}
	}

	protected void hideBackIcon() {
		ActionBar actionBar = getActionBar();
		if (actionBar == null) {
			throw new NullPointerException("action bar is null");
		}
		actionBar.setDisplayHomeAsUpEnabled(false);
	}

	protected void toast(String str) {
		Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
	}

	protected void toast(int id) {
		Toast.makeText(this, id, Toast.LENGTH_SHORT).show();
	}
	
	protected ProgressDialog showSpinnerDialog() {
		// activity = modifyDialogContext(activity);
		ProgressDialog dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(true);
		dialog.setMessage(getString(R.string.chat_utils_hardLoading));
		if (!isFinishing()) {
			dialog.show();
		}
		return dialog;
	}

	public boolean filterException(AVException e) {
		if (e != null) {
//			toast(e.getMessage());
			int errorCode = e.getCode();
			Log.d("BaseActivity",e.getMessage() + "   filterException" + e.getCode());
			switch (errorCode) {
			case 200:
				toast("没有提供用户名，或者用户名为空");
				break;
			case 201:
				toast("没有提供密码，或者密码为空");
				break;
			case 202:
				toast("用户名已经被占用");
				break;
			case 203:
				toast("电子邮箱地址已经被占用");
				break;
			case 204:
				toast("没有提供电子邮箱地址");
				break;
			case 205:
				toast("找不到电子邮箱地址对应的用户");
				break;
			case 206:
				toast("无法修改用户信息");
				break;
			case 207:
				toast("无只能通过注册创建用户，不允许第三方登录");
				break;
			case 208:
				toast("此第三方帐号已经绑定到一个用户，不可绑定到其他用户");
				break;
			case 210:
				toast(" 用户名和密码不匹配");
				break;
			case 211:
				toast("该用户未注册");
				break;
			case 212:
				toast(" 请提供手机号码");
				break;
			case 213:
				toast(" 手机号码对应的用户不存在");
				break;
			case 214:
				toast(" 手机号码已经被注册");
				break;
			case 215:
				toast(" 未验证的手机号码");
				break;
			case 603:
				toast("无效的短信验证码");
				break;
			default:
				toast("操作请求失败,请重试");
				break;
			}
			return false;
		} else {
			return true;
		}
	}
}
