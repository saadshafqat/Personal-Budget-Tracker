package com.saarimproj.myapplication.Models;

public class MonthlyChildModel {
    String incomeTitle,expenseTitle,incomeAmount,expenseAmount,totalincomeAmount,totalExpenseAmount,balance;

    public MonthlyChildModel(String incomeTitle, String expenseTitle, String incomeAmount, String expenseAmount, String totalincomeAmount, String totalExpenseAmount, String balance) {
        this.incomeTitle = incomeTitle;
        this.expenseTitle = expenseTitle;
        this.incomeAmount = incomeAmount;
        this.expenseAmount = expenseAmount;
        this.totalincomeAmount = totalincomeAmount;
        this.totalExpenseAmount = totalExpenseAmount;
        this.balance = balance;
    }

    public String getIncomeTitle() {
        return incomeTitle;
    }

    public void setIncomeTitle(String incomeTitle) {
        this.incomeTitle = incomeTitle;
    }

    public String getExpenseTitle() {
        return expenseTitle;
    }

    public void setExpenseTitle(String expenseTitle) {
        this.expenseTitle = expenseTitle;
    }

    public String getIncomeAmount() {
        return incomeAmount;
    }

    public void setIncomeAmount(String incomeAmount) {
        this.incomeAmount = incomeAmount;
    }

    public String getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(String expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public String getTotalincomeAmount() {
        return totalincomeAmount;
    }

    public void setTotalincomeAmount(String totalincomeAmount) {
        this.totalincomeAmount = totalincomeAmount;
    }

    public String getTotalExpenseAmount() {
        return totalExpenseAmount;
    }

    public void setTotalExpenseAmount(String totalExpenseAmount) {
        this.totalExpenseAmount = totalExpenseAmount;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }
}
