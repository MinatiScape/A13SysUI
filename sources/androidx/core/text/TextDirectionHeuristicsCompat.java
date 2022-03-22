package androidx.core.text;
/* loaded from: classes.dex */
public final class TextDirectionHeuristicsCompat {
    public static final TextDirectionHeuristicInternal FIRSTSTRONG_LTR;
    public static final TextDirectionHeuristicInternal FIRSTSTRONG_RTL;
    public static final TextDirectionHeuristicInternal LTR = new TextDirectionHeuristicInternal(null, false);
    public static final TextDirectionHeuristicInternal RTL = new TextDirectionHeuristicInternal(null, true);

    /* loaded from: classes.dex */
    public static class FirstStrong implements TextDirectionAlgorithm {
        public static final FirstStrong INSTANCE = new FirstStrong();

        @Override // androidx.core.text.TextDirectionHeuristicsCompat.TextDirectionAlgorithm
        public final int checkRtl(CharSequence charSequence, int i) {
            int i2 = i + 0;
            int i3 = 2;
            for (int i4 = 0; i4 < i2 && i3 == 2; i4++) {
                byte directionality = Character.getDirectionality(charSequence.charAt(i4));
                TextDirectionHeuristicInternal textDirectionHeuristicInternal = TextDirectionHeuristicsCompat.LTR;
                if (directionality != 0) {
                    if (!(directionality == 1 || directionality == 2)) {
                        switch (directionality) {
                            case 14:
                            case 15:
                                break;
                            case 16:
                            case 17:
                                break;
                            default:
                                i3 = 2;
                                break;
                        }
                    }
                    i3 = 0;
                }
                i3 = 1;
            }
            return i3;
        }
    }

    /* loaded from: classes.dex */
    public interface TextDirectionAlgorithm {
        int checkRtl(CharSequence charSequence, int i);
    }

    /* loaded from: classes.dex */
    public static abstract class TextDirectionHeuristicImpl implements TextDirectionHeuristicCompat {
        public final TextDirectionAlgorithm mAlgorithm;

        public abstract boolean defaultIsRtl();

        public final boolean isRtl(CharSequence charSequence, int i) {
            if (charSequence == null || i < 0 || charSequence.length() - i < 0) {
                throw new IllegalArgumentException();
            }
            TextDirectionAlgorithm textDirectionAlgorithm = this.mAlgorithm;
            if (textDirectionAlgorithm == null) {
                return defaultIsRtl();
            }
            int checkRtl = textDirectionAlgorithm.checkRtl(charSequence, i);
            if (checkRtl == 0) {
                return true;
            }
            if (checkRtl != 1) {
                return defaultIsRtl();
            }
            return false;
        }

        public TextDirectionHeuristicImpl(FirstStrong firstStrong) {
            this.mAlgorithm = firstStrong;
        }
    }

    /* loaded from: classes.dex */
    public static class TextDirectionHeuristicInternal extends TextDirectionHeuristicImpl {
        public final boolean mDefaultIsRtl;

        public TextDirectionHeuristicInternal(FirstStrong firstStrong, boolean z) {
            super(firstStrong);
            this.mDefaultIsRtl = z;
        }

        @Override // androidx.core.text.TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl
        public final boolean defaultIsRtl() {
            return this.mDefaultIsRtl;
        }
    }

    static {
        FirstStrong firstStrong = FirstStrong.INSTANCE;
        FIRSTSTRONG_LTR = new TextDirectionHeuristicInternal(firstStrong, false);
        FIRSTSTRONG_RTL = new TextDirectionHeuristicInternal(firstStrong, true);
    }
}
