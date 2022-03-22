package androidx.preference;

import android.app.DialogFragment;
import android.app.Fragment;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.content.res.TypedArrayUtils;
import androidx.core.view.ViewCompat;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;
@Deprecated
/* loaded from: classes.dex */
public abstract class PreferenceFragment extends Fragment implements PreferenceManager.OnPreferenceTreeClickListener, PreferenceManager.OnDisplayPreferenceDialogListener, PreferenceManager.OnNavigateToScreenListener, DialogPreference.TargetFragment {
    public boolean mHavePrefs;
    public boolean mInitDone;
    public RecyclerView mList;
    public PreferenceManager mPreferenceManager;
    public ContextThemeWrapper mStyledContext;
    public final DividerDecoration mDividerDecoration = new DividerDecoration();
    public int mLayoutResId = 2131624397;
    public final AnonymousClass1 mHandler = new Handler() { // from class: androidx.preference.PreferenceFragment.1
        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            if (message.what == 1) {
                PreferenceFragment preferenceFragment = PreferenceFragment.this;
                Objects.requireNonNull(preferenceFragment);
                PreferenceScreen preferenceScreen = preferenceFragment.getPreferenceScreen();
                if (preferenceScreen != null) {
                    preferenceFragment.mList.setAdapter(new PreferenceGroupAdapter(preferenceScreen));
                    preferenceScreen.onAttached();
                }
            }
        }
    };
    public final AnonymousClass2 mRequestFocus = new Runnable() { // from class: androidx.preference.PreferenceFragment.2
        @Override // java.lang.Runnable
        public final void run() {
            RecyclerView recyclerView = PreferenceFragment.this.mList;
            recyclerView.focusableViewAvailable(recyclerView);
        }
    };

    /* loaded from: classes.dex */
    public class DividerDecoration extends RecyclerView.ItemDecoration {
        public boolean mAllowDividerAfterLastItem = true;
        public Drawable mDivider;
        public int mDividerHeight;

        public DividerDecoration() {
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public final void onDrawOver(Canvas canvas, RecyclerView recyclerView) {
            if (this.mDivider != null) {
                int childCount = recyclerView.getChildCount();
                int width = recyclerView.getWidth();
                for (int i = 0; i < childCount; i++) {
                    View childAt = recyclerView.getChildAt(i);
                    if (shouldDrawDividerBelow(childAt, recyclerView)) {
                        int height = childAt.getHeight() + ((int) childAt.getY());
                        this.mDivider.setBounds(0, height, width, this.mDividerHeight + height);
                        this.mDivider.draw(canvas);
                    }
                }
            }
        }

        @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
        public final void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
            if (shouldDrawDividerBelow(view, recyclerView)) {
                rect.bottom = this.mDividerHeight;
            }
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0019  */
        /* JADX WARN: Removed duplicated region for block: B:9:0x0018 A[RETURN] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean shouldDrawDividerBelow(android.view.View r5, androidx.recyclerview.widget.RecyclerView r6) {
            /*
                r4 = this;
                androidx.recyclerview.widget.RecyclerView$ViewHolder r0 = r6.getChildViewHolder(r5)
                boolean r1 = r0 instanceof androidx.preference.PreferenceViewHolder
                r2 = 0
                r3 = 1
                if (r1 == 0) goto L_0x0015
                androidx.preference.PreferenceViewHolder r0 = (androidx.preference.PreferenceViewHolder) r0
                java.util.Objects.requireNonNull(r0)
                boolean r0 = r0.mDividerAllowedBelow
                if (r0 == 0) goto L_0x0015
                r0 = r3
                goto L_0x0016
            L_0x0015:
                r0 = r2
            L_0x0016:
                if (r0 != 0) goto L_0x0019
                return r2
            L_0x0019:
                boolean r4 = r4.mAllowDividerAfterLastItem
                int r5 = r6.indexOfChild(r5)
                int r0 = r6.getChildCount()
                int r0 = r0 - r3
                if (r5 >= r0) goto L_0x003e
                int r5 = r5 + r3
                android.view.View r4 = r6.getChildAt(r5)
                androidx.recyclerview.widget.RecyclerView$ViewHolder r4 = r6.getChildViewHolder(r4)
                boolean r5 = r4 instanceof androidx.preference.PreferenceViewHolder
                if (r5 == 0) goto L_0x003d
                androidx.preference.PreferenceViewHolder r4 = (androidx.preference.PreferenceViewHolder) r4
                java.util.Objects.requireNonNull(r4)
                boolean r4 = r4.mDividerAllowedAbove
                if (r4 == 0) goto L_0x003d
                r2 = r3
            L_0x003d:
                r4 = r2
            L_0x003e:
                return r4
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.preference.PreferenceFragment.DividerDecoration.shouldDrawDividerBelow(android.view.View, androidx.recyclerview.widget.RecyclerView):boolean");
        }
    }

    /* loaded from: classes.dex */
    public interface OnPreferenceDisplayDialogCallback {
        boolean onPreferenceDisplayDialog();
    }

    /* loaded from: classes.dex */
    public interface OnPreferenceStartFragmentCallback {
        boolean onPreferenceStartFragment(Preference preference);
    }

    /* loaded from: classes.dex */
    public interface OnPreferenceStartScreenCallback {
        void onPreferenceStartScreen(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen);
    }

    @Deprecated
    public abstract void onCreatePreferences(String str);

    /* JADX WARN: Finally extract failed */
    @Deprecated
    public final void addPreferencesFromResource(int i) {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager != null) {
            ContextThemeWrapper contextThemeWrapper = this.mStyledContext;
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            preferenceManager.mNoCommit = true;
            PreferenceInflater preferenceInflater = new PreferenceInflater(contextThemeWrapper, preferenceManager);
            XmlResourceParser xml = contextThemeWrapper.getResources().getXml(i);
            try {
                PreferenceGroup inflate = preferenceInflater.inflate(xml, preferenceScreen);
                xml.close();
                PreferenceScreen preferenceScreen2 = (PreferenceScreen) inflate;
                preferenceScreen2.onAttachedToHierarchy(preferenceManager);
                SharedPreferences.Editor editor = preferenceManager.mEditor;
                if (editor != null) {
                    editor.apply();
                }
                preferenceManager.mNoCommit = false;
                setPreferenceScreen(preferenceScreen2);
            } catch (Throwable th) {
                xml.close();
                throw th;
            }
        } else {
            throw new RuntimeException("This should be called after super.onCreate.");
        }
    }

    @Override // androidx.preference.DialogPreference.TargetFragment
    @Deprecated
    public final <T extends Preference> T findPreference(CharSequence charSequence) {
        PreferenceScreen preferenceScreen;
        PreferenceManager preferenceManager = this.mPreferenceManager;
        if (preferenceManager == null || (preferenceScreen = preferenceManager.mPreferenceScreen) == null) {
            return null;
        }
        return (T) preferenceScreen.findPreference(charSequence);
    }

    @Deprecated
    public final PreferenceScreen getPreferenceScreen() {
        PreferenceManager preferenceManager = this.mPreferenceManager;
        Objects.requireNonNull(preferenceManager);
        return preferenceManager.mPreferenceScreen;
    }

    @Override // android.app.Fragment
    public final View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        RecyclerView recyclerView;
        ContextThemeWrapper contextThemeWrapper = this.mStyledContext;
        TypedArray obtainStyledAttributes = contextThemeWrapper.obtainStyledAttributes(null, R$styleable.PreferenceFragment, TypedArrayUtils.getAttr(contextThemeWrapper, 2130969623, 16844038), 0);
        this.mLayoutResId = obtainStyledAttributes.getResourceId(0, this.mLayoutResId);
        Drawable drawable = obtainStyledAttributes.getDrawable(1);
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(2, -1);
        boolean z = obtainStyledAttributes.getBoolean(3, true);
        obtainStyledAttributes.recycle();
        LayoutInflater cloneInContext = layoutInflater.cloneInContext(this.mStyledContext);
        View inflate = cloneInContext.inflate(this.mLayoutResId, viewGroup, false);
        View findViewById = inflate.findViewById(16908351);
        if (findViewById instanceof ViewGroup) {
            ViewGroup viewGroup2 = (ViewGroup) findViewById;
            if (!this.mStyledContext.getPackageManager().hasSystemFeature("android.hardware.type.automotive") || (recyclerView = (RecyclerView) viewGroup2.findViewById(2131428677)) == null) {
                recyclerView = (RecyclerView) cloneInContext.inflate(2131624400, viewGroup2, false);
                getActivity();
                recyclerView.setLayoutManager(new LinearLayoutManager(1));
                PreferenceRecyclerViewAccessibilityDelegate preferenceRecyclerViewAccessibilityDelegate = new PreferenceRecyclerViewAccessibilityDelegate(recyclerView);
                recyclerView.mAccessibilityDelegate = preferenceRecyclerViewAccessibilityDelegate;
                ViewCompat.setAccessibilityDelegate(recyclerView, preferenceRecyclerViewAccessibilityDelegate);
            }
            this.mList = recyclerView;
            recyclerView.addItemDecoration(this.mDividerDecoration);
            DividerDecoration dividerDecoration = this.mDividerDecoration;
            if (drawable != null) {
                Objects.requireNonNull(dividerDecoration);
                dividerDecoration.mDividerHeight = drawable.getIntrinsicHeight();
            } else {
                dividerDecoration.mDividerHeight = 0;
            }
            dividerDecoration.mDivider = drawable;
            RecyclerView recyclerView2 = PreferenceFragment.this.mList;
            Objects.requireNonNull(recyclerView2);
            if (recyclerView2.mItemDecorations.size() != 0) {
                RecyclerView.LayoutManager layoutManager = recyclerView2.mLayout;
                if (layoutManager != null) {
                    layoutManager.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
                }
                recyclerView2.markItemDecorInsetsDirty();
                recyclerView2.requestLayout();
            }
            if (dimensionPixelSize != -1) {
                DividerDecoration dividerDecoration2 = this.mDividerDecoration;
                Objects.requireNonNull(dividerDecoration2);
                dividerDecoration2.mDividerHeight = dimensionPixelSize;
                RecyclerView recyclerView3 = PreferenceFragment.this.mList;
                Objects.requireNonNull(recyclerView3);
                if (recyclerView3.mItemDecorations.size() != 0) {
                    RecyclerView.LayoutManager layoutManager2 = recyclerView3.mLayout;
                    if (layoutManager2 != null) {
                        layoutManager2.assertNotInLayoutOrScroll("Cannot invalidate item decorations during a scroll or layout");
                    }
                    recyclerView3.markItemDecorInsetsDirty();
                    recyclerView3.requestLayout();
                }
            }
            DividerDecoration dividerDecoration3 = this.mDividerDecoration;
            Objects.requireNonNull(dividerDecoration3);
            dividerDecoration3.mAllowDividerAfterLastItem = z;
            if (this.mList.getParent() == null) {
                viewGroup2.addView(this.mList);
            }
            post(this.mRequestFocus);
            return inflate;
        }
        throw new RuntimeException("Content has view with id attribute 'android.R.id.list_container' that is not a ViewGroup class");
    }

    @Override // android.app.Fragment
    public final void onDestroyView() {
        PreferenceScreen preferenceScreen;
        removeCallbacks(this.mRequestFocus);
        removeMessages(1);
        if (this.mHavePrefs && (preferenceScreen = getPreferenceScreen()) != null) {
            preferenceScreen.onDetached();
        }
        this.mList = null;
        super.onDestroyView();
    }

    @Deprecated
    public final void setPreferenceScreen(PreferenceScreen preferenceScreen) {
        boolean z;
        PreferenceManager preferenceManager = this.mPreferenceManager;
        Objects.requireNonNull(preferenceManager);
        PreferenceScreen preferenceScreen2 = preferenceManager.mPreferenceScreen;
        if (preferenceScreen != preferenceScreen2) {
            if (preferenceScreen2 != null) {
                preferenceScreen2.onDetached();
            }
            preferenceManager.mPreferenceScreen = preferenceScreen;
            z = true;
        } else {
            z = false;
        }
        if (z) {
            this.mHavePrefs = true;
            if (this.mInitDone && !hasMessages(1)) {
                obtainMessage(1).sendToTarget();
            }
        }
    }

    @Override // android.app.Fragment
    public void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        TypedValue typedValue = new TypedValue();
        getActivity().getTheme().resolveAttribute(2130969627, typedValue, true);
        int i = typedValue.resourceId;
        if (i == 0) {
            i = 2132017595;
        }
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(getActivity(), i);
        this.mStyledContext = contextThemeWrapper;
        PreferenceManager preferenceManager = new PreferenceManager(contextThemeWrapper);
        this.mPreferenceManager = preferenceManager;
        preferenceManager.mOnNavigateToScreenListener = this;
        if (getArguments() != null) {
            str = getArguments().getString("androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT");
        } else {
            str = null;
        }
        onCreatePreferences(str);
    }

    @Override // androidx.preference.PreferenceManager.OnDisplayPreferenceDialogListener
    @Deprecated
    public void onDisplayPreferenceDialog(Preference preference) {
        boolean z;
        DialogFragment dialogFragment;
        if (getActivity() instanceof OnPreferenceDisplayDialogCallback) {
            z = ((OnPreferenceDisplayDialogCallback) getActivity()).onPreferenceDisplayDialog();
        } else {
            z = false;
        }
        if (!z && getFragmentManager().findFragmentByTag("androidx.preference.PreferenceFragment.DIALOG") == null) {
            if (preference instanceof EditTextPreference) {
                Objects.requireNonNull(preference);
                String str = preference.mKey;
                dialogFragment = new EditTextPreferenceDialogFragment();
                Bundle bundle = new Bundle(1);
                bundle.putString("key", str);
                dialogFragment.setArguments(bundle);
            } else if (preference instanceof ListPreference) {
                Objects.requireNonNull(preference);
                String str2 = preference.mKey;
                dialogFragment = new ListPreferenceDialogFragment();
                Bundle bundle2 = new Bundle(1);
                bundle2.putString("key", str2);
                dialogFragment.setArguments(bundle2);
            } else if (preference instanceof MultiSelectListPreference) {
                Objects.requireNonNull(preference);
                String str3 = preference.mKey;
                dialogFragment = new MultiSelectListPreferenceDialogFragment();
                Bundle bundle3 = new Bundle(1);
                bundle3.putString("key", str3);
                dialogFragment.setArguments(bundle3);
            } else {
                throw new IllegalArgumentException("Tried to display dialog for unknown preference type. Did you forget to override onDisplayPreferenceDialog()?");
            }
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(getFragmentManager(), "androidx.preference.PreferenceFragment.DIALOG");
        }
    }

    @Override // androidx.preference.PreferenceManager.OnPreferenceTreeClickListener
    @Deprecated
    public boolean onPreferenceTreeClick(Preference preference) {
        Objects.requireNonNull(preference);
        if (preference.mFragment == null || !(getActivity() instanceof OnPreferenceStartFragmentCallback)) {
            return false;
        }
        return ((OnPreferenceStartFragmentCallback) getActivity()).onPreferenceStartFragment(preference);
    }

    @Override // android.app.Fragment
    public final void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        if (preferenceScreen != null) {
            Bundle bundle2 = new Bundle();
            preferenceScreen.dispatchSaveInstanceState(bundle2);
            bundle.putBundle("android:preferences", bundle2);
        }
    }

    @Override // android.app.Fragment
    public final void onStart() {
        super.onStart();
        PreferenceManager preferenceManager = this.mPreferenceManager;
        Objects.requireNonNull(preferenceManager);
        preferenceManager.mOnPreferenceTreeClickListener = this;
        PreferenceManager preferenceManager2 = this.mPreferenceManager;
        Objects.requireNonNull(preferenceManager2);
        preferenceManager2.mOnDisplayPreferenceDialogListener = this;
    }

    @Override // android.app.Fragment
    public final void onStop() {
        super.onStop();
        PreferenceManager preferenceManager = this.mPreferenceManager;
        Objects.requireNonNull(preferenceManager);
        preferenceManager.mOnPreferenceTreeClickListener = null;
        PreferenceManager preferenceManager2 = this.mPreferenceManager;
        Objects.requireNonNull(preferenceManager2);
        preferenceManager2.mOnDisplayPreferenceDialogListener = null;
    }

    @Override // android.app.Fragment
    public final void onViewCreated(View view, Bundle bundle) {
        PreferenceScreen preferenceScreen;
        Bundle bundle2;
        PreferenceScreen preferenceScreen2;
        super.onViewCreated(view, bundle);
        if (!(bundle == null || (bundle2 = bundle.getBundle("android:preferences")) == null || (preferenceScreen2 = getPreferenceScreen()) == null)) {
            preferenceScreen2.dispatchRestoreInstanceState(bundle2);
        }
        if (this.mHavePrefs && (preferenceScreen = getPreferenceScreen()) != null) {
            this.mList.setAdapter(new PreferenceGroupAdapter(preferenceScreen));
            preferenceScreen.onAttached();
        }
        this.mInitDone = true;
    }
}
