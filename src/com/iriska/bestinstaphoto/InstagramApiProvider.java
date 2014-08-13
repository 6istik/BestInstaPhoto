package com.iriska.bestinstaphoto;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Works with Instagram Api
 *
 */
public class InstagramApiProvider {
	List<ImageItem> images = new ArrayList<ImageItem>();
	
	
	/**
	 * @param _insta_token instagram acces token for oauth
	 * @param url query url
	 * @param num_images num of images to return
	 * @return list of filled ImageItem
	 */
	protected List<ImageItem> GetUserMedia(String _insta_token, String url, int num_images) {
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

			for (int r = 0; r < imageObjs.length(); r++) {
				// get "images" Object
				JSONObject currentItem = imageObjs.getJSONObject(r);
				images.add(new ImageItem(currentItem.getJSONObject("images")
						.getJSONObject("thumbnail").getString("url"),
						currentItem.getJSONObject("images")
								.getJSONObject("standard_resolution")
								.getString("url"), currentItem.getJSONObject(
								"likes").getInt("count")));
			}
			Collections.sort(images);
			if (images.size() > num_images)
			{
				images.subList(0, images.size() - num_images).clear();
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

	
	/** Search userId by username if exist
	 * @param insta_token instagram access token
	 * @param username username to search
	 * @return UserId as String
	 */
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
			if (data != null && data.length() > 0) {
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
