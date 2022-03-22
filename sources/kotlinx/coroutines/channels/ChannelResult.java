package kotlinx.coroutines.channels;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Channel.kt */
/* loaded from: classes.dex */
public final class ChannelResult<T> {
    public static final Failed failed = new Failed();
    public final Object holder;

    /* compiled from: Channel.kt */
    /* loaded from: classes.dex */
    public static final class Closed extends Failed {
        public final Throwable cause;

        public final boolean equals(Object obj) {
            if (!(obj instanceof Closed) || !Intrinsics.areEqual(this.cause, ((Closed) obj).cause)) {
                return false;
            }
            return true;
        }

        public final int hashCode() {
            Throwable th = this.cause;
            if (th == null) {
                return 0;
            }
            return th.hashCode();
        }

        @Override // kotlinx.coroutines.channels.ChannelResult.Failed
        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Closed(");
            m.append(this.cause);
            m.append(')');
            return m.toString();
        }

        public Closed(Throwable th) {
            this.cause = th;
        }
    }

    /* compiled from: Channel.kt */
    /* loaded from: classes.dex */
    public static class Failed {
        public String toString() {
            return "Failed";
        }
    }

    public final boolean equals(Object obj) {
        Object obj2 = this.holder;
        if (!(obj instanceof ChannelResult)) {
            return false;
        }
        ChannelResult channelResult = (ChannelResult) obj;
        Objects.requireNonNull(channelResult);
        if (!Intrinsics.areEqual(obj2, channelResult.holder)) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        Object obj = this.holder;
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public final String toString() {
        Object obj = this.holder;
        if (obj instanceof Closed) {
            return ((Closed) obj).toString();
        }
        return "Value(" + obj + ')';
    }

    public /* synthetic */ ChannelResult(Object obj) {
        this.holder = obj;
    }
}
