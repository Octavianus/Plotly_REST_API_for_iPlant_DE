package main.java.plotly;

// import com.google.gwt.user.client.rpc.RemoteService;
// import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

//@RemoteServiceRelativePath("UserData")

public interface ClientUserRequest /* extends RemoteService*/ {
	
	/*
	 * In UserData class UserData(String username, String email, String platform); Receive all the user input when create a new plotly user.
	 * 							String username: plotly username, should be same as iplant username
	 * 							String email: Email account associated with the user.
	 * 							String platform: Language or platform that the client is making the request from, default is java.
	 * 					 UserData(String username, String api_key, String platform, String plottype, String plottitle, String filename, boolean publ); Receive all the user input when plot a graph.
	 * 							String username: plotly username, should be same as iplant username.
	 * 							String api_key: Key used to access your plotly account with through the API.
	 * 							String platform: Language or platform that the client is making the request from, default is java.
	 * 							String plottype: What kind of graph that the user need, ex: heatmap
	 * 							String plottitle: The title of the graph.
	 * 							String filename: file name of the graph file on plotly.
	 * 							boolean publ: World readable, false means, only can see the graph after signed in.
	 *  				 select(String inputFileName, String outputFileName) deal with the user's selection of file and column and user-defined filter
	 *  				 		String inputFileName is the selecting file that are going to pre-process
	 *  						String outputFileName is the destination where to save after the  pre-process.
	 *  				 getURLParameters() parse the New user request POST url parameters.
	 *  				 getURLParameters(String outpufFileName) parse the Plot graph request POST url parameters.
	 *  						String outputFileName is the destination where to save after the  pre-process.
	 *  
	 *  Both above need to get access to the iRODS.
	 *  So assume the file type is iRODSFile at first
	 */ 
	
	UserData UserData(String username, String email, String platform);
	UserData UserData(String username, String api_key, String platform, String plottype, String plottitle, String filename, boolean publ);
	UserData select(String inputfileName, String outputfileName);
	UserData getURLParameters();
	UserData getURLParameters(String outpufFileName);
	// So assume that the file type is iRODSFile at first, read and write file from iRods.
	irodsFileReader readfile(String inputFileName);
	irodsFileWriter writefile(String outpufFileName); 
}