package com.saarimproj.myapplication.Models;

import java.util.List;

public class MonthlyParentModel {
    String parentdate;
    List<MonthlyChildModel> childModelList;

    public MonthlyParentModel(String parentdate,  List<MonthlyChildModel> childModelList) {
        this.parentdate = parentdate;
        this.childModelList = childModelList;
    }


    public String getParentdate() {
        return parentdate;
    }

    public void setParentdate(String parentdate) {
        this.parentdate = parentdate;
    }

    public List<MonthlyChildModel> getChildModelList() {
        return childModelList;
    }

    public void setChildModelList(List<MonthlyChildModel> childModelList) {
        this.childModelList = childModelList;
    }
}
