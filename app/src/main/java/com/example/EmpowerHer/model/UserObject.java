package com.example.EmpowerHer.model;

public class UserObject{

	private String psd;
	private String gender;
	private String school;
	private String RegNo;
	private String imageURL;
	private String name;
	private String id;
	private boolean isAdmin;
	private String userType;
	private boolean userApproved;
	private String email;
	private String studentClass;


	public UserObject() {

	}
	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean Admin) {
		isAdmin = Admin;
	}

	public String getStudentClass() {
		return studentClass;
	}

	public void setStudentClass(String StudentClass) {
		this.studentClass = StudentClass;
	}

	public void setPsd(String Psd){
		this.psd = Psd;
	}

	public String getPsd(){
		return psd;
	}

	public void setGender(String Gender){
		this.gender = Gender;
	}

	public String getGender(){
		return gender;
	}

	public void setSchool(String School){
		this.school = School;
	}

	public String getSchool(){
		return school;
	}

	public void setRegNo(String regNo){
		this.RegNo = regNo;
	}

	public String getRegNo(){
		return RegNo;
	}

	public void setImageURL(String ImageURL){
		this.imageURL = ImageURL;
	}

	public String getImageURL(){
		return imageURL;
	}

	public void setName(String Name){
		this.name = Name;
	}

	public String getName(){
		return name;
	}

	public void setId(String Id){
		this.id = Id;
	}

	public String getId(){
		return id;
	}

	public void setIsAdmin(boolean IsAdmin){
		this.isAdmin = IsAdmin;
	}

	public boolean isIsAdmin(){
		return isAdmin;
	}

	public void setUserType(String UserType){
		this.userType = UserType;
	}

	public String getUserType(){
		return userType;
	}

	public void setUserApproved(boolean UserApproved){
		this.userApproved = UserApproved;
	}

	public boolean isUserApproved(){
		return userApproved;
	}

	public void setEmail(String Email){
		this.email = Email;
	}

	public String getEmail(){
		return email;
	}
}
