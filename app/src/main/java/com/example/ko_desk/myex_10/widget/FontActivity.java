package com.example.ko_desk.myex_10.widget;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class FontActivity extends Activity {

    public static void setGlobalFont(Context context, View view) {
        if(view != null) {
            if(view instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup)view;
                int vgCnt = vg.getChildCount();

                for(int i=0; i<vgCnt; i++) {
                    View v = vg.getChildAt(i);
                    if(v instanceof TextView) {
                        ((TextView) v).setTypeface(Typeface.createFromAsset(
                                context.getAssets(),"fonts/NanumBarunpenR.otf"
                        ));

                    }
                    setGlobalFont(context,v);
                }

            }
        }
    }
}
