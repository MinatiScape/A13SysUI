package androidx.leanback.widget;

import androidx.leanback.widget.ItemAlignmentFacet;
/* loaded from: classes.dex */
public final class ItemAlignment {
    public final Axis vertical = new Axis(1);
    public final Axis horizontal = new Axis(0);

    /* loaded from: classes.dex */
    public static final class Axis extends ItemAlignmentFacet.ItemAlignmentDef {
        public int mOrientation;

        public Axis(int i) {
            this.mOrientation = i;
        }
    }
}
