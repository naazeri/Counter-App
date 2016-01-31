package ir.nazery.zekrshomar.database;

import android.content.Context;

import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;
import com.orhanobut.hawk.LogLevel;

import java.util.ArrayList;
import java.util.List;

/*** Created by REZA on 1/25/2016 ***/
public class DataManager {
    public void initDB(Context context) throws Exception {
        Hawk.init(context)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .setPassword("myPASS")
                .setStorage(HawkBuilder.newSharedPrefStorage(context))
                .setLogLevel(LogLevel.NONE)
                .build();
    }

    public List<Zekr> getZekrs(Context context) throws Exception {
//        initDB(context);
        return Hawk.get(Zekr.DB_ZEKRS, new ArrayList<Zekr>());
        //        for (int i = 0; i < 100; ++i) {
//            int day = i%7;
//            switch (day) {
//                case 0:
//                    list.add( new Zekr("شنبه", i+1) );
//                    break;
//                case 6:
//                    list.add( new Zekr("جمعه", i+1) );
//                    break;
//                default:
//                    list.add( new Zekr(String.format("%d شنبه", day), i+1) );
//            }
//        }
    }

    public void setZekrs(Context context, List<Zekr> list) throws Exception {
//        initDB(context);
        Hawk.put(Zekr.DB_ZEKRS, list);
    }

    public void updateDB(Context context, Zekr zekr, int position) throws Exception {
//        initDB(context);
        List<Zekr> list = Hawk.get(Zekr.DB_ZEKRS, new ArrayList<Zekr>());
        if (position >= 0) {
            list.set(position, zekr);
        } else {
            list.add(zekr);
        }
        Hawk.put(Zekr.DB_ZEKRS, list);
    }
}
