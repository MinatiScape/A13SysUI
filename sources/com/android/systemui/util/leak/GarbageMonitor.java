package com.android.systemui.util.leak;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.os.Process;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.util.LongSparseArray;
import android.view.View;
import androidx.core.content.FileProvider;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.CoreStartable;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSIconViewImpl;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.util.concurrency.MessageRouter;
import com.android.systemui.util.leak.GarbageMonitor;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda0;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda20;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class GarbageMonitor implements Dumpable {
    public static final boolean DEBUG;
    public static final boolean ENABLE_AM_HEAP_LIMIT;
    public static final boolean HEAP_TRACKING_ENABLED;
    public static final boolean LEAK_REPORTING_ENABLED;
    public final Context mContext;
    public final DelayableExecutor mDelayableExecutor;
    public final DumpTruck mDumpTruck;
    public long mHeapLimit;
    public final LeakReporter mLeakReporter;
    public final MessageRouter mMessageRouter;
    public MemoryTile mQSTile;
    public final TrackedGarbage mTrackedGarbage;
    public final LongSparseArray<ProcessMemInfo> mData = new LongSparseArray<>();
    public final ArrayList<Long> mPids = new ArrayList<>();

    /* loaded from: classes.dex */
    public static class MemoryGraphIcon extends QSTile.Icon {
        public long limit;
        public long rss;

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public final Drawable getDrawable(Context context) {
            MemoryIconDrawable memoryIconDrawable = new MemoryIconDrawable(context);
            long j = this.rss;
            if (j != memoryIconDrawable.rss) {
                memoryIconDrawable.rss = j;
                memoryIconDrawable.invalidateSelf();
            }
            long j2 = this.limit;
            if (j2 != memoryIconDrawable.limit) {
                memoryIconDrawable.limit = j2;
                memoryIconDrawable.invalidateSelf();
            }
            return memoryIconDrawable;
        }

        public MemoryGraphIcon(int i) {
        }
    }

    /* loaded from: classes.dex */
    public static class MemoryIconDrawable extends Drawable {
        public final Drawable baseIcon;
        public final float dp;
        public long limit;
        public final Paint paint;
        public long rss;

        @Override // android.graphics.drawable.Drawable
        public final int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public final void draw(Canvas canvas) {
            this.baseIcon.draw(canvas);
            long j = this.limit;
            if (j > 0) {
                long j2 = this.rss;
                if (j2 > 0) {
                    float min = Math.min(1.0f, ((float) j2) / ((float) j));
                    Rect bounds = getBounds();
                    float f = this.dp;
                    canvas.translate((f * 8.0f) + bounds.left, (f * 5.0f) + bounds.top);
                    float f2 = this.dp;
                    canvas.drawRect(0.0f, (1.0f - min) * f2 * 14.0f, (8.0f * f2) + 1.0f, (f2 * 14.0f) + 1.0f, this.paint);
                }
            }
        }

        @Override // android.graphics.drawable.Drawable
        public final int getIntrinsicHeight() {
            return this.baseIcon.getIntrinsicHeight();
        }

        @Override // android.graphics.drawable.Drawable
        public final int getIntrinsicWidth() {
            return this.baseIcon.getIntrinsicWidth();
        }

        @Override // android.graphics.drawable.Drawable
        public final void setAlpha(int i) {
            this.baseIcon.setAlpha(i);
        }

        @Override // android.graphics.drawable.Drawable
        public final void setColorFilter(ColorFilter colorFilter) {
            this.baseIcon.setColorFilter(colorFilter);
            this.paint.setColorFilter(colorFilter);
        }

        public MemoryIconDrawable(Context context) {
            Paint paint = new Paint();
            this.paint = paint;
            this.baseIcon = context.getDrawable(2131232051).mutate();
            this.dp = context.getResources().getDisplayMetrics().density;
            paint.setColor(QSIconViewImpl.getIconColorForState(context, 2));
        }

        @Override // android.graphics.drawable.Drawable
        public final void setBounds(int i, int i2, int i3, int i4) {
            super.setBounds(i, i2, i3, i4);
            this.baseIcon.setBounds(i, i2, i3, i4);
        }

        @Override // android.graphics.drawable.Drawable
        public final void setTint(int i) {
            super.setTint(i);
            this.baseIcon.setTint(i);
        }

        @Override // android.graphics.drawable.Drawable
        public final void setTintList(ColorStateList colorStateList) {
            super.setTintList(colorStateList);
            this.baseIcon.setTintList(colorStateList);
        }

        @Override // android.graphics.drawable.Drawable
        public final void setTintMode(PorterDuff.Mode mode) {
            super.setTintMode(mode);
            this.baseIcon.setTintMode(mode);
        }
    }

    /* loaded from: classes.dex */
    public static class MemoryTile extends QSTileImpl<QSTile.State> {
        public boolean dumpInProgress;
        public final GarbageMonitor gm;
        public ProcessMemInfo pmi;

        /* renamed from: com.android.systemui.util.leak.GarbageMonitor$MemoryTile$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public class AnonymousClass1 extends Thread {
            public static final /* synthetic */ int $r8$clinit = 0;

            public AnonymousClass1() {
                super("HeapDumpThread");
            }

            @Override // java.lang.Thread, java.lang.Runnable
            public final void run() {
                int i;
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException unused) {
                }
                GarbageMonitor garbageMonitor = MemoryTile.this.gm;
                boolean z = GarbageMonitor.LEAK_REPORTING_ENABLED;
                Objects.requireNonNull(garbageMonitor);
                DumpTruck dumpTruck = garbageMonitor.mDumpTruck;
                ArrayList<Long> arrayList = garbageMonitor.mPids;
                Objects.requireNonNull(dumpTruck);
                File file = new File(dumpTruck.context.getCacheDir(), "leak");
                file.mkdirs();
                dumpTruck.hprofUri = null;
                dumpTruck.body.setLength(0);
                StringBuilder sb = dumpTruck.body;
                sb.append("Build: ");
                sb.append(Build.DISPLAY);
                sb.append("\n\nProcesses:\n");
                ArrayList arrayList2 = new ArrayList();
                int myPid = Process.myPid();
                Iterator<Long> it = arrayList.iterator();
                while (it.hasNext()) {
                    int intValue = it.next().intValue();
                    StringBuilder sb2 = dumpTruck.body;
                    sb2.append("  pid ");
                    sb2.append(intValue);
                    GarbageMonitor garbageMonitor2 = dumpTruck.mGarbageMonitor;
                    Objects.requireNonNull(garbageMonitor2);
                    ProcessMemInfo processMemInfo = garbageMonitor2.mData.get(intValue);
                    if (processMemInfo != null) {
                        StringBuilder sb3 = dumpTruck.body;
                        sb3.append(":");
                        sb3.append(" up=");
                        sb3.append(System.currentTimeMillis() - processMemInfo.startTime);
                        sb3.append(" rss=");
                        sb3.append(processMemInfo.currentRss);
                        dumpTruck.rss = processMemInfo.currentRss;
                        i = myPid;
                    } else {
                        i = myPid;
                    }
                    if (intValue == i) {
                        String path = new File(file, String.format("heap-%d.ahprof", Integer.valueOf(intValue))).getPath();
                        Log.v("DumpTruck", "Dumping memory info for process " + intValue + " to " + path);
                        try {
                            Debug.dumpHprofData(path);
                            arrayList2.add(path);
                            dumpTruck.body.append(" (hprof attached)");
                        } catch (IOException e) {
                            Log.e("DumpTruck", "error dumping memory:", e);
                            StringBuilder sb4 = dumpTruck.body;
                            sb4.append("\n** Could not dump heap: \n");
                            sb4.append(e.toString());
                            sb4.append("\n");
                        }
                    }
                    dumpTruck.body.append("\n");
                    myPid = i;
                }
                try {
                    String canonicalPath = new File(file, String.format("hprof-%d.zip", Long.valueOf(System.currentTimeMillis()))).getCanonicalPath();
                    if (DumpTruck.zipUp(canonicalPath, arrayList2)) {
                        dumpTruck.hprofUri = FileProvider.getPathStrategy(dumpTruck.context, "com.android.systemui.fileprovider").getUriForFile(new File(canonicalPath));
                        Log.v("DumpTruck", "Heap dump accessible at URI: " + dumpTruck.hprofUri);
                    }
                } catch (IOException e2) {
                    Log.e("DumpTruck", "unable to zip up heapdumps", e2);
                    StringBuilder sb5 = dumpTruck.body;
                    sb5.append("\n** Could not zip up files: \n");
                    sb5.append(e2.toString());
                    sb5.append("\n");
                }
                Intent intent = new Intent("android.intent.action.SEND_MULTIPLE");
                intent.addFlags(268435456);
                intent.addFlags(1);
                intent.putExtra("android.intent.extra.SUBJECT", String.format("SystemUI memory dump (rss=%dM)", Long.valueOf(dumpTruck.rss / 1024)));
                intent.putExtra("android.intent.extra.TEXT", dumpTruck.body.toString());
                if (dumpTruck.hprofUri != null) {
                    ArrayList<? extends Parcelable> arrayList3 = new ArrayList<>();
                    arrayList3.add(dumpTruck.hprofUri);
                    intent.setType("application/zip");
                    intent.putParcelableArrayListExtra("android.intent.extra.STREAM", arrayList3);
                    intent.setClipData(new ClipData(new ClipDescription("content", new String[]{"text/plain"}), new ClipData.Item(dumpTruck.hprofUri)));
                    intent.addFlags(1);
                }
                MemoryTile.this.mHandler.post(new BubbleStackView$$ExternalSyntheticLambda20(this, intent, 3));
            }
        }

        @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
        public final int getMetricsCategory() {
            return 0;
        }

        @Override // com.android.systemui.qs.tileimpl.QSTileImpl
        public final Intent getLongClickIntent() {
            return new Intent();
        }

        @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
        public final CharSequence getTileLabel() {
            return this.mState.label;
        }

        @Override // com.android.systemui.qs.tileimpl.QSTileImpl
        public final void handleClick(View view) {
            if (!this.dumpInProgress) {
                this.dumpInProgress = true;
                refreshState(null);
                new AnonymousClass1().start();
            }
        }

        @Override // com.android.systemui.qs.tileimpl.QSTileImpl
        public final void handleUpdateState(QSTile.State state, Object obj) {
            int i;
            String str;
            GarbageMonitor garbageMonitor = this.gm;
            int myPid = Process.myPid();
            Objects.requireNonNull(garbageMonitor);
            this.pmi = garbageMonitor.mData.get(myPid);
            MemoryGraphIcon memoryGraphIcon = new MemoryGraphIcon(0);
            memoryGraphIcon.limit = this.gm.mHeapLimit;
            boolean z = this.dumpInProgress;
            if (z) {
                i = 0;
            } else {
                i = 2;
            }
            state.state = i;
            if (z) {
                str = "Dumping...";
            } else {
                str = this.mContext.getString(2131952433);
            }
            state.label = str;
            ProcessMemInfo processMemInfo = this.pmi;
            if (processMemInfo != null) {
                long j = processMemInfo.currentRss;
                memoryGraphIcon.rss = j;
                state.secondaryLabel = String.format("rss: %s / %s", GarbageMonitor.m147$$Nest$smformatBytes(j * 1024), GarbageMonitor.m147$$Nest$smformatBytes(this.gm.mHeapLimit * 1024));
            } else {
                memoryGraphIcon.rss = 0L;
                state.secondaryLabel = null;
            }
            state.icon = memoryGraphIcon;
        }

        @Override // com.android.systemui.qs.tileimpl.QSTileImpl
        public final QSTile.State newTileState() {
            return new QSTile.State();
        }

        public MemoryTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, GarbageMonitor garbageMonitor) {
            super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
            this.gm = garbageMonitor;
        }

        @Override // com.android.systemui.qs.tileimpl.QSTileImpl
        public final void handleSetListening(boolean z) {
            MemoryTile memoryTile;
            super.handleSetListening(z);
            GarbageMonitor garbageMonitor = this.gm;
            if (garbageMonitor != null) {
                if (z) {
                    memoryTile = this;
                } else {
                    memoryTile = null;
                }
                boolean z2 = GarbageMonitor.LEAK_REPORTING_ENABLED;
                Objects.requireNonNull(garbageMonitor);
                garbageMonitor.mQSTile = memoryTile;
                if (memoryTile != null) {
                    memoryTile.refreshState(null);
                }
            }
            ActivityManager activityManager = (ActivityManager) this.mContext.getSystemService(ActivityManager.class);
            if (z) {
                long j = this.gm.mHeapLimit;
                if (j > 0) {
                    activityManager.setWatchHeapLimit(j * 1024);
                    return;
                }
            }
            activityManager.clearWatchHeapLimit();
        }
    }

    /* loaded from: classes.dex */
    public static class Service extends CoreStartable {
        public final GarbageMonitor mGarbageMonitor;

        @Override // com.android.systemui.CoreStartable, com.android.systemui.Dumpable
        public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            GarbageMonitor garbageMonitor = this.mGarbageMonitor;
            if (garbageMonitor != null) {
                printWriter.println("GarbageMonitor params:");
                printWriter.println(String.format("   mHeapLimit=%d KB", Long.valueOf(garbageMonitor.mHeapLimit)));
                printWriter.println(String.format("   GARBAGE_INSPECTION_INTERVAL=%d (%.1f mins)", 900000L, Float.valueOf(15.0f)));
                printWriter.println(String.format("   HEAP_TRACK_INTERVAL=%d (%.1f mins)", 60000L, Float.valueOf(1.0f)));
                printWriter.println(String.format("   HEAP_TRACK_HISTORY_LEN=%d (%.1f hr total)", 720, Float.valueOf(12.0f)));
                printWriter.println("GarbageMonitor tracked processes:");
                Iterator<Long> it = garbageMonitor.mPids.iterator();
                while (it.hasNext()) {
                    ProcessMemInfo processMemInfo = garbageMonitor.mData.get(it.next().longValue());
                    if (processMemInfo != null) {
                        printWriter.print("{ \"pid\": ");
                        printWriter.print(processMemInfo.pid);
                        printWriter.print(", \"name\": \"");
                        printWriter.print(processMemInfo.name.replace('\"', '-'));
                        printWriter.print("\", \"start\": ");
                        printWriter.print(processMemInfo.startTime);
                        printWriter.print(", \"rss\": [");
                        for (int i = 0; i < processMemInfo.rss.length; i++) {
                            if (i > 0) {
                                printWriter.print(",");
                            }
                            long[] jArr = processMemInfo.rss;
                            printWriter.print(jArr[(processMemInfo.head + i) % jArr.length]);
                        }
                        printWriter.println("] }");
                    }
                }
            }
        }

        @Override // com.android.systemui.CoreStartable
        public final void start() {
            boolean z = false;
            if (Settings.Secure.getInt(this.mContext.getContentResolver(), "sysui_force_enable_leak_reporting", 0) != 0) {
                z = true;
            }
            if (GarbageMonitor.LEAK_REPORTING_ENABLED || z) {
                GarbageMonitor garbageMonitor = this.mGarbageMonitor;
                Objects.requireNonNull(garbageMonitor);
                if (garbageMonitor.mTrackedGarbage != null) {
                    garbageMonitor.mMessageRouter.sendMessage(1000);
                }
            }
            if (GarbageMonitor.HEAP_TRACKING_ENABLED || z) {
                GarbageMonitor garbageMonitor2 = this.mGarbageMonitor;
                Objects.requireNonNull(garbageMonitor2);
                long myPid = Process.myPid();
                String packageName = garbageMonitor2.mContext.getPackageName();
                long currentTimeMillis = System.currentTimeMillis();
                synchronized (garbageMonitor2.mPids) {
                    if (!garbageMonitor2.mPids.contains(Long.valueOf(myPid))) {
                        garbageMonitor2.mPids.add(Long.valueOf(myPid));
                        garbageMonitor2.logPids();
                        garbageMonitor2.mData.put(myPid, new ProcessMemInfo(myPid, packageName, currentTimeMillis));
                    }
                }
                garbageMonitor2.mMessageRouter.sendMessage(3000);
            }
        }

        public Service(Context context, GarbageMonitor garbageMonitor) {
            super(context);
            this.mGarbageMonitor = garbageMonitor;
        }
    }

    /* loaded from: classes.dex */
    public static class ProcessMemInfo implements Dumpable {
        public long currentRss;
        public String name;
        public long pid;
        public long startTime;
        public long[] rss = new long[720];
        public long max = 1;
        public int head = 0;

        public ProcessMemInfo(long j, String str, long j2) {
            this.pid = j;
            this.name = str;
            this.startTime = j2;
        }

        @Override // com.android.systemui.Dumpable
        public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            printWriter.print("{ \"pid\": ");
            printWriter.print(this.pid);
            printWriter.print(", \"name\": \"");
            printWriter.print(this.name.replace('\"', '-'));
            printWriter.print("\", \"start\": ");
            printWriter.print(this.startTime);
            printWriter.print(", \"rss\": [");
            for (int i = 0; i < this.rss.length; i++) {
                if (i > 0) {
                    printWriter.print(",");
                }
                long[] jArr = this.rss;
                printWriter.print(jArr[(this.head + i) % jArr.length]);
            }
            printWriter.println("] }");
        }
    }

    /* renamed from: -$$Nest$smformatBytes  reason: not valid java name */
    public static String m147$$Nest$smformatBytes(long j) {
        String[] strArr = {"B", "K", "M", "G", "T"};
        int i = 0;
        while (i < 5 && j >= 1024) {
            j /= 1024;
            i++;
        }
        return j + strArr[i];
    }

    static {
        boolean z;
        boolean z2 = true;
        if (!Build.IS_DEBUGGABLE || !SystemProperties.getBoolean("debug.enable_leak_reporting", false)) {
            z = false;
        } else {
            z = true;
        }
        LEAK_REPORTING_ENABLED = z;
        boolean z3 = Build.IS_DEBUGGABLE;
        HEAP_TRACKING_ENABLED = z3;
        if (!z3 || !SystemProperties.getBoolean("debug.enable_sysui_heap_limit", false)) {
            z2 = false;
        }
        ENABLE_AM_HEAP_LIMIT = z2;
        DEBUG = Log.isLoggable("GarbageMonitor", 3);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("GarbageMonitor params:");
        printWriter.println(String.format("   mHeapLimit=%d KB", Long.valueOf(this.mHeapLimit)));
        printWriter.println(String.format("   GARBAGE_INSPECTION_INTERVAL=%d (%.1f mins)", 900000L, Float.valueOf(15.0f)));
        printWriter.println(String.format("   HEAP_TRACK_INTERVAL=%d (%.1f mins)", 60000L, Float.valueOf(1.0f)));
        printWriter.println(String.format("   HEAP_TRACK_HISTORY_LEN=%d (%.1f hr total)", 720, Float.valueOf(12.0f)));
        printWriter.println("GarbageMonitor tracked processes:");
        Iterator<Long> it = this.mPids.iterator();
        while (it.hasNext()) {
            ProcessMemInfo processMemInfo = this.mData.get(it.next().longValue());
            if (processMemInfo != null) {
                printWriter.print("{ \"pid\": ");
                printWriter.print(processMemInfo.pid);
                printWriter.print(", \"name\": \"");
                printWriter.print(processMemInfo.name.replace('\"', '-'));
                printWriter.print("\", \"start\": ");
                printWriter.print(processMemInfo.startTime);
                printWriter.print(", \"rss\": [");
                for (int i = 0; i < processMemInfo.rss.length; i++) {
                    if (i > 0) {
                        printWriter.print(",");
                    }
                    long[] jArr = processMemInfo.rss;
                    printWriter.print(jArr[(processMemInfo.head + i) % jArr.length]);
                }
                printWriter.println("] }");
            }
        }
    }

    public final void logPids() {
        if (DEBUG) {
            StringBuffer stringBuffer = new StringBuffer("Now tracking processes: ");
            for (int i = 0; i < this.mPids.size(); i++) {
                this.mPids.get(i).intValue();
                stringBuffer.append(" ");
            }
            Log.v("GarbageMonitor", stringBuffer.toString());
        }
    }

    public GarbageMonitor(Context context, DelayableExecutor delayableExecutor, MessageRouter messageRouter, LeakDetector leakDetector, LeakReporter leakReporter, DumpManager dumpManager) {
        Context applicationContext = context.getApplicationContext();
        this.mContext = applicationContext;
        this.mDelayableExecutor = delayableExecutor;
        this.mMessageRouter = messageRouter;
        messageRouter.subscribeTo(1000, new MessageRouter.SimpleMessageListener() { // from class: com.android.systemui.util.leak.GarbageMonitor$$ExternalSyntheticLambda0
            @Override // com.android.systemui.util.concurrency.MessageRouter.SimpleMessageListener
            public final void onMessage() {
                boolean z;
                GarbageMonitor garbageMonitor = GarbageMonitor.this;
                Objects.requireNonNull(garbageMonitor);
                if (garbageMonitor.mTrackedGarbage.countOldGarbage() > 5) {
                    Runtime.getRuntime().gc();
                    z = true;
                } else {
                    z = false;
                }
                if (z) {
                    garbageMonitor.mDelayableExecutor.executeDelayed(new WifiEntry$$ExternalSyntheticLambda0(garbageMonitor, 4), 100L);
                }
                garbageMonitor.mMessageRouter.cancelMessages(1000);
                garbageMonitor.mMessageRouter.sendMessageDelayed(1000, 900000L);
            }
        });
        messageRouter.subscribeTo(3000, new MessageRouter.SimpleMessageListener() { // from class: com.android.systemui.util.leak.GarbageMonitor$$ExternalSyntheticLambda1
            @Override // com.android.systemui.util.concurrency.MessageRouter.SimpleMessageListener
            public final void onMessage() {
                GarbageMonitor garbageMonitor = GarbageMonitor.this;
                Objects.requireNonNull(garbageMonitor);
                synchronized (garbageMonitor.mPids) {
                    int i = 0;
                    while (true) {
                        if (i >= garbageMonitor.mPids.size()) {
                            break;
                        }
                        int intValue = garbageMonitor.mPids.get(i).intValue();
                        long[] rss = Process.getRss(intValue);
                        if (rss != null || rss.length != 0) {
                            long j = rss[0];
                            long j2 = intValue;
                            GarbageMonitor.ProcessMemInfo processMemInfo = garbageMonitor.mData.get(j2);
                            long[] jArr = processMemInfo.rss;
                            int i2 = processMemInfo.head;
                            processMemInfo.currentRss = j;
                            jArr[i2] = j;
                            processMemInfo.head = (i2 + 1) % jArr.length;
                            if (j > processMemInfo.max) {
                                processMemInfo.max = j;
                            }
                            if (j == 0) {
                                if (GarbageMonitor.DEBUG) {
                                    Log.v("GarbageMonitor", "update: pid " + intValue + " has rss=0, it probably died");
                                }
                                garbageMonitor.mData.remove(j2);
                            }
                            i++;
                        } else if (GarbageMonitor.DEBUG) {
                            Log.e("GarbageMonitor", "update: Process.getRss() didn't provide any values.");
                        }
                    }
                    int size = garbageMonitor.mPids.size();
                    while (true) {
                        size--;
                        if (size < 0) {
                            break;
                        }
                        if (garbageMonitor.mData.get(garbageMonitor.mPids.get(size).intValue()) == null) {
                            garbageMonitor.mPids.remove(size);
                            garbageMonitor.logPids();
                        }
                    }
                }
                GarbageMonitor.MemoryTile memoryTile = garbageMonitor.mQSTile;
                if (memoryTile != null) {
                    memoryTile.refreshState(null);
                }
                garbageMonitor.mMessageRouter.cancelMessages(3000);
                garbageMonitor.mMessageRouter.sendMessageDelayed(3000, 60000L);
            }
        });
        Objects.requireNonNull(leakDetector);
        this.mTrackedGarbage = leakDetector.mTrackedGarbage;
        this.mLeakReporter = leakReporter;
        this.mDumpTruck = new DumpTruck(applicationContext, this);
        dumpManager.registerDumpable("GarbageMonitor", this);
        if (ENABLE_AM_HEAP_LIMIT) {
            this.mHeapLimit = Settings.Global.getInt(context.getContentResolver(), "systemui_am_heap_limit", applicationContext.getResources().getInteger(2131493046));
        }
    }
}
