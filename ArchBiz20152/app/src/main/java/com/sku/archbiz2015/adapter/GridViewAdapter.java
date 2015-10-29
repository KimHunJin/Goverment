package com.sku.archbiz2015.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sku.archbiz2015.R;
import com.sku.archbiz2015.item.GridViewItem;
import com.sku.archbiz2015.item.Holder;

import java.util.ArrayList;



/**
 * Created by HunJin on 2015-10-15.
 */
public class GridViewAdapter extends ArrayAdapter{

    private LayoutInflater mInflater = null;  // layinflater로 custom layout 구성
    private Context mContext = null;

    public GridViewAdapter(Context context, int resource, ArrayList<GridViewItem> list) {
        super(context, resource, list);

        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Holder holder;

        if(convertView==null) {
            convertView = mInflater.inflate(R.layout.grid_view_item, null);
            holder = new Holder();
            holder.imageView = (ImageView)convertView.findViewById(R.id.gridViewImage);
            holder.text = (TextView)convertView.findViewById(R.id.gridViewText);
            convertView.setTag(holder);
        }
        // 홀더를 이용하여 저장
        holder = (Holder) convertView.getTag();
        GridViewItem item = (GridViewItem) getItem(position);

        return convertView;
    }
}
