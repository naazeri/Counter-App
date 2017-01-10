package ir.nazery.zekrshomar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;
import ir.nazeri.dataegg.DataEggActivity;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class AboutusActivity extends AppCompatActivity {

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aboutus);

        try {
            setTitle("درباره ما");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showAsreZohur(View view) {
        Uri uri = Uri.parse("http://avvalmarket.ir/frontpage/apps/ir.nazery.asrezohur");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(base));
    }

    public void titleClick(View view) {
        if (++i > 10) {
            startActivity(new Intent(this, DataEggActivity.class));
            i = 0;
        }
    }

    public void showTaeftar(View view) {
        Uri uri = Uri.parse("http://avvalmarket.ir/frontpage/apps/ir.nazery.timetilleftar");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
