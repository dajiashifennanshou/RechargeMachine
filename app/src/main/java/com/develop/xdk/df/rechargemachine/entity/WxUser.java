package com.develop.xdk.df.rechargemachine.entity;

/**
 * 用户信息
 */
public class WxUser {
    private long autoid;
	private String name,lost,department,money,accountid,clientid,pdid,schoolName;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}



	public long getAutoid() {
		return autoid;
	}

	public void setAutoid(long autoid) {
		this.autoid = autoid;
	}

	public String getSchoolName() {
		return schoolName;
	}

	public void setSchoolName(String schoolName) {
		this.schoolName = schoolName;
	}

	public String getLost() {
		return lost;
	}

	public void setLost(String lost) {
		this.lost = lost;
	}

	public String getAccountid() {
		return accountid;
	}

	public void setAccountid(String accountid) {
		this.accountid = accountid;
	}

	public String getClientid() {
		return clientid;
	}

	public void setClientid(String clientid) {
		this.clientid = clientid;
	}

	public String getPdid() {
		return pdid;
	}

	public void setPdid(String pdid) {
		this.pdid = pdid;
	}

	public String getDepartment() {
		return department;
	}

	public void setDepartment(String department) {
		this.department = department;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}
	
}
