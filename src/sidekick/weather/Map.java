package sidekick.weather;

import java.util.ArrayList;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Map extends Fragment{
	
	Context context;
	private GoogleMap googleMap;
	
	SQLiteOpenHelper helper;
	SQLiteDatabase db;
	
	double lati,lngi;
	
	ArrayList<String> venueNames = new ArrayList<String>();
	ArrayList<String> lat = new ArrayList<String>();
	ArrayList<String> lng = new ArrayList<String>();
	
	SharedPreferences pref;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		context = getActivity();
		
		View view = inflater.inflate(R.layout.map_layout, container, false);
		
		pref =  context.getSharedPreferences("data", 0);
		
		
		return view;
	}
	
	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);

		if (isVisibleToUser) {
			
			SharedPreferences pref = context.getSharedPreferences("data", 0);
			String launch = pref.getString("launch_map", "go");
			
			if(launch.equals("go")){
			
				initilizeMap();
				loadVenues();
											    
			}
		}
		
	}
	
	private void loadVenues(){
		
		helper = new MySqliteHelper(context, "MyDB", null, 1);
		db = helper.getWritableDatabase();
		
		Cursor result = db.query("Loc", null, null, null, null, null, null);
		while (result.moveToNext()){
		
			
			lat.add( "" + result.getDouble(0) );
			lng.add( "" + result.getDouble(1) );
			venueNames.add( result.getString(2) );
			
														
		}
		
		db.close();
		
		for (int i = 0; i < lat.size(); i++) {
			
		lati = Double.parseDouble( lat.get(i) );
		lngi = Double.parseDouble( lng.get(i) );
		
		MarkerOptions marker = new MarkerOptions()
        					   .position( new LatLng(lati, lngi) )
        					   .title(venueNames.get(i))
        					   .icon( BitmapDescriptorFactory.fromResource(R.drawable.marker) );
         
        googleMap.addMarker(marker);
        
		}
		
		MarkerOptions mymarker = new MarkerOptions()
							   .position( new LatLng(Double.parseDouble(pref.getString("lat", "0.0")), 
									   				 Double.parseDouble(pref.getString("lng", "0.0"))) )
							   .title("Me")
							   .icon( BitmapDescriptorFactory.fromResource(R.drawable.marker) );

		googleMap.addMarker(mymarker);
		
		CameraPosition cameraPosition = new CameraPosition.Builder()
		.target( new LatLng( Double.parseDouble(pref.getString("lat", "0.0")), 
							 Double.parseDouble(pref.getString("lng", "0.0")) ))
		.zoom(12)
		.build();

		googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

		
        SharedPreferences pref =  context.getSharedPreferences("data", 0);
		SharedPreferences.Editor edt = pref.edit();
		edt.putString( "launch_map", "no" );
		edt.commit();
	}
	
	private void initilizeMap() {
        if (googleMap == null) {
            googleMap =  ((SupportMapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
 
            // check if map is created successfully or not
            if (googleMap == null) {
                Toast.makeText(context,
                        "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                        .show();
            }
        }
	}

	public void onDestroyView() 
	 {
	    super.onDestroyView(); 
	    Fragment fragment = (getFragmentManager().findFragmentById(R.id.map));  
	    FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
	    ft.remove(fragment);
	    ft.commit();
	}

}
