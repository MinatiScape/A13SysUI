package com.android.systemui.log;

import android.os.Trace;
import android.util.Log;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
/* compiled from: LogBuffer.kt */
/* loaded from: classes.dex */
public final class LogBuffer {
    public final ArrayDeque<LogMessageImpl> buffer;
    public boolean frozen;
    public final LogcatEchoTracker logcatEchoTracker;
    public final int maxLogs;
    public final String name;
    public final int poolSize;
    public final boolean systrace;

    public final synchronized void freeze() {
        boolean z = this.frozen;
        if (!z) {
            LogLevel logLevel = LogLevel.DEBUG;
            LogBuffer$freeze$2 logBuffer$freeze$2 = LogBuffer$freeze$2.INSTANCE;
            if (!z) {
                LogMessageImpl obtain = obtain("LogBuffer", logLevel, logBuffer$freeze$2);
                obtain.str1 = this.name;
                push(obtain);
            }
            this.frozen = true;
        }
    }

    public final synchronized LogMessageImpl obtain(String str, LogLevel logLevel, Function1<? super LogMessage, String> function1) {
        LogMessageImpl logMessageImpl;
        if (this.frozen) {
            Function1<LogMessage, String> function12 = LogMessageImplKt.DEFAULT_RENDERER;
            logMessageImpl = new LogMessageImpl();
        } else if (this.buffer.size() > this.maxLogs - this.poolSize) {
            logMessageImpl = this.buffer.removeFirst();
        } else {
            Function1<LogMessage, String> function13 = LogMessageImplKt.DEFAULT_RENDERER;
            logMessageImpl = new LogMessageImpl();
        }
        logMessageImpl.reset(str, logLevel, System.currentTimeMillis(), function1);
        return logMessageImpl;
    }

    public final synchronized void push(LogMessageImpl logMessageImpl) {
        boolean z;
        if (!this.frozen) {
            if (this.buffer.size() == this.maxLogs) {
                Log.e("LogBuffer", "LogBuffer " + this.name + " has exceeded its pool size");
                this.buffer.removeFirst();
            }
            this.buffer.add(logMessageImpl);
            if (!this.logcatEchoTracker.isBufferLoggable(this.name, logMessageImpl.level) && !this.logcatEchoTracker.isTagLoggable(logMessageImpl.tag, logMessageImpl.level)) {
                z = false;
                echo(logMessageImpl, z, this.systrace);
            }
            z = true;
            echo(logMessageImpl, z, this.systrace);
        }
    }

    public final synchronized void unfreeze() {
        boolean z = this.frozen;
        if (z) {
            LogLevel logLevel = LogLevel.DEBUG;
            LogBuffer$unfreeze$2 logBuffer$unfreeze$2 = LogBuffer$unfreeze$2.INSTANCE;
            if (!z) {
                LogMessageImpl obtain = obtain("LogBuffer", logLevel, logBuffer$unfreeze$2);
                obtain.str1 = this.name;
                push(obtain);
            }
            this.frozen = false;
        }
    }

    public static void dumpMessage(LogMessageImpl logMessageImpl, PrintWriter printWriter) {
        SimpleDateFormat simpleDateFormat = LogBufferKt.DATE_FORMAT;
        Objects.requireNonNull(logMessageImpl);
        printWriter.print(simpleDateFormat.format(Long.valueOf(logMessageImpl.timestamp)));
        printWriter.print(" ");
        printWriter.print(logMessageImpl.level.getShortString());
        printWriter.print(" ");
        printWriter.print(logMessageImpl.tag);
        printWriter.print(": ");
        printWriter.println(logMessageImpl.printer.invoke(logMessageImpl));
    }

    public final void echo(LogMessageImpl logMessageImpl, boolean z, boolean z2) {
        if (z || z2) {
            String invoke = logMessageImpl.printer.invoke(logMessageImpl);
            if (z2) {
                Trace.instantForTrack(4096L, "UI Events", this.name + " - " + logMessageImpl.level.getShortString() + ' ' + logMessageImpl.tag + ": " + invoke);
            }
            if (z) {
                int ordinal = logMessageImpl.level.ordinal();
                if (ordinal == 0) {
                    Log.v(logMessageImpl.tag, invoke);
                } else if (ordinal == 1) {
                    Log.d(logMessageImpl.tag, invoke);
                } else if (ordinal == 2) {
                    Log.i(logMessageImpl.tag, invoke);
                } else if (ordinal == 3) {
                    Log.w(logMessageImpl.tag, invoke);
                } else if (ordinal == 4) {
                    Log.e(logMessageImpl.tag, invoke);
                } else if (ordinal == 5) {
                    Log.wtf(logMessageImpl.tag, invoke);
                }
            }
        }
    }

    public LogBuffer(String str, int i, int i2, LogcatEchoTracker logcatEchoTracker, boolean z) {
        this.name = str;
        this.maxLogs = i;
        this.poolSize = i2;
        this.logcatEchoTracker = logcatEchoTracker;
        this.systrace = z;
        if (i >= i2) {
            this.buffer = new ArrayDeque<>();
            return;
        }
        throw new IllegalArgumentException("maxLogs must be greater than or equal to poolSize, but maxLogs=" + i + " < " + i2 + "=poolSize");
    }
}
