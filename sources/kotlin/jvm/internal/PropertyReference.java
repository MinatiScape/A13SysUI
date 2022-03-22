package kotlin.jvm.internal;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.jvm.KotlinReflectionNotSupportedError;
import kotlin.reflect.KCallable;
import kotlin.reflect.KProperty;
/* loaded from: classes.dex */
public abstract class PropertyReference extends CallableReference implements KProperty {
    public PropertyReference() {
    }

    public final boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof PropertyReference) {
            PropertyReference propertyReference = (PropertyReference) obj;
            return getOwner().equals(propertyReference.getOwner()) && getName().equals(propertyReference.getName()) && getSignature().equals(propertyReference.getSignature()) && Intrinsics.areEqual(this.receiver, propertyReference.receiver);
        } else if (obj instanceof KProperty) {
            return obj.equals(compute());
        } else {
            return false;
        }
    }

    public PropertyReference(Object obj, Class cls, String str, String str2, int i) {
        super(obj, cls, str, str2, (i & 1) != 1 ? false : true);
    }

    public final KProperty getReflected() {
        KCallable compute = compute();
        if (compute != this) {
            return (KProperty) compute;
        }
        throw new KotlinReflectionNotSupportedError();
    }

    public final int hashCode() {
        int hashCode = getName().hashCode();
        return getSignature().hashCode() + ((hashCode + (getOwner().hashCode() * 31)) * 31);
    }

    public final String toString() {
        KCallable compute = compute();
        if (compute != this) {
            return compute.toString();
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("property ");
        m.append(getName());
        m.append(" (Kotlin reflection is not available)");
        return m.toString();
    }
}
