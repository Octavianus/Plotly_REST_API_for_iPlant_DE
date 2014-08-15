package main.java.plotly;

// import com.google.gwt.user.client.rpc.RemoteService;
// import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

//@RemoteServiceRelativePath("UserData")

public interface ClientUserRequest /* extends RemoteService*/ {
	
	//For multi- user 
	UserData[] UserData(String username);
	
	
}