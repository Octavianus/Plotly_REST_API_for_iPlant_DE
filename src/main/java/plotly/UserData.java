package main.java.plotly;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Arrays.asList;
import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import net.sf.json.*;

public class UserData {
	
	// File
	private String SelectingFile;
	private String outPutFile;
	
	// New User
	private String username = "un=";
	private String email = "email=";
	private String platform = "platform=";
	private String tmp_pw = "tmp_pw=";
	// private String version = "version=0.1";
	
	// Plot Data
	// api_key is default, given by iplant
	private String api_key = "key=5afprolg26";
	// origin = plot as default
	private String origin = "origin=plot";
	private String args = "args=";
	private String kwargs = "kwargs=";
	private String url = "";
	private String message = "";
	private String warning = "Warning message: ";
	private String error = "Error message: ";
	private String filename = "";
	
	/* 
	 * The followings set up all the parameters.
	 */
	public UserData() {
	}
	
	public UserData(String SelectingFile) {
		this.SelectingFile = SelectingFile;
	}
	
	public String getSelectingFile(){
		return this.SelectingFile;
	}
	
	public void setOutPutFile(String outPutFile){
		this.outPutFile = outPutFile; 
	}
	
	public String getOutPutFile(){
		return this.outPutFile;
	}
	
	public void  set_api_key(String set_api_key){
		this.api_key += set_api_key;
	}
	
	public String get_api_key(){
		return this.api_key;
	}
	
	public void  setUsername(String user_name){
		this.username += user_name;
	}
	
	public String getUsername(){
		return this.username;
	}

	public void  setEmail(String Email){
		this.email += Email;
	}
	
	public String getEmail(){
		return this.email;
	}
	
	public void  setURL(String url){
		this.url = url;
	}
	
	public String getURL(){
		return this.url;
	}

	public void  setPlatform(String platform){
		this.platform += platform;
	}
	
	public String getPlatform(){
		return this.platform;
	}
	
	public void  setPwd(String pwd){
		this.tmp_pw += pwd;
	}
	
	public String getPwd(){
		return this.tmp_pw;
	}
	
	/*	Get Set
	 *  private String message = "";
	 *	private String warning = "Warning message: ";
	 *	private String error = "Error message: ";
	 *	private String filename = "";
	 */
	
	/* 
	 * save the columns which are selecting into a temp file
	 */
	public void select(String in, String out) throws Exception{
		try {
			// Try to read the CSV files locally.
			// TODO Read from iRODS and UI
			String sig = "significant";
			
			List<String> selectingColumn = asList("gene","sample_1","sample_2","value_1","value_2","significant");
			List<Integer> indexofSelectingColumns = new ArrayList<Integer>();
			
			CSVReader reader = new CSVReader(new FileReader(in));
			CSVWriter writer = new CSVWriter(new FileWriter(out));
			
			List<String[]> rs = reader.readAll();
			// Get the header of the file, and check the significant field.
			String[] header = rs.get(0);
			
			// If there is a column marks the significant, filter the rows that are
			// not significant in order to save the executing time.
			// Also record the index of all the selecting columns.
			
			// TODO user could choice if they need to filter the insignificant rows. 
			// Reduce disk I/O by editing the file in the RAM all the time.
			int indexofHeader = 0;
			boolean removeInsig = true;
			while(indexofHeader < header.length){
				/*
				 * Select the rows here
				 */
				if(header[indexofHeader].matches(sig) && removeInsig)
				{
					// Scan start from the data, remove the rows that are insignificant.
					int indexofRows = 1;
					while(indexofRows < rs.size()){
						if(rs.get(indexofRows)[indexofHeader].matches("no")){
							// System.out.println(rs.remove(indexofRows));
							rs.remove(indexofRows);
							// -- Tricky bug since the size has been trimmed.
							indexofRows --;
							// System.out.println(indexofRows);
						}
						indexofRows ++;
					}
				}
				
				/*
				 * Record the index of all the selecting columns.
				 */
				
				Iterator<String> it = selectingColumn.iterator();
				while(it.hasNext()){
					if(header[indexofHeader].matches(it.next()))
						indexofSelectingColumns.add(indexofHeader);
				}
				
				indexofHeader ++;
			}
			
			/*
			 * Select the columns here
			 */
			int indexofRows = 0;
			List<String[]> outputFile = new ArrayList<String[]>();
			while(indexofRows < rs.size())
			{
				Iterator<Integer> it = indexofSelectingColumns.iterator();
				String[] entries;
				String oneLine = "";
				while(it.hasNext())
				{
					oneLine += rs.get(indexofRows)[it.next()];
					if(it.hasNext())
						oneLine += "#";
				}
				
				entries = oneLine.split("#");
				outputFile.add(entries);
				indexofRows ++;
			}
			
			writer.writeAll(outputFile);
			
			// Clean the butt
			reader.close();
			writer.close();
			
		} catch (FileNotFoundException e) {
			// TODO file not found error code or msg returned.
			System.out.println("File not found");
			e.printStackTrace();
		}
		
	}	
	
	/* 
	 * Get the url of POST request 
	 */
	public String getURLParemeters() throws Exception{
		String URLParameters = username +"&"+ email +"&"+ platform;
		return URLParameters;
	}
	
	/*
	 * Overload the for POST plot method 
	 */
	public String getURLParemeters(String in, String out) throws Exception{
		String URLParameters = "";
		CSVReader reader = new CSVReader( new FileReader(out));
		List<String[]> rs = reader.readAll();
		String[] header = rs.get(0);

		
		/* Thi is the example to plot a char./ String urlParameters2 = "un=david90test&key=5afprolg26&origin=plot&"
				+ "platform=Java"
				+ "&args=[[0, 1, 2], [3, 4, 5], [1, 2, 3], [6, 6, 5]]&"
				+ "kwargs={\"filename\": \"plot from api\",\"fileopt\": \"overwrite\",\"style\": {\"type\": \"bar\"},\"traces\": [1],\"layout\": {\"title\": \"experimental data\"},\"world_readable\": true}";
		/*/
		

		// Got the data from csv file
		String xdata = "";
		
		String ydata = "";
		
		String zdata = "";
		
	
		// TODO filename, xaxis, yaxis, tile of the graph should be collected from the user.
		// data like fileopt in the kwargs should be by default for now.
	
		String prefix_data = username +"&"+ api_key +"&" + origin + "&" + platform + "&";
		String data = "args=[{\"z\":[[9,10,10,9],[7,8,4,2]],\"x\":[\"sample1\",\"sample2\",\"sample3\",\"sample4\"],\"y\":[\"AA\",\"BB\",\"CC\"],\"name\":\"example\",\"type\":\"heatmap\"}]" + "&";
		String butt_data = "kwargs={\"filename\":\"plot from api\", \"fileopt\":\"overwrite\", \"style\":{\"type\":\"heatmap\"},\"layout\":{\"title\":\"heatmap\"},\"world_readable\":true}"; 
		URLParameters = prefix_data + data + butt_data;
		
		return URLParameters;
	}
	


}
