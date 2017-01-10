package ir.nazery.zekrshomar.database;

import android.content.Context;
import android.os.Handler;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import java.util.LinkedList;
import java.util.List;

import ir.nazery.zekrshomar.untils.Constant;

/*** Created by REZA on 1/25/2016 ***/
public class DataManager {
    public void initDB(Context context) throws Exception {
        Hawk.init(context)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setStorage(HawkBuilder.newSharedPrefStorage(context))
                .setLogLevel(LogLevel.NONE)
                .build();
    }

    public List<Zekr> getZekrs() throws Exception {
        return Hawk.get(Constant.DB_ZEKRS, new LinkedList<Zekr>());
    }

    public void setZekrs(List<Zekr> list) throws Exception {
        Hawk.put(Constant.DB_ZEKRS, list);
    }

    public void updateDB(Zekr zekr, int position) {
        List<Zekr> list = Hawk.get(Constant.DB_ZEKRS, new LinkedList<Zekr>());
        if (position >= 0) {
            list.set(position, zekr);
        } else {
            list.add(zekr);
        }
        Hawk.put(Constant.DB_ZEKRS, list);
    }

    public void updateDB(Zekr zekr) {
        List<Zekr> list = Hawk.get(Constant.DB_ZEKRS, new LinkedList<Zekr>());
        if (zekr.getPosition() >= 0) {
            list.set(zekr.getPosition(), zekr);
        } else {
            list.add(zekr);
        }
        Hawk.put(Constant.DB_ZEKRS, list);
    }
}
