package com.sku.archbiz2015.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.sku.archbiz2015.activitys.SecondPageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

/**
 * Created by HunJin on 2015-11-22.
 */
public class NetworkSelectGPS extends AsyncTask<Object, Void, String> {
    Context mContext;
    ProgressDialog mProgress;
    JSONObject jsonObject = null;

    String address;
    String loadName;
    Double latitude;
    Double longitude;

    HttpURLConnection urlConnection;
    BufferedReader in;
    String PHtml = "";
    String urlString;

    ArrayList<String> fileUrlList;

    public NetworkSelectGPS(Context context) {
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
    protected String doInBackground(Object... params) {
        loadName = (String)params[0];
        loadName = loadName.trim();

        Log.e("NetworkSelect","startDoInBackground");
        urlString = "http://54.149.51.26/goverment/searchGPS.php?loadName="+loadName;
        htmlRead(urlString);

        return address;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPostExecute","StartOnPostExecute");
        try {
            jsonObject = new JSONObject(s);
            int cnt = jsonObject.getInt("cnt");
            Log.e("cnt", cnt + "");
            fileUrlList = new ArrayList<>();
            if(cnt < 1) {
//                DataProvider.setHashFile("DefaultImage","https://s3-us-west-2.amazonaws.com/valueupmos/1");
                Toast.makeText(mContext.getApplicationContext(),"주소를 찾지 못했습니다.",Toast.LENGTH_SHORT).show();
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("ret");
                Double latitude = jsonArray.getJSONObject(0).getDouble("위도");
                Double longitude = jsonArray.getJSONObject(0).getDouble("경도");
                new NetworkGetImagePath(mContext).execute(loadName,latitude,longitude);
            }
//            it.putExtra("dataProvider",dataProvider);

            mProgress.dismiss();

        } catch (JSONException je){
            je.printStackTrace();
        }
    }

    protected void htmlRead(String urlString) {
        Log.e("htmlRead", "htmlRead Start");
        Log.e("urlString", urlString);
        address = "";
        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                in = new BufferedReader(new
                        InputStreamReader(urlConnection.getInputStream()));
                while ((PHtml = in.readLine()) != null) {
                    address += PHtml;
                }
            }
            Log.e("address", address);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
