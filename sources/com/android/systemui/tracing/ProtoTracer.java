package com.android.systemui.tracing;

import android.content.Context;
import android.os.Trace;
import android.util.Log;
import com.android.internal.util.TraceBuffer;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.tracing.FrameProtoTracer;
import com.android.systemui.tracing.nano.SystemUiTraceEntryProto;
import com.android.systemui.tracing.nano.SystemUiTraceFileProto;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import com.google.protobuf.nano.MessageNano;
import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ProtoTracer implements Dumpable, FrameProtoTracer.ProtoTraceParams<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> {
    public final Context mContext;
    public final FrameProtoTracer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> mProtoTracer = new FrameProtoTracer<>(this);

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("ProtoTracer:");
        printWriter.print("    ");
        StringBuilder sb = new StringBuilder();
        sb.append("enabled: ");
        FrameProtoTracer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> frameProtoTracer = this.mProtoTracer;
        Objects.requireNonNull(frameProtoTracer);
        sb.append(frameProtoTracer.mEnabled);
        printWriter.println(sb.toString());
        printWriter.print("    ");
        StringBuilder sb2 = new StringBuilder();
        sb2.append("usagePct: ");
        FrameProtoTracer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> frameProtoTracer2 = this.mProtoTracer;
        Objects.requireNonNull(frameProtoTracer2);
        sb2.append(frameProtoTracer2.mBuffer.getBufferSize() / 1048576.0f);
        printWriter.println(sb2.toString());
        printWriter.print("    ");
        printWriter.println("file: " + new File(this.mContext.getFilesDir(), "sysui_trace.pb"));
    }

    public final void stop() {
        FrameProtoTracer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto, SystemUiTraceProto> frameProtoTracer = this.mProtoTracer;
        Objects.requireNonNull(frameProtoTracer);
        synchronized (frameProtoTracer.mLock) {
            if (frameProtoTracer.mEnabled) {
                frameProtoTracer.mEnabled = false;
                try {
                    try {
                        Trace.beginSection("ProtoTracer.writeToFile");
                        TraceBuffer<MessageNano, SystemUiTraceFileProto, SystemUiTraceEntryProto> traceBuffer = frameProtoTracer.mBuffer;
                        File file = frameProtoTracer.mTraceFile;
                        Objects.requireNonNull((ProtoTracer) frameProtoTracer.mParams);
                        traceBuffer.writeTraceToFile(file, new SystemUiTraceFileProto());
                    } catch (IOException e) {
                        Log.e("FrameProtoTracer", "Unable to write buffer to file", e);
                    }
                } finally {
                    Trace.endSection();
                }
            }
        }
    }

    public ProtoTracer(Context context, DumpManager dumpManager) {
        this.mContext = context;
        dumpManager.registerDumpable(this);
    }
}
