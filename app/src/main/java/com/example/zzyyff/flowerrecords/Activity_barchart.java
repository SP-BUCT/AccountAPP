package com.example.zzyyff.flowerrecords;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class Activity_barchart extends AppCompatActivity {
    ImageView back;
    tools_MyDatabaseHelper dbHelper;
    SQLiteDatabase db;
    BarChart outBarchart;
    TextView nullrecord;
    TextView incomeshow;
    TextView costshow;
    String choose_year = "2021";
    String choose_month = "1";
    LinearLayout clicktocost;
    LinearLayout clicktoincome;
    List<String> property_out = new ArrayList<>();
    List<Float> costList = new ArrayList<>();
    List<class_tablelist> tablelist = new ArrayList<>();
    List<Integer> countList = new ArrayList<>();
    List<Double> percntList = new ArrayList<>();
    ScrollView scrollView;
    TypedValue typedValue = new TypedValue();

    private String[] mMonths = new String[]{
            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};

    int COLORFUL[] = new int[]{Color.parseColor("#f6ec66"),
            Color.parseColor("#f97272"),
            Color.parseColor("#00818a"),
            Color.parseColor("#97de95"),
            Color.parseColor("#11cbd7"),
            Color.parseColor("#5fcc9c"),
            Color.parseColor("#ffdd93"),
            Color.parseColor("#ffb6b9")};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(Activity_StyleChanged.gettheme((String) SPUtils.get(this, "theme", "AppTheme")));
        setContentView(R.layout.activity_barchart);

        back = findViewById(R.id.btnBack);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        outBarchart = findViewById(R.id.mBarChart);
        nullrecord = findViewById(R.id.nullrecord);
        clicktocost = findViewById(R.id.clickcost);
        clicktoincome = findViewById(R.id.clickincome);
        costshow = findViewById(R.id.costshow);
        incomeshow = findViewById(R.id.incomeshow);
        scrollView = findViewById(R.id.scrollview);
        final boolean[] selectIsout = {false};
        final boolean[] selectIsin = {false};

        dbHelper = new tools_MyDatabaseHelper(Activity_barchart.this, "record.db", null, 1);
        db = dbHelper.getWritableDatabase();
        scrollView.setVerticalScrollBarEnabled(false);
        getTheme().resolveAttribute(R.attr.colorPrimary, typedValue, true);

        selectIsout[0] = true;
        clicktoincome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectIsout[0] == true && selectIsin[0] == false) {
                    selectin();//查收入表并显示柱状图

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        incomeshow.setTextColor(getColor(typedValue.resourceId));
                    }
                    costshow.setTextColor(Color.parseColor("#BEBEBE"));
                    selectIsout[0] = false;
                    selectIsin[0] = true;
                }
            }
        });
        clicktocost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectIsout[0] == false && selectIsin[0] == true) {
                    selectout();//查支出表并显示柱状图
                    incomeshow.setTextColor(Color.parseColor("#BEBEBE"));

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        costshow.setTextColor(getColor(typedValue.resourceId));
                    }

                    selectIsout[0] = true;
                    selectIsin[0] = false;
                }
            }
        });
    }

    private void initoutBarChart(String inorout) {
        DecimalFormat df = new DecimalFormat("#.##");
        double alloutcome = 0;
        List<BarEntry> yVals = new ArrayList <>(); //y值
        yVals.clear();
        for (int i = 0; i < property_out.size(); i++) {
            alloutcome += costList.get(i);
            yVals.add(new BarEntry(i, costList.get(i)));
        }

        BarDataSet barDataSet = new BarDataSet(yVals, "");//创建饼图的一个数据集
        barDataSet.setValueTextSize(11f);
        barDataSet.setColors(COLORFUL); //设置成丰富多彩的颜色

        BarData bardata = new BarData(barDataSet);//生成BarData
        outBarchart.setData(bardata);//给BarChart填充数据
        outBarchart.getLegend().setPosition(Legend.LegendPosition.RIGHT_OF_CHART_CENTER);
        outBarchart.getLegend().setForm(Legend.LegendForm.CIRCLE);//设置注解的位置和形状
        outBarchart.getLegend().setTextSize(12);

        outBarchart.getDescription().setEnabled(false);//设置描述
        outBarchart.setPinchZoom(true);//设置按比例放缩柱状图

        //x坐标轴设置
        XAxis xAxis = outBarchart.getXAxis();//获取x轴
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);//设置X轴标签显示位置
        xAxis.setDrawGridLines(false);//不绘制格网线
        xAxis.setGranularity(1f);//设置最小间隔，防止当放大时，出现重复标签。

        //X轴自定义值
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //当设置了xAxis.setAxisMinimum(0f);时，value的值可能为负值，会报空指针异常
                if (value < 0) {
                    return "";
                }
                //x轴标签的值
                String labelValue = property_out.get((int) value % property_out.size());
