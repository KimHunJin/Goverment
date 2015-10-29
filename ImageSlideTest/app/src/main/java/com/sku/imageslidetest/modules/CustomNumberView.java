package com.sku.imageslidetest.modules;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.hkm.slider.SliderTypes.AdvancedTextSliderView;
import com.sku.imageslidetest.R;

/**
 * Created by hesk on 19/8/15.
 */
public class CustomNumberView extends AdvancedTextSliderView<TextView, ImageView> {

    public CustomNumberView(Context context) {
        super(context);
    }

    @Override
    protected int renderedLayoutTextBanner() {
        return R.layout.feature_banner_slide;
    }
}