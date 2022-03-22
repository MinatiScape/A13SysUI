package kotlinx.coroutines.flow;

import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.internal.Symbol;
/* compiled from: SharedFlow.kt */
/* loaded from: classes.dex */
public final class SharedFlowKt {
    public static final Symbol NO_VALUE = new Symbol("NO_VALUE");

    public static SharedFlowImpl MutableSharedFlow$default(int i, int i2) {
        BufferOverflow bufferOverflow;
        boolean z;
        BufferOverflow bufferOverflow2 = BufferOverflow.SUSPEND;
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            bufferOverflow = bufferOverflow2;
        } else {
            bufferOverflow = null;
        }
        boolean z2 = true;
        if (i >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            if (i <= 0 && bufferOverflow != bufferOverflow2) {
                z2 = false;
            }
            if (z2) {
                int i3 = i + 0;
                if (i3 < 0) {
                    i3 = Integer.MAX_VALUE;
                }
                return new SharedFlowImpl(0, i3, bufferOverflow);
            }
            throw new IllegalArgumentException(Intrinsics.stringPlus("replay or extraBufferCapacity must be positive with non-default onBufferOverflow strategy ", bufferOverflow).toString());
        }
        throw new IllegalArgumentException(Intrinsics.stringPlus("extraBufferCapacity cannot be negative, but was ", Integer.valueOf(i)).toString());
    }
}
