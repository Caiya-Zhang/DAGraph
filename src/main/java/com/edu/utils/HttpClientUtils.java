package com.edu.utils;
import java.io.InputStream;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpClientUtils {
 



	//Encapsulate the request object
	public static JsonArray getJSONObject(String url){

		try {

			//Build the POST request
			HttpGet httpPost = new HttpGet(url);
			//Build the request header
			httpPost.setHeader("Content-type","application/json;charset=UTF-8");
			//Build HTTP client who sends HTTPS request
			CloseableHttpClient httpClient =  HttpClients.createDefault();;
			//Do Post request
			CloseableHttpResponse resp = httpClient.execute(httpPost);
			if (resp.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			//Get response entity
			HttpEntity entity = resp.getEntity();
			//Read the data from file to the program
			InputStream content = entity.getContent();
			String inline = "";
			Scanner sc = new Scanner(content);
			while (sc.hasNext()) {
				inline += sc.nextLine();
			}
			JsonArray jsonArray = new
					JsonParser().parse(inline).getAsJsonArray();
			sc.close();
			return jsonArray;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
