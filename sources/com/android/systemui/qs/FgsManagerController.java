package com.android.systemui.qs;

import android.app.IActivityManager;
import android.app.IForegroundServiceObserver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.ArrayMap;
import android.util.IndentingPrintWriter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.qs.FgsManagerController;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.util.DeviceConfigProxy;
import com.android.systemui.util.time.SystemClock;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.collections.EmptyList;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: FgsManagerController.kt */
/* loaded from: classes.dex */
public final class FgsManagerController extends IForegroundServiceObserver.Stub implements Dumpable {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final IActivityManager activityManager;
    public final Executor backgroundExecutor;
    public boolean changesSinceDialog;
    public final Context context;
    public final DeviceConfigProxy deviceConfigProxy;
    public SystemUIDialog dialog;
    public final DialogLaunchAnimator dialogLaunchAnimator;
    public final DumpManager dumpManager;
    public boolean initialized;
    public boolean isAvailable;
    public final Executor mainExecutor;
    public final PackageManager packageManager;
    public final SystemClock systemClock;
    public final Object lock = new Object();
    public final LinkedHashMap runningServiceTokens = new LinkedHashMap();
    public final AppListAdapter appListAdapter = new AppListAdapter();
    public ArrayMap<UserPackage, RunningApp> runningApps = new ArrayMap<>();
    public final LinkedHashSet onNumberOfPackagesChangedListeners = new LinkedHashSet();
    public final LinkedHashSet onDialogDismissedListeners = new LinkedHashSet();

    /* compiled from: FgsManagerController.kt */
    /* loaded from: classes.dex */
    public final class AppListAdapter extends RecyclerView.Adapter<AppItemViewHolder> {
        public final Object lock = new Object();
        public List<RunningApp> data = EmptyList.INSTANCE;

        public AppListAdapter() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final int getItemCount() {
            return this.data.size();
        }

