package com.iriska.bestinstaphoto;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InstaLoginActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setTitle(getString(R.string.login_title));

		final WebView mWebView = new WebView(this);
		setContentView(mWebView);

		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

			@Override
			public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
				if (url.contains("access_token")) {

					mWebView.setEnabled(false);
					mWebView.setVisibility(View.GONE);
					String accessToken = url.substring(url.indexOf("#access_token") + 14);

					InstaApp.SavePreference(InstaApp.ACCESS_TOKEN, accessToken);
					
					startActivity(new Intent(InstaLoginActivity.this, MainActivity.class));

				} else {
					super.doUpdateVisitedHistory(view, url, isReload);
				}
			}
		});


		String clientId = getString(R.string.client_id);
		String redirectUri = getString(R.string.redirect_uri);
		String url = "https://instagram.com/oauth/authorize/?client_id=" + clientId +
				"&redirect_uri=" + redirectUri + "&response_type=token";
		mWebView.loadUrl(url);

	}
}
