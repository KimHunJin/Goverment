package example.test.archbiz2015;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;

import example.test.archbiz2015.adapter.GridViewAdapter;
import example.test.archbiz2015.item.GridViewItem;

public class GirdViewTest extends AppCompatActivity {
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

