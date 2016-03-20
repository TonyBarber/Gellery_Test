package jkwusu.gallery_test.Showimagefile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.os.Handler;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import jkwusu.gallery_test.ImageFile;
import jkwusu.gallery_test.R;
import jkwusu.gallery_test.Showimagetile.ShowImageActivity;

public class MainActivity extends Activity {

    private HashMap<String,List<String>> mGroupMap=new HashMap<String,List<String>>();
    private List<ImageFile> list=new ArrayList<ImageFile>();
    private final static int SCAN_OK=1;
    private ProgressDialog mProgressDialog;
    private GroupAdapter adapter;
    private ListView mGroupListView;

    private Handler mHandler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SCAN_OK:
                    //关进度条
                    mProgressDialog.dismiss();
                    adapter=new GroupAdapter(MainActivity.this,list=subImageFromHash(mGroupMap),mGroupListView);
                    mGroupListView.setAdapter(adapter);
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mGroupListView= (ListView) findViewById(R.id.main_list);
        getImages();

        mGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> childList=mGroupMap.get(list.get(position).getFoldername());
                Intent mIntent=new Intent(MainActivity.this,ShowImageActivity.class);
                mIntent.putStringArrayListExtra("data",(ArrayList<String>)childList);
                startActivity(mIntent);
            }
        });

    }

    //contentprovider扫描手机图片，单开线程运行
    private void getImages(){
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(this,"无外部存储",Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressDialog=ProgressDialog.show(this,null,"正在加载");
        new Thread(new Runnable() {
            @Override
            public void run() {
                Uri mImageUri= MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                ContentResolver mContentResolver=MainActivity.this.getContentResolver();

                Cursor mCursor=mContentResolver.query(mImageUri,null,MediaStore.Images.Media.MIME_TYPE+
                        "=? or "+MediaStore.Images.Media.MIME_TYPE+ "=?",new String[]{"image/jpeg","image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                while(mCursor.moveToNext()){
                    String path=mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));  //获取图片路径
                    String parentName=new File(path).getParentFile().getName();   //获取图片的父路径名
                    if(!mGroupMap.containsKey(parentName)){
                        List<String> childList=new ArrayList<String>();
                        childList.add(path);
                        mGroupMap.put(parentName,childList);
                    }else{
                        mGroupMap.get(parentName).add(path);
                    }
                }
                mCursor.close();
                mHandler.sendEmptyMessage(SCAN_OK); //扫描图片完成
            }
        }).start();
    }

    //扫描hashmap组装
    private List<ImageFile> subImageFromHash(HashMap<String,List<String>> mGroupMap){
        if(mGroupMap.size()==0) {
            return null;
        }
            List<ImageFile> list=new ArrayList<ImageFile>();
            Iterator<Map.Entry<String,List<String>>> it=mGroupMap.entrySet().iterator();
            while(it.hasNext()){
                Map.Entry<String,List<String>> entry=it.next();
                ImageFile imageFile=new ImageFile();
                String key=entry.getKey();
                List<String> value=entry.getValue();

                imageFile.setFoldername(key);
                imageFile.setImagenum(value.size());
                imageFile.setTopImagePath(value.get(0));

                list.add(imageFile);
            }


        return list;

    }

}
