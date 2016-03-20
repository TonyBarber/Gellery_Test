package jkwusu.gallery_test.Showimage;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import java.util.List;

import jkwusu.gallery_test.MyImageView;
import jkwusu.gallery_test.NativeImageLoader;
import jkwusu.gallery_test.R;

/**
 * Created by jkwusu on 2016/3/13.
 */
public class BigViewAdapter extends PagerAdapter {

    private List<String> list;
    private Context context;
    MyImageView imageView;
    View view;


    @Override
    public int getCount() {
        return list.size();
    }

    public BigViewAdapter(List<String> list,Context context){
        this.list=list;
        this.context=context;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view= (View) object;
        container.removeView(view);
    }



    @Override
    public Object instantiateItem(ViewGroup container, int position) {


        String path=list.get(position);

        view = LayoutInflater.from(context).inflate(R.layout.bigimage, null);
        imageView = (MyImageView) view.findViewById(R.id.bigimage);

        Bitmap bitmap= NativeImageLoader.getInstance().loadNativeImage(path, new NativeImageLoader.NativeImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
            }
        });


        if (bitmap!=null) {
            imageView.setImageBitmap(bitmap);
        }else
        {
            imageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }


}
