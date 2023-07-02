package com.saarimproj.myapplication.Models;

public class YearlyModel {
    String totalIncome;
    String totalExpense;
    String totalBalance;

    public YearlyModel( String totalIncome, String totalExpense, String totalBalance) {

        this.totalIncome = totalIncome;
        this.totalExpense = totalExpense;
        this.totalBalance = totalBalance;
    }

    public String getTotalIncome() {
        return totalIncome;
    }

    public void setTotalIncome(String totalIncome) {
        this.totalIncome = totalIncome;
    }

    public String getTotalExpense() {
        return totalExpense;
    }

    public void setTotalExpense(String totalExpense) {
        this.totalExpense = totalExpense;
    }

    public String getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(String totalBalance) {
        this.totalBalance = totalBalance;
    }
}
