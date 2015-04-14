package co.extrastrength.shadycam;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.View;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;

import co.extrastrength.shadycam.util.CompareSizesByArea;

public class GLSLCameraView extends GLSurfaceView implements GLSLRenderer.SurfaceListener {
    private static String TAG = "GLSLCameraView";

    // Camera Specific Vars
    private String mCameraId;
    private SurfaceTexture mCameraSurfaceTexture;
    private Size mPreviewSize;

    private Handler mCaptureHandler = new Handler();
    private Handler mBackgroundHandler = new Handler();

    public GLSLRenderer mRenderer;
    public GLSLCameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setEGLContextClientVersion(2);
        mRenderer = new GLSLRenderer();
        mRenderer.setSurfaceListener(this);
        setRenderer(mRenderer);
        setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float percent = event.getX() / v.getWidth();
                mRenderer.setTouchPercent(percent);
                return true;
            }
        });

    }

    void tryCreatePreview() {
        if (mCameraSurfaceTexture != null)
            createCameraPreviewSession();
    }
    @Override
    public void gotSurfaceTexture(SurfaceTexture st) {
        mCameraSurfaceTexture = st;
        tryCreatePreview();
    }

    private void createCameraPreviewSession() {
        try {
            mCameraSurfaceTexture.setDefaultBufferSize(1024, 768);
            Surface surface = new Surface(mCameraSurfaceTexture);
            Camera c = Camera.open();
            c.setDisplayOrientation(90);
            c.setPreviewTexture(mCameraSurfaceTexture);
            c.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
