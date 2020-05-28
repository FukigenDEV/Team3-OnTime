package com.example.ontimeapp;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.widget.LinearLayout;

import java.util.List;

class UIAnimation {

    void setXPosAnim(Context context, LinearLayout object, float destination) {
        ObjectAnimator animationUI = ObjectAnimator.ofFloat(object, "translationX",
                destination * context.getResources().getDisplayMetrics().density);
        animationUI.setDuration(250);
        animationUI.start();
    }

    void hideMainNav(Context context, List<LinearLayout> list) {
        setXPosAnim(context, list.get(0), -260);
        setXPosAnim(context, list.get(1), -260);
    }

    void showMainNav(Context context, List<LinearLayout> list) {
        setXPosAnim(context, list.get(0), 0);
        setXPosAnim(context, list.get(1), 0);
    }

}
