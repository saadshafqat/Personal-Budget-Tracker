package com.saarimproj.myapplication;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.saarimproj.myapplication.Tools.Constraints;
import com.saarimproj.myapplication.Tools.SharedPrefs;

import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity {
    ImageView editAccountIdentity,editBankDetails,editOpenBalance;
    TextView txtAccIdentity,txtBankDetails,txtopenbalance;
    MaterialCardView btnNext;
    AlertDialog.Builder alertDialog;
    String profileName="";
    SharedPrefs prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        inStart();
        setListeners();
    }
    private void inStart() {
        alertDialog=new AlertDialog.Builder(EditProfileActivity.this);

        editAccountIdentity=findViewById(R.id.imageView2);
        editBankDetails=findViewById(R.id.imageView3);
        editOpenBalance=findViewById(R.id.imageView4);

        txtopenbalance=findViewById(R.id.text_openbalance);
        txtAccIdentity=findViewById(R.id.text_accIdentity);
        txtBankDetails=findViewById(R.id.text_bankDetails);

        btnNext=findViewById(R.id.next);

    }
    private void setListeners() {
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getError()){
                    prefs.setStr(Constraints.userAccountIdentity,txtAccIdentity.getText().toString());
                    prefs.setStr(Constraints.userBankDetails,txtBankDetails.getText().toString());
                    prefs.setStr(Constraints.useropeningbalance,txtopenbalance.getText().toString());
                    prefs.setBool(Constraints.userRegistered,true);
                    try {
                        prefs.setStr(Constraints.userUID,createTransactionID());

                    }catch (Exception e){

                    }



                    Intent intent=new Intent(EditProfileActivity.this,MainActivity.class);
                    startActivity(intent);


                }else{

                }
            }
        });


        editAccountIdentity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(txtAccIdentity.getText().toString(),txtAccIdentity);
            }
        });
        editOpenBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(txtopenbalance.getText().toString(),txtopenbalance);

            }
        });
        editBankDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(txtBankDetails.getText().toString(),txtBankDetails);

            }
        });



    }
    private boolean getError() {
        if(txtBankDetails.getText().toString().isEmpty()){
            Toast.makeText(this, "Bank Details cannot be empty", Toast.LENGTH_SHORT).show();
            return true;
        }else if(txtopenbalance.getText().toString().isEmpty()){
            Toast.makeText(this, "Opening Balance cannot be empty", Toast.LENGTH_SHORT).show();
            return true;
        }else if(txtAccIdentity.getText().toString().isEmpty()){
            Toast.makeText(this, "Account Identity cannot be empty", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void openDialog(String profile_profile, TextView textPrimaryProfile) {
        LayoutInflater li = LayoutInflater.from(EditProfileActivity.this);
        final View customLayout =li.inflate(R.layout.item_text, null);
        alertDialog.setView(customLayout);
        EditText text=customLayout.findViewById(R.id.textget);
        text.setText(profile_profile);
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                profileName=profile_profile;
                textPrimaryProfile.setText(profileName);
            }
        });
        alertDialog.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(text.getText().toString().isEmpty()){
                    Toast.makeText(EditProfileActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
                }else{
                    profileName=text.getText().toString();
                    textPrimaryProfile.setText(profileName);
                }


            }
        });
        AlertDialog alert = alertDialog.create();
        alert.setCanceledOnTouchOutside(true);
        alert.show();

    }

    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
}