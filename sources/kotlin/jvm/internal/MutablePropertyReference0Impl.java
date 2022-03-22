package kotlin.jvm.internal;
/* loaded from: classes.dex */
public class MutablePropertyReference0Impl extends MutablePropertyReference0 {
    public Object get() {
        return getGetter().call();
    }

    public MutablePropertyReference0Impl(Object obj, Class cls, String str, String str2) {
        super(obj, cls, str, str2);
    }
}
