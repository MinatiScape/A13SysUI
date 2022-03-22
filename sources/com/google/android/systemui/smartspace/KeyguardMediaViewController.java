package com.google.android.systemui.smartspace;

import android.content.ComponentName;
import android.content.Context;
import android.media.MediaMetadata;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
/* compiled from: KeyguardMediaViewController.kt */
/* loaded from: classes.dex */
public final class KeyguardMediaViewController {
    public CharSequence artist;
    public final BroadcastDispatcher broadcastDispatcher;
    public final Context context;
    public final ComponentName mediaComponent;
    public final KeyguardMediaViewController$mediaListener$1 mediaListener = new NotificationMediaManager.MediaListener() { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1
        @Override // com.android.systemui.statusbar.NotificationMediaManager.MediaListener
        public final void onPrimaryMetadataOrStateChanged(final MediaMetadata mediaMetadata, final int i) {
            KeyguardMediaViewController keyguardMediaViewController = KeyguardMediaViewController.this;
            Objects.requireNonNull(keyguardMediaViewController);
            DelayableExecutor delayableExecutor = keyguardMediaViewController.uiExecutor;
            final KeyguardMediaViewController keyguardMediaViewController2 = KeyguardMediaViewController.this;
            delayableExecutor.execute(new Runnable() { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1$onPrimaryMetadataOrStateChanged$1
                /* JADX WARN: Removed duplicated region for block: B:29:0x00ae  */
                /* JADX WARN: Removed duplicated region for block: B:34:? A[RETURN, SYNTHETIC] */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final void run() {
                    /*
                        r6 = this;
                        com.google.android.systemui.smartspace.KeyguardMediaViewController r0 = com.google.android.systemui.smartspace.KeyguardMediaViewController.this
                        android.media.MediaMetadata r1 = r2
                        int r6 = r3
                        java.util.Objects.requireNonNull(r0)
                        boolean r6 = com.android.systemui.statusbar.NotificationMediaManager.isPlayingState(r6)
                        r2 = 0
                        if (r6 != 0) goto L_0x001f
                        r0.title = r2
                        r0.artist = r2
                        com.android.systemui.plugins.BcSmartspaceDataPlugin$SmartspaceView r6 = r0.smartspaceView
                        if (r6 != 0) goto L_0x001a
                        goto L_0x00ba
                    L_0x001a:
                        r6.setMediaTarget(r2)
                        goto L_0x00ba
                    L_0x001f:
                        if (r1 != 0) goto L_0x0023
                        r6 = r2
                        goto L_0x003c
                    L_0x0023:
                        java.lang.String r6 = "android.media.metadata.TITLE"
                        java.lang.CharSequence r6 = r1.getText(r6)
                        boolean r3 = android.text.TextUtils.isEmpty(r6)
                        if (r3 == 0) goto L_0x003c
                        android.content.Context r6 = r0.context
                        android.content.res.Resources r6 = r6.getResources()
                        r3 = 2131952861(0x7f1304dd, float:1.9542177E38)
                        java.lang.String r6 = r6.getString(r3)
                    L_0x003c:
                        if (r1 != 0) goto L_0x0040
                        r1 = r2
                        goto L_0x0046
                    L_0x0040:
                        java.lang.String r3 = "android.media.metadata.ARTIST"
                        java.lang.CharSequence r1 = r1.getText(r3)
                    L_0x0046:
                        java.lang.CharSequence r3 = r0.title
                        boolean r3 = android.text.TextUtils.equals(r3, r6)
                        if (r3 == 0) goto L_0x0057
                        java.lang.CharSequence r3 = r0.artist
                        boolean r3 = android.text.TextUtils.equals(r3, r1)
                        if (r3 == 0) goto L_0x0057
                        goto L_0x00ba
                    L_0x0057:
                        r0.title = r6
                        r0.artist = r1
                        if (r6 != 0) goto L_0x005e
                        goto L_0x00a5
                    L_0x005e:
                        android.app.smartspace.SmartspaceAction$Builder r1 = new android.app.smartspace.SmartspaceAction$Builder
                        java.lang.String r6 = r6.toString()
                        java.lang.String r3 = "deviceMediaTitle"
                        r1.<init>(r3, r6)
                        java.lang.CharSequence r6 = r0.artist
                        android.app.smartspace.SmartspaceAction$Builder r6 = r1.setSubtitle(r6)
                        com.android.systemui.statusbar.NotificationMediaManager r1 = r0.mediaManager
                        android.graphics.drawable.Icon r1 = r1.getMediaIcon()
                        android.app.smartspace.SmartspaceAction$Builder r6 = r6.setIcon(r1)
                        android.app.smartspace.SmartspaceAction r6 = r6.build()
                        com.google.android.systemui.smartspace.KeyguardMediaViewController$init$2 r1 = r0.userTracker
                        if (r1 != 0) goto L_0x0082
                        r1 = r2
                    L_0x0082:
                        int r1 = r1.getCurrentUserId()
                        android.os.UserHandle r1 = android.os.UserHandle.of(r1)
                        android.app.smartspace.SmartspaceTarget$Builder r3 = new android.app.smartspace.SmartspaceTarget$Builder
                        android.content.ComponentName r4 = r0.mediaComponent
                        java.lang.String r5 = "deviceMedia"
                        r3.<init>(r5, r4, r1)
                        r1 = 31
                        android.app.smartspace.SmartspaceTarget$Builder r1 = r3.setFeatureType(r1)
                        android.app.smartspace.SmartspaceTarget$Builder r6 = r1.setHeaderAction(r6)
                        android.app.smartspace.SmartspaceTarget r6 = r6.build()
                        com.android.systemui.plugins.BcSmartspaceDataPlugin$SmartspaceView r1 = r0.smartspaceView
                        if (r1 != 0) goto L_0x00a7
                    L_0x00a5:
                        r6 = r2
                        goto L_0x00ac
                    L_0x00a7:
                        r1.setMediaTarget(r6)
                        kotlin.Unit r6 = kotlin.Unit.INSTANCE
                    L_0x00ac:
                        if (r6 != 0) goto L_0x00ba
                        r0.title = r2
                        r0.artist = r2
                        com.android.systemui.plugins.BcSmartspaceDataPlugin$SmartspaceView r6 = r0.smartspaceView
                        if (r6 != 0) goto L_0x00b7
                        goto L_0x00ba
                    L_0x00b7:
                        r6.setMediaTarget(r2)
                    L_0x00ba:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1$onPrimaryMetadataOrStateChanged$1.run():void");
                }
            });
        }
    };
    public final NotificationMediaManager mediaManager;
    public final BcSmartspaceDataPlugin plugin;
    public BcSmartspaceDataPlugin.SmartspaceView smartspaceView;
    public CharSequence title;
    public final DelayableExecutor uiExecutor;
    public KeyguardMediaViewController$init$2 userTracker;

    @VisibleForTesting
    public static /* synthetic */ void getSmartspaceView$annotations() {
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1] */
    public KeyguardMediaViewController(Context context, BcSmartspaceDataPlugin bcSmartspaceDataPlugin, DelayableExecutor delayableExecutor, NotificationMediaManager notificationMediaManager, BroadcastDispatcher broadcastDispatcher) {
        this.context = context;
        this.plugin = bcSmartspaceDataPlugin;
        this.uiExecutor = delayableExecutor;
        this.mediaManager = notificationMediaManager;
        this.broadcastDispatcher = broadcastDispatcher;
        this.mediaComponent = new ComponentName(context, KeyguardMediaViewController.class);
    }
}
