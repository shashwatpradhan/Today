package sidekick.weather;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Home extends Fragment {
	
	ArrayList<String> result = new ArrayList<String>();
	TextView txtHome;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.home_layout, container, false);
		txtHome = (TextView) view.findViewById(R.id.txtHome);
		
		String uri = "http://api.openweathermap.org/data/2.5/weather?lat=18.53&lon=73.85";
		
		Context context = getActivity();
		
		SharedPreferences pref = context.getSharedPreferences("data", 0);
		String launch = pref.getString("launch", "go");

		if(launch.equals("go")){
			
			MyThread obj = new MyThread(context, txtHome);
			obj.execute(uri);
			
			
		}
		else{
			//SharedPreferences pref = context.getSharedPreferences("data", 0);
			
			txtHome.append("\n" + pref.getString("desc", "N/A") +
					       "\n" + pref.getString("icon", "N/A") +
					       "\n" + pref.getString("temp", "N/A") +
					        "\n" + pref.getString("cod", "N/A") );
		}
				
		return view;
	}
	

}
