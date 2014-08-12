package com.iriska.bestinstaphoto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class InstagramImagesLoader extends
		AsyncTask<String, ImageItem[], ImageItem[]> {

	private String LOGTAG = "instadebug";
	Context context;
	int numofbestimages = 6;

	private ProgressDialog dialog;

	/*
	 * @Override protected void onPreExecute() { super.onPreExecute(); dialog =
	 * new ProgressDialog(context); dialog.setTitle("Please wait");
	 * dialog.show(); }
	 */
	public InstagramImagesLoader(Context ctx) {

		dialog = new ProgressDialog(ctx);

			}

	// for array which is associated with Gridview
	List<ImageItem> images = new ArrayList<ImageItem>();

	//ImageItem[] images;
	String accessToken;

	protected void onPreExecute() {
		accessToken = InstaApp.GetPreference().getString(InstaApp.ACCESS_TOKEN,
				null);
		dialog.setMessage("Loading...");
		dialog.show();
	}

	@Override
	protected ImageItem[] doInBackground(String... username) {

		String user_name = username[0];
		String query;
		String userId = SearchUserId(accessToken, user_name);
		if (userId != null) {
			query = "https://api.instagram.com/v1/users/" + userId
					+ "/media/recent?access_token=" + accessToken + "&count=-1";
			
			images = GetUserMedia(accessToken, query);
			
		}
		return images.toArray(new ImageItem[images.size()]);
	}

	@Override
	protected void onProgressUpdate(ImageItem[]... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(ImageItem[] result) {
		super.onPostExecute(result);
		if (dialog.isShowing()) {
			dialog.dismiss();
			dialog = null;
		}
	}

	// this method gets user Media. uses access token and user id as paramters
	protected List<ImageItem> GetUserMedia(String _insta_token, String url) {
		// Creating HTTP client
		DefaultHttpClient httpClient = new DefaultHttpClient();

		// Creating HTTP Post
		String post_url = url;
		HttpGet httpget = new HttpGet(post_url);
		// Making HTTP Request
		try {

			HttpResponse response = httpClient.execute(httpget);
			String responseBody = EntityUtils.toString(response.getEntity());
			JSONObject retrieved = new JSONObject(responseBody);
			JSONArray imageObjs = retrieved.getJSONArray("data");

			//images = new ImageItem[imageObjs.length()];
			for (int r = 0; r < imageObjs.length(); r++) {
				// get "images" Object
				JSONObject currentItem = imageObjs.getJSONObject(r);
				images.add( new ImageItem(
						currentItem.getJSONObject("images").getJSONObject("thumbnail").getString("url"), 
						currentItem.getJSONObject("images").getJSONObject("standard_resolution").getString("url"),
						currentItem.getJSONObject("likes").getInt("count")));
			}
			Collections.sort(images);
			
			images.subList( 0, images.size() - 1 - numofbestimages).clear();


		} catch (ClientProtocolException e) {
			// writing exception to log
			e.printStackTrace();

		} catch (IOException e) {
			// writing exception to log
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return images;
	}

	// Get userid by username
	protected String SearchUserId(String insta_token, String username) {
		String user_id = null;
		DefaultHttpClient httpClient = new DefaultHttpClient();

		String post_url = "https://api.instagram.com/v1/users/search?q="
				+ username + "&access_token=" + insta_token;
		HttpGet httpget = new HttpGet(post_url);

		try {

			HttpResponse response = httpClient.execute(httpget);
			String responseBody = EntityUtils.toString(response.getEntity());
			JSONObject retrieved = new JSONObject(responseBody);
			JSONArray data = retrieved.getJSONArray("data");
			if (data!= null && data.length()>0)
			{
				user_id = data.getJSONObject(0).getString("id");
			}
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return user_id;
	}
}