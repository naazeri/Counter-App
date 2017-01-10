package ir.nazery.zekrshomar.database;

import android.os.Bundle;

import ir.nazery.zekrshomar.untils.Constant;

/***
 * Created by REZA on 1/25/2016
 ***/
public class Zekr {

    private String zekrName;
    private Integer zekrCount;
    private Integer position;

    public Zekr(Bundle bundle) {
        if (bundle != null) {
            this.zekrName = bundle.getString(Constant.ZEKR_NAME, "");
            this.zekrCount = bundle.getInt(Constant.ZEKR_COUNT, 0);
            this.position = bundle.getInt(Constant.ZEKR_POSITION, -1);
        } else {
            this.zekrName = "";
            this.zekrCount = 0;
            this.position = -1;
        }
    }

    public Zekr(String zekrName, int zekrCount, int position) {
        this.zekrName = zekrName;
        this.zekrCount = zekrCount;
        this.position = position;
    }

    public Zekr() {
        this.zekrName = "";
        this.zekrCount = 0;
        this.position = -1;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString(Constant.ZEKR_NAME, getZekrName());
        b.putInt(Constant.ZEKR_COUNT, getZekrCountAsInteger());
        b.putInt(Constant.ZEKR_POSITION, getPosition());
        return b;
    }

    public String getZekrName() {
        return zekrName;
    }

    public void setZekrName(String zekrName) {
        this.zekrName = zekrName;
    }

    public Integer getZekrCountAsInteger() {
        return zekrCount;
    }

    public String getZekrCountAsString() {
        return zekrCount.toString();
    }

    public void setZekrCount(int zekrCount) {
        this.zekrCount = zekrCount;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }
}
