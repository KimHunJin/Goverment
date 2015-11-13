package com.sku.archbiz2015.activitys;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;

import com.sku.archbiz2015.R;
import com.sku.archbiz2015.adapter.GridViewAdapter;
import com.sku.archbiz2015.item.GridViewItem;

import java.util.ArrayList;


public class GirdViewActivity extends AppCompatActivity {
    ArrayList<GridViewItem> gridArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gird_view_test);

        GridView gridView = (GridView)findViewById(R.id.gridView);
        gridArrayList = new ArrayList<>();
        GridViewAdapter gridViewAdapter = new GridViewAdapter(this, R.layout.grid_view_item, gridArrayList);
        gridView.setAdapter(gridViewAdapter);

        for(int i=0;i<10;i++) {
            gridArrayList.add(new GridViewItem());
        }
        gridViewAdapter.notifyDataSetChanged();
    }
}

