package com.longney.ui.entry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.longney.dslrpan.R;
import com.longney.ui.MainActivity;

public class RegisterSuccessActivity extends EntryBaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_success);
		initActionBar("注册成功");
		hideBackIcon();
		findViewById(R.id.btn_login_rightnow).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Utils.goActivity(ctx, EntryLoginActivity.class);
//				MainActivity.goMainActivityFromActivity(RegisterSuccessActivity.this);
				Intent intent = new Intent(RegisterSuccessActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
