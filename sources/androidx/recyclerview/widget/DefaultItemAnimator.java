package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class DefaultItemAnimator extends SimpleItemAnimator {
    public static TimeInterpolator sDefaultInterpolator;
    public ArrayList<RecyclerView.ViewHolder> mPendingRemovals = new ArrayList<>();
    public ArrayList<RecyclerView.ViewHolder> mPendingAdditions = new ArrayList<>();
    public ArrayList<MoveInfo> mPendingMoves = new ArrayList<>();
    public ArrayList<ChangeInfo> mPendingChanges = new ArrayList<>();
    public ArrayList<ArrayList<RecyclerView.ViewHolder>> mAdditionsList = new ArrayList<>();
    public ArrayList<ArrayList<MoveInfo>> mMovesList = new ArrayList<>();
    public ArrayList<ArrayList<ChangeInfo>> mChangesList = new ArrayList<>();
    public ArrayList<RecyclerView.ViewHolder> mAddAnimations = new ArrayList<>();
    public ArrayList<RecyclerView.ViewHolder> mMoveAnimations = new ArrayList<>();
    public ArrayList<RecyclerView.ViewHolder> mRemoveAnimations = new ArrayList<>();
    public ArrayList<RecyclerView.ViewHolder> mChangeAnimations = new ArrayList<>();

    /* loaded from: classes.dex */
    public static class ChangeInfo {
        public int fromX;
        public int fromY;
        public RecyclerView.ViewHolder newHolder;
        public RecyclerView.ViewHolder oldHolder;
        public int toX;
        public int toY;

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ChangeInfo{oldHolder=");
            m.append(this.oldHolder);
            m.append(", newHolder=");
            m.append(this.newHolder);
            m.append(", fromX=");
            m.append(this.fromX);
            m.append(", fromY=");
            m.append(this.fromY);
            m.append(", toX=");
            m.append(this.toX);
            m.append(", toY=");
            return Insets$$ExternalSyntheticOutline0.m(m, this.toY, '}');
        }

        public ChangeInfo(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
            this.oldHolder = viewHolder;
            this.newHolder = viewHolder2;
            this.fromX = i;
            this.fromY = i2;
            this.toX = i3;
            this.toY = i4;
        }
    }

    /* loaded from: classes.dex */
    public static class MoveInfo {
        public int fromX;
        public int fromY;
        public RecyclerView.ViewHolder holder;
        public int toX;
        public int toY;

        public MoveInfo(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4) {
            this.holder = viewHolder;
            this.fromX = i;
            this.fromY = i2;
            this.toX = i3;
            this.toY = i4;
        }
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final boolean animateChange(RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder viewHolder2, int i, int i2, int i3, int i4) {
        if (viewHolder == viewHolder2) {
            return animateMove(viewHolder, i, i2, i3, i4);
        }
        float translationX = viewHolder.itemView.getTranslationX();
        float translationY = viewHolder.itemView.getTranslationY();
        float alpha = viewHolder.itemView.getAlpha();
        resetAnimation(viewHolder);
        viewHolder.itemView.setTranslationX(translationX);
        viewHolder.itemView.setTranslationY(translationY);
        viewHolder.itemView.setAlpha(alpha);
        resetAnimation(viewHolder2);
        viewHolder2.itemView.setTranslationX(-((int) ((i3 - i) - translationX)));
        viewHolder2.itemView.setTranslationY(-((int) ((i4 - i2) - translationY)));
        viewHolder2.itemView.setAlpha(0.0f);
        this.mPendingChanges.add(new ChangeInfo(viewHolder, viewHolder2, i, i2, i3, i4));
        return true;
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final boolean animateMove(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, int i4) {
        View view = viewHolder.itemView;
        int translationX = i + ((int) view.getTranslationX());
        int translationY = i2 + ((int) viewHolder.itemView.getTranslationY());
        resetAnimation(viewHolder);
        int i5 = i3 - translationX;
        int i6 = i4 - translationY;
        if (i5 == 0 && i6 == 0) {
            dispatchAnimationFinished(viewHolder);
            return false;
        }
        if (i5 != 0) {
            view.setTranslationX(-i5);
        }
        if (i6 != 0) {
            view.setTranslationY(-i6);
        }
        this.mPendingMoves.add(new MoveInfo(viewHolder, translationX, translationY, i3, i4));
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public final void endAnimation(RecyclerView.ViewHolder viewHolder) {
        View view = viewHolder.itemView;
        view.animate().cancel();
        int size = this.mPendingMoves.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            } else if (this.mPendingMoves.get(size).holder == viewHolder) {
                view.setTranslationY(0.0f);
                view.setTranslationX(0.0f);
                dispatchAnimationFinished(viewHolder);
                this.mPendingMoves.remove(size);
            }
        }
        endChangeAnimation(this.mPendingChanges, viewHolder);
        if (this.mPendingRemovals.remove(viewHolder)) {
            view.setAlpha(1.0f);
            dispatchAnimationFinished(viewHolder);
        }
        if (this.mPendingAdditions.remove(viewHolder)) {
            view.setAlpha(1.0f);
            dispatchAnimationFinished(viewHolder);
        }
        int size2 = this.mChangesList.size();
        while (true) {
            size2--;
            if (size2 < 0) {
                break;
            }
            ArrayList<ChangeInfo> arrayList = this.mChangesList.get(size2);
            endChangeAnimation(arrayList, viewHolder);
            if (arrayList.isEmpty()) {
                this.mChangesList.remove(size2);
            }
        }
        int size3 = this.mMovesList.size();
        while (true) {
            size3--;
            if (size3 < 0) {
                break;
            }
            ArrayList<MoveInfo> arrayList2 = this.mMovesList.get(size3);
            int size4 = arrayList2.size();
            while (true) {
                size4--;
                if (size4 < 0) {
                    break;
                } else if (arrayList2.get(size4).holder == viewHolder) {
                    view.setTranslationY(0.0f);
                    view.setTranslationX(0.0f);
                    dispatchAnimationFinished(viewHolder);
                    arrayList2.remove(size4);
                    if (arrayList2.isEmpty()) {
                        this.mMovesList.remove(size3);
                    }
                }
            }
        }
        int size5 = this.mAdditionsList.size();
        while (true) {
            size5--;
            if (size5 >= 0) {
                ArrayList<RecyclerView.ViewHolder> arrayList3 = this.mAdditionsList.get(size5);
                if (arrayList3.remove(viewHolder)) {
                    view.setAlpha(1.0f);
                    dispatchAnimationFinished(viewHolder);
                    if (arrayList3.isEmpty()) {
                        this.mAdditionsList.remove(size5);
                    }
                }
            } else {
                this.mRemoveAnimations.remove(viewHolder);
                this.mAddAnimations.remove(viewHolder);
                this.mChangeAnimations.remove(viewHolder);
                this.mMoveAnimations.remove(viewHolder);
                dispatchFinishedWhenDone();
                return;
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public final void endAnimations() {
        int size = this.mPendingMoves.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            MoveInfo moveInfo = this.mPendingMoves.get(size);
            View view = moveInfo.holder.itemView;
            view.setTranslationY(0.0f);
            view.setTranslationX(0.0f);
            dispatchAnimationFinished(moveInfo.holder);
            this.mPendingMoves.remove(size);
        }
        int size2 = this.mPendingRemovals.size();
        while (true) {
            size2--;
            if (size2 < 0) {
                break;
            }
            dispatchAnimationFinished(this.mPendingRemovals.get(size2));
            this.mPendingRemovals.remove(size2);
        }
        int size3 = this.mPendingAdditions.size();
        while (true) {
            size3--;
            if (size3 < 0) {
                break;
            }
            RecyclerView.ViewHolder viewHolder = this.mPendingAdditions.get(size3);
            viewHolder.itemView.setAlpha(1.0f);
            dispatchAnimationFinished(viewHolder);
            this.mPendingAdditions.remove(size3);
        }
        int size4 = this.mPendingChanges.size();
        while (true) {
            size4--;
            if (size4 < 0) {
                break;
            }
            ChangeInfo changeInfo = this.mPendingChanges.get(size4);
            RecyclerView.ViewHolder viewHolder2 = changeInfo.oldHolder;
            if (viewHolder2 != null) {
                endChangeAnimationIfNecessary(changeInfo, viewHolder2);
            }
            RecyclerView.ViewHolder viewHolder3 = changeInfo.newHolder;
            if (viewHolder3 != null) {
                endChangeAnimationIfNecessary(changeInfo, viewHolder3);
            }
        }
        this.mPendingChanges.clear();
        if (isRunning()) {
            int size5 = this.mMovesList.size();
            while (true) {
                size5--;
                if (size5 < 0) {
                    break;
                }
                ArrayList<MoveInfo> arrayList = this.mMovesList.get(size5);
                int size6 = arrayList.size();
                while (true) {
                    size6--;
                    if (size6 >= 0) {
                        MoveInfo moveInfo2 = arrayList.get(size6);
                        View view2 = moveInfo2.holder.itemView;
                        view2.setTranslationY(0.0f);
                        view2.setTranslationX(0.0f);
                        dispatchAnimationFinished(moveInfo2.holder);
                        arrayList.remove(size6);
                        if (arrayList.isEmpty()) {
                            this.mMovesList.remove(arrayList);
                        }
                    }
                }
            }
            int size7 = this.mAdditionsList.size();
            while (true) {
                size7--;
                if (size7 < 0) {
                    break;
                }
                ArrayList<RecyclerView.ViewHolder> arrayList2 = this.mAdditionsList.get(size7);
                int size8 = arrayList2.size();
                while (true) {
                    size8--;
                    if (size8 >= 0) {
                        RecyclerView.ViewHolder viewHolder4 = arrayList2.get(size8);
                        viewHolder4.itemView.setAlpha(1.0f);
                        dispatchAnimationFinished(viewHolder4);
                        arrayList2.remove(size8);
                        if (arrayList2.isEmpty()) {
                            this.mAdditionsList.remove(arrayList2);
                        }
                    }
                }
            }
            int size9 = this.mChangesList.size();
            while (true) {
                size9--;
                if (size9 >= 0) {
                    ArrayList<ChangeInfo> arrayList3 = this.mChangesList.get(size9);
                    int size10 = arrayList3.size();
                    while (true) {
                        size10--;
                        if (size10 >= 0) {
                            ChangeInfo changeInfo2 = arrayList3.get(size10);
                            RecyclerView.ViewHolder viewHolder5 = changeInfo2.oldHolder;
                            if (viewHolder5 != null) {
                                endChangeAnimationIfNecessary(changeInfo2, viewHolder5);
                            }
                            RecyclerView.ViewHolder viewHolder6 = changeInfo2.newHolder;
                            if (viewHolder6 != null) {
                                endChangeAnimationIfNecessary(changeInfo2, viewHolder6);
                            }
                            if (arrayList3.isEmpty()) {
                                this.mChangesList.remove(arrayList3);
                            }
                        }
                    }
                } else {
                    cancelAll(this.mRemoveAnimations);
                    cancelAll(this.mMoveAnimations);
                    cancelAll(this.mAddAnimations);
                    cancelAll(this.mChangeAnimations);
                    dispatchAnimationsFinished();
                    return;
                }
            }
        }
    }

    public final boolean endChangeAnimationIfNecessary(ChangeInfo changeInfo, RecyclerView.ViewHolder viewHolder) {
        if (changeInfo.newHolder == viewHolder) {
            changeInfo.newHolder = null;
        } else if (changeInfo.oldHolder != viewHolder) {
            return false;
        } else {
            changeInfo.oldHolder = null;
        }
        viewHolder.itemView.setAlpha(1.0f);
        viewHolder.itemView.setTranslationX(0.0f);
        viewHolder.itemView.setTranslationY(0.0f);
        dispatchAnimationFinished(viewHolder);
        return true;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public final boolean isRunning() {
        if (!this.mPendingAdditions.isEmpty() || !this.mPendingChanges.isEmpty() || !this.mPendingMoves.isEmpty() || !this.mPendingRemovals.isEmpty() || !this.mMoveAnimations.isEmpty() || !this.mRemoveAnimations.isEmpty() || !this.mAddAnimations.isEmpty() || !this.mChangeAnimations.isEmpty() || !this.mMovesList.isEmpty() || !this.mAdditionsList.isEmpty() || !this.mChangesList.isEmpty()) {
            return true;
        }
        return false;
    }

    public final void resetAnimation(RecyclerView.ViewHolder viewHolder) {
        if (sDefaultInterpolator == null) {
            sDefaultInterpolator = new ValueAnimator().getInterpolator();
        }
        viewHolder.itemView.animate().setInterpolator(sDefaultInterpolator);
        endAnimation(viewHolder);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public final void runPendingAnimations() {
        long j;
        long j2;
        boolean z = !this.mPendingRemovals.isEmpty();
        boolean z2 = !this.mPendingMoves.isEmpty();
        boolean z3 = !this.mPendingChanges.isEmpty();
        boolean z4 = !this.mPendingAdditions.isEmpty();
        if (z || z2 || z4 || z3) {
            Iterator<RecyclerView.ViewHolder> it = this.mPendingRemovals.iterator();
            while (it.hasNext()) {
                final RecyclerView.ViewHolder next = it.next();
                final View view = next.itemView;
                final ViewPropertyAnimator animate = view.animate();
                this.mRemoveAnimations.add(next);
                animate.setDuration(this.mRemoveDuration).alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.DefaultItemAnimator.4
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        animate.setListener(null);
                        view.setAlpha(1.0f);
                        DefaultItemAnimator defaultItemAnimator = DefaultItemAnimator.this;
                        RecyclerView.ViewHolder viewHolder = next;
                        Objects.requireNonNull(defaultItemAnimator);
                        defaultItemAnimator.dispatchAnimationFinished(viewHolder);
                        DefaultItemAnimator.this.mRemoveAnimations.remove(next);
                        DefaultItemAnimator.this.dispatchFinishedWhenDone();
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationStart(Animator animator) {
                        Objects.requireNonNull(DefaultItemAnimator.this);
                    }
                }).start();
            }
            this.mPendingRemovals.clear();
            if (z2) {
                final ArrayList<MoveInfo> arrayList = new ArrayList<>();
                arrayList.addAll(this.mPendingMoves);
                this.mMovesList.add(arrayList);
                this.mPendingMoves.clear();
                Runnable runnable = new Runnable() { // from class: androidx.recyclerview.widget.DefaultItemAnimator.1
                    @Override // java.lang.Runnable
                    public final void run() {
                        Iterator it2 = arrayList.iterator();
                        while (it2.hasNext()) {
                            MoveInfo moveInfo = (MoveInfo) it2.next();
                            final DefaultItemAnimator defaultItemAnimator = DefaultItemAnimator.this;
                            final RecyclerView.ViewHolder viewHolder = moveInfo.holder;
                            int i = moveInfo.fromX;
                            int i2 = moveInfo.fromY;
                            int i3 = moveInfo.toX;
                            int i4 = moveInfo.toY;
                            Objects.requireNonNull(defaultItemAnimator);
                            final View view2 = viewHolder.itemView;
                            final int i5 = i3 - i;
                            final int i6 = i4 - i2;
                            if (i5 != 0) {
                                view2.animate().translationX(0.0f);
                            }
                            if (i6 != 0) {
                                view2.animate().translationY(0.0f);
                            }
                            final ViewPropertyAnimator animate2 = view2.animate();
                            defaultItemAnimator.mMoveAnimations.add(viewHolder);
                            animate2.setDuration(defaultItemAnimator.mMoveDuration).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.DefaultItemAnimator.6
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationCancel(Animator animator) {
                                    if (i5 != 0) {
                                        view2.setTranslationX(0.0f);
                                    }
                                    if (i6 != 0) {
                                        view2.setTranslationY(0.0f);
                                    }
                                }

                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationEnd(Animator animator) {
                                    animate2.setListener(null);
                                    DefaultItemAnimator defaultItemAnimator2 = DefaultItemAnimator.this;
                                    RecyclerView.ViewHolder viewHolder2 = viewHolder;
                                    Objects.requireNonNull(defaultItemAnimator2);
                                    defaultItemAnimator2.dispatchAnimationFinished(viewHolder2);
                                    DefaultItemAnimator.this.mMoveAnimations.remove(viewHolder);
                                    DefaultItemAnimator.this.dispatchFinishedWhenDone();
                                }

                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationStart(Animator animator) {
                                    Objects.requireNonNull(DefaultItemAnimator.this);
                                }
                            }).start();
                        }
                        arrayList.clear();
                        DefaultItemAnimator.this.mMovesList.remove(arrayList);
                    }
                };
                if (z) {
                    View view2 = arrayList.get(0).holder.itemView;
                    long j3 = this.mRemoveDuration;
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                    ViewCompat.Api16Impl.postOnAnimationDelayed(view2, runnable, j3);
                } else {
                    runnable.run();
                }
            }
            if (z3) {
                final ArrayList<ChangeInfo> arrayList2 = new ArrayList<>();
                arrayList2.addAll(this.mPendingChanges);
                this.mChangesList.add(arrayList2);
                this.mPendingChanges.clear();
                Runnable runnable2 = new Runnable() { // from class: androidx.recyclerview.widget.DefaultItemAnimator.2
                    @Override // java.lang.Runnable
                    public final void run() {
                        final View view3;
                        Iterator it2 = arrayList2.iterator();
                        while (it2.hasNext()) {
                            final ChangeInfo changeInfo = (ChangeInfo) it2.next();
                            final DefaultItemAnimator defaultItemAnimator = DefaultItemAnimator.this;
                            Objects.requireNonNull(defaultItemAnimator);
                            RecyclerView.ViewHolder viewHolder = changeInfo.oldHolder;
                            final View view4 = null;
                            if (viewHolder == null) {
                                view3 = null;
                            } else {
                                view3 = viewHolder.itemView;
                            }
                            RecyclerView.ViewHolder viewHolder2 = changeInfo.newHolder;
                            if (viewHolder2 != null) {
                                view4 = viewHolder2.itemView;
                            }
                            if (view3 != null) {
                                final ViewPropertyAnimator duration = view3.animate().setDuration(defaultItemAnimator.mChangeDuration);
                                defaultItemAnimator.mChangeAnimations.add(changeInfo.oldHolder);
                                duration.translationX(changeInfo.toX - changeInfo.fromX);
                                duration.translationY(changeInfo.toY - changeInfo.fromY);
                                duration.alpha(0.0f).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.DefaultItemAnimator.7
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public final void onAnimationEnd(Animator animator) {
                                        duration.setListener(null);
                                        view3.setAlpha(1.0f);
                                        view3.setTranslationX(0.0f);
                                        view3.setTranslationY(0.0f);
                                        DefaultItemAnimator defaultItemAnimator2 = DefaultItemAnimator.this;
                                        RecyclerView.ViewHolder viewHolder3 = changeInfo.oldHolder;
                                        Objects.requireNonNull(defaultItemAnimator2);
                                        defaultItemAnimator2.dispatchAnimationFinished(viewHolder3);
                                        DefaultItemAnimator.this.mChangeAnimations.remove(changeInfo.oldHolder);
                                        DefaultItemAnimator.this.dispatchFinishedWhenDone();
                                    }

                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public final void onAnimationStart(Animator animator) {
                                        DefaultItemAnimator defaultItemAnimator2 = DefaultItemAnimator.this;
                                        RecyclerView.ViewHolder viewHolder3 = changeInfo.oldHolder;
                                        Objects.requireNonNull(defaultItemAnimator2);
                                    }
                                }).start();
                            }
                            if (view4 != null) {
                                final ViewPropertyAnimator animate2 = view4.animate();
                                defaultItemAnimator.mChangeAnimations.add(changeInfo.newHolder);
                                animate2.translationX(0.0f).translationY(0.0f).setDuration(defaultItemAnimator.mChangeDuration).alpha(1.0f).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.DefaultItemAnimator.8
                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public final void onAnimationEnd(Animator animator) {
                                        animate2.setListener(null);
                                        view4.setAlpha(1.0f);
                                        view4.setTranslationX(0.0f);
                                        view4.setTranslationY(0.0f);
                                        DefaultItemAnimator defaultItemAnimator2 = DefaultItemAnimator.this;
                                        RecyclerView.ViewHolder viewHolder3 = changeInfo.newHolder;
                                        Objects.requireNonNull(defaultItemAnimator2);
                                        defaultItemAnimator2.dispatchAnimationFinished(viewHolder3);
                                        DefaultItemAnimator.this.mChangeAnimations.remove(changeInfo.newHolder);
                                        DefaultItemAnimator.this.dispatchFinishedWhenDone();
                                    }

                                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                    public final void onAnimationStart(Animator animator) {
                                        DefaultItemAnimator defaultItemAnimator2 = DefaultItemAnimator.this;
                                        RecyclerView.ViewHolder viewHolder3 = changeInfo.newHolder;
                                        Objects.requireNonNull(defaultItemAnimator2);
                                    }
                                }).start();
                            }
                        }
                        arrayList2.clear();
                        DefaultItemAnimator.this.mChangesList.remove(arrayList2);
                    }
                };
                if (z) {
                    View view3 = arrayList2.get(0).oldHolder.itemView;
                    long j4 = this.mRemoveDuration;
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                    ViewCompat.Api16Impl.postOnAnimationDelayed(view3, runnable2, j4);
                } else {
                    runnable2.run();
                }
            }
            if (z4) {
                final ArrayList<RecyclerView.ViewHolder> arrayList3 = new ArrayList<>();
                arrayList3.addAll(this.mPendingAdditions);
                this.mAdditionsList.add(arrayList3);
                this.mPendingAdditions.clear();
                Runnable runnable3 = new Runnable() { // from class: androidx.recyclerview.widget.DefaultItemAnimator.3
                    @Override // java.lang.Runnable
                    public final void run() {
                        Iterator it2 = arrayList3.iterator();
                        while (it2.hasNext()) {
                            final RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) it2.next();
                            final DefaultItemAnimator defaultItemAnimator = DefaultItemAnimator.this;
                            Objects.requireNonNull(defaultItemAnimator);
                            final View view4 = viewHolder.itemView;
                            final ViewPropertyAnimator animate2 = view4.animate();
                            defaultItemAnimator.mAddAnimations.add(viewHolder);
                            animate2.alpha(1.0f).setDuration(defaultItemAnimator.mAddDuration).setListener(new AnimatorListenerAdapter() { // from class: androidx.recyclerview.widget.DefaultItemAnimator.5
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationCancel(Animator animator) {
                                    view4.setAlpha(1.0f);
                                }

                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationEnd(Animator animator) {
                                    animate2.setListener(null);
                                    DefaultItemAnimator defaultItemAnimator2 = DefaultItemAnimator.this;
                                    RecyclerView.ViewHolder viewHolder2 = viewHolder;
                                    Objects.requireNonNull(defaultItemAnimator2);
                                    defaultItemAnimator2.dispatchAnimationFinished(viewHolder2);
                                    DefaultItemAnimator.this.mAddAnimations.remove(viewHolder);
                                    DefaultItemAnimator.this.dispatchFinishedWhenDone();
                                }

                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationStart(Animator animator) {
                                    Objects.requireNonNull(DefaultItemAnimator.this);
                                }
                            }).start();
                        }
                        arrayList3.clear();
                        DefaultItemAnimator.this.mAdditionsList.remove(arrayList3);
                    }
                };
                if (z || z2 || z3) {
                    long j5 = 0;
                    if (z) {
                        j = this.mRemoveDuration;
                    } else {
                        j = 0;
                    }
                    if (z2) {
                        j2 = this.mMoveDuration;
                    } else {
                        j2 = 0;
                    }
                    if (z3) {
                        j5 = this.mChangeDuration;
                    }
                    View view4 = arrayList3.get(0).itemView;
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap3 = ViewCompat.sViewPropertyAnimatorMap;
                    ViewCompat.Api16Impl.postOnAnimationDelayed(view4, runnable3, Math.max(j2, j5) + j);
                    return;
                }
                runnable3.run();
            }
        }
    }

    public static void cancelAll(ArrayList arrayList) {
        int size = arrayList.size();
        while (true) {
            size--;
            if (size >= 0) {
                ((RecyclerView.ViewHolder) arrayList.get(size)).itemView.animate().cancel();
            } else {
                return;
            }
        }
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final void animateAdd(RecyclerView.ViewHolder viewHolder) {
        resetAnimation(viewHolder);
        viewHolder.itemView.setAlpha(0.0f);
        this.mPendingAdditions.add(viewHolder);
    }

    @Override // androidx.recyclerview.widget.SimpleItemAnimator
    public final void animateRemove(RecyclerView.ViewHolder viewHolder) {
        resetAnimation(viewHolder);
        this.mPendingRemovals.add(viewHolder);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemAnimator
    public final boolean canReuseUpdatedViewHolder(RecyclerView.ViewHolder viewHolder, List<Object> list) {
        if (!list.isEmpty() || super.canReuseUpdatedViewHolder(viewHolder, list)) {
            return true;
        }
        return false;
    }

    public final void dispatchFinishedWhenDone() {
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }

    public final void endChangeAnimation(ArrayList arrayList, RecyclerView.ViewHolder viewHolder) {
        int size = arrayList.size();
        while (true) {
            size--;
            if (size >= 0) {
                ChangeInfo changeInfo = (ChangeInfo) arrayList.get(size);
                if (endChangeAnimationIfNecessary(changeInfo, viewHolder) && changeInfo.oldHolder == null && changeInfo.newHolder == null) {
                    arrayList.remove(changeInfo);
                }
            } else {
                return;
            }
        }
    }
}
