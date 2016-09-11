package ir.nazery.zekrshomar.fragments;

import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;
import com.squareup.otto.Subscribe;

import in.championswimmer.sfg.lib.SimpleFingerGestures;
import ir.nazery.zekrshomar.MainActivity;
import ir.nazery.zekrshomar.R;
import ir.nazery.zekrshomar.database.DataManager;
import ir.nazery.zekrshomar.database.Zekr;

public class CounterFragment extends Fragment implements
        SimpleFingerGestures.OnFingerGestureListener {

    private EditText zekrCount_textView;
    public static String TAG = "aksjdfoiuqwer";
    public Zekr zekr;
    private int actionLeft, actionRight, actionUp, actionDown;
    private DataManager dataManager;
    private EditText zekrName_textView;
    private MediaPlayer mediaPlayer;

    public CounterFragment() {
    }

    public static CounterFragment newInstance(int position) {
        CounterFragment fragment = new CounterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Zekr.ZEKR_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        SimpleFingerGestures gestures = new SimpleFingerGestures();
        gestures.setDebug(false);
        gestures.setConsumeTouchEvents(true);
        gestures.setOnFingerGestureListener(this);
        view.setOnTouchListener(gestures);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dataManager = new DataManager();
        int position = getArguments().getInt(Zekr.ZEKR_POSITION, -1);

        if (position > -1) {
            try {
                zekr = dataManager.getZekrs().get(position);
            } catch (Exception e) {
                e.printStackTrace();
                if (getView() != null) {
                    Snackbar.make(getView(), R.string.errorInDB, Snackbar.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getActivity(), R.string.errorInDB, Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            zekr = new Zekr("", 0);
        }

        zekrName_textView = (EditText) view.findViewById(R.id.counter_zekrName_textView);
        zekrCount_textView = (EditText) view.findViewById(R.id.counter_zekrCount_textView);

        zekrName_textView.setText(zekr.getZekrName());
        zekrCount_textView.setText(zekr.getZekrCountAsString());

        Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "font/b_yekan.ttf");
        zekrName_textView.setTypeface(font);
    }

    private void setActions() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        actionLeft = Integer.parseInt(preferences.getString(getString(R.string.settings_swipe_key_left), getString(R.string.settings_swipe_defaultVal_left)));
        actionRight = Integer.parseInt(preferences.getString(getString(R.string.settings_swipe_key_right), getString(R.string.settings_swipe_defaultVal_right)));
        actionUp = Integer.parseInt(preferences.getString(getString(R.string.settings_swipe_key_up), getString(R.string.settings_swipe_defaultVal_up)));
        actionDown = Integer.parseInt(preferences.getString(getString(R.string.settings_swipe_key_down), getString(R.string.settings_swipe_defaultVal_down)));
//        Log.d(TAG, "setActions: " + actionLeft + "  " + actionRight + "  " + actionUp + "  " + actionDown);
    }

    private void updateDisplay(Zekr zekr) {
        zekrCount_textView.setText(zekr.getZekrCountAsString());
    }

    public boolean changeValue(int n) {
//        int zekrCount = zekr.getZekrCountAsInteger() + n;
        try {
            int zekrCount = Integer.parseInt(zekrCount_textView.getText().toString()) + n;
            if (zekrCount <= 0) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music);
                }

                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music);
                }
                mediaPlayer.start();
            }

            if (zekrCount < 0) {
                return false;
            }
            zekr.setZekrCount(zekrCount);
            updateDisplay(zekr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return true;
    }

    @Override
    public boolean onSwipeUp(int i, long l, double v) {
        changeValue(actionUp);
        return false;
    }

    @Override
    public boolean onSwipeDown(int i, long l, double v) {
        changeValue(actionDown);
        return false;
    }

    @Override
    public boolean onSwipeLeft(int i, long l, double v) {
        changeValue(actionLeft);
        return false;
    }

    @Override
    public boolean onSwipeRight(int i, long l, double v) {
        changeValue(actionRight);
        return false;
    }

    @Override
    public boolean onPinch(int i, long l, double v) {
        return false;
    }

    @Override
    public boolean onUnpinch(int i, long l, double v) {
        return false;
    }

    @Override
    public boolean onDoubleTap(int i) {
        return false;
    }

    @Subscribe
    public void volumeButtonClick(Integer value) {
//        Log.d(TAG, "volumeButtonClick: " + value);
        changeValue(value);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActions();
        MainActivity.bus.register(this);
    }

    @Override
    public void onPause() {
        try {
            MainActivity.bus.post(zekr);

            int position = getArguments().getInt(Zekr.ZEKR_POSITION);
            zekr.setZekrName(zekrName_textView.getText().toString());
            zekr.setZekrCount(Integer.parseInt(zekrCount_textView.getText().toString()));
            dataManager.updateDB(zekr, position);

        } catch (NumberFormatException e1) {
            Toast.makeText(getActivity(), "تعداد ذکر خالی است", Toast.LENGTH_LONG).show();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(TAG, "onPause: Error", e2);
            Toast.makeText(getActivity(), R.string.errorInDB, Toast.LENGTH_LONG).show();
        }

        MainActivity.bus.unregister(this);
        super.onPause();
    }
}
