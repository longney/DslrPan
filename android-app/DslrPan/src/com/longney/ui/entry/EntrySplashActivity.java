package com.longney.ui.entry;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.avos.avoscloud.AVUser;
import com.longney.dslrpan.R;
import com.longney.service.UserService;
import com.longney.ui.base.BaseActivity;
import com.longney.ui.base.MyApplication;
import com.longney.utils.Utils;

public class EntrySplashActivity extends BaseActivity {
  public static final int SPLASH_DURATION = 2000;
  private static final int GO_MAIN_MSG = 1;
  private static final int GO_LOGIN_MSG = 2;

  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      switch (msg.what) {
        case GO_MAIN_MSG:
          MyApplication.goMainActivityFromActivity(EntrySplashActivity.this);
          finish();
          break;
        case GO_LOGIN_MSG:
        	SharedPreferences setting = getSharedPreferences("setting", 0);  
		    Boolean user_first = setting.getBoolean("FIRST",true);  
		    if(user_first){ //引导界面
//		    	 setting.edit().putBoolean("FIRST", false).commit();
//		    	 Intent intent = new Intent(EntrySplashActivity.this,FirstUseInterface.class);
//				 startActivity(intent);
		    	Utils.goActivity(ctx, EntryLoginActivity.class);
		     }else{ 
		    	 Utils.goActivity(ctx, EntryLoginActivity.class);
		        
		    }  
		    finish();
	        break;
         
      }
    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    // TODO Auto-generated method stub
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    if (AVUser.getCurrentUser() != null) {
      UserService.updateUserInfo();
      handler.sendEmptyMessageDelayed(GO_MAIN_MSG, SPLASH_DURATION);
    } else {
      handler.sendEmptyMessageDelayed(GO_LOGIN_MSG, SPLASH_DURATION);
    }
  }
}
