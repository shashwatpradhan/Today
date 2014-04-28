package sidekick.weather;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.facebook.HttpMethod;
import com.facebook.Request;
import com.facebook.RequestBatch;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionLoginBehavior;
import com.facebook.SessionState;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphUser;
import com.google.gson.JsonObject;

import android.R.string;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Toast;

public class Startup extends Activity {
	
	Button btnLoginFB, btnLoginFS;
	
	String CLIENT_ID="2NGPK5MXGAWV4VT4Y3VIWEDZUDLQYVNHXD50OEWWO3K4V1OK";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startup_layout);
		
		btnLoginFB =  (Button) findViewById(R.id.btnLoginFB);
		btnLoginFS =  (Button) findViewById(R.id.btnLoginFS);
		
		btnLoginFB.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				Session.openActiveSession(Startup.this, true, new Session.StatusCallback() {
					
					@Override
					public void call(Session session, SessionState state, Exception exception) {
						
//						curl \
//					    -F 'access_token=…' \
//					    -F 'batch=[{"method":"GET", "relative_url":"me"},{"method":"GET", "relative_url":"me/friends?limit=50"}]' \
//					    https://graph.facebook.com
						
						if(session.isOpened()){
						
						RequestBatch myBatchReq = new RequestBatch();
//						myBatchReq.add(new Request(session, "/me", null, HttpMethod.GET, new Request.Callback() {
//							
//							@Override
//							public void onCompleted(Response response) {
//								
//								System.out.println(response.toString());
//								
//					        	try {
//					        		GraphObject user = response.getGraphObject();
//						        	JSONObject obj = user.getInnerJSONObject();
//									Toast.makeText(Startup.this, obj.getString("name") +" - "+ obj.getString("birthday"), Toast.LENGTH_LONG).show();
//								} catch (JSONException e) {
//									
//									e.printStackTrace();
//								}
//								
//							}
//						}));
						
//						String accessToken = session.getAccessToken();
						myBatchReq.add(new Request(session, "/me/friends", getRequestParameters(), HttpMethod.GET, new Request.Callback() {
							
							@Override
							public void onCompleted(Response response) {
								
								System.out.println(response.toString());
								
								try {
									GraphObject user = response.getGraphObject();
									JSONObject jObj = user.getInnerJSONObject();
									JSONArray jDataArray = jObj.getJSONArray("data");
									
									for(int i=0; i<jDataArray.length(); i++){
										JSONObject frndObj = jDataArray.getJSONObject(i);
										String bday = "empty";
										
										if( frndObj.has("birthday") ){
											bday = frndObj.getString("birthday");
										}
										Toast.makeText(Startup.this, frndObj.getString("name") +" - "+ bday , Toast.LENGTH_LONG).show();
									}
								} catch (JSONException e) {
									
									e.printStackTrace();
								}
							}
						}));
						
						
						
//						myBatchReq.addCallback(new RequestBatch.Callback() {
//							
//							@Override
//							public void onBatchCompleted(RequestBatch batch) {
//
//								System.out.println( batch.toString() );
//							
//								
//							}
//						});
						myBatchReq.executeAsync();
						
						}
//						new Request(
//							    session,
//							    "/me",
//							    null,
//							    HttpMethod.GET,
//							    new Request.Callback() {
//							        public void onCompleted(Response response) {
//							        	
//							        	GraphObject user = response.getGraphObject();
//							        	JSONObject obj = user.getInnerJSONObject();
//							        	try {
//											Toast.makeText(Startup.this, obj.getString("name") +" - "+ obj.getString("birthday"), Toast.LENGTH_LONG).show();
//										} catch (JSONException e) {
//											// TODO Auto-generated catch block
//											e.printStackTrace();
//										}
//							            
////							        	Toast.makeText(Startup.this, response.toString(), Toast.LENGTH_LONG).show();
////							        	System.out.println(response.toString());
//							        	
////							        	try {
////											JSONObject obj = new JSONObject(response.toString());
////											Toast.makeText(Startup.this, obj.getString("name") +"-"+ obj.getString("birthday"), Toast.LENGTH_LONG).show();
////										} catch (JSONException e) {
////											// TODO Auto-generated catch block
////											e.printStackTrace();
////										}
//							        }
//							    }
//							).executeAsync();
						
					}
				});
				
			}
		});
				
				
		btnLoginFS.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				startActivity(new Intent().setClass(v.getContext(),
						      OAuthActivity.class));
				
			}
		});
		
	}// end of onCreate
	
	private Bundle getRequestParameters() {
		
		Bundle parameters = new Bundle(1);
	    parameters.putString("fields", "name, birthday");
	    return parameters;
	      
	}
		
	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }
	

	
}
