package ir.nazery.zekrshomar;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import ir.nazery.zekrshomar.database.DataManager;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/***
 * Created by reza on 95/10/20.
 ***/
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        try {
            new DataManager().initDB(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.errorInDB, Toast.LENGTH_LONG).show();
        }
//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("font/b_yekan.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
    }
}
