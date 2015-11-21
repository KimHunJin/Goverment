package com.sku.archbiz2015.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ProgressBar;

import com.sku.archbiz2015.activitys.SecondPageActivity;
import com.sku.archbiz2015.utils.DataProvider;

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
import java.util.Objects;

/**
 * Created by HunJin on 2015-11-21.
 */
public class NetworkGetImagePath extends AsyncTask<Object, Void, String> {

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

    public NetworkGetImagePath(Context context) {
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

        latitude = (Double)params[1];
        longitude = (Double)params[2];

        urlString = "http://54.149.51.26/goverment/getImageLoad.php?loadName="+loadName;
        htmlRead(urlString);

        return address;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        try {
            jsonObject = new JSONObject(s);
            int cnt = jsonObject.getInt("cnt");
            Log.e("cnt",cnt+"");
            fileUrlList = new ArrayList<>();
            if(cnt < 1) {
//                DataProvider.setHashFile("DefaultImage","https://s3-us-west-2.amazonaws.com/valueupmos/1");
                fileUrlList.add("https://s3-us-west-2.amazonaws.com/valueupmos/1");
            } else {
                JSONArray jsonArray = jsonObject.getJSONArray("ret");

                for(int i=0;i<cnt;i++) {
                    String mFIleName = jsonArray.getJSONObject(i).getString("파일_명");
                    mFIleName = mFIleName.trim();
                    try {
                        mFIleName = URLEncoder.encode(mFIleName,"utf-8");
                        fileUrlList.add("https://s3-us-west-2.amazonaws.com/valueupmos/"+mFIleName);
                        Log.e("파일명", mFIleName);
                    } catch(UnsupportedEncodingException ue) {
                        ue.printStackTrace();
                    }
                }
            }

            Intent it = new Intent(mContext, SecondPageActivity.class);
            it.putExtra("Latitude", latitude);
            it.putExtra("Longitude",longitude);
            it.putExtra("GPSName",loadName);
            it.putExtra("fileUrlList", fileUrlList);
            mContext.startActivity(it);
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
