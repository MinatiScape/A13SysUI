package com.android.systemui.tuner;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.util.TypedValue;
import android.widget.EditText;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationBarInflaterView;
import com.android.systemui.tuner.TunerService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;
@Deprecated
/* loaded from: classes.dex */
public class NavBarTuner extends TunerPreferenceFragment {
    public static final int[][] ICONS = {new int[]{2131232221, 2131953413}, new int[]{2131231745, 2131953425}, new int[]{2131232244, 2131953422}, new int[]{2131232025, 2131953418}, new int[]{2131232248, 2131953426}, new int[]{2131232052, 2131953421}};
    public Handler mHandler;
    public final ArrayList<TunerService.Tunable> mTunables = new ArrayList<>();

    public final void bindButton(final String str, final String str2, String str3) {
        final ListPreference listPreference = (ListPreference) findPreference("type_" + str3);
        final Preference findPreference = findPreference("keycode_" + str3);
        final ListPreference listPreference2 = (ListPreference) findPreference("icon_" + str3);
        CharSequence[] charSequenceArr = new CharSequence[6];
        CharSequence[] charSequenceArr2 = new CharSequence[6];
        int applyDimension = (int) TypedValue.applyDimension(1, 14.0f, getContext().getResources().getDisplayMetrics());
        int i = 0;
        while (true) {
            int[][] iArr = ICONS;
            if (i < 6) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
                Drawable loadDrawable = Icon.createWithResource(getContext().getPackageName(), iArr[i][0]).loadDrawable(getContext());
                loadDrawable.setTint(-16777216);
                loadDrawable.setBounds(0, 0, applyDimension, applyDimension);
                spannableStringBuilder.append("  ", new ImageSpan(loadDrawable, 1), 0);
                spannableStringBuilder.append((CharSequence) " ");
                spannableStringBuilder.append((CharSequence) getString(iArr[i][1]));
                charSequenceArr[i] = spannableStringBuilder;
                charSequenceArr2[i] = getContext().getPackageName() + "/" + iArr[i][0];
                i++;
            } else {
                listPreference2.setEntries(charSequenceArr);
                listPreference2.mEntryValues = charSequenceArr2;
                TunerService.Tunable navBarTuner$$ExternalSyntheticLambda5 = new TunerService.Tunable() { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda5
                    @Override // com.android.systemui.tuner.TunerService.Tunable
                    public final void onTuningChanged(String str4, final String str5) {
                        final NavBarTuner navBarTuner = NavBarTuner.this;
                        final String str6 = str2;
                        final ListPreference listPreference3 = listPreference;
                        final ListPreference listPreference4 = listPreference2;
                        final Preference preference = findPreference;
                        int[][] iArr2 = NavBarTuner.ICONS;
                        Objects.requireNonNull(navBarTuner);
                        navBarTuner.mHandler.post(new Runnable() { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda8
                            @Override // java.lang.Runnable
                            public final void run() {
                                String str7;
                                NavBarTuner navBarTuner2 = NavBarTuner.this;
                                String str8 = str5;
                                String str9 = str6;
                                ListPreference listPreference5 = listPreference3;
                                ListPreference listPreference6 = listPreference4;
                                Preference preference2 = preference;
                                int[][] iArr3 = NavBarTuner.ICONS;
                                Objects.requireNonNull(navBarTuner2);
                                if (str8 == null) {
                                    str8 = str9;
                                }
                                String extractButton = NavigationBarInflaterView.extractButton(str8);
                                if (extractButton.startsWith("key")) {
                                    listPreference5.setValue("key");
                                    if (!extractButton.contains(":")) {
                                        str7 = null;
                                    } else {
                                        str7 = extractButton.substring(extractButton.indexOf(":") + 1, extractButton.indexOf(")"));
                                    }
                                    int extractKeycode = NavigationBarInflaterView.extractKeycode(extractButton);
                                    listPreference6.setValue(str7);
                                    navBarTuner2.updateSummary(listPreference6);
                                    preference2.setSummary(extractKeycode + "");
                                    preference2.setVisible(true);
                                    listPreference6.setVisible(true);
                                    return;
                                }
                                listPreference5.setValue(extractButton);
                                preference2.setVisible(false);
                                listPreference6.setVisible(false);
                            }
                        });
                    }
                };
                this.mTunables.add(navBarTuner$$ExternalSyntheticLambda5);
                ((TunerService) Dependency.get(TunerService.class)).addTunable(navBarTuner$$ExternalSyntheticLambda5, str);
                Preference.OnPreferenceChangeListener navBarTuner$$ExternalSyntheticLambda1 = new Preference.OnPreferenceChangeListener() { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda1
                    @Override // androidx.preference.Preference.OnPreferenceChangeListener
                    public final boolean onPreferenceChange(Preference preference, Serializable serializable) {
                        final NavBarTuner navBarTuner = NavBarTuner.this;
                        final String str4 = str;
                        final ListPreference listPreference3 = listPreference;
                        final Preference preference2 = findPreference;
                        final ListPreference listPreference4 = listPreference2;
                        int[][] iArr2 = NavBarTuner.ICONS;
                        Objects.requireNonNull(navBarTuner);
                        navBarTuner.mHandler.post(new Runnable() { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda7
                            @Override // java.lang.Runnable
                            public final void run() {
                                NavBarTuner navBarTuner2 = NavBarTuner.this;
                                String str5 = str4;
                                ListPreference listPreference5 = listPreference3;
                                Preference preference3 = preference2;
                                ListPreference listPreference6 = listPreference4;
                                int[][] iArr3 = NavBarTuner.ICONS;
                                Objects.requireNonNull(navBarTuner2);
                                NavBarTuner.setValue(str5, listPreference5, preference3, listPreference6);
                                navBarTuner2.updateSummary(listPreference6);
                            }
                        });
                        return true;
                    }
                };
                Objects.requireNonNull(listPreference);
                listPreference.mOnChangeListener = navBarTuner$$ExternalSyntheticLambda1;
                listPreference2.mOnChangeListener = navBarTuner$$ExternalSyntheticLambda1;
                Preference.OnPreferenceClickListener navBarTuner$$ExternalSyntheticLambda3 = new Preference.OnPreferenceClickListener() { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda3
                    @Override // androidx.preference.Preference.OnPreferenceClickListener
                    public final void onPreferenceClick(Preference preference) {
                        final NavBarTuner navBarTuner = NavBarTuner.this;
                        final Preference preference2 = findPreference;
                        final String str4 = str;
                        final ListPreference listPreference3 = listPreference;
                        final ListPreference listPreference4 = listPreference2;
                        int[][] iArr2 = NavBarTuner.ICONS;
                        Objects.requireNonNull(navBarTuner);
                        final EditText editText = new EditText(navBarTuner.getContext());
                        AlertDialog.Builder builder = new AlertDialog.Builder(navBarTuner.getContext());
                        Objects.requireNonNull(preference);
                        builder.setTitle(preference.mTitle).setView(editText).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda0
                            @Override // android.content.DialogInterface.OnClickListener
                            public final void onClick(DialogInterface dialogInterface, int i2) {
                                int i3;
                                NavBarTuner navBarTuner2 = NavBarTuner.this;
                                EditText editText2 = editText;
                                Preference preference3 = preference2;
                                String str5 = str4;
                                ListPreference listPreference5 = listPreference3;
                                ListPreference listPreference6 = listPreference4;
                                int[][] iArr3 = NavBarTuner.ICONS;
                                Objects.requireNonNull(navBarTuner2);
                                try {
                                    i3 = Integer.parseInt(editText2.getText().toString());
                                } catch (Exception unused) {
                                    i3 = 66;
                                }
                                preference3.setSummary(i3 + "");
                                NavBarTuner.setValue(str5, listPreference5, preference3, listPreference6);
                            }
                        }).show();
                    }
                };
                Objects.requireNonNull(findPreference);
                findPreference.mOnClickListener = navBarTuner$$ExternalSyntheticLambda3;
                return;
            }
        }
    }

    @Override // androidx.preference.PreferenceFragment, android.app.Fragment
    public final void onCreate(Bundle bundle) {
        this.mHandler = new Handler();
        super.onCreate(bundle);
    }

    public final void updateSummary(ListPreference listPreference) {
        try {
            int applyDimension = (int) TypedValue.applyDimension(1, 14.0f, getContext().getResources().getDisplayMetrics());
            Objects.requireNonNull(listPreference);
            String str = listPreference.mValue.split("/")[0];
            int parseInt = Integer.parseInt(listPreference.mValue.split("/")[1]);
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            Drawable loadDrawable = Icon.createWithResource(str, parseInt).loadDrawable(getContext());
            loadDrawable.setTint(-16777216);
            loadDrawable.setBounds(0, 0, applyDimension, applyDimension);
            spannableStringBuilder.append("  ", new ImageSpan(loadDrawable, 1), 0);
            spannableStringBuilder.append((CharSequence) " ");
            int i = 0;
            while (true) {
                int[][] iArr = ICONS;
                if (i < 6) {
                    if (iArr[i][0] == parseInt) {
                        spannableStringBuilder.append((CharSequence) getString(iArr[i][1]));
                    }
                    i++;
                } else {
                    listPreference.setSummary(spannableStringBuilder);
                    return;
                }
            }
        } catch (Exception e) {
            Log.d("NavButton", "Problem with summary", e);
            listPreference.setSummary(null);
        }
    }

    public static void setValue(String str, ListPreference listPreference, Preference preference, ListPreference listPreference2) {
        Objects.requireNonNull(listPreference);
        String str2 = listPreference.mValue;
        if ("key".equals(str2)) {
            Objects.requireNonNull(listPreference2);
            String str3 = listPreference2.mValue;
            int i = 66;
            try {
                i = Integer.parseInt(preference.getSummary().toString());
            } catch (Exception unused) {
            }
            str2 = str2 + "(" + i + ":" + str3 + ")";
        }
        ((TunerService) Dependency.get(TunerService.class)).setValue(str, str2);
    }

    @Override // android.app.Fragment
    public final void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override // androidx.preference.PreferenceFragment
    public final void onCreatePreferences(String str) {
        addPreferencesFromResource(2132213769);
        final ListPreference listPreference = (ListPreference) findPreference("layout");
        TunerService.Tunable navBarTuner$$ExternalSyntheticLambda4 = new TunerService.Tunable() { // from class: com.android.systemui.tuner.NavBarTuner$$ExternalSyntheticLambda4
            @Override // com.android.systemui.tuner.TunerService.Tunable
            public final void onTuningChanged(String str2, String str3) {
                NavBarTuner navBarTuner = NavBarTuner.this;
                ListPreference listPreference2 = listPreference;
                int[][] iArr = NavBarTuner.ICONS;
                Objects.requireNonNull(navBarTuner);
                navBarTuner.mHandler.post(new NavBarTuner$$ExternalSyntheticLambda6(str3, listPreference2, 0));
            }
        };
        this.mTunables.add(navBarTuner$$ExternalSyntheticLambda4);
        ((TunerService) Dependency.get(TunerService.class)).addTunable(navBarTuner$$ExternalSyntheticLambda4, "sysui_nav_bar");
        NavBarTuner$$ExternalSyntheticLambda2 navBarTuner$$ExternalSyntheticLambda2 = NavBarTuner$$ExternalSyntheticLambda2.INSTANCE;
        Objects.requireNonNull(listPreference);
        listPreference.mOnChangeListener = navBarTuner$$ExternalSyntheticLambda2;
        bindButton("sysui_nav_bar_left", "space", "left");
        bindButton("sysui_nav_bar_right", "menu_ime", "right");
    }

    @Override // android.app.Fragment
    public final void onDestroy() {
        super.onDestroy();
        this.mTunables.forEach(NavBarTuner$$ExternalSyntheticLambda9.INSTANCE);
    }
}
