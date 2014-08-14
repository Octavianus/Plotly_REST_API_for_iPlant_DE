package main.java.plotly;

import net.sf.json.JSONObject;
import net.sf.json.JSONSerializer;

public class Response {
	
	private String res = null;
	private JSONObject JsonRes = null;
	private String errMsg = null;
	private String WarnMsg = null;
	private String url = null;
	private String msg = null;
	
	public Response(){
		
	}
	
	public void ConvertoJson(){
		JSONObject json = (JSONObject) JSONSerializer.toJSON(this.res);
		this.JsonRes = json;
	}

	public Response(String res){
		this.res = res;
	}
	
	public String getWarnMsg(){
		this.WarnMsg = JsonRes.getString("warning");
		return this.WarnMsg;
	}
	
	public String getError(){
		this.msg = JsonRes.getString("error") ;
		return this.msg;
	}
	
	public String getMessage(){
		this.errMsg = JsonRes.getString("message") ;
		return this.errMsg;
	}
	
	public String getURL(){
		this.url = JsonRes.getString("url");
		return this.url;
	}
}
