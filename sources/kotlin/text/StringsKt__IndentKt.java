package kotlin.text;

import androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import androidx.leanback.R$styleable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt__IteratorsJVMKt;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.sequences.SequencesKt___SequencesKt;
import kotlin.sequences.TransformingSequence;
/* compiled from: Indent.kt */
/* loaded from: classes.dex */
public class StringsKt__IndentKt extends R$styleable {
    public static final String trimIndent(String str) {
        int i;
        boolean z;
        List list = SequencesKt___SequencesKt.toList(new TransformingSequence(StringsKt__StringsKt.rangesDelimitedBy$StringsKt__StringsKt$default(str, new String[]{"\r\n", "\n", "\r"}, false, 0), new StringsKt__StringsKt$splitToSequence$1(str)));
        ArrayList arrayList = new ArrayList();
        for (Object obj : list) {
            if (true ^ StringsKt__StringsJVMKt.isBlank((String) obj)) {
                arrayList.add(obj);
            }
        }
        ArrayList arrayList2 = new ArrayList(CollectionsKt__IteratorsJVMKt.collectionSizeOrDefault(arrayList, 10));
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str2 = (String) it.next();
            int length = str2.length();
            int i2 = 0;
            while (true) {
                if (i2 >= length) {
                    i2 = -1;
                    break;
                }
                int i3 = i2 + 1;
                if (!CharsKt__CharKt.isWhitespace(str2.charAt(i2))) {
                    break;
                }
                i2 = i3;
            }
            if (i2 == -1) {
                i2 = str2.length();
            }
            arrayList2.add(Integer.valueOf(i2));
        }
        Integer num = (Integer) CollectionsKt___CollectionsKt.minOrNull(arrayList2);
        if (num == null) {
            i = 0;
        } else {
            i = num.intValue();
        }
        int size = (list.size() * 0) + str.length();
        StringsKt__IndentKt$getIndentFunction$1 stringsKt__IndentKt$getIndentFunction$1 = StringsKt__IndentKt$getIndentFunction$1.INSTANCE;
        int lastIndex = SetsKt__SetsKt.getLastIndex(list);
        ArrayList arrayList3 = new ArrayList();
        int i4 = 0;
        for (Object obj2 : list) {
            int i5 = i4 + 1;
            String str3 = null;
            if (i4 >= 0) {
                String str4 = (String) obj2;
                if (!(i4 == 0 || i4 == lastIndex) || !StringsKt__StringsJVMKt.isBlank(str4)) {
                    if (i >= 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        int length2 = str4.length();
                        if (i <= length2) {
                            length2 = i;
                        }
                        str3 = str4.substring(length2);
                        Objects.requireNonNull(stringsKt__IndentKt$getIndentFunction$1);
                    } else {
                        throw new IllegalArgumentException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Requested character count ", i, " is less than zero.").toString());
                    }
                }
                if (str3 != null) {
                    arrayList3.add(str3);
                }
                i4 = i5;
            } else {
                SetsKt__SetsKt.throwIndexOverflow();
                throw null;
            }
        }
        StringBuilder sb = new StringBuilder(size);
        CollectionsKt___CollectionsKt.joinTo$default(arrayList3, sb, "\n");
        return sb.toString();
    }
}
