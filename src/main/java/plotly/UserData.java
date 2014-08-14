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
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
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
	private String args = "args=[{";
	private String kwargs = "kwargs={\"filename\":\"";
	private String url = "";
	private String filename = "";
	private String plottype = "";
	private String plottitle = "";

	private List<String> samplesName = asList("sampleNO.1","sampleNO.2");
	
	// set the threshold of the raw data.
	private int threshold = 0;
	private int columnofValues = 0;
	private int columnsBeforeValues = 1;
	
	/* 
	 * The followings set up all the parameters.
	 * The getXXX() seems redundant now, we'll see.
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
	
	public void setFilename(String filename){
		this.filename = filename;
	}

	public void setPlotType(String plottype){
		this.plottype = plottype;
	}
	
	public void setPlotTitle(String plottitle){
		this.plottitle = plottitle;
	}

	public void setThreshold(int threshold){
		this.threshold = threshold;
	}

	public void setColumnofValues(int columnofValues){
		this.columnofValues = columnofValues;
	}
	
	public int getColumnofValues(){
		return this.columnofValues;
	}
	
	/* 
	 * save the columns which are selecting into a temp file
	 */
	public void select(String in, String out) throws Exception{
		try {
			// Try to read the CSV files locally.
			// TODO Read from iRODS and UI, and THIS IS FOR THE TREATED DATA
			// TODO HOW AHOUT THE RAW DATA.
			String sig = "significant";
			int columnofValues = 0;
			
			List<String> selectingColumn = asList("gene","value_1","value_2");
			List<Integer> indexofSelectingColumns = new ArrayList<Integer>();
			
			CSVReader reader = new CSVReader(new FileReader(in));
			CSVWriter writer = new CSVWriter(new FileWriter(out));
			
			List<String[]> rs = reader.readAll();
			// Get the header of the file, and check the significant field.
			String[] header = rs.get(0);
			
			// Set the number of selecting values in a raw or treated file.
			Iterator<String> it = selectingColumn.iterator();
			int tmpWalker = 0;
			while(tmpWalker < columnsBeforeValues){
				it.next();
				tmpWalker ++;
			}
			while(it.hasNext()){
					columnofValues++;
					it.next();
			}
			setColumnofValues(columnofValues);
			
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
				
				Iterator<String> it1 = selectingColumn.iterator();
				while(it1.hasNext()){
					if(header[indexofHeader].matches(it1.next()))
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
				Iterator<Integer> it1 = indexofSelectingColumns.iterator();
				String[] entries;
				String oneLine = "";
				while(it1.hasNext())
				{
					oneLine += rs.get(indexofRows)[it1.next()];
					if(it1.hasNext())
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

		/* This is the example to plot a char./
				String urlParameters2 = "un=david90test&key=5afprolg26&origin=plot&"
				+ "platform=Java"
				+ "&args=[[0, 1, 2], [3, 4, 5], [1, 2, 3], [6, 6, 5]]&"
				+ "kwargs={\"filename\": \"plot from api\",\"fileopt\": \"overwrite\",\"style\": {\"type\": \"bar\"},\"traces\": [1],\"layout\": {\"title\": \"experimental data\"},\"world_readable\": true}";
		/*/
		

		// Got the data from csv file
		// the default yaxis data is from gene, the index is 1.
		// the zaxis data will be specified by user
		// the default xaxis data is the following columns, which after the gene col.
		int indexofYaxis = 0;
		
		String xdata = "\"x\":[";	
		String ydata = "\"y\":[";
		String zdata = "\"z\":[";
		
		// Set y and z data.
		int indexofRows = 1; // Jump over the header row.
		while(indexofRows < rs.size())
		{
			zdata += "[";	
			int tmpWalker = 0;
			while(tmpWalker < getColumnofValues()){
				if(tmpWalker + 1 == getColumnofValues()){
					zdata += rs.get(indexofRows)[tmpWalker + columnsBeforeValues] + "],";
				}else
					zdata += rs.get(indexofRows)[tmpWalker + columnsBeforeValues] + ",";
				tmpWalker++;
			}
			
			
			if(indexofRows + 1 == rs.size()){
				ydata += "\"" + rs.get(indexofRows)[indexofYaxis] + "\"],";
				zdata = zdata.substring(0, zdata.length() -1);
				zdata += "],";
			}else{
				ydata += "\"" + rs.get(indexofRows)[indexofYaxis] + "\",";
			}
			indexofRows ++;
		}
		
		// Set x data
		int tmpWalker = 0;
		while(tmpWalker < samplesName.size()){
			if(tmpWalker + 1 == samplesName.size())
			{
				xdata += "\"" + samplesName.get(tmpWalker) + "\"";
				xdata += "],";
			}else
				xdata += "\"" + samplesName.get(tmpWalker) + "\",";
			tmpWalker ++;
		}
		
		String prefix_data = username + "&" + api_key +"&" + origin + "&" + platform + "&";
		args += zdata + xdata + ydata + "\"name\":\"example\",\"type\":\"" + plottype + "\"}]" + "&";
		kwargs += filename + "\",\"fileopt\":\"overwrite\", \"style\":{\"type\":\"" 
				+ plottype + "\"},\"layout\":{\"title\":\"" 
				+ plottitle + "\"},\"world_readable\":true}";
			
		//data += xdata + ydata + zdata;
		
		// TODO filename, xaxis, yaxis, tile of the graph should be collected from the user.
		// data like fileopt in the kwargs should be by default for now.
	
		//String data = "args=[{\"z\":[[9,10,10,9],[7,8,4,2]],\"x\":[\"sample1\",\"sample2\",\"sample3\",\"sample4\"],\"y\":[\"AA\",\"BB\",\"CC\"],\"name\":\"example\",\"type\":\"heatmap\"}]" + "&"; 
		URLParameters = prefix_data + args + kwargs;
		
		return URLParameters;
	}
}
