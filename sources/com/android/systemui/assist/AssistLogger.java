package com.android.systemui.assist;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
import com.android.keyguard.KeyguardUpdateMonitor;
import java.util.Set;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
/* compiled from: AssistLogger.kt */
/* loaded from: classes.dex */
public class AssistLogger {
    public static final Set<AssistantSessionEvent> SESSION_END_EVENTS = SetsKt__SetsKt.setOf(AssistantSessionEvent.ASSISTANT_SESSION_INVOCATION_CANCELLED, AssistantSessionEvent.ASSISTANT_SESSION_CLOSE);
    public final AssistUtils assistUtils;
    public final Context context;
    public InstanceId currentInstanceId;
    public final InstanceIdSequence instanceIdSequence = new InstanceIdSequence(1048576);
    public final PhoneStateMonitor phoneStateMonitor;
    public final UiEventLogger uiEventLogger;

    public void reportAssistantInvocationExtraData() {
    }

    /* JADX WARN: Removed duplicated region for block: B:59:0x00c8  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void reportAssistantInvocationEventFromLegacy(int r24, boolean r25, android.content.ComponentName r26, java.lang.Integer r27) {
        /*
            Method dump skipped, instructions count: 286
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.assist.AssistLogger.reportAssistantInvocationEventFromLegacy(int, boolean, android.content.ComponentName, java.lang.Integer):void");
    }

    public final void reportAssistantSessionEvent(UiEventLogger.UiEventEnum uiEventEnum) {
        ComponentName assistComponentForUser = this.assistUtils.getAssistComponentForUser(KeyguardUpdateMonitor.getCurrentUser());
        int i = 0;
        try {
            i = this.context.getPackageManager().getApplicationInfo(assistComponentForUser.getPackageName(), 0).uid;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("AssistLogger", "Unable to find Assistant UID", e);
        }
        UiEventLogger uiEventLogger = this.uiEventLogger;
        String flattenToString = assistComponentForUser.flattenToString();
        InstanceId instanceId = this.currentInstanceId;
        if (instanceId == null) {
            instanceId = this.instanceIdSequence.newInstanceId();
        }
        this.currentInstanceId = instanceId;
        uiEventLogger.logWithInstanceId(uiEventEnum, i, flattenToString, instanceId);
        if (CollectionsKt___CollectionsKt.contains(SESSION_END_EVENTS, uiEventEnum)) {
            this.currentInstanceId = null;
        }
    }

    public AssistLogger(Context context, UiEventLogger uiEventLogger, AssistUtils assistUtils, PhoneStateMonitor phoneStateMonitor) {
        this.context = context;
        this.uiEventLogger = uiEventLogger;
        this.assistUtils = assistUtils;
        this.phoneStateMonitor = phoneStateMonitor;
    }
}
