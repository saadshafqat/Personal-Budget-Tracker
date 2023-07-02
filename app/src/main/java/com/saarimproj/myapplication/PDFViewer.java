package com.saarimproj.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.saarimproj.myapplication.Tools.Constraints;

import es.voghdev.pdfviewpager.library.PDFViewPager;

public class PDFViewer extends AppCompatActivity  {
    PDFViewPager pdfViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdfviewer);
        pdfViewer=findViewById(R.id.pdfViewPager);
        pdfViewer = new PDFViewPager(PDFViewer.this, Constraints.path);







    }


}