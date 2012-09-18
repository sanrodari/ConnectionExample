package com.example.connectionexample;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private Connector connector = new Connector();
	private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        editText = (EditText) findViewById(R.id.editText);
    }
    
    public void connectToService(View view) {
    	// Tratar de acceder a la informaci—n.
		if (connector.checkConnectivity(this)) {
			new ConnectGetTask().execute(
				"http://androidexample.phpfogapp.com/index.php?/welcome/hello", "GET");
		}
		// Mostrar el error de no haber conectividad.
		else {
			Toast.makeText(MainActivity.this,
				"No hay conectividad, no se ha podido cargar la informaci—n.",
				Toast.LENGTH_LONG).show();
		}
	}
    
    public void connectToServicePost(View view) {
    	// Tratar de acceder a la informaci—n.
		if (connector.checkConnectivity(this)) {
			new ConnectGetTask().execute(
				"http://androidexample.phpfogapp.com/index.php?/welcome/echoPostParams", "POST");
		}
		// Mostrar el error de no haber conectividad.
		else {
			Toast.makeText(MainActivity.this,
				"No hay conectividad, no se ha podido cargar la informaci—n.",
				Toast.LENGTH_LONG).show();
		}
	}
    
    private class ConnectGetTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			String method = params[1];
			String result;
			
			// GET
			if(method.equalsIgnoreCase("GET")) {
				result = new Connector().doGet(url);
			}
			// POST
			else if(method.equalsIgnoreCase("POST")) {
				List<NameValuePair> postParams = new ArrayList<NameValuePair>();
				
				// Si ha sido ingresado algo en el campo de texto entonces
				// se manda como par‡metro.
				String enteredText = editText.getText().toString();
				if(!TextUtils.isEmpty(enteredText)) {
					postParams.add(new BasicNameValuePair("sendParam", enteredText));
				}
				
				result = new Connector().doPost(url, postParams);
			}
			// Por defecto GET
			else {
				result = new Connector().doGet(url);
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			Toast.makeText(MainActivity.this, "Informaci—n recuperada: [" + result + "]",
				Toast.LENGTH_LONG).show();
		}
    	
    }

}