        /* JADX WARN: Type inference failed for: r7v1, types: [T, java.lang.Object] */
        /* JADX WARN: Unknown variable types count: 1 */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onBindViewHolder(com.android.systemui.qs.FgsManagerController.AppItemViewHolder r6, int r7) {
            /*
                r5 = this;
                com.android.systemui.qs.FgsManagerController$AppItemViewHolder r6 = (com.android.systemui.qs.FgsManagerController.AppItemViewHolder) r6
                kotlin.jvm.internal.Ref$ObjectRef r0 = new kotlin.jvm.internal.Ref$ObjectRef
                r0.<init>()
                java.lang.Object r1 = r5.lock
                monitor-enter(r1)
                java.util.List<com.android.systemui.qs.FgsManagerController$RunningApp> r2 = r5.data     // Catch: all -> 0x00a3
                java.lang.Object r7 = r2.get(r7)     // Catch: all -> 0x00a3
                r0.element = r7     // Catch: all -> 0x00a3
                monitor-exit(r1)
                com.android.systemui.qs.FgsManagerController r5 = com.android.systemui.qs.FgsManagerController.this
                android.widget.ImageView r1 = r6.iconView
                com.android.systemui.qs.FgsManagerController$RunningApp r7 = (com.android.systemui.qs.FgsManagerController.RunningApp) r7
                java.util.Objects.requireNonNull(r7)
                android.graphics.drawable.Drawable r7 = r7.icon
                r1.setImageDrawable(r7)
                android.widget.TextView r7 = r6.appLabelView
                T r1 = r0.element
                com.android.systemui.qs.FgsManagerController$RunningApp r1 = (com.android.systemui.qs.FgsManagerController.RunningApp) r1
                java.util.Objects.requireNonNull(r1)
                java.lang.CharSequence r1 = r1.appLabel
                r7.setText(r1)
                android.widget.TextView r7 = r6.durationView
                com.android.systemui.util.time.SystemClock r1 = r5.systemClock
                long r1 = r1.elapsedRealtime()
                T r3 = r0.element
                com.android.systemui.qs.FgsManagerController$RunningApp r3 = (com.android.systemui.qs.FgsManagerController.RunningApp) r3
                java.util.Objects.requireNonNull(r3)
                long r3 = r3.timeStarted
                long r1 = r1 - r3
                r3 = 60000(0xea60, double:2.9644E-319)
                long r1 = java.lang.Math.max(r1, r3)
                r3 = 20
                java.lang.CharSequence r1 = android.text.format.DateUtils.formatDuration(r1, r3)
                r7.setText(r1)
                android.widget.Button r7 = r6.stopButton
                com.android.systemui.qs.FgsManagerController$AppListAdapter$onBindViewHolder$2$1 r1 = new com.android.systemui.qs.FgsManagerController$AppListAdapter$onBindViewHolder$2$1
                r1.<init>()
                r7.setOnClickListener(r1)
                T r5 = r0.element
                com.android.systemui.qs.FgsManagerController$RunningApp r5 = (com.android.systemui.qs.FgsManagerController.RunningApp) r5
                java.util.Objects.requireNonNull(r5)
                com.android.systemui.qs.FgsManagerController$UIControl r5 = r5.uiControl
                com.android.systemui.qs.FgsManagerController$UIControl r7 = com.android.systemui.qs.FgsManagerController.UIControl.HIDE_BUTTON
                if (r5 != r7) goto L_0x006e
                android.widget.Button r5 = r6.stopButton
                r7 = 4
                r5.setVisibility(r7)
            L_0x006e:
                T r5 = r0.element
                com.android.systemui.qs.FgsManagerController$RunningApp r5 = (com.android.systemui.qs.FgsManagerController.RunningApp) r5
                java.util.Objects.requireNonNull(r5)
                boolean r5 = r5.stopped
                r7 = 0
                if (r5 == 0) goto L_0x008f
                android.widget.Button r5 = r6.stopButton
                r5.setEnabled(r7)
                android.widget.Button r5 = r6.stopButton
                r7 = 2131952361(0x7f1302e9, float:1.9541163E38)
                r5.setText(r7)
                android.widget.TextView r5 = r6.durationView
                r6 = 8
                r5.setVisibility(r6)
                goto L_0x00a2
            L_0x008f:
                android.widget.Button r5 = r6.stopButton
                r0 = 1
                r5.setEnabled(r0)
                android.widget.Button r5 = r6.stopButton
                r0 = 2131952360(0x7f1302e8, float:1.954116E38)
                r5.setText(r0)
                android.widget.TextView r5 = r6.durationView
                r5.setVisibility(r7)
            L_0x00a2:
                return
            L_0x00a3:
                r5 = move-exception
                monitor-exit(r1)
                throw r5
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.FgsManagerController.AppListAdapter.onBindViewHolder(androidx.recyclerview.widget.RecyclerView$ViewHolder, int):void");
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public final RecyclerView.ViewHolder onCreateViewHolder(RecyclerView recyclerView, int i) {
            return new AppItemViewHolder(LayoutInflater.from(recyclerView.getContext()).inflate(2131624098, (ViewGroup) recyclerView, false));
        }
    }

    /* compiled from: FgsManagerController.kt */
    /* loaded from: classes.dex */
    public interface OnDialogDismissedListener {
        void onDialogDismissed();
    }

    /* compiled from: FgsManagerController.kt */
    /* loaded from: classes.dex */
    public interface OnNumberOfPackagesChangedListener {
        void onNumberOfPackagesChanged(int i);
    }

    /* compiled from: FgsManagerController.kt */
    /* loaded from: classes.dex */
    public static final class RunningApp {
        public CharSequence appLabel = "";
        public Drawable icon;
        public final String packageName;
        public boolean stopped;
        public final long timeStarted;
        public final UIControl uiControl;
        public final int userId;

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof RunningApp)) {
                return false;
            }
            RunningApp runningApp = (RunningApp) obj;
            return this.userId == runningApp.userId && Intrinsics.areEqual(this.packageName, runningApp.packageName) && this.timeStarted == runningApp.timeStarted && this.uiControl == runningApp.uiControl;
        }

        public final int hashCode() {
            int hashCode = this.packageName.hashCode();
            int hashCode2 = Long.hashCode(this.timeStarted);
            return this.uiControl.hashCode() + ((hashCode2 + ((hashCode + (Integer.hashCode(this.userId) * 31)) * 31)) * 31);
        }

