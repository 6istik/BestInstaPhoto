package com.iriska.bestinstaphoto;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InstagramImagesLoader extends
		AsyncTask<String, ImageItem[], ImageItem[]> {

	Context context;
	int numofbestimages = 6;
	ArrayList<String> sources = new ArrayList<String>();
	ArrayList<Integer> likes = new ArrayList<Integer>();
	EditText txtUserName;
	Button btnGoCollage;
	private ProgressDialog dialog;
	boolean isErrorOccured = false;
	String errorMessage;
	List<ImageItem> images = new ArrayList<ImageItem>();
	String accessToken;
	
	public InstagramImagesLoader(Context ctx, EditText userEditText, Button btnGo) {
		dialog = new ProgressDialog(ctx);
		context = ctx;
		txtUserName = userEditText;
		btnGoCollage = btnGo;
	}
	
	protected void onPreExecute() {
		accessToken = InstaApp.GetPreference().getString(InstaApp.ACCESS_TOKEN,
				null);
		btnGoCollage.setEnabled(false);
		txtUserName.setEnabled(false);
		dialog.setMessage("Подождите, идет загрузка...");
		dialog.show();
	}

	@Override
	protected ImageItem[] doInBackground(String... username) {

		String user_name = username[0];
		String query;
		InstagramApiProvider provider = new InstagramApiProvider();
		String userId = provider.SearchUserId(accessToken, user_name);
		if (userId != null) {
			query = "https://api.instagram.com/v1/users/" + userId
					+ "/media/recent?access_token=" + accessToken + "&count=-1";

			images = provider.GetUserMedia(accessToken, query, numofbestimages);
		} else {
			isErrorOccured = true;
			errorMessage = String.format("Пользователя %s не существует в Instagram", user_name) ;
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
		btnGoCollage.setEnabled(true);
		txtUserName.setEnabled(true);
		
		if (isErrorOccured)
		{
			Toast.makeText(context,
					errorMessage, Toast.LENGTH_LONG)
					.show();
			return;
		}else startCollage();
	}

	private void startCollage() {
		sources.clear();
		likes.clear();
		ListIterator<ImageItem> li = images.listIterator(images.size());
		while(li.hasPrevious()) {
			ImageItem item = li.previous();
			sources.add(item.getSource());
			likes.add(item.getLikesCount());
		}
		
		Intent intent = new Intent(context, CollageActivity.class);
		intent.putStringArrayListExtra("images", sources);
		intent.putIntegerArrayListExtra("likes", likes);
		context.startActivity(intent);
	}
}