package ir.nazery.zekrshomar;

import ir.nazery.zekrshomar.lib.Remember;

/***
 * Created by reza on 95/10/20.
 ***/
public class App extends com.activeandroid.app.Application {


    @Override
    public void onCreate() {
        super.onCreate();

        Remember.init(this, getPackageName());
//        try {
//            new DataManager().initDB(this);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(this, R.string.errorInDB, Toast.LENGTH_LONG).show();
//        }

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("font/b_yekan.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
    }
}
