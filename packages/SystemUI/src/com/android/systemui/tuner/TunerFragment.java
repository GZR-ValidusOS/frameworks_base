/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.systemui.tuner;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v14.preference.PreferenceFragment;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.Preference.OnPreferenceChangeListener;
import android.support.v7.preference.PreferenceScreen;

import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.MetricsProto.MetricsEvent;
import com.android.systemui.R;

public class TunerFragment extends PreferenceFragment 
	implements OnPreferenceChangeListener {

    private static final String TAG = "TunerFragment";

    private static final String STATUS_BAR_VALIDUS_LOGO = "status_bar_validus_logo";
    private static final String STATUS_BAR_VALIDUS_LOGO_STYLE = "status_bar_validus_logo_style";

    private SwitchPreference mValidusLogo;
    private ListPreference mValidusLogoStyle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PreferenceScreen prefSet = getPreferenceScreen();

        final ContentResolver resolver = getActivity().getContentResolver();

        mValidusLogo = (SwitchPreference) findPreference(STATUS_BAR_VALIDUS_LOGO);
        mValidusLogo.setChecked((Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_VALIDUS_LOGO, 0) == 1));

        mValidusLogoStyle = (ListPreference) findPreference(STATUS_BAR_VALIDUS_LOGO_STYLE);
        mValidusLogoStyle.setOnPreferenceChangeListener(this);
        mValidusLogoStyle.setValue(Integer.toString(Settings.System.getInt(resolver,
                Settings.System.STATUS_BAR_VALIDUS_LOGO_STYLE, 0)));
        mValidusLogoStyle.setSummary(mValidusLogoStyle.getEntry());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getActivity().getActionBar() != null) {
            getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.tuner_prefs);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R.string.systemui_tuner_statusbar_title);

        MetricsLogger.visibility(getContext(), MetricsEvent.TUNER, true);
    }

    @Override
    public void onPause() {
        super.onPause();

        MetricsLogger.visibility(getContext(), MetricsEvent.TUNER, false);
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
	ContentResolver resolver = getActivity().getContentResolver();

	if (preference == mValidusLogoStyle) {
	    int val = Integer.parseInt((String) newValue);
	    int index = mValidusLogoStyle.findIndexOfValue((String) newValue);
	    Settings.System.putInt(resolver,
		    Settings.System.STATUS_BAR_VALIDUS_LOGO_STYLE, val);
	    mValidusLogoStyle.setSummary(mValidusLogoStyle.getEntries()[index]);
	    return true;
	}
	return false;
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if  (preference == mValidusLogo) {
            boolean checked = ((SwitchPreference)preference).isChecked();
            Settings.System.putInt(getActivity().getContentResolver(),
                    Settings.System.STATUS_BAR_VALIDUS_LOGO, checked ? 1:0);
            return true;
          }
        return super.onPreferenceTreeClick(preference);
    }
}
