package ir.nazery.zekrshomar.fragments;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
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
import ir.nazery.zekrshomar.model.Zekr;

public class CounterFragment extends Fragment implements SimpleFingerGestures.OnFingerGestureListener {

    private static String TAG = "aaaa";
    private static Zekr zekr;
    private int actionLeft = 1;
    private int actionRight = -1;
    private int actionUp = 0;
    private int actionDown = 0;
    private EditText zekrCount_editText, zekrName_editText;
    private MediaPlayer mediaPlayer;
    private Integer index = 0;

    public CounterFragment() {
    }

    public static CounterFragment newInstance(Zekr z) {
        CounterFragment fragment = new CounterFragment();
//        Bundle bundle = zekr.toBundle();
//        fragment.setArguments(bundle);
        zekr = z;
        return fragment;
    }

//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            zekr = new Zekr(bundle);
//        }
//    }

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

        zekrName_editText = (EditText) view.findViewById(R.id.counter_zekrName_textView);
        zekrCount_editText = (EditText) view.findViewById(R.id.counter_zekrCount_textView);
        View emptyView = view.findViewById(R.id.emptyView);

        zekrName_editText.setText(zekr.getZekrName());
        zekrCount_editText.setText(zekr.getZekrCountAsString());

        View[] viewList = {zekrName_editText, zekrCount_editText, emptyView};
        String[] stringList = {"نام ذکر را اینجا وارد کنید", "تعداد ذکر را اینجا وارد کنید", "از کلیدهای ولوم گوشی جهت افزایش یا کاهش تعداد ذکر استفاده کنید یا برروی این بخش دست خود را به چپ یا راست بکشید"};
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

//    private void setActions() {
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
//        actionLeft = Integer.parseInt(preferences.getString(getString(R.string.settings_swipe_key_left), getString(R.string.settings_swipe_defaultVal_left)));
//        actionRight = Integer.parseInt(preferences.getString(getString(R.string.settings_swipe_key_right), getString(R.string.settings_swipe_defaultVal_right)));
//        actionUp = Integer.parseInt(preferences.getString(getString(R.string.settings_swipe_key_up), getString(R.string.settings_swipe_defaultVal_up)));
//        actionDown = Integer.parseInt(preferences.getString(getString(R.string.settings_swipe_key_down), getString(R.string.settings_swipe_defaultVal_down)));
////        Log.d(TAG, "setActions: " + actionLeft + "  " + actionRight + "  " + actionUp + "  " + actionDown);
//    }

    private void updateUI(Zekr zekr) {
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
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }

                mediaPlayer = MediaPlayer.create(getActivity(), R.raw.music);
                mediaPlayer.start();
                vibrate();

                zekrCount = 0;
            }

            zekr.setZekrCount(zekrCount);
            updateUI(zekr);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    private void vibrate() {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(200);
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
    public void onDestroyView() {
        super.onDestroyView();

        try {
            zekr.setZekrName(zekrName_editText.getText().toString());

            if (zekrCount_editText.getText().toString().isEmpty()) {
                zekr.setZekrCount(0);
            } else {
                zekr.setZekrCount(Integer.parseInt(zekrCount_editText.getText().toString()));
            }

            zekr.save();
//            EventBus.getDefault().post(zekr);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), R.string.errorInDB, Toast.LENGTH_LONG).show();
        }
    }
}
