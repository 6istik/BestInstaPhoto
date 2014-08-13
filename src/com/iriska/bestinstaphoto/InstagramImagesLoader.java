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

	private String LOGTAG = "instadebug";
	Context context;
	int numofbestimages = 6;
	ArrayList<String> sources = new ArrayList<String>();
	EditText txtUserName;
	Button btnGoCollage;
	private ProgressDialog dialog;
	boolean isErrorOccured = false;
	String errorMessage;
	/*
	 * @Override protected void onPreExecute() { super.onPreExecute(); dialog =
	 * new ProgressDialog(context); dialog.setTitle("Please wait");
	 * dialog.show(); }
	 */
	public InstagramImagesLoader(Context ctx, EditText userEditText, Button btnGo) {

		dialog = new ProgressDialog(ctx);
		context = ctx;
		txtUserName = userEditText;
		btnGoCollage = btnGo;
	}

	List<ImageItem> images = new ArrayList<ImageItem>();

	String accessToken;

	protected void onPreExecute() {
		accessToken = InstaApp.GetPreference().getString(InstaApp.ACCESS_TOKEN,
				null);
		btnGoCollage.setEnabled(false);
		txtUserName.setEnabled(false);
		dialog.setMessage("���������, ���� ��������...");
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
		} else 
		{
			isErrorOccured = true;
			errorMessage = String.format("������������ %s �� ���������� � Instagram", user_name) ;
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
		ListIterator<ImageItem> li = images.listIterator(images.size());
		while(li.hasPrevious()) {
			sources.add(li.previous().getSource());
		}
		Intent intent = new Intent(context, CollageActivity.class);
		//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putStringArrayListExtra("images", sources);
		context.startActivity(intent);
	}
}