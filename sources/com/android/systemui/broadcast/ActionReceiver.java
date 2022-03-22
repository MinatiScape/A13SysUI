package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Unit;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ActionReceiver.kt */
/* loaded from: classes.dex */
public final class ActionReceiver extends BroadcastReceiver implements Dumpable {
    public static final AtomicInteger index = new AtomicInteger(0);
    public final String action;
    public final Executor bgExecutor;
    public final BroadcastDispatcherLogger logger;
    public final Function2<BroadcastReceiver, IntentFilter, Unit> registerAction;
    public boolean registered;
    public final Function1<BroadcastReceiver, Unit> unregisterAction;
    public final int userId;
    public final ArraySet<ReceiverData> receiverDatas = new ArraySet<>();
    public final ArraySet<String> activeCategories = new ArraySet<>();

    public final IntentFilter createFilter() {
        IntentFilter intentFilter = new IntentFilter(this.action);
        for (String str : this.activeCategories) {
            intentFilter.addCategory(str);
        }
        return intentFilter;
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        boolean z = printWriter instanceof IndentingPrintWriter;
        if (z) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        printWriter.println(Intrinsics.stringPlus("Registered: ", Boolean.valueOf(this.registered)));
        printWriter.println("Receivers:");
        if (z) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        Iterator<ReceiverData> it = this.receiverDatas.iterator();
        while (it.hasNext()) {
            ReceiverData next = it.next();
            Objects.requireNonNull(next);
            printWriter.println(next.receiver);
        }
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
        printWriter.println(Intrinsics.stringPlus("Categories: ", CollectionsKt___CollectionsKt.joinToString$default(this.activeCategories, ", ", null, null, null, 62)));
        if (z) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public ActionReceiver(String str, int i, Function2<? super BroadcastReceiver, ? super IntentFilter, Unit> function2, Function1<? super BroadcastReceiver, Unit> function1, Executor executor, BroadcastDispatcherLogger broadcastDispatcherLogger) {
        this.action = str;
        this.userId = i;
        this.registerAction = function2;
        this.unregisterAction = function1;
        this.bgExecutor = executor;
        this.logger = broadcastDispatcherLogger;
    }

    @Override // android.content.BroadcastReceiver
    public final void onReceive(final Context context, final Intent intent) throws IllegalStateException {
        if (Intrinsics.areEqual(intent.getAction(), this.action)) {
            final int andIncrement = index.getAndIncrement();
            this.logger.logBroadcastReceived(andIncrement, this.userId, intent);
            this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.broadcast.ActionReceiver$onReceive$1
                @Override // java.lang.Runnable
                public final void run() {
                    final ActionReceiver actionReceiver = ActionReceiver.this;
                    ArraySet<ReceiverData> arraySet = actionReceiver.receiverDatas;
                    final Intent intent2 = intent;
                    final Context context2 = context;
                    final int i = andIncrement;
                    for (final ReceiverData receiverData : arraySet) {
                        Objects.requireNonNull(receiverData);
                        if (receiverData.filter.matchCategories(intent2.getCategories()) == null) {
                            receiverData.executor.execute(new Runnable() { // from class: com.android.systemui.broadcast.ActionReceiver$onReceive$1$1$1
                                @Override // java.lang.Runnable
                                public final void run() {
                                    ReceiverData receiverData2 = ReceiverData.this;
                                    Objects.requireNonNull(receiverData2);
                                    receiverData2.receiver.setPendingResult(actionReceiver.getPendingResult());
                                    ReceiverData receiverData3 = ReceiverData.this;
                                    Objects.requireNonNull(receiverData3);
                                    receiverData3.receiver.onReceive(context2, intent2);
                                    ActionReceiver actionReceiver2 = actionReceiver;
                                    BroadcastDispatcherLogger broadcastDispatcherLogger = actionReceiver2.logger;
                                    int i2 = i;
                                    String str = actionReceiver2.action;
                                    ReceiverData receiverData4 = ReceiverData.this;
                                    Objects.requireNonNull(receiverData4);
                                    broadcastDispatcherLogger.logBroadcastDispatched(i2, str, receiverData4.receiver);
                                }
                            });
                        }
                    }
                }
            });
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Received intent for ");
        m.append((Object) intent.getAction());
        m.append(" in receiver for ");
        m.append(this.action);
        m.append('}');
        throw new IllegalStateException(m.toString());
    }
}
