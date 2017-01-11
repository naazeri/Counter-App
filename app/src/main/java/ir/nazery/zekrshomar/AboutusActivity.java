package ir.nazery.zekrshomar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ir.nazeri.dataegg.DataEggActivity;
import ir.nazery.zekrshomar.untils.Constant;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutusActivity extends AppCompatActivity {

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        switch (Constant.MARKET_TARGET) {
            case bazar:
                setContentView(R.layout.activity_aboutus_bazar);
                break;
            case myket:
                setContentView(R.layout.activity_aboutus_myket);
                break;
            case avalmarket:
                setContentView(R.layout.activity_aboutus_avvalmarket);
                break;
        }

        try {
            setTitle("درباره ما");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLink(View view) {
        try {
            Uri uri = Uri.parse(view.getContentDescription().toString());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void titleClick(View view) {
        if (++i > 10) {
            startActivity(new Intent(this, DataEggActivity.class));
            i = 0;
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }
}
