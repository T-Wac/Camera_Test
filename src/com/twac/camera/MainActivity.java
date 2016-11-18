package com.twac.camera;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends Activity {
	private static int REQ_1 = 1;
	private static int REQ_2 = 2;
	private ImageView image;
	private String mfilePath;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		image = (ImageView) findViewById(R.id.image);
		mfilePath = Environment.getExternalStorageDirectory().getPath();
		mfilePath = mfilePath + "/" + "demo.png";

	}

	public void startCamera(View v) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(intent, REQ_1);
	}

	public void startCamera2(View v) {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		Uri photoUri = Uri.fromFile(new File(mfilePath));
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
		startActivityForResult(intent, REQ_2);
	}

	public void customCamera(View v){
		Intent intent=new Intent(MainActivity.this, CustomCamera.class);
		startActivity(intent);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQ_1) {
				Bundle bundle = data.getExtras();
				Bitmap bitmap = (Bitmap) bundle.get("data");
				image.setImageBitmap(bitmap);
			}

			else if (requestCode == REQ_2) {
				FileInputStream fis = null;
				try {

					fis = new FileInputStream(mfilePath);
					Bitmap bitmap = BitmapFactory.decodeStream(fis);
					image.setImageBitmap(bitmap);
					
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} finally {
					try {
						fis.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

}
