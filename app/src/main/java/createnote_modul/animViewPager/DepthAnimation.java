package createnote_modul.animViewPager;

import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by l on 4/26/2017.
 */

public class DepthAnimation implements ViewPager.PageTransformer {

    private float currentTranformer;

    public DepthAnimation() {
    }

    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) {
            view.setAlpha(0);

        } else if (position <= 0) {
            view.setAlpha(1+position);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);

        } else if (position <= 1) {
            view.setAlpha(1 - position);

            view.setTranslationX(pageWidth * -position);

            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            float scaleFactorForX;
            if(position >0.5f){
                scaleFactorForX = - Math.abs(position*position*position*position);
                currentTranformer = scaleFactorForX;
            }else{
                scaleFactorForX = (1-position)*(1-position)*(1-position);
            }

            view.setScaleX(scaleFactorForX);
            view.setScaleY(scaleFactor);

        } else {
            view.setAlpha(0);
        }
    }
}
