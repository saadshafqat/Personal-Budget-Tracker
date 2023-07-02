package com.saarimproj.myapplication;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.saarimproj.myapplication.Adapters.CurrencyAdapter;
import com.saarimproj.myapplication.Models.CurrencyModel;
import com.saarimproj.myapplication.Tools.Constraints;
import com.saarimproj.myapplication.Tools.SharedPrefs;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class onBoarding extends AppCompatActivity {
    private static final int PERMISSION_REQUEST_CODE = 101;
    TextView currency;
ImageView edit;
MaterialCardView next;
SharedPrefs prefs;
List<CurrencyModel> listCurrency=new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding);


        inStart();
        setListeners();

    }
    private void requestPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Toast.makeText(onBoarding.this, "Allow Storage Permissions to store PDF", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s",getApplicationContext().getPackageName())));
                startActivityForResult(intent, 2296);

            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2296);
            }
        } else {
            //below android 11
            ActivityCompat.requestPermissions(onBoarding.this, new String[]{WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }
    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int result = ContextCompat.checkSelfPermission(onBoarding.this, READ_EXTERNAL_STORAGE);
            int result1 = ContextCompat.checkSelfPermission(onBoarding.this, WRITE_EXTERNAL_STORAGE);
            return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
        }
    }
    private void setListeners() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkPermission()){
                    requestPermission();
                }else{
                    Intent intent=new Intent(onBoarding.this,ProfileActivity.class);
                    startActivity(intent);
                }


            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadJSONDataintoList();
                popupCurrencyDialog();
            }
        });
    }

    private void inStart() {

        prefs=new SharedPrefs(onBoarding.this);
        currency=findViewById(R.id.currname);
        edit=findViewById(R.id.edit);
        next=findViewById(R.id.next);

        if(prefs.getBool(Constraints.userRegistered)){
            Intent intent=new Intent(onBoarding.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

        setUpCurrency();
    }
    private void setUpCurrency() {
        String currencytxt=prefs.getStr("currency");
        if(currencytxt.equalsIgnoreCase("none")){
            currency.setText("INR-Indian rupee");
            prefs.setStr(Constraints.currency,"INR-Indian rupee");

        }else{
            currency.setText(currencytxt);
        }

    }

    private void popupCurrencyDialog() {
        Dialog dialog;
        dialog=new Dialog(this);
        dialog.setContentView(R.layout.layout_currency);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        dialog.show();
        RecyclerView recyclerView=dialog.findViewById(R.id.currency_RecyclerView);
        EditText searchtxt=dialog.findViewById(R.id.search_currecy);
        ImageView back=dialog.findViewById(R.id.back);
        CurrencyAdapter adapter=new CurrencyAdapter(listCurrency, dialog.getContext(),dialog);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(onBoarding.this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);
        recyclerView.setAdapter(adapter);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });


        searchtxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


            }

            @Override
            public void afterTextChanged(Editable editable) {
                List<CurrencyModel> filterlist = new ArrayList<>();
                for (CurrencyModel item : listCurrency) {


                    if (item.getName().toLowerCase().contains(editable.toString().toLowerCase())) {
                        filterlist.add(item);
                    }else if (item.getCode().toLowerCase().contains(editable.toString().toLowerCase())) {
                        filterlist.add(item);
                    }


                    adapter.filteredList(filterlist);
                }
                if (filterlist.isEmpty()) {

                }else{
                }


            }
        });
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                setUpCurrency();
            }
        });





    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("Currency.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
    private void loadJSONDataintoList() {
        listCurrency.clear();
        JSONObject obj = null;
        try {
            obj = new JSONObject(loadJSONFromAsset());
            JSONArray keys = obj.names();
            for(int i=0;i<keys.length();i++){
                JSONObject key  = obj.getJSONObject(keys.getString(i));
                String abbr = keys.getString(i);
                String code = key.getString("code");
                String symbol_native = key.getString("symbol_native");
                String name = key.getString("name");
                CurrencyModel currencyModel=new CurrencyModel(name,symbol_native,abbr,code);
                listCurrency.add(currencyModel);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2296) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    // perform action when allow permission success
                } else {
                    Toast.makeText(this, "Allow permission for storage access to save PDF!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        // perform action when allow permission success
                    } else {
                        Toast.makeText(this, "Allow permission for storage access to save pdf!", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }
}