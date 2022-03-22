package com.android.systemui;

import android.util.MathUtils;
import java.util.Objects;
import kotlinx.coroutines.channels.AbstractChannel;
import kotlinx.coroutines.channels.ArrayChannel;
import kotlinx.coroutines.channels.BufferOverflow;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ConflatedChannel;
import kotlinx.coroutines.channels.LinkedListChannel;
import kotlinx.coroutines.channels.RendezvousChannel;
/* loaded from: classes.dex */
public final class R$anim {
    public static Boolean sUsePeopleFiltering;

    public static final float convertGammaToLinearFloat(int i, float f, float f2) {
        float f3;
        float norm = MathUtils.norm(0.0f, 65535.0f, i);
        if (norm <= 0.5f) {
            f3 = MathUtils.sq(norm / 0.5f);
        } else {
            f3 = MathUtils.exp((norm - 0.5599107f) / 0.17883277f) + 0.28466892f;
        }
        return MathUtils.lerp(f, f2, MathUtils.constrain(f3, 0.0f, 12.0f) / 12.0f);
    }

    public static final float zigzag(float f, float f2, float f3) {
        float f4 = 2;
        float f5 = (f % f3) / (f3 / f4);
        if (f5 > 1.0f) {
            f5 = f4 - f5;
        }
        return MathUtils.lerp(0.0f, f2, f5);
    }

    public static AbstractChannel Channel$default(int i, int i2) {
        BufferOverflow bufferOverflow;
        BufferOverflow bufferOverflow2 = BufferOverflow.SUSPEND;
        boolean z = false;
        if ((i2 & 1) != 0) {
            i = 0;
        }
        if ((i2 & 2) != 0) {
            bufferOverflow = bufferOverflow2;
        } else {
            bufferOverflow = null;
        }
        int i3 = 1;
        if (i == -2) {
            if (bufferOverflow == bufferOverflow2) {
                Objects.requireNonNull(Channel.Factory);
                i3 = Channel.Factory.CHANNEL_DEFAULT_CAPACITY;
            }
            return new ArrayChannel(i3, bufferOverflow, null);
        } else if (i == -1) {
            if (bufferOverflow == bufferOverflow2) {
                z = true;
            }
            if (z) {
                return new ConflatedChannel(null);
            }
            throw new IllegalArgumentException("CONFLATED capacity cannot be used with non-default onBufferOverflow".toString());
        } else if (i != 0) {
            if (i == Integer.MAX_VALUE) {
                return new LinkedListChannel(null);
            }
            if (i == 1 && bufferOverflow == BufferOverflow.DROP_OLDEST) {
                return new ConflatedChannel(null);
            }
            return new ArrayChannel(i, bufferOverflow, null);
        } else if (bufferOverflow == bufferOverflow2) {
            return new RendezvousChannel(null);
        } else {
            return new ArrayChannel(1, bufferOverflow, null);
        }
    }

    public static final int getBurnInOffset(int i, boolean z) {
        float f;
        float currentTimeMillis = ((float) System.currentTimeMillis()) / 60000.0f;
        float f2 = i;
        if (z) {
            f = 83.0f;
        } else {
            f = 521.0f;
        }
        return (int) zigzag(currentTimeMillis, f2, f);
    }
}
