package ir.nazery.zekrshomar.fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import in.championswimmer.sfg.lib.SimpleFingerGestures;
import ir.nazery.zekrshomar.R;
import ir.nazery.zekrshomar.database.DataManager;
import ir.nazery.zekrshomar.database.Zekr;

public class CounterFragment extends Fragment implements SimpleFingerGestures.OnFingerGestureListener {

    private EditText zekrCount_textView;
    //    private ZekrChangeListener listener;
    public static String TAG = "CounterFragment";
    private Zekr zekr;
    private int actionLeft, actionRight, actionUp, actionDown;

    public CounterFragment() {
    }

//    public interface ZekrChangeListener {
//        void onZekrChange(Zekr zekr, int position);
//    }

    public static CounterFragment newInstance(int position) {
        CounterFragment fragment = new CounterFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Zekr.ZEKR_POSITION, position);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int position = getArguments().getInt(Zekr.ZEKR_POSITION, -1);
        if (position >= 0) {
            try {
                zekr = new DataManager().getZekrs(getActivity()).get(position);
            } catch (Exception e) {
                e.printStackTrace();
                Snackbar.make(getView(), R.string.errorInDB, Snackbar.LENGTH_LONG).show();
            }
        } else {
            zekr = new Zekr("", 0);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_counter, container, false);

        SimpleFingerGestures gestures = new SimpleFingerGestures();
        gestures.setDebug(true);
        gestures.setConsumeTouchEvents(true);

        gestures.setOnFingerGestureListener(this);

        view.setOnTouchListener(gestures);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EditText zekrName_textView = (EditText) view.findViewById(R.id.counter_zekrName_textView);
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

    private void updateDisplay() {
        zekrCount_textView.setText(zekr.getZekrCountAsString());
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

//    private void plusPlus() {
//        zekr.setZekrCount(zekr.getZekrCountAsInteger() + 1);
//    }

    private boolean changeValue(int n) {
        int zekrCount = zekr.getZekrCountAsInteger() + (n);
        if (zekrCount < 0) {
            return false;
        }
        zekr.setZekrCount(zekrCount);
        updateDisplay();
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        setActions();
    }

    @Override
    public void onPause() {
        try {
            int position = getArguments().getInt(Zekr.ZEKR_POSITION);
            EditText zekrName_textView = (EditText) getView().findViewById(R.id.counter_zekrName_textView);
            zekr.setZekrName(zekrName_textView.getText().toString());
            Context context = getActivity();
            new DataManager().updateDB(context, zekr, position);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "onPause: Error", e);
            Toast.makeText(getActivity(), R.string.errorInDB, Toast.LENGTH_LONG).show();
        }
        super.onPause();
    }
}
