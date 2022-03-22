package com.google.common.base;

import java.util.Arrays;
/* loaded from: classes.dex */
public final class MoreObjects$ToStringHelper {
    public final String className;
    public final ValueHolder holderHead;
    public ValueHolder holderTail;

    /* loaded from: classes.dex */
    public static final class UnconditionalValueHolder extends ValueHolder {
    }

    /* loaded from: classes.dex */
    public static class ValueHolder {
        public String name;
        public ValueHolder next;
        public Object value;
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder(32);
        sb.append(this.className);
        sb.append('{');
        ValueHolder valueHolder = this.holderHead.next;
        String str = "";
        while (valueHolder != null) {
            Object obj = valueHolder.value;
            boolean z = valueHolder instanceof UnconditionalValueHolder;
            sb.append(str);
            String str2 = valueHolder.name;
            if (str2 != null) {
                sb.append(str2);
                sb.append('=');
            }
            if (obj == null || !obj.getClass().isArray()) {
                sb.append(obj);
            } else {
                String deepToString = Arrays.deepToString(new Object[]{obj});
                sb.append((CharSequence) deepToString, 1, deepToString.length() - 1);
            }
            valueHolder = valueHolder.next;
            str = ", ";
        }
        sb.append('}');
        return sb.toString();
    }

    public MoreObjects$ToStringHelper(String str) {
        ValueHolder valueHolder = new ValueHolder();
        this.holderHead = valueHolder;
        this.holderTail = valueHolder;
        this.className = str;
    }
}
