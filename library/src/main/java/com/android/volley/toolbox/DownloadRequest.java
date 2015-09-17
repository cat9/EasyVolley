package com.android.volley.toolbox;

import com.android.volley.AuthFailureError;
import com.android.volley.CanceledError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyLog;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by miku on 15-9-15.
 */
public class DownloadRequest extends Request<File> {

    private final Response.Listener<File> mListener;
    private final String mSavePath;
    private boolean mAutoResume;

    public DownloadRequest(String url, String fileSavePath, Response.Listener<File> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, fileSavePath, false, listener, errorListener);
    }

    public DownloadRequest(int method, String url, String fileSavePath, boolean autoResume, Response.Listener<File> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
        mSavePath = fileSavePath;
        mAutoResume = autoResume;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (mAutoResume) {
            File downloadFile = new File(mSavePath);
            long fileLen = 0;
            if (downloadFile.isFile() && downloadFile.exists()) {
                fileLen = downloadFile.length();
            }
            if (fileLen > 0) {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("RANGE", "bytes=" + fileLen + "-");
                return headers;
            }
        }
        return super.getHeaders();
    }

    public boolean isSupportRange(final HttpResponse response) {
        if (response == null) return false;
        Header header = response.getFirstHeader("Accept-Ranges");
        if (header != null) {
            return "bytes".equals(header.getValue());
        }
        header = response.getFirstHeader("Content-Range");
        if (header != null) {
            String value = header.getValue();
            return value != null && value.startsWith("bytes");
        }
        return false;
    }

    @Override
    public byte[] handleRawResponse(HttpResponse httpResponse) throws IOException, ServerError, CanceledError {
        int statusCode = httpResponse.getStatusLine().getStatusCode();

        if (statusCode < 300) {
            mAutoResume = mAutoResume && isSupportRange(httpResponse);

            HttpEntity entity = httpResponse.getEntity();

            File targetFile = new File(mSavePath);

            if (!targetFile.exists()) {
                File dir = targetFile.getParentFile();
                if (dir.exists() || dir.mkdirs()) {
                    targetFile.createNewFile();
                }
            }

            long current = 0;
            BufferedInputStream bis = null;
            BufferedOutputStream bos = null;
            try {
                FileOutputStream fileOutputStream = null;
                if (mAutoResume) {
                    current = targetFile.length();
                    fileOutputStream = new FileOutputStream(mSavePath, true);
                } else {
                    fileOutputStream = new FileOutputStream(mSavePath);
                }
                long total = entity.getContentLength() + current;
                bis = new BufferedInputStream(entity.getContent());
                bos = new BufferedOutputStream(fileOutputStream);

                if (isCanceled()) {
                    throw new CanceledError();
                }
                postProgress(false, current, total);

                byte[] tmp = new byte[4096];
                int len;
                while ((len = bis.read(tmp)) != -1) {
                    bos.write(tmp, 0, len);
                    current += len;
                    if (isCanceled()) {
                        throw new CanceledError();
                    }
                    postProgress(false, current, total);
                }
                bos.flush();
                postProgress(false, total, total);
                return mSavePath.getBytes();
            } finally {
                try {
                    // Close the InputStream and release the resources by "consuming the content".
                    entity.consumeContent();
                } catch (IOException e) {
                    // This can happen if there was an exception above that left the entity in
                    // an invalid state.
                    VolleyLog.v("Error occured when calling consumingContent");
                }

                closeQuietly(bos);
                closeQuietly(bis);
            }
        } else if(statusCode==416) {
            throw new IOException("may be the file have been download finished.");
        }else{
            throw new IOException();
        }
    }

    public void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable e) {
            }
        }
    }

    @Override
    protected Response<File> parseNetworkResponse(NetworkResponse response) {
        String filename = new String(response.data);
        if (response.data.length > 0) {
            return Response.success(new File(filename), null);
        } else {
            return Response.error(new ParseError(response));
        }
    }

    @Override
    protected void deliverResponse(File response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }
}
