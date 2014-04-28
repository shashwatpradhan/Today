package sidekick.weather;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class MyThread extends AsyncTask<String, ArrayList<String>, ArrayList<String>>{
	
	Context context;
	String proceed = "false", desc, icon, temp, cod; 
	ArrayList<String> result = new ArrayList<String>();
	ProgressDialog dialog;
	
	GetResultInterface interfaceObj = null;
	TextView txtHome;
	
	
	public MyThread(Context context, TextView txtHome) {
		this.context = context;
		this.txtHome = txtHome;
		
	}
	
	
//	public MyThread(Context context, GetResultInterface interfaceObj) {
//		this.context = context;
//		this.interfaceObj = interfaceObj;
//		
//	}
	
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		
		dialog = new ProgressDialog(context);
		dialog.setMessage("Loading...");
		dialog.setCancelable(false);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.show();
		
	}


	@SuppressWarnings("unchecked")
	@Override
	protected ArrayList<String> doInBackground(String... address) {
		String add = address[0];
		System.out.println("Address= " + add);
		
		HttpGet httpGet = new HttpGet(add);
		HttpClient httpClient = new DefaultHttpClient();
		
		try {
			
			HttpResponse httpResponse = httpClient.execute(httpGet);
			InputStream in = httpResponse.getEntity().getContent();
			
			int ch;
			StringBuffer buffer = new StringBuffer();
			while((ch = in.read())!= -1){
				buffer.append((char)ch);
			}
			
			in.close();
			
			System.out.println("*********************************" +
			   				   "\nProceding to JSON");
			
			try {
				JSONObject jWeather = new JSONObject(buffer.toString());
				JSONArray jWeatherArray = jWeather.getJSONArray("weather");
				
				JSONObject jWeatherObj = jWeatherArray.getJSONObject(0);
				desc = jWeatherObj.getString("description");
				icon = jWeatherObj.getString("icon");
				
				JSONObject jMain = jWeather.getJSONObject("main");
				temp = jMain.getString("temp");
				
				cod = jWeather.getString("cod");
				
				proceed = "true";
				result.add(proceed); // 0
				result.add(desc);    // 1
				result.add(icon);    // 2
				result.add(temp);  	 // 3
				result.add(cod);	 // 4
				
				return result;
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			
			
		result.add(0, proceed);
		
		//publishProgress(result);
		
		return result;
	}// end of DoInBackground
	
	
	@Override
	protected void onPostExecute(ArrayList<String> result) {
		super.onPostExecute(result);
		
		if(result.get(0).equals("true")){
			
			dialog.dismiss();
			
			txtHome.append("\n" + result.get(1) +
						   "\n" + result.get(2) +
						   "\n" + result.get(3) +
						   "\n" + result.get(4) );
			
			SharedPreferences pref =  context.getSharedPreferences("data", 0);
			SharedPreferences.Editor edt = pref.edit();
			edt.putString( "desc", result.get(1) );
			edt.putString( "icon", result.get(2) );
			edt.putString( "temp", result.get(3) );
			edt.putString( "cod", result.get(4) );
			edt.putString( "launch", "no" );
			edt.commit();
		
		}
		else{
			dialog.dismiss();
			Toast.makeText(context, "result not found...", Toast.LENGTH_SHORT).show();
		}
		
//		if(result.get(0).equals("true")){
//			dialog.dismiss();
//			txtHome.append("\n" + result.get(1) +
//						   "\n" + result.get(2) +
//						   "\n" + result.get(3) +
//						   "\n" + result.get(4) );
//
//		}
//		else{
//			dialog.dismiss();
//			Toast.makeText(context, "result not found...", Toast.LENGTH_SHORT).show();
//		}
	
		
		
	}
		

}// end of MyThread