        public final void dump(IndentingPrintWriter indentingPrintWriter, SystemClock systemClock) {
            indentingPrintWriter.println("RunningApp: [");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println(Intrinsics.stringPlus("userId=", Integer.valueOf(this.userId)));
            indentingPrintWriter.println(Intrinsics.stringPlus("packageName=", this.packageName));
            indentingPrintWriter.println("timeStarted=" + this.timeStarted + " (time since start = " + (systemClock.elapsedRealtime() - this.timeStarted) + "ms)\"");
            indentingPrintWriter.println(Intrinsics.stringPlus("uiControl=", this.uiControl));
            indentingPrintWriter.println(Intrinsics.stringPlus("appLabel=", this.appLabel));
            indentingPrintWriter.println(Intrinsics.stringPlus("icon=", this.icon));
            indentingPrintWriter.println(Intrinsics.stringPlus("stopped=", Boolean.valueOf(this.stopped)));
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("]");
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("RunningApp(userId=");
            m.append(this.userId);
            m.append(", packageName=");
            m.append(this.packageName);
            m.append(", timeStarted=");
            m.append(this.timeStarted);
            m.append(", uiControl=");
            m.append(this.uiControl);
            m.append(')');
            return m.toString();
        }

        public RunningApp(int i, String str, long j, UIControl uIControl) {
            this.userId = i;
            this.packageName = str;
            this.timeStarted = j;
            this.uiControl = uIControl;
        }
    }

    /* compiled from: FgsManagerController.kt */
    /* loaded from: classes.dex */
    public static final class StartTimeAndTokens {
        public final long startTime;
        public final SystemClock systemClock;
        public final LinkedHashSet tokens = new LinkedHashSet();

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return (obj instanceof StartTimeAndTokens) && Intrinsics.areEqual(this.systemClock, ((StartTimeAndTokens) obj).systemClock);
        }

        public final int hashCode() {
            return this.systemClock.hashCode();
        }

        public final void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println("StartTimeAndTokens: [");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println("startTime=" + this.startTime + " (time running = " + (this.systemClock.elapsedRealtime() - this.startTime) + "ms)");
            indentingPrintWriter.println("tokens: [");
            indentingPrintWriter.increaseIndent();
            for (IBinder iBinder : this.tokens) {
                indentingPrintWriter.println(String.valueOf(iBinder));
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("]");
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("]");
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("StartTimeAndTokens(systemClock=");
            m.append(this.systemClock);
            m.append(')');
            return m.toString();
        }

        public StartTimeAndTokens(SystemClock systemClock) {
            this.systemClock = systemClock;
            this.startTime = systemClock.elapsedRealtime();
        }
    }

    /* compiled from: FgsManagerController.kt */
    /* loaded from: classes.dex */
    public enum UIControl {
        NORMAL,
        HIDE_BUTTON,
        HIDE_ENTRY
    }

    /* compiled from: FgsManagerController.kt */
    /* loaded from: classes.dex */
    public final class UserPackage {
        public final String packageName;
        public final Lazy uiControl$delegate;
        public final int userId;

        public final int hashCode() {
            return Objects.hash(Integer.valueOf(this.userId), this.packageName);
        }

        public final void dump(IndentingPrintWriter indentingPrintWriter) {
            indentingPrintWriter.println("UserPackage: [");
            indentingPrintWriter.increaseIndent();
            indentingPrintWriter.println(Intrinsics.stringPlus("userId=", Integer.valueOf(this.userId)));
            indentingPrintWriter.println(Intrinsics.stringPlus("packageName=", this.packageName));
            indentingPrintWriter.println(Intrinsics.stringPlus("uiControl=", (UIControl) this.uiControl$delegate.getValue()));
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("]");
        }

        public final boolean equals(Object obj) {
            if (!(obj instanceof UserPackage)) {
                return false;
            }
            UserPackage userPackage = (UserPackage) obj;
            if (!Intrinsics.areEqual(userPackage.packageName, this.packageName) || userPackage.userId != this.userId) {
                return false;
            }
            return true;
        }

        public UserPackage(FgsManagerController fgsManagerController, int i, String str) {
            this.userId = i;
            this.packageName = str;
            this.uiControl$delegate = LazyKt__LazyJVMKt.lazy(new FgsManagerController$UserPackage$uiControl$2(fgsManagerController, this));
        }
    }

    /* compiled from: FgsManagerController.kt */
    /* loaded from: classes.dex */
    public static final class AppItemViewHolder extends RecyclerView.ViewHolder {
        public final TextView appLabelView;
        public final TextView durationView;
        public final ImageView iconView;
        public final Button stopButton;

        public AppItemViewHolder(View view) {
            super(view);
            this.appLabelView = (TextView) view.requireViewById(2131427963);
            this.durationView = (TextView) view.requireViewById(2131427961);
            this.iconView = (ImageView) view.requireViewById(2131427962);
            this.stopButton = (Button) view.requireViewById(2131427964);
        }
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter);
        synchronized (this.lock) {
            indentingPrintWriter.println(Intrinsics.stringPlus("changesSinceDialog=", Boolean.valueOf(this.changesSinceDialog)));
            indentingPrintWriter.println("Running service tokens: [");
            indentingPrintWriter.increaseIndent();
            for (Map.Entry entry : this.runningServiceTokens.entrySet()) {
                indentingPrintWriter.println("{");
                indentingPrintWriter.increaseIndent();
                ((UserPackage) entry.getKey()).dump(indentingPrintWriter);
                ((StartTimeAndTokens) entry.getValue()).dump(indentingPrintWriter);
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println("}");
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("]");
            indentingPrintWriter.println("Loaded package UI info: [");
            indentingPrintWriter.increaseIndent();
            for (Map.Entry<UserPackage, RunningApp> entry2 : this.runningApps.entrySet()) {
                indentingPrintWriter.println("{");
                entry2.getKey().dump(indentingPrintWriter);
                entry2.getValue().dump(indentingPrintWriter, this.systemClock);
                indentingPrintWriter.println("}");
            }
            indentingPrintWriter.decreaseIndent();
            indentingPrintWriter.println("]");
        }
    }

    public final int getNumRunningPackages() {
        int numRunningPackagesLocked;
        synchronized (this.lock) {
            numRunningPackagesLocked = getNumRunningPackagesLocked();
        }
        return numRunningPackagesLocked;
    }

    public final int getNumRunningPackagesLocked() {
        boolean z;
        Set<UserPackage> keySet = this.runningServiceTokens.keySet();
        if ((keySet instanceof Collection) && keySet.isEmpty()) {
            return 0;
        }
        int i = 0;
        for (UserPackage userPackage : keySet) {
            Objects.requireNonNull(userPackage);
            if (((UIControl) userPackage.uiControl$delegate.getValue()) != UIControl.HIDE_ENTRY) {
                z = true;
            } else {
                z = false;
            }
            if (z && (i = i + 1) < 0) {
                throw new ArithmeticException("Count overflow has happened.");
            }
        }
        return i;
    }

    public final void onForegroundStateChanged(IBinder iBinder, String str, int i, boolean z) {
        synchronized (this.lock) {
            int numRunningPackagesLocked = getNumRunningPackagesLocked();
            UserPackage userPackage = new UserPackage(this, i, str);
            if (z) {
                LinkedHashMap linkedHashMap = this.runningServiceTokens;
                Object obj = linkedHashMap.get(userPackage);
                if (obj == null) {
                    obj = new StartTimeAndTokens(this.systemClock);
                    linkedHashMap.put(userPackage, obj);
                }
                ((StartTimeAndTokens) obj).tokens.add(iBinder);
            } else {
                StartTimeAndTokens startTimeAndTokens = (StartTimeAndTokens) this.runningServiceTokens.get(userPackage);
                boolean z2 = false;
                if (startTimeAndTokens != null) {
                    startTimeAndTokens.tokens.remove(iBinder);
                    if (startTimeAndTokens.tokens.isEmpty()) {
                        z2 = true;
                    }
                }
                if (z2) {
                    this.runningServiceTokens.remove(userPackage);
                }
            }
            final int numRunningPackagesLocked2 = getNumRunningPackagesLocked();
            if (numRunningPackagesLocked2 != numRunningPackagesLocked) {
                this.changesSinceDialog = true;
                for (final OnNumberOfPackagesChangedListener onNumberOfPackagesChangedListener : this.onNumberOfPackagesChangedListeners) {
                    this.backgroundExecutor.execute(new Runnable() { // from class: com.android.systemui.qs.FgsManagerController$onForegroundStateChanged$1$3$1
                        @Override // java.lang.Runnable
                        public final void run() {
                            FgsManagerController.OnNumberOfPackagesChangedListener.this.onNumberOfPackagesChanged(numRunningPackagesLocked2);
                        }
                    });
                }
            }
            updateAppItemsLocked();
        }
    }

    public final void updateAppItemsLocked() {
        boolean z;
        if (this.dialog == null) {
            this.runningApps.clear();
            return;
        }
        Set keySet = this.runningServiceTokens.keySet();
        ArrayList arrayList = new ArrayList();
        Iterator it = keySet.iterator();
        while (true) {
            boolean z2 = false;
            if (!it.hasNext()) {
                break;
            }
            Object next = it.next();
            UserPackage userPackage = (UserPackage) next;
            Objects.requireNonNull(userPackage);
            if (((UIControl) userPackage.uiControl$delegate.getValue()) != UIControl.HIDE_ENTRY) {
                RunningApp runningApp = this.runningApps.get(userPackage);
                if (runningApp != null && runningApp.stopped) {
                    z = true;
                } else {
                    z = false;
                }
                if (!z) {
                    z2 = true;
                }
            }
            if (z2) {
                arrayList.add(next);
            }
        }
        Set<UserPackage> keySet2 = this.runningApps.keySet();
        ArrayList arrayList2 = new ArrayList();
        for (Object obj : keySet2) {
            if (!this.runningServiceTokens.containsKey((UserPackage) obj)) {
                arrayList2.add(obj);
            }
        }
        Iterator it2 = arrayList.iterator();
        while (it2.hasNext()) {
            UserPackage userPackage2 = (UserPackage) it2.next();
            PackageManager packageManager = this.packageManager;
            Objects.requireNonNull(userPackage2);
            ApplicationInfo applicationInfoAsUser = packageManager.getApplicationInfoAsUser(userPackage2.packageName, 0, userPackage2.userId);
            ArrayMap<UserPackage, RunningApp> arrayMap = this.runningApps;
            int i = userPackage2.userId;
            String str = userPackage2.packageName;
            Object obj2 = this.runningServiceTokens.get(userPackage2);
            Intrinsics.checkNotNull(obj2);
            long j = ((StartTimeAndTokens) obj2).startTime;
            UIControl uIControl = (UIControl) userPackage2.uiControl$delegate.getValue();
            CharSequence loadLabel = applicationInfoAsUser.loadLabel(this.packageManager);
            Drawable loadIcon = applicationInfoAsUser.loadIcon(this.packageManager);
            RunningApp runningApp2 = new RunningApp(i, str, j, uIControl);
            runningApp2.appLabel = loadLabel;
            runningApp2.icon = loadIcon;
            arrayMap.put(userPackage2, runningApp2);
        }
        Iterator it3 = arrayList2.iterator();
        while (it3.hasNext()) {
            UserPackage userPackage3 = (UserPackage) it3.next();
            RunningApp runningApp3 = this.runningApps.get(userPackage3);
            Intrinsics.checkNotNull(runningApp3);
            RunningApp runningApp4 = runningApp3;
            RunningApp runningApp5 = new RunningApp(runningApp4.userId, runningApp4.packageName, runningApp4.timeStarted, runningApp4.uiControl);
            runningApp5.stopped = true;
            runningApp5.appLabel = runningApp4.appLabel;
            runningApp5.icon = runningApp4.icon;
            this.runningApps.put(userPackage3, runningApp5);
        }
        this.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.qs.FgsManagerController$updateAppItemsLocked$3
            /* JADX WARN: Code restructure failed: missing block: B:20:0x00bf, code lost:
                if (r7[(r15 + 1) + r8] > r7[(r15 - 1) + r8]) goto L_0x00d0;
             */
            /* JADX WARN: Removed duplicated region for block: B:40:0x0106  */
            /* JADX WARN: Removed duplicated region for block: B:47:0x0128  */
            /* JADX WARN: Type inference failed for: r3v0, types: [java.util.List<com.android.systemui.qs.FgsManagerController$RunningApp>, T] */
            @Override // java.lang.Runnable
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final void run() {
                /*
                    Method dump skipped, instructions count: 909
                    To view this dump add '--comments-level debug' option
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.FgsManagerController$updateAppItemsLocked$3.run():void");
            }
        });
    }

    public FgsManagerController(Context context, Executor executor, Executor executor2, SystemClock systemClock, IActivityManager iActivityManager, PackageManager packageManager, DeviceConfigProxy deviceConfigProxy, DialogLaunchAnimator dialogLaunchAnimator, DumpManager dumpManager) {
        this.context = context;
        this.mainExecutor = executor;
        this.backgroundExecutor = executor2;
        this.systemClock = systemClock;
        this.activityManager = iActivityManager;
        this.packageManager = packageManager;
        this.deviceConfigProxy = deviceConfigProxy;
        this.dialogLaunchAnimator = dialogLaunchAnimator;
        this.dumpManager = dumpManager;
    }
}
