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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.saarimproj.myapplication.Tools.Constraints;
import com.saarimproj.myapplication.Tools.SharedPrefs;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
ImageView editImage,editProfileName,editProfilePurpose,editAddInfo;
TextView textPrimaryProfile,textProfilePurpose,textAdditionalInfo;
CircleImageView imageView;
MaterialCardView btnNext;
AlertDialog.Builder alertDialog;
String profileName="";
SharedPrefs prefs;


Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        inStart();
        setListeners();
    }
    private void inStart() {
        prefs=new SharedPrefs(ProfileActivity.this);
        alertDialog=new AlertDialog.Builder(ProfileActivity.this);
        editImage=findViewById(R.id.imageView);
        editProfileName=findViewById(R.id.imageView2);
        editProfilePurpose=findViewById(R.id.imageView3);
        editAddInfo=findViewById(R.id.imageView4);
        btnNext=findViewById(R.id.next);
        imageView=findViewById(R.id.profile_image);
        textPrimaryProfile=findViewById(R.id.txt_profilename);
        textProfilePurpose=findViewById(R.id.txt_purpose);
        textAdditionalInfo=findViewById(R.id.txt_additionalinfo);



    }

    private void setListeners() {

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!getError()){
                    prefs.setStr(Constraints.userProfileName,textPrimaryProfile.getText().toString());
                    prefs.setStr(Constraints.userProilfePurpose,textProfilePurpose.getText().toString());
                    prefs.setStr(Constraints.userAdditionalInfo,textAdditionalInfo.getText().toString());
                    if(uri==null){
                        prefs.setStr(Constraints.userProilfePicPath,getURLForResource(R.drawable.man));
                    }else{
                        prefs.setStr(Constraints.userProilfePicPath,uri.toString());
                    }
                    Intent intent=new Intent(ProfileActivity.this,EditProfileActivity.class);
                    startActivity(intent);


                }else{

                }
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(intent,
                            "Select Picture"), 1);
            }
        });
        editProfileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(textPrimaryProfile.getText().toString(),textPrimaryProfile);
            }
        });
        editProfilePurpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(textProfilePurpose.getText().toString(),textProfilePurpose);

            }
        });
        editAddInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog(textAdditionalInfo.getText().toString(),textAdditionalInfo);

            }
        });

    }

    private boolean getError() {
        if(textPrimaryProfile.getText().toString().isEmpty()){
            Toast.makeText(this, "Profile Name cannot be empty", Toast.LENGTH_SHORT).show();
            return true;
        }else if(textProfilePurpose.getText().toString().isEmpty()){
            Toast.makeText(this, "Profile Purpose cannot be empty", Toast.LENGTH_SHORT).show();
            return true;
        }else if(textAdditionalInfo.getText().toString().isEmpty()){
            Toast.makeText(this, "Additional info cannot be empty", Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    private void openDialog(String profile_profile, TextView textPrimaryProfile) {
        LayoutInflater li = LayoutInflater.from(ProfileActivity.this);
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
                    Toast.makeText(ProfileActivity.this, "Field cannot be empty", Toast.LENGTH_SHORT).show();
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
    public String getURLForResource (int resourceId) {
        return Uri.parse("android.resource://"+R.class.getPackage().getName()+"/" +resourceId).toString();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                uri = data.getData();
                imageView.setImageURI(uri);
            }
        }



    }
}