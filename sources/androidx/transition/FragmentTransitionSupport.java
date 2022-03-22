package androidx.transition;

import android.annotation.SuppressLint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.os.CancellationSignal;
import androidx.fragment.app.DefaultSpecialEffectsController;
import androidx.fragment.app.FragmentTransitionImpl;
import androidx.transition.Transition;
import java.util.ArrayList;
import java.util.Objects;
@SuppressLint({"RestrictedApi"})
/* loaded from: classes.dex */
public class FragmentTransitionSupport extends FragmentTransitionImpl {
    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final void setEpicenter(Object obj, View view) {
        if (view != null) {
            final Rect rect = new Rect();
            FragmentTransitionImpl.getBoundsOnScreen(view, rect);
            ((Transition) obj).setEpicenterCallback(new Transition.EpicenterCallback() { // from class: androidx.transition.FragmentTransitionSupport.1
                @Override // androidx.transition.Transition.EpicenterCallback
                public final Rect onGetEpicenter() {
                    return rect;
                }
            });
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final void addTarget(Object obj, View view) {
        ((Transition) obj).addTarget(view);
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final void addTargets(Object obj, ArrayList<View> arrayList) {
        Transition transition;
        Transition transition2 = (Transition) obj;
        if (transition2 != null) {
            int i = 0;
            if (transition2 instanceof TransitionSet) {
                TransitionSet transitionSet = (TransitionSet) transition2;
                int size = transitionSet.mTransitions.size();
                while (i < size) {
                    if (i < 0 || i >= transitionSet.mTransitions.size()) {
                        transition = null;
                    } else {
                        transition = transitionSet.mTransitions.get(i);
                    }
                    addTargets(transition, arrayList);
                    i++;
                }
            } else if (!hasSimpleTarget(transition2) && FragmentTransitionImpl.isNullOrEmpty(transition2.mTargets)) {
                int size2 = arrayList.size();
                while (i < size2) {
                    transition2.addTarget(arrayList.get(i));
                    i++;
                }
            }
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final void beginDelayedTransition(ViewGroup viewGroup, Object obj) {
        TransitionManager.beginDelayedTransition(viewGroup, (Transition) obj);
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final Object cloneTransition(Object obj) {
        if (obj != null) {
            return ((Transition) obj).clone();
        }
        return null;
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final Object mergeTransitionsInSequence(Object obj, Object obj2, Object obj3) {
        Transition transition = (Transition) obj;
        Transition transition2 = (Transition) obj2;
        Transition transition3 = (Transition) obj3;
        if (transition != null && transition2 != null) {
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.addTransition(transition);
            transitionSet.addTransition(transition2);
            transitionSet.setOrdering(1);
            transition = transitionSet;
        } else if (transition == null) {
            if (transition2 != null) {
                transition = transition2;
            } else {
                transition = null;
            }
        }
        if (transition3 == null) {
            return transition;
        }
        TransitionSet transitionSet2 = new TransitionSet();
        if (transition != null) {
            transitionSet2.addTransition(transition);
        }
        transitionSet2.addTransition(transition3);
        return transitionSet2;
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final Object mergeTransitionsTogether(Object obj, Object obj2) {
        TransitionSet transitionSet = new TransitionSet();
        if (obj != null) {
            transitionSet.addTransition((Transition) obj);
        }
        transitionSet.addTransition((Transition) obj2);
        return transitionSet;
    }

    public final void replaceTargets(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        int i;
        Transition transition;
        Transition transition2 = (Transition) obj;
        int i2 = 0;
        if (transition2 instanceof TransitionSet) {
            TransitionSet transitionSet = (TransitionSet) transition2;
            Objects.requireNonNull(transitionSet);
            int size = transitionSet.mTransitions.size();
            while (i2 < size) {
                if (i2 < 0 || i2 >= transitionSet.mTransitions.size()) {
                    transition = null;
                } else {
                    transition = transitionSet.mTransitions.get(i2);
                }
                replaceTargets(transition, arrayList, arrayList2);
                i2++;
            }
        } else if (!hasSimpleTarget(transition2)) {
            ArrayList<View> arrayList3 = transition2.mTargets;
            if (arrayList3.size() == arrayList.size() && arrayList3.containsAll(arrayList)) {
                if (arrayList2 == null) {
                    i = 0;
                } else {
                    i = arrayList2.size();
                }
                while (i2 < i) {
                    transition2.addTarget(arrayList2.get(i2));
                    i2++;
                }
                int size2 = arrayList.size();
                while (true) {
                    size2--;
                    if (size2 >= 0) {
                        transition2.removeTarget(arrayList.get(size2));
                    } else {
                        return;
                    }
                }
            }
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final void scheduleHideFragmentView(Object obj, final View view, final ArrayList<View> arrayList) {
        ((Transition) obj).addListener(new Transition.TransitionListener() { // from class: androidx.transition.FragmentTransitionSupport.2
            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionCancel() {
            }

            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionPause() {
            }

            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionResume() {
            }

            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                view.setVisibility(8);
                int size = arrayList.size();
                for (int i = 0; i < size; i++) {
                    ((View) arrayList.get(i)).setVisibility(0);
                }
            }

            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionStart(Transition transition) {
                transition.removeListener(this);
                transition.addListener(this);
            }
        });
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final void scheduleRemoveTargets(Object obj, final Object obj2, final ArrayList arrayList, final Object obj3, final ArrayList arrayList2) {
        ((Transition) obj).addListener(new TransitionListenerAdapter() { // from class: androidx.transition.FragmentTransitionSupport.3
            public final /* synthetic */ Object val$exitTransition = null;
            public final /* synthetic */ ArrayList val$exitingViews = null;

            @Override // androidx.transition.TransitionListenerAdapter, androidx.transition.Transition.TransitionListener
            public final void onTransitionStart(Transition transition) {
                Object obj4 = obj2;
                if (obj4 != null) {
                    FragmentTransitionSupport.this.replaceTargets(obj4, arrayList, null);
                }
                Object obj5 = this.val$exitTransition;
                if (obj5 != null) {
                    FragmentTransitionSupport.this.replaceTargets(obj5, this.val$exitingViews, null);
                }
                Object obj6 = obj3;
                if (obj6 != null) {
                    FragmentTransitionSupport.this.replaceTargets(obj6, arrayList2, null);
                }
            }

            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
            }
        });
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final void setListenerForTransitionEnd(Object obj, CancellationSignal cancellationSignal, final DefaultSpecialEffectsController.AnonymousClass9 r3) {
        final Transition transition = (Transition) obj;
        cancellationSignal.setOnCancelListener(new CancellationSignal.OnCancelListener() { // from class: androidx.transition.FragmentTransitionSupport.4
            @Override // androidx.core.os.CancellationSignal.OnCancelListener
            public final void onCancel() {
                Transition.this.cancel();
            }
        });
        transition.addListener(new Transition.TransitionListener() { // from class: androidx.transition.FragmentTransitionSupport.5
            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionCancel() {
            }

            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionPause() {
            }

            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionResume() {
            }

            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionStart(Transition transition2) {
            }

            @Override // androidx.transition.Transition.TransitionListener
            public final void onTransitionEnd(Transition transition2) {
                r3.run();
            }
        });
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final void setSharedElementTargets(Object obj, View view, ArrayList<View> arrayList) {
        TransitionSet transitionSet = (TransitionSet) obj;
        Objects.requireNonNull(transitionSet);
        ArrayList<View> arrayList2 = transitionSet.mTargets;
        arrayList2.clear();
        int size = arrayList.size();
        for (int i = 0; i < size; i++) {
            FragmentTransitionImpl.bfsAddViewChildren(arrayList2, arrayList.get(i));
        }
        arrayList2.add(view);
        arrayList.add(view);
        addTargets(transitionSet, arrayList);
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final void swapSharedElementTargets(Object obj, ArrayList<View> arrayList, ArrayList<View> arrayList2) {
        TransitionSet transitionSet = (TransitionSet) obj;
        if (transitionSet != null) {
            transitionSet.mTargets.clear();
            transitionSet.mTargets.addAll(arrayList2);
            replaceTargets(transitionSet, arrayList, arrayList2);
        }
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final Object wrapTransitionInSet(Object obj) {
        if (obj == null) {
            return null;
        }
        TransitionSet transitionSet = new TransitionSet();
        transitionSet.addTransition((Transition) obj);
        return transitionSet;
    }

    public static boolean hasSimpleTarget(Transition transition) {
        Objects.requireNonNull(transition);
        if (!FragmentTransitionImpl.isNullOrEmpty(transition.mTargetIds) || !FragmentTransitionImpl.isNullOrEmpty(null) || !FragmentTransitionImpl.isNullOrEmpty(null)) {
            return true;
        }
        return false;
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final void setEpicenter(Object obj, final Rect rect) {
        ((Transition) obj).setEpicenterCallback(new Transition.EpicenterCallback() { // from class: androidx.transition.FragmentTransitionSupport.6
            @Override // androidx.transition.Transition.EpicenterCallback
            public final Rect onGetEpicenter() {
                Rect rect2 = rect;
                if (rect2 == null || rect2.isEmpty()) {
                    return null;
                }
                return rect;
            }
        });
    }

    @Override // androidx.fragment.app.FragmentTransitionImpl
    public final boolean canHandle(Object obj) {
        return obj instanceof Transition;
    }
}
