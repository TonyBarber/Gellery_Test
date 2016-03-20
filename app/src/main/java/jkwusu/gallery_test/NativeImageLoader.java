package jkwusu.gallery_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Message;
import android.support.v4.util.LruCache;

import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;
import android.os.Handler;

/**
 * Created by jkwusu on 2016/3/11.
 */
public class NativeImageLoader {
    private static LruCache<String,Bitmap> mMemoryCache;
    private static NativeImageLoader mInstance=new NativeImageLoader();
    private ExecutorService mImageThreadPool=Executors.newFixedThreadPool(1);

    //获取实例
    public static NativeImageLoader getInstance(){
        return mInstance;
    }

    //设定其占用内存的1/4
    private NativeImageLoader(){
        final int maxMemory= (int) Runtime.getRuntime().maxMemory();
        final int cacheSize=maxMemory/4;
        mMemoryCache=new LruCache<String,Bitmap>(cacheSize){
              protected int sizeOf(String key,Bitmap bitmap){
                  return bitmap.getRowBytes()*bitmap.getHeight()/1024;
              }
        };
    }

    //加载本地图片不裁剪
    public Bitmap loadNativeImage(final String path,final NativeImageCallBack mCallBack) {
        return this.loadNativeImage(path,null,mCallBack);
    }

    //加载本地图片裁剪
    public Bitmap loadNativeImage(final String path,final Point mPoint,final NativeImageCallBack mCallBack){

        //先读内存中的bitmap
        Bitmap bitmap=getBitmapFromMemCache(path);
        final Handler mHandler=new Handler() {
            public void handleMessage(Message msg){
               super.handleMessage(msg);
                mCallBack.onImageLoader((Bitmap) msg.obj,path);
            }
        };

        //不在内存，启动线程加载本地图片，并且将其加入内存
        if(bitmap==null){
            mImageThreadPool.execute(new Runnable() {
                @Override
                public void run() {
                    Bitmap mBitmap=decodeThumbBitmapForFile(path,mPoint==null?0:mPoint.x,mPoint==null?0:mPoint.y);
                    Message msg=mHandler.obtainMessage();
                    msg.obj=mBitmap;
                    mHandler.sendMessage(msg);
                    addBitmapToMemoryCache(path,mBitmap);
                }
            });
        }

        return bitmap;
    }

    //根据view的宽和高来获取图片的缩略图
    private Bitmap decodeThumbBitmapForFile(String path,int viewwidth,int viewheight){
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        BitmapFactory.decodeFile(path,options);
        options.inSampleSize=computeScale(options,viewwidth,viewheight);
        options.inJustDecodeBounds=false;
        return BitmapFactory.decodeFile(path,options);
    }

    //根据view的宽和高来计算bitmap缩放比例
    private int computeScale(BitmapFactory.Options options,int viewwidth,int viewheight){
        int inSampleSize=1;
        if (viewheight==0||viewwidth==0){
            return inSampleSize;
        }
        int bitmapwidth=options.outWidth;
        int bitmapheight=options.outHeight;
        //根据bitmap和设定图片的view，计算缩放比例，选取小的那个
        if (bitmapwidth>viewwidth || bitmapheight>viewwidth){
            int widthscale=Math.round((float)bitmapwidth/(float)viewwidth);
            int heightscale=Math.round((float)bitmapheight/(float)viewheight);
            inSampleSize=widthscale<heightscale?widthscale:heightscale;
        }
        return inSampleSize;
    }

    //bitmap加入内存缓存
    private void addBitmapToMemoryCache(String key,Bitmap bitmap){
        if (getBitmapFromMemCache(key)==null && bitmap!=null){
            mMemoryCache.put(key,bitmap);
        }
    }

    //根据key获取内存图片
    private Bitmap getBitmapFromMemCache(String key){
        Bitmap bitmap=mMemoryCache.get(key);
        return bitmap;
    }

    //本地图片回调接口
    public interface NativeImageCallBack{
        public void onImageLoader(Bitmap bitmap,String path);
    }
}
