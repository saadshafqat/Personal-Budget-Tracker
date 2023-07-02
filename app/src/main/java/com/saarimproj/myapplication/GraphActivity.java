package com.saarimproj.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;
import com.google.android.material.card.MaterialCardView;
import com.saarimproj.myapplication.Models.IncomeModel;
import com.saarimproj.myapplication.Tools.Constraints;
import com.saarimproj.myapplication.Tools.DBHelper;
import com.saarimproj.myapplication.Tools.SharedPrefs;

import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    MaterialCardView income;
    Boolean incomecheck=true;
    Boolean expensecheck=false;
    MaterialCardView expense;
    List<IncomeModel> listIncome=new ArrayList<>();
    List<IncomeModel> listExpense=new ArrayList<>();
    PieChart pieChart;
    TextView type,typeTotal;
    SharedPrefs prefs;
    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Graphs");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // and this
                finish();
            }
        });
        inStart();

        setListeners();
        getIncomeData();

    }

    private void setListeners() {
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                income.setStrokeColor(getResources().getColor(R.color.cardTeal));
                expense.setStrokeColor(getResources().getColor(R.color.white));
                incomecheck=true;
                expensecheck=false;
                getIncomeData();

            }
        });
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expense.setStrokeColor(getResources().getColor(R.color.cadRed));
                income.setStrokeColor(getResources().getColor(R.color.white));
                incomecheck=false;
                expensecheck=true;
                getExpenseData();

            }
        });
    }

    private void getExpenseData() {
        listExpense.clear();

        float sum=0;
        Cursor cursor= dbHelper.getInTransaction("Expense");
        while (cursor.moveToNext()) {
            IncomeModel model=new IncomeModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
            listExpense.add(model);
            sum+=Float.parseFloat(cursor.getString(5));
        }
        type.setText("Total Expense (Debit)");
        String s1=prefs.getStr(Constraints.currency);
        String[] words=s1.split("-");



        typeTotal.setText(""+sum+" "+words[0]);
        setChart(listExpense,"Expense");
    }

    private void getIncomeData() {
        listIncome.clear();
        float sum=0;
        Cursor cursor= dbHelper.getInTransaction("Income");
        while (cursor.moveToNext()) {
            IncomeModel model=new IncomeModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
            listIncome.add(model);
            sum+=Float.parseFloat(cursor.getString(5));
        }
        type.setText("Total Income (Credit)");
        String s1=prefs.getStr(Constraints.currency);
        String[] words=s1.split("-");



        typeTotal.setText(""+sum+" "+words[0]);
        setChart(listIncome,"Incomes");

    }

    private void setChart(List<IncomeModel> listIncome,String label) {
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (int i = 0; i < listIncome.size() ; i++) {
//            entries.add(new PieEntry((float) ((Math.random() * range) + range / 5),
//                    parties[i % parties.length],
//                    getResources().getDrawable(R.drawable.star)));
            PieEntry entry=new PieEntry(Float.parseFloat(listIncome.get(i).getTransammount()),listIncome.get(i).getTitle());
            entries.add(entry);

        }
        PieDataSet dataSet = new PieDataSet(entries, label);

        dataSet.setDrawIcons(false);

        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);



        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(7f);
        data.setValueTextColor(getResources().getColor(R.color.primaryteal));

        pieChart.setData(data);

        // undo all highlights
        pieChart.highlightValues(null);

        pieChart.invalidate();

    }

    private void inStart() {
        dbHelper=new DBHelper(GraphActivity.this);
        income=findViewById(R.id.materialCardView3);
        expense=findViewById(R.id.materialCardView2);
        prefs=new SharedPrefs(GraphActivity.this);

        pieChart=findViewById(R.id.chart1);
        type=findViewById(R.id.textView23);
        typeTotal=findViewById(R.id.textView21);
        income.setStrokeColor(getResources().getColor(R.color.cardTeal));

        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);

        pieChart.setDragDecelerationFrictionCoef(0.95f);



        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColor(Color.WHITE);

        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);

        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);

        pieChart.setDrawCenterText(true);

        pieChart.setRotationAngle(0);
        // enable rotation of the chart by touch
        pieChart.setRotationEnabled(true);
        pieChart.setHighlightPerTapEnabled(true);


        pieChart.setEntryLabelColor(getResources().getColor(R.color.cardTeal));

        pieChart.setEntryLabelTextSize(7f);

    }
}