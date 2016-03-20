package jkwusu.gallery_test.Showimage;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import java.util.List;

import jkwusu.gallery_test.R;

public class ShowBigImage extends Activity {

    private ViewPager vp;
    private BigViewAdapter vpAdapter;
    private List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_big_image);

        vp= (ViewPager) findViewById(R.id.viewpager);
        Intent intent=getIntent();
        Bundle bd=intent.getExtras();
        list=bd.getStringArrayList("data");
        int position=bd.getInt("position");

        vpAdapter=new BigViewAdapter(list,this);
        vp.setAdapter(vpAdapter);
        vp.setCurrentItem(position);
    }
}
