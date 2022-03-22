package com.android.systemui.shared.tracing;

import com.android.systemui.tracing.nano.SystemUiTraceProto;
/* loaded from: classes.dex */
public interface ProtoTraceable<T> {
    void writeToProto(SystemUiTraceProto systemUiTraceProto);
}
