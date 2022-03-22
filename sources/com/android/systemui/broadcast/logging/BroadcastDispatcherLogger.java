package com.android.systemui.broadcast.logging;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogLevel;
import com.android.systemui.log.LogMessageImpl;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import kotlin.sequences.SequencesKt__SequencesKt;
import kotlin.sequences.SequencesKt___SequencesKt;
/* compiled from: BroadcastDispatcherLogger.kt */
/* loaded from: classes.dex */
public final class BroadcastDispatcherLogger {
    public final LogBuffer buffer;

    /* compiled from: BroadcastDispatcherLogger.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public static String flagToString(int i) {
            boolean z;
            StringBuilder sb = new StringBuilder("");
            if ((i & 1) != 0) {
                sb.append("instant_apps,");
            }
            if ((i & 4) != 0) {
                sb.append("not_exported,");
            }
            if ((i & 2) != 0) {
                sb.append("exported");
            }
            if (sb.length() == 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                sb.append(i);
            }
            return sb.toString();
        }
    }

    public final void logContextReceiverUnregistered(int i, String str) {
        LogLevel logLevel = LogLevel.INFO;
        BroadcastDispatcherLogger$logContextReceiverUnregistered$2 broadcastDispatcherLogger$logContextReceiverUnregistered$2 = BroadcastDispatcherLogger$logContextReceiverUnregistered$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logContextReceiverUnregistered$2);
            obtain.int1 = i;
            obtain.str1 = str;
            logBuffer.push(obtain);
        }
    }

    public BroadcastDispatcherLogger(LogBuffer logBuffer) {
        this.buffer = logBuffer;
    }

    public final void logBroadcastDispatched(int i, String str, BroadcastReceiver broadcastReceiver) {
        String obj = broadcastReceiver.toString();
        LogLevel logLevel = LogLevel.DEBUG;
        BroadcastDispatcherLogger$logBroadcastDispatched$2 broadcastDispatcherLogger$logBroadcastDispatched$2 = BroadcastDispatcherLogger$logBroadcastDispatched$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logBroadcastDispatched$2);
            obtain.int1 = i;
            obtain.str1 = str;
            obtain.str2 = obj;
            logBuffer.push(obtain);
        }
    }

    public final void logBroadcastReceived(int i, int i2, Intent intent) {
        String intent2 = intent.toString();
        LogLevel logLevel = LogLevel.INFO;
        BroadcastDispatcherLogger$logBroadcastReceived$2 broadcastDispatcherLogger$logBroadcastReceived$2 = BroadcastDispatcherLogger$logBroadcastReceived$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logBroadcastReceived$2);
            obtain.int1 = i;
            obtain.int2 = i2;
            obtain.str1 = intent2;
            logBuffer.push(obtain);
        }
    }

    public final void logContextReceiverRegistered(int i, int i2, IntentFilter intentFilter) {
        String str;
        String joinToString$default = SequencesKt___SequencesKt.joinToString$default(SequencesKt__SequencesKt.asSequence(intentFilter.actionsIterator()), "Actions(");
        if (intentFilter.countCategories() != 0) {
            str = SequencesKt___SequencesKt.joinToString$default(SequencesKt__SequencesKt.asSequence(intentFilter.categoriesIterator()), "Categories(");
        } else {
            str = "";
        }
        LogLevel logLevel = LogLevel.INFO;
        BroadcastDispatcherLogger$logContextReceiverRegistered$2 broadcastDispatcherLogger$logContextReceiverRegistered$2 = BroadcastDispatcherLogger$logContextReceiverRegistered$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logContextReceiverRegistered$2);
            obtain.int1 = i;
            if (!Intrinsics.areEqual(str, "")) {
                joinToString$default = joinToString$default + '\n' + str;
            }
            obtain.str1 = joinToString$default;
            obtain.str2 = Companion.flagToString(i2);
            logBuffer.push(obtain);
        }
    }

    public final void logReceiverRegistered(int i, BroadcastReceiver broadcastReceiver, int i2) {
        String obj = broadcastReceiver.toString();
        String flagToString = Companion.flagToString(i2);
        LogLevel logLevel = LogLevel.INFO;
        BroadcastDispatcherLogger$logReceiverRegistered$2 broadcastDispatcherLogger$logReceiverRegistered$2 = BroadcastDispatcherLogger$logReceiverRegistered$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logReceiverRegistered$2);
            obtain.int1 = i;
            obtain.str1 = obj;
            obtain.str2 = flagToString;
            logBuffer.push(obtain);
        }
    }

    public final void logReceiverUnregistered(int i, BroadcastReceiver broadcastReceiver) {
        String obj = broadcastReceiver.toString();
        LogLevel logLevel = LogLevel.INFO;
        BroadcastDispatcherLogger$logReceiverUnregistered$2 broadcastDispatcherLogger$logReceiverUnregistered$2 = BroadcastDispatcherLogger$logReceiverUnregistered$2.INSTANCE;
        LogBuffer logBuffer = this.buffer;
        Objects.requireNonNull(logBuffer);
        if (!logBuffer.frozen) {
            LogMessageImpl obtain = logBuffer.obtain("BroadcastDispatcherLog", logLevel, broadcastDispatcherLogger$logReceiverUnregistered$2);
            obtain.int1 = i;
            obtain.str1 = obj;
            logBuffer.push(obtain);
        }
    }
}
