package com.saarimproj.myapplication.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.saarimproj.myapplication.Models.IncomeModel;
import com.saarimproj.myapplication.R;
import com.saarimproj.myapplication.Tools.Constraints;
import com.saarimproj.myapplication.Tools.DBHelper;
import com.saarimproj.myapplication.Tools.SharedPrefs;

import java.util.Calendar;
import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
Context context;
List<IncomeModel> listData;
Boolean incomecheck=true;
Boolean expensecheck=false;
DBHelper dbHelper;
SharedPrefs prefs;

    public IncomeAdapter(Context context, List<IncomeModel> listData) {
        this.context = context;
        this.listData = listData;
    }

    @NonNull
    @Override
    public IncomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_income, parent, false);
        return new IncomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeAdapter.ViewHolder holder, int position) {
        holder.incomeTitle.setText(listData.get(position).getTitle());
        holder.incomeDescription.setText(listData.get(position).getDescription());
        holder.incomeAmount.setText(listData.get(position).getTransammount());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupOptions(holder,holder.getAdapterPosition());
            }
        });


    }

    @Override
    public int getItemCount() {
        return listData.size();
    }
    private void popupOptions(IncomeAdapter.ViewHolder holder, int adapterPosition) {
        Dialog dialog;
        dialog=new Dialog(context);

        dialog.setContentView(R.layout.item_options);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);

        ImageView edit=dialog.findViewById(R.id.imageView5);
        ImageView delete=dialog.findViewById(R.id.imageView17);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editItem(holder,adapterPosition);
                dialog.dismiss();
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteItem(adapterPosition);
                dialog.dismiss();
            }
        });



        dialog.show();

    }

    private void deleteItem(int adapterPosition) {
        dbHelper=new DBHelper(context);
        dbHelper.deletefromTransaction(listData.get(adapterPosition).getTransID());
        listData.remove(adapterPosition);
        notifyDataSetChanged();

    }

    private void editItem(ViewHolder holder, int adapterPosition) {
        Dialog dialog=new Dialog(context);
        final String[] Selected = new String[]{"January", "February", "March", "April",
                "May", "June", "July", "Augest", "September", "October", "November", "December"};

        final Calendar c = Calendar.getInstance();
//        int numberMonth = c.get(Calendar.MONTH);
//        int numberday = c.get(Calendar.DATE);
//        int numberyear=c.get(Calendar.YEAR);

        dialog.setContentView(R.layout.item_addtransaction);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setGravity(Gravity.BOTTOM);



//        TextView daynumber=dialog.findViewById(R.id.day);
//        TextView monthyear=dialog.findViewById(R.id.textView7);
//        TextView daytext=dialog.findViewById(R.id.textView8);
        MaterialCardView income=dialog.findViewById(R.id.materialCardView3);
        MaterialCardView expense=dialog.findViewById(R.id.materialCardView2);
        income.setStrokeColor(context.getResources().getColor(R.color.cardTeal));
        FloatingActionButton addTrans=dialog.findViewById(R.id.editText6);

        EditText title=dialog.findViewById(R.id.editText);
        TextInputEditText description=dialog.findViewById(R.id.textInputEditText);
        EditText amount=dialog.findViewById(R.id.editText2);

        title.setText(listData.get(adapterPosition).getTitle());
        amount.setText(listData.get(adapterPosition).getTransammount());
        description.setText(listData.get(adapterPosition).getDescription());

        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                income.setStrokeColor(context.getResources().getColor(R.color.cardTeal));
                expense.setStrokeColor(context.getResources().getColor(R.color.white));
                incomecheck=true;
                expensecheck=false;

            }
        });
        expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                expense.setStrokeColor(context.getResources().getColor(R.color.cadRed));
                income.setStrokeColor(context.getResources().getColor(R.color.white));
                incomecheck=false;
                expensecheck=true;

            }
        });
        dbHelper=new DBHelper(context);
        prefs=new SharedPrefs(context);

        addTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValues(title,description,amount)){
                    try{
                        String date=listData.get(adapterPosition).getDate();
                        if(incomecheck){
                            dbHelper.editTransaction(prefs.getStr(Constraints.userUID),listData.get(adapterPosition).getTransID(),listData.get(adapterPosition).getDate(),listData.get(adapterPosition).getMonth(),listData.get(adapterPosition).getYear(),amount.getText().toString(),title.getText().toString(),description.getText().toString(),"none","Income");
                            listData.get(adapterPosition).setTransammount(amount.getText().toString());
                            listData.get(adapterPosition).setTitle(title.getText().toString());
                            listData.get(adapterPosition).setDescription(description.getText().toString());
                            listData.get(adapterPosition).setType("Income");
                            notifyDataSetChanged();
                         dialog.dismiss();

                        }else{

                            dbHelper.editTransaction(prefs.getStr(Constraints.userUID),listData.get(adapterPosition).getTransID(),listData.get(adapterPosition).getDate(),listData.get(adapterPosition).getMonth(),listData.get(adapterPosition).getYear(),amount.getText().toString(),title.getText().toString(),description.getText().toString(),"none","Expense");
                          //  getExpenseData(numberYear,numberMonth,numberDate);
                           // updateBalance();
                            listData.get(adapterPosition).setTransammount(amount.getText().toString());
                            listData.get(adapterPosition).setTitle(title.getText().toString());
                            listData.get(adapterPosition).setDescription(description.getText().toString());
                            listData.get(adapterPosition).setType("Expense");
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }


                    }catch (Exception e){

                    }



                }
            }
        });





        dialog.show();



    }

    private boolean checkValues(EditText title, TextInputEditText description, EditText amount) {
        if(title.getText().toString().isEmpty()){
            Toast.makeText(context, "Title cannot empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(description.getText().toString().isEmpty()){
            Toast.makeText(context, "Description cannot empty", Toast.LENGTH_SHORT).show();

            return  false;
        }
        else if(amount.getText().toString().isEmpty()){
            Toast.makeText(context, "Amount cannot empty", Toast.LENGTH_SHORT).show();

            return false;
        }
        return  true;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView incomeTitle,incomeDescription,incomeAmount;
        MaterialCardView layout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            incomeAmount=itemView.findViewById(R.id.txt_transactionAmount);
            incomeTitle=itemView.findViewById(R.id.txt_transactionTitle);
            incomeDescription=itemView.findViewById(R.id.txt_transactionDescription);
            layout=itemView.findViewById(R.id.item_notes_layout);
        }
    }
}

