package com.saarimproj.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.saarimproj.myapplication.Models.MonthlyParentModel;
import com.saarimproj.myapplication.R;

import java.util.Calendar;
import java.util.List;

public class ParentMonthlyAdapter extends RecyclerView.Adapter<ParentMonthlyAdapter.ViewHolder> {
    List<MonthlyParentModel> parentModelList;
    Context context;
    private RecyclerView.RecycledViewPool
            viewPool
            = new RecyclerView
            .RecycledViewPool();

    final String[] Selected = new String[]{"January", "February", "March", "April",
            "May", "June", "July", "Augest", "September", "October", "November", "December"};
    public ParentMonthlyAdapter(List<MonthlyParentModel> parentModelList, Context context) {
        this.parentModelList = parentModelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ParentMonthlyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.item_parentmonthly, parent, false);
        return new ParentMonthlyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ParentMonthlyAdapter.ViewHolder holder, int position) {
        String s1=parentModelList.get(position).getParentdate();
        String[] words=s1.split("\\s");
       holder
               .ParentItemTitle
               .setText(words[0]+" "+Selected[Integer.parseInt(words[1])]+" "+words[2]);



        LinearLayoutManager layoutManager
                = new LinearLayoutManager(
                holder.ChildRecyclerView.getContext(), LinearLayoutManager.VERTICAL, false);

        layoutManager
                .setInitialPrefetchItemCount(
                        parentModelList.get(position).getChildModelList()
                                .size());

        ChildMonthlyAdapter childItemAdapter
                = new ChildMonthlyAdapter(
                parentModelList.get(position).getChildModelList());
        holder.ChildRecyclerView
                .setLayoutManager(layoutManager);
        holder.ChildRecyclerView
                .setAdapter(childItemAdapter);
        holder
                .ChildRecyclerView
                .setRecycledViewPool(viewPool);

        float sumIncome=0;
        float sumExpense=0;

        for(int i=0;i<parentModelList.get(position).getChildModelList().size();i++){
            if(!parentModelList.get(position).getChildModelList().get(i).getIncomeAmount().equalsIgnoreCase("")){
                sumIncome+=Float.parseFloat(parentModelList.get(position).getChildModelList().get(i).getIncomeAmount());

            }
            if(!parentModelList.get(position).getChildModelList().get(i).getExpenseAmount().equalsIgnoreCase("")){
                sumExpense+=Float.parseFloat(parentModelList.get(position).getChildModelList().get(i).getExpenseAmount());

            }

        }
        holder.incomeSum.setText(""+sumIncome);
        holder.expenseSum.setText(""+sumExpense);
        float sum=sumIncome-sumExpense;
        holder.balanceMonthly.setText("Balance= "+sum);



    }

    @Override
    public int getItemCount() {
        return parentModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView ParentItemTitle;
        private TextView incomeSum;
        private TextView expenseSum;
        private TextView balanceMonthly;
        private RecyclerView ChildRecyclerView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ParentItemTitle=itemView.findViewById(R.id.parent_date);
            incomeSum=itemView.findViewById(R.id.txt_incomeSum);
            expenseSum=itemView.findViewById(R.id.txt_expenseSum);
            balanceMonthly=itemView.findViewById(R.id.balanceMonthly);
            ChildRecyclerView=itemView.findViewById(R.id.childMonthlyRecyclerView);
        }
    }
}
