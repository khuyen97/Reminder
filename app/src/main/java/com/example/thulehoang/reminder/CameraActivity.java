//package com.example.thulehoang.reminder;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.Camera;
//import android.graphics.SurfaceTexture;
//import android.hardware.camera2.CameraManager;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.Environment;
//import android.view.TextureView;
//import android.view.View;
//import android.view.ViewTreeObserver;
//import android.widget.ImageView;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.text.SimpleDateFormat;
//
///**
// * Created by thulehoang on 20/03/2018.
// */
//
//public class CameraActivity implements TextureView.SurfaceTextureListener{
//    private Camera camera;
//    private TextureView previewCamera;
//    private int widthPreview;
//    private int heightPreview;
//    private boolean hasPreviewSize;
//
//    private onCameraReadyListener onCameraReadyListener;
//    private int cameraID;
//
//    public CameraManager(TextureView textureView){
//        this.previewCamera = textureView;
//        previewCamera.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//                if(hasPreviewSize){
//                    return;
//                }
//                widthPreview = previewCamera.getWidth();
//                heightPreview = previewCamera.getHeight();
//                hasPreviewSize = true;
//                previewCamera.setSurfaceTextureListener(CameraManager.this);
//                onCameraReadyListener.onCameraReady();
//            }
//        });
//        cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
//    }
//
//    public void open() {
//        if (camera == null) {
//            camera = Camera.open(cameraID);
//
//            //config preview size
//            //config picture
//            Camera.Parameters parameters = camera.getParameters();
//            Camera.Size size = getBestCameraSize(parameters.getSupportedPreviewSizes());
//            parameters.setPreviewSize(size.width, size.height);
//            parameters.setPictureSize(size.width, size.height);
//            // parameters.setPictureFormat(ImageFormat.JPEG);
//            // parameters.setJpegQuality(100);
//            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
//
//            camera.setParameters(parameters);
//            camera.setDisplayOrientation(90);
//        }
//    }
//
//    private Camera.Size getBestCameraSize(List<Camera.Size> sizes) {
//        Camera.Size result = null;
//        float ratio = widthPreview > heightPreview
//                ? (float) widthPreview / heightPreview :
//                (float) heightPreview / widthPreview;
//        float offset = 0.0f;
//
//        for (Camera.Size size : sizes) {
//            int w = size.width;
//            int h = size.height;
//            float r = w > h ? (float) w / h : (float) h / w;
//            float delta = Math.abs(ratio - r);
//            if (delta <= offset) {
//                result = size;
//                offset = delta;
//
//            }
//        }
//        if (result == null) {
//            int multiple = widthPreview * heightPreview;
//            offset = Integer.MAX_VALUE;
//            for (Camera.Size size : sizes) {
//                int w = size.width;
//                int h = size.height;
//                int m = w * h;
//                int delta = Math.abs(multiple - m);
//                if (delta < offset) {
//                    result = size;
//                    offset = delta;
//                }
//            }
//        }
//
//        return result;
//    }
//
//    public void close() {
//        if (camera != null) {
//            camera.stopPreview();
//            camera.release();
//            camera = null;
//        }
//    }
//
//    public void toggle() {
//        cameraID = (cameraID + 1) % 2;
//        close();
//        open();
//
//        onSurfaceTextureAvailable(previewCamera.getSurfaceTexture(), widthPreview, heightPreview);
//    }
//
//    public void capturePicture(View view) {
//        takePicture();
//    }
//
//    public void takePicture() {
//        camera.takePicture(null, null, new Camera.PictureCallback() {
//            @Override
//            public void onPictureTaken(byte[] bytes, Camera camera) {
//                //Convert
//                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//
//                //Save to storage
//                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_hhmmss");
//                String fileName = sdf.format(System.currentTimeMillis()) + ".jpg";
//
//                String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + Environment.DIRECTORY_PICTURES + "/"
//                        + fileName;
//                try {
//                    File file = new File(path);
//                    FileOutputStream fos = new FileOutputStream(new File(path));
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
//
//                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//                    Uri contentUri = Uri.fromFile(file);
//                    intent.setData(contentUri);
//                    previewCamera.getContext().sendBroadcast(intent);
//
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//                camera.startPreview();
//            }
//        });
//    }
//
//    @Override
//    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int widthPreview, int heightPreview) {
//        try {
//            camera.setPreviewTexture(surfaceTexture);
//            camera.startPreview();
//            camera.autoFocus(null);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int widthPreview, int heightPreview) {
//    }
//
//    @Override
//    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
//        return true;
//    }
//
//    @Override
//    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
//    }
//
//    public void setOnCameraReadyListener(CameraManger.onCameraReadyListener onCameraReadyListener) {
//        this.onCameraReadyListener = onCameraReadyListener;
//    }
//
//    public interface onCameraReadyListener {
//        void onCameraReady();
//    }
//
//}
