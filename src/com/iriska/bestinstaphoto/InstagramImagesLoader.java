package com.iriska.bestinstaphoto;

import java.io.IOException;
import java.util.Date;

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

	private String insta_token;
	private String LOGTAG = "instadebug";

	Context context;

	private ProgressDialog dialog;

	/*
	 * @Override protected void onPreExecute() { super.onPreExecute(); dialog =
	 * new ProgressDialog(context); dialog.setTitle("Please wait");
	 * dialog.show(); }
	 */
	public InstagramImagesLoader(Context ctx) {

		dialog = new ProgressDialog(ctx);

		/*
		 * SharedPreferences prefs = PreferenceManager
		 * .getDefaultSharedPreferences(context); if
		 * (prefs.getString("insta_token", "") == "") { InstaLoginDialog
		 * insta_login = new InstaLoginDialog(); Bundle args = new Bundle();
		 * args.putLong("startdate", dates[0]); args.putLong("enddate",
		 * dates[1]); insta_login.setArguments(args);
		 * insta_login.setStyle(DialogFragment.STYLE_NORMAL, 0);
		 * insta_login.setCancelable(false);
		 * insta_login.show(MainFragment.getInstance().getFragmentManager(),
		 * "InstagramLoginDialog"); } else { insta_token =
		 * prefs.getString("insta_token", ""); insta_id =
		 * prefs.getString("insta_id", ""); }
		 */
	}

	// for array which is associated with Gridview
	ImageItem[] images;
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
		String userId = SearchUserId(insta_token, user_name);
		if (user_name != null) {
			query = "https://api.instagram.com/v1/users/" + userId
					+ "/media/recent?access_token=" + insta_token + "&count=-1";
			GetUserMedia(insta_token, query);

			images = GetUserMedia(insta_token, query);
		}
		return images;
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
		int i = 0;
		if (result != null) {
			while (i < result.length) {
				Long seconds = result[i].getCreateddate();
				Long millis = seconds * 1000;
				Date date = new Date(millis);
				Log.d(LOGTAG,
						"images" + i + " result url "
								+ result[i].getThumbnail() + ", date "
								+ date.toGMTString());
				i++;

			}
		}
		// (PictureSelect.instagram);

	}

	// this method gets user Media. uses access token and user id as paramters
	protected ImageItem[] GetUserMedia(String _insta_token, String url) {
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

			images = new ImageItem[imageObjs.length()];
			for (int r = 0; r < imageObjs.length(); r++) {
				// get "images" Object
				images[r] = new ImageItem(imageObjs.getJSONObject(r)
						.getJSONObject("images").getJSONObject("thumbnail")
						.getString("url"), imageObjs.getJSONObject(r)
						.getJSONObject("images")
						.getJSONObject("standard_resolution").getString("url"),
						imageObjs.getJSONObject(r).getLong("created_time"));
				publishProgress(images);

			}

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
			JSONObject data = retrieved.getJSONObject("data");
			user_id = data.getString("id");

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