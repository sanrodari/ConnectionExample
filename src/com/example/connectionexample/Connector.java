package com.example.connectionexample;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Connector {
	
	public boolean checkConnectivity(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager)
			context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}
	
	public String doGet(final String url) {
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response = httpclient.execute(new HttpGet(url));
			return convertHttpResponseToString(response);
		} catch (Exception e) {
			Log.e(Constants.TAG, "No se ha podido realizar la conexi—n.");
			throw new IllegalStateException("No se ha podido realizar la conexi—n.", e);
		}
	}
	
	public String doPost(String url, List<NameValuePair> nameValuePairs) {
	    HttpClient httpclient = new DefaultHttpClient();
	    HttpPost httpPost = new HttpPost(url);

	    try {
	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        HttpResponse response = httpclient.execute(httpPost);
	        return convertHttpResponseToString(response);
	    } catch (Exception e) {
			Log.e(Constants.TAG, "No se ha podido realizar la conexi—n.");
			throw new IllegalStateException("No se ha podido realizar la conexi—n.", e);
		}
	}
	
	private String convertHttpResponseToString(HttpResponse response) {
		try {
			StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				response.getEntity().writeTo(out);
				out.close();
				String responseString = out.toString();
				
				return responseString;
			} else {
				response.getEntity().getContent().close();
				throw new IOException(statusLine.getReasonPhrase());
			}
		} catch (Exception e) {
			Log.e(Constants.TAG, "No se ha podido realizar la conexi—n.");
			throw new IllegalStateException("No se ha podido realizar la conexi—n.", e);
		}
	}

}
