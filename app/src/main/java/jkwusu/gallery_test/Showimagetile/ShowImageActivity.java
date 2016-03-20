package jkwusu.gallery_test.Showimagetile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import jkwusu.gallery_test.R;
import jkwusu.gallery_test.Showimage.ShowBigImage;

/**
 * Created by jkwusu on 2016/3/12.
 */
public class ShowImageActivity extends Activity{
    private GridView mGridView;
    private List<String> list;
    private ChildAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_image_activity);

        mGridView= (GridView) findViewById(R.id.child_grid);
        list=getIntent().getStringArrayListExtra("data");
        adapter=new ChildAdapter(this,list,mGridView);
        mGridView.setAdapter(adapter);

        mGridView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<String> thislist = list;
                Intent mIntent = new Intent(ShowImageActivity.this, ShowBigImage.class);
                Bundle bd=new Bundle();
                bd.putInt("position",position);
                bd.putStringArrayList("data",(ArrayList<String>) thislist);
                mIntent.putExtras(bd);
                startActivity(mIntent);
            }
        });

    }


}
