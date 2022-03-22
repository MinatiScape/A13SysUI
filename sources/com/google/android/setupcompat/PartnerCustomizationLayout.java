package com.google.android.setupcompat;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import com.android.systemui.R$array;
import com.android.systemui.R$id;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda0;
import com.google.android.setupcompat.internal.LifecycleFragment;
import com.google.android.setupcompat.internal.PersistableBundles;
import com.google.android.setupcompat.internal.SetupCompatServiceInvoker;
import com.google.android.setupcompat.internal.SetupCompatServiceInvoker$$ExternalSyntheticLambda0;
import com.google.android.setupcompat.internal.TemplateLayout;
import com.google.android.setupcompat.logging.CustomEvent;
import com.google.android.setupcompat.logging.MetricKey;
import com.google.android.setupcompat.logging.internal.FooterBarMixinMetrics;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.partnerconfig.ResourceEntry;
import com.google.android.setupcompat.template.FooterBarMixin;
import com.google.android.setupcompat.template.FooterButton;
import com.google.android.setupcompat.template.StatusBarMixin;
import com.google.android.setupcompat.template.SystemNavBarMixin;
import com.google.android.setupcompat.util.Logger;
import com.google.android.setupcompat.util.WizardManagerHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Objects;
import java.util.concurrent.RejectedExecutionException;
/* loaded from: classes.dex */
public class PartnerCustomizationLayout extends TemplateLayout {
    public static final Logger LOG = new Logger("PartnerCustomizationLayout");
    public Activity activity;
    public boolean useDynamicColor;
    public boolean useFullDynamicColorAttr;
    public boolean usePartnerResourceAttr;
    public final ViewTreeObserver.OnWindowFocusChangeListener windowFocusChangeListener;

    public PartnerCustomizationLayout(Context context) {
        this(context, 0, 0);
    }

