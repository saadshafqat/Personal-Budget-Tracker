package com.saarimproj.myapplication.Fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.saarimproj.myapplication.Adapters.IncomeAdapter;
import com.saarimproj.myapplication.Adapters.YearlyAdapter;
import com.saarimproj.myapplication.Models.CurrencyModel;
import com.saarimproj.myapplication.Models.ParentYearlyModel;
import com.saarimproj.myapplication.Models.YearlyModel;
import com.saarimproj.myapplication.R;
import com.saarimproj.myapplication.Tools.Constraints;
import com.saarimproj.myapplication.Tools.DBHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link YearlyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class YearlyFragment extends Fragment {
    ImageView back,forward;
    int numberYear;
    DBHelper dbHelper;
    List<ParentYearlyModel> parentYearlyModels=new ArrayList<>();
    List<YearlyModel> childyearlyModel=new ArrayList<>();
    TextView year;
    RecyclerView recyclerView;
    YearlyAdapter adapter;
    FloatingActionButton pdfsave;
    final String[] Selected = new String[]{"January", "February", "March", "April",
            "May", "June", "July", "Augest", "September", "October", "November", "December"};
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public YearlyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment YearlyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static YearlyFragment newInstance(String param1, String param2) {
        YearlyFragment fragment = new YearlyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v=inflater.inflate(R.layout.fragment_yearly, container, false);
       back=v.findViewById(R.id.imageView6);
       forward=v.findViewById(R.id.imageView7);
       year=v.findViewById(R.id.textView7);
       recyclerView=v.findViewById(R.id.yearlyRecyclerView);
       pdfsave=v.findViewById(R.id.action_yearly);

        final Calendar c = Calendar.getInstance();
//        numberMonth = c.get(Calendar.MONTH);
        numberYear=c.get(Calendar.YEAR);

       setListeners();

        setDate();

        return v;
    }

    private void setDate() {
        year.setText(""+numberYear);
        getData();


    }

    private void getData() {
        childyearlyModel.clear();
        parentYearlyModels.clear();
        final String[] Selected = new String[]{"Jan.", "Feb.", "March", "April",
                "May", "June", "July", "Aug.", "Sept.", "October", "Nov.", "Dec"};
        dbHelper=new DBHelper(getContext());
        Cursor cursor=dbHelper.countTransactions(String.valueOf(numberYear));
        while (cursor.moveToNext()){
            childyearlyModel=getSplitInfo(cursor.getString(0));
            ParentYearlyModel parentYearlyModel=new ParentYearlyModel(Selected[Integer.parseInt(cursor.getString(0))],childyearlyModel);
            parentYearlyModels.add(parentYearlyModel);
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new YearlyAdapter(parentYearlyModels,getContext());
        recyclerView.setAdapter(adapter);



    }

    private List<YearlyModel> getSplitInfo(String month) {
        dbHelper=new DBHelper(getContext());
        Cursor cursor=dbHelper.getTransaction(month,"Income");
        float incomeSum=0;
        while (cursor.moveToNext()){
            incomeSum+=Float.parseFloat(cursor.getString(5));
        }
        cursor.close();
        Cursor cursor2=dbHelper.getTransaction(month,"Expense");
        float expenseSum=0;
        while (cursor2.moveToNext()){
            expenseSum+=Float.parseFloat(cursor2.getString(5));
        }
        cursor2.close();

        float finalsum=incomeSum-expenseSum;

        YearlyModel model=new YearlyModel(String.valueOf(incomeSum),String.valueOf(expenseSum),String.valueOf(finalsum));

        List<YearlyModel> list=new ArrayList<>();
        list.add(model);
        return list;





    }

    private void setListeners() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    numberYear--;
                    setDate();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               numberYear++;
                setDate();

            }
        });
        pdfsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPdf();
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
            setDate();
        }
    }
    private void createPdf() throws FileNotFoundException, DocumentException {
        Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
        Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfs");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
            Log.i("MonthlyFragment", "Pdf Directory created");
        }

        //Create time stamp
        Date date = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(date);

        File myFile = new File(pdfFolder + timeStamp + ".pdf");
        Constraints.path= pdfFolder.toString() + timeStamp.toString() + ".pdf";

        OutputStream output = new FileOutputStream(myFile);

        //Step 1
        Document document = new Document();

        //Step 2
        PdfWriter.getInstance(document, output);

        //Step 3
        document.addTitle("Monthly Report");
        document.setPageSize(PageSize.LETTER);

        //open document
        document.open();

        DecimalFormat df = new DecimalFormat("0.00");
        //specify column widths
        float[] columnWidths = {1.5f, 2f, 3f};
        //create PDF table with the given widths
        PdfPTable table = new PdfPTable(columnWidths);
        // set table width a percentage of the page width
        table.setWidthPercentage(90f);

        //insert column headings
//        insertCell(table, "Order No", Element.ALIGN_RIGHT, 1, bfBold12);
//        insertCell(table, "Account No", Element.ALIGN_LEFT, 1, bfBold12);
//        insertCell(table, "Account Name", Element.ALIGN_LEFT, 1, bfBold12);
//        insertCell(table, "Order Total", Element.ALIGN_RIGHT, 1, bfBold12);
//        table.setHeaderRows(1);

        //insert an empty row


        for(int i=0;i<parentYearlyModels.size();i++)
        {

            float expensesum=0;
            float incomesum=0;
            float balancesum=0;
            insertCell(table, parentYearlyModels.get(i).getMonth(), Element.ALIGN_LEFT, 4, bfBold12);
            insertCell(table, "Total Income", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Total Expense", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Balance", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);
            for(int j=0; j<parentYearlyModels.get(i).getYearlyModels().size(); j++){
                balancesum+=Float.parseFloat(parentYearlyModels.get(i).getYearlyModels().get(j).getTotalBalance());
                    insertCell(table, parentYearlyModels.get(i).getYearlyModels().get(j).getTotalIncome(), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, parentYearlyModels.get(i).getYearlyModels().get(j).getTotalExpense(), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, parentYearlyModels.get(i).getYearlyModels().get(j).getTotalBalance(), Element.ALIGN_LEFT, 1, bf12);

            }
            float sum=incomesum-expensesum;
            insertCell(table, "Balance: "+balancesum, Element.ALIGN_RIGHT, 3, bfBold12);

        }



        //just some random data to fill

        //merge the cells to create a footer for that section

        //repeat the same as above to display another location

        Date date2 = new Date() ;
        String timeStampnew = new SimpleDateFormat("dd-MM-yyyy").format(date2);
        Paragraph paragraph = new Paragraph("Created: "+timeStampnew );
        //add the PDF table to the paragraph
        paragraph.add(table);
        // add the paragraph to the document
        document.add(paragraph);

        //Step 5: Close the document
        document.close();
        Toast.makeText(getContext(), "Document Saved to Internal Storage/Documents/"+timeStamp+".pdf", Toast.LENGTH_SHORT).show();


    }
    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(align);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);

    }

}