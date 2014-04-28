package sidekick.weather;

import java.util.ArrayList;

import br.com.condesales.EasyFoursquareAsync;
import br.com.condesales.criterias.VenuesCriteria;
import br.com.condesales.listeners.FoursquareVenuesResquestListener;
import br.com.condesales.models.Venue;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class List extends Fragment{
	
	EasyFoursquareAsync async;
	Context context;
	ArrayList<String> venueNames = new ArrayList<String>();
	ArrayList<String> lat = new ArrayList<String>();
	ArrayList<String> lng = new ArrayList<String>();
	
	ListView myList;
	
	SQLiteOpenHelper helper;
	SQLiteDatabase db;
	
	MyLocationListener myLocObj;
	
	SharedPreferences pref;
	SharedPreferences.Editor edt;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		context = getActivity();
		
		View view = inflater.inflate(R.layout.list_layout, container, false);
		myList = (ListView) view.findViewById(R.id.myList);
		
		pref =  context.getSharedPreferences("data", 0);
		
		myLocObj =  new MyLocationListener();
		manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		
		getLocation();
		
		return view;
	}
	
//	@Override
//	public void onResume() {
//		super.onResume();
//		
//		async = new EasyFoursquareAsync(getActivity());
//        requestVenueNearby();
//        
//	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser) {
		
//			Toast.makeText(context, "" + isVisibleToUser, Toast.LENGTH_LONG).show();
			
			SharedPreferences pref = context.getSharedPreferences("data", 0);
			String launch = pref.getString("launch_list", "go");
			
			if(launch.equals("go")){
				async = new EasyFoursquareAsync(getActivity());
			    requestVenueNearby();
			    
			}
		}
		
	}
	
	
	private void requestVenueNearby(){
	    
   	 Location myloc = new Location("");
   	 
//   	  myloc.setLatitude(Double.parseDouble( pref.getString("lat", "0.0") ));
//        myloc.setLongitude(Double.parseDouble( pref.getString("lng", "0.0") ));
        
   	 myloc.setLatitude(18.53);
   	 myloc.setLongitude(73.85);
   	 
        VenuesCriteria myCri = new VenuesCriteria();
        myCri.setLocation(myloc);
        
        async.getVenuesNearby(new FoursquareVenuesResquestListener() {
			
			@Override
			public void onError(String errorMsg) {
				Toast.makeText(context, "error", Toast.LENGTH_LONG).show();
				
			}
			
			@Override
			public void onVenuesFetched(ArrayList<Venue> venues) {
								
				SharedPreferences.Editor edt = pref.edit();
				edt.putString( "launch_list", "no" );
				edt.commit();
				
				Toast.makeText(context, "Success", Toast.LENGTH_LONG).show();
				
				
				helper =  new MySqliteHelper(getActivity(), "MyDB", null, 1);
				db = helper.getWritableDatabase();
				
				
				for(int i=0; i< venues.size(); i++){
					
				venueNames.add(venues.get(i).getName());
				
				br.com.condesales.models.Location loc = venues.get(i).getLocation();
				//String lat = "" + loc.getLat();
				
				double lat = loc.getLat();
				double lng = loc.getLng();
				
				ContentValues row = new ContentValues();
				row.put("lat", lat);
				row.put("lng", lng);
				row.put("name", venues.get(i).getName());
				
				db.insert("Loc", null, row);
			
				myList.setAdapter(new ArrayAdapter<String>(getActivity(), 
						android.R.layout.simple_list_item_1, venueNames));
						
				}
				
				db.close();
			}
        }, myCri);
   	
   }
	
	LocationManager manager;
	AlertDialog dialog;
	AlertDialog.Builder builder;
	boolean gps, nw;	
	
public void getLocation() {
		
		gps = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
		if( gps ){
			Toast.makeText(context, "fetching from GPS", Toast.LENGTH_LONG).show();
			manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 
										0, 0, myLocObj);
		}
		
		nw = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if( nw ){
			Toast.makeText(context, "fetching from NW", Toast.LENGTH_LONG).show();
			manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 
					0, 0, myLocObj);
		}
		
		if(!gps && !nw){
			
//			Toast.makeText( BasicArchitectActivity.this, "Please enable GPS and Network positioning in your Settings ", Toast.LENGTH_LONG ).show();
			
			builder = new AlertDialog.Builder(context);
			builder.setTitle("Error");
			builder.setMessage("Unable to find your current Location");
			builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
					
				}
			});
			
			builder.setNegativeButton("Setting", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
					startActivity(intent);
		
				}
				
			});
			
			
			dialog = builder.create();
			dialog.show();
		}
		
	}
	
	
	class MyLocationListener implements LocationListener{

		double currentlat, currentlng;
		
		@Override
		public void onLocationChanged(Location location) {
			if(location != null ){
				
				currentlat = location.getLatitude();
				currentlng = location.getLongitude();

				edt =  pref.edit();
				edt.putString("lat", "" + currentlat);
				edt.putString("lng", "" + currentlng);
				edt.commit();
			
			}
		}

		@Override
		public void onProviderDisabled(String provider) {}

		@Override
		public void onProviderEnabled(String provider) {}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {}
		
	}// end of MyLocatonListener

}
