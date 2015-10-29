package com.sku.imageslidetest.Util;

import android.util.Log;
import android.view.View;

import com.hkm.slider.Animations.BaseAnimationInterface;
import com.sku.imageslidetest.R;

public class ChildAnimationExample implements BaseAnimationInterface {

    private final static String TAG = "ChildAnimationExample";

    @Override
    public void onPrepareCurrentItemLeaveScreen(View current) {
        View descriptionLayout = current.findViewById(R.id.ns_desc_frame);
        if (descriptionLayout != null) {
            current.findViewById(R.id.ns_desc_frame).setVisibility(View.INVISIBLE);
        }
        Log.e(TAG, "onPrepareCurrentItemLeaveScreen called");
    }

    @Override
    public void onPrepareNextItemShowInScreen(View next) {
        View descriptionLayout = next.findViewById(R.id.ns_desc_frame);
        if (descriptionLayout != null) {
            next.findViewById(R.id.ns_desc_frame).setVisibility(View.INVISIBLE);
        }
        Log.e(TAG, "onPrepareNextItemShowInScreen called");
    }

    @Override
    public void onCurrentItemDisappear(View view) {
        Log.e(TAG, "onCurrentItemDisappear called");
    }

    @Override
    public void onNextItemAppear(View view) {

        View descriptionLayout = view.findViewById(R.id.ns_desc_frame);
        if (descriptionLayout != null) {
            view.findViewById(R.id.ns_desc_frame).setVisibility(View.VISIBLE);
//            ValueAnimator animator = ObjectAnimator.ofFloat(
//                    descriptionLayout, "y", -descriptionLayout.getHeight(),
//                    0).setDuration(500);
//            animator.start();
//            new BounceInAnimator().animate(descriptionLayout);
//            new StandUpAnimator().animate(descriptionLayout);
        }
        Log.e(TAG, "onCurrentItemDisappear called");
    }
}
