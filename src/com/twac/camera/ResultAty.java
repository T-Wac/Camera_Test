package com.twac.camera;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.ImageView;

public class ResultAty extends Activity {
	private ImageView imageCapture;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.result);
		imageCapture = (ImageView) findViewById(R.id.image_capture);
		String path = getIntent().getStringExtra("picPath");

		try {
			FileInputStream fis = new FileInputStream(path);
			Matrix matrix = new Matrix();
			matrix.setRotate(90);
			Bitmap bitmap = BitmapFactory.decodeStream(fis);
			bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
					bitmap.getHeight(), matrix, true);
			imageCapture.setImageBitmap(bitmap);
			fis.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Bitmap bitmap = BitmapFactory.decodeFile(path);
		// imageCapture.setImageBitmap(bitmap);
	}

}
