package com.android.systemui.tuner;

import android.content.Context;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Process;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceGroupAdapter;
import androidx.preference.PreferenceManager;
import androidx.preference.PreferenceScreen;
import androidx.preference.PreferenceViewHolder;
import com.android.systemui.Dependency;
import com.android.systemui.doze.DozeTriggers$$ExternalSyntheticLambda4;
import com.android.systemui.tuner.ShortcutParser;
import com.android.systemui.tuner.ShortcutPicker;
import com.android.systemui.tuner.TunerService;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ShortcutPicker extends PreferenceFragment implements TunerService.Tunable {
    public static final /* synthetic */ int $r8$clinit = 0;
    public String mKey;
    public SelectablePreference mNonePreference;
    public final ArrayList<SelectablePreference> mSelectablePreferences = new ArrayList<>();
    public TunerService mTunerService;

    /* loaded from: classes.dex */
    public static class AppPreference extends SelectablePreference {
        public boolean mBinding;
        public final LauncherActivityInfo mInfo;

        @Override // androidx.preference.CheckBoxPreference, androidx.preference.Preference
        public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
            this.mBinding = true;
            if (getIcon() == null) {
                setIcon(this.mInfo.getBadgedIcon(this.mContext.getResources().getConfiguration().densityDpi));
            }
            this.mBinding = false;
            super.onBindViewHolder(preferenceViewHolder);
        }

        @Override // androidx.preference.Preference
        public final void notifyChanged() {
            if (!this.mBinding) {
                super.notifyChanged();
            }
        }

        @Override // com.android.systemui.tuner.SelectablePreference, androidx.preference.Preference
        public final String toString() {
            return this.mInfo.getComponentName().flattenToString();
        }

        public AppPreference(Context context, LauncherActivityInfo launcherActivityInfo) {
            super(context);
            this.mInfo = launcherActivityInfo;
            setTitle(context.getString(2131953417, launcherActivityInfo.getLabel()));
            setSummary(context.getString(2131953412, launcherActivityInfo.getLabel()));
        }
    }

    /* loaded from: classes.dex */
    public static class ShortcutPreference extends SelectablePreference {
        public boolean mBinding;
        public final ShortcutParser.Shortcut mShortcut;

        @Override // androidx.preference.CheckBoxPreference, androidx.preference.Preference
        public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
            this.mBinding = true;
            if (getIcon() == null) {
                setIcon(this.mShortcut.icon.loadDrawable(this.mContext));
            }
            this.mBinding = false;
            super.onBindViewHolder(preferenceViewHolder);
        }

        @Override // androidx.preference.Preference
        public final void notifyChanged() {
            if (!this.mBinding) {
                super.notifyChanged();
            }
        }

        @Override // com.android.systemui.tuner.SelectablePreference, androidx.preference.Preference
        public final String toString() {
            return this.mShortcut.toString();
        }

        public ShortcutPreference(Context context, ShortcutParser.Shortcut shortcut, CharSequence charSequence) {
            super(context);
            this.mShortcut = shortcut;
            setTitle(shortcut.label);
            setSummary(context.getString(2131953412, charSequence));
        }
    }

    @Override // androidx.preference.PreferenceFragment
    public final void onCreatePreferences(String str) {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        Objects.requireNonNull(preferenceManager);
        final Context context = preferenceManager.mContext;
        PreferenceManager preferenceManager2 = this.mPreferenceManager;
        Objects.requireNonNull(preferenceManager2);
        final PreferenceScreen preferenceScreen = new PreferenceScreen(context, null);
        preferenceScreen.onAttachedToHierarchy(preferenceManager2);
        preferenceScreen.mOrderingAsAdded = true;
        final PreferenceCategory preferenceCategory = new PreferenceCategory(context, null);
        preferenceCategory.setTitle(2131953423);
        SelectablePreference selectablePreference = new SelectablePreference(context);
        this.mNonePreference = selectablePreference;
        this.mSelectablePreferences.add(selectablePreference);
        this.mNonePreference.setTitle(2131952669);
        SelectablePreference selectablePreference2 = this.mNonePreference;
        Objects.requireNonNull(selectablePreference2);
        selectablePreference2.setIcon(AppCompatResources.getDrawable(selectablePreference2.mContext, 2131232245));
        selectablePreference2.mIconResId = 2131232245;
        preferenceScreen.addPreference(this.mNonePreference);
        List<LauncherActivityInfo> activityList = ((LauncherApps) getContext().getSystemService(LauncherApps.class)).getActivityList(null, Process.myUserHandle());
        preferenceScreen.addPreference(preferenceCategory);
        activityList.forEach(new Consumer() { // from class: com.android.systemui.tuner.ShortcutPicker$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                final ShortcutPicker shortcutPicker = ShortcutPicker.this;
                final Context context2 = context;
                final PreferenceScreen preferenceScreen2 = preferenceScreen;
                PreferenceCategory preferenceCategory2 = preferenceCategory;
                final LauncherActivityInfo launcherActivityInfo = (LauncherActivityInfo) obj;
                int i = ShortcutPicker.$r8$clinit;
                Objects.requireNonNull(shortcutPicker);
                try {
                    ArrayList shortcuts = new ShortcutParser(shortcutPicker.getContext(), launcherActivityInfo.getComponentName()).getShortcuts();
                    ShortcutPicker.AppPreference appPreference = new ShortcutPicker.AppPreference(context2, launcherActivityInfo);
                    shortcutPicker.mSelectablePreferences.add(appPreference);
                    if (shortcuts.size() != 0) {
                        preferenceScreen2.addPreference(appPreference);
                        shortcuts.forEach(new Consumer() { // from class: com.android.systemui.tuner.ShortcutPicker$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj2) {
                                ShortcutPicker shortcutPicker2 = ShortcutPicker.this;
                                Context context3 = context2;
                                LauncherActivityInfo launcherActivityInfo2 = launcherActivityInfo;
                                PreferenceScreen preferenceScreen3 = preferenceScreen2;
                                int i2 = ShortcutPicker.$r8$clinit;
                                Objects.requireNonNull(shortcutPicker2);
                                ShortcutPicker.ShortcutPreference shortcutPreference = new ShortcutPicker.ShortcutPreference(context3, (ShortcutParser.Shortcut) obj2, launcherActivityInfo2.getLabel());
                                shortcutPicker2.mSelectablePreferences.add(shortcutPreference);
                                preferenceScreen3.addPreference(shortcutPreference);
                            }
                        });
                    } else {
                        preferenceCategory2.addPreference(appPreference);
                    }
                } catch (PackageManager.NameNotFoundException unused) {
                }
            }
        });
        preferenceScreen.removePreference(preferenceCategory);
        for (int i = 0; i < preferenceCategory.getPreferenceCount(); i++) {
            Preference preference = preferenceCategory.getPreference(0);
            preferenceCategory.removePreference(preference);
            if (Integer.MAX_VALUE != preference.mOrder) {
                preference.mOrder = Integer.MAX_VALUE;
                Preference.OnPreferenceChangeInternalListener onPreferenceChangeInternalListener = preference.mListener;
                if (onPreferenceChangeInternalListener != null) {
                    PreferenceGroupAdapter preferenceGroupAdapter = (PreferenceGroupAdapter) onPreferenceChangeInternalListener;
                    preferenceGroupAdapter.mHandler.removeCallbacks(preferenceGroupAdapter.mSyncRunnable);
                    preferenceGroupAdapter.mHandler.post(preferenceGroupAdapter.mSyncRunnable);
                }
            }
            preferenceScreen.addPreference(preference);
        }
        setPreferenceScreen(preferenceScreen);
        this.mKey = getArguments().getString("androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT");
        TunerService tunerService = (TunerService) Dependency.get(TunerService.class);
        this.mTunerService = tunerService;
        tunerService.addTunable(this, this.mKey);
    }

    @Override // androidx.preference.PreferenceFragment, androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
    public final boolean onPreferenceTreeClick(Preference preference) {
        this.mTunerService.setValue(this.mKey, preference.toString());
        getActivity().onBackPressed();
        return true;
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public final void onTuningChanged(String str, String str2) {
        if (str2 == null) {
            str2 = "";
        }
        this.mSelectablePreferences.forEach(new DozeTriggers$$ExternalSyntheticLambda4(str2, 3));
    }

    @Override // android.app.Fragment
    public final void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if ("sysui_keyguard_left".equals(this.mKey)) {
            getActivity().setTitle(2131952670);
        } else {
            getActivity().setTitle(2131952671);
        }
    }

    @Override // android.app.Fragment
    public final void onDestroy() {
        super.onDestroy();
        this.mTunerService.removeTunable(this);
    }
}
