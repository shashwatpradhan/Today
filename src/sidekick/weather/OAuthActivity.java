package sidekick.weather;

import br.com.condesales.constants.FoursquareConstants;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class OAuthActivity extends Activity implements GetResultInterface{
	
	public static String CLIENT_ID = "POMKQEXC42ADKK34YSC4YLBU1WWDQRCHR23IPOGXNLFZKUXJ";
    public static String CLIENT_SECRET = "T4DFPEMQIQWPW1FELS5NV40GUZI24BCSYRU2IIJHB2WF0KAL";
    public static String CALLBACK_URL = "http://localhost";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_layout);
        
        String url = "https://foursquare.com/oauth2/authenticate" +
					  "?client_id=" +CLIENT_ID+
					  "&response_type=code"+
					  "&redirect_uri=" +CALLBACK_URL;
        
        WebView webview = (WebView)findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        
        webview.loadUrl(url);
        
        webview.setWebViewClient(new myWebClient());
       
        
    }
    
    class myWebClient extends WebViewClient{
    	
    	@Override
    	public boolean shouldOverrideUrlLoading(WebView view, String url) {
    		
    		String accessCodeFragment = "code=";
    		if(url.contains(accessCodeFragment)) {
		        
				String accessCode = url.substring(url.indexOf(accessCodeFragment));
				//System.out.println("AccessCode= " + accessCode);
				//Toast.makeText(OAuthActivity.this, "AccessCode= " + accessCode, Toast.LENGTH_SHORT).show();
            	           		
		
				String code = accessCode.substring(5);
				System.out.println("Code= " + code);
				Toast.makeText(OAuthActivity.this, "Code= " + code, Toast.LENGTH_SHORT).show();
				
        		String address = "https://foursquare.com/oauth2/access_token?client_id=" + CLIENT_ID + "&client_secret=" + CLIENT_SECRET + 
        						"&grant_type=authorization_code" + "&redirect_uri=" + CALLBACK_URL + 
        						"&code=" + code;
        		
        		System.out.println("URL= " + address);
        		
        		new getToken(OAuthActivity.this).execute(address);
        		
        		//webview.postUrl(OAUTH_ACCESS_TOKEN_URL, query.getBytes());
      		
									
        	}
    		
    		return true;
    	}
    }

	@Override
	public void processFinish(String result) {
		
		Toast.makeText(OAuthActivity.this, "Token= " + result, Toast.LENGTH_SHORT).show();
		
		SharedPreferences settings = getSharedPreferences(FoursquareConstants.SHARED_PREF_FILE, 0);
	    SharedPreferences.Editor editor = settings.edit();
	    editor.putString(FoursquareConstants.ACCESS_TOKEN, result);
	    // Commit the edits!
	    editor.commit();
		
		Intent intent = new Intent(OAuthActivity.this, Weather.class);
		startActivity(intent);
		
	}

}
