package kotlin.jvm.internal;

import java.util.Objects;
/* compiled from: PackageReference.kt */
/* loaded from: classes.dex */
public final class PackageReference implements ClassBasedDeclarationContainer {
    public final Class<?> jClass;

    public final boolean equals(Object obj) {
        if (obj instanceof PackageReference) {
            Class<?> cls = this.jClass;
            PackageReference packageReference = (PackageReference) obj;
            Objects.requireNonNull(packageReference);
            if (Intrinsics.areEqual(cls, packageReference.jClass)) {
                return true;
            }
        }
        return false;
    }

    public final int hashCode() {
        return this.jClass.hashCode();
    }

    public final String toString() {
        return Intrinsics.stringPlus(this.jClass.toString(), " (Kotlin reflection is not available)");
    }

    public PackageReference(Class cls) {
        this.jClass = cls;
    }

    @Override // kotlin.jvm.internal.ClassBasedDeclarationContainer
    public final Class<?> getJClass() {
        return this.jClass;
    }
}
