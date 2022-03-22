package com.android.systemui.shared.tracing;

import android.os.SystemClock;
import android.view.Choreographer;
import com.android.internal.util.TraceBuffer;
import com.android.systemui.tracing.ProtoTracer;
import com.android.systemui.tracing.nano.SystemUiTraceEntryProto;
import com.android.systemui.tracing.nano.SystemUiTraceFileProto;
import com.android.systemui.tracing.nano.SystemUiTraceProto;
import com.google.protobuf.nano.MessageNano;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class FrameProtoTracer<P, S extends P, T extends P, R> implements Choreographer.FrameCallback {
    public final TraceBuffer<P, S, T> mBuffer;
    public Choreographer mChoreographer;
    public volatile boolean mEnabled;
    public boolean mFrameScheduled;
    public final ProtoTraceParams<P, S, T, R> mParams;
    public final AnonymousClass1 mProvider;
    public final File mTraceFile;
    public final Object mLock = new Object();
    public final LinkedList mPool = new LinkedList();
    public final ArrayList<ProtoTraceable<R>> mTraceables = new ArrayList<>();
    public final ArrayList<ProtoTraceable<R>> mTmpTraceables = new ArrayList<>();

    /* loaded from: classes.dex */
    public interface ProtoTraceParams<P, S, T, R> {
    }

    public final void logState() {
        synchronized (this.mLock) {
            this.mTmpTraceables.addAll(this.mTraceables);
        }
        TraceBuffer<P, S, T> traceBuffer = this.mBuffer;
        ProtoTraceParams<P, S, T, R> protoTraceParams = this.mParams;
        Object poll = this.mPool.poll();
        ArrayList<ProtoTraceable<R>> arrayList = this.mTmpTraceables;
        Objects.requireNonNull((ProtoTracer) protoTraceParams);
        SystemUiTraceEntryProto systemUiTraceEntryProto = (SystemUiTraceEntryProto) poll;
        if (systemUiTraceEntryProto == null) {
            systemUiTraceEntryProto = new SystemUiTraceEntryProto();
        }
        systemUiTraceEntryProto.elapsedRealtimeNanos = SystemClock.elapsedRealtimeNanos();
        SystemUiTraceProto systemUiTraceProto = systemUiTraceEntryProto.systemUi;
        if (systemUiTraceProto == null) {
            systemUiTraceProto = new SystemUiTraceProto();
        }
        systemUiTraceEntryProto.systemUi = systemUiTraceProto;
        Iterator<ProtoTraceable<R>> it = arrayList.iterator();
        while (it.hasNext()) {
            it.next().writeToProto(systemUiTraceEntryProto.systemUi);
        }
        traceBuffer.add(systemUiTraceEntryProto);
        this.mTmpTraceables.clear();
        this.mFrameScheduled = false;
    }

    public FrameProtoTracer(ProtoTraceParams<P, S, T, R> protoTraceParams) {
        TraceBuffer.ProtoProvider<P, S, T> protoProvider = new TraceBuffer.ProtoProvider<P, S, T>() { // from class: com.android.systemui.shared.tracing.FrameProtoTracer.1
            public final byte[] getBytes(P p) {
                Objects.requireNonNull((ProtoTracer) FrameProtoTracer.this.mParams);
                return MessageNano.toByteArray((MessageNano) p);
            }

            public final int getItemSize(P p) {
                Objects.requireNonNull((ProtoTracer) FrameProtoTracer.this.mParams);
                MessageNano messageNano = (MessageNano) p;
                Objects.requireNonNull(messageNano);
                if (messageNano.cachedSize < 0) {
                    messageNano.getSerializedSize();
                }
                return messageNano.cachedSize;
            }

            public final void write(S s, Queue<T> queue, OutputStream outputStream) throws IOException {
                Objects.requireNonNull((ProtoTracer) FrameProtoTracer.this.mParams);
                SystemUiTraceFileProto systemUiTraceFileProto = (SystemUiTraceFileProto) s;
                systemUiTraceFileProto.magicNumber = 4851032422572317011L;
                systemUiTraceFileProto.entry = (SystemUiTraceEntryProto[]) queue.toArray(new SystemUiTraceEntryProto[0]);
                outputStream.write(MessageNano.toByteArray(systemUiTraceFileProto));
            }
        };
        this.mProvider = protoProvider;
        this.mParams = protoTraceParams;
        this.mBuffer = new TraceBuffer<>(1048576, protoProvider, new Consumer<T>() { // from class: com.android.systemui.shared.tracing.FrameProtoTracer.2
            @Override // java.util.function.Consumer
            public final void accept(T t) {
                FrameProtoTracer frameProtoTracer = FrameProtoTracer.this;
                Objects.requireNonNull(frameProtoTracer);
                frameProtoTracer.mPool.add(t);
            }
        });
        ProtoTracer protoTracer = (ProtoTracer) protoTraceParams;
        Objects.requireNonNull(protoTracer);
        this.mTraceFile = new File(protoTracer.mContext.getFilesDir(), "sysui_trace.pb");
    }

    @Override // android.view.Choreographer.FrameCallback
    public final void doFrame(long j) {
        logState();
    }
}
