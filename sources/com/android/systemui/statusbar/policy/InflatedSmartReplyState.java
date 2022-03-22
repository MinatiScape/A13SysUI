package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.SmartReplyView;
import java.util.ArrayList;
import java.util.List;
/* compiled from: InflatedSmartReplyState.kt */
/* loaded from: classes.dex */
public final class InflatedSmartReplyState {
    public final boolean hasPhishingAction;
    public final SmartReplyView.SmartActions smartActions;
    public final SmartReplyView.SmartReplies smartReplies;
    public final SuppressedActions suppressedActions;

    /* compiled from: InflatedSmartReplyState.kt */
    /* loaded from: classes.dex */
    public static final class SuppressedActions {
        public final List<Integer> suppressedActionIndices;

        public SuppressedActions(ArrayList arrayList) {
            this.suppressedActionIndices = arrayList;
        }
    }

    public InflatedSmartReplyState(SmartReplyView.SmartReplies smartReplies, SmartReplyView.SmartActions smartActions, SuppressedActions suppressedActions, boolean z) {
        this.smartReplies = smartReplies;
        this.smartActions = smartActions;
        this.suppressedActions = suppressedActions;
        this.hasPhishingAction = z;
    }
}
