package com.android.systemui.dreams.complication;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Constraints;
import com.android.systemui.dreams.complication.ComplicationLayoutEngine;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class ComplicationLayoutEngine {
    public final ConstraintLayout mLayout;
    public final int mMargin;
    public final HashMap<ComplicationId, ViewEntry> mEntries = new HashMap<>();
    public final HashMap<Integer, PositionGroup> mPositions = new HashMap<>();

    /* loaded from: classes.dex */
    public static class PositionGroup implements DirectionGroup.Parent {
        public final HashMap<Integer, DirectionGroup> mDirectionGroups = new HashMap<>();

        public final void onEntriesChanged() {
            final boolean z;
            ViewEntry viewEntry;
            ViewEntry viewEntry2 = null;
            for (DirectionGroup directionGroup : this.mDirectionGroups.values()) {
                Objects.requireNonNull(directionGroup);
                if (directionGroup.mViews.isEmpty()) {
                    viewEntry = null;
                } else {
                    viewEntry = directionGroup.mViews.get(0);
                }
                if (viewEntry2 == null || (viewEntry != null && viewEntry.compareTo(viewEntry2) > 0)) {
                    viewEntry2 = viewEntry;
                }
            }
            if (viewEntry2 != null) {
                for (DirectionGroup directionGroup2 : this.mDirectionGroups.values()) {
                    View view = viewEntry2.mView;
                    Objects.requireNonNull(directionGroup2);
                    Iterator<ViewEntry> it = directionGroup2.mViews.iterator();
                    final View view2 = view;
                    while (it.hasNext()) {
                        final ViewEntry next = it.next();
                        Objects.requireNonNull(next);
                        ComplicationLayoutParams complicationLayoutParams = next.mLayoutParams;
                        final Constraints.LayoutParams layoutParams = new Constraints.LayoutParams(((ViewGroup.LayoutParams) complicationLayoutParams).width, ((ViewGroup.LayoutParams) complicationLayoutParams).height);
                        ComplicationLayoutParams complicationLayoutParams2 = next.mLayoutParams;
                        Objects.requireNonNull(complicationLayoutParams2);
                        final int i = complicationLayoutParams2.mDirection;
                        ComplicationLayoutParams complicationLayoutParams3 = next.mLayoutParams;
                        Objects.requireNonNull(complicationLayoutParams3);
                        final boolean z2 = complicationLayoutParams3.mSnapToGuide;
                        if (view2 == next.mView) {
                            z = true;
                        } else {
                            z = false;
                        }
                        ComplicationLayoutParams complicationLayoutParams4 = next.mLayoutParams;
                        Consumer complicationLayoutEngine$ViewEntry$$ExternalSyntheticLambda0 = new Consumer() { // from class: com.android.systemui.dreams.complication.ComplicationLayoutEngine$ViewEntry$$ExternalSyntheticLambda0
                            @Override // java.util.function.Consumer
                            public final void accept(Object obj) {
                                ComplicationLayoutEngine.ViewEntry viewEntry3 = ComplicationLayoutEngine.ViewEntry.this;
                                boolean z3 = z;
                                int i2 = i;
                                ConstraintLayout.LayoutParams layoutParams2 = layoutParams;
                                View view3 = view2;
                                boolean z4 = z2;
                                Objects.requireNonNull(viewEntry3);
                                int intValue = ((Integer) obj).intValue();
                                if (intValue == 1) {
                                    if (z3 || i2 != 2) {
                                        layoutParams2.topToTop = 0;
                                    } else {
                                        layoutParams2.topToBottom = view3.getId();
                                    }
                                    if (z4 && (i2 == 8 || i2 == 4)) {
                                        layoutParams2.endToStart = 2131427728;
                                    }
                                } else if (intValue == 2) {
                                    if (z3 || i2 != 1) {
                                        layoutParams2.bottomToBottom = 0;
                                    } else {
                                        layoutParams2.bottomToTop = view3.getId();
                                    }
                                    if (z4 && (i2 == 8 || i2 == 4)) {
                                        layoutParams2.topToBottom = 2131427725;
                                    }
                                } else if (intValue == 4) {
                                    if (z3 || i2 != 8) {
                                        layoutParams2.startToStart = 0;
                                    } else {
                                        layoutParams2.startToEnd = view3.getId();
                                    }
                                    if (z4 && (i2 == 2 || i2 == 1)) {
                                        layoutParams2.endToStart = 2131427727;
                                    }
                                } else if (intValue == 8) {
                                    if (z3 || i2 != 4) {
                                        layoutParams2.endToEnd = 0;
                                    } else {
                                        layoutParams2.endToStart = view3.getId();
                                    }
                                    if (z4 && (i2 == 1 || i2 == 2)) {
                                        layoutParams2.startToEnd = 2131427726;
                                    }
                                }
                                if (z3) {
                                    return;
                                }
                                if (i2 == 1) {
                                    layoutParams2.setMargins(0, 0, 0, viewEntry3.mMargin);
                                } else if (i2 == 2) {
                                    layoutParams2.setMargins(0, viewEntry3.mMargin, 0, 0);
                                } else if (i2 == 4) {
                                    layoutParams2.setMarginEnd(viewEntry3.mMargin);
                                } else if (i2 == 8) {
                                    layoutParams2.setMarginStart(viewEntry3.mMargin);
                                }
                            }
                        };
                        for (int i2 = 1; i2 <= 8; i2 <<= 1) {
                            if ((complicationLayoutParams4.mPosition & i2) == i2) {
                                complicationLayoutEngine$ViewEntry$$ExternalSyntheticLambda0.accept(Integer.valueOf(i2));
                            }
                        }
                        Objects.requireNonNull(complicationLayoutParams4);
                        next.mView.setLayoutParams(layoutParams);
                        view2 = next.mView;
                    }
                }
            }
        }
    }

    /* loaded from: classes.dex */
    public static class ViewEntry implements Comparable<ViewEntry> {
        public final int mCategory;
        public final ComplicationLayoutParams mLayoutParams;
        public final int mMargin;
        public final Parent mParent;
        public final View mView;

        /* loaded from: classes.dex */
        public interface Parent {
        }

        public final int compareTo(ViewEntry viewEntry) {
            int i = viewEntry.mCategory;
            int i2 = this.mCategory;
            if (i != i2) {
                return i2 == 2 ? 1 : -1;
            }
            ComplicationLayoutParams complicationLayoutParams = viewEntry.mLayoutParams;
            Objects.requireNonNull(complicationLayoutParams);
            int i3 = complicationLayoutParams.mWeight;
            ComplicationLayoutParams complicationLayoutParams2 = this.mLayoutParams;
            Objects.requireNonNull(complicationLayoutParams2);
            if (i3 == complicationLayoutParams2.mWeight) {
                return 0;
            }
            ComplicationLayoutParams complicationLayoutParams3 = this.mLayoutParams;
            Objects.requireNonNull(complicationLayoutParams3);
            int i4 = complicationLayoutParams3.mWeight;
            ComplicationLayoutParams complicationLayoutParams4 = viewEntry.mLayoutParams;
            Objects.requireNonNull(complicationLayoutParams4);
            return i4 > complicationLayoutParams4.mWeight ? 1 : -1;
        }

        public ViewEntry(View view, ComplicationLayoutParams complicationLayoutParams, int i, Parent parent, int i2) {
            this.mView = view;
            view.setId(View.generateViewId());
            this.mLayoutParams = complicationLayoutParams;
            this.mCategory = i;
            this.mParent = parent;
            this.mMargin = i2;
        }
    }

    /* loaded from: classes.dex */
    public static class DirectionGroup implements ViewEntry.Parent {
        public final Parent mParent;
        public final ArrayList<ViewEntry> mViews = new ArrayList<>();

        /* loaded from: classes.dex */
        public interface Parent {
        }

        public DirectionGroup(PositionGroup positionGroup) {
            this.mParent = positionGroup;
        }
    }

    public final void removeComplication(ComplicationId complicationId) {
        if (!this.mEntries.containsKey(complicationId)) {
            Log.e("ComplicationLayoutEngine", "could not find id:" + complicationId);
            return;
        }
        ViewEntry viewEntry = this.mEntries.get(complicationId);
        Objects.requireNonNull(viewEntry);
        DirectionGroup directionGroup = (DirectionGroup) viewEntry.mParent;
        Objects.requireNonNull(directionGroup);
        directionGroup.mViews.remove(viewEntry);
        ((PositionGroup) directionGroup.mParent).onEntriesChanged();
        ((ViewGroup) viewEntry.mView.getParent()).removeView(viewEntry.mView);
    }

    public ComplicationLayoutEngine(ConstraintLayout constraintLayout, int i) {
        this.mLayout = constraintLayout;
        this.mMargin = i;
    }
}
