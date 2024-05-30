package com.boom.vyveska.data;

import java.util.ArrayList;

public class profile_info {
    private static profile_info dataObject = null;

    ArrayList<PostInfo> postInfos = new ArrayList<>();
    ArrayList<PostFullInfo> orderInfos = new ArrayList<>();
    ArrayList<ZakazInfo> Infos = new ArrayList<>();
    user_info my_user_info = new user_info();

    ZakazInfo zakazInfo;

    company_info companyInfo = new company_info();


    public ArrayList<ZakazInfo> getZakaz() {
        return Infos;
    }

    public void setZakazInfo(ArrayList<ZakazInfo> Infos) {
        this.Infos = Infos;
    }
    public ArrayList<PostFullInfo> getOrder() {
        return orderInfos;
    }

    public void setOrder(ArrayList<PostFullInfo> Infos) {
        this.orderInfos = Infos;
    }
    public ArrayList<PostInfo> getPost() {
        return postInfos;
    }

    public void setPost(ArrayList<PostInfo> Infos) {
        this.postInfos = Infos;
    }
    public user_info getMy_user_info() {
        return my_user_info;
    }

    public void setMy_user_info(user_info my_user_info) {
        this.my_user_info = my_user_info;
    }


    public company_info getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(company_info companyInfo) {
        this.companyInfo = companyInfo;
    }





    public static profile_info getInstance() {
        if (dataObject == null)
            dataObject = new profile_info();
        return dataObject;
    }
}

