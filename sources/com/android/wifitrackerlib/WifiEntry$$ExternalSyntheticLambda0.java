package com.android.wifitrackerlib;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.os.Debug;
import android.os.SystemClock;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.ArrayMap;
import android.util.Log;
import com.android.internal.util.IndentingPrintWriter;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.systemui.util.leak.LeakDetector;
import com.android.systemui.util.leak.LeakDetector$$ExternalSyntheticLambda0;
import com.android.systemui.util.leak.LeakDetector$$ExternalSyntheticLambda1;
import com.android.systemui.util.leak.LeakReporter;
import com.android.systemui.util.leak.TrackedGarbage;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.google.android.systemui.dreamliner.DockObserver;
import com.google.android.systemui.elmyra.gates.Gate;
import com.google.android.systemui.input.TouchContextService;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.ref.Reference;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiEntry$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ WifiEntry$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v10 */
    /* JADX WARN: Type inference failed for: r1v5, types: [int] */
    /* JADX WARN: Type inference failed for: r1v7 */
    @Override // java.lang.Runnable
    public final void run() {
        IOException e;
        File file;
        File file2;
        FileOutputStream fileOutputStream;
        Throwable th;
        String str;
        File file3;
        File file4;
        Throwable th2;
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                WifiEntry wifiEntry = (WifiEntry) this.f$0;
                Objects.requireNonNull(wifiEntry);
                WifiEntry.WifiEntryCallback wifiEntryCallback = wifiEntry.mListener;
                if (wifiEntryCallback != null) {
                    wifiEntryCallback.onUpdated();
                    return;
                }
                return;
            case 1:
                KeyguardUpdateMonitor.AnonymousClass15 r0 = (KeyguardUpdateMonitor.AnonymousClass15) this.f$0;
                Objects.requireNonNull(r0);
                KeyguardUpdateMonitor.this.updateBiometricListeningState(2);
                return;
            case 2:
            default:
                TouchContextService touchContextService = (TouchContextService) this.f$0;
                String str2 = TouchContextService.INTERFACE;
                Objects.requireNonNull(touchContextService);
                touchContextService.onDisplayChanged(0);
                return;
            case 3:
                StatusBar.AnonymousClass10 r02 = (StatusBar.AnonymousClass10) this.f$0;
                Objects.requireNonNull(r02);
                StatusBar.this.mCommandQueueCallbacks.onEmergencyActionLaunchGestureDetected();
                return;
            case 4:
                GarbageMonitor garbageMonitor = (GarbageMonitor) this.f$0;
                Objects.requireNonNull(garbageMonitor);
                String countOldGarbage = garbageMonitor.mTrackedGarbage.countOldGarbage();
                if (countOldGarbage > 5) {
                    LeakReporter leakReporter = garbageMonitor.mLeakReporter;
                    Objects.requireNonNull(leakReporter);
                    String str3 = "LeakReporter";
                    try {
                        try {
                            File file5 = new File(leakReporter.mContext.getCacheDir(), "leak");
                            file5.mkdir();
                            file = new File(file5, "leak.hprof");
                            Debug.dumpHprofData(file.getAbsolutePath());
                            file2 = new File(file5, "leak.dump");
                            fileOutputStream = new FileOutputStream(file2);
                        } catch (IOException e2) {
                            e = e2;
                            countOldGarbage = str3;
                        }
                    } catch (IOException e3) {
                        e = e3;
                    }
                    try {
                        PrintWriter printWriter = new PrintWriter(fileOutputStream);
                        printWriter.print("Build: ");
                        printWriter.println(SystemProperties.get("ro.build.description"));
                        printWriter.println();
                        printWriter.flush();
                        LeakDetector leakDetector = leakReporter.mLeakDetector;
                        fileOutputStream.getFD();
                        Objects.requireNonNull(leakDetector);
                        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
                        indentingPrintWriter.println("SYSUI LEAK DETECTOR");
                        indentingPrintWriter.increaseIndent();
                        try {
                            try {
                                try {
                                    if (leakDetector.mTrackedCollections != null) {
                                        if (leakDetector.mTrackedGarbage != null) {
                                            try {
                                                indentingPrintWriter.println("TrackedCollections:");
                                                indentingPrintWriter.increaseIndent();
                                                leakDetector.mTrackedCollections.dump(indentingPrintWriter, LeakDetector$$ExternalSyntheticLambda0.INSTANCE);
                                                indentingPrintWriter.decreaseIndent();
                                                indentingPrintWriter.println();
                                                indentingPrintWriter.println("TrackedObjects:");
                                                indentingPrintWriter.increaseIndent();
                                                leakDetector.mTrackedCollections.dump(indentingPrintWriter, LeakDetector$$ExternalSyntheticLambda1.INSTANCE);
                                                indentingPrintWriter.decreaseIndent();
                                                indentingPrintWriter.println();
                                                indentingPrintWriter.print("TrackedGarbage:");
                                                indentingPrintWriter.increaseIndent();
                                                TrackedGarbage trackedGarbage = leakDetector.mTrackedGarbage;
                                                Objects.requireNonNull(trackedGarbage);
                                                synchronized (trackedGarbage) {
                                                    while (true) {
                                                        try {
                                                            Reference<? extends Object> poll = trackedGarbage.mRefQueue.poll();
                                                            if (poll != null) {
                                                                trackedGarbage.mGarbage.remove(poll);
                                                            } else {
                                                                long uptimeMillis = SystemClock.uptimeMillis();
                                                                ArrayMap arrayMap = new ArrayMap();
                                                                ArrayMap arrayMap2 = new ArrayMap();
                                                                Iterator<TrackedGarbage.LeakReference> it = trackedGarbage.mGarbage.iterator();
                                                                while (it.hasNext()) {
                                                                    TrackedGarbage.LeakReference next = it.next();
                                                                    try {
                                                                        Class<?> cls = next.clazz;
                                                                        arrayMap.put(cls, Integer.valueOf(((Integer) arrayMap.getOrDefault(cls, 0)).intValue() + 1));
                                                                        if (next.createdUptimeMillis + 60000 < uptimeMillis) {
                                                                            z = true;
                                                                        } else {
                                                                            z = false;
                                                                        }
                                                                        if (z) {
                                                                            Class<?> cls2 = next.clazz;
                                                                            arrayMap2.put(cls2, Integer.valueOf(((Integer) arrayMap2.getOrDefault(cls2, 0)).intValue() + 1));
                                                                        }
                                                                        file2 = file2;
                                                                        str3 = str3;
                                                                        it = it;
                                                                        file = file;
                                                                    } catch (Throwable th3) {
                                                                        th2 = th3;
                                                                        throw th2;
                                                                    }
                                                                }
                                                                str = str3;
                                                                file3 = file;
                                                                file4 = file2;
                                                                for (Map.Entry entry : arrayMap.entrySet()) {
                                                                    indentingPrintWriter.print(((Class) entry.getKey()).getName());
                                                                    indentingPrintWriter.print(": ");
                                                                    indentingPrintWriter.print(entry.getValue());
                                                                    indentingPrintWriter.print(" total, ");
                                                                    indentingPrintWriter.print(arrayMap2.getOrDefault(entry.getKey(), 0));
                                                                    indentingPrintWriter.print(" old");
                                                                    indentingPrintWriter.println();
                                                                }
                                                                indentingPrintWriter.decreaseIndent();
                                                                indentingPrintWriter.decreaseIndent();
                                                                indentingPrintWriter.println();
                                                                printWriter.close();
                                                                fileOutputStream.close();
                                                                NotificationManager notificationManager = (NotificationManager) leakReporter.mContext.getSystemService(NotificationManager.class);
                                                                NotificationChannel notificationChannel = new NotificationChannel("leak", "Leak Alerts", 4);
                                                                notificationChannel.enableVibration(true);
                                                                notificationManager.createNotificationChannel(notificationChannel);
                                                                notificationManager.notify(str, 0, new Notification.Builder(leakReporter.mContext, notificationChannel.getId()).setAutoCancel(true).setShowWhen(true).setContentTitle("Memory Leak Detected").setContentText(String.format("SystemUI has detected %d leaked objects. Tap to send", Integer.valueOf((int) countOldGarbage))).setSmallIcon(17303574).setContentIntent(PendingIntent.getActivityAsUser(leakReporter.mContext, 0, leakReporter.getIntent(file3, file4), 201326592, null, UserHandle.CURRENT)).build());
                                                                return;
                                                            }
                                                        } catch (Throwable th4) {
                                                            th2 = th4;
                                                        }
                                                    }
                                                }
                                            } catch (Throwable th5) {
                                                th = th5;
                                                str = str3;
                                                fileOutputStream.close();
                                                throw th;
                                            }
                                        }
                                    }
                                    fileOutputStream.close();
                                    NotificationManager notificationManager2 = (NotificationManager) leakReporter.mContext.getSystemService(NotificationManager.class);
                                    NotificationChannel notificationChannel2 = new NotificationChannel("leak", "Leak Alerts", 4);
                                    notificationChannel2.enableVibration(true);
                                    notificationManager2.createNotificationChannel(notificationChannel2);
                                    notificationManager2.notify(str, 0, new Notification.Builder(leakReporter.mContext, notificationChannel2.getId()).setAutoCancel(true).setShowWhen(true).setContentTitle("Memory Leak Detected").setContentText(String.format("SystemUI has detected %d leaked objects. Tap to send", Integer.valueOf((int) countOldGarbage))).setSmallIcon(17303574).setContentIntent(PendingIntent.getActivityAsUser(leakReporter.mContext, 0, leakReporter.getIntent(file3, file4), 201326592, null, UserHandle.CURRENT)).build());
                                    return;
                                } catch (IOException e4) {
                                    e = e4;
                                    countOldGarbage = str;
                                    Log.e(countOldGarbage, "Couldn't dump heap for leak", e);
                                    return;
                                }
                                printWriter.close();
                            } catch (Throwable th6) {
                                th = th6;
                                fileOutputStream.close();
                                throw th;
                            }
                            str = str3;
                            file3 = file;
                            file4 = file2;
                            indentingPrintWriter.println("disabled");
                            indentingPrintWriter.decreaseIndent();
                            indentingPrintWriter.println();
                        } catch (Throwable th7) {
                            th = th7;
                        }
                    } catch (Throwable th8) {
                        th = th8;
                    }
                } else {
                    return;
                }
            case 5:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                WifiEntry$$ExternalSyntheticLambda1 wifiEntry$$ExternalSyntheticLambda1 = new WifiEntry$$ExternalSyntheticLambda1(bubbleStackView, 5);
                bubbleStackView.mAnimateInFlyout = wifiEntry$$ExternalSyntheticLambda1;
                bubbleStackView.mFlyout.postDelayed(wifiEntry$$ExternalSyntheticLambda1, 200L);
                return;
            case FalsingManager.VERSION /* 6 */:
                DockObserver dockObserver = (DockObserver) this.f$0;
                String str4 = DockObserver.ACTION_START_DREAMLINER_CONTROL_SERVICE;
                Objects.requireNonNull(dockObserver);
                dockObserver.notifyDreamlinerLatestFanLevel();
                return;
            case 7:
                Gate gate = (Gate) this.f$0;
                Objects.requireNonNull(gate);
                Gate.Listener listener = gate.mListener;
                if (listener != null) {
                    listener.onGateChanged(gate);
                    return;
                }
                return;
        }
    }
}
