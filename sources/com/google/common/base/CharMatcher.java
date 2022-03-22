package com.google.common.base;
/* loaded from: classes.dex */
public abstract class CharMatcher {

    /* loaded from: classes.dex */
    public static abstract class FastMatcher extends CharMatcher {
    }

    /* loaded from: classes.dex */
    public static final class Whitespace extends NamedFastMatcher {
        static {
            Integer.numberOfLeadingZeros(31);
            new Whitespace();
        }
    }

    public String toString() {
        return super.toString();
    }

    /* loaded from: classes.dex */
    public static abstract class NamedFastMatcher extends FastMatcher {
        public final String description = "CharMatcher.whitespace()";

        @Override // com.google.common.base.CharMatcher
        public final String toString() {
            return this.description;
        }
    }
}
