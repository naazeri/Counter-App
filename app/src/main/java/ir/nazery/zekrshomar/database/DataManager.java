package ir.nazery.zekrshomar.database;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import java.util.LinkedList;
import java.util.List;

/*** Created by REZA on 1/25/2016 ***/
public class DataManager {
    public void initDB(Context context) throws Exception {
//        Remember.init(context, context.getPackageName());
        Hawk.init(context)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setPassword("myPASS")
                .setStorage(HawkBuilder.newSharedPrefStorage(context))
                .setLogLevel(LogLevel.NONE)
                .build();
    }

    public List<Zekr> getZekrs() throws Exception {
        return Hawk.get(Zekr.DB_ZEKRS, new LinkedList<Zekr>());
    }

    public void setZekrs(List<Zekr> list) throws Exception {
        Hawk.put(Zekr.DB_ZEKRS, list);
    }

    public void updateDB(Zekr zekr, int position) throws Exception {
        List<Zekr> list = Hawk.get(Zekr.DB_ZEKRS, new LinkedList<Zekr>());
        if (position >= 0) {
            list.set(position, zekr);
        } else {
            list.add(zekr);
        }
        Hawk.put(Zekr.DB_ZEKRS, list);
    }
}
