package com.iriska.bestinstaphoto;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.print.PrintHelper;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

public class CollageActivity extends Activity {
	GridView gvCollage;
	Button btnPrint;

	ArrayList<String> images = new ArrayList<String>();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collage_activity);

		images = getIntent().getExtras().getStringArrayList("images");
		gvCollage = (GridView) findViewById(R.id.gvChoosePicture);
		gvCollage.setAdapter(new InstaImageAdapter(this, images));

		btnPrint = (Button) findViewById(R.id.btnPrint);
		btnPrint.setOnClickListener(printButtonClicked);
	}

	public OnClickListener printButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			printPhoto();
		}
	};

	private void printPhoto() {
		PrintHelper photoPrinter = new PrintHelper(CollageActivity.this);
		photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
		gvCollage.setDrawingCacheEnabled(true);
		gvCollage.buildDrawingCache();
		Bitmap bitmap = Bitmap.createBitmap(gvCollage.getDrawingCache());

		photoPrinter.printBitmap("MostPopular", bitmap);
		gvCollage.setDrawingCacheEnabled(false);
		
		Toast.makeText(CollageActivity.this,
				R.string.print_status, Toast.LENGTH_SHORT)
				.show();
	}

}
