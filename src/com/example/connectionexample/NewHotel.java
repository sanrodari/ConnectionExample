package com.example.connectionexample;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewHotel extends Activity {

    private Connector connector = new Connector();
    
    private EditText nameEdit;
    
    private EditText reservationValueEdit;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_hotel);

		nameEdit             = (EditText) findViewById(R.id.hotelNameEdit);
		reservationValueEdit = (EditText) findViewById(R.id.reservationValueEdit);
    }
    
    public void createNewHotelClick(View view) {
		if (connector.checkConnectivity(this) && validateForm()) {
			new NewHotelTask().execute(
				nameEdit.getText().toString(), reservationValueEdit.getText().toString());
		}
		// Mostrar el error de no haber conectividad.
		else if(!connector.checkConnectivity(this)) {
			Toast.makeText(this,
				"No hay conectividad, no se ha podido cargar la información.",
				Toast.LENGTH_LONG).show();
		}
		// Si no ha escrito un nombre para el nuevo hotel.
		else {
			Toast.makeText(this,
					"El campo del nombre es requerido.",
					Toast.LENGTH_LONG).show();
		}
	}
    
    private class NewHotelTask extends AsyncTask<String, Void, String> {
    	@Override
    	protected String doInBackground(String... params) {
    		try {
	    		String url = "http://androidexample.phpfogapp.com/index.php?/hotels/insertHotel";
	    		List<NameValuePair> postParams = new ArrayList<NameValuePair>();
	    		
	    		// Primer parámetro se recibe el nombre, en el segundo se recibe
	    		// el valor de la reserva.
	    		postParams.add(new BasicNameValuePair("name", params[0]));
	    		postParams.add(new BasicNameValuePair("valueReservation", params[1]));
	    		
				String postResponse = connector.doPost(url, postParams);
    		
				JSONObject responseJson = new JSONObject(postResponse);
				
				if(responseJson.getBoolean("success")) {
					return "success";
				}
				else {
					return responseJson.getString("message");
				}
			} catch (JSONException e) {
				e.printStackTrace();
				
				Log.e("ConnectionExample", "Error al interpertrar la respuesta del servidor.");
				return null;
			}
    	}
    	
    	@Override
    	protected void onPostExecute(String result) {
    		// Cuando ocurrió un error al interpretar la respuesta.
    		if(result == null) {
    			Toast.makeText(NewHotel.this,
    					"Error al interpertrar la respuesta del servidor.",
    					Toast.LENGTH_LONG).show();
    		}
    		// Cuando se creó correctamente.
    		else if(result.equalsIgnoreCase("success")) {
    			Toast.makeText(NewHotel.this,
    					"El nuevo hotel ha sido creado exitosamente.",
    					Toast.LENGTH_LONG).show();
    		}
    		// Cuando se recibió un mensaje de error.
    		else {
    			Toast.makeText(NewHotel.this,
    					"El siguiente error ocurrió en el servidor: " + result + ".",
    					Toast.LENGTH_LONG).show();
    		}
    		
    		// Finaliza la actividad.
    		finish();
    	}
    	
    }
    
    private boolean validateForm() {
    	return !TextUtils.isEmpty(nameEdit.getText().toString());
    }
    
}
