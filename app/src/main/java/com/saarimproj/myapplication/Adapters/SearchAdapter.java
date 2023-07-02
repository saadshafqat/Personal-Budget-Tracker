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
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    List<IncomeModel> list;
    Context context;
    DBHelper dbHelper;
    Boolean incomecheck=true;
    Boolean expensecheck=false;
    SharedPrefs prefs;

    public SearchAdapter(List<IncomeModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_search, parent, false);
        return new SearchAdapter.ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull SearchAdapter.ViewHolder holder, int position) {

        holder.title.setText(list.get(position).getTitle());
        holder.description.setText(list.get(position).getDescription());
        holder.date.setText(list.get(position).getDate());
        holder.amount.setText(list.get(position).getTransammount());

        if(list.get(position).getCategory().equalsIgnoreCase("Expense")){
            holder.amount.setTextColor(context.getResources().getColor(R.color.cadRed));
        }
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupOptions(holder,holder.getAdapterPosition());
            }
        });

    }
    private void popupOptions(SearchAdapter.ViewHolder holder, int adapterPosition) {
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
        dbHelper.deletefromTransaction(list.get(adapterPosition).getTransID());
        list.remove(adapterPosition);
        notifyDataSetChanged();

    }

    private void editItem(SearchAdapter.ViewHolder holder, int adapterPosition) {
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

        title.setText(list.get(adapterPosition).getTitle());
        amount.setText(list.get(adapterPosition).getTransammount());
        description.setText(list.get(adapterPosition).getDescription());

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
                        String date=list.get(adapterPosition).getDate();
                        if(incomecheck){
                            list.get(adapterPosition).setTransammount(amount.getText().toString());
                            list.get(adapterPosition).setTitle(title.getText().toString());
                            list.get(adapterPosition).setDescription(description.getText().toString());
                            list.get(adapterPosition).setType("Income");
                            notifyDataSetChanged();
                            dbHelper.editTransaction(prefs.getStr(Constraints.userUID),list.get(adapterPosition).getTransID(),list.get(adapterPosition).getDate(),list.get(adapterPosition).getMonth(),list.get(adapterPosition).getYear(),amount.getText().toString(),title.getText().toString(),description.getText().toString(),"none","Income");
                            dialog.dismiss();

                        }else{
                            list.get(adapterPosition).setTransammount(amount.getText().toString());
                            list.get(adapterPosition).setTitle(title.getText().toString());
                            list.get(adapterPosition).setDescription(description.getText().toString());
                            list.get(adapterPosition).setType("Expense");
                            notifyDataSetChanged();
                            dbHelper.editTransaction(prefs.getStr(Constraints.userUID),list.get(adapterPosition).getTransID(),list.get(adapterPosition).getDate(),list.get(adapterPosition).getMonth(),list.get(adapterPosition).getYear(),amount.getText().toString(),title.getText().toString(),description.getText().toString(),"none","Expense");
                            //  getExpenseData(numberYear,numberMonth,numberDate);
                            // updateBalance();
                            dialog.dismiss();
                        }


                    }catch (Exception e){

                    }



                }
            }
        });





        dialog.show();



    }


    @Override
    public int getItemCount() {
        return list.size();
    }
    public void filteredList(List<IncomeModel> filterlist) {
        list = filterlist;
        notifyDataSetChanged();
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
        TextView title;
        TextView description;
        TextView date;
        TextView amount;
        ConstraintLayout constraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.search_title);
            description=itemView.findViewById(R.id.search_description);
            date=itemView.findViewById(R.id.search_date);
            amount=itemView.findViewById(R.id.search_amount);
            constraintLayout=itemView.findViewById(R.id.searchLayout);

        }
    }
}
