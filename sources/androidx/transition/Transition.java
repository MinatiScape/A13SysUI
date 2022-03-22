package androidx.transition;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Path;
import android.graphics.Rect;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.view.InflateException;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ListView;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.collection.ArrayMap;
import androidx.collection.LongSparseArray;
import androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline0;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.R$dimen;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public abstract class Transition implements Cloneable {
    public static final int[] DEFAULT_MATCH_ORDER = {2, 1, 3, 4};
    public static final AnonymousClass1 STRAIGHT_PATH_MOTION = new PathMotion() { // from class: androidx.transition.Transition.1
        @Override // androidx.transition.PathMotion
        public final Path getPath(float f, float f2, float f3, float f4) {
            Path path = new Path();
            path.moveTo(f, f2);
            path.lineTo(f3, f4);
            return path;
        }
    };
    public static ThreadLocal<ArrayMap<Animator, AnimationInfo>> sRunningAnimators = new ThreadLocal<>();
    public ArrayList<Animator> mAnimators;
    public ArrayList<Animator> mCurrentAnimators;
    public long mDuration;
    public TransitionValuesMaps mEndValues;
    public ArrayList<TransitionValues> mEndValuesList;
    public boolean mEnded;
    public EpicenterCallback mEpicenterCallback;
    public TimeInterpolator mInterpolator;
    public ArrayList<TransitionListener> mListeners;
    public int[] mMatchOrder;
    public String mName;
    public int mNumInstances;
    public TransitionSet mParent;
    public PathMotion mPathMotion;
    public boolean mPaused;
    public TransitionPropagation mPropagation;
    public long mStartDelay;
    public TransitionValuesMaps mStartValues;
    public ArrayList<TransitionValues> mStartValuesList;
    public ArrayList<Integer> mTargetIds;
    public ArrayList<View> mTargets;

    /* loaded from: classes.dex */
    public static abstract class EpicenterCallback {
        public abstract Rect onGetEpicenter();
    }

    /* loaded from: classes.dex */
    public interface TransitionListener {
        void onTransitionCancel();

        void onTransitionEnd(Transition transition);

        void onTransitionPause();

        void onTransitionResume();

        void onTransitionStart(Transition transition);
    }

    public Transition() {
        this.mName = getClass().getName();
        this.mStartDelay = -1L;
        this.mDuration = -1L;
        this.mInterpolator = null;
        this.mTargetIds = new ArrayList<>();
        this.mTargets = new ArrayList<>();
        this.mStartValues = new TransitionValuesMaps();
        this.mEndValues = new TransitionValuesMaps();
        this.mParent = null;
        this.mMatchOrder = DEFAULT_MATCH_ORDER;
        this.mCurrentAnimators = new ArrayList<>();
        this.mNumInstances = 0;
        this.mPaused = false;
        this.mEnded = false;
        this.mListeners = null;
        this.mAnimators = new ArrayList<>();
        this.mPathMotion = STRAIGHT_PATH_MOTION;
    }

    public abstract void captureEndValues(TransitionValues transitionValues);

    public abstract void captureStartValues(TransitionValues transitionValues);

    public Animator createAnimator(ViewGroup viewGroup, TransitionValues transitionValues, TransitionValues transitionValues2) {
        return null;
    }

    public String[] getTransitionProperties() {
        return null;
    }

    public boolean isTransitionRequired(TransitionValues transitionValues, TransitionValues transitionValues2) {
        if (transitionValues == null || transitionValues2 == null) {
            return false;
        }
        String[] transitionProperties = getTransitionProperties();
        if (transitionProperties != null) {
            for (String str : transitionProperties) {
                if (!isValueChanged(transitionValues, transitionValues2, str)) {
                }
            }
            return false;
        }
        for (String str2 : transitionValues.values.keySet()) {
            if (isValueChanged(transitionValues, transitionValues2, str2)) {
            }
        }
        return false;
        return true;
    }

    public String toString(String str) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(str);
        m.append(getClass().getSimpleName());
        m.append("@");
        m.append(Integer.toHexString(hashCode()));
        m.append(": ");
        String sb = m.toString();
        if (this.mDuration != -1) {
            StringBuilder m2 = DebugInfo$$ExternalSyntheticOutline0.m(sb, "dur(");
            m2.append(this.mDuration);
            m2.append(") ");
            sb = m2.toString();
        }
        if (this.mStartDelay != -1) {
            StringBuilder m3 = DebugInfo$$ExternalSyntheticOutline0.m(sb, "dly(");
            m3.append(this.mStartDelay);
            m3.append(") ");
            sb = m3.toString();
        }
        if (this.mInterpolator != null) {
            StringBuilder m4 = DebugInfo$$ExternalSyntheticOutline0.m(sb, "interp(");
            m4.append(this.mInterpolator);
            m4.append(") ");
            sb = m4.toString();
        }
        if (this.mTargetIds.size() <= 0 && this.mTargets.size() <= 0) {
            return sb;
        }
        String m5 = SupportMenuInflater$$ExternalSyntheticOutline0.m(sb, "tgts(");
        if (this.mTargetIds.size() > 0) {
            for (int i = 0; i < this.mTargetIds.size(); i++) {
                if (i > 0) {
                    m5 = SupportMenuInflater$$ExternalSyntheticOutline0.m(m5, ", ");
                }
                StringBuilder m6 = VendorAtomValue$$ExternalSyntheticOutline1.m(m5);
                m6.append(this.mTargetIds.get(i));
                m5 = m6.toString();
            }
        }
        if (this.mTargets.size() > 0) {
            for (int i2 = 0; i2 < this.mTargets.size(); i2++) {
                if (i2 > 0) {
                    m5 = SupportMenuInflater$$ExternalSyntheticOutline0.m(m5, ", ");
                }
                StringBuilder m7 = VendorAtomValue$$ExternalSyntheticOutline1.m(m5);
                m7.append(this.mTargets.get(i2));
                m5 = m7.toString();
            }
        }
        return SupportMenuInflater$$ExternalSyntheticOutline0.m(m5, ")");
    }

    /* loaded from: classes.dex */
    public static class AnimationInfo {
        public String mName;
        public Transition mTransition;
        public TransitionValues mValues;
        public View mView;
        public WindowIdImpl mWindowId;

        public AnimationInfo(View view, String str, Transition transition, WindowIdApi18 windowIdApi18, TransitionValues transitionValues) {
            this.mView = view;
            this.mName = str;
            this.mValues = transitionValues;
            this.mWindowId = windowIdApi18;
            this.mTransition = transition;
        }
    }

    public static void addViewValues(TransitionValuesMaps transitionValuesMaps, View view, TransitionValues transitionValues) {
        transitionValuesMaps.mViewValues.put(view, transitionValues);
        int id = view.getId();
        if (id >= 0) {
            if (transitionValuesMaps.mIdValues.indexOfKey(id) >= 0) {
                transitionValuesMaps.mIdValues.put(id, null);
            } else {
                transitionValuesMaps.mIdValues.put(id, view);
            }
        }
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        String transitionName = ViewCompat.Api21Impl.getTransitionName(view);
        if (transitionName != null) {
            if (transitionValuesMaps.mNameValues.containsKey(transitionName)) {
                transitionValuesMaps.mNameValues.put(transitionName, null);
            } else {
                transitionValuesMaps.mNameValues.put(transitionName, view);
            }
        }
        if (view.getParent() instanceof ListView) {
            ListView listView = (ListView) view.getParent();
            if (listView.getAdapter().hasStableIds()) {
                long itemIdAtPosition = listView.getItemIdAtPosition(listView.getPositionForView(view));
                LongSparseArray<View> longSparseArray = transitionValuesMaps.mItemIdValues;
                Objects.requireNonNull(longSparseArray);
                if (longSparseArray.mGarbage) {
                    longSparseArray.gc();
                }
                if (R$dimen.binarySearch(longSparseArray.mKeys, longSparseArray.mSize, itemIdAtPosition) >= 0) {
                    LongSparseArray<View> longSparseArray2 = transitionValuesMaps.mItemIdValues;
                    Objects.requireNonNull(longSparseArray2);
                    View view2 = (View) longSparseArray2.get(itemIdAtPosition, null);
                    if (view2 != null) {
                        ViewCompat.Api16Impl.setHasTransientState(view2, false);
                        transitionValuesMaps.mItemIdValues.put(itemIdAtPosition, null);
                        return;
                    }
                    return;
                }
                ViewCompat.Api16Impl.setHasTransientState(view, true);
                transitionValuesMaps.mItemIdValues.put(itemIdAtPosition, view);
            }
        }
    }

    public static ArrayMap<Animator, AnimationInfo> getRunningAnimators() {
        ArrayMap<Animator, AnimationInfo> arrayMap = sRunningAnimators.get();
        if (arrayMap != null) {
            return arrayMap;
        }
        ArrayMap<Animator, AnimationInfo> arrayMap2 = new ArrayMap<>();
        sRunningAnimators.set(arrayMap2);
        return arrayMap2;
    }

    public static boolean isValueChanged(TransitionValues transitionValues, TransitionValues transitionValues2, String str) {
        Object obj = transitionValues.values.get(str);
        Object obj2 = transitionValues2.values.get(str);
        if (obj == null && obj2 == null) {
            return false;
        }
        if (obj == null || obj2 == null) {
            return true;
        }
        return true ^ obj.equals(obj2);
    }

    public Transition addListener(TransitionListener transitionListener) {
        if (this.mListeners == null) {
            this.mListeners = new ArrayList<>();
        }
        this.mListeners.add(transitionListener);
        return this;
    }

    public Transition addTarget(View view) {
        this.mTargets.add(view);
        return this;
    }

    public void cancel() {
        int size = this.mCurrentAnimators.size();
        while (true) {
            size--;
            if (size < 0) {
                break;
            }
            this.mCurrentAnimators.get(size).cancel();
        }
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList != null && arrayList.size() > 0) {
            ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
            int size2 = arrayList2.size();
            for (int i = 0; i < size2; i++) {
                ((TransitionListener) arrayList2.get(i)).onTransitionCancel();
            }
        }
    }

    public final void captureHierarchy(View view, boolean z) {
        if (view != null) {
            view.getId();
            if (view.getParent() instanceof ViewGroup) {
                TransitionValues transitionValues = new TransitionValues(view);
                if (z) {
                    captureStartValues(transitionValues);
                } else {
                    captureEndValues(transitionValues);
                }
                transitionValues.mTargetedTransitions.add(this);
                capturePropagationValues(transitionValues);
                if (z) {
                    addViewValues(this.mStartValues, view, transitionValues);
                } else {
                    addViewValues(this.mEndValues, view, transitionValues);
                }
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                for (int i = 0; i < viewGroup.getChildCount(); i++) {
                    captureHierarchy(viewGroup.getChildAt(i), z);
                }
            }
        }
    }

    public void capturePropagationValues(TransitionValues transitionValues) {
        if (this.mPropagation != null && !transitionValues.values.isEmpty()) {
            this.mPropagation.getPropagationProperties();
            String[] strArr = VisibilityPropagation.VISIBILITY_PROPAGATION_VALUES;
            boolean z = false;
            int i = 0;
            while (true) {
                if (i >= 2) {
                    z = true;
                    break;
                } else if (!transitionValues.values.containsKey(strArr[i])) {
                    break;
                } else {
                    i++;
                }
            }
            if (!z) {
                this.mPropagation.captureValues(transitionValues);
            }
        }
    }

    public final void clearValues(boolean z) {
        if (z) {
            this.mStartValues.mViewValues.clear();
            this.mStartValues.mIdValues.clear();
            this.mStartValues.mItemIdValues.clear();
            return;
        }
        this.mEndValues.mViewValues.clear();
        this.mEndValues.mIdValues.clear();
        this.mEndValues.mItemIdValues.clear();
    }

    public Transition clone() {
        try {
            Transition transition = (Transition) super.clone();
            transition.mAnimators = new ArrayList<>();
            transition.mStartValues = new TransitionValuesMaps();
            transition.mEndValues = new TransitionValuesMaps();
            transition.mStartValuesList = null;
            transition.mEndValuesList = null;
            return transition;
        } catch (CloneNotSupportedException unused) {
            return null;
        }
    }

    public void createAnimators(ViewGroup viewGroup, TransitionValuesMaps transitionValuesMaps, TransitionValuesMaps transitionValuesMaps2, ArrayList<TransitionValues> arrayList, ArrayList<TransitionValues> arrayList2) {
        int i;
        int i2;
        boolean z;
        Animator createAnimator;
        View view;
        Animator animator;
        TransitionValues transitionValues;
        Animator animator2;
        Animator animator3;
        TransitionValues transitionValues2;
        ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        SparseIntArray sparseIntArray = new SparseIntArray();
        int size = arrayList.size();
        long j = Long.MAX_VALUE;
        int i3 = 0;
        while (i3 < size) {
            TransitionValues transitionValues3 = arrayList.get(i3);
            TransitionValues transitionValues4 = arrayList2.get(i3);
            if (transitionValues3 != null && !transitionValues3.mTargetedTransitions.contains(this)) {
                transitionValues3 = null;
            }
            if (transitionValues4 != null && !transitionValues4.mTargetedTransitions.contains(this)) {
                transitionValues4 = null;
            }
            if (!(transitionValues3 == null && transitionValues4 == null)) {
                if (transitionValues3 == null || transitionValues4 == null || isTransitionRequired(transitionValues3, transitionValues4)) {
                    z = true;
                } else {
                    z = false;
                }
                if (z && (createAnimator = createAnimator(viewGroup, transitionValues3, transitionValues4)) != null) {
                    if (transitionValues4 != null) {
                        view = transitionValues4.view;
                        String[] transitionProperties = getTransitionProperties();
                        if (transitionProperties != null && transitionProperties.length > 0) {
                            transitionValues2 = new TransitionValues(view);
                            animator3 = createAnimator;
                            i2 = size;
                            ArrayMap<View, TransitionValues> arrayMap = transitionValuesMaps2.mViewValues;
                            Objects.requireNonNull(arrayMap);
                            TransitionValues orDefault = arrayMap.getOrDefault(view, null);
                            if (orDefault != null) {
                                int i4 = 0;
                                while (i4 < transitionProperties.length) {
                                    transitionValues2.values.put(transitionProperties[i4], orDefault.values.get(transitionProperties[i4]));
                                    i4++;
                                    i3 = i3;
                                    orDefault = orDefault;
                                }
                            }
                            i = i3;
                            int i5 = runningAnimators.mSize;
                            for (int i6 = 0; i6 < i5; i6++) {
                                AnimationInfo orDefault2 = runningAnimators.getOrDefault(runningAnimators.keyAt(i6), null);
                                if (orDefault2.mValues != null && orDefault2.mView == view && orDefault2.mName.equals(this.mName) && orDefault2.mValues.equals(transitionValues2)) {
                                    transitionValues = transitionValues2;
                                    animator2 = null;
                                    break;
                                }
                            }
                        } else {
                            animator3 = createAnimator;
                            i2 = size;
                            i = i3;
                            transitionValues2 = null;
                        }
                        transitionValues = transitionValues2;
                        animator2 = animator3;
                        animator = animator2;
                    } else {
                        i2 = size;
                        i = i3;
                        view = transitionValues3.view;
                        transitionValues = null;
                        animator = createAnimator;
                    }
                    if (animator != null) {
                        TransitionPropagation transitionPropagation = this.mPropagation;
                        if (transitionPropagation != null) {
                            long startDelay = transitionPropagation.getStartDelay(viewGroup, this, transitionValues3, transitionValues4);
                            sparseIntArray.put(this.mAnimators.size(), (int) startDelay);
                            j = Math.min(startDelay, j);
                        }
                        String str = this.mName;
                        ViewUtilsApi29 viewUtilsApi29 = ViewUtils.IMPL;
                        runningAnimators.put(animator, new AnimationInfo(view, str, this, new WindowIdApi18(viewGroup), transitionValues));
                        this.mAnimators.add(animator);
                        j = j;
                    }
                    i3 = i + 1;
                    size = i2;
                }
            }
            i2 = size;
            i = i3;
            i3 = i + 1;
            size = i2;
        }
        if (sparseIntArray.size() != 0) {
            for (int i7 = 0; i7 < sparseIntArray.size(); i7++) {
                Animator animator4 = this.mAnimators.get(sparseIntArray.keyAt(i7));
                animator4.setStartDelay(animator4.getStartDelay() + (sparseIntArray.valueAt(i7) - j));
            }
        }
    }

    public final void end() {
        int i = this.mNumInstances - 1;
        this.mNumInstances = i;
        if (i == 0) {
            ArrayList<TransitionListener> arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                int size = arrayList2.size();
                for (int i2 = 0; i2 < size; i2++) {
                    ((TransitionListener) arrayList2.get(i2)).onTransitionEnd(this);
                }
            }
            for (int i3 = 0; i3 < this.mStartValues.mItemIdValues.size(); i3++) {
                View valueAt = this.mStartValues.mItemIdValues.valueAt(i3);
                if (valueAt != null) {
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                    ViewCompat.Api16Impl.setHasTransientState(valueAt, false);
                }
            }
            for (int i4 = 0; i4 < this.mEndValues.mItemIdValues.size(); i4++) {
                View valueAt2 = this.mEndValues.mItemIdValues.valueAt(i4);
                if (valueAt2 != null) {
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                    ViewCompat.Api16Impl.setHasTransientState(valueAt2, false);
                }
            }
            this.mEnded = true;
        }
    }

    public final TransitionValues getMatchedTransitionValues(View view, boolean z) {
        ArrayList<TransitionValues> arrayList;
        ArrayList<TransitionValues> arrayList2;
        TransitionSet transitionSet = this.mParent;
        if (transitionSet != null) {
            return transitionSet.getMatchedTransitionValues(view, z);
        }
        if (z) {
            arrayList = this.mStartValuesList;
        } else {
            arrayList = this.mEndValuesList;
        }
        if (arrayList == null) {
            return null;
        }
        int size = arrayList.size();
        int i = -1;
        int i2 = 0;
        while (true) {
            if (i2 >= size) {
                break;
            }
            TransitionValues transitionValues = arrayList.get(i2);
            if (transitionValues == null) {
                return null;
            }
            if (transitionValues.view == view) {
                i = i2;
                break;
            }
            i2++;
        }
        if (i < 0) {
            return null;
        }
        if (z) {
            arrayList2 = this.mEndValuesList;
        } else {
            arrayList2 = this.mStartValuesList;
        }
        return arrayList2.get(i);
    }

    public final TransitionValues getTransitionValues(View view, boolean z) {
        TransitionValuesMaps transitionValuesMaps;
        TransitionSet transitionSet = this.mParent;
        if (transitionSet != null) {
            return transitionSet.getTransitionValues(view, z);
        }
        if (z) {
            transitionValuesMaps = this.mStartValues;
        } else {
            transitionValuesMaps = this.mEndValues;
        }
        ArrayMap<View, TransitionValues> arrayMap = transitionValuesMaps.mViewValues;
        Objects.requireNonNull(arrayMap);
        return arrayMap.getOrDefault(view, null);
    }

    public void pause(View view) {
        if (!this.mEnded) {
            for (int size = this.mCurrentAnimators.size() - 1; size >= 0; size--) {
                this.mCurrentAnimators.get(size).pause();
            }
            ArrayList<TransitionListener> arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                int size2 = arrayList2.size();
                for (int i = 0; i < size2; i++) {
                    ((TransitionListener) arrayList2.get(i)).onTransitionPause();
                }
            }
            this.mPaused = true;
        }
    }

    public Transition removeListener(TransitionListener transitionListener) {
        ArrayList<TransitionListener> arrayList = this.mListeners;
        if (arrayList == null) {
            return this;
        }
        arrayList.remove(transitionListener);
        if (this.mListeners.size() == 0) {
            this.mListeners = null;
        }
        return this;
    }

    public Transition removeTarget(View view) {
        this.mTargets.remove(view);
        return this;
    }

    public void resume(ViewGroup viewGroup) {
        if (this.mPaused) {
            if (!this.mEnded) {
                int size = this.mCurrentAnimators.size();
                while (true) {
                    size--;
                    if (size < 0) {
                        break;
                    }
                    this.mCurrentAnimators.get(size).resume();
                }
                ArrayList<TransitionListener> arrayList = this.mListeners;
                if (arrayList != null && arrayList.size() > 0) {
                    ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                    int size2 = arrayList2.size();
                    for (int i = 0; i < size2; i++) {
                        ((TransitionListener) arrayList2.get(i)).onTransitionResume();
                    }
                }
            }
            this.mPaused = false;
        }
    }

    public void setPathMotion(PathMotion pathMotion) {
        if (pathMotion == null) {
            this.mPathMotion = STRAIGHT_PATH_MOTION;
        } else {
            this.mPathMotion = pathMotion;
        }
    }

    public final void start() {
        if (this.mNumInstances == 0) {
            ArrayList<TransitionListener> arrayList = this.mListeners;
            if (arrayList != null && arrayList.size() > 0) {
                ArrayList arrayList2 = (ArrayList) this.mListeners.clone();
                int size = arrayList2.size();
                for (int i = 0; i < size; i++) {
                    ((TransitionListener) arrayList2.get(i)).onTransitionStart(this);
                }
            }
            this.mEnded = false;
        }
        this.mNumInstances++;
    }

    public final void captureValues(ViewGroup viewGroup, boolean z) {
        clearValues(z);
        if (this.mTargetIds.size() > 0 || this.mTargets.size() > 0) {
            for (int i = 0; i < this.mTargetIds.size(); i++) {
                View findViewById = viewGroup.findViewById(this.mTargetIds.get(i).intValue());
                if (findViewById != null) {
                    TransitionValues transitionValues = new TransitionValues(findViewById);
                    if (z) {
                        captureStartValues(transitionValues);
                    } else {
                        captureEndValues(transitionValues);
                    }
                    transitionValues.mTargetedTransitions.add(this);
                    capturePropagationValues(transitionValues);
                    if (z) {
                        addViewValues(this.mStartValues, findViewById, transitionValues);
                    } else {
                        addViewValues(this.mEndValues, findViewById, transitionValues);
                    }
                }
            }
            for (int i2 = 0; i2 < this.mTargets.size(); i2++) {
                View view = this.mTargets.get(i2);
                TransitionValues transitionValues2 = new TransitionValues(view);
                if (z) {
                    captureStartValues(transitionValues2);
                } else {
                    captureEndValues(transitionValues2);
                }
                transitionValues2.mTargetedTransitions.add(this);
                capturePropagationValues(transitionValues2);
                if (z) {
                    addViewValues(this.mStartValues, view, transitionValues2);
                } else {
                    addViewValues(this.mEndValues, view, transitionValues2);
                }
            }
            return;
        }
        captureHierarchy(viewGroup, z);
    }

    public final boolean isValidTarget(View view) {
        int id = view.getId();
        if ((this.mTargetIds.size() != 0 || this.mTargets.size() != 0) && !this.mTargetIds.contains(Integer.valueOf(id)) && !this.mTargets.contains(view)) {
            return false;
        }
        return true;
    }

    public void runAnimators() {
        start();
        final ArrayMap<Animator, AnimationInfo> runningAnimators = getRunningAnimators();
        Iterator<Animator> it = this.mAnimators.iterator();
        while (it.hasNext()) {
            Animator next = it.next();
            if (runningAnimators.containsKey(next)) {
                start();
                if (next != null) {
                    next.addListener(new AnimatorListenerAdapter() { // from class: androidx.transition.Transition.2
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            runningAnimators.remove(animator);
                            Transition.this.mCurrentAnimators.remove(animator);
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationStart(Animator animator) {
                            Transition.this.mCurrentAnimators.add(animator);
                        }
                    });
                    long j = this.mDuration;
                    if (j >= 0) {
                        next.setDuration(j);
                    }
                    long j2 = this.mStartDelay;
                    if (j2 >= 0) {
                        next.setStartDelay(next.getStartDelay() + j2);
                    }
                    TimeInterpolator timeInterpolator = this.mInterpolator;
                    if (timeInterpolator != null) {
                        next.setInterpolator(timeInterpolator);
                    }
                    next.addListener(new AnimatorListenerAdapter() { // from class: androidx.transition.Transition.3
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            Transition.this.end();
                            animator.removeListener(this);
                        }
                    });
                    next.start();
                }
            }
        }
        this.mAnimators.clear();
        end();
    }

    @SuppressLint({"RestrictedApi"})
    public Transition(Context context, AttributeSet attributeSet) {
        boolean z;
        this.mName = getClass().getName();
        this.mStartDelay = -1L;
        this.mDuration = -1L;
        this.mInterpolator = null;
        this.mTargetIds = new ArrayList<>();
        this.mTargets = new ArrayList<>();
        this.mStartValues = new TransitionValuesMaps();
        this.mEndValues = new TransitionValuesMaps();
        this.mParent = null;
        this.mMatchOrder = DEFAULT_MATCH_ORDER;
        this.mCurrentAnimators = new ArrayList<>();
        this.mNumInstances = 0;
        this.mPaused = false;
        this.mEnded = false;
        this.mListeners = null;
        this.mAnimators = new ArrayList<>();
        this.mPathMotion = STRAIGHT_PATH_MOTION;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, Styleable.TRANSITION);
        XmlResourceParser xmlResourceParser = (XmlResourceParser) attributeSet;
        long namedInt = TypedArrayUtils.getNamedInt(obtainStyledAttributes, xmlResourceParser, "duration", 1, -1);
        if (namedInt >= 0) {
            setDuration(namedInt);
        }
        long namedInt2 = TypedArrayUtils.getNamedInt(obtainStyledAttributes, xmlResourceParser, "startDelay", 2, -1);
        if (namedInt2 > 0) {
            setStartDelay(namedInt2);
        }
        int resourceId = !TypedArrayUtils.hasAttribute(xmlResourceParser, "interpolator") ? 0 : obtainStyledAttributes.getResourceId(0, 0);
        if (resourceId > 0) {
            setInterpolator(AnimationUtils.loadInterpolator(context, resourceId));
        }
        String namedString = TypedArrayUtils.getNamedString(obtainStyledAttributes, xmlResourceParser, "matchOrder", 3);
        if (namedString != null) {
            StringTokenizer stringTokenizer = new StringTokenizer(namedString, ",");
            int[] iArr = new int[stringTokenizer.countTokens()];
            int i = 0;
            while (stringTokenizer.hasMoreTokens()) {
                String trim = stringTokenizer.nextToken().trim();
                if ("id".equalsIgnoreCase(trim)) {
                    iArr[i] = 3;
                } else if ("instance".equalsIgnoreCase(trim)) {
                    iArr[i] = 1;
                } else if ("name".equalsIgnoreCase(trim)) {
                    iArr[i] = 2;
                } else if ("itemId".equalsIgnoreCase(trim)) {
                    iArr[i] = 4;
                } else if (trim.isEmpty()) {
                    int[] iArr2 = new int[iArr.length - 1];
                    System.arraycopy(iArr, 0, iArr2, 0, i);
                    i--;
                    iArr = iArr2;
                } else {
                    throw new InflateException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Unknown match type in matchOrder: '", trim, "'"));
                }
                i++;
            }
            if (iArr.length == 0) {
                this.mMatchOrder = DEFAULT_MATCH_ORDER;
            } else {
                for (int i2 = 0; i2 < iArr.length; i2++) {
                    int i3 = iArr[i2];
                    if (i3 >= 1 && i3 <= 4) {
                        int i4 = iArr[i2];
                        int i5 = 0;
                        while (true) {
                            if (i5 >= i2) {
                                z = false;
                                break;
                            } else if (iArr[i5] == i4) {
                                z = true;
                                break;
                            } else {
                                i5++;
                            }
                        }
                        if (z) {
                            throw new IllegalArgumentException("matches contains a duplicate value");
                        }
                    } else {
                        throw new IllegalArgumentException("matches contains invalid value");
                    }
                }
                this.mMatchOrder = (int[]) iArr.clone();
            }
        }
        obtainStyledAttributes.recycle();
    }

    public final String toString() {
        return toString("");
    }

    public Transition setDuration(long j) {
        this.mDuration = j;
        return this;
    }

    public void setEpicenterCallback(EpicenterCallback epicenterCallback) {
        this.mEpicenterCallback = epicenterCallback;
    }

    public Transition setInterpolator(TimeInterpolator timeInterpolator) {
        this.mInterpolator = timeInterpolator;
        return this;
    }

    public void setPropagation(TransitionPropagation transitionPropagation) {
        this.mPropagation = transitionPropagation;
    }

    public Transition setStartDelay(long j) {
        this.mStartDelay = j;
        return this;
    }
}
