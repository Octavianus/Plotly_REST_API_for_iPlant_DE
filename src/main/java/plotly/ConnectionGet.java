package main.java.plotly;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionGet {

		private String un = "";
		private String platform = "";
		private String email = "";
		
		public ConnectionGet(){
		}
		
		// this data already given by iplant if only one user
		public ConnectionGet(String un, String email, String platform) {
			this.un = un;
			this.email = email;
			this.platform = platform;
		}
		
		public static void main(String[] args) throws Exception{
			// Constructor of the connection
			// TODO get the data from UI
			// If given by iplant, only one account.
			String un = "david90test";
			String email = "wxdsdtc@gmail.com";
			String platform = "java";
			String outPutFile = "plots.csv";
			String SelectingFile = "gene_exp.csv";
			String filename = "plot from api";
			String plottitle = "a heat map example";
			// TODO Extend to other plot type
			// Now the default plot type is heatmap
			String plottype = "heatmap";
			
			
			ConnectionGet conn = new ConnectionGet(un, email, platform);
			
			UserData getPara = new UserData();
			getPara.setEmail(email);
			getPara.setUsername(un);
			getPara.setPlatform(platform);
			getPara.setFilename(filename);
			getPara.setPlotType(plottype);
			getPara.setPlotTitle(plottitle);
			
			// Test 1 -- for new user
			String urlParameters1 = getPara.getURLParemeters();
			System.out.println("Testing 1 - Register a new user in plotly");
			conn.New_User(urlParameters1);
			
			// Test 2 -- for new plot
			// TODO selectingColumn from iplant UI
			getPara.select(SelectingFile, outPutFile /* selectingColumn*/);
			String urlParameters2 = getPara.getURLParemeters(SelectingFile, outPutFile);
			System.out.println("Testing 2 - Send a plot request to plotly");
			conn.Plot_Data(urlParameters2);
			
		}
		
		// Register a new user from plotly.
		private void New_User(String urlParameters) throws Exception{
			// Use plotly rest api to request a new user account.
			try{
				// Set up a plotly account with the needed parameters.
				String url = "https://plot.ly/apimkacct";
				
				URL obj = new URL(url);
				// There is a way to install unirest-java with Maven. 
				// Send post request
				HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Accept", "application/json");
				conn.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
		 
				if(conn.getResponseCode() > 200){
					throw new RuntimeException("Failed : HTTP error code:" + conn.getResponseCode());
				}
				// TODO if the result is null, throw error
				
				// Temp strings to store the reply from plotly.
				String fullOutPut = "";
				String oneLine = "";
				
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
				
				while((oneLine = br.readLine()) != null){
					//test
					System.out.println("Json Msg:" + oneLine);
					fullOutPut += oneLine;
				}
				
				// Read value from the json format response 
				// temp strings to store the result from the returned response.
				String returnMsg = "";
				String tmp_pw = "";
				String key = "";
				String api_key = "";
				// TODO Give values to the above from the returned buffer br which in 
				// json format. And judge if there are msg or error. 
				// few places http://json.org/java/, net.sf.json for more places in the backend.
				
				System.out.println("Json Msg:" + fullOutPut);
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// Provide with required parameters to plotly. 
		private void Plot_Data(String urlParameters) throws Exception{	
			// TODO Read and determine the rows and columns in file to the url parameters. 
			
			// User input of a input and temp output file.
			
			// Return a series of strings
			// UserData process = new UserData();
			//process.select(SelectingFile, outPutFile);
			
			/*
			 * Seccesfully check return a api based on the parameters
			 */
			try{
				// Set up a plotly account with the needed parameters.
				String url = "https://plot.ly/clientresp";
				// TODO separate apart the parameters based on user input. 
				//String urlParameters1 = "un=david90test&key=5afprolg26&origin=plot&platform=Java&args=[[0, 1, 2], [3, 4, 5], [1, 2, 3], [6, 6, 5]]&kwargs={\"filename\": \"plot from api\",\"fileopt\": \"overwrite\",\"style\": {\"type\": \"bar\"},\"traces\": [1],\"layout\": {\"title\": \"experimental data\"},\"world_readable\": true}";
				
				URL obj = new URL(url);
				// There is a way to install unirest-java with Maven. 
				// Send post request
				HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
				conn.setRequestMethod("POST");
				conn.setRequestProperty("Accept", "application/json");
				conn.setDoOutput(true);
				DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();
		 
				if(conn.getResponseCode() > 200){
					throw new RuntimeException("Failed : HTTP error code:" + conn.getResponseCode());
				}
				// TODO if the result is null, throw error
				
				// Temp strings to store the reply from plotly.
				String fullOutPut = "";
				String oneLine = "";
				
				BufferedReader br = new BufferedReader(new InputStreamReader(
						(conn.getInputStream())));
				
				while((oneLine = br.readLine()) != null){
					//test
					System.out.println("Json Msg:" + oneLine);
					fullOutPut += oneLine;
				}
				
				// Read value from the json format response 
				// temp strings to store the result of the returned response.
				// TODO Give values to the above from the returned buffer br which in 
				// json format. And judge if there are msg or error. http://json.org/java/
				
				// System.out.println("Json Msg:" + fullOutPut);
				// TODO Return the msg to the iplant UI
				Response res = new Response(fullOutPut);
				// Convert String to Json.
				res.ConvertoJson();
				System.out.println(res.getURL());
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			/*
			*/
			
		}
}
