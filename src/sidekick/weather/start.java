package sidekick.weather;

import android.app.Application;
import android.content.SharedPreferences;

public class start extends Application{
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		SharedPreferences pref = getSharedPreferences("data", 0);
		SharedPreferences.Editor edt = pref.edit();
		edt.putString("launch_list", "go");
		edt.commit();
		
	}

}
