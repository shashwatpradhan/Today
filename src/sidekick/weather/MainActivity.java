package sidekick.weather;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	TextView txt1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_main_layout);
				
		//Button bt
		
		LoginButton authButton = (LoginButton) findViewById(R.id.authButton);
		
		authButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// start Facebook Login
				  Session.openActiveSession(MainActivity.this, true, new Session.StatusCallback() {

				    // callback when session changes state
				    @Override
				    public void call(Session session, SessionState state, Exception exception) {

				    	if (session.isOpened()) {
				    		
				    		Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {

				    			  // callback after Graph API response with user object
				    			  @Override
				    			  public void onCompleted(GraphUser user, Response response) {
				    				  
				    				  if (user != null) {
				    					  Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
				    					 // TextView welcome = (TextView) findViewById(R.id.welcome);
				    					  //welcome.setText("Hello " + user.getName() + "!");
				    					}
				    			  }
				    			});
				    	}
				    	
				    }
				  });
				
			}
		});
		
	}
	
	@Override
	  public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
	  }

}
