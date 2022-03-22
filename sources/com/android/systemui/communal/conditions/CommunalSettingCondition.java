package com.android.systemui.communal.conditions;

import android.database.ContentObserver;
import android.os.Handler;
import com.android.systemui.util.condition.Condition;
import com.android.systemui.util.settings.SecureSettings;
/* loaded from: classes.dex */
public final class CommunalSettingCondition extends Condition {
    public final AnonymousClass1 mCommunalSettingContentObserver;
    public final SecureSettings mSecureSettings;

    /* renamed from: com.android.systemui.communal.conditions.CommunalSettingCondition$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends ContentObserver {
        public AnonymousClass1(Handler handler) {
            super(handler);
        }

        @Override // android.database.ContentObserver
        public final void onChange(boolean z) {
            boolean z2 = false;
            if (CommunalSettingCondition.this.mSecureSettings.getIntForUser("communal_mode_enabled", 0, 0) == 1) {
                z2 = true;
            }
            CommunalSettingCondition.this.updateCondition(z2);
        }
    }

    @Override // com.android.systemui.util.condition.Condition
    public final void start() {
        this.mSecureSettings.registerContentObserverForUser(this.mCommunalSettingContentObserver);
        this.mCommunalSettingContentObserver.onChange(false);
    }

    @Override // com.android.systemui.util.condition.Condition
    public final void stop() {
        this.mSecureSettings.unregisterContentObserver(this.mCommunalSettingContentObserver);
    }

    public CommunalSettingCondition(Handler handler, SecureSettings secureSettings) {
        this.mSecureSettings = secureSettings;
        this.mCommunalSettingContentObserver = new AnonymousClass1(handler);
    }
}
