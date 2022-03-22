package kotlin.jvm.internal;

import com.android.systemui.statusbar.notification.AnimatedImageNotificationManager;
import java.io.Serializable;
import java.util.Objects;
/* loaded from: classes.dex */
public class AdaptedFunctionReference implements FunctionBase, Serializable {
    public final Object receiver;
    private final Class owner = AnimatedImageNotificationManager.class;
    private final String name = "updateAnimatedImageDrawables";
    private final String signature = "updateAnimatedImageDrawables(Lcom/android/systemui/statusbar/notification/collection/NotificationEntry;)Lkotlin/Unit;";
    private final boolean isTopLevel = false;
    private final int arity = 1;
    private final int flags = 4;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AdaptedFunctionReference)) {
            return false;
        }
        AdaptedFunctionReference adaptedFunctionReference = (AdaptedFunctionReference) obj;
        return this.isTopLevel == adaptedFunctionReference.isTopLevel && this.arity == adaptedFunctionReference.arity && this.flags == adaptedFunctionReference.flags && Intrinsics.areEqual(this.receiver, adaptedFunctionReference.receiver) && Intrinsics.areEqual(this.owner, adaptedFunctionReference.owner) && this.name.equals(adaptedFunctionReference.name) && this.signature.equals(adaptedFunctionReference.signature);
    }

    public AdaptedFunctionReference(AnimatedImageNotificationManager animatedImageNotificationManager) {
        this.receiver = animatedImageNotificationManager;
    }

    public final int hashCode() {
        int i;
        int i2;
        Object obj = this.receiver;
        int i3 = 0;
        if (obj != null) {
            i = obj.hashCode();
        } else {
            i = 0;
        }
        int i4 = i * 31;
        Class cls = this.owner;
        if (cls != null) {
            i3 = cls.hashCode();
        }
        int hashCode = (this.signature.hashCode() + ((this.name.hashCode() + ((i4 + i3) * 31)) * 31)) * 31;
        if (this.isTopLevel) {
            i2 = 1231;
        } else {
            i2 = 1237;
        }
        return ((((hashCode + i2) * 31) + this.arity) * 31) + this.flags;
    }

    public final String toString() {
        Objects.requireNonNull(Reflection.factory);
        return ReflectionFactory.renderLambdaToString(this);
    }

    @Override // kotlin.jvm.internal.FunctionBase
    public final int getArity() {
        return this.arity;
    }
}
