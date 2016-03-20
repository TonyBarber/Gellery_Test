package jkwusu.gallery_test;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by jkwusu on 2016/3/12.
 */
public class MyImageView extends ImageView {
    private OnMeasureListener onMeasureListener;

    public void SetOnMeasureListener(OnMeasureListener onMeasureListener){
        this.onMeasureListener=onMeasureListener;
    }

    protected void onMeasure(int widthMeasureSpec,int heightMeasureSpec){
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        if(onMeasureListener!=null){
            onMeasureListener.onMeasureSize(getMeasuredWidth(),getMeasuredHeight());
        }
    }
    public MyImageView(Context context) {
        super(context);
    }

    public MyImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    public interface OnMeasureListener{
        public void onMeasureSize(int width,int height);
    }

}
