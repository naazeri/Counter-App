package ir.nazery.zekrshomar;

import android.app.Application;
import android.content.Context;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/***
 * Created by reza on 95/10/20.
 ***/
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
//                .setDefaultFontPath("font/b_yekan.ttf")
//                .setFontAttrId(R.attr.fontPath)
//                .build()
//        );
    }
}
