package com.example.connectionexample;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Actividad para crear un nuevo hotel.
 * 
 * @author sanrodari
 */
public class NewHotel extends Activity {

	/**
	 * Objeto que nos permite hacer las conexiones.
	 */
    private Connector connector = new Connector();
    
    /**
     * Campo de texto para el nombre.
     */
    private EditText nameEdit;
    
    /**
     * Campo de texto para el valor de la reserva por noche.
     */
    private EditText reservationValueEdit;

	@Override
    public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_hotel);

		nameEdit             = (EditText) findViewById(R.id.hotelNameEdit);
		reservationValueEdit = (EditText) findViewById(R.id.reservationValueEdit);
    }
    
	/**
	 * Cuando se ejecuta el botón de creación de un nuevo hotel se delega esto
	 * a la tarea NewHotel que es la encargada de interactuar con el WS.
	 * @param view
	 */
    public void createNewHotelClick(View view) {
		if (connector.checkConnectivity(this) && validateForm()) {
			// TODO Acá se debe realizar una verificación de la entrada de los
			// usuarios para evitar ataques de inyección de código.
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
    
    /**
     * Tarea encarga de obtener los datos del formulario de nuevo hotel y 
     * enviarlos al WS de creación de nuevo hotel. 
     * @author sanrodari
     */
    private class NewHotelTask extends AsyncTask<String, Void, String> {
    	
    	/**
    	 * Configura y realiza la petición POST con los parámetros ingresados
    	 * por el usuario.
    	 */
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
			} catch (Exception e) {
				e.printStackTrace();
				
				Log.e("ConnectionExample", "Error al interpertrar la respuesta del servidor.");
				return null;
			}
    	}
    	
    	/**
    	 * Notifica el resultado al usuario y finaliza la actividad.
    	 */
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
