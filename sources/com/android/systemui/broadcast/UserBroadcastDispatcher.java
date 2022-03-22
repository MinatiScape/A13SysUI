package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArrayMap;
import android.util.ArraySet;
import android.util.IndentingPrintWriter;
import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.android.internal.util.Preconditions;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.EmptySequence;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
/* compiled from: UserBroadcastDispatcher.kt */
/* loaded from: classes.dex */
public final class UserBroadcastDispatcher implements Dumpable {
    public final Executor bgExecutor;
    public final UserBroadcastDispatcher$bgHandler$1 bgHandler;
    public final Context context;
    public final BroadcastDispatcherLogger logger;
    public final int userId;
    public final ArrayMap<Pair<String, Integer>, ActionReceiver> actionsToActionsReceivers = new ArrayMap<>();
    public final ArrayMap<BroadcastReceiver, Set<String>> receiverToActions = new ArrayMap<>();

    public static /* synthetic */ void getActionsToActionsReceivers$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    static {
        new AtomicInteger(0);
    }

    public ActionReceiver createActionReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core(String str, int i) {
        return new ActionReceiver(str, this.userId, new UserBroadcastDispatcher$createActionReceiver$1(this, i), new UserBroadcastDispatcher$createActionReceiver$2(this, str), this.bgExecutor, this.logger);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        boolean z;
        boolean z2 = printWriter instanceof IndentingPrintWriter;
        if (z2) {
            ((IndentingPrintWriter) printWriter).increaseIndent();
        }
        for (Map.Entry<Pair<String, Integer>, ActionReceiver> entry : this.actionsToActionsReceivers.entrySet()) {
            Pair<String, Integer> key = entry.getKey();
            ActionReceiver value = entry.getValue();
            StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('(');
            m.append(key.getFirst());
            m.append(": ");
            int intValue = key.getSecond().intValue();
            StringBuilder sb = new StringBuilder("");
            if ((intValue & 1) != 0) {
                sb.append("instant_apps,");
            }
            if ((intValue & 4) != 0) {
                sb.append("not_exported,");
            }
            if ((intValue & 2) != 0) {
                sb.append("exported");
            }
            if (sb.length() == 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                sb.append(intValue);
            }
            m.append(sb.toString());
            m.append("):");
            printWriter.println(m.toString());
            Objects.requireNonNull(value);
            boolean z3 = printWriter instanceof IndentingPrintWriter;
            if (z3) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            printWriter.println(Intrinsics.stringPlus("Registered: ", Boolean.valueOf(value.registered)));
            printWriter.println("Receivers:");
            if (z3) {
                ((IndentingPrintWriter) printWriter).increaseIndent();
            }
            Iterator<ReceiverData> it = value.receiverDatas.iterator();
            while (it.hasNext()) {
                ReceiverData next = it.next();
                Objects.requireNonNull(next);
                printWriter.println(next.receiver);
            }
            if (z3) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
            printWriter.println(Intrinsics.stringPlus("Categories: ", CollectionsKt___CollectionsKt.joinToString$default(value.activeCategories, ", ", null, null, null, 62)));
            if (z3) {
                ((IndentingPrintWriter) printWriter).decreaseIndent();
            }
        }
        if (z2) {
            ((IndentingPrintWriter) printWriter).decreaseIndent();
        }
    }

