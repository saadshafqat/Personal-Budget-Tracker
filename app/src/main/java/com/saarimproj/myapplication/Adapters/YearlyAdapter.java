package com.saarimproj.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.saarimproj.myapplication.Models.ParentYearlyModel;
import com.saarimproj.myapplication.R;

import java.util.List;

public class YearlyAdapter extends RecyclerView.Adapter<YearlyAdapter.ViewHolder> {
    List<ParentYearlyModel> listData;
    Context context;

    public YearlyAdapter(List<ParentYearlyModel> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public YearlyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_yearly, parent, false);
        return new YearlyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YearlyAdapter.ViewHolder holder, int position) {
        holder.month.setText(listData.get(position).getMonth());
        holder.incomeSum.setText(listData.get(position).getYearlyModels().get(position).getTotalIncome());
        holder.expenseSum.setText(listData.get(position).getYearlyModels().get(position).getTotalExpense());
        holder.balance.setText(listData.get(position).getYearlyModels().get(position).getTotalBalance());

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView month;
        TextView incomeSum;
        TextView expenseSum;
        TextView balance;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            month=itemView.findViewById(R.id.txt_MonthYearly);
            incomeSum=itemView.findViewById(R.id.txt_incomeSumYearly);
            expenseSum=itemView.findViewById(R.id.txt_expenseSumYearly);
            balance=itemView.findViewById(R.id.txt_balanceYearly);
        }
    }
}
