package androidx.slice;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AlignmentSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import androidx.core.util.Pair;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import okio.Okio;
/* loaded from: classes.dex */
public final class SliceItem extends CustomVersionedParcelable {
    public String mFormat;
    public String[] mHints;
    public SliceItemHolder mHolder;
    public Object mObj;
    public CharSequence mSanitizedText;
    public String mSubType;

    /* loaded from: classes.dex */
    public interface ActionHandler {
        void onAction();
    }

    public SliceItem(Object obj, String str, String str2, String[] strArr) {
        this.mHints = strArr;
        this.mFormat = str;
        this.mSubType = str2;
        this.mObj = obj;
    }

    public final boolean hasAnyHints(String... strArr) {
        for (String str : strArr) {
            if (Okio.contains(this.mHints, str)) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:71:0x01d4, code lost:
        if (r14.equals("image") == false) goto L_0x01fb;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.String toString(java.lang.String r15) {
        /*
            Method dump skipped, instructions count: 642
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.slice.SliceItem.toString(java.lang.String):java.lang.String");
    }

    public final void addHint() {
        Object[] objArr;
        String[] strArr = this.mHints;
        int i = 0;
        if (strArr != null) {
            int length = strArr.length;
            objArr = (Object[]) Array.newInstance(String.class, length + 1);
            System.arraycopy(strArr, 0, objArr, 0, length);
            i = length;
        } else {
            objArr = (Object[]) Array.newInstance(String.class, 1);
        }
        objArr[i] = "partial";
        this.mHints = (String[]) objArr;
    }

    public final boolean fireActionInternal(Context context, Intent intent) throws PendingIntent.CanceledException {
        F f = ((Pair) this.mObj).first;
        if (f instanceof PendingIntent) {
            ((PendingIntent) f).send(context, 0, intent, null, null);
            return false;
        }
        ((ActionHandler) f).onAction();
        return true;
    }

    public final PendingIntent getAction() {
        F f = ((Pair) this.mObj).first;
        if (f instanceof PendingIntent) {
            return (PendingIntent) f;
        }
        return null;
    }

    public final List<String> getHints() {
        return Arrays.asList(this.mHints);
    }

    public final int getInt() {
        return ((Integer) this.mObj).intValue();
    }

    public final long getLong() {
        return ((Long) this.mObj).longValue();
    }

    public final CharSequence getSanitizedText() {
        if (this.mSanitizedText == null) {
            CharSequence charSequence = (CharSequence) this.mObj;
            if (charSequence instanceof Spannable) {
                fixSpannableText((Spannable) charSequence);
            } else if (charSequence instanceof Spanned) {
                Spanned spanned = (Spanned) charSequence;
                boolean z = false;
                Object[] spans = spanned.getSpans(0, spanned.length(), Object.class);
                int length = spans.length;
                int i = 0;
                while (true) {
                    boolean z2 = true;
                    if (i >= length) {
                        z = true;
                        break;
                    }
                    Object obj = spans[i];
                    if (!(obj instanceof AlignmentSpan) && !(obj instanceof ForegroundColorSpan) && !(obj instanceof RelativeSizeSpan) && !(obj instanceof StyleSpan)) {
                        z2 = false;
                    }
                    if (!z2) {
                        break;
                    }
                    i++;
                }
                if (!z) {
                    SpannableString spannableString = new SpannableString(charSequence);
                    fixSpannableText(spannableString);
                    charSequence = spannableString;
                }
            }
            this.mSanitizedText = charSequence;
        }
        return this.mSanitizedText;
    }

    public final Slice getSlice() {
        if ("action".equals(this.mFormat)) {
            return (Slice) ((Pair) this.mObj).second;
        }
        return (Slice) this.mObj;
    }

    public final boolean hasHint(String str) {
        return Okio.contains(this.mHints, str);
    }

    public static void fixSpannableText(Spannable spannable) {
        Object[] spans;
        boolean z;
        Object obj;
        for (Object obj2 : spannable.getSpans(0, spannable.length(), Object.class)) {
            if ((obj2 instanceof AlignmentSpan) || (obj2 instanceof ForegroundColorSpan) || (obj2 instanceof RelativeSizeSpan) || (obj2 instanceof StyleSpan)) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                obj = obj2;
            } else {
                obj = null;
            }
            if (obj != obj2) {
                if (obj != null) {
                    spannable.setSpan(obj, spannable.getSpanStart(obj2), spannable.getSpanEnd(obj2), spannable.getSpanFlags(obj2));
                }
                spannable.removeSpan(obj2);
            }
        }
    }

    public SliceItem(Object obj, String str, String str2, List<String> list) {
        this(obj, str, str2, (String[]) list.toArray(new String[list.size()]));
    }

    public SliceItem() {
        this.mHints = Slice.NO_HINTS;
        this.mFormat = "text";
        this.mSubType = null;
    }

    public SliceItem(PendingIntent pendingIntent, Slice slice, String str, String[] strArr) {
        this(new Pair(pendingIntent, slice), "action", str, strArr);
    }

    public final String toString() {
        return toString("");
    }
}