    public final boolean isReceiverReferenceHeld$frameworks__base__packages__SystemUI__android_common__SystemUI_core(BroadcastReceiver broadcastReceiver) {
        boolean z;
        boolean z2;
        Collection<ActionReceiver> values = this.actionsToActionsReceivers.values();
        if (!values.isEmpty()) {
            for (ActionReceiver actionReceiver : values) {
                Objects.requireNonNull(actionReceiver);
                ArraySet<ReceiverData> arraySet = actionReceiver.receiverDatas;
                if (!(arraySet instanceof Collection) || !arraySet.isEmpty()) {
                    Iterator<ReceiverData> it = arraySet.iterator();
                    while (it.hasNext()) {
                        ReceiverData next = it.next();
                        Objects.requireNonNull(next);
                        if (Intrinsics.areEqual(next.receiver, broadcastReceiver)) {
                            z2 = true;
                            continue;
                            break;
                        }
                    }
                }
                z2 = false;
                continue;
                if (z2) {
                    z = true;
                    break;
                }
            }
        }
        z = false;
        if (z || this.receiverToActions.containsKey(broadcastReceiver)) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.broadcast.UserBroadcastDispatcher$bgHandler$1] */
    public UserBroadcastDispatcher(Context context, int i, final Looper looper, Executor executor, BroadcastDispatcherLogger broadcastDispatcherLogger) {
        this.context = context;
        this.userId = i;
        this.bgExecutor = executor;
        this.logger = broadcastDispatcherLogger;
        this.bgHandler = new Handler(looper) { // from class: com.android.systemui.broadcast.UserBroadcastDispatcher$bgHandler$1
            @Override // android.os.Handler
            public final void handleMessage(Message message) {
                Sequence sequence;
                Sequence sequence2;
                int i2 = message.what;
                if (i2 == 0) {
                    UserBroadcastDispatcher userBroadcastDispatcher = UserBroadcastDispatcher.this;
                    Object obj = message.obj;
                    Objects.requireNonNull(obj, "null cannot be cast to non-null type com.android.systemui.broadcast.ReceiverData");
                    ReceiverData receiverData = (ReceiverData) obj;
                    int i3 = message.arg1;
                    Objects.requireNonNull(userBroadcastDispatcher);
                    Preconditions.checkState(userBroadcastDispatcher.bgHandler.getLooper().isCurrentThread(), "This method should only be called from BG thread");
                    ArrayMap<BroadcastReceiver, Set<String>> arrayMap = userBroadcastDispatcher.receiverToActions;
                    BroadcastReceiver broadcastReceiver = receiverData.receiver;
                    Set<String> set = arrayMap.get(broadcastReceiver);
                    if (set == null) {
                        set = new ArraySet<>();
                        arrayMap.put(broadcastReceiver, set);
                    }
                    Set<String> set2 = set;
                    Iterator<String> actionsIterator = receiverData.filter.actionsIterator();
                    if (actionsIterator == null) {
                        sequence = null;
                    } else {
                        sequence = SequencesKt__SequencesKt.asSequence(actionsIterator);
                    }
                    if (sequence == null) {
                        sequence = EmptySequence.INSTANCE;
                    }
                    CollectionsKt__ReversedViewsKt.addAll(set2, sequence);
                    Iterator<String> actionsIterator2 = receiverData.filter.actionsIterator();
                    while (actionsIterator2.hasNext()) {
                        String next = actionsIterator2.next();
                        ArrayMap<Pair<String, Integer>, ActionReceiver> arrayMap2 = userBroadcastDispatcher.actionsToActionsReceivers;
                        Pair<String, Integer> pair = new Pair<>(next, Integer.valueOf(i3));
                        ActionReceiver actionReceiver = arrayMap2.get(pair);
                        if (actionReceiver == null) {
                            actionReceiver = userBroadcastDispatcher.createActionReceiver$frameworks__base__packages__SystemUI__android_common__SystemUI_core(next, i3);
                            arrayMap2.put(pair, actionReceiver);
                        }
                        ActionReceiver actionReceiver2 = actionReceiver;
                        Objects.requireNonNull(actionReceiver2);
                        if (receiverData.filter.hasAction(actionReceiver2.action)) {
                            ArraySet<String> arraySet = actionReceiver2.activeCategories;
                            Iterator<String> categoriesIterator = receiverData.filter.categoriesIterator();
                            if (categoriesIterator == null) {
                                sequence2 = null;
                            } else {
                                sequence2 = SequencesKt__SequencesKt.asSequence(categoriesIterator);
                            }
                            if (sequence2 == null) {
                                sequence2 = EmptySequence.INSTANCE;
                            }
                            boolean addAll = CollectionsKt__ReversedViewsKt.addAll(arraySet, sequence2);
                            if (actionReceiver2.receiverDatas.add(receiverData) && actionReceiver2.receiverDatas.size() == 1) {
                                actionReceiver2.registerAction.invoke(actionReceiver2, actionReceiver2.createFilter());
                                actionReceiver2.registered = true;
                            } else if (addAll) {
                                actionReceiver2.unregisterAction.invoke(actionReceiver2);
                                actionReceiver2.registerAction.invoke(actionReceiver2, actionReceiver2.createFilter());
                            }
                        } else {
                            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Trying to attach to ");
                            m.append(actionReceiver2.action);
                            m.append(" without correct action,receiver: ");
                            m.append(receiverData.receiver);
                            throw new IllegalArgumentException(m.toString());
                        }
                    }
                    userBroadcastDispatcher.logger.logReceiverRegistered(userBroadcastDispatcher.userId, receiverData.receiver, i3);
                } else if (i2 == 1) {
                    UserBroadcastDispatcher userBroadcastDispatcher2 = UserBroadcastDispatcher.this;
                    Object obj2 = message.obj;
                    Objects.requireNonNull(obj2, "null cannot be cast to non-null type android.content.BroadcastReceiver");
                    BroadcastReceiver broadcastReceiver2 = (BroadcastReceiver) obj2;
                    Objects.requireNonNull(userBroadcastDispatcher2);
                    Preconditions.checkState(userBroadcastDispatcher2.bgHandler.getLooper().isCurrentThread(), "This method should only be called from BG thread");
                    for (String str : userBroadcastDispatcher2.receiverToActions.getOrDefault(broadcastReceiver2, new LinkedHashSet())) {
                        for (Map.Entry<Pair<String, Integer>, ActionReceiver> entry : userBroadcastDispatcher2.actionsToActionsReceivers.entrySet()) {
                            ActionReceiver value = entry.getValue();
                            if (Intrinsics.areEqual(entry.getKey().getFirst(), str)) {
                                Objects.requireNonNull(value);
                                if (CollectionsKt__ReversedViewsKt.filterInPlace$CollectionsKt__MutableCollectionsKt(value.receiverDatas, new ActionReceiver$removeReceiver$1(broadcastReceiver2)) && value.receiverDatas.isEmpty() && value.registered) {
                                    value.unregisterAction.invoke(value);
                                    value.registered = false;
                                    value.activeCategories.clear();
                                }
                            }
                        }
                    }
                    userBroadcastDispatcher2.receiverToActions.remove(broadcastReceiver2);
                    userBroadcastDispatcher2.logger.logReceiverUnregistered(userBroadcastDispatcher2.userId, broadcastReceiver2);
                }
            }
        };
    }
}
