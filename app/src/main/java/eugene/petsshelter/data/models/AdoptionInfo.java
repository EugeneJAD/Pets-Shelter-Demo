package eugene.petsshelter.data.models;

import com.google.gson.annotations.SerializedName;

public class AdoptionInfo{

	@SerializedName("checkList")
	private String checkList;

	@SerializedName("fees")
	private String fees;

	@SerializedName("hours")
	private String hours;

	@SerializedName("hoursInfo")
	private String hoursInfo;

	@SerializedName("intro")
	private String intro;

	@SerializedName("feesExpl")
	private String feesExpl;

	public void setCheckList(String checkList){
		this.checkList = checkList;
	}

	public String getCheckList(){
		return checkList;
	}

	public void setFees(String fees){
		this.fees = fees;
	}

	public String getFees(){
		return fees;
	}

	public void setHours(String hours){
		this.hours = hours;
	}

	public String getHours(){
		return hours;
	}

	public void setHoursInfo(String hoursInfo){
		this.hoursInfo = hoursInfo;
	}

	public String getHoursInfo(){
		return hoursInfo;
	}

	public void setIntro(String intro){
		this.intro = intro;
	}

	public String getIntro(){
		return intro;
	}

	public void setFeesExpl(String feesExpl){
		this.feesExpl = feesExpl;
	}

	public String getFeesExpl(){
		return feesExpl;
	}
}