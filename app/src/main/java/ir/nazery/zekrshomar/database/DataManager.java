package ir.nazery.zekrshomar.database;

import java.util.List;

import ir.nazery.zekrshomar.model.Zekr;

/***
 * Created by REZA on 1/25/2016
 ***/
public class DataManager {
//    public void initDB(Context context) throws Exception {
//        Hawk.init(context)
//                .setEncryption(new NoEncryption())
//                .build();
//    }

    public List<Zekr> getZekrs() throws Exception {
//        return Hawk.get(Constant.DB_ZEKRS, new LinkedList<Zekr>());
        return Zekr.getAll();
    }

    public void setZekrs(List<Zekr> list) throws Exception {
//        Hawk.put(Constant.DB_ZEKRS, list);
    }


    public void updateDB(Zekr zekr) {
//        List<Zekr> list = Hawk.get(Constant.DB_ZEKRS, new LinkedList<Zekr>());
//        if (zekr.getPosition() >= 0) {
//            list.set(zekr.getPosition(), zekr);
//        } else {
//            list.add(zekr);
//        }
//        Hawk.put(Constant.DB_ZEKRS, list);
    }
}
