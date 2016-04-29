package com.longney.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.longney.dslrpan.R;
import com.longney.service.UserService;
import com.longney.ui.base.BaseActivity;
import com.longney.ui.entry.EntryLoginActivity;

public class MainActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
//		findViewById(R.id.textView1).setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				
//			}
//		});
		String info = AVUser.getCurrentUser().getMobilePhoneNumber();
		((TextView)findViewById(R.id.textView1)).setText("已登录，用户名：" + info);
		
		findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AVUser.logOut();
				Intent intent = new Intent(MainActivity.this, EntryLoginActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
	
}
