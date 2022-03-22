package com.google.android.systemui.assist;

import android.content.Context;
import android.util.StatsEvent;
import android.util.StatsLog;
import com.android.internal.app.AssistUtils;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.assist.AssistLogger;
import com.android.systemui.assist.PhoneStateMonitor;
import com.google.android.systemui.assist.uihints.AssistantPresenceHandler;
import java.util.Objects;
/* compiled from: GoogleAssistLogger.kt */
/* loaded from: classes.dex */
public final class GoogleAssistLogger extends AssistLogger {
    public final AssistantPresenceHandler assistantPresenceHandler;

    public GoogleAssistLogger(Context context, UiEventLogger uiEventLogger, AssistUtils assistUtils, PhoneStateMonitor phoneStateMonitor, AssistantPresenceHandler assistantPresenceHandler) {
        super(context, uiEventLogger, assistUtils, phoneStateMonitor);
        this.assistantPresenceHandler = assistantPresenceHandler;
    }

    @Override // com.android.systemui.assist.AssistLogger
    public final void reportAssistantInvocationExtraData() {
        StatsEvent.Builder atomId = StatsEvent.newBuilder().setAtomId(100045);
        InstanceId instanceId = this.currentInstanceId;
        if (instanceId == null) {
            instanceId = this.instanceIdSequence.newInstanceId();
        }
        this.currentInstanceId = instanceId;
        StatsEvent.Builder writeInt = atomId.writeInt(instanceId.getId());
        AssistantPresenceHandler assistantPresenceHandler = this.assistantPresenceHandler;
        Objects.requireNonNull(assistantPresenceHandler);
        StatsLog.write(writeInt.writeBoolean(assistantPresenceHandler.mNgaIsAssistant).build());
    }
}