//                if (labelValue.length() > 4) {
//                    labelValue = labelValue.substring(0,4)+"…";
//                }
                return labelValue;
            }
        });

        //y轴设置
        YAxis leftAxis = outBarchart.getAxisLeft();//获取左侧y轴
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);//设置y轴标签显示在外侧
        leftAxis.setAxisMinimum(0f);//设置Y轴最小值
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(false);//禁止绘制y轴标签
        leftAxis.setDrawAxisLine(false);//禁止绘制y轴

        outBarchart.getAxisRight().setEnabled(false);//禁用右侧y轴

        //图例设置
        Legend legend = outBarchart.getLegend();
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);//图例水平居中
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);//图例在图表上方
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);//图例的方向为水平
        legend.setDrawInside(false);//绘制在chart的外侧
        legend.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);//图例中的文字方向

        legend.setForm(Legend.LegendForm.SQUARE);//图例窗体的形状
        legend.setFormSize(0f);//图例窗体的大小
        legend.setTextSize(16f);//图例文字的大小
        //legend.setYOffset(-2f);

        outBarchart.setExtraBottomOffset(10);//距视图窗口底部的偏移，类似与paddingbottom
        outBarchart.setExtraTopOffset(30);//距视图窗口顶部的偏移，类似与paddingtop
        outBarchart.setFitBars(true);//使两侧的柱图完全显示
        outBarchart.animateX(1500);//数据显示动画，从左往右依次显示
    }

    private void selectout(){
        property_out.clear();
        costList.clear();
        countList.clear();
        float sum_cost = 0;
        int count = 0;
        //求各类别数量
        Cursor cursor = db.rawQuery("select count(*) from record" +
                        " Where inorout=? and date_year =? and date_month=?" +
                        " group by property",
                new String[]{"out", choose_year,
                        choose_month});
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
                countList.add(count);
            } while (cursor.moveToNext());
        }
        else {
            countList.add(count);
        }

        //求各类别总和
        Cursor cursor1 = db.rawQuery("select sum(cost) from record" +
                        " Where inorout=? and date_year =? and date_month=?" +
                        " group by property",
                new String[]{"out", choose_year,
                        choose_month});
        if (cursor1.moveToFirst()) {
            do {
                sum_cost = cursor1.getFloat(0);
                costList.add(sum_cost);
            } while (cursor1.moveToNext());
        }
        else {
            costList.add(sum_cost);
        }

        //找类别
        Cursor cursor2 = db.query("record", null,
                "date_year=? and date_month=? and inorout=?",
                new String[]{choose_year,
                        choose_month,"out"},
                "property", null, null);
        if (cursor2.moveToFirst()) {
            do {
                property_out.add(cursor2.getString(cursor2.getColumnIndex("property")));
                outBarchart.setVisibility(View.VISIBLE);
                nullrecord.setVisibility(View.INVISIBLE);
            } while (cursor2.moveToNext());
            initoutBarChart("支出");
        }
        else {
            outBarchart.setVisibility(View.INVISIBLE);
            nullrecord.setVisibility(View.VISIBLE);
        }
    }

    private void selectin(){
        property_out.clear();
        costList.clear();
        countList.clear();
        float sum_cost = 0;
        int count = 0;

        //求各类别数量
        Cursor cursor = db.rawQuery("select count(*) from record" +
                        " Where inorout=? and date_year =? and date_month=?" +
                        " group by property",
                new String[]{"in", choose_year,
                        choose_month});
        if (cursor.moveToFirst()) {
            do {
                count = cursor.getInt(0);
                countList.add(count);
            } while (cursor.moveToNext());
        }

        //求各类别总和
        Cursor cursor1 = db.rawQuery("select sum(income) from record" +
                        " Where inorout=? and date_year =? and date_month=?" +
                        " group by property",
                new String[]{"in", choose_year,
                        choose_month});
        if (cursor1.moveToFirst()) {
            do {
                sum_cost = cursor1.getFloat(0);
                costList.add(sum_cost);
            } while (cursor1.moveToNext());
        }

        //找类别
        Cursor cursor2 = db.query("record", null,
                "date_year=? and date_month=? and inorout=?",
                new String[]{choose_year,
                        choose_month,"in"},
                "property", null, null);
        if (cursor2.moveToFirst()) {
            do {
                property_out.add(cursor2.getString(cursor2.getColumnIndex("property")));
                outBarchart.setVisibility(View.VISIBLE);
                nullrecord.setVisibility(View.INVISIBLE);
            } while (cursor2.moveToNext());
            initoutBarChart("收入");
        }
        else {
            outBarchart.setVisibility(View.INVISIBLE);
            nullrecord.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        selectin();
        selectout();//查支出表并显示饼状图
    }

}