package androidx.fragment.app;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.concurrent.futures.AbstractResolvableFuture$$ExternalSyntheticOutline0;
import androidx.fragment.R$styleable;
import androidx.fragment.app.strictmode.FragmentStrictMode;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FragmentLayoutInflaterFactory implements LayoutInflater.Factory2 {
    public final FragmentManager mFragmentManager;

    @Override // android.view.LayoutInflater.Factory
    public final View onCreateView(String str, Context context, AttributeSet attributeSet) {
        return onCreateView(null, str, context, attributeSet);
    }

    @Override // android.view.LayoutInflater.Factory2
    public final View onCreateView(View view, String str, Context context, AttributeSet attributeSet) {
        boolean z;
        final FragmentStateManager fragmentStateManager;
        if (FragmentContainerView.class.getName().equals(str)) {
            return new FragmentContainerView(context, attributeSet, this.mFragmentManager);
        }
        Fragment fragment = null;
        if (!"fragment".equals(str)) {
            return null;
        }
        String attributeValue = attributeSet.getAttributeValue(null, "class");
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.Fragment);
        int i = 0;
        if (attributeValue == null) {
            attributeValue = obtainStyledAttributes.getString(0);
        }
        int resourceId = obtainStyledAttributes.getResourceId(1, -1);
        String string = obtainStyledAttributes.getString(2);
        obtainStyledAttributes.recycle();
        if (attributeValue != null) {
            try {
                z = Fragment.class.isAssignableFrom(FragmentFactory.loadClass(context.getClassLoader(), attributeValue));
            } catch (ClassNotFoundException unused) {
                z = false;
            }
            if (z) {
                if (view != null) {
                    i = view.getId();
                }
                if (i == -1 && resourceId == -1 && string == null) {
                    throw new IllegalArgumentException(attributeSet.getPositionDescription() + ": Must specify unique android:id, android:tag, or have a parent with an id for " + attributeValue);
                }
                if (resourceId != -1) {
                    fragment = this.mFragmentManager.findFragmentById(resourceId);
                }
                if (fragment == null && string != null) {
                    fragment = this.mFragmentManager.findFragmentByTag(string);
                }
                if (fragment == null && i != -1) {
                    fragment = this.mFragmentManager.findFragmentById(i);
                }
                if (fragment == null) {
                    FragmentFactory fragmentFactory = this.mFragmentManager.getFragmentFactory();
                    context.getClassLoader();
                    fragment = fragmentFactory.instantiate(attributeValue);
                    fragment.mFromLayout = true;
                    fragment.mFragmentId = resourceId != 0 ? resourceId : i;
                    fragment.mContainerId = i;
                    fragment.mTag = string;
                    fragment.mInLayout = true;
                    FragmentManager fragmentManager = this.mFragmentManager;
                    fragment.mFragmentManager = fragmentManager;
                    Objects.requireNonNull(fragmentManager);
                    fragment.mHost = fragmentManager.mHost;
                    FragmentManager fragmentManager2 = this.mFragmentManager;
                    Objects.requireNonNull(fragmentManager2);
                    Objects.requireNonNull(fragmentManager2.mHost);
                    fragment.onInflate();
                    fragmentStateManager = this.mFragmentManager.addFragment(fragment);
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "Fragment " + fragment + " has been inflated via the <fragment> tag: id=0x" + Integer.toHexString(resourceId));
                    }
                } else if (!fragment.mInLayout) {
                    fragment.mInLayout = true;
                    FragmentManager fragmentManager3 = this.mFragmentManager;
                    fragment.mFragmentManager = fragmentManager3;
                    Objects.requireNonNull(fragmentManager3);
                    fragment.mHost = fragmentManager3.mHost;
                    FragmentManager fragmentManager4 = this.mFragmentManager;
                    Objects.requireNonNull(fragmentManager4);
                    Objects.requireNonNull(fragmentManager4.mHost);
                    fragment.onInflate();
                    fragmentStateManager = this.mFragmentManager.createOrGetFragmentStateManager(fragment);
                    if (FragmentManager.isLoggingEnabled(2)) {
                        Log.v("FragmentManager", "Retained Fragment " + fragment + " has been re-attached via the <fragment> tag: id=0x" + Integer.toHexString(resourceId));
                    }
                } else {
                    throw new IllegalArgumentException(attributeSet.getPositionDescription() + ": Duplicate id 0x" + Integer.toHexString(resourceId) + ", tag " + string + ", or parent id 0x" + Integer.toHexString(i) + " with another fragment for " + attributeValue);
                }
                ViewGroup viewGroup = (ViewGroup) view;
                FragmentStrictMode.onFragmentTagUsage(fragment, viewGroup);
                fragment.mContainer = viewGroup;
                fragmentStateManager.moveToExpectedState();
                fragmentStateManager.ensureInflatedView();
                View view2 = fragment.mView;
                if (view2 != null) {
                    if (resourceId != 0) {
                        view2.setId(resourceId);
                    }
                    if (fragment.mView.getTag() == null) {
                        fragment.mView.setTag(string);
                    }
                    fragment.mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: androidx.fragment.app.FragmentLayoutInflaterFactory.1
                        @Override // android.view.View.OnAttachStateChangeListener
                        public final void onViewDetachedFromWindow(View view3) {
                        }

                        @Override // android.view.View.OnAttachStateChangeListener
                        public final void onViewAttachedToWindow(View view3) {
                            FragmentStateManager fragmentStateManager2 = fragmentStateManager;
                            Objects.requireNonNull(fragmentStateManager2);
                            Fragment fragment2 = fragmentStateManager2.mFragment;
                            fragmentStateManager.moveToExpectedState();
                            SpecialEffectsController.getOrCreateController((ViewGroup) fragment2.mView.getParent(), FragmentLayoutInflaterFactory.this.mFragmentManager.getSpecialEffectsControllerFactory()).forceCompleteAllOperations();
                        }
                    });
                    return fragment.mView;
                }
                throw new IllegalStateException(AbstractResolvableFuture$$ExternalSyntheticOutline0.m("Fragment ", attributeValue, " did not create a view."));
            }
        }
        return null;
    }

    public FragmentLayoutInflaterFactory(FragmentManager fragmentManager) {
        this.mFragmentManager = fragmentManager;
    }
}
