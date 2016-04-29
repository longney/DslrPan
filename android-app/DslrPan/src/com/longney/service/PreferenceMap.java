package com.longney.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVUser;
import com.longney.dslrpan.R;
import com.longney.model.avobject.MyUser;
import com.longney.model.avobject.User;
import com.longney.ui.base.MyApplication;
import com.longney.utils.Logger;

/**
 * Created by lzw on 14-6-19.
 */
public class PreferenceMap {
  public static final String ADD_REQUEST_N = "addRequestN";
  public static final String LATITUDE = "latitude";
  public static final String LONGITUDE = "longitude";
  public static final String NOTIFY_WHEN_NEWS = "notifyWhenNews";
  public static final String VOICE_NOTIFY = "voiceNotify";
  public static final String VIBRATE_NOTIFY = "vibrateNotify";
  public static final String NEARBY_ORDER = "nearbyOrder";
  public static final String LAST_CONNECTED_USER_ID = "lastConnectedUserId"; //最后一次控制的UserId
  public static final String HISTORY_LOGIN_INFO = "historyLoginInfo"; //登录历史记录
  public static final String CURRENT_USER_AVATAR_URL = "current_user_avatar_url";
  public static boolean changeUser = false;
  //int addRequestN;
  //String latitude;
  //String longitude;
  public static PreferenceMap currentUserPreferenceMap;
  Context cxt;
  SharedPreferences pref;
  SharedPreferences.Editor editor;
  int nearbyOrder;

  public PreferenceMap(Context cxt) {
    this.cxt = cxt;
    pref = PreferenceManager.getDefaultSharedPreferences(cxt);
    editor = pref.edit();
    Logger.d("PreferenceMap init no specific user");
  }

  public PreferenceMap(Context cxt, String prefName) {
    this.cxt = cxt;
    pref = cxt.getSharedPreferences(prefName, Context.MODE_PRIVATE);
    editor = pref.edit();
  }

  public static PreferenceMap getCurUserPrefDao(Context ctx) {
    if (currentUserPreferenceMap == null) {
      currentUserPreferenceMap = new PreferenceMap(ctx, User.getCurrentUserId());
    }
    return currentUserPreferenceMap;
  }

  public static PreferenceMap getMyPrefDao(Context ctx) {
	MyUser user = AVUser.getCurrentUser(MyUser.class);
    if (user == null) {
      throw new RuntimeException("user is null");
    }
    return new PreferenceMap(ctx, user.getObjectId());
  }

  public int getAddRequestN() {
    return pref.getInt(ADD_REQUEST_N, 0);
  }

  public void setAddRequestN(int addRequestN) {
    editor.putInt(ADD_REQUEST_N, addRequestN).commit();
  }

  private String getLatitude() {
    return pref.getString(LATITUDE, null);
  }

  private void setLatitude(String latitude) {
    editor.putString(LATITUDE, latitude).commit();
  }

  private String getLongitude() {
    return pref.getString(LONGITUDE, null);
  }

  private void setLongitude(String longitude) {
    editor.putString(LONGITUDE, longitude).commit();
  }

  public AVGeoPoint getLocation() {
    String latitudeStr = getLatitude();
    String longitudeStr = getLongitude();
    if (latitudeStr == null || longitudeStr == null) {
      return null;
    }
    double latitude = Double.parseDouble(latitudeStr);
    double longitude = Double.parseDouble(longitudeStr);
    return new AVGeoPoint(latitude, longitude);
  }

  public void setLocation(AVGeoPoint location) {
    if (location == null) {
      throw new NullPointerException("location is null");
    }
    setLatitude(location.getLatitude() + "");
    setLongitude(location.getLongitude() + "");
  }

  public boolean isNotifyWhenNews() {
    return pref.getBoolean(NOTIFY_WHEN_NEWS,
    	MyApplication.mApp.getResources().getBoolean(R.bool.defaultNotifyWhenNews));
  }

  public void setNotifyWhenNews(boolean notifyWhenNews) {
    editor.putBoolean(NOTIFY_WHEN_NEWS, notifyWhenNews).commit();
  }

  boolean getBooleanByResId(int resId) {
    return MyApplication.mApp.getResources().getBoolean(resId);
  }

  public boolean isVoiceNotify() {
    return pref.getBoolean(VOICE_NOTIFY,
        getBooleanByResId(R.bool.defaultVoiceNotify));
  }

  public void setVoiceNotify(boolean voiceNotify) {
    editor.putBoolean(VOICE_NOTIFY, voiceNotify).commit();
  }

  public boolean isVibrateNotify() {
    return pref.getBoolean(VIBRATE_NOTIFY,
        getBooleanByResId(R.bool.defaultVibrateNotify));
  }

  public void setVibrateNotify(boolean vibrateNotify) {
    editor.putBoolean(VIBRATE_NOTIFY, vibrateNotify);
  }


  public int getNearbyOrder() {
    return pref.getInt(NEARBY_ORDER, UserService.ORDER_UPDATED_AT);
  }

  public void setNearbyOrder(int nearbyOrder) {
    editor.putInt(NEARBY_ORDER, nearbyOrder).commit();
  }
  
  public String getHistoryLoginInfo(){
	  return pref.getString(HISTORY_LOGIN_INFO, "");
  }
  
  public void setHistoryLoginInfo(String userName){
	  editor.putString(HISTORY_LOGIN_INFO, userName).commit();
  }
  
  public String getLastConnectedUserId(){
	  return pref.getString(LAST_CONNECTED_USER_ID, null);
  }
  
  public void setLastConnectedUserId(String userId){
//	  changeUser = true;
	  editor.putString(LAST_CONNECTED_USER_ID, userId).commit();
  }
  
  public String getCurrentUserAvatarUrl(){
	  return pref.getString(CURRENT_USER_AVATAR_URL, null);
  }
  
  public void setCurrentUserAvatarUrl(String avatarUrl){
	  editor.putString(CURRENT_USER_AVATAR_URL, avatarUrl).commit();
  }
}
