package ir.nazery.zekrshomar;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import ir.nazery.zekrshomar.database.DataManager;
import ir.nazery.zekrshomar.fragments.CounterFragment;
import ir.nazery.zekrshomar.fragments.SettingFragment;
import ir.nazery.zekrshomar.fragments.ZekrListFragment;
import ir.nazery.zekrshomar.setting.SettingsActivity;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ZekrListFragment.OnClickListener {

    private String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initDB();
        loadFirstFragment();
    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getFragmentManager().beginTransaction()
                .add(R.id.mainContainer, new ZekrListFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getFragmentManager().getBackStackEntryCount() == 0) {
                super.onBackPressed();
            } else {
                getFragmentManager().popBackStack();
            }
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
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {
            case R.id.navView_zekrs:
                emptyFragmentStack();
                getFragmentManager().beginTransaction()
                        .replace(R.id.mainContainer, new ZekrListFragment())
                        .commit();
                break;
            case R.id.nav_gallery:

                break;
            case R.id.nav_slideshow:
                break;
            case R.id.navView_settings:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void emptyFragmentStack() {
//        int count = getFragmentManager().getBackStackEntryCount();
//        Log.d(TAG, "stack count: " + count);
//
//        boolean condition = getFragmentManager().popBackStackImmediate();
//        count = getFragmentManager().getBackStackEntryCount();
//        Log.d(TAG, String.format("stack count: %d with condition: %s", count, condition));
//
//        condition = getFragmentManager().popBackStackImmediate();
//        count = getFragmentManager().getBackStackEntryCount();
//        Log.d(TAG, String.format("stack count: %d with condition: %s", count, condition));

        while (getFragmentManager().popBackStackImmediate()) {
            Log.d(TAG, "pop stack");
        }
    }

    @Override
    public void onItemSelected(int position) {
        getFragmentManager().beginTransaction()
                .replace(R.id.mainContainer, CounterFragment.newInstance(position))
                .addToBackStack(CounterFragment.TAG)
                .commit();

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
