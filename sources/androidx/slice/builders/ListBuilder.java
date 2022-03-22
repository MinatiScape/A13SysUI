package androidx.slice.builders;

import android.content.Context;
import android.net.Uri;
import androidx.core.graphics.drawable.IconCompat;
import androidx.core.util.Pair;
import java.util.ArrayList;
/* loaded from: classes.dex */
public final class ListBuilder extends TemplateSliceBuilder {
    public androidx.slice.builders.impl.ListBuilder mImpl;

    /* loaded from: classes.dex */
    public static class RowBuilder {
        public CharSequence mContentDescription;
        public SliceAction mPrimaryAction;
        public CharSequence mTitle;
        public boolean mTitleLoading;
        public final Uri mUri;
        public final ArrayList mEndItems = new ArrayList();
        public final ArrayList mEndTypes = new ArrayList();
        public final ArrayList mEndLoads = new ArrayList();

        public final RowBuilder addEndItem(IconCompat iconCompat, int i) {
            this.mEndItems.add(new Pair(iconCompat, Integer.valueOf(i)));
            this.mEndTypes.add(1);
            this.mEndLoads.add(Boolean.FALSE);
            return this;
        }

        public RowBuilder(Uri uri) {
            this.mUri = uri;
        }
    }

    /* loaded from: classes.dex */
    public static class HeaderBuilder {
        public SliceAction mPrimaryAction;
        public CharSequence mTitle;
        public boolean mTitleLoading;
        public final Uri mUri;

        public HeaderBuilder(Uri uri) {
            this.mUri = uri;
        }
    }

    public final ListBuilder addRow(RowBuilder rowBuilder) {
        this.mImpl.addRow(rowBuilder);
        return this;
    }

    public ListBuilder(Context context, Uri uri) {
        super(context, uri);
        this.mImpl.setTtl();
    }
}
