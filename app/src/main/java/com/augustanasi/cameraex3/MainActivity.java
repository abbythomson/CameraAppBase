package com.augustanasi.cameraex3;

import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import java.io.FileOutputStream;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {
    Camera camera;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    Button start;
    Button stop;
    Button take;
    Camera.PictureCallback rawCallback;
    Camera.ShutterCallback shutterCallback;
    Camera.PictureCallback pngCallback;


    private final String tag = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        start = (Button)findViewById(R.id.startbtn);
        stop = (Button)findViewById(R.id.stopbtn);
        take = (Button)findViewById(R.id.takebtn);
        take.setVisibility(View.INVISIBLE);

        start.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View arg){
                startCamera();
            }
        });
        stop.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View view){
                stopCamera();
            }
        });
        take.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
              // captureImage();
            }
        });

        surfaceView = (SurfaceView)findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        rawCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Log.d("Log","onPictureTaken - raw");
            }
        };

        shutterCallback = new Camera.ShutterCallback() {
            @Override
            public void onShutter() {
                Log.i("Log","onshutter'd");
            }
        };

       /* pngCallback = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                FileOutputStream outputStream = null;
                try{
                    outputStream = new FileOutputStream(String.format("currentPhoto.png",System.currentTimeMillis()));
                    outputStream.write(data);
                    outputStream.close();
                    Log.d("Log", "onPictureTaken - wrote bytes: "+data.length);
                }
            }
        }*/
    }

    private void startCamera(){
        if(Camera.getNumberOfCameras()>0){
            if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)){
                camera = Camera.open(0);
            }else{
                camera = Camera.open();
            }

        }else{
            return;
        }

        Camera.Parameters param;
        param = camera.getParameters();

        param.setPreviewFrameRate(20);
        param.setPreviewSize(surfaceView.getWidth(),surfaceView.getHeight());
        camera.setParameters(param);

        try{
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e){
            Log.e(tag, "init_camera: "+e);
            return;
        }
    }

    private void stopCamera(){
        camera.stopPreview();
        camera.release();
        surfaceView.clearFocus();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

    }

    public void surfaceChanged(SurfaceHolder holder, int arg1, int arg2, int arg3){

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

}
