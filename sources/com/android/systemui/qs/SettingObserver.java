package com.android.systemui.qs;

import android.app.ActivityManager;
import android.database.ContentObserver;
import android.os.Handler;
import com.android.systemui.util.settings.SettingsProxy;
/* loaded from: classes.dex */
public abstract class SettingObserver extends ContentObserver {
    public final int mDefaultValue;
    public boolean mListening;
    public int mObservedValue;
    public final String mSettingName;
    public final SettingsProxy mSettingsProxy;
    public int mUserId;

    public SettingObserver(SettingsProxy settingsProxy, Handler handler, String str) {
        this(settingsProxy, handler, str, ActivityManager.getCurrentUser());
    }

    public abstract void handleValueChanged(int i, boolean z);

    public SettingObserver(SettingsProxy settingsProxy, Handler handler, String str, int i) {
        super(handler);
        this.mSettingsProxy = settingsProxy;
        this.mSettingName = str;
        this.mDefaultValue = 0;
        this.mObservedValue = 0;
        this.mUserId = i;
    }

    public final int getValue() {
        if (this.mListening) {
            return this.mObservedValue;
        }
        return this.mSettingsProxy.getIntForUser(this.mSettingName, this.mDefaultValue, this.mUserId);
    }

    @Override // android.database.ContentObserver
    public final void onChange(boolean z) {
        boolean z2;
        int intForUser = this.mSettingsProxy.getIntForUser(this.mSettingName, this.mDefaultValue, this.mUserId);
        if (intForUser != this.mObservedValue) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mObservedValue = intForUser;
        handleValueChanged(intForUser, z2);
    }

    public final void setListening(boolean z) {
        if (z != this.mListening) {
            this.mListening = z;
            if (z) {
                this.mObservedValue = this.mSettingsProxy.getIntForUser(this.mSettingName, this.mDefaultValue, this.mUserId);
                SettingsProxy settingsProxy = this.mSettingsProxy;
                settingsProxy.registerContentObserverForUser(settingsProxy.getUriFor(this.mSettingName), false, this, this.mUserId);
                return;
            }
            this.mSettingsProxy.unregisterContentObserver(this);
            this.mObservedValue = this.mDefaultValue;
        }
    }

    public final void setUserId(int i) {
        this.mUserId = i;
        if (this.mListening) {
            setListening(false);
            setListening(true);
        }
    }
}
