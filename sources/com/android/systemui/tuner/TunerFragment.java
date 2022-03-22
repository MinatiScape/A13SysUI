package com.android.systemui.tuner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import com.android.internal.logging.MetricsLogger;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
/* loaded from: classes.dex */
public class TunerFragment extends PreferenceFragment {
    public static final String[] DEBUG_ONLY = {"nav_bar", "lockscreen", "picture_in_picture"};
    public final TunerService mTunerService;

    /* loaded from: classes.dex */
    public static class TunerWarningFragment extends DialogFragment {
        @Override // android.app.DialogFragment
        public final Dialog onCreateDialog(Bundle bundle) {
            return new AlertDialog.Builder(getContext()).setTitle(2131953430).setMessage(2131953429).setPositiveButton(2131952410, new DialogInterface.OnClickListener() { // from class: com.android.systemui.tuner.TunerFragment.TunerWarningFragment.1
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    Settings.Secure.putInt(TunerWarningFragment.this.getContext().getContentResolver(), "seen_tuner_warning", 1);
                }
            }).show();
        }
    }

    @Override // android.app.Fragment
    public final void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.add(0, 2, 0, 2131953153);
    }

    @SuppressLint({"ValidFragment"})
    public TunerFragment(TunerService tunerService) {
        this.mTunerService = tunerService;
    }

    @Override // android.app.Fragment
    public final void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override // androidx.preference.PreferenceFragment, android.app.Fragment
    public final void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override // androidx.preference.PreferenceFragment
    public final void onCreatePreferences(String str) {
        addPreferencesFromResource(2132213781);
        if (!getContext().getSharedPreferences("plugin_prefs", 0).getBoolean("plugins", false)) {
            getPreferenceScreen().removePreference(findPreference("plugins"));
        }
        if (!new AmbientDisplayConfiguration(getContext()).alwaysOnAvailable()) {
            getPreferenceScreen().removePreference(findPreference("doze"));
        }
        if (!Build.IS_DEBUGGABLE) {
            int i = 0;
            while (true) {
                String[] strArr = DEBUG_ONLY;
                if (i >= 3) {
                    break;
                }
                Preference findPreference = findPreference(strArr[i]);
                if (findPreference != null) {
                    getPreferenceScreen().removePreference(findPreference);
                }
                i++;
            }
        }
        if (Settings.Secure.getInt(getContext().getContentResolver(), "seen_tuner_warning", 0) == 0 && getFragmentManager().findFragmentByTag("tuner_warning") == null) {
            new TunerWarningFragment().show(getFragmentManager(), "tuner_warning");
        }
    }

    @Override // android.app.Fragment
    public final boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 2) {
            this.mTunerService.showResetRequest(new CarrierTextManager$$ExternalSyntheticLambda0(this, 7));
            return true;
        } else if (itemId != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            getActivity().finish();
            return true;
        }
    }

    @Override // android.app.Fragment
    public final void onPause() {
        super.onPause();
        MetricsLogger.visibility(getContext(), 227, false);
    }

    @Override // android.app.Fragment
    public final void onResume() {
        super.onResume();
        getActivity().setTitle(2131953349);
        MetricsLogger.visibility(getContext(), 227, true);
    }
}
