package ir.nazery.zekrshomar.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import ir.nazery.zekrshomar.R;

public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
