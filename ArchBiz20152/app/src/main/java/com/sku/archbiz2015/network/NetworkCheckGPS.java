package com.sku.archbiz2015.network;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.sku.archbiz2015.activitys.MapSelectActivity;
import com.sku.archbiz2015.activitys.SecondPageActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by HunJin on 2015-11-17.
 * 모든 네트워크 통신을 이곳에서 처리해보려고 합니다.
 */
    public class NetworkCheckGPS extends AsyncTask<Object, Void, String> {

    Context mContext;
    ProgressDialog mProgress;
    JSONObject jsonObject = null;

    int paramNumber;
    Double mLatitude;
    Double mLongitude;
    Double mValue;
    String mGPSName;

    String address = "";
    HttpURLConnection urlConnection;
    BufferedReader in;
    String PHtml = "";
    String urlString;

    public NetworkCheckGPS(Context context) {
        mContext = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mProgress = new ProgressDialog(mContext);
        init();
        mProgress.setMessage("Please Waiting");
        mProgress.show();
    }

    @Override
    protected String doInBackground(Object... params) {

        paramNumber = (int) params[0];
        switch (paramNumber) {

            case 1: {
                // 현재 GPS값이 있는지 없는지를 체크
                mLatitude = (Double) params[1];
                mLongitude = (Double) params[2];
                mValue = (Double) params[3];

                urlString = "http://54.149.51.26/api/selectMyLocation.php?lat=" + mLatitude + "&lot=" + mLongitude + "&value=" + mValue;
//                urlString = "http://54.149.51.26/api/selectMyLocation.php?lat=37.48884140&lot=126.85964810&value=0.0001";
                Log.e("urlString", urlString);
                htmlRead(urlString);
                break;
            }
            case 2: {

            }

        }

        return address;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        Log.e("onPost", "onPostExecute Start");

        if (paramNumber == 1) {
            Log.e("String2",s);
            try {
                jsonObject = new JSONObject(s);
                int cnt = jsonObject.getInt("cnt");
                Log.e("cnt", cnt+"");
                if (cnt < 1) {  // 현재 오차에서 검색되는 것이 없다면
                    mValue += 0.17;
                    Log.e("urlString", urlString);
                    cancel(true);
                    this.cancel(true);
                    onCancelled();
                    Log.e("cancel",isCancelled()+"");
                    new NetworkCheckGPS(mContext).execute(1, mLatitude, mLongitude, mValue);
                    mProgress.dismiss();
                } else if (cnt == 1) {
                    mLatitude = jsonObject.getJSONArray("ret").getJSONObject(0).getDouble("위도");
                    mLongitude = jsonObject.getJSONArray("ret").getJSONObject(0).getDouble("경도");
//                    mGPSName = jsonObject.getJSONArray("ret").getJSONObject(0).getString("도로명_대지_위치");
                    mGPSName = jsonObject.getJSONArray("ret").getJSONObject(0).getString("대지_위치");
                    Log.e("GPS", mLatitude + "   " + mLongitude);
                    Intent it = new Intent(mContext, SecondPageActivity.class);
                    it.putExtra("Latitude", mLatitude);
                    it.putExtra("Longitude", mLongitude);
                    it.putExtra("GPSName", mGPSName);
                    mContext.startActivity(it);
                    mProgress.dismiss();
                } else if (cnt > 1) {
                    Log.e("cnt>1",cnt+"");
                    JSONArray jArray = jsonObject.getJSONArray("ret");
                    Log.e("jArray", jArray.toString());
//                    ArrayList<GPSItem> mGPSList = new ArrayList<>();
                    ArrayList<Double> mLatitudeGPS = new ArrayList<>();
                    ArrayList<Double> mLongitudeGPS = new ArrayList<>();
                    ArrayList<String> mGPSNameList = new ArrayList<>();
                    Log.e("GPSListArray","GPS");
                    for(int i=0;i<jArray.length();i++) {
                        JSONObject jsonGPS = jArray.getJSONObject(i);
                        mLatitudeGPS.add(jsonGPS.getDouble("위도"));
                        mLongitudeGPS.add(jsonGPS.getDouble("경도"));
//                        mGPSNameList.add(jsonGPS.getString("도로명_대지_위치"));
                        mGPSNameList.add(jsonGPS.getString("대지_위치"));
/*                        GPSItem gpsItem = new GPSItem();
                        gpsItem.setmLatitude(jsonGPS.getDouble("위도"));
                        gpsItem.setmLongitude(jsonGPS.getDouble("경도"));
                        mGPSList.add(gpsItem);*/

                    }
                    Log.e("mContext",mContext+"");
                    Intent it = new Intent(mContext, MapSelectActivity.class);
                    it.putExtra("Latitude",mLatitudeGPS);
                    it.putExtra("Longitude",mLongitudeGPS);
                    it.putExtra("GPSName",mGPSNameList);
                    mContext.startActivity(it);
                    mProgress.dismiss();

                }
            } catch (JSONException je) {
                je.printStackTrace();
            }
        } else if(paramNumber == 2) {
        }
    }

    protected void htmlRead(String urlString) {
        Log.e("htmlRead","htmlRead Start");
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

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }

    protected void init() {
        Log.e("init","init start");
        mLatitude = 0.0;
        mLongitude = 0.0;
        PHtml = "";
        urlString = "";
    }
}
