package com.saarimproj.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.saarimproj.myapplication.Adapters.ViewPagerAdapter;
import com.saarimproj.myapplication.Tools.SharedPrefs;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager2;
    ViewPagerAdapter viewPagerAdapter;
    SharedPrefs prefs;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Personal Budget Tracker");


        inStart();
        setListeners();




    }
    private void inStart() {
        viewPager2=findViewById(R.id.viewPager);
        tabLayout=findViewById(R.id.tabs);
        setupBiometric();
        prefs=new SharedPrefs(MainActivity.this);
        if(getAppLock()){
            biometricPrompt.authenticate(promptInfo);
        }else{

        }


        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPager2.setAdapter(viewPagerAdapter);

        tabLayout.setupWithViewPager(viewPager2);
        setupTabIcons();

    }

    private void setupBiometric() {
        executor = ContextCompat.getMainExecutor(this);
        biometricPrompt = new BiometricPrompt(MainActivity.this,
                executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode,
                                              @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);


            }

            @Override
            public void onAuthenticationSucceeded(
                    @NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Use account password")
                .build();

    }


    private boolean getAppLock() {
        return prefs.getBool("applock");
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(R.drawable.pencil);
    }

    private void setListeners() {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {


        case R.id.search: {
            Intent intent = new Intent(MainActivity.this, SearchActivity.class);
            startActivity(intent);
            return (true);
        }
        case R.id.setting: {
            Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(intent);
            return(true);
        }

        case R.id.charts:
            //add the function to perform here
            Intent intent=new Intent(MainActivity.this,GraphActivity.class);
            startActivity(intent);
            return(true);

    }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    protected void onResume() {
        super.onResume();
       inStart();
    }
}