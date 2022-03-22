package com.android.systemui.statusbar.policy;

import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.DevicePolicyManagerWrapper;
import com.android.systemui.shared.system.PackageManagerWrapper;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class SmartReplyStateInflaterImpl implements SmartReplyStateInflater {
    public final ActivityManagerWrapper activityManagerWrapper;
    public final SmartReplyConstants constants;
    public final DevicePolicyManagerWrapper devicePolicyManagerWrapper;
    public final PackageManagerWrapper packageManagerWrapper;
    public final SmartActionInflater smartActionsInflater;
    public final SmartReplyInflater smartRepliesInflater;

    /* JADX WARN: Removed duplicated region for block: B:105:0x01b3  */
    /* JADX WARN: Removed duplicated region for block: B:106:0x01b8  */
    /* JADX WARN: Removed duplicated region for block: B:108:0x01bc  */
    /* JADX WARN: Removed duplicated region for block: B:118:0x014b A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:121:0x00fb A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:129:0x01bf A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0078  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0096  */
    /* JADX WARN: Removed duplicated region for block: B:35:0x009c  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00ed  */
    /* JADX WARN: Removed duplicated region for block: B:56:0x00f2  */
    /* JADX WARN: Removed duplicated region for block: B:82:0x016e  */
    /* JADX WARN: Removed duplicated region for block: B:92:0x018c  */
    @Override // com.android.systemui.statusbar.policy.SmartReplyStateInflater
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.android.systemui.statusbar.policy.InflatedSmartReplyState inflateSmartReplyState(com.android.systemui.statusbar.notification.collection.NotificationEntry r18) {
        /*
            Method dump skipped, instructions count: 461
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl.inflateSmartReplyState(com.android.systemui.statusbar.notification.collection.NotificationEntry):com.android.systemui.statusbar.policy.InflatedSmartReplyState");
    }

    /* JADX WARN: Code restructure failed: missing block: B:52:0x007d, code lost:
        if (com.android.systemui.statusbar.NotificationUiAdjustment.areDifferent(r0, r3) == false) goto L_0x007f;
     */
    /* JADX WARN: Removed duplicated region for block: B:57:0x00ae  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x00b4  */
    /* JADX WARN: Removed duplicated region for block: B:61:0x00b6  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x00cf  */
    /* JADX WARN: Removed duplicated region for block: B:66:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0103  */
    @Override // com.android.systemui.statusbar.policy.SmartReplyStateInflater
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder inflateSmartReplyViewHolder(android.content.Context r15, android.content.Context r16, com.android.systemui.statusbar.notification.collection.NotificationEntry r17, com.android.systemui.statusbar.policy.InflatedSmartReplyState r18, com.android.systemui.statusbar.policy.InflatedSmartReplyState r19) {
        /*
            Method dump skipped, instructions count: 275
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.SmartReplyStateInflaterImpl.inflateSmartReplyViewHolder(android.content.Context, android.content.Context, com.android.systemui.statusbar.notification.collection.NotificationEntry, com.android.systemui.statusbar.policy.InflatedSmartReplyState, com.android.systemui.statusbar.policy.InflatedSmartReplyState):com.android.systemui.statusbar.policy.InflatedSmartReplyViewHolder");
    }

    public SmartReplyStateInflaterImpl(SmartReplyConstants smartReplyConstants, ActivityManagerWrapper activityManagerWrapper, PackageManagerWrapper packageManagerWrapper, DevicePolicyManagerWrapper devicePolicyManagerWrapper, SmartReplyInflater smartReplyInflater, SmartActionInflater smartActionInflater) {
        this.constants = smartReplyConstants;
        this.activityManagerWrapper = activityManagerWrapper;
        this.packageManagerWrapper = packageManagerWrapper;
        this.devicePolicyManagerWrapper = devicePolicyManagerWrapper;
        this.smartRepliesInflater = smartReplyInflater;
        this.smartActionsInflater = smartActionInflater;
    }
}
