package createnote_modul.models;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by l on 4/22/2017.
 */

public class CusView extends View {
    public static final int DRAW_CIRCLE = 1;
    public static final int DRAW_CIRCLE_VS_STROKE = 2;
    public static final int DRAW_TWO_HALF_COLOR = 3;
    public static final int DRAW_AUTOFILL_CIRCLE = 4;

    public final float STROKE_DISTANCE = 20.0f;

    String color = "#e504f9";
    String colorOld = "#e504f9";
    int styleDraw = 1;

    float increase = 0;

    int visualDuration = 0;

    public CusView(Context context) {
        this(context, null);
    }

    public CusView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public String getColorOld() {
        return colorOld;
    }

    public void setColorOld(String colorOld) {
        this.colorOld = colorOld;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public int getStyleDraw() {
        return styleDraw;
    }

    public void setStyleDraw(int styleDraw) {
        this.styleDraw = styleDraw;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //Draw component in here
        Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        Paint mPaintOld = new Paint(Paint.ANTI_ALIAS_FLAG);

        switch (styleDraw){
            case(DRAW_CIRCLE):{
                mPaint.setColor(Color.parseColor(color));
                mPaint.setStyle(Paint.Style.FILL);
                float radius = (float)(getWidth() > getHeight() ? getHeight() : getWidth())/2;
                float cx = getWidth()/2;
                float cy = getHeight()/2;
                canvas.drawCircle(cx,cy,radius,mPaint);
                break;
            }
            case(DRAW_CIRCLE_VS_STROKE):{
                mPaint.setColor(Color.parseColor(color));
                mPaint.setStyle(Paint.Style.FILL);
                float radius = (float)(getWidth() > getHeight() ? getHeight() : getWidth())/2 - STROKE_DISTANCE;
                float cx = getWidth()/2;
                float cy = getHeight()/2;
                canvas.drawCircle(cx,cy,radius,mPaint);

                mPaint.setColor(Color.parseColor(color));
                mPaint.setStyle(Paint.Style.STROKE);
                mPaint.setStrokeWidth(STROKE_DISTANCE/2);
                radius = (float)(getWidth() > getHeight() ? getHeight() : getWidth())/2 - STROKE_DISTANCE/2 ;
                cx = getWidth()/2;
                cy = getHeight()/2;
                canvas.drawCircle(cx,cy,radius,mPaint);
                break;
            }
            case(DRAW_TWO_HALF_COLOR):{
                mPaint.setColor(Color.parseColor(color));
                mPaintOld.setColor(Color.parseColor(colorOld));
                float min = (float)(getWidth() > getHeight() ? getHeight() : getWidth());
                float left = 0;
                float top = 0;
                float right = min;
                float bottom = min;

                canvas.drawArc(new RectF(left,top,right,bottom),180,180,false,mPaint);
                canvas.drawArc(new RectF(left,top,right,bottom),0,180,false,mPaintOld);
                break;
            }
            case(DRAW_AUTOFILL_CIRCLE):{
                mPaint.setColor(Color.parseColor(color));
                mPaintOld.setColor(Color.parseColor(colorOld));
                float min = (float)(getWidth() > getHeight() ? getHeight() : getWidth());
                float left = 0;
                float top = 0;
                float right = min;
                float bottom = min;

                increase += 2;
                canvas.drawArc(new RectF(left,top,right,bottom),180-increase,180+increase,false,mPaint);
                canvas.drawArc(new RectF(left,top,right,bottom),0+increase,180-increase,false,mPaintOld);

                visualDuration = (int)(180 + increase);
                if(visualDuration < 365){
                    invalidate();
                }
                break;
            }
        }
    }

    public int getVisualDuration() {
        return visualDuration;
    }
}
