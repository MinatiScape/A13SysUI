package com.android.systemui.tuner;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.fragments.FragmentService;
import java.util.Objects;
/* loaded from: classes.dex */
public class TunerActivity extends Activity implements PreferenceFragment.OnPreferenceStartFragmentCallback, PreferenceFragment.OnPreferenceStartScreenCallback {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final DemoModeController mDemoModeController;
    public final TunerService mTunerService;

    /* loaded from: classes.dex */
    public static class SubSettingsFragment extends PreferenceFragment {
        public PreferenceScreen mParentScreen;

        @Override // androidx.preference.PreferenceFragment
        public final void onCreatePreferences(String str) {
            this.mParentScreen = (PreferenceScreen) ((PreferenceFragment) getTargetFragment()).getPreferenceScreen().findPreference(str);
            PreferenceManager preferenceManager = this.mPreferenceManager;
            Objects.requireNonNull(preferenceManager);
            PreferenceScreen preferenceScreen = new PreferenceScreen(preferenceManager.mContext, null);
            preferenceScreen.onAttachedToHierarchy(preferenceManager);
            setPreferenceScreen(preferenceScreen);
            while (this.mParentScreen.getPreferenceCount() > 0) {
                Preference preference = this.mParentScreen.getPreference(0);
                this.mParentScreen.removePreference(preference);
                preferenceScreen.addPreference(preference);
            }
        }

        @Override // android.app.Fragment
        public final void onDestroy() {
            super.onDestroy();
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            while (preferenceScreen.getPreferenceCount() > 0) {
                Preference preference = preferenceScreen.getPreference(0);
                preferenceScreen.removePreference(preference);
                this.mParentScreen.addPreference(preference);
            }
        }
    }

    public TunerActivity(DemoModeController demoModeController, TunerService tunerService) {
        this.mDemoModeController = demoModeController;
        this.mTunerService = tunerService;
    }

    @Override // android.app.Activity
    public final void onBackPressed() {
        if (!getFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }

    @Override // android.app.Activity
    public final void onCreate(Bundle bundle) {
        Fragment fragment;
        super.onCreate(bundle);
        setTheme(2132018048);
        getWindow().addFlags(Integer.MIN_VALUE);
        boolean z = true;
        requestWindowFeature(1);
        setContentView(2131624625);
        Toolbar toolbar = (Toolbar) findViewById(2131427412);
        if (toolbar != null) {
            setActionBar(toolbar);
        }
        if (getFragmentManager().findFragmentByTag("tuner") == null) {
            String action = getIntent().getAction();
            if (action == null || !action.equals("com.android.settings.action.DEMO_MODE")) {
                z = false;
            }
            if (z) {
                fragment = new DemoModeFragment(this.mDemoModeController);
            } else {
                fragment = new TunerFragment(this.mTunerService);
            }
            getFragmentManager().beginTransaction().replace(2131427742, fragment, "tuner").commit();
        }
    }

    @Override // android.app.Activity
    public final void onDestroy() {
        super.onDestroy();
        TunerActivity$$ExternalSyntheticLambda0 tunerActivity$$ExternalSyntheticLambda0 = TunerActivity$$ExternalSyntheticLambda0.INSTANCE;
        Dependency dependency = Dependency.sDependency;
        Objects.requireNonNull(dependency);
        Object remove = dependency.mDependencies.remove(FragmentService.class);
        if (remove instanceof Dumpable) {
            dependency.mDumpManager.unregisterDumpable(remove.getClass().getName());
        }
        if (remove != null) {
            tunerActivity$$ExternalSyntheticLambda0.accept(remove);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public final boolean onMenuItemSelected(int i, MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onMenuItemSelected(i, menuItem);
        }
        onBackPressed();
        return true;
    }

    @Override // androidx.preference.PreferenceFragment.OnPreferenceStartFragmentCallback
    public final boolean onPreferenceStartFragment(Preference preference) {
        try {
            Objects.requireNonNull(preference);
            Fragment fragment = (Fragment) Class.forName(preference.mFragment).newInstance();
            Bundle bundle = new Bundle(1);
            bundle.putString("androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT", preference.mKey);
            fragment.setArguments(bundle);
            FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
            setTitle(preference.mTitle);
            beginTransaction.replace(2131427742, fragment);
            beginTransaction.addToBackStack("PreferenceFragment");
            beginTransaction.commit();
            return true;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            Log.d("TunerActivity", "Problem launching fragment", e);
            return false;
        }
    }

    @Override // androidx.preference.PreferenceFragment.OnPreferenceStartScreenCallback
    public final void onPreferenceStartScreen(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen) {
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        SubSettingsFragment subSettingsFragment = new SubSettingsFragment();
        Bundle bundle = new Bundle(1);
        Objects.requireNonNull(preferenceScreen);
        bundle.putString("androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT", preferenceScreen.mKey);
        subSettingsFragment.setArguments(bundle);
        subSettingsFragment.setTargetFragment(preferenceFragment, 0);
        beginTransaction.replace(2131427742, subSettingsFragment);
        beginTransaction.addToBackStack("PreferenceFragment");
        beginTransaction.commit();
    }
}
