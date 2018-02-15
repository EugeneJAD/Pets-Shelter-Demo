package eugene.petsshelter.data.models;

import com.google.gson.annotations.SerializedName;

public class ApiError {

	@SerializedName("msg")
	private String msg;

	@SerializedName("code")
	private int code;

	@SerializedName("service_version")
	private String serviceVersion;

	public void setMsg(String msg){
		this.msg = msg;
	}

	public String getMsg(){
		return msg;
	}

	public void setCode(int code){
		this.code = code;
	}

	public int getCode(){
		return code;
	}

	public void setServiceVersion(String serviceVersion){
		this.serviceVersion = serviceVersion;
	}

	public String getServiceVersion(){
		return serviceVersion;
	}
}