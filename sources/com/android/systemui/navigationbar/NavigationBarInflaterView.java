package com.android.systemui.navigationbar;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Space;
import androidx.leanback.R$color;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.navigationbar.buttons.KeyButtonDrawable;
import com.android.systemui.navigationbar.buttons.KeyButtonView;
import com.android.systemui.navigationbar.buttons.ReverseLinearLayout;
import com.android.systemui.recents.OverviewProxyService;
import java.util.Objects;
/* loaded from: classes.dex */
public class NavigationBarInflaterView extends FrameLayout implements NavigationModeController.ModeChangedListener {
    public boolean mAlternativeOrder;
    @VisibleForTesting
    public SparseArray<ButtonDispatcher> mButtonDispatchers;
    public String mCurrentLayout;
    public FrameLayout mHorizontal;
    public boolean mIsVertical;
    public LayoutInflater mLandscapeInflater;
    public View mLastLandscape;
    public View mLastPortrait;
    public LayoutInflater mLayoutInflater;
    public int mNavBarMode;
    public OverviewProxyService mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
    public FrameLayout mVertical;

    public static void addAll(ButtonDispatcher buttonDispatcher, ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            int id = viewGroup.getChildAt(i).getId();
            Objects.requireNonNull(buttonDispatcher);
            if (id == buttonDispatcher.mId) {
                buttonDispatcher.addView(viewGroup.getChildAt(i));
            }
            if (viewGroup.getChildAt(i) instanceof ViewGroup) {
                addAll(buttonDispatcher, (ViewGroup) viewGroup.getChildAt(i));
            }
        }
    }

    public final void inflateButtons(String[] strArr, ViewGroup viewGroup, boolean z, boolean z2) {
        LayoutInflater layoutInflater;
        View view;
        View view2;
        int i;
        ViewGroup.LayoutParams layoutParams;
        String str;
        for (String str2 : strArr) {
            if (z) {
                layoutInflater = this.mLandscapeInflater;
            } else {
                layoutInflater = this.mLayoutInflater;
            }
            String extractButton = extractButton(str2);
            if ("left".equals(extractButton)) {
                extractButton = extractButton("space");
            } else if ("right".equals(extractButton)) {
                extractButton = extractButton("menu_ime");
            }
            String str3 = null;
            if ("home".equals(extractButton)) {
                view = layoutInflater.inflate(2131624130, viewGroup, false);
            } else if ("back".equals(extractButton)) {
                view = layoutInflater.inflate(2131624012, viewGroup, false);
            } else if ("recent".equals(extractButton)) {
                view = layoutInflater.inflate(2131624444, viewGroup, false);
            } else if ("menu_ime".equals(extractButton)) {
                view = layoutInflater.inflate(2131624269, viewGroup, false);
            } else if ("space".equals(extractButton)) {
                view = layoutInflater.inflate(2131624315, viewGroup, false);
            } else if ("clipboard".equals(extractButton)) {
                view = layoutInflater.inflate(2131624030, viewGroup, false);
            } else if ("contextual".equals(extractButton)) {
                view = layoutInflater.inflate(2131624042, viewGroup, false);
            } else if ("home_handle".equals(extractButton)) {
                view = layoutInflater.inflate(2131624131, viewGroup, false);
            } else if ("ime_switcher".equals(extractButton)) {
                view = layoutInflater.inflate(2131624139, viewGroup, false);
            } else if (extractButton.startsWith("key")) {
                if (!extractButton.contains(":")) {
                    str = null;
                } else {
                    str = extractButton.substring(extractButton.indexOf(":") + 1, extractButton.indexOf(")"));
                }
                int extractKeycode = extractKeycode(extractButton);
                view = layoutInflater.inflate(2131624064, viewGroup, false);
                final KeyButtonView keyButtonView = (KeyButtonView) view;
                Objects.requireNonNull(keyButtonView);
                keyButtonView.mCode = extractKeycode;
                if (str != null) {
                    if (str.contains(":")) {
                        new AsyncTask<Icon, Void, Drawable>() { // from class: com.android.systemui.navigationbar.buttons.KeyButtonView.2
                            @Override // android.os.AsyncTask
                            public final Drawable doInBackground(Icon[] iconArr) {
                                return iconArr[0].loadDrawable(((ImageView) keyButtonView).mContext);
                            }

                            @Override // android.os.AsyncTask
                            public final void onPostExecute(Drawable drawable) {
                                keyButtonView.setImageDrawable(drawable);
                            }
                        }.execute(Icon.createWithContentUri(str));
                    } else if (str.contains("/")) {
                        int indexOf = str.indexOf(47);
                        new AsyncTask<Icon, Void, Drawable>() { // from class: com.android.systemui.navigationbar.buttons.KeyButtonView.2
                            @Override // android.os.AsyncTask
                            public final Drawable doInBackground(Icon[] iconArr) {
                                return iconArr[0].loadDrawable(((ImageView) keyButtonView).mContext);
                            }

                            @Override // android.os.AsyncTask
                            public final void onPostExecute(Drawable drawable) {
                                keyButtonView.setImageDrawable(drawable);
                            }
                        }.execute(Icon.createWithResource(str.substring(0, indexOf), Integer.parseInt(str.substring(indexOf + 1))));
                    }
                }
            } else {
                view = null;
            }
            if (view != null) {
                if (str2.contains("[")) {
                    str3 = str2.substring(str2.indexOf("[") + 1, str2.indexOf("]"));
                }
                if (str3 != null) {
                    if (str3.contains("W") || str3.contains("A")) {
                        ReverseLinearLayout.ReverseRelativeLayout reverseRelativeLayout = new ReverseLinearLayout.ReverseRelativeLayout(((FrameLayout) this).mContext);
                        FrameLayout.LayoutParams layoutParams2 = new FrameLayout.LayoutParams(view.getLayoutParams());
                        if (z) {
                            if (z2) {
                                i = 48;
                            } else {
                                i = 80;
                            }
                        } else if (z2) {
                            i = 8388611;
                        } else {
                            i = 8388613;
                        }
                        if (str3.endsWith("WC")) {
                            i = 17;
                        } else if (str3.endsWith("C")) {
                            i = 16;
                        }
                        reverseRelativeLayout.mDefaultGravity = i;
                        reverseRelativeLayout.setGravity(i);
                        reverseRelativeLayout.addView(view, layoutParams2);
                        if (str3.contains("W")) {
                            reverseRelativeLayout.setLayoutParams(new LinearLayout.LayoutParams(0, -1, Float.parseFloat(str3.substring(0, str3.indexOf("W")))));
                        } else {
                            reverseRelativeLayout.setLayoutParams(new LinearLayout.LayoutParams((int) (Float.parseFloat(str3.substring(0, str3.indexOf("A"))) * ((FrameLayout) this).mContext.getResources().getDisplayMetrics().density), -1));
                        }
                        reverseRelativeLayout.setClipChildren(false);
                        reverseRelativeLayout.setClipToPadding(false);
                        view = reverseRelativeLayout;
                    } else {
                        view.getLayoutParams().width = (int) (layoutParams.width * Float.parseFloat(str3));
                    }
                }
                viewGroup.addView(view);
                addToDispatchers(view);
                if (z) {
                    view2 = this.mLastLandscape;
                } else {
                    view2 = this.mLastPortrait;
                }
                if (view instanceof ReverseLinearLayout.ReverseRelativeLayout) {
                    view = ((ReverseLinearLayout.ReverseRelativeLayout) view).getChildAt(0);
                }
                if (view2 != null) {
                    view.setAccessibilityTraversalAfter(view2.getId());
                }
                if (z) {
                    this.mLastLandscape = view;
                } else {
                    this.mLastPortrait = view;
                }
            }
        }
    }

    public final void updateAlternativeOrder() {
        updateAlternativeOrder(this.mHorizontal.findViewById(2131427921));
        updateAlternativeOrder(this.mHorizontal.findViewById(2131427677));
        updateAlternativeOrder(this.mVertical.findViewById(2131427921));
        updateAlternativeOrder(this.mVertical.findViewById(2131427677));
    }

    public static String extractButton(String str) {
        if (!str.contains("[")) {
            return str;
        }
        return str.substring(0, str.indexOf("["));
    }

    public static int extractKeycode(String str) {
        if (!str.contains("(")) {
            return 1;
        }
        return Integer.parseInt(str.substring(str.indexOf("(") + 1, str.indexOf(":")));
    }

    public final void addToDispatchers(View view) {
        SparseArray<ButtonDispatcher> sparseArray = this.mButtonDispatchers;
        if (sparseArray != null) {
            int indexOfKey = sparseArray.indexOfKey(view.getId());
            if (indexOfKey >= 0) {
                this.mButtonDispatchers.valueAt(indexOfKey).addView(view);
            }
            if (view instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) view;
                int childCount = viewGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    addToDispatchers(viewGroup.getChildAt(i));
                }
            }
        }
    }

    public final void clearViews() {
        if (this.mButtonDispatchers != null) {
            for (int i = 0; i < this.mButtonDispatchers.size(); i++) {
                ButtonDispatcher valueAt = this.mButtonDispatchers.valueAt(i);
                Objects.requireNonNull(valueAt);
                valueAt.mViews.clear();
            }
        }
        ViewGroup viewGroup = (ViewGroup) this.mHorizontal.findViewById(2131428478);
        for (int i2 = 0; i2 < viewGroup.getChildCount(); i2++) {
            ((ViewGroup) viewGroup.getChildAt(i2)).removeAllViews();
        }
        ViewGroup viewGroup2 = (ViewGroup) this.mVertical.findViewById(2131428478);
        for (int i3 = 0; i3 < viewGroup2.getChildCount(); i3++) {
            ((ViewGroup) viewGroup2.getChildAt(i3)).removeAllViews();
        }
    }

    @VisibleForTesting
    public void createInflaters() {
        this.mLayoutInflater = LayoutInflater.from(((FrameLayout) this).mContext);
        Configuration configuration = new Configuration();
        configuration.setTo(((FrameLayout) this).mContext.getResources().getConfiguration());
        configuration.orientation = 2;
        this.mLandscapeInflater = LayoutInflater.from(((FrameLayout) this).mContext.createConfigurationContext(configuration));
    }

    public final String getDefaultLayout() {
        int i;
        if (R$color.isGesturalMode(this.mNavBarMode)) {
            i = 2131952140;
        } else if (this.mOverviewProxyService.shouldShowSwipeUpUI()) {
            i = 2131952141;
        } else {
            i = 2131952139;
        }
        return getContext().getString(i);
    }

    public final void inflateLayout(String str) {
        this.mCurrentLayout = str;
        if (str == null) {
            str = getDefaultLayout();
        }
        String[] split = str.split(";", 3);
        if (split.length != 3) {
            Log.d("NavBarInflater", "Invalid layout.");
            split = getDefaultLayout().split(";", 3);
        }
        String[] split2 = split[0].split(",");
        String[] split3 = split[1].split(",");
        String[] split4 = split[2].split(",");
        inflateButtons(split2, (ViewGroup) this.mHorizontal.findViewById(2131427921), false, true);
        inflateButtons(split2, (ViewGroup) this.mVertical.findViewById(2131427921), true, true);
        inflateButtons(split3, (ViewGroup) this.mHorizontal.findViewById(2131427677), false, false);
        inflateButtons(split3, (ViewGroup) this.mVertical.findViewById(2131427677), true, false);
        ((LinearLayout) this.mHorizontal.findViewById(2131427921)).addView(new Space(((FrameLayout) this).mContext), new LinearLayout.LayoutParams(0, 0, 1.0f));
        ((LinearLayout) this.mVertical.findViewById(2131427921)).addView(new Space(((FrameLayout) this).mContext), new LinearLayout.LayoutParams(0, 0, 1.0f));
        inflateButtons(split4, (ViewGroup) this.mHorizontal.findViewById(2131427921), false, false);
        inflateButtons(split4, (ViewGroup) this.mVertical.findViewById(2131427921), true, false);
        updateButtonDispatchersCurrentView();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        NavigationModeController navigationModeController = (NavigationModeController) Dependency.get(NavigationModeController.class);
        Objects.requireNonNull(navigationModeController);
        navigationModeController.mListeners.remove(this);
        super.onDetachedFromWindow();
    }

    public final void updateButtonDispatchersCurrentView() {
        FrameLayout frameLayout;
        if (this.mButtonDispatchers != null) {
            if (this.mIsVertical) {
                frameLayout = this.mVertical;
            } else {
                frameLayout = this.mHorizontal;
            }
            for (int i = 0; i < this.mButtonDispatchers.size(); i++) {
                ButtonDispatcher valueAt = this.mButtonDispatchers.valueAt(i);
                Objects.requireNonNull(valueAt);
                View findViewById = frameLayout.findViewById(valueAt.mId);
                valueAt.mCurrentView = findViewById;
                KeyButtonDrawable keyButtonDrawable = valueAt.mImageDrawable;
                if (keyButtonDrawable != null) {
                    keyButtonDrawable.setCallback(findViewById);
                }
                View view = valueAt.mCurrentView;
                if (view != null) {
                    view.setTranslationX(0.0f);
                    valueAt.mCurrentView.setTranslationY(0.0f);
                    valueAt.mCurrentView.setTranslationZ(0.0f);
                }
            }
        }
    }

    public NavigationBarInflaterView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mNavBarMode = 0;
        createInflaters();
        this.mNavBarMode = ((NavigationModeController) Dependency.get(NavigationModeController.class)).addListener(this);
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        removeAllViews();
        FrameLayout frameLayout = (FrameLayout) this.mLayoutInflater.inflate(2131624318, (ViewGroup) this, false);
        this.mHorizontal = frameLayout;
        addView(frameLayout);
        FrameLayout frameLayout2 = (FrameLayout) this.mLayoutInflater.inflate(2131624319, (ViewGroup) this, false);
        this.mVertical = frameLayout2;
        addView(frameLayout2);
        updateAlternativeOrder();
        clearViews();
        inflateLayout(getDefaultLayout());
    }

    public final void updateAlternativeOrder(View view) {
        if (view instanceof ReverseLinearLayout) {
            ReverseLinearLayout reverseLinearLayout = (ReverseLinearLayout) view;
            boolean z = this.mAlternativeOrder;
            Objects.requireNonNull(reverseLinearLayout);
            reverseLinearLayout.mIsAlternativeOrder = z;
            reverseLinearLayout.updateOrder();
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }
}
