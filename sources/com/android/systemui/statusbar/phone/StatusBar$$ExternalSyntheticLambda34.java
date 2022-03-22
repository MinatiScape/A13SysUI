package com.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import android.app.Notification;
import android.app.PendingIntent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.service.notification.StatusBarNotification;
import android.util.EventLog;
import com.android.systemui.lowlightclock.LowLightClockController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationInlineImageCache;
import com.android.systemui.statusbar.notification.row.NotificationInlineImageResolver;
import com.android.systemui.statusbar.window.StatusBarWindowController;
import com.android.systemui.unfold.UnfoldTransitionProgressProvider;
import com.android.systemui.unfold.util.JankMonitorTransitionProgressListener;
import com.google.android.systemui.assist.uihints.TranscriptionView;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda34 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda34(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.window.StatusBarWindowController$$ExternalSyntheticLambda1] */
    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) this.f$0;
                NotificationEntry notificationEntry = (NotificationEntry) obj;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                Objects.requireNonNull(notificationEntry);
                StatusBarNotification statusBarNotification = notificationEntry.mSbn;
                Notification notification = statusBarNotification.getNotification();
                if (notification.fullScreenIntent != null) {
                    try {
                        EventLog.writeEvent(36003, statusBarNotification.getKey());
                        statusBar.wakeUpForFullScreenIntent();
                        notification.fullScreenIntent.send();
                        notificationEntry.interruption = true;
                        notificationEntry.lastFullScreenIntentLaunchTime = SystemClock.elapsedRealtime();
                        return;
                    } catch (PendingIntent.CanceledException unused) {
                        return;
                    }
                } else {
                    return;
                }
            case 1:
                NotificationInlineImageResolver notificationInlineImageResolver = (NotificationInlineImageResolver) this.f$0;
                Uri uri = (Uri) obj;
                int i = NotificationInlineImageResolver.$r8$clinit;
                Objects.requireNonNull(notificationInlineImageResolver);
                NotificationInlineImageCache notificationInlineImageCache = (NotificationInlineImageCache) notificationInlineImageResolver.mImageCache;
                Objects.requireNonNull(notificationInlineImageCache);
                if (!notificationInlineImageCache.mCache.containsKey(uri)) {
                    NotificationInlineImageCache notificationInlineImageCache2 = (NotificationInlineImageCache) notificationInlineImageResolver.mImageCache;
                    Objects.requireNonNull(notificationInlineImageCache2);
                    NotificationInlineImageCache.PreloadImageTask preloadImageTask = new NotificationInlineImageCache.PreloadImageTask(notificationInlineImageCache2.mResolver);
                    preloadImageTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, uri);
                    notificationInlineImageCache2.mCache.put(uri, preloadImageTask);
                    return;
                }
                return;
            case 2:
                NotificationShadeWindowViewController notificationShadeWindowViewController = (NotificationShadeWindowViewController) this.f$0;
                Objects.requireNonNull(notificationShadeWindowViewController);
                ((LowLightClockController) obj).attachLowLightClockView(notificationShadeWindowViewController.mView);
                return;
            case 3:
                final StatusBarWindowController statusBarWindowController = (StatusBarWindowController) this.f$0;
                Objects.requireNonNull(statusBarWindowController);
                ((UnfoldTransitionProgressProvider) obj).addCallback(new JankMonitorTransitionProgressListener(new Supplier() { // from class: com.android.systemui.statusbar.window.StatusBarWindowController$$ExternalSyntheticLambda1
                    @Override // java.util.function.Supplier
                    public final Object get() {
                        StatusBarWindowController statusBarWindowController2 = StatusBarWindowController.this;
                        Objects.requireNonNull(statusBarWindowController2);
                        return statusBarWindowController2.mStatusBarWindowView;
                    }
                }));
                return;
            default:
                TranscriptionView.TranscriptionSpan transcriptionSpan = (TranscriptionView.TranscriptionSpan) obj;
                float animatedFraction = ((ValueAnimator) this.f$0).getAnimatedFraction();
                Objects.requireNonNull(transcriptionSpan);
                transcriptionSpan.mCurrentFraction = animatedFraction;
                return;
        }
    }
}