    @Override // com.google.android.setupcompat.internal.TemplateLayout
    public final void onBeforeTemplateInflated(AttributeSet attributeSet, int i) {
        boolean z;
        this.usePartnerResourceAttr = true;
        Activity lookupActivityFromContext = lookupActivityFromContext(getContext());
        this.activity = lookupActivityFromContext;
        boolean isAnySetupWizard = WizardManagerHelper.isAnySetupWizard(lookupActivityFromContext.getIntent());
        TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$id.SucPartnerCustomizationLayout, i, 0);
        if (!obtainStyledAttributes.hasValue(2)) {
            Logger logger = LOG;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Attribute sucUsePartnerResource not found in ");
            m.append(this.activity.getComponentName());
            logger.e(m.toString());
        }
        if (isAnySetupWizard || obtainStyledAttributes.getBoolean(2, true)) {
            z = true;
        } else {
            z = false;
        }
        this.usePartnerResourceAttr = z;
        this.useDynamicColor = obtainStyledAttributes.hasValue(0);
        this.useFullDynamicColorAttr = obtainStyledAttributes.getBoolean(0, false);
        obtainStyledAttributes.recycle();
        Logger logger2 = LOG;
        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("activity=");
        m2.append(this.activity.getClass().getSimpleName());
        m2.append(" isSetupFlow=");
        m2.append(isAnySetupWizard);
        m2.append(" enablePartnerResourceLoading=");
        m2.append(true);
        m2.append(" usePartnerResourceAttr=");
        m2.append(this.usePartnerResourceAttr);
        m2.append(" useDynamicColor=");
        m2.append(this.useDynamicColor);
        m2.append(" useFullDynamicColorAttr=");
        m2.append(this.useFullDynamicColorAttr);
        String sb = m2.toString();
        Objects.requireNonNull(logger2);
        if (Log.isLoggable("SetupLibrary", 3)) {
            Log.d("SetupLibrary", ((String) logger2.prefix).concat(sb));
        }
    }

    public PartnerCustomizationLayout(Context context, int i) {
        this(context, i, 0);
    }

    public static Activity lookupActivityFromContext(Context context) {
        if (context instanceof Activity) {
            return (Activity) context;
        }
        if (context instanceof ContextWrapper) {
            return lookupActivityFromContext(((ContextWrapper) context).getBaseContext());
        }
        throw new IllegalArgumentException("Cannot find instance of Activity in parent tree");
    }

    @Override // com.google.android.setupcompat.internal.TemplateLayout
    public ViewGroup findContainer(int i) {
        if (i == 0) {
            i = 2131428951;
        }
        return super.findContainer(i);
    }

    public final void init(AttributeSet attributeSet, int i) {
        boolean z;
        if (!isInEditMode()) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$id.SucPartnerCustomizationLayout, i, 0);
            boolean z2 = obtainStyledAttributes.getBoolean(1, true);
            obtainStyledAttributes.recycle();
            if (z2) {
                setSystemUiVisibility(1024);
            }
            registerMixin(StatusBarMixin.class, new StatusBarMixin(this, this.activity.getWindow(), attributeSet, i));
            registerMixin(SystemNavBarMixin.class, new SystemNavBarMixin(this, this.activity.getWindow()));
            registerMixin(FooterBarMixin.class, new FooterBarMixin(this, attributeSet, i));
            SystemNavBarMixin systemNavBarMixin = (SystemNavBarMixin) getMixin(SystemNavBarMixin.class);
            Objects.requireNonNull(systemNavBarMixin);
            TypedArray obtainStyledAttributes2 = systemNavBarMixin.templateLayout.getContext().obtainStyledAttributes(attributeSet, R$id.SucSystemNavBarMixin, i, 0);
            int color = obtainStyledAttributes2.getColor(1, 0);
            if (systemNavBarMixin.windowOfActivity != null) {
                if (systemNavBarMixin.applyPartnerResources && !systemNavBarMixin.useFullDynamicColor) {
                    Context context = systemNavBarMixin.templateLayout.getContext();
                    color = PartnerConfigHelper.get(context).getColor(context, PartnerConfig.CONFIG_NAVIGATION_BAR_BG_COLOR);
                }
                systemNavBarMixin.windowOfActivity.setNavigationBarColor(color);
            }
            Window window = systemNavBarMixin.windowOfActivity;
            if (window == null || (window.getDecorView().getSystemUiVisibility() & 16) == 16) {
                z = true;
            } else {
                z = false;
            }
            boolean z3 = obtainStyledAttributes2.getBoolean(0, z);
            if (systemNavBarMixin.windowOfActivity != null) {
                if (systemNavBarMixin.applyPartnerResources) {
                    Context context2 = systemNavBarMixin.templateLayout.getContext();
                    z3 = PartnerConfigHelper.get(context2).getBoolean(context2, PartnerConfig.CONFIG_LIGHT_NAVIGATION_BAR, false);
                }
                if (z3) {
                    systemNavBarMixin.windowOfActivity.getDecorView().setSystemUiVisibility(16 | systemNavBarMixin.windowOfActivity.getDecorView().getSystemUiVisibility());
                } else {
                    systemNavBarMixin.windowOfActivity.getDecorView().setSystemUiVisibility(systemNavBarMixin.windowOfActivity.getDecorView().getSystemUiVisibility() & (-17));
                }
            }
            TypedArray obtainStyledAttributes3 = systemNavBarMixin.templateLayout.getContext().obtainStyledAttributes(new int[]{16844141});
            int color2 = obtainStyledAttributes2.getColor(2, obtainStyledAttributes3.getColor(0, 0));
            if (systemNavBarMixin.windowOfActivity != null) {
                if (systemNavBarMixin.applyPartnerResources) {
                    Context context3 = systemNavBarMixin.templateLayout.getContext();
                    PartnerConfigHelper partnerConfigHelper = PartnerConfigHelper.get(context3);
                    PartnerConfig partnerConfig = PartnerConfig.CONFIG_NAVIGATION_BAR_DIVIDER_COLOR;
                    if (partnerConfigHelper.isPartnerConfigAvailable(partnerConfig)) {
                        color2 = PartnerConfigHelper.get(context3).getColor(context3, partnerConfig);
                    }
                }
                systemNavBarMixin.windowOfActivity.setNavigationBarDividerColor(color2);
            }
            obtainStyledAttributes3.recycle();
            obtainStyledAttributes2.recycle();
            this.activity.getWindow().addFlags(Integer.MIN_VALUE);
            this.activity.getWindow().clearFlags(67108864);
            this.activity.getWindow().clearFlags(134217728);
        }
    }

    @Override // com.google.android.setupcompat.internal.TemplateLayout
    public View onInflateTemplate(LayoutInflater layoutInflater, int i) {
        if (i == 0) {
            i = 2131624352;
        }
        return inflateTemplate(layoutInflater, 0, i);
    }

    public final boolean shouldApplyDynamicColor() {
        if (this.useDynamicColor && PartnerConfigHelper.get(getContext()).isAvailable()) {
            return true;
        }
        return false;
    }

    public final boolean shouldApplyPartnerResource() {
        if (this.usePartnerResourceAttr && PartnerConfigHelper.get(getContext()).isAvailable()) {
            return true;
        }
        return false;
    }

    /* renamed from: $r8$lambda$0I8CGaPG-55DphyAapa3mtQ-RWk  reason: not valid java name */
    public static void m163$r8$lambda$0I8CGaPG55DphyAapa3mtQRWk(PartnerCustomizationLayout partnerCustomizationLayout, boolean z) {
        Objects.requireNonNull(partnerCustomizationLayout);
        SetupCompatServiceInvoker setupCompatServiceInvoker = SetupCompatServiceInvoker.get(partnerCustomizationLayout.getContext());
        String shortString = partnerCustomizationLayout.activity.getComponentName().toShortString();
        Activity activity = partnerCustomizationLayout.activity;
        Bundle bundle = new Bundle();
        bundle.putString(ResourceEntry.KEY_PACKAGE_NAME, activity.getComponentName().getPackageName());
        bundle.putString("screenName", activity.getComponentName().getShortClassName());
        bundle.putInt("hash", partnerCustomizationLayout.hashCode());
        bundle.putBoolean("focus", z);
        bundle.putLong("timeInMillis", System.currentTimeMillis());
        Objects.requireNonNull(setupCompatServiceInvoker);
        try {
            setupCompatServiceInvoker.loggingExecutor.execute(new SetupCompatServiceInvoker$$ExternalSyntheticLambda0(setupCompatServiceInvoker, shortString, bundle, 0));
        } catch (RejectedExecutionException e) {
            SetupCompatServiceInvoker.LOG.e(String.format("Screen %s report focus changed failed.", shortString), e);
        }
    }

    public PartnerCustomizationLayout(Context context, int i, int i2) {
        super(context, i, i2);
        this.windowFocusChangeListener = new ViewTreeObserver.OnWindowFocusChangeListener() { // from class: com.google.android.setupcompat.PartnerCustomizationLayout$$ExternalSyntheticLambda0
            @Override // android.view.ViewTreeObserver.OnWindowFocusChangeListener
            public final void onWindowFocusChanged(boolean z) {
                PartnerCustomizationLayout.m163$r8$lambda$0I8CGaPG55DphyAapa3mtQRWk(PartnerCustomizationLayout.this, z);
            }
        };
        init(null, 2130969819);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onAttachedToWindow() {
        String str;
        super.onAttachedToWindow();
        Activity activity = this.activity;
        int i = LifecycleFragment.$r8$clinit;
        if (WizardManagerHelper.isAnySetupWizard(activity.getIntent())) {
            SetupCompatServiceInvoker setupCompatServiceInvoker = SetupCompatServiceInvoker.get(activity.getApplicationContext());
            String componentName = activity.getComponentName().toString();
            Bundle bundle = new Bundle();
            bundle.putString("screenName", activity.getComponentName().toString());
            bundle.putString("intentAction", activity.getIntent().getAction());
            Objects.requireNonNull(setupCompatServiceInvoker);
            try {
                setupCompatServiceInvoker.loggingExecutor.execute(new StartingWindowController$$ExternalSyntheticLambda0(setupCompatServiceInvoker, componentName, bundle, 1));
            } catch (RejectedExecutionException e) {
                SetupCompatServiceInvoker.LOG.e(String.format("Screen %s bind back fail.", componentName), e);
            }
            FragmentManager fragmentManager = activity.getFragmentManager();
            if (fragmentManager != null && !fragmentManager.isDestroyed()) {
                Fragment findFragmentByTag = fragmentManager.findFragmentByTag("lifecycle_monitor");
                if (findFragmentByTag == null) {
                    LifecycleFragment lifecycleFragment = new LifecycleFragment();
                    try {
                        fragmentManager.beginTransaction().add(lifecycleFragment, "lifecycle_monitor").commitNow();
                        findFragmentByTag = lifecycleFragment;
                    } catch (IllegalStateException e2) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Error occurred when attach to Activity:");
                        m.append(activity.getComponentName());
                        Log.e("LifecycleFragment", m.toString(), e2);
                    }
                } else if (!(findFragmentByTag instanceof LifecycleFragment)) {
                    Log.wtf("LifecycleFragment", activity.getClass().getSimpleName() + " Incorrect instance on lifecycle fragment.");
                }
                LifecycleFragment lifecycleFragment2 = (LifecycleFragment) findFragmentByTag;
            }
        }
        if (WizardManagerHelper.isAnySetupWizard(this.activity.getIntent())) {
            getViewTreeObserver().addOnWindowFocusChangeListener(this.windowFocusChangeListener);
        }
        FooterBarMixin footerBarMixin = (FooterBarMixin) getMixin(FooterBarMixin.class);
        Objects.requireNonNull(footerBarMixin);
        FooterBarMixinMetrics footerBarMixinMetrics = footerBarMixin.metrics;
        boolean isPrimaryButtonVisible = footerBarMixin.isPrimaryButtonVisible();
        Objects.requireNonNull(footerBarMixinMetrics);
        String str2 = "Visible";
        if (!footerBarMixinMetrics.primaryButtonVisibility.equals("Unknown")) {
            str = footerBarMixinMetrics.primaryButtonVisibility;
        } else if (isPrimaryButtonVisible) {
            str = str2;
        } else {
            str = "Invisible";
        }
        footerBarMixinMetrics.primaryButtonVisibility = str;
        FooterBarMixinMetrics footerBarMixinMetrics2 = footerBarMixin.metrics;
        boolean isSecondaryButtonVisible = footerBarMixin.isSecondaryButtonVisible();
        Objects.requireNonNull(footerBarMixinMetrics2);
        if (!footerBarMixinMetrics2.secondaryButtonVisibility.equals("Unknown")) {
            str2 = footerBarMixinMetrics2.secondaryButtonVisibility;
        } else if (!isSecondaryButtonVisible) {
            str2 = "Invisible";
        }
        footerBarMixinMetrics2.secondaryButtonVisibility = str2;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        PersistableBundle persistableBundle;
        PersistableBundle persistableBundle2;
        super.onDetachedFromWindow();
        if (WizardManagerHelper.isAnySetupWizard(this.activity.getIntent())) {
            FooterBarMixin footerBarMixin = (FooterBarMixin) getMixin(FooterBarMixin.class);
            Objects.requireNonNull(footerBarMixin);
            FooterBarMixinMetrics footerBarMixinMetrics = footerBarMixin.metrics;
            boolean isPrimaryButtonVisible = footerBarMixin.isPrimaryButtonVisible();
            boolean isSecondaryButtonVisible = footerBarMixin.isSecondaryButtonVisible();
            Objects.requireNonNull(footerBarMixinMetrics);
            footerBarMixinMetrics.primaryButtonVisibility = FooterBarMixinMetrics.updateButtonVisibilityState(footerBarMixinMetrics.primaryButtonVisibility, isPrimaryButtonVisible);
            footerBarMixinMetrics.secondaryButtonVisibility = FooterBarMixinMetrics.updateButtonVisibilityState(footerBarMixinMetrics.secondaryButtonVisibility, isSecondaryButtonVisible);
            FooterButton footerButton = footerBarMixin.primaryButton;
            FooterButton footerButton2 = footerBarMixin.secondaryButton;
            if (footerButton != null) {
                persistableBundle = footerButton.getMetrics("PrimaryFooterButton");
            } else {
                persistableBundle = PersistableBundle.EMPTY;
            }
            if (footerButton2 != null) {
                persistableBundle2 = footerButton2.getMetrics("SecondaryFooterButton");
            } else {
                persistableBundle2 = PersistableBundle.EMPTY;
            }
            FooterBarMixinMetrics footerBarMixinMetrics2 = footerBarMixin.metrics;
            Objects.requireNonNull(footerBarMixinMetrics2);
            PersistableBundle persistableBundle3 = new PersistableBundle();
            persistableBundle3.putString(FooterBarMixinMetrics.EXTRA_PRIMARY_BUTTON_VISIBILITY, footerBarMixinMetrics2.primaryButtonVisibility);
            persistableBundle3.putString(FooterBarMixinMetrics.EXTRA_SECONDARY_BUTTON_VISIBILITY, footerBarMixinMetrics2.secondaryButtonVisibility);
            PersistableBundle[] persistableBundleArr = {persistableBundle2};
            Logger logger = PersistableBundles.LOG;
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(Arrays.asList(persistableBundle3, persistableBundle));
            Collections.addAll(arrayList, persistableBundleArr);
            PersistableBundle persistableBundle4 = new PersistableBundle();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                PersistableBundle persistableBundle5 = (PersistableBundle) it.next();
                for (String str : persistableBundle5.keySet()) {
                    R$array.checkArgument(!persistableBundle4.containsKey(str), String.format("Found duplicate key [%s] while attempting to merge bundles.", str));
                }
                persistableBundle4.putAll(persistableBundle5);
            }
            androidx.fragment.R$id.logCustomEvent(getContext(), CustomEvent.create(MetricKey.get("SetupCompatMetrics", this.activity), persistableBundle4));
        }
        getViewTreeObserver().removeOnWindowFocusChangeListener(this.windowFocusChangeListener);
    }

    public final boolean useFullDynamicColor() {
        if (!shouldApplyDynamicColor() || !this.useFullDynamicColorAttr) {
            return false;
        }
        return true;
    }

    public PartnerCustomizationLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.windowFocusChangeListener = new ViewTreeObserver.OnWindowFocusChangeListener() { // from class: com.google.android.setupcompat.PartnerCustomizationLayout$$ExternalSyntheticLambda0
            @Override // android.view.ViewTreeObserver.OnWindowFocusChangeListener
            public final void onWindowFocusChanged(boolean z) {
                PartnerCustomizationLayout.m163$r8$lambda$0I8CGaPG55DphyAapa3mtQRWk(PartnerCustomizationLayout.this, z);
            }
        };
        init(attributeSet, 2130969819);
    }

    @TargetApi(QSTileImpl.H.STALE)
    public PartnerCustomizationLayout(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.windowFocusChangeListener = new ViewTreeObserver.OnWindowFocusChangeListener() { // from class: com.google.android.setupcompat.PartnerCustomizationLayout$$ExternalSyntheticLambda0
            @Override // android.view.ViewTreeObserver.OnWindowFocusChangeListener
            public final void onWindowFocusChanged(boolean z) {
                PartnerCustomizationLayout.m163$r8$lambda$0I8CGaPG55DphyAapa3mtQRWk(PartnerCustomizationLayout.this, z);
            }
        };
        init(attributeSet, i);
    }
}
