package com.example.progressring;

import android.view.View;

/**
 * Created by danny on 15-11-2.
 */
public class MeasureModeTeller {

    public static String tellMode(int measuredMode) {
        if (measuredMode == View.MeasureSpec.EXACTLY) {
            return "MeasureSpec.EXACTLY";
        } else if (measuredMode == View.MeasureSpec.AT_MOST) {
            return "MeasureSpec.AT_MOST";
        } else if (measuredMode == View.MeasureSpec.UNSPECIFIED) {
            return "MeasureSpec.UNSPECIFIED";
        } else {
            return "UNKOWN";
        }
    }

}
