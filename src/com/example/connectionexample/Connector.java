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
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

/**
 * Clase para realizar las conexiones por medio del Apache HTTP Client.
 * @author sanrodari
 */
public class Connector {

	/**
	 * Verifica la disponibilidad de acceso a Internet del dispositivo.
	 * 
	 * @param context Contexto de la aplicación.
	 * @return true de haber disponibilidad.
	 */
	public boolean checkConnectivity(Context context) {
		ConnectivityManager connMgr = (ConnectivityManager)
			context.getSystemService(Context.CONNECTIVITY_SERVICE);
			
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		return networkInfo != null && networkInfo.isConnected();
	}
	
	/**
	 * Realiza una petición GET a una URL determinada.
	 * 
	 * @param url URL a la que se desea realizar la petición.
	 * @return Cadena con la respuesta del servidor, si ocurre una excepción se retorna null.
	 */
	public String doGet(final String url) {
		try {
			HttpParams httpParameters = new BasicHttpParams();
			// Se ponen 3 segundos de timeout.
			HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
			
			HttpClient httpclient = new DefaultHttpClient(httpParameters);
			HttpResponse response = httpclient.execute(new HttpGet(url));
			return convertHttpResponseToString(response);
		} catch (Exception e) {
			Log.e(Constants.TAG, "No se ha podido realizar la conexión.");
			return null;
		}
	}
	
	/**
	 * Realiza una petición POST a una URL determinada.
	 * 
	 * @param url URL a la que se desea realizar la petición.
	 * @param nameValuePairs Parámetros de la petición POST.
	 * @return Cadena con la respuesta del servidor, si ocurrió una excepción se retorna null.
	 */
	public String doPost(String url, List<NameValuePair> nameValuePairs) {
		HttpParams httpParameters = new BasicHttpParams();
		// Se ponen 3 segundos de timeout.
		HttpConnectionParams.setConnectionTimeout(httpParameters, 3000);
		
	    HttpClient httpclient = new DefaultHttpClient(httpParameters);
	    HttpPost httpPost = new HttpPost(url);

	    try {
	        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

	        HttpResponse response = httpclient.execute(httpPost);
	        return convertHttpResponseToString(response);
	    } catch (Exception e) {
			Log.e(Constants.TAG, "No se ha podido realizar la conexión.");
			return null;
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
			Log.e(Constants.TAG, "No se ha podido realizar la conexión.");
			return null;
		}
	}
}
