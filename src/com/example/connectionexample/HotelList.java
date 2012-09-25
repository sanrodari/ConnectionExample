package com.example.connectionexample;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Actividad que tiene la lista de hoteles recuperados.
 * 
 * @author sanrodari
 */
public class HotelList extends Activity {

	/**
	 * Widget para mostrar la lista de hoteles.
	 */
	private ListView hotelList;
	
	/**
	 * Objeto que nos permite hacer las conexiones.
	 */
	private Connector connector = new Connector();
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotel_list);
        
        hotelList = (ListView) findViewById(R.id.hotelList);
    }

    /**
     * Se realiza la carga de hoteles por medio de la tarea QueryHotels.
     */
    @Override
    protected void onStart() {
    	super.onStart();
    	
    	if(connector.checkConnectivity(this)) {
    		new QueryHotelsTask().execute();
    	}
    	// Mostrar el error de no haber conectividad.
    	else {
    		Toast.makeText(this,
    				"No hay conectividad, no se ha podido cargar la información.",
    				Toast.LENGTH_LONG).show();
    	}
    }
    
    /**
     * Se navega a la actividad para crear nuevos hoteles.
     * @param view
     */
    public void newHotelClick(View view) {
    	Intent intent = new Intent(this, NewHotel.class);
    	startActivity(intent);
	}
    
    /**
     * Tarea que realiza la consulta de los hoteles. 
     * 
     * @author sanrodari
     */
    private class QueryHotelsTask extends AsyncTask<Void, Void, String[]> {
    	
    	/**
    	 * Se hace la petición al WS.
    	 */
		@Override
		protected String[] doInBackground(Void... voids) {
			try {
				List<Hotel> recoveryHotels = new ArrayList<Hotel>();
				
				String hotelsJsonString = connector.doGet("http://androidexample.phpfogapp.com/index.php?/hotels/all");
				if(hotelsJsonString == null) {
					return new String[]{ "Error al tratar de conectar con el servidor." };
				}
				
				JSONArray hotelsArray = new JSONArray(hotelsJsonString);
				
				for (int i = 0; i < hotelsArray.length(); i++) {
					JSONObject jsonHotel = hotelsArray.getJSONObject(i);
					
					Hotel hotel = new Hotel();
					
					hotel.setId(jsonHotel.getLong("id"));
					hotel.setName(jsonHotel.getString("name"));
					
					if(!jsonHotel.isNull("value_reservation")) {
						hotel.setValueReservation(jsonHotel.getDouble("value_reservation"));
					}
					else {
						hotel.setValueReservation(0.0);
					}
					
					recoveryHotels.add(hotel);
				}
				
				String[] hotels = new String[recoveryHotels.size()];
				for (int i = 0; i < hotels.length; i++) {
					Hotel hotel = recoveryHotels.get(i);
					hotels[i] = "Hotel: " + hotel.getName() + ", valor: " + hotel.getValueReservation(); 
				}
				
				return hotels;
			} catch (JSONException e) {
				e.printStackTrace();
				
				Log.e("ConnectionExample", "Error al interpretrar la respuesta del servidor.");
				Toast.makeText(HotelList.this,
						"Error al interpertrar la respuesta del servidor.",
						Toast.LENGTH_LONG).show();
				return new String[]{"Error al interpertrar la respuesta del servidor."};
			}
		}

		/**
		 * Se pone la lista con la información.
		 */
		@Override
		protected void onPostExecute(final String[] result) {
			hotelList.setAdapter(new BaseAdapter() {
				@Override
				public View getView(int position, View convertView, ViewGroup arg2) {
					TextView textView;
	        		if (convertView == null) {
						textView = new TextView(getApplicationContext());
	        		} else {
	        			textView = (TextView) convertView;
	        		}
					
	        		textView.setTextColor(Color.rgb(0, 0, 0));
	        		textView.setText(result[position]);
					return textView;
				}
				
				@Override
				public long getItemId(int arg0) {
					return arg0;
				}
				
				@Override
				public Object getItem(int arg0) {
					return arg0;
				}
				
				@Override
				public int getCount() {
					return result.length;
				}
			});
		}
    	
    }

}
