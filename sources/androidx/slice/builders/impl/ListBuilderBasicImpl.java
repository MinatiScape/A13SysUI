package androidx.slice.builders.impl;

import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceItem;
import androidx.slice.SliceSpecs;
import androidx.slice.builders.ListBuilder;
import androidx.slice.builders.SliceAction;
import androidx.slice.core.SliceActionImpl;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ListBuilderBasicImpl extends TemplateBuilderImpl implements ListBuilder {
    public IconCompat mIconCompat;
    public SliceAction mSliceAction;
    public CharSequence mTitle;

    public ListBuilderBasicImpl(Slice.Builder builder) {
        super(builder, SliceSpecs.BASIC);
    }

    @Override // androidx.slice.builders.impl.ListBuilder
    public final void addRow(ListBuilder.RowBuilder rowBuilder) {
        SliceAction sliceAction;
        CharSequence charSequence;
        if (this.mTitle == null && (charSequence = rowBuilder.mTitle) != null) {
            this.mTitle = charSequence;
        }
        if (this.mSliceAction == null && (sliceAction = rowBuilder.mPrimaryAction) != null) {
            this.mSliceAction = sliceAction;
        }
    }

    @Override // androidx.slice.builders.impl.TemplateBuilderImpl
    public final void apply(Slice.Builder builder) {
        Slice.Builder builder2 = new Slice.Builder(this.mSliceBuilder);
        SliceAction sliceAction = this.mSliceAction;
        if (sliceAction != null) {
            if (this.mTitle == null) {
                SliceActionImpl sliceActionImpl = sliceAction.mSliceAction;
                Objects.requireNonNull(sliceActionImpl);
                if (sliceActionImpl.mTitle != null) {
                    SliceAction sliceAction2 = this.mSliceAction;
                    Objects.requireNonNull(sliceAction2);
                    SliceActionImpl sliceActionImpl2 = sliceAction2.mSliceAction;
                    Objects.requireNonNull(sliceActionImpl2);
                    this.mTitle = sliceActionImpl2.mTitle;
                }
            }
            if (this.mIconCompat == null && this.mSliceAction.getIcon() != null) {
                this.mIconCompat = this.mSliceAction.getIcon();
            }
            this.mSliceAction.setPrimaryAction(builder2);
        }
        CharSequence charSequence = this.mTitle;
        if (charSequence != null) {
            builder2.addItem(new SliceItem(charSequence, "text", (String) null, new String[]{"title"}));
        }
        IconCompat iconCompat = this.mIconCompat;
        if (iconCompat != null) {
            builder.addIcon(iconCompat, null, "title");
        }
        Slice build = builder2.build();
        Objects.requireNonNull(builder);
        builder.addSubSlice(build, null);
    }

    @Override // androidx.slice.builders.impl.ListBuilder
    public final void setHeader(ListBuilder.HeaderBuilder headerBuilder) {
        CharSequence charSequence = headerBuilder.mTitle;
        if (charSequence != null) {
            this.mTitle = charSequence;
        }
        SliceAction sliceAction = headerBuilder.mPrimaryAction;
        if (sliceAction != null) {
            this.mSliceAction = sliceAction;
        }
    }

    @Override // androidx.slice.builders.impl.ListBuilder
    public final void setTtl() {
        Slice.Builder builder = this.mSliceBuilder;
        Objects.requireNonNull(builder);
        builder.mItems.add(new SliceItem((Object) (-1L), "long", "millis", new String[]{"ttl"}));
    }
}
