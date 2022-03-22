package androidx.fragment;
/* loaded from: classes.dex */
public class R$styleable {
    public static final int[] Fragment = {16842755, 16842960, 16842961};
    public static final int[] FragmentContainerView = {16842755, 16842961};

    public static final int compareValues(Comparable comparable, Comparable comparable2) {
        if (comparable == comparable2) {
            return 0;
        }
        if (comparable == null) {
            return -1;
        }
        if (comparable2 == null) {
            return 1;
        }
        return comparable.compareTo(comparable2);
    }
}
