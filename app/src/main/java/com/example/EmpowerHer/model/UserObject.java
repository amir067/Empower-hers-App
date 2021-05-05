package com.example.EmpowerHer.model;

import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class UserObject {

	private String psd;
	private String gender;

	private String imageURL;
	private String name;
	private String id;

	private String email;
	private String address;


	public UserObject() {

	}

	public String getPsd() {
		return psd;
	}

	public void setPsd(String psd) {
		this.psd = psd;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getImageURL() {
		return imageURL;
	}

	public void setImageURL(String imageURL) {
		this.imageURL = imageURL;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
