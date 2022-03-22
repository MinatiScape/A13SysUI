package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerExecutor;
import android.os.Looper;
import android.os.Message;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.IndentingPrintWriter;
import android.util.SparseArray;
import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dumpable;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.settings.UserTracker;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.Pair;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BroadcastDispatcher.kt */
/* loaded from: classes.dex */
public final class BroadcastDispatcher implements Dumpable {
    public final Executor bgExecutor;
    public final Looper bgLooper;
    public final Context context;
    public final BroadcastDispatcher$handler$1 handler;
    public final BroadcastDispatcherLogger logger;
    public final SparseArray<UserBroadcastDispatcher> receiversByUser = new SparseArray<>(20);
    public final UserTracker userTracker;

    public final void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter) {
        registerReceiver$default(this, broadcastReceiver, intentFilter, null, null, 28);
    }

    public final void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle) {
        registerReceiver$default(this, broadcastReceiver, intentFilter, executor, userHandle, 16);
    }

    public final void registerReceiverWithHandler(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler) {
        registerReceiverWithHandler$default(this, broadcastReceiver, intentFilter, handler, null, 24);
    }

    public final void registerReceiverWithHandler(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler, UserHandle userHandle) {
        registerReceiverWithHandler$default(this, broadcastReceiver, intentFilter, handler, userHandle, 16);
    }

    public static /* synthetic */ void registerReceiver$default(BroadcastDispatcher broadcastDispatcher, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle, int i) {
        Executor executor2;
        UserHandle userHandle2;
        int i2;
        if ((i & 4) != 0) {
            executor2 = null;
        } else {
            executor2 = executor;
        }
        if ((i & 8) != 0) {
            userHandle2 = null;
        } else {
            userHandle2 = userHandle;
        }
        if ((i & 16) != 0) {
            i2 = 2;
        } else {
            i2 = 0;
        }
        broadcastDispatcher.registerReceiver(broadcastReceiver, intentFilter, executor2, userHandle2, i2);
    }

    public static void registerReceiverWithHandler$default(BroadcastDispatcher broadcastDispatcher, BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Handler handler, UserHandle userHandle, int i) {
        int i2;
        if ((i & 8) != 0) {
            userHandle = broadcastDispatcher.context.getUser();
        }
        if ((i & 16) != 0) {
            i2 = 2;
        } else {
            i2 = 0;
        }
        Objects.requireNonNull(broadcastDispatcher);
        broadcastDispatcher.registerReceiver(broadcastReceiver, intentFilter, new HandlerExecutor(handler), userHandle, i2);
    }

    @VisibleForTesting
    public UserBroadcastDispatcher createUBRForUser(int i) {
        return new UserBroadcastDispatcher(this.context, i, this.bgLooper, this.bgExecutor, this.logger);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        boolean z;
        printWriter.println("Broadcast dispatcher:");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.increaseIndent();
        int size = this.receiversByUser.size();
        int i = 0;
        while (i < size) {
            int i2 = i + 1;
            indentingPrintWriter.println(Intrinsics.stringPlus("User ", Integer.valueOf(this.receiversByUser.keyAt(i))));
            UserBroadcastDispatcher valueAt = this.receiversByUser.valueAt(i);
            Objects.requireNonNull(valueAt);
            indentingPrintWriter.increaseIndent();
            for (Map.Entry<Pair<String, Integer>, ActionReceiver> entry : valueAt.actionsToActionsReceivers.entrySet()) {
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
                indentingPrintWriter.println(m.toString());
                Objects.requireNonNull(value);
                indentingPrintWriter.increaseIndent();
                indentingPrintWriter.println(Intrinsics.stringPlus("Registered: ", Boolean.valueOf(value.registered)));
                indentingPrintWriter.println("Receivers:");
                indentingPrintWriter.increaseIndent();
                Iterator<ReceiverData> it = value.receiverDatas.iterator();
                while (it.hasNext()) {
                    ReceiverData next = it.next();
                    Objects.requireNonNull(next);
                    indentingPrintWriter.println(next.receiver);
                }
                indentingPrintWriter.decreaseIndent();
                indentingPrintWriter.println(Intrinsics.stringPlus("Categories: ", CollectionsKt___CollectionsKt.joinToString$default(value.activeCategories, ", ", null, null, null, 62)));
                indentingPrintWriter.decreaseIndent();
            }
            indentingPrintWriter.decreaseIndent();
            i = i2;
        }
        indentingPrintWriter.decreaseIndent();
    }

    public final void registerReceiver(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle, int i) {
        StringBuilder sb = new StringBuilder();
        if (intentFilter.countActions() == 0) {
            sb.append("Filter must contain at least one action. ");
        }
        if (intentFilter.countDataAuthorities() != 0) {
            sb.append("Filter cannot contain DataAuthorities. ");
        }
        if (intentFilter.countDataPaths() != 0) {
            sb.append("Filter cannot contain DataPaths. ");
        }
        if (intentFilter.countDataSchemes() != 0) {
            sb.append("Filter cannot contain DataSchemes. ");
        }
        if (intentFilter.countDataTypes() != 0) {
            sb.append("Filter cannot contain DataTypes. ");
        }
        if (intentFilter.getPriority() != 0) {
            sb.append("Filter cannot modify priority. ");
        }
        if (TextUtils.isEmpty(sb)) {
            if (executor == null) {
                executor = this.context.getMainExecutor();
            }
            if (userHandle == null) {
                userHandle = this.context.getUser();
            }
            obtainMessage(0, i, 0, new ReceiverData(broadcastReceiver, intentFilter, executor, userHandle)).sendToTarget();
            return;
        }
        throw new IllegalArgumentException(sb.toString());
    }

    public final void unregisterReceiver(BroadcastReceiver broadcastReceiver) {
        obtainMessage(1, broadcastReceiver).sendToTarget();
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.broadcast.BroadcastDispatcher$handler$1] */
    public BroadcastDispatcher(Context context, final Looper looper, Executor executor, DumpManager dumpManager, BroadcastDispatcherLogger broadcastDispatcherLogger, UserTracker userTracker) {
        this.context = context;
        this.bgLooper = looper;
        this.bgExecutor = executor;
        this.logger = broadcastDispatcherLogger;
        this.userTracker = userTracker;
        this.handler = new Handler(looper) { // from class: com.android.systemui.broadcast.BroadcastDispatcher$handler$1
            @Override // android.os.Handler
            public final void handleMessage(Message message) {
                int i;
                int i2 = message.what;
                int i3 = 0;
                if (i2 == 0) {
                    Object obj = message.obj;
                    Objects.requireNonNull(obj, "null cannot be cast to non-null type com.android.systemui.broadcast.ReceiverData");
                    ReceiverData receiverData = (ReceiverData) obj;
                    int i4 = message.arg1;
                    if (receiverData.user.getIdentifier() == -2) {
                        i = BroadcastDispatcher.this.userTracker.getUserId();
                    } else {
                        i = receiverData.user.getIdentifier();
                    }
                    if (i >= -1) {
                        BroadcastDispatcher broadcastDispatcher = BroadcastDispatcher.this;
                        UserBroadcastDispatcher userBroadcastDispatcher = broadcastDispatcher.receiversByUser.get(i, broadcastDispatcher.createUBRForUser(i));
                        BroadcastDispatcher.this.receiversByUser.put(i, userBroadcastDispatcher);
                        Objects.requireNonNull(userBroadcastDispatcher);
                        userBroadcastDispatcher.bgHandler.obtainMessage(0, i4, 0, receiverData).sendToTarget();
                        return;
                    }
                    throw new IllegalStateException("Attempting to register receiver for invalid user {" + i + '}');
                } else if (i2 == 1) {
                    int size = BroadcastDispatcher.this.receiversByUser.size();
                    while (i3 < size) {
                        int i5 = i3 + 1;
                        UserBroadcastDispatcher valueAt = BroadcastDispatcher.this.receiversByUser.valueAt(i3);
                        Object obj2 = message.obj;
                        Objects.requireNonNull(obj2, "null cannot be cast to non-null type android.content.BroadcastReceiver");
                        Objects.requireNonNull(valueAt);
                        valueAt.bgHandler.obtainMessage(1, (BroadcastReceiver) obj2).sendToTarget();
                        i3 = i5;
                    }
                } else if (i2 != 2) {
                    super.handleMessage(message);
                } else {
                    UserBroadcastDispatcher userBroadcastDispatcher2 = BroadcastDispatcher.this.receiversByUser.get(message.arg1);
                    if (userBroadcastDispatcher2 != null) {
                        Object obj3 = message.obj;
                        Objects.requireNonNull(obj3, "null cannot be cast to non-null type android.content.BroadcastReceiver");
                        userBroadcastDispatcher2.bgHandler.obtainMessage(1, (BroadcastReceiver) obj3).sendToTarget();
                    }
                }
            }
        };
    }
}
