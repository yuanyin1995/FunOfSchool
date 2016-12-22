package com.funOfSchool.model;

/**
 * Created by asus1 on 2016/12/21.
 */
public class Myprize {
    private String myprizeId;
    private String myprizeName;
    private String validDate;

    public Myprize(String myprizeId, String myprizeName, String validDate) {
        this.myprizeId = myprizeId;
        this.myprizeName = myprizeName;
        this.validDate = validDate;
    }

    public String getMyprizeId() {
        return myprizeId;
    }

    public void setMyprizeId(String myprizeId) {
        this.myprizeId = myprizeId;
    }

    public String getMyprizeName() {
        return myprizeName;
    }

    public void setMyprizeName(String myprizeName) {
        this.myprizeName = myprizeName;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }
}
