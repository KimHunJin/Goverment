package com.sku.archbiz2015.activitys;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.hkm.slider.Animations.DescriptionAnimation;
import com.hkm.slider.SliderLayout;
import com.hkm.slider.SliderTypes.BaseSliderView;
import com.hkm.slider.SliderTypes.TextSliderView;
import com.hkm.slider.TransformerL;
import com.hkm.slider.Tricks.ViewPagerEx;
import com.sku.archbiz2015.R;
import com.sku.archbiz2015.utils.NumZero;
import com.sku.archbiz2015.utils.DataProvider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Random;

public class SecondPageActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    private android.support.v7.app.AlertDialog mDialog;

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_ALBUM = 2;
    private static final int REQUEST_IMAGE_CROP = 3;
    private String uriImage;

    private Uri mImageCaptureUri;
    Bitmap photo;

    private SliderLayout mDemoSlider;
    private boolean numbered = false;
    GoogleMap mapSecond;  //구글맵

    /**
     * 슬라이드를 통해 이미지를 처리하는 매서드입니다.
     */
    @SuppressLint("ResourceAsColor")
    private void setupSlider() {
        // remember setup first
        mDemoSlider.setPresetTransformer(TransformerL.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setOffscreenPageLimit(3);
        mDemoSlider.setSliderTransformDuration(400, new LinearOutSlowInInterpolator());
        mDemoSlider.getPagerIndicator().setDefaultIndicatorColor(R.color.red_pink_24, R.color.red_pink_26);
        final NumZero n = new NumZero(this);
        mDemoSlider.setNumLayout(n);
        mDemoSlider.presentation(SliderLayout.PresentationConfig.Numbers);
        mDemoSlider.setPresetTransformer("Accordion");

        //and data second. it is a must because you will except the data to be streamed into the pipline.
//        defaultCompleteSlider(DataProvider.getFileSrcHorizontal());
        defaultCompleteSlider(DataProvider.getFileSrcHorizontal());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void defaultCompleteSlider(final HashMap<String, String> maps) {
        for (String name : maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .enableSaveImageByLongClick(getFragmentManager())
                    .setOnSliderClickListener(this);
            //add your extra information
            textSliderView.getBundle().putString("extra", name);
            mDemoSlider.addSlider(textSliderView);
        }
    }

    /**
     * 메인 메서드를 시작합니다.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_page);
        mDemoSlider = (SliderLayout) findViewById(R.id.slider);

        ImageView imgToolBack = (ImageView)findViewById(R.id.img_tool_back);
        imgToolBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        MapsInitializer.initialize(getApplicationContext());  // 구글맵을 사용하귀 위해 초기화를 시킵니다.

        Intent it = getIntent();
        double latitude = it.getExtras().getDouble("Latitude");
        double longitude = it.getExtras().getDouble("Longitude");

        mapInit(latitude, longitude);

        setupSlider();  // 이미지 슬라이드를 불러옵니다.

        final RelativeLayout reImgPicture = (RelativeLayout)findViewById(R.id.relativePicture);
        reImgPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사진을 찍을 것인지 앨범에서 가져올 것인지를 선택하여 이미지를 처리합니다.
                mDialog = createDialog();
                mDialog.show();
            }
        });

        ImageView imgMoreBuilding = (ImageView) findViewById(R.id.btnMoreBuilding);
        imgMoreBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(getApplication(), GirdViewActivity.class);
                startActivity(it);
            }
        });

        final RelativeLayout reMapShow = (RelativeLayout)findViewById(R.id.relativeMapShow);
        final RelativeLayout reImgShow = (RelativeLayout)findViewById(R.id.relativeImgShow);
        reImgShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoSlider.setVisibility(View.VISIBLE);
                reImgShow.setVisibility(View.INVISIBLE);
                reMapShow.setVisibility(View.VISIBLE);
                reImgPicture.setVisibility(View.VISIBLE);
            }
        });

        reMapShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDemoSlider.setVisibility(View.INVISIBLE);
                reImgPicture.setVisibility(View.VISIBLE);
                reImgShow.setVisibility(View.VISIBLE);
                reMapShow.setVisibility(View.INVISIBLE);
            }
        });

        Button btnValue1 = (Button)findViewById(R.id.btnValue1);
        Button btnValue2 = (Button)findViewById(R.id.btnValue2);
        Button btnValue3 = (Button)findViewById(R.id.btnValue3);
        Button btnValue4 = (Button)findViewById(R.id.btnValue4);

        btnValue1.setText((int)(100*Math.random()+1)+"");
        btnValue2.setText((int)(100*Math.random()+1)+"");
        btnValue3.setText((int)(100*Math.random()+1)+"");
        btnValue4.setText((int)(100*Math.random()+1)+"");

    }

    /**
     * 지도를 초기화시킵니다.
     * 초기화된 지도는 현재 위치를 마커로 찍습니다.
     *
     * @param latitude
     * @param longitude
     */
    private void mapInit(double latitude, double longitude) {
        final LatLng latLng = new LatLng(latitude,longitude);

        mapSecond = ((MapFragment) getFragmentManager().findFragmentById(R.id.mapSecond)).getMap();
        mapSecond.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mapSecond.animateCamera(CameraUpdateFactory.zoomTo(17));

        mapSecond.getUiSettings().setScrollGesturesEnabled(false);
        mapSecond.getUiSettings().setZoomControlsEnabled(false);

        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title("위치");
        options.snippet("위도 : " + latitude + "\n경도 : " + longitude);
        uriImage = latitude + "_" + longitude;
        Log.e("Lat", latitude + "  " + longitude);

        mapSecond.addMarker(options).showInfoWindow();
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this, slider.getBundle().get("extra") + "", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    // dialog를 띄워 선택을 합니다.
    private android.support.v7.app.AlertDialog createDialog() {
        final android.support.v7.app.AlertDialog.Builder mAB = new android.support.v7.app.AlertDialog.Builder(this);
        mAB.setTitle("사진선택하기");
        mAB.setCancelable(true);
        mAB.setNeutralButton("사진 촬영", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                doTakePhotoAction();
//                finish();
            }
        })
                .setNegativeButton("앨범에서 가져오기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        doTakeAlbumAction();
//                        Intent mIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                        startActivityForResult(mIntent, REQUEST_IMAGE_ALBUM);
                    }
                })
                .setPositiveButton("취소하기", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setDismiss(mDialog);
                    }
                });
        return mAB.create();
    }

    private void setDismiss(Dialog dia) {
        if (dia != null && dia.isShowing())
            dia.dismiss();
    }

    /**
     * 카메라 호출 하기
     */
    private void doTakePhotoAction() {
        Log.i("profile", "doTakePhotoAction()");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Crop된 이미지를 저장할 파일의 경로를 생성
        mImageCaptureUri = createSaveCropFile();
        intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
                mImageCaptureUri);
        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE);
    }

    /**
     * 앨범 호출 하기
     */
    private void doTakeAlbumAction() {
        Log.i("profile", "doTakeAlbumAction()");
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, REQUEST_IMAGE_ALBUM);
    }

    /**
     * Crop된 이미지가 저장될 파일을 만든다.
     *
     * @return Uri
     */
    private Uri createSaveCropFile() {
        Uri uri;
        String url = "tmp_" + uriImage + ".jpg";
        Log.i("url" , url);
        uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/BigData/",
                url));
        return uri;
    }

    /**
     * Result Code
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("profile", "onActivityResultX");
        if (resultCode != RESULT_OK) {
            Log.i("result code",requestCode+"");
            return;
        }

        switch (requestCode) {
            case REQUEST_IMAGE_ALBUM: {
                Log.d("profile", "PICK_FROM_ALBUM");

                // 이후의 처리가 카메라와 같으므로 일단 break없이 진행합니다.
                // 실제 코드에서는 좀더 합리적인 방법을 선택하시기 바랍니다.
                mImageCaptureUri = data.getData();
                File original_file = getImageFile(mImageCaptureUri);

                mImageCaptureUri = createSaveCropFile();
                File cpoy_file = new File(mImageCaptureUri.getPath());

                // SD카드에 저장된 파일을 이미지 Crop을 위해 복사한다.
                copyFile(original_file, cpoy_file);
            }

            case REQUEST_IMAGE_CAPTURE: {
                Log.d("profile", "PICK_FROM_CAMERA");

                // 이미지를 가져온 이후의 리사이즈할 이미지 크기를 결정합니다.
                // 이후에 이미지 크롭 어플리케이션을 호출하게 됩니다.

                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(mImageCaptureUri, "image/*");

                // //// 이부분이 크롭할 사이즈 결정.
                intent.putExtra("outputX", 144);
                intent.putExtra("outputY", 112);
                // intent.putExtra("aspectY", 16);
                intent.putExtra("scale", true);
                intent.putExtra("noFaceDetection", true);

                // ////
                // Crop한 이미지를 저장할 Path
                intent.putExtra("output", mImageCaptureUri);

                // Return Data를 사용하면 번들 용량 제한으로 크기가 큰 이미지는
                // 넘겨 줄 수 없다.
                // intent.putExtra("return-data", true);
                startActivityForResult(intent, REQUEST_IMAGE_CROP);

                break;
            }

            case REQUEST_IMAGE_CROP: {
                Log.w("profile", "CROP_FROM_CAMERA");

                // Crop 된 이미지를 넘겨 받습니다.
                Log.w("profile", "mImageCaptureUri = " + mImageCaptureUri);

                String full_path = mImageCaptureUri.getPath();
                String[] s_path = full_path.split("/");
                // mnt 가 있는 경우다.
                int index = s_path[0].length() + 1;

                String photo_path = full_path.substring(index, full_path.length());

                Log.w("profile", "비트맵 Image path = " + photo_path);

                BitmapFactory.Options options = new BitmapFactory.Options();
                for (options.inSampleSize = 1; options.inSampleSize <= 32; options.inSampleSize++) {
                    try {
                        photo = BitmapFactory.decodeFile(photo_path, options);
                        Log.d("TAG_LOG", "Decoded successfully for sampleSize "
                                + options.inSampleSize);
                        break;
                    } catch (OutOfMemoryError outOfMemoryError) {
                        // If an OutOfMemoryError occurred, we continue with for
                        // loop and next inSampleSize value
                        Log.e("TAG_LOG",
                                "outOfMemoryError while reading file for sampleSize "
                                        + options.inSampleSize
                                        + " retrying with higher value");
                    }
                }

                // photo = BitmapFactory.decodeFile(photo_path);
                // img.setImageBitmap(photo);
                // img_phoz.setBackgroundDrawable(new BitmapDrawable(photo));
                // // 네트워크로 보낸다.
                // new NetworkSetImage().execute();

                Toast.makeText(getApplicationContext(),"사진완료",Toast.LENGTH_SHORT).show();
                break;
            }
        }
    }

    /**
     * 선택된 uri의 사진 Path를 가져온다. uri 가 null 경우 마지막에 저장된 사진을 가져온다.
     *
     * @param uri
     * @return
     */
    private File getImageFile(Uri uri) {
        Log.i("getIamgeFile", "getImageFile");
        String[] projection = { MediaStore.Images.Media.DATA };
        if (uri == null) {
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor mCursor = getContentResolver().query(uri, projection, null,
                null, MediaStore.Images.Media.DATE_MODIFIED + " desc");
        if (mCursor == null || mCursor.getCount() < 1) {
            return null; // no cursor or no record
        }
        int column_index = mCursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        String path = mCursor.getString(column_index);

        if (mCursor != null) {
            mCursor.close();
            mCursor = null;
        }

        return new File(path);
    }

    /**
     * 파일 복사
     *
     * @param srcFile
     *            : 복사할 File
     * @param destFile
     *            : 복사될 File
     * @return
     */
    public static boolean copyFile(File srcFile, File destFile) {
        boolean result = false;
        try {
            InputStream in = new FileInputStream(srcFile);
            try {
                result = copyToFile(in, destFile);
            } finally {
                in.close();
            }
        } catch (IOException e) {
            result = false;
        }
        return result;
    }

    /**
     * Copy data from a source stream to destFile. Return true if succeed,
     * return false if failed.
     */
    private static boolean copyToFile(InputStream inputStream, File destFile) {
        try {
            OutputStream out = new FileOutputStream(destFile);
            try {
                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) >= 0) {
                    out.write(buffer, 0, bytesRead);
                }
            } finally {
                out.close();
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
