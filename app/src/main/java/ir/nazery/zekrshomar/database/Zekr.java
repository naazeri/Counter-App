package ir.nazery.zekrshomar.database;

import android.os.Bundle;

/***
 * Created by REZA on 1/25/2016
 ***/
public class Zekr {
    /***
     * Constant for field references
     ***/
    public static final String ZEKR_NAME = "ZN";
    public static final String ZEKR_COUNT = "ZC";
    public static final String DB_ZEKRS = "Zekrs";
    public static final String ZEKR_POSITION = "P";

    /***
     * Fields
     ***/
    private String zekrName;
    private int zekrCount;

    public Zekr(Bundle bundle) {
        if (bundle != null) {
            this.zekrName = bundle.getString(ZEKR_NAME, "");
            this.zekrCount = bundle.getInt(ZEKR_COUNT, 0);
        } else {
            this.zekrName = "";
            this.zekrCount = 0;
        }
    }

    public Zekr(String zekrName, int zekrCount) {
        this.zekrName = zekrName;
        this.zekrCount = zekrCount;
    }

    public String getZekrName() {
        return zekrName;
    }

    public void setZekrName(String zekrName) {
        this.zekrName = zekrName;
    }

    public int getZekrCountAsInteger() {
        return zekrCount;
    }

    public String getZekrCountAsString() {
        return String.valueOf(zekrCount);
    }

    public void setZekrCount(int zekrCount) {
        this.zekrCount = zekrCount;
    }

    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString(ZEKR_NAME, getZekrName());
        b.putInt(ZEKR_COUNT, getZekrCountAsInteger());
        return b;
    }
}
