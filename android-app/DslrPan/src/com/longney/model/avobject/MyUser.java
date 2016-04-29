package com.longney.model.avobject;

import com.avos.avoscloud.AVUser;

public class MyUser extends AVUser {
	public void setInstallationID(String installationid) {
		this.put("installationID", installationid);
	}

	public String getInstallationID() {
		return this.getString("installationID");
	}
	
}
