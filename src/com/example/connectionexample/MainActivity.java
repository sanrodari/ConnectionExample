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

/**
 * Actividad que ilustra el uso básico de peticiones HTTP a servicios Web.
 * 
 * @author sanrodari
 */
public class MainActivity extends Activity {
	
	/**
	 * Objeto que nos permite hacer las conexiones.
	 */
	private Connector connector = new Connector();
	
	private EditText editText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        editText = (EditText) findViewById(R.id.editText);
    }
    
    public void connectToService(View view) {
    	// Tratar de acceder a la información.
		if (connector.checkConnectivity(this)) {
			new ConnectTask().execute(
				"http://androidexample.phpfogapp.com/index.php?/welcome/hello", "GET");
		}
		// Mostrar el error de no haber conectividad.
		else {
			Toast.makeText(MainActivity.this,
				"No hay conectividad, no se ha podido cargar la información.",
				Toast.LENGTH_LONG).show();
		}
	}
    
    public void connectToServicePost(View view) {
    	// Tratar de acceder a la información.
		if (connector.checkConnectivity(this)) {
			new ConnectTask().execute(
				"http://androidexample.phpfogapp.com/index.php?/welcome/echoPostParams", "POST");
		}
		// Mostrar el error de no haber conectividad.
		else {
			Toast.makeText(MainActivity.this,
				"No hay conectividad, no se ha podido cargar la información.",
				Toast.LENGTH_LONG).show();
		}
	}
    
    private class ConnectTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String url = params[0];
			String method = params[1];
			String result;
			
			// POST
			if(method.equalsIgnoreCase("POST")) {
				result = new Connector().doGet(url);
			}
			// Por defecto GET
			else {
				List<NameValuePair> postParams = new ArrayList<NameValuePair>();
				
				// Si ha sido ingresado algo en el campo de texto entonces
				// se manda como parámetro.
				String enteredText = editText.getText().toString();
				if(!TextUtils.isEmpty(enteredText)) {
					postParams.add(new BasicNameValuePair("sendParam", enteredText));
				}
				
				result = new Connector().doPost(url, postParams);
			}
			
			return result;
		}
		
		@Override
		protected void onPostExecute(String result) {
			if(result != null) {
				Toast.makeText(MainActivity.this, "Información recuperada: [" + result + "]",
					Toast.LENGTH_LONG).show();
			}
			else {
				Toast.makeText(MainActivity.this, "Error al tratar de conectar con el servidor.",
						Toast.LENGTH_LONG).show();
			}
		}
    	
    }

}
