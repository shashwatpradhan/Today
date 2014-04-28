package sidekick.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class UserInfo extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userinfo_layout);
		
		TextView txtInfo = (TextView) findViewById(R.id.txtInfo);
		
		Intent intent = getIntent();
		
		txtInfo.setText( "id= "+ intent.getStringExtra("id") + "\nname= " +
						 intent.getStringExtra("name") + "\nlink= " +
						 intent.getStringExtra("link") + "\nusername= " +
						 intent.getStringExtra("username") + "\nbday= " +
						 intent.getStringExtra("Bday") + "\nlocation= " +
						 intent.getStringExtra("lname") + "\nlocale= " +
						 intent.getStringExtra("locale")	
					   );
	}
	
}
