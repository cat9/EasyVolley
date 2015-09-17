# EasyVolley

**EasyVolley** library,Improve from google Volley,make it easier to program,for upload files,download files.

##Usage
-------------------
###Normal Request
```
RequestParams params = new RequestParams();
params.addBodyParameter("session_id", "4095c7d3c0c9240aa6975044");
params.addBodyParameter("birthday", "1986-01-01");

String url = "http://yourhost/index.php?act=User&m=updateProfile";
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
```
-------------------
###FilesUpload Request
```
RequestParams params = new RequestParams();
params.addBodyParameter("session_id", "4095c7d3c0c9240aa6975044");
params.addBodyParameter("hello_video", new File("/sdcard/20150914_153044.mp4"));
params.addBodyParameter("hello_pic", new File("/sdcard/20150914_153044.jpg"));

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
```
-------------------
###FileDownload Request
FileDownload support continue download where you canceled it.
```
    DownloadRequest(int method, String url, String fileSavePath, boolean autoResume,
    Response.Listener<File> listener, Response.ErrorListener errorListener);

```

```
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
```

##Request cancel
you can cancel the request by using:
```
request.cancel();
```
or **cancel all request by TAG**,this can be very useful when your **Activity** is about to destroy!
```
mRequestQueue.cancelAll(TAG);
```
