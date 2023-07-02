package com.saarimproj.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.google.android.material.textfield.TextInputEditText;
import com.saarimproj.myapplication.Adapters.CurrencyAdapter;
import com.saarimproj.myapplication.Adapters.SearchAdapter;
import com.saarimproj.myapplication.Models.CurrencyModel;
import com.saarimproj.myapplication.Models.IncomeModel;
import com.saarimproj.myapplication.Tools.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {
TextInputEditText searchtxt;
RecyclerView recyclerView;
SearchAdapter adapter;
List<IncomeModel> datalist=new ArrayList<>();
DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // and this
                finish();
            }
        });

        inStart();
        getTransactions();





    }

    private void getTransactions() {
        datalist.clear();
        Cursor cursor= dbHelper.getAllTransactions();
        while (cursor.moveToNext()) {
            IncomeModel model=new IncomeModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
            datalist.add(model);

        }
        cursor.close();
        adapter=new SearchAdapter(datalist, SearchActivity.this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);






        searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<IncomeModel> filterlist = new ArrayList<>();
                for (IncomeModel item : datalist) {


                    if (item.getTitle().toLowerCase().contains(editable.toString().toLowerCase())) {
                        filterlist.add(item);
                    }else if (item.getDescription().toLowerCase().contains(editable.toString().toLowerCase())) {
                        filterlist.add(item);
                    }
                    else if (item.getDate().toLowerCase().contains(editable.toString().toLowerCase())) {
                        filterlist.add(item);
                    }  else if (item.getTransammount().toLowerCase().contains(editable.toString().toLowerCase())) {
                        filterlist.add(item);
                    }


                    adapter.filteredList(filterlist);
                }
                if (filterlist.isEmpty()) {

                }else{
                }
            }
        });



    }

    private void inStart() {
        searchtxt=findViewById(R.id.searchView);
        recyclerView=findViewById(R.id.searchRecyclerView);
        dbHelper=new DBHelper(SearchActivity.this);



    }
}