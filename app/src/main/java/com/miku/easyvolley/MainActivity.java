package com.miku.easyvolley;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestParams;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.DownloadRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;

public class MainActivity extends Activity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");
        setContentView(R.layout.activity_main);

        mRequestQueue = Volley.newRequestQueue(this);
        int[] ids = new int[]{R.id.testNormalRequest, R.id.testFileUpload, R.id.testFileDownload, R.id.cancel};
        for (int id : ids) {
            findViewById(id).setOnClickListener(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e(TAG, "onDestroy");
        mRequestQueue.cancelAll(TAG);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.testFileUpload:
                testFileUpload();
                break;
            case R.id.testNormalRequest:
                testNormalString();
                break;
            case R.id.testFileDownload:
                testDownloadRequest();
                break;
            case R.id.cancel:
                mRequestQueue.cancelAll(TAG);
            default:
                break;
        }


    }

    private void testFileUpload() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("session_id", "4095c7d3c0c9240aa6975044d04fa1ea1442306755512");
        params.addBodyParameter("hello_video", new File("/sdcard/rcWish/VideoCapture/20150914_153044.mp4"));
        params.addBodyParameter("hello_pic", new File("/sdcard/rcWish/VideoCapture/20150914_153044.jpg"));

        String url = "http://yourhost/index.php?act=ActorSetting&m=updateVideo";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        request.setLoadingListener(new Response.LoadingListener() {
            @Override
            public void onLoading(boolean isUpload, long total, long current) {
                Log.e(TAG, "isUpload:" + isUpload + ",total:" + total + ",current:" + current);
            }
        });
        request.setRequestParams(params);
        request.setTag(TAG);
        mRequestQueue.add(request);
    }

    private void testNormalString() {
        RequestParams params = new RequestParams();
        params.addBodyParameter("session_id", "4095c7d3c0c9240aa6975044d04fa1ea1442306755512");
        params.addBodyParameter("birthday", "1986-01-01");

        String url = "http://yourhost/index.php?act=User&m=updateMyProfile";
        StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRequestParams(params);
        request.setTag(TAG);
        mRequestQueue.add(request);
    }

    private void testDownloadRequest() {
        String url = "http://yourhost/ff.apk";
        DownloadRequest request = new DownloadRequest(Request.Method.GET, url, "/sdcard/out.apk", false, new Response.Listener<File>() {
            @Override
            public void onResponse(File response) {
                Log.e(TAG, "download finish:" + response.getAbsolutePath());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setLoadingListener(new Response.LoadingListener() {
            @Override
            public void onLoading(boolean isUpload, long total, long current) {
                Log.e(TAG, "isUpload:" + isUpload + ",total:" + total + ",current:" + current);
            }
        });
        request.setTag(TAG);
        mRequestQueue.add(request);
    }
}