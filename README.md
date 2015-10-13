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
if you have any problem,welcome to contact me via my email:lxr309@gmail.com


# EasyVolley

**EasyVolley**库是Google Volley的改良版本，它简化了请求参数处理，添加对文件的**上传**及**下载**的支持，其他相似库，例如**VolleyPlus**等，它们是把要上传及下载的东西，全部读取到内存，然后再发送出去或保存到文件，这必然会出现内存损耗过多的问题，如果是大文件，手机必然处理不过来。

##用法
-------------------
###普通请求
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
###文件上传请求
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
###文件下载请求
文件下载支持断点续传机制，参数autoResume
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

##取消请求
你可以在任何地方使用如下方法取消请求。
```
request.cancel();
```
或者使用**TAG**来取消请求。当你的Activity要被Destory时，把该Activity的相关请求取消掉，是一个相当好的习惯！
```
mRequestQueue.cancelAll(TAG);
```
如果有任何问题，欢迎讨论解决：lxr309@gmail.com

