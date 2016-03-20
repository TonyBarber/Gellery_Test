package jkwusu.gallery_test.Showimagefile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.security.Policy;
import java.util.List;

import jkwusu.gallery_test.ImageFile;
import jkwusu.gallery_test.MyImageView;
import jkwusu.gallery_test.NativeImageLoader;
import jkwusu.gallery_test.R;

/**
 * Created by jkwusu on 2016/3/12.
 */
public class GroupAdapter extends BaseAdapter {

    private List<ImageFile> list;
    private Point mPoint = new Point(0,0);
    private ListView mListView;
    protected LayoutInflater mInflater;

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public GroupAdapter(Context context,List<ImageFile> list,ListView mListView){
        this.list=list;
        this.mListView=mListView;
        mInflater=LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        ImageFile mimagefile=list.get(position);
        String path=mimagefile.getTopImagePath();
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=mInflater.inflate(R.layout.group_item,null);
            viewHolder.myImageView= (MyImageView) convertView.findViewById(R.id.group_image);
            viewHolder.mTextViewTitle= (TextView) convertView.findViewById(R.id.group_title);
            viewHolder.mTextViewCounts= (TextView) convertView.findViewById(R.id.group_count);

            viewHolder.myImageView.SetOnMeasureListener(new MyImageView.OnMeasureListener() {
                @Override
                public void onMeasureSize(int width, int height) {
                    mPoint.set(width,height);
                }
            });
            convertView.setTag(viewHolder);
        }else{
            viewHolder= (ViewHolder) convertView.getTag();
            viewHolder.myImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }
        viewHolder.mTextViewTitle.setText(mimagefile.getFoldername());
        viewHolder.mTextViewCounts.setText(Integer.toString(mimagefile.getImagenum()));
        viewHolder.myImageView.setTag(path);

        Bitmap bitmap= NativeImageLoader.getInstance().loadNativeImage(path, mPoint, new NativeImageLoader.NativeImageCallBack() {
            @Override
            public void onImageLoader(Bitmap bitmap, String path) {
                ImageView imageView= (ImageView) mListView.findViewWithTag(path);
                if (bitmap!=null&&imageView!=null){
                    imageView.setImageBitmap(bitmap);
                }
            }
        });

        if (bitmap!=null){
            viewHolder.myImageView.setImageBitmap(bitmap);
        }else {
            viewHolder.myImageView.setImageResource(R.drawable.friends_sends_pictures_no);
        }

        return convertView;
    }

    public static class ViewHolder{
        public MyImageView myImageView;
        public TextView mTextViewTitle;
        public TextView mTextViewCounts;
    }
}
