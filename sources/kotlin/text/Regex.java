package kotlin.text;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/* compiled from: Regex.kt */
/* loaded from: classes.dex */
public final class Regex implements Serializable {
    private Set<Object> _options;
    private final Pattern nativePattern;

    /* compiled from: Regex.kt */
    /* loaded from: classes.dex */
    public static final class Serialized implements Serializable {
        private static final long serialVersionUID = 0;
        private final int flags;
        private final String pattern;

        private final Object readResolve() {
            return new Regex(Pattern.compile(this.pattern, this.flags));
        }

        public Serialized(String str, int i) {
            this.pattern = str;
            this.flags = i;
        }
    }

    public Regex(Pattern pattern) {
        this.nativePattern = pattern;
    }

    public final List split(String str) {
        int i = 0;
        StringsKt__StringsKt.requireNonNegativeLimit(0);
        Matcher matcher = this.nativePattern.matcher(str);
        if (!matcher.find()) {
            return Collections.singletonList(str.toString());
        }
        ArrayList arrayList = new ArrayList(10);
        do {
            arrayList.add(str.subSequence(i, matcher.start()).toString());
            i = matcher.end();
        } while (matcher.find());
        arrayList.add(str.subSequence(i, str.length()).toString());
        return arrayList;
    }

    private final Object writeReplace() {
        return new Serialized(this.nativePattern.pattern(), this.nativePattern.flags());
    }

    public final boolean matches(CharSequence charSequence) {
        return this.nativePattern.matcher(charSequence).matches();
    }

    public final String replace(String str) {
        return this.nativePattern.matcher(str).replaceAll("");
    }

    public final String toString() {
        return this.nativePattern.toString();
    }

    public Regex(String str) {
        this(Pattern.compile(str));
    }
}
