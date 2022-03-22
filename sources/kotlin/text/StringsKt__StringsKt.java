package kotlin.text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntProgression;
import kotlin.ranges.IntRange;
import kotlin.sequences.SequencesKt___SequencesKt$asIterable$$inlined$Iterable$1;
/* compiled from: Strings.kt */
/* loaded from: classes.dex */
public class StringsKt__StringsKt extends StringsKt__StringsJVMKt {
    public static boolean contains$default(String str, String str2) {
        return indexOf$default(str, str2, 0, false, 2) >= 0;
    }

    public static final int indexOf$StringsKt__StringsKt(CharSequence charSequence, CharSequence charSequence2, int i, int i2, boolean z, boolean z2) {
        IntProgression intProgression;
        if (!z2) {
            if (i < 0) {
                i = 0;
            }
            int length = charSequence.length();
            if (i2 > length) {
                i2 = length;
            }
            intProgression = new IntRange(i, i2);
        } else {
            int lastIndex = getLastIndex(charSequence);
            if (i > lastIndex) {
                i = lastIndex;
            }
            if (i2 < 0) {
                i2 = 0;
            }
            intProgression = new IntProgression(i, i2, -1);
        }
        if (!(charSequence instanceof String) || !(charSequence2 instanceof String)) {
            int i3 = intProgression.first;
            int i4 = intProgression.last;
            int i5 = intProgression.step;
            if ((i5 > 0 && i3 <= i4) || (i5 < 0 && i4 <= i3)) {
                while (true) {
                    int i6 = i3 + i5;
                    if (regionMatchesImpl(charSequence2, charSequence, i3, charSequence2.length(), z)) {
                        return i3;
                    }
                    if (i3 == i4) {
                        break;
                    }
                    i3 = i6;
                }
            }
        } else {
            int i7 = intProgression.first;
            int i8 = intProgression.last;
            int i9 = intProgression.step;
            if ((i9 > 0 && i7 <= i8) || (i9 < 0 && i8 <= i7)) {
                while (true) {
                    int i10 = i7 + i9;
                    if (StringsKt__StringsJVMKt.regionMatches((String) charSequence2, 0, (String) charSequence, i7, charSequence2.length(), z)) {
                        return i7;
                    }
                    if (i7 == i8) {
                        break;
                    }
                    i7 = i10;
                }
            }
        }
        return -1;
    }

    public static int indexOf$default(String str, char c, int i, int i2) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        return str.indexOf(c, i);
    }

    public static DelimitedRangesSequence rangesDelimitedBy$StringsKt__StringsKt$default(CharSequence charSequence, String[] strArr, boolean z, int i) {
        requireNonNegativeLimit(i);
        return new DelimitedRangesSequence(charSequence, 0, i, new StringsKt__StringsKt$rangesDelimitedBy$2(Arrays.asList(strArr), z));
    }

    public static final boolean regionMatchesImpl(CharSequence charSequence, CharSequence charSequence2, int i, int i2, boolean z) {
        if (i < 0 || charSequence.length() - i2 < 0 || i > charSequence2.length() - i2) {
            return false;
        }
        int i3 = 0;
        while (i3 < i2) {
            int i4 = i3 + 1;
            if (!CharsKt__CharKt.equals(charSequence.charAt(0 + i3), charSequence2.charAt(i3 + i), z)) {
                return false;
            }
            i3 = i4;
        }
        return true;
    }

    public static List split$default(CharSequence charSequence, String[] strArr) {
        boolean z = true;
        if (strArr.length == 1) {
            String str = strArr[0];
            if (str.length() != 0) {
                z = false;
            }
            if (!z) {
                requireNonNegativeLimit(0);
                int indexOf = indexOf(charSequence, str, 0, false);
                if (indexOf == -1) {
                    return Collections.singletonList(charSequence.toString());
                }
                ArrayList arrayList = new ArrayList(10);
                int i = 0;
                do {
                    arrayList.add(charSequence.subSequence(i, indexOf).toString());
                    i = str.length() + indexOf;
                    indexOf = indexOf(charSequence, str, i, false);
                } while (indexOf != -1);
                arrayList.add(charSequence.subSequence(i, charSequence.length()).toString());
                return arrayList;
            }
        }
        SequencesKt___SequencesKt$asIterable$$inlined$Iterable$1 sequencesKt___SequencesKt$asIterable$$inlined$Iterable$1 = new SequencesKt___SequencesKt$asIterable$$inlined$Iterable$1(rangesDelimitedBy$StringsKt__StringsKt$default(charSequence, strArr, false, 0));
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(sequencesKt___SequencesKt$asIterable$$inlined$Iterable$1, 10));
        Iterator<Object> it = sequencesKt___SequencesKt$asIterable$$inlined$Iterable$1.iterator();
        while (it.hasNext()) {
            arrayList2.add(substring(charSequence, (IntRange) it.next()));
        }
        return arrayList2;
    }

    public static String substringAfter$default(String str, String str2) {
        int indexOf$default = indexOf$default(str, str2, 0, false, 6);
        if (indexOf$default == -1) {
            return str;
        }
        return str.substring(str2.length() + indexOf$default, str.length());
    }

    public static final int indexOf(CharSequence charSequence, String str, int i, boolean z) {
        if (z || !(charSequence instanceof String)) {
            return indexOf$StringsKt__StringsKt(charSequence, str, i, charSequence.length(), z, false);
        }
        return ((String) charSequence).indexOf(str, i);
    }

    public static /* synthetic */ int indexOf$default(CharSequence charSequence, String str, int i, boolean z, int i2) {
        if ((i2 & 2) != 0) {
            i = 0;
        }
        if ((i2 & 4) != 0) {
            z = false;
        }
        return indexOf(charSequence, str, i, z);
    }

    public static final void requireNonNegativeLimit(int i) {
        boolean z;
        if (i >= 0) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException(Intrinsics.stringPlus("Limit must be non-negative, but was ", Integer.valueOf(i)).toString());
        }
    }

    public static final String substring(CharSequence charSequence, IntRange intRange) {
        return charSequence.subSequence(Integer.valueOf(intRange.first).intValue(), Integer.valueOf(intRange.last).intValue() + 1).toString();
    }

    public static final int getLastIndex(CharSequence charSequence) {
        return charSequence.length() - 1;
    }

    public static final CharSequence trim(String str) {
        int i;
        int length = str.length() - 1;
        int i2 = 0;
        boolean z = false;
        while (i2 <= length) {
            if (!z) {
                i = i2;
            } else {
                i = length;
            }
            boolean isWhitespace = CharsKt__CharKt.isWhitespace(str.charAt(i));
            if (!z) {
                if (!isWhitespace) {
                    z = true;
                } else {
                    i2++;
                }
            } else if (!isWhitespace) {
                break;
            } else {
                length--;
            }
        }
        return str.subSequence(i2, length + 1);
    }
}
