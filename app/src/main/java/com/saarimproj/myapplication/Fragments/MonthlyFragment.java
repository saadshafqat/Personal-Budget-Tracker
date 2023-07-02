package com.saarimproj.myapplication.Fragments;

import static androidx.core.content.PackageManagerCompat.LOG_TAG;

import android.content.Intent;
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
import com.saarimproj.myapplication.Adapters.ParentMonthlyAdapter;
import com.saarimproj.myapplication.Models.IncomeModel;
import com.saarimproj.myapplication.Models.MonthlyChildModel;
import com.saarimproj.myapplication.Models.MonthlyParentModel;
import com.saarimproj.myapplication.PDFViewer;
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
 * Use the {@link MonthlyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MonthlyFragment extends Fragment {
    ImageView back,forward;
    int numberMonth,numberYear;
    TextView date;
    TextView credit,debit,balance;
    RecyclerView parentRecyclerView;
    FloatingActionButton pdfButton;
    ParentMonthlyAdapter parentmonthlyAdapter;
    List<MonthlyParentModel> parentList=new ArrayList<>();
    List<MonthlyChildModel> childList=new ArrayList<>();
    DBHelper dbHelper;
    List<IncomeModel> listIncome=new ArrayList<>();
    List<IncomeModel> listExpense=new ArrayList<>();
    final String[] Selected = new String[]{"January", "February", "March", "April",
            "May", "June", "July", "Augest", "September", "October", "November", "December"};

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MonthlyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MonthlyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MonthlyFragment newInstance(String param1, String param2) {
        MonthlyFragment fragment = new MonthlyFragment();
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
        View v= inflater.inflate(R.layout.fragment_monthly, container, false);
        back=v.findViewById(R.id.imageView6);
        forward=v.findViewById(R.id.imageView7);
        date=v.findViewById(R.id.textView7);
        pdfButton=v.findViewById(R.id.action_pdf);
        credit=v.findViewById(R.id.txt_credit);
        debit=v.findViewById(R.id.txt_debit);
        balance=v.findViewById(R.id.txt_balance);
        parentRecyclerView=v.findViewById(R.id.parentMonthlyRecyclerView);

        final Calendar c = Calendar.getInstance();
        numberMonth = c.get(Calendar.MONTH);
        numberYear=c.get(Calendar.YEAR);
        setDate();

        setListeners();




        return v;
    }

    private void getTransactionsCount(int numberMonth,int numberYear) {
        parentList.clear();
      childList.clear();
        dbHelper=new DBHelper(getContext());
        Cursor cursor=dbHelper.countTransactions(String.valueOf(numberMonth),String.valueOf(numberYear));
        int sum=0;
        while (cursor.moveToNext()){
             childList= fillUpChildList(cursor.getString(0));

            MonthlyParentModel model=new MonthlyParentModel(cursor.getString(0),childList);
            parentList.add(model);

        }

        cursor.close();
        parentRecyclerView.setHasFixedSize(true);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        parentmonthlyAdapter=new ParentMonthlyAdapter(parentList,getContext());
        parentRecyclerView.setAdapter(parentmonthlyAdapter);


    }

    private List<MonthlyChildModel> fillUpChildList(String date) {
        List<MonthlyChildModel> listIncome=new ArrayList<>();
        List<MonthlyChildModel> listExpense=new ArrayList<>();
        List<MonthlyChildModel> finalList=new ArrayList<>();

        Cursor cursor=dbHelper.getIncomeTransaction(date,"Income");
        float sumincome=0;

        while (cursor.moveToNext()){
            sumincome+=Float.parseFloat(cursor.getString(5));
            MonthlyChildModel model=new MonthlyChildModel(cursor.getString(6),"",cursor.getString(5),"",String.valueOf(sumincome),"","");
            listIncome.add(model);
        }
        cursor.close();

       Cursor cursor2=dbHelper.getIncomeTransaction(date,"Expense");
        float sumexpense=0;

        while (cursor2.moveToNext()){
            sumexpense+=Float.parseFloat(cursor2.getString(5));
            MonthlyChildModel model=new MonthlyChildModel("",cursor2.getString(6),"",cursor2.getString(5),"",String.valueOf(sumexpense),"");
            listExpense.add(model);
        }
        cursor2.close();
        listIncome.addAll(listExpense);



        return listIncome;


    }

    private void setListeners() {
        pdfButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    createPdf();
                } catch (FileNotFoundException | DocumentException e) {
                    e.printStackTrace();
                }

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberMonth==0){
                    numberMonth=11;
                    numberYear--;
                }else{
                    numberMonth--;
                }
                setDate();
            }
        });
        forward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberMonth==11){
                    numberMonth=0;
                    numberYear++;
                }else{
                    numberMonth++;
                }
                setDate();

            }
        });
    }

    private List<String[]> getSampleData() {


        int count = 20;


        List<String[]> temp = new ArrayList<>();
        for (int i = 0; i < count; i++)
        {
            temp.add(new String[] {"C1-R"+ (i+1),"C2-R"+ (i+1)});
        }
        return  temp;


    }

    private void setDate() {
        final String[] Selected = new String[]{"January", "February", "March", "April",
                "May", "June", "July", "Augest", "September", "October", "November", "December"};
        date.setText(Selected[numberMonth]+" "+numberYear);
      getIncomeData(numberMonth,numberYear,"Income");
      getExpenseData(numberMonth,numberYear,"Expense");
      getTransactionsCount(numberMonth,numberYear);
      float cred=Float.parseFloat(credit.getText().toString());
      float deb=Float.parseFloat(debit.getText().toString());
      float sum=cred-deb;
      balance.setText(""+sum);

    }

    private void getExpenseData(int numberMonth, int numberYear, String expense) {
        listExpense.clear();
        dbHelper=new DBHelper(getContext());
        Cursor cursor=dbHelper.getIncomeMonthly(String.valueOf(numberMonth),String.valueOf(numberYear),expense);
        float sum=0;
        while (cursor.moveToNext()){
            IncomeModel model=new IncomeModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
            listIncome.add(model);
            sum+=Float.parseFloat(cursor.getString(5));
        }
        cursor.close();
        debit.setText(""+sum);

    }

    private void getIncomeData(int numberMonth, int numberYear, String income) {
        listIncome.clear();

        dbHelper=new DBHelper(getContext());
        Cursor cursor=dbHelper.getIncomeMonthly(String.valueOf(numberMonth),String.valueOf(numberYear),income);
        float sum=0;
        while (cursor.moveToNext())
        {
            IncomeModel model=new IncomeModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5),cursor.getString(6),cursor.getString(7),cursor.getString(8),cursor.getString(9));
            listIncome.add(model);
            sum+=Float.parseFloat(cursor.getString(5));
        }
        cursor.close();
        credit.setText(""+sum);
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


        for(int i=0;i<parentList.size();i++)
        {
            String s1=parentList.get(i).getParentdate();
            String[] words=s1.split("\\s");
            float expensesum=0;
            float incomesum=0;
            insertCell(table, words[0]+" "+Selected[Integer.parseInt(words[1])]+" "+words[2], Element.ALIGN_LEFT, 4, bfBold12);
            insertCell(table, "Title", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Type", Element.ALIGN_CENTER, 1, bfBold12);
            insertCell(table, "Transaction Amount", Element.ALIGN_CENTER, 1, bfBold12);
            table.setHeaderRows(1);
            for(int j=0; j<parentList.get(i).getChildModelList().size(); j++){
                if(parentList.get(i).getChildModelList().get(j).getIncomeTitle().equalsIgnoreCase("")){
                    insertCell(table, parentList.get(i).getChildModelList().get(j).getExpenseTitle(), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, "Expense", Element.ALIGN_LEFT, 1, bf12);
                    expensesum+=Float.parseFloat(parentList.get(i).getChildModelList().get(j).getExpenseAmount());
                    insertCell(table, parentList.get(i).getChildModelList().get(j).getExpenseAmount(), Element.ALIGN_LEFT, 1, bf12);

                }if(parentList.get(i).getChildModelList().get(j).getExpenseTitle().equalsIgnoreCase("")){
                    insertCell(table, parentList.get(i).getChildModelList().get(j).getIncomeTitle(), Element.ALIGN_LEFT, 1, bf12);
                    insertCell(table, "Income", Element.ALIGN_LEFT, 1, bf12);
                    incomesum+=Float.parseFloat(parentList.get(i).getChildModelList().get(j).getIncomeAmount());
                    insertCell(table, parentList.get(i).getChildModelList().get(j).getIncomeAmount(), Element.ALIGN_LEFT, 1, bf12);
                }



            }
            insertCell(table, "Total Income: "+incomesum, Element.ALIGN_RIGHT, 3, bfBold12);
            insertCell(table, "Total Expense: "+expensesum, Element.ALIGN_RIGHT, 3, bfBold12);
            float sum=incomesum-expensesum;
            insertCell(table, "Balance: "+sum, Element.ALIGN_RIGHT, 3, bfBold12);

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