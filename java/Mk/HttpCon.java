package Mk;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @author Motyak
 */
public class HttpCon {
	
	public static void main(String[] args) throws IOException
	{
		{
			System.out.println("Simple example : printing 5 random 2-digit numbers generated from random.org");
			String url = "https://www.random.org/integers/?num=5&min=1&max=99&col=5&base=10&format=plain&rnd=new";
			String res = HttpCon.request(Type.GET, url, null, null);
			System.out.println(res);
		}
		
		{
			System.out.println("Full example : getting all fast food restaurants in Monaco from Overpass API");
			String url = "https://lz4.overpass-api.de/api/interpreter";
			String[] headers = {"Content-Type: text/xml"};
			String data = 
				"<osm-script output=\"json\">\r\n" + 
				"  <query type=\"node\">\r\n" + 
				"    <has-kv k=\"amenity\" v=\"fast_food\"/>\r\n" + 
				"    <bbox-query s=\"43.7247599\" w=\"7.4090279\" n=\"43.7519311\" e=\"7.4398704\"/>\r\n" + 
				"  </query>\r\n" + 
				"  <print/>\r\n" + 
				"</osm-script>";
			final Request OVERPASS_REQ = new Request(Type.POST, url, headers, data);
			String res = HttpCon.exec(OVERPASS_REQ);
			System.out.println(res);
		}
	}
	
	/**
	 * Send an HTTP request to a server (URL)
	 * @param reqType the HTTP method to use
	 * @param url the URL of the website to send the request to (can contain query strings)
	 * @param headers the HTTP headers to use (can be set to null if none)
	 * @param data the data to send to the server if any (can be set to null if none)
	 * @return a response from the server
	 * @throws IOException if the connection with the server cannot be established
	 */
	public static String request(Type reqType, String url, String[] headers, String data) throws IOException {
		URL u = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) u.openConnection();
		
		connection.setRequestMethod(reqType.toString());
		
		if(headers != null) {
			String field, value;
			for(String s : headers) {
				if(s != null) {
					field = s.substring(0, s.indexOf(':'));
					value = s.substring(s.indexOf(':') + 1);
					connection.setRequestProperty(field, value);
				}
			}
		}
			
		if(data != null) {
			connection.setDoOutput(true);
			OutputStream outputStream = connection.getOutputStream();
			byte[] b = data.getBytes("UTF-8");
			outputStream.write(b);
			outputStream.flush();
			outputStream.close();
		}
		
		System.out.println("Storing the response..");
		StringBuilder content;

		try (BufferedReader input = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			String line;
			content = new StringBuilder();
			while ((line = input.readLine()) != null) {
				content.append(line);
				content.append(System.lineSeparator());
			}
		} 
		finally {
			connection.disconnect();
		}
		
		return content.toString();
	}
	
	/**
	 * Send an HTTP request to a server (URL)
	 * @param r the HTTP request to execute
	 * @return a response from the server
	 * @throws IOException if the connection with the server cannot be established
	 */
	public static String exec(Request r) throws IOException {
		return HttpCon.request(r.reqType, r.url, r.headers, r.data);
	}
	
	static public enum Type { GET, POST; }

	static public class Request {
		public Type reqType;
		public String url;
		public String[] headers;
		public String data;
		
		public Request(Type reqType, String url, String[] headers, String data) {
			this.reqType = reqType;
			this.url = url;
			this.headers = headers;
			this.data = data;
		}
		
		@Override
		public String toString() {
			try {
				return HttpCon.exec(this);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "";	//in case of error
		}
	}
	
}


