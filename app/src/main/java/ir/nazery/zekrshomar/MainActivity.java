package ir.nazery.zekrshomar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;
import com.squareup.otto.ThreadEnforcer;

import ir.nazery.zekrshomar.database.DataManager;
import ir.nazery.zekrshomar.database.Zekr;
import ir.nazery.zekrshomar.fragments.CounterFragment;
import ir.nazery.zekrshomar.fragments.ZekrListFragment;
import ir.nazery.zekrshomar.setting.SettingsActivity;

public class MainActivity extends AppCompatActivity implements
        ZekrListFragment.OnZekrClickListener {

    private final String TAG = "aksjdfoiuqwer";
    private final String COUNTER_FRAGMENT = "cf";
    public static Bus bus = new Bus(ThreadEnforcer.MAIN);
    private final int ADD = 1;
    private final int MINUS = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initDB();
        loadFirstFragment();

        bus.register(this);
    }

    @Subscribe
    public void zekrChange(Zekr zekr) {
        Log.d(TAG, String.format("event info: %s  %s", zekr.getZekrName(), zekr.getZekrCountAsString()));
        Toast.makeText(MainActivity.this, "info: " + zekr.getZekrName(), Toast.LENGTH_SHORT).show();
    }

    private void initDB() {
        try {
            new DataManager().initDB(this);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, R.string.errorInDB, Toast.LENGTH_LONG).show();
        }
    }

    private void loadFirstFragment() {
        ZekrListFragment fragment = new ZekrListFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, fragment)
                .commit();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        try {
            int action = event.getAction();
            int keyCode = event.getKeyCode();

            if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
//                Log.d(TAG, "volume up");
                if (action == KeyEvent.ACTION_DOWN) {
//                    CounterFragment fragment = (CounterFragment) getSupportFragmentManager().findFragmentByTag(COUNTER_FRAGMENT);
//                    fragment.changeValue(ADD);
                    bus.post(ADD);
                }
                return true;
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
//                Log.d(TAG, "volume down");
                if (action == KeyEvent.ACTION_DOWN) {
//                    CounterFragment fragment = (CounterFragment) getSupportFragmentManager().findFragmentByTag(COUNTER_FRAGMENT);
//                    fragment.changeValue(MINUS);
                    bus.post(MINUS);
                }
                return true;
            }
        } catch (Exception ignored) {
        }

        return super.dispatchKeyEvent(event);
    }

//    @Produce
//    Integer add() {
//        return ADD;
//    }
//
//    @Produce
//    Integer minus() {
//        return MINUS;
//    }

    @Override
    public void onZekrSelected(int position) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, CounterFragment.newInstance(position), COUNTER_FRAGMENT)
                .addToBackStack(null)
                .commit();

    }

//    private void emptyFragmentStack() {
//        while (getSupportFragmentManager().popBackStackImmediate()) {
//        }
//    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
            case R.id.action_rate:
                try {
                    showBazarRate();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "خطا", Toast.LENGTH_SHORT).show();
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showMyketRate() throws Exception {
        // myket rate
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(String.format("myket://comment/#Intent;scheme=comment;package=%s;end", getPackageName())));
        startActivity(intent);
    }

    private void showBazarRate() throws Exception {
        // bazar rate
        Intent intent = new Intent(Intent.ACTION_EDIT);
        intent.setData(Uri.parse("bazaar://details?id=" + getPackageName()));
        intent.setPackage("com.farsitel.bazaar");
        startActivity(intent);
    }

//    @Override
//    public void onZekrChange(Zekr zekr, int position) {
//        try {
//            DataManager dataManager = new DataManager();
//            List<Zekr> list = dataManager.getZekrs(this);
//            list.set(position, zekr);
//            dataManager.setZekrs(this, list);
//        } catch (Exception e) {
//            e.printStackTrace();
//            Snackbar.make(getCurrentFocus(), R.string.errorInDB, Snackbar.LENGTH_LONG).show();
//        }
//    }
}
