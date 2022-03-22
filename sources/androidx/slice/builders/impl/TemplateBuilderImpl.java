package androidx.slice.builders.impl;

import androidx.slice.Slice;
import androidx.slice.SliceSpec;
import androidx.slice.SystemClock;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class TemplateBuilderImpl {
    public SystemClock mClock;
    public Slice.Builder mSliceBuilder;
    public final SliceSpec mSpec;

    public TemplateBuilderImpl(Slice.Builder builder, SliceSpec sliceSpec) {
        this(builder, sliceSpec, new SystemClock());
    }

    public abstract void apply(Slice.Builder builder);

    public TemplateBuilderImpl(Slice.Builder builder, SliceSpec sliceSpec, SystemClock systemClock) {
        this.mSliceBuilder = builder;
        this.mSpec = sliceSpec;
        this.mClock = systemClock;
    }

    public Slice build() {
        Slice.Builder builder = this.mSliceBuilder;
        SliceSpec sliceSpec = this.mSpec;
        Objects.requireNonNull(builder);
        builder.mSpec = sliceSpec;
        apply(this.mSliceBuilder);
        return this.mSliceBuilder.build();
    }
}
