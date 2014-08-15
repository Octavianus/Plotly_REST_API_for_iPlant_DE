package main.java.plotly;

import static java.util.Arrays.asList;

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
import java.util.List;

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
			// TODO devide the string by "," or " "
			String selectingColumn = "gene,value_1,value_2";
			// TODO Extend to other plot type
			// Now the default plot type is heatmap, threshold is 50
			String plottype = "heatmap";
			double threshold = 50;
			
			String fullOutput = "";
			
			ConnectionGet conn = new ConnectionGet(un, email, platform);
			
			UserData getPara = new UserData();
			getPara.setEmail(email);
			getPara.setUsername(un);
			getPara.setPlatform(platform);
			getPara.setFilename(filename);
			getPara.setPlotType(plottype);
			getPara.setPlotTitle(plottitle);
			getPara.setThreshold(threshold);
			getPara.setSelectingColumns(selectingColumn);
			
			// Test 1 -- for new user
			String urlParameters1 = getPara.getURLParemeters();
			System.out.println("Testing 1 - Register a new user in plotly");
			fullOutput = conn.New_User(urlParameters1);
			
			// Test 2 -- for new plot
			// TODO selectingColumn from iplant UI
			getPara.select(SelectingFile, outPutFile);
			String urlParameters2 = getPara.getURLParemeters(SelectingFile, outPutFile);
			System.out.println("Testing 2 - Send a plot request to plotly");
			fullOutput = conn.Plot_Data(urlParameters2);
			
			// TODO Return the msg to the iplant UI
			Response res = new Response(fullOutput);
			// Convert String to Json.
			res.ConvertoJson();
			System.out.println(res.getURL());
			
		}
		
		// Register a new user from plotly.
		private String New_User(String urlParameters) throws Exception{
			// Use plotly rest api to request a new user account.
			String fullOutPut = "";
			
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
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return fullOutPut;
		}
		
		// Provide with required parameters to plotly. 
		private String Plot_Data(String urlParameters) throws Exception{	
			// User input of a input and temp output file.
			
			String fullOutPut = "";
			
			/*
			 * Seccesfully check return a api based on the parameters
			 */
			try{
				// Set up a plotly account with the needed parameters.
				String url = "https://plot.ly/clientresp";				
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
				
				
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			return fullOutPut;
			
		}
}
