package androidx.slice;

import android.app.PendingIntent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.net.Uri;
import androidx.core.graphics.drawable.IconCompat;
import androidx.versionedparcelable.CustomVersionedParcelable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Slice extends CustomVersionedParcelable {
    public static final String[] NO_HINTS = new String[0];
    public static final SliceItem[] NO_ITEMS = new SliceItem[0];
    public String[] mHints;
    public SliceItem[] mItems;
    public SliceSpec mSpec;
    public String mUri;

    /* loaded from: classes.dex */
    public static class Builder {
        public int mChildId;
        public SliceSpec mSpec;
        public final Uri mUri;
        public ArrayList<SliceItem> mItems = new ArrayList<>();
        public ArrayList<String> mHints = new ArrayList<>();

        public Builder(Uri uri) {
            this.mUri = uri;
        }

        public final Builder addHints(String... strArr) {
            this.mHints.addAll(Arrays.asList(strArr));
            return this;
        }

        public final Builder addInt(int i, String str, String... strArr) {
            this.mItems.add(new SliceItem(Integer.valueOf(i), "int", str, strArr));
            return this;
        }

        public final Builder addItem(SliceItem sliceItem) {
            this.mItems.add(sliceItem);
            return this;
        }

        public final Builder addLong(long j, String str, String... strArr) {
            this.mItems.add(new SliceItem(Long.valueOf(j), "long", str, strArr));
            return this;
        }

        public final Builder addText(CharSequence charSequence, String str, String... strArr) {
            this.mItems.add(new SliceItem(charSequence, "text", str, strArr));
            return this;
        }

        public final Slice build() {
            ArrayList<SliceItem> arrayList = this.mItems;
            ArrayList<String> arrayList2 = this.mHints;
            return new Slice(arrayList, (String[]) arrayList2.toArray(new String[arrayList2.size()]), this.mUri, this.mSpec);
        }

        public final Builder addAction(PendingIntent pendingIntent, Slice slice, String str) {
            Objects.requireNonNull(pendingIntent);
            Objects.requireNonNull(slice);
            this.mItems.add(new SliceItem(pendingIntent, slice, str, slice.mHints));
            return this;
        }

        public final Builder addIcon(IconCompat iconCompat, String str, String... strArr) {
            Objects.requireNonNull(iconCompat);
            if (Slice.isValidIcon(iconCompat)) {
                this.mItems.add(new SliceItem(iconCompat, "image", str, strArr));
            }
            return this;
        }

        public final Builder addSubSlice(Slice slice, String str) {
            Objects.requireNonNull(slice);
            this.mItems.add(new SliceItem(slice, "slice", str, slice.mHints));
            return this;
        }

        public Builder(Builder builder) {
            Objects.requireNonNull(builder);
            Uri.Builder appendPath = builder.mUri.buildUpon().appendPath("_gen");
            int i = builder.mChildId;
            builder.mChildId = i + 1;
            this.mUri = appendPath.appendPath(String.valueOf(i)).build();
        }
    }

    public Slice(ArrayList<SliceItem> arrayList, String[] strArr, Uri uri, SliceSpec sliceSpec) {
        this.mSpec = null;
        this.mItems = NO_ITEMS;
        this.mUri = null;
        this.mHints = strArr;
        this.mItems = (SliceItem[]) arrayList.toArray(new SliceItem[arrayList.size()]);
        this.mUri = uri.toString();
        this.mSpec = sliceSpec;
    }

    public final String toString(String str) {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m(str, "Slice ");
        String[] strArr = this.mHints;
        if (strArr.length > 0) {
            appendHints(m, strArr);
            m.append(' ');
        }
        m.append('[');
        m.append(this.mUri);
        m.append("] {\n");
        String str2 = str + "  ";
        int i = 0;
        while (true) {
            SliceItem[] sliceItemArr = this.mItems;
            if (i < sliceItemArr.length) {
                m.append(sliceItemArr[i].toString(str2));
                i++;
            } else {
                m.append(str);
                m.append('}');
                return m.toString();
            }
        }
    }

    public static void appendHints(StringBuilder sb, String[] strArr) {
        if (!(strArr == null || strArr.length == 0)) {
            sb.append('(');
            int length = strArr.length - 1;
            for (int i = 0; i < length; i++) {
                sb.append(strArr[i]);
                sb.append(", ");
            }
            sb.append(strArr[length]);
            sb.append(")");
        }
    }

    public static boolean isValidIcon(IconCompat iconCompat) {
        if (iconCompat == null) {
            return false;
        }
        if (iconCompat.mType != 2 || iconCompat.getResId() != 0) {
            return true;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Failed to add icon, invalid resource id: ");
        m.append(iconCompat.getResId());
        throw new IllegalArgumentException(m.toString());
    }

    public final List<SliceItem> getItems() {
        return Arrays.asList(this.mItems);
    }

    public final Uri getUri() {
        return Uri.parse(this.mUri);
    }

    public Slice() {
        this.mSpec = null;
        this.mItems = NO_ITEMS;
        this.mHints = NO_HINTS;
        this.mUri = null;
    }

    public final String toString() {
        return toString("");
    }
}
