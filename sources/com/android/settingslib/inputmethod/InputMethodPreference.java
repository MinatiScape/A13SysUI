package com.android.settingslib.inputmethod;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodSubtype;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.PrimarySwitchPreference;
import java.io.Serializable;
import java.util.Objects;
/* loaded from: classes.dex */
public class InputMethodPreference extends PrimarySwitchPreference implements Preference.OnPreferenceClickListener, Preference.OnPreferenceChangeListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public AlertDialog mDialog = null;
    public final InputMethodInfo mImi;
    public final OnSavePreferenceListener mOnSaveListener;
    public final int mUserId;

    /* loaded from: classes.dex */
    public interface OnSavePreferenceListener {
        void onSaveInputMethodPreference();
    }

    public final boolean isTv() {
        if ((this.mContext.getResources().getConfiguration().uiMode & 15) == 4) {
            return true;
        }
        return false;
    }

    @Override // androidx.preference.Preference.OnPreferenceChangeListener
    public final boolean onPreferenceChange(Preference preference, Serializable serializable) {
        boolean z;
        if (this.mSwitch == null || !this.mChecked) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            setCheckedInternal(false);
            return false;
        }
        if (!this.mImi.isSystem()) {
            AlertDialog alertDialog = this.mDialog;
            if (alertDialog != null && alertDialog.isShowing()) {
                this.mDialog.dismiss();
            }
            Context context = this.mContext;
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(true);
            builder.setTitle(17039380);
            builder.setMessage(context.getString(2131952451, this.mImi.getServiceInfo().applicationInfo.loadLabel(context.getPackageManager())));
            builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.android.settingslib.inputmethod.InputMethodPreference$$ExternalSyntheticLambda3
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i) {
                    InputMethodPreference inputMethodPreference = InputMethodPreference.this;
                    int i2 = InputMethodPreference.$r8$clinit;
                    Objects.requireNonNull(inputMethodPreference);
                    if (inputMethodPreference.mImi.getServiceInfo().directBootAware || inputMethodPreference.isTv()) {
                        inputMethodPreference.setCheckedInternal(true);
                    } else {
                        inputMethodPreference.showDirectBootWarnDialog();
                    }
                }
            });
            builder.setNegativeButton(17039360, new InputMethodPreference$$ExternalSyntheticLambda2(this, 0));
            builder.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.settingslib.inputmethod.InputMethodPreference$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnCancelListener
                public final void onCancel(DialogInterface dialogInterface) {
                    InputMethodPreference inputMethodPreference = InputMethodPreference.this;
                    int i = InputMethodPreference.$r8$clinit;
                    Objects.requireNonNull(inputMethodPreference);
                    inputMethodPreference.setCheckedInternal(false);
                }
            });
            AlertDialog create = builder.create();
            this.mDialog = create;
            create.show();
        } else if (this.mImi.getServiceInfo().directBootAware || isTv()) {
            setCheckedInternal(true);
        } else if (!isTv()) {
            showDirectBootWarnDialog();
        }
        return false;
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public final void onPreferenceClick(Preference preference) {
        Context context = this.mContext;
        try {
            Intent intent = this.mIntent;
            if (intent != null) {
                context.startActivityAsUser(intent, UserHandle.of(this.mUserId));
            }
        } catch (ActivityNotFoundException e) {
            Log.d("InputMethodPreference", "IME's Settings Activity Not Found", e);
            Toast.makeText(context, context.getString(2131952353, this.mImi.loadLabel(context.getPackageManager())), 1).show();
        }
    }

    public final void showDirectBootWarnDialog() {
        AlertDialog alertDialog = this.mDialog;
        if (alertDialog != null && alertDialog.isShowing()) {
            this.mDialog.dismiss();
        }
        Context context = this.mContext;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage(context.getText(2131952280));
        builder.setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.android.settingslib.inputmethod.InputMethodPreference$$ExternalSyntheticLambda4
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                InputMethodPreference inputMethodPreference = InputMethodPreference.this;
                int i2 = InputMethodPreference.$r8$clinit;
                Objects.requireNonNull(inputMethodPreference);
                inputMethodPreference.setCheckedInternal(true);
            }
        });
        builder.setNegativeButton(17039360, new InputMethodPreference$$ExternalSyntheticLambda1(this, 0));
        AlertDialog create = builder.create();
        this.mDialog = create;
        create.show();
    }

    @VisibleForTesting
    public InputMethodPreference(Context context, InputMethodInfo inputMethodInfo, CharSequence charSequence, boolean z, OnSavePreferenceListener onSavePreferenceListener, int i) {
        super(context);
        this.mPersistent = false;
        this.mImi = inputMethodInfo;
        this.mOnSaveListener = onSavePreferenceListener;
        setKey(inputMethodInfo.getId());
        setTitle(charSequence);
        String settingsActivity = inputMethodInfo.getSettingsActivity();
        if (TextUtils.isEmpty(settingsActivity)) {
            this.mIntent = null;
        } else {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.setClassName(inputMethodInfo.getPackageName(), settingsActivity);
            this.mIntent = intent;
        }
        context = i != UserHandle.myUserId() ? this.mContext.createContextAsUser(UserHandle.of(i), 0) : context;
        Object obj = InputMethodSettingValuesWrapper.sInstanceMapLock;
        int userId = context.getUserId();
        synchronized (InputMethodSettingValuesWrapper.sInstanceMapLock) {
            if (InputMethodSettingValuesWrapper.sInstanceMap.size() == 0) {
                InputMethodSettingValuesWrapper.sInstanceMap.put(userId, new InputMethodSettingValuesWrapper(context));
            } else if (InputMethodSettingValuesWrapper.sInstanceMap.indexOfKey(userId) >= 0) {
                InputMethodSettingValuesWrapper.sInstanceMap.get(userId);
            } else {
                InputMethodSettingValuesWrapper.sInstanceMap.put(context.getUserId(), new InputMethodSettingValuesWrapper(context));
            }
        }
        this.mUserId = i;
        if (inputMethodInfo.isSystem()) {
            int i2 = InputMethodAndSubtypeUtil.$r8$clinit;
            if (!inputMethodInfo.isAuxiliaryIme()) {
                int subtypeCount = inputMethodInfo.getSubtypeCount();
                for (int i3 = 0; i3 < subtypeCount; i3++) {
                    InputMethodSubtype subtypeAt = inputMethodInfo.getSubtypeAt(i3);
                    if ("keyboard".equalsIgnoreCase(subtypeAt.getMode()) && subtypeAt.isAsciiCapable()) {
                        break;
                    }
                }
            }
        }
        this.mOnClickListener = this;
        this.mOnChangeListener = this;
    }

    @Override // com.android.settingslib.PrimarySwitchPreference, com.android.settingslib.RestrictedPreference, com.android.settingslib.widget.TwoTargetPreference, androidx.preference.Preference
    public final void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        Switch r0 = this.mSwitch;
        if (r0 != null) {
            r0.setOnClickListener(new InputMethodPreference$$ExternalSyntheticLambda5(this, r0, 0));
        }
        ImageView imageView = (ImageView) preferenceViewHolder.itemView.findViewById(16908294);
        int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(2131166973);
        if (imageView != null && dimensionPixelSize > 0) {
            ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
            layoutParams.height = dimensionPixelSize;
            layoutParams.width = dimensionPixelSize;
            imageView.setLayoutParams(layoutParams);
        }
    }

    public final void setCheckedInternal(boolean z) {
        setChecked(z);
        this.mOnSaveListener.onSaveInputMethodPreference();
        notifyChanged();
    }
}
