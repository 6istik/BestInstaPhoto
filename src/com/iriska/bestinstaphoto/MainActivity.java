package com.iriska.bestinstaphoto;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {
	EditText txtUserName;
	Button btnGo;
	String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		txtUserName = (EditText) findViewById(R.id.textUserName);
		btnGo = (Button) findViewById(R.id.btnGoCollage);
		
		String accessToken = InstaApp.GetPreference().getString(
				InstaApp.ACCESS_TOKEN, null);
		if (accessToken == null) {
			startActivity(new Intent(this, InstaLoginActivity.class));
			return;
		}
		
		String previous_username = InstaApp.GetPreference().getString(
				InstaApp.PREFERENCE_USERNAME, null);
		if (previous_username != null) {
			txtUserName.setText(previous_username);
		}
		btnGo.setOnClickListener(goButtonClicked);
	}

	public OnClickListener goButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {

			username = txtUserName.getText().toString();
			if (username.matches("")) {
				Toast.makeText(MainActivity.this,
						R.string.error_empty_username, Toast.LENGTH_SHORT)
						.show();
				return;
			}
			InstaApp.SavePreference(InstaApp.PREFERENCE_USERNAME, username.toString());
			InstagramImagesLoader insta = new InstagramImagesLoader(MainActivity.this, txtUserName, btnGo);
			insta.execute(username);
		}
	};

}
