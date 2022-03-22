package androidx.slice.builders;

import android.app.slice.SliceManager;
import android.content.Context;
import android.net.Uri;
import androidx.slice.Slice;
import androidx.slice.SliceConvert;
import androidx.slice.SliceProvider;
import androidx.slice.SliceSpec;
import androidx.slice.SliceSpecs;
import androidx.slice.SystemClock;
import androidx.slice.builders.impl.ListBuilder;
import androidx.slice.builders.impl.ListBuilderBasicImpl;
import androidx.slice.builders.impl.ListBuilderImpl;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class TemplateSliceBuilder {
    public final Slice.Builder mBuilder;
    public ArrayList mSpecs;

    public final boolean checkCompatible(SliceSpec sliceSpec) {
        boolean z;
        int size = this.mSpecs.size();
        for (int i = 0; i < size; i++) {
            SliceSpec sliceSpec2 = (SliceSpec) this.mSpecs.get(i);
            Objects.requireNonNull(sliceSpec2);
            if (sliceSpec2.mType.equals(sliceSpec.mType) && sliceSpec2.mRevision >= sliceSpec.mRevision) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                return true;
            }
        }
        return false;
    }

    public TemplateSliceBuilder(Context context, Uri uri) {
        ArrayList arrayList;
        ListBuilder listBuilder;
        this.mBuilder = new Slice.Builder(uri);
        if (SliceProvider.sSpecs != null) {
            arrayList = new ArrayList(SliceProvider.sSpecs);
        } else {
            arrayList = new ArrayList(SliceConvert.wrap(((SliceManager) context.getSystemService(SliceManager.class)).getPinnedSpecs(uri)));
        }
        this.mSpecs = arrayList;
        ListBuilder listBuilder2 = (ListBuilder) this;
        SliceSpec sliceSpec = SliceSpecs.LIST_V2;
        if (listBuilder2.checkCompatible(sliceSpec)) {
            listBuilder = new ListBuilderImpl(listBuilder2.mBuilder, sliceSpec, new SystemClock());
        } else {
            SliceSpec sliceSpec2 = SliceSpecs.LIST;
            if (listBuilder2.checkCompatible(sliceSpec2)) {
                listBuilder = new ListBuilderImpl(listBuilder2.mBuilder, sliceSpec2, new SystemClock());
            } else if (listBuilder2.checkCompatible(SliceSpecs.BASIC)) {
                listBuilder = new ListBuilderBasicImpl(listBuilder2.mBuilder);
            } else {
                listBuilder = null;
            }
        }
        if (listBuilder != null) {
            listBuilder2.mImpl = listBuilder;
            return;
        }
        throw new IllegalArgumentException("No valid specs found");
    }
}
