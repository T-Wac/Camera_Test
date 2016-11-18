package com.twac.camera;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;

import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

public class CustomCamera extends Activity implements SurfaceHolder.Callback {
	private SurfaceView mPreview;
	private Camera mcamera;
	private SurfaceHolder mHolder;
	private Camera.PictureCallback mPictureCallback = new Camera.PictureCallback() {

		@Override
		public void onPictureTaken(byte[] data, Camera camera) {
			File tempfile = new File("/sdcard/temp.png");
			try {

				FileOutputStream fos = new FileOutputStream(tempfile);
				fos.write(data);
				fos.close();
				Intent intent = new Intent(CustomCamera.this, ResultAty.class);
				intent.putExtra("picPath", tempfile.getAbsolutePath());
				startActivity(intent);
				CustomCamera.this.finish();

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.custom_camera);
		mPreview = (SurfaceView) findViewById(R.id.preview);
		mHolder = mPreview.getHolder();
		mHolder.addCallback(this);
		mPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				mcamera.autoFocus(null);

			}
		});

	}

	public void capture(View v) {
		Camera.Parameters parameters = mcamera.getParameters();
		parameters.setFocusMode(Camera.Parameters.FLASH_MODE_AUTO);
		parameters.setPictureFormat(ImageFormat.JPEG);
		parameters.setPreviewSize(800, 400);
		mcamera.autoFocus(new AutoFocusCallback() {

			@Override
			public void onAutoFocus(boolean success, Camera camera) {
				if (success) {
					mcamera.takePicture(null, null, mPictureCallback);
				}

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mcamera == null) {
			mcamera = getCamera();
			if (mHolder != null) {
				setStartPreview(mcamera, mHolder);
			}
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		releaseCamera();
	}

	// 获取camera对象
	private Camera getCamera() {
		Camera camera;
		try {
			camera = Camera.open();
		} catch (Exception e) {
			camera = null;
			e.printStackTrace();
		}
		return camera;
	}

	// 开始预览相机内容
	public void setStartPreview(Camera camera, SurfaceHolder holder) {
		try {

			camera.setPreviewDisplay(holder);
			// 将camera预览角度进行调整
			camera.setDisplayOrientation(90);
			camera.startPreview();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	// 释放相机资源
	private void releaseCamera() {
		if (mcamera != null) {
			mcamera.setPreviewCallback(null);
			mcamera.stopPreview();
			mcamera.release();
			mcamera = null;
		}

	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		setStartPreview(mcamera, mHolder);
	}

	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		mcamera.stopPreview();
		setStartPreview(mcamera, mHolder);

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		releaseCamera();
	}
}
