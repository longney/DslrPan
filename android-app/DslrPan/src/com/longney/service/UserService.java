package com.longney.service;

import java.io.IOException;

import android.text.TextUtils;
import android.widget.ImageView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.FollowCallback;
import com.avos.avoscloud.SaveCallback;
import com.longney.model.avobject.MyUser;
import com.longney.model.avobject.User;
import com.longney.utils.Logger;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * Created by lzw on 14-9-15.
 */
public class UserService {
  public static final int ORDER_UPDATED_AT = 1;
  public static final int ORDER_DISTANCE = 0;
 
  public static MyUser findUser(String id) throws AVException {
    AVQuery<MyUser> q = AVObject.getQuery(MyUser.class);
    q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
    return q.get(id);
  }

  public static void findUserByPhoneNumber(String phoneNumber, FindCallback<MyUser> findCallback){
	  AVQuery<MyUser> q = AVObject.getQuery(MyUser.class);
	   q.setCachePolicy(AVQuery.CachePolicy.NETWORK_ELSE_CACHE);
	   q.whereEqualTo("mobilePhoneNumber", phoneNumber);
	   q.findInBackground(findCallback);
  }
  

  public static void saveSex(User.Gender gender, SaveCallback saveCallback) {
	MyUser user = AVUser.getCurrentUser(MyUser.class);
    User.setGender(user, gender);
    user.saveInBackground(saveCallback);
  }

  public static MyUser signUp(String name, String password) throws AVException {
	MyUser user = new MyUser();
    user.setMobilePhoneNumber(name);
    user.setUsername(name);
    user.setPassword(password);
    user.signUp();
    return user;
  }

  public static void saveAvatar(String path) throws IOException, AVException {
	MyUser user = AVUser.getCurrentUser(MyUser.class);
    final AVFile file = AVFile.withAbsoluteLocalPath(user.getUsername(), path);
    file.save();
    user.put(User.AVATAR, file);

    user.save();
    user.fetch();
    
  }
  
	public static void saveAvatar(String path, final SaveCallback callBack) throws IOException, AVException {
		final MyUser user = AVUser.getCurrentUser(MyUser.class);
		final AVFile file = AVFile.withAbsoluteLocalPath(user.getUsername(), path);
		file.saveInBackground(new SaveCallback() {
			@Override
			public void done(AVException arg0) {
				user.put(User.AVATAR, file.getUrl());
				user.saveInBackground(callBack);
				try {
					user.fetch();
				} catch (AVException e) {
					e.printStackTrace();
				}
			}
		});

  }

  public static void updateUserInfo() {
	MyUser user = AVUser.getCurrentUser(MyUser.class);
    if (user != null) {
      AVInstallation installation = AVInstallation.getCurrentInstallation();
      if (installation != null) {
        user.put(User.INSTALLATION, installation);
        user.put("installationID",AVInstallation.getCurrentInstallation().getInstallationId()); //保存设备号
        user.saveInBackground();
      }
    }
  }

  public static void updateUserInstallation(){
	  String userInstallationid = AVInstallation.getCurrentInstallation().getInstallationId();
	  MyUser user = AVUser.getCurrentUser(MyUser.class);
	  user.put("installationID", userInstallationid);
	  user.saveInBackground(new SaveCallback() {
          @Override
          public void done(AVException e) {
            if (e != null) {
              e.printStackTrace();
              Logger.v("aasanderlee " + "updateUserInstallation e.printStackTrace():"+e.toString());
            } else {
              Logger.v("aasanderlee " + "updateUserInstallation sucess");
            }
          }
        });
  }
  public static String loadInstallationIDWithUserID(String userid) {
	  String ret = null;
	  try {
		MyUser user = findUser(userid);
		ret = user.getInstallationID();
	} catch (AVException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
		Logger.v("aasanderlee " + "loadInstallationIDWithUserID e.printStackTrace():"+e.toString());
	}
	  return ret;
  }
  public static void addFriend(String friendId, final SaveCallback saveCallback) {
	MyUser user = AVUser.getCurrentUser(MyUser.class);
    user.followInBackground(friendId, new FollowCallback() {
      @Override
      public void done(AVObject object, AVException e) {
        saveCallback.done(e);
      }
    });
  }
  
  public static void addSelfBeOthersFriend(String friendId, final SaveCallback saveCallback) {
	MyUser user = null;
	try {
		user = UserService.findUser(friendId);
	} catch (AVException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	}
	if(user != null){
			user.followInBackground(AVUser.getCurrentUser().getObjectId(), new FollowCallback() {
				@Override
				public void done(AVObject object, AVException e) {
					saveCallback.done(e);
				}
			});
	}
  }

  public static void removeFriend(String friendId, final SaveCallback saveCallback) {
	MyUser user = AVUser.getCurrentUser(MyUser.class);
    user.unfollowInBackground(friendId, new FollowCallback() {
      @Override
      public void done(AVObject object, AVException e) {
        saveCallback.done(e);
      }
    });
  }
  
}
