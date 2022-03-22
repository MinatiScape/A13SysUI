package com.android.systemui.qs.external;

import com.android.internal.logging.InstanceIdSequence;
import com.android.internal.logging.UiEventLogger;
/* compiled from: TileRequestDialogEventLogger.kt */
/* loaded from: classes.dex */
public final class TileRequestDialogEventLogger {
    public final InstanceIdSequence instanceIdSequence;
    public final UiEventLogger uiEventLogger;

    public TileRequestDialogEventLogger(UiEventLogger uiEventLogger, InstanceIdSequence instanceIdSequence) {
        this.uiEventLogger = uiEventLogger;
        this.instanceIdSequence = instanceIdSequence;
    }
}
