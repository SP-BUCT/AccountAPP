package com.example.zzyyff.flowerrecords;

import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.github.mikephil.charting.charts.PieChart;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Activity_MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,BottomNavigationBar.OnTabSelectedListener{

    android.support.v7.widget.Toolbar toolbar;
    BottomNavigationBar bottomNavigationBar;
    adapter_MyFragmentPager adapter;
    ArrayList<Fragment> fragments;
    ViewPager myviewpager;
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    tools_MyDatabaseHelper dbHelper2;
    SQLiteDatabase db2;
    tools_MyDatabaseHelper dbHelper3;
    SQLiteDatabase db3;
    tools_MyDatabaseHelper dbHelper4;
    SQLiteDatabase db4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this,"theme","AppTheme")));
        setContentView(R.layout.activity_main);



        dbHelper = new tools_MyDatabaseHelper(Activity_MainActivity.this, "record.db", null, 1);
        db = dbHelper.getWritableDatabase();
        dbHelper2 = new tools_MyDatabaseHelper(Activity_MainActivity.this, "diary.db", null, 1);
        db2 = dbHelper2.getWritableDatabase();
        dbHelper3 = new tools_MyDatabaseHelper(Activity_MainActivity.this, "credit.db", null, 1);
        db3 = dbHelper3.getWritableDatabase();
        dbHelper4 = new tools_MyDatabaseHelper(Activity_MainActivity.this, "mine.db", null, 1);
        db4 = dbHelper4.getWritableDatabase();

        //添加初始账户
        Cursor cursor = db4.rawQuery("select * from mine", null);
        if(cursor.getCount()==0) {
            ContentValues values = new ContentValues();
            values.put("name", "QJ-QWG-JHC");
            values.put("date_signed", "no");
            values.put("quota", 1);
            db4.insert("mine",null,values);
        }

        initToolbar();
        initViewpager();
        initNavigationBar();

    }

    @Override
    protected void onResume() {
        super.onResume();
        quitFullScreen();
    }



    void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    void initViewpager(){
        myviewpager = (ViewPager)findViewById(R.id.viewpager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new fragment_list());
        fragments.add(new fragment_table());
        fragments.add(new fragment_keepaccount());
        fragments.add(new fragment_credit());
        fragments.add(new fragment_mine());
        adapter = new adapter_MyFragmentPager(getSupportFragmentManager(),fragments);
        myviewpager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        myviewpager.setOnPageChangeListener(this);
    }


    private void initNavigationBar(){
        final TypedValue typedValue = new TypedValue();
        this.getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);
        bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.botton_bar);
        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setTabSelectedListener(this);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        bottomNavigationBar.addItem(new BottomNavigationItem(R.drawable.list,"明细").setActiveColorResource(typedValue.resourceId))
                .addItem(new BottomNavigationItem(R.drawable.table,"图表").setActiveColorResource(typedValue.resourceId))
                .addItem(new BottomNavigationItem(R.drawable.add,"记账").setActiveColorResource(typedValue.resourceId))
                .addItem(new BottomNavigationItem(R.drawable.diary,"账户").setActiveColorResource(typedValue.resourceId))
                .addItem(new BottomNavigationItem(R.drawable.mine,"我的").setActiveColorResource(typedValue.resourceId))
                .setFirstSelectedPosition(0).initialise();
        setBottomNavigationItem(11,16,0,27);
    }
    private void setBottomNavigationItem(int space, int imgLen,int centerSpace,int centerImglen) {
        float contentLen = 36;
        Class barClass = bottomNavigationBar.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mTabContainer")) {
                try { //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(bottomNavigationBar);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
                        //获取到容器内的各个 Tab
                        View view = mTabContainer.getChildAt(j);
                        //获取到Tab内的各个显示控件
                        // 获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        if(j!=2) {
                            //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                            labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (Math.sqrt(2) * (contentLen - imgLen - space)));
                            //获取到Tab内的图像控件
                            ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                            //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) DisplayUtil.dip2px(this, imgLen), (int) DisplayUtil.dip2px(this, imgLen));
                            params.gravity = Gravity.CENTER;
                            iconView.setLayoutParams(params);
                        }else
                        {
                            //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                            labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (Math.sqrt(2) * (contentLen - centerImglen - centerSpace)));
                            //获取到Tab内的图像控件
                            ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                            //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) DisplayUtil.dip2px(this, centerImglen), (int) DisplayUtil.dip2px(this, centerImglen));
                            params.gravity = Gravity.CENTER;
                            iconView.setLayoutParams(params);

                        }

                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public static class DisplayUtil {
        public static int dip2px(Context context, float dipValue){
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int)(dipValue * scale + 0.5f);
        }
        public static int px2dip(Context context, float pxValue){
            final float scale = context.getResources().getDisplayMetrics().density;
            return (int)(pxValue / scale + 0.5f);
        }
    }

    //ViewPager的方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    @Override
    public void onPageSelected(int position) {
        switch (position)
        {
            case 0:
                bottomNavigationBar.selectTab(0);
                break;
            case 1:
                bottomNavigationBar.selectTab(1);
                break;
            case 2:
                bottomNavigationBar.selectTab(2);
                break;
            case 3:
                bottomNavigationBar.selectTab(3);
                break;
            case 4:
                bottomNavigationBar.selectTab(4);
                break;
        }

    }
    @Override
    public void onPageScrollStateChanged(int state) {

    }


    //底部导航栏的方法重写
    @Override
    public void onTabSelected(int position) {
        switch (position) {
            case 0:
                myviewpager.setCurrentItem(0);
                break;
            case 1:
                PieChart pieChart = findViewById(R.id.mPieChart);
                if(pieChart!=null) {
                    pieChart.animateY(1000);
                }
                myviewpager.setCurrentItem(1);
                break;
            case 2:
                myviewpager.setCurrentItem(2);
                break;
            case 3:
                myviewpager.setCurrentItem(3);
                break;
            case 4:
                myviewpager.setCurrentItem(4);
                break;
        }

    }
    @Override
    public void onTabUnselected(int position) {
    }
    // 与onTabSelected(int position)一致
    @Override
    public void onTabReselected(int position) {
        switch (position) {
            case 0:
                myviewpager.setCurrentItem(0);
                break;
            case 1:
                PieChart pieChart = findViewById(R.id.mPieChart);
                if(pieChart!=null) {
                    pieChart.animateY(1000);
                }
                myviewpager.setCurrentItem(1);
                break;
            case 2:
                myviewpager.setCurrentItem(2);
                break;
            case 3:
                myviewpager.setCurrentItem(3);
                break;
            case 4:
                myviewpager.setCurrentItem(4);
                break;
        }
    }



    //退出程序
    public boolean onKeyDown(int KeyCode, KeyEvent event)
    {
        if(KeyCode == KeyEvent.KEYCODE_BACK) {
            new android.support.v7.app.AlertDialog.Builder(Activity_MainActivity.this)
                    .setIcon(R.drawable.logo)
                    .setTitle("警告")
                    .setMessage("确定退出吗？")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface arg0, int arg1) {
                            finish();
                        }
                    }).setNegativeButton("取消", null)
                    .create().show();
        }
        return super.onKeyDown(KeyCode,event);

    }

    public void addBill() {
        initViewpager();
        bottomNavigationBar.selectTab(0);
    }


    private void quitFullScreen(){
        final WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(attrs);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

}