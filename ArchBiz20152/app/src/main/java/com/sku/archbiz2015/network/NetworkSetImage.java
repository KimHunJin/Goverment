package com.sku.archbiz2015.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Created by HunJin on 2015-11-21.
 */
public class NetworkSetImage extends AsyncTask<Object, Void, String> {

    Context mContext;
    ProgressDialog mProgress;
    JSONObject jsonObject = null;

    String loadName;
    String time;
    Bitmap photo;
    String address = "";

    public NetworkSetImage(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage("Please Waiting");
        mProgress.show();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        Toast.makeText(mContext.getApplicationContext(),"등록 되었습니다.",Toast.LENGTH_SHORT).show();
        mProgress.dismiss();
    }

    @Override
    protected String doInBackground(Object... params) {

        loadName = (String)params[0];
        photo = (Bitmap)params[1];
        time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date(System.currentTimeMillis()));

        Log.e("photo",photo.toString());
        Log.e("loadName",loadName);
        Log.e("time",time);

        try {
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setParameter("http.connection.timeout", 10000);
            HttpPost httpPost = null;

            httpPost = new HttpPost("http://54.149.51.26/goverment/setImageServer.php");
            MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            reqEntity.addPart("loadName", new StringBody(loadName, Charset.defaultCharset()));
            reqEntity.addPart("time", new StringBody(time, Charset.defaultCharset()));

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            Log.e("photo", photo.toString());

            byte[] data = bos.toByteArray();
            ByteArrayBody bab = new ByteArrayBody(data, time +".jpeg");  // need change img name (ex. time.jpeg)
            reqEntity.addPart("img", bab);
            Log.e("bab", bab.toString());
            Log.e("photo", data.toString());

            httpPost.setEntity(reqEntity);
            HttpResponse response = httpClient.execute(httpPost);

            // 1. 버퍼로 json 읽기.
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"), 8);
            Log.e("reader","success reader");
            StringBuilder builder = new StringBuilder();
            Log.e("Build","success Build");
            for (String line = null; (line = reader.readLine()) != null;) {
                builder.append(line).append("\n");
            }

            Log.e("return", "json : " + builder.toString());

            // 2. json 형식으로 만들기.
            jsonObject = new JSONObject(builder.toString());
            if(jsonObject.getInt("err")==0) {
                address = 0+"";
            }
            Log.e("err",jsonObject.getInt("err")+"");
            Log.e("jObject",builder.toString());

        } catch(Exception e) {
            e.printStackTrace();
        }
        return address;
    }
}
