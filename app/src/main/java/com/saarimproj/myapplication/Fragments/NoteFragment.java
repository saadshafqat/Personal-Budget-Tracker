package com.saarimproj.myapplication.Fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.saarimproj.myapplication.Adapters.NotesAdapter;
import com.saarimproj.myapplication.Models.NotesModel;
import com.saarimproj.myapplication.R;
import com.saarimproj.myapplication.Tools.Constraints;
import com.saarimproj.myapplication.Tools.DBHelper;
import com.saarimproj.myapplication.Tools.SharedPrefs;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {
ImageView back,forward;
TextView date,nothingfound;
int numberMonth,numberyear;
RecyclerView recyclerView;
FloatingActionButton addNote;
NotesAdapter adapter;
    Dialog dialog;
    List<NotesModel> listNotes=new ArrayList<>();
    DBHelper helper;
    SharedPrefs prefs;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
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
        View view=inflater.inflate(R.layout.fragment_note, container, false);
        back=view.findViewById(R.id.imageView6);
        forward=view.findViewById(R.id.imageView7);
        recyclerView=view.findViewById(R.id.notesRecyclerView);
        nothingfound=view.findViewById(R.id.notxtNotes);
        date=view.findViewById(R.id.textView7);
        addNote=view.findViewById(R.id.addnote);
        dialog=new Dialog(getContext());
        helper=new DBHelper(getContext());
        prefs=new SharedPrefs(getContext());





        final Calendar c = Calendar.getInstance();
        numberMonth = c.get(Calendar.MONTH);
        numberyear=c.get(Calendar.YEAR);
        setListeners();
        setDate();






        return view;
    }

    private void setDate() {
        final String[] Selected = new String[]{"January", "February", "March", "April",
                "May", "June", "July", "Augest", "September", "October", "November", "December"};


        date.setText(Selected[numberMonth]+" "+numberyear);

        getNotes(Selected[numberMonth]+" "+numberyear);



    }

    private void getNotes(String s) {
        listNotes.clear();
        Cursor cursor=helper.getNotesfrom(s);
        while (cursor.moveToNext()) {
            NotesModel model=new NotesModel(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getString(5));
            listNotes.add(model);
        }
        cursor.close();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter=new NotesAdapter(getContext(),listNotes);
        recyclerView.setAdapter(adapter);
        if(listNotes.isEmpty()){
            nothingfound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }else{
            nothingfound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }


    }

    private void setListeners() {

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             openDialog();
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numberMonth==0){
                    numberMonth=11;
                    numberyear--;
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
                    numberyear++;
                }else{
                    numberMonth++;
                }
                setDate();

            }
        });
    }

    private void openDialog() {

        final String[] Selected = new String[]{"January", "February", "March", "April",
                "May", "June", "July", "Augest", "September", "October", "November", "December"};

        final Calendar c = Calendar.getInstance();
        int numberMonth = c.get(Calendar.MONTH);
        int numberday = c.get(Calendar.DATE);
        int numberyear=c.get(Calendar.YEAR);

        dialog.setContentView(R.layout.item_addnote);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.));
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);



        TextView daynumber=dialog.findViewById(R.id.day);
        TextView monthyear=dialog.findViewById(R.id.textView7);
        TextView daytext=dialog.findViewById(R.id.textView8);
        MaterialCardView dateCard=dialog.findViewById(R.id.dateCard);
        MaterialCardView addNote=dialog.findViewById(R.id.addnote);
        MaterialCardView cancelNote=dialog.findViewById(R.id.cancelnote);
        TextInputEditText notedescription=dialog.findViewById(R.id.notedescription);

        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(notedescription.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Note cannot be empty", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }else{
                    try {
                        if(helper.insertNote(createTransactionID(),prefs.getStr(Constraints.userUID),daynumber.getText().toString(),notedescription.getText().toString(),monthyear.getText().toString(),daytext.getText().toString())){
                           getNotes(monthyear.getText().toString());
                            dialog.dismiss();
                        }else{
                            Toast.makeText(getContext(), "Note cannot added", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        cancelNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });



        daynumber.setText(""+numberday);
        monthyear.setText(Selected[numberMonth]+" "+numberyear);
        switch (c.get(Calendar.DAY_OF_WEEK)){
            case Calendar.SATURDAY:
                daytext.setText("Saturday");
                break;
            case Calendar.MONDAY:
                daytext.setText("Monday");
                break;
            case Calendar.TUESDAY:
                daytext.setText("Tuesday");
                break;
            case Calendar.WEDNESDAY:
                daytext.setText("Wednesday");
                break;
            case Calendar.THURSDAY:
                daytext.setText("Thursday");
                break;
            case Calendar.FRIDAY:
                daytext.setText("Friday");
                break;
            case Calendar.SUNDAY:
                daytext.setText("Sunday");
                break;
        }
        dateCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int mYear, mMonth, mDay;
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(dialog.getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                Calendar selectedDate = Calendar.getInstance();
                                selectedDate.set(year, monthOfYear, dayOfMonth);
                                int numberMonth = selectedDate.get(Calendar.MONTH);
                                int numberday = selectedDate.get(Calendar.DATE);
                                int numberyear=selectedDate.get(Calendar.YEAR);
                                daynumber.setText(""+numberday);
                                monthyear.setText(Selected[numberMonth]+" "+numberyear);
                                switch (selectedDate.get(Calendar.DAY_OF_WEEK)){
                                    case Calendar.SATURDAY:
                                        daytext.setText("Saturday");
                                        break;
                                    case Calendar.MONDAY:
                                        daytext.setText("Monday");
                                        break;
                                    case Calendar.TUESDAY:
                                        daytext.setText("Tuesday");
                                        break;
                                    case Calendar.WEDNESDAY:
                                        daytext.setText("Wednesday");
                                        break;
                                    case Calendar.THURSDAY:
                                        daytext.setText("Thursday");
                                        break;
                                    case Calendar.FRIDAY:
                                        daytext.setText("Friday");
                                        break;
                                    case Calendar.SUNDAY:
                                        daytext.setText("Sunday");
                                        break;
                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });










        dialog.show();
    }
    public String createTransactionID() throws Exception{
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }
}