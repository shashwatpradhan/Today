package sidekick.weather;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;

public class getToken extends AsyncTask<String, Void, String>{

	String token;
	
	GetResultInterface interfaceObj = null;
	
	//TextView txtRating, txtPlaceName, txtIsOpen, txtAddress, txtTips;
	//public Info interfaceObj;
	
	public getToken(GetResultInterface interfaceObj) {
		
		this.interfaceObj = interfaceObj;
		
	}
	
	@Override
	protected String doInBackground(String... address) {
		
		HttpGet httpGet = new HttpGet(address[0]);
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
			
			JSONObject jObj = new JSONObject(buffer.toString());
			token = jObj.getString("access_token");
			
			return token;
			
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		
		if(result != null)
			interfaceObj.processFinish(result);
	}

}
