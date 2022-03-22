package com.google.android.systemui.assist.uihints;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.provider.Settings;
import com.android.internal.app.AssistUtils;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import java.util.HashSet;
import java.util.Iterator;
/* loaded from: classes.dex */
public final class AssistantPresenceHandler implements NgaMessageHandler.ConfigInfoListener {
    public final AssistUtils mAssistUtils;
    public final ContentResolver mContentResolver;
    public boolean mGoogleIsAssistant;
    public boolean mNgaIsAssistant;
    public boolean mSysUiIsNgaUi;
    public final HashSet mAssistantPresenceChangeListeners = new HashSet();
    public final HashSet mSysUiIsNgaUiChangeListeners = new HashSet();

    /* loaded from: classes.dex */
    public interface AssistantPresenceChangeListener {
        void onAssistantPresenceChanged(boolean z, boolean z2);
    }

    /* loaded from: classes.dex */
    public interface SysUiIsNgaUiChangeListener {
        void onSysUiIsNgaUiChanged(boolean z);
    }

    public final void updateAssistantPresence(boolean z, boolean z2, boolean z3) {
        boolean z4;
        boolean z5 = true;
        if (!z || !z2) {
            z4 = false;
        } else {
            z4 = true;
        }
        if (!z4 || !z3) {
            z5 = false;
        }
        if (!(this.mGoogleIsAssistant == z && this.mNgaIsAssistant == z4)) {
            this.mGoogleIsAssistant = z;
            this.mNgaIsAssistant = z4;
            ContentResolver contentResolver = this.mContentResolver;
            int i = z4 ? 1 : 0;
            int i2 = z4 ? 1 : 0;
            Settings.Secure.putInt(contentResolver, "com.google.android.systemui.assist.uihints.NGA_IS_ASSISTANT", i);
            Iterator it = this.mAssistantPresenceChangeListeners.iterator();
            while (it.hasNext()) {
                ((AssistantPresenceChangeListener) it.next()).onAssistantPresenceChanged(this.mGoogleIsAssistant, this.mNgaIsAssistant);
            }
        }
        if (this.mSysUiIsNgaUi != z5) {
            this.mSysUiIsNgaUi = z5;
            ContentResolver contentResolver2 = this.mContentResolver;
            int i3 = z5 ? 1 : 0;
            int i4 = z5 ? 1 : 0;
            Settings.Secure.putInt(contentResolver2, "com.google.android.systemui.assist.uihints.SYS_UI_IS_NGA_UI", i3);
            Iterator it2 = this.mSysUiIsNgaUiChangeListeners.iterator();
            while (it2.hasNext()) {
                ((SysUiIsNgaUiChangeListener) it2.next()).onSysUiIsNgaUiChanged(this.mSysUiIsNgaUi);
            }
        }
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ConfigInfoListener
    public final void onConfigInfo(NgaMessageHandler.ConfigInfo configInfo) {
        boolean z;
        ComponentName assistComponentForUser = this.mAssistUtils.getAssistComponentForUser(-2);
        if (assistComponentForUser == null || !"com.google.android.googlequicksearchbox/com.google.android.voiceinteraction.GsaVoiceInteractionService".equals(assistComponentForUser.flattenToString())) {
            z = false;
        } else {
            z = true;
        }
        updateAssistantPresence(z, configInfo.ngaIsAssistant, configInfo.sysUiIsNgaUi);
    }

    public AssistantPresenceHandler(Context context, AssistUtils assistUtils) {
        boolean z;
        ContentResolver contentResolver = context.getContentResolver();
        this.mContentResolver = contentResolver;
        this.mAssistUtils = assistUtils;
        boolean z2 = false;
        if (Settings.Secure.getInt(contentResolver, "com.google.android.systemui.assist.uihints.NGA_IS_ASSISTANT", 0) != 0) {
            z = true;
        } else {
            z = false;
        }
        this.mNgaIsAssistant = z;
        this.mSysUiIsNgaUi = Settings.Secure.getInt(contentResolver, "com.google.android.systemui.assist.uihints.SYS_UI_IS_NGA_UI", 0) != 0 ? true : z2;
    }
}
