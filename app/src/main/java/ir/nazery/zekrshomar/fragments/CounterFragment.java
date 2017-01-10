package ir.nazery.zekrshomar.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;
import in.championswimmer.sfg.lib.SimpleFingerGestures;
import ir.nazery.zekrshomar.R;
import ir.nazery.zekrshomar.database.DataManager;
import ir.nazery.zekrshomar.database.Zekr;

public class CounterFragment extends Fragment implements
        SimpleFingerGestures.OnFingerGestureListener {

    public static String TAG = "aksjdfoiuqwer";
    public Zekr zekr;
    private int actionLeft, actionRight, actionUp, actionDown;
    private DataManager dataManager;
    private EditText zekrCount_editText, zekrName_editText;
    private MediaPlayer mediaPlayer;
    private Integer index = 0;

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

        zekrName_editText = (EditText) view.findViewById(R.id.counter_zekrName_textView);
        zekrCount_editText = (EditText) view.findViewById(R.id.counter_zekrCount_textView);
        View emptyView = view.findViewById(R.id.emptyView);

        zekrName_editText.setText(zekr.getZekrName());
        zekrCount_editText.setText(zekr.getZekrCountAsString());

        View[] viewList = {zekrName_editText, zekrCount_editText, emptyView};
        String[] stringList = {"نام ذکر را اینجا وارد کنید", "تعداد ذکر را اینجا وارد کنید", "از کلیدهای ولوم جهت افزایش یا کاهش ذکر استفاده کنید یا برروی این بخش دست خود را از چپ به راست یا برعکس بکشید"};
        showHelp(viewList, stringList);
    }

    private void showHelp(final View[] views, final String[] texts) {
        if (index < views.length) {
            new MaterialIntroView.Builder(getActivity())
                    .enableDotAnimation(true)
                    .enableIcon(true)
                    .setFocusGravity(FocusGravity.CENTER)
                    .setFocusType(Focus.MINIMUM)
                    .setDelayMillis(100)
                    .enableFadeAnimation(true)
                    .performClick(false)
                    .setInfoText(texts[index])
                    .setTarget(views[index])
                    .setUsageId(index.toString()) //THIS SHOULD BE UNIQUE ID
                    .setListener(new MaterialIntroListener() {
                        @Override
                        public void onUserClicked(String s) {
                            showHelp(views, texts);
                        }
                    })
                    .show();
            index++;
        }
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
        zekrCount_editText.setText(zekr.getZekrCountAsString());
    }

    public boolean changeValue(int n) {
//        int zekrCount = zekr.getZekrCountAsInteger() + n;
        try {
            String s = zekrCount_editText.getText().toString();
            if (s.isEmpty()) {
                s = "0";
            }
            int zekrCount = Integer.parseInt(s) + n;
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

                vibrate();
            }

            if (zekrCount < 0) {
                zekr.setZekrCount(0);
                updateDisplay(zekr);
                return false;
            }

            zekr.setZekrCount(zekrCount);
            updateDisplay(zekr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return true;
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void volumeButtonClick(Integer value) {
//        Log.d(TAG, "volumeButtonClick: " + value);
        changeValue(value);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        setActions();
    }

    @Override
    public void onPause() {
        try {
            if (zekrName_editText.getText().toString().isEmpty() || zekrCount_editText.getText().toString().isEmpty()) {
                super.onPause();
                return;
            }

            int position = getArguments().getInt(Zekr.ZEKR_POSITION);
            zekr.setZekrName(zekrName_editText.getText().toString());
            zekr.setZekrCount(Integer.parseInt(zekrCount_editText.getText().toString()));
            dataManager.updateDB(zekr, position);

        } catch (NumberFormatException e1) {
            Toast.makeText(getActivity(), "تعداد ذکر خالی است", Toast.LENGTH_LONG).show();
        } catch (Exception e2) {
            e2.printStackTrace();
            Log.e(TAG, "onPause: Error", e2);
            Toast.makeText(getActivity(), R.string.errorInDB, Toast.LENGTH_LONG).show();
        }

        super.onPause();
    }
}
