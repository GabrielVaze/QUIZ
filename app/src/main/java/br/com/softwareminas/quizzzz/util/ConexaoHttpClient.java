package br.com.softwareminas.quizzzz.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class ConexaoHttpClient {	
	public static final int HTTP_TIMEOUT = 60 * 1000;
	private static HttpURLConnection httpClient;
	
	
	public static String executaHttpPost(String urlpost, ArrayList<NomeValor> parametrosPost) throws Exception{
		URL url = new URL(urlpost);
		HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		DataOutputStream dStream = new DataOutputStream(httpConn.getOutputStream());
		String parametros="";
		String ecomercial = "";
		for (int i = 0; i<parametrosPost.size();i++) {
			parametros = parametros + ecomercial+ parametrosPost.get(i).getName()+"="+parametrosPost.get(i).getValue();
			ecomercial="&";
		}
		dStream.writeBytes(parametros);
		dStream.flush();
		dStream.close();
		int responseCode = httpConn.getResponseCode();
		final StringBuilder output = new StringBuilder("Request URL " + url);
		output.append(System.getProperty("line.separator") + "Request Parameters " + parametros);
		output.append(System.getProperty("line.separator")  + "Response Code " + responseCode);
		output.append(System.getProperty("line.separator")  + "Type " + "POST");
		BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
		String line = "";
		StringBuilder responseOutput = new StringBuilder();
		while((line = br.readLine()) != null ) {
		    responseOutput.append(line);
		}
		br.close();
		httpConn.disconnect();
		return responseOutput.toString();
	}
	
	public static String executaHttpGet(String urlcompleta) throws Exception{
		
		URL url = new URL(urlcompleta);
		HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
		httpConn.setRequestMethod("GET");
		
		BufferedReader br = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
		String line = "";
		StringBuilder responseOutput = new StringBuilder();
		while((line = br.readLine()) != null ) {
		    responseOutput.append(line);
		}
		br.close();		
		httpConn.disconnect();
		return responseOutput.toString();
	}


	public static String enviarPostJSON(String urlpost, String body, ArrayList<NomeValor> headers) throws Exception{
		URL url = new URL(urlpost);
		HttpURLConnection httpConn = (HttpURLConnection)url.openConnection();
		httpConn.setRequestMethod("POST");
		httpConn.setRequestProperty("X-Client-Version", "1.0.0");
		for (int i = 0; i<= headers.size()-1; i++) {
			httpConn.setRequestProperty(headers.get(i).getName(), headers.get(i).getValue());
		}
		httpConn.setDoOutput(true);

		OutputStream os = httpConn.getOutputStream();
		OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
		osw.write(body);
		osw.flush();
		osw.close();
		os.close();
		httpConn.connect();

		BufferedInputStream bis = new BufferedInputStream(httpConn.getInputStream());
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		int result2 = bis.read();
		while(result2 != -1) {
			buf.write((byte) result2);
			result2 = bis.read();
		}
		return buf.toString();
	}
	
}

