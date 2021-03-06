package com.example.android.environmentalnews;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            //add settings menu layout
            addPreferencesFromResource(R.xml.settings_main);


            Preference sectionName = findPreference(getString(R.string.settings_section_key));
            bindPreferenceSummaryToValue(sectionName);

            Preference searchPhrase = findPreference(getString(R.string.settings_search_key));
            bindPreferenceSummaryToValue(searchPhrase);

            Preference orderBy = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderBy);


        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String preferenceValue = value.toString();
            //Shows the chosen preference value summary
            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(preferenceValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(preferenceValue);
            }
            return true;
        }

        //Checks device for preferences stored in SharedPreferences
        // and if set already shows them in preference summary
        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences =
                    PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }
    }
}
