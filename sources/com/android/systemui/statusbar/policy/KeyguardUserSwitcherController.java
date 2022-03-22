package com.android.systemui.statusbar.policy;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.core.graphics.ColorUtils;
import com.android.keyguard.KeyguardConstants;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardVisibilityHelper;
import com.android.settingslib.animation.AppearAnimationUtils;
import com.android.settingslib.drawable.CircleFramedDrawable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.communal.CommunalStateController;
import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.stack.AnimationProperties;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardUserSwitcherController extends ViewController<KeyguardUserSwitcherView> {
    public static final AnimationProperties ANIMATION_PROPERTIES;
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final KeyguardUserAdapter mAdapter;
    public final KeyguardUserSwitcherScrim mBackground;
    public int mBarState;
    public ObjectAnimator mBgAnimator;
    public float mDarkAmount;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final KeyguardVisibilityHelper mKeyguardVisibilityHelper;
    public KeyguardUserSwitcherListView mListView;
    public final ScreenLifecycle mScreenLifecycle;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final UserSwitcherController mUserSwitcherController;
    public boolean mUserSwitcherOpen;
    public final AnonymousClass1 mInfoCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.policy.KeyguardUserSwitcherController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardVisibilityChanged(boolean z) {
            if (KeyguardUserSwitcherController.DEBUG) {
                Log.d("KeyguardUserSwitcherController", String.format("onKeyguardVisibilityChanged %b", Boolean.valueOf(z)));
            }
            if (!z) {
                KeyguardUserSwitcherController.this.closeSwitcherIfOpenAndNotSimple(false);
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onUserSwitching(int i) {
            KeyguardUserSwitcherController.this.closeSwitcherIfOpenAndNotSimple(false);
        }
    };
    public final AnonymousClass2 mScreenObserver = new ScreenLifecycle.Observer() { // from class: com.android.systemui.statusbar.policy.KeyguardUserSwitcherController.2
        @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
        public final void onScreenTurnedOff() {
            if (KeyguardUserSwitcherController.DEBUG) {
                Log.d("KeyguardUserSwitcherController", "onScreenTurnedOff");
            }
            KeyguardUserSwitcherController.this.closeSwitcherIfOpenAndNotSimple(false);
        }
    };
    public final AnonymousClass3 mStatusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.policy.KeyguardUserSwitcherController.3
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onDozeAmountChanged(float f, float f2) {
            boolean z = true;
            if (KeyguardUserSwitcherController.DEBUG) {
                Log.d("KeyguardUserSwitcherController", String.format("onDozeAmountChanged: linearAmount=%f amount=%f", Float.valueOf(f), Float.valueOf(f2)));
            }
            KeyguardUserSwitcherController keyguardUserSwitcherController = KeyguardUserSwitcherController.this;
            Objects.requireNonNull(keyguardUserSwitcherController);
            if (f2 != 1.0f) {
                z = false;
            }
            if (f2 != keyguardUserSwitcherController.mDarkAmount) {
                keyguardUserSwitcherController.mDarkAmount = f2;
                KeyguardUserSwitcherListView keyguardUserSwitcherListView = keyguardUserSwitcherController.mListView;
                Objects.requireNonNull(keyguardUserSwitcherListView);
                int childCount = keyguardUserSwitcherListView.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    View childAt = keyguardUserSwitcherListView.getChildAt(i);
                    if (childAt instanceof KeyguardUserDetailItemView) {
                        KeyguardUserDetailItemView keyguardUserDetailItemView = (KeyguardUserDetailItemView) childAt;
                        Objects.requireNonNull(keyguardUserDetailItemView);
                        if (keyguardUserDetailItemView.mDarkAmount != f2) {
                            keyguardUserDetailItemView.mDarkAmount = f2;
                            keyguardUserDetailItemView.mName.setTextColor(ColorUtils.blendARGB(keyguardUserDetailItemView.mTextColor, -1, f2));
                        }
                    }
                }
                if (z) {
                    keyguardUserSwitcherController.closeSwitcherIfOpenAndNotSimple(false);
                }
            }
        }

        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public final void onStateChanged(int i) {
            if (KeyguardUserSwitcherController.DEBUG) {
                Log.d("KeyguardUserSwitcherController", String.format("onStateChanged: newState=%d", Integer.valueOf(i)));
            }
            boolean goingToFullShade = KeyguardUserSwitcherController.this.mStatusBarStateController.goingToFullShade();
            boolean isKeyguardFadingAway = KeyguardUserSwitcherController.this.mKeyguardStateController.isKeyguardFadingAway();
            KeyguardUserSwitcherController keyguardUserSwitcherController = KeyguardUserSwitcherController.this;
            int i2 = keyguardUserSwitcherController.mBarState;
            keyguardUserSwitcherController.mBarState = i;
            if (keyguardUserSwitcherController.mStatusBarStateController.goingToFullShade() || KeyguardUserSwitcherController.this.mKeyguardStateController.isKeyguardFadingAway()) {
                KeyguardUserSwitcherController.this.closeSwitcherIfOpenAndNotSimple(true);
            }
            KeyguardUserSwitcherController keyguardUserSwitcherController2 = KeyguardUserSwitcherController.this;
            Objects.requireNonNull(keyguardUserSwitcherController2);
            keyguardUserSwitcherController2.mKeyguardVisibilityHelper.setViewVisibility(i, isKeyguardFadingAway, goingToFullShade, i2);
        }
    };
    public final AnonymousClass4 mDataSetObserver = new DataSetObserver() { // from class: com.android.systemui.statusbar.policy.KeyguardUserSwitcherController.4
        @Override // android.database.DataSetObserver
        public final void onChanged() {
            KeyguardUserSwitcherController keyguardUserSwitcherController = KeyguardUserSwitcherController.this;
            Objects.requireNonNull(keyguardUserSwitcherController);
            int childCount = keyguardUserSwitcherController.mListView.getChildCount();
            int count = keyguardUserSwitcherController.mAdapter.getCount();
            int max = Math.max(childCount, count);
            if (KeyguardUserSwitcherController.DEBUG) {
                Log.d("KeyguardUserSwitcherController", String.format("refreshUserList childCount=%d adapterCount=%d", Integer.valueOf(childCount), Integer.valueOf(count)));
            }
            boolean z = false;
            for (int i = 0; i < max; i++) {
                if (i < count) {
                    View view = null;
                    if (i < childCount) {
                        view = keyguardUserSwitcherController.mListView.getChildAt(i);
                    }
                    KeyguardUserDetailItemView keyguardUserDetailItemView = (KeyguardUserDetailItemView) keyguardUserSwitcherController.mAdapter.getView(i, view, keyguardUserSwitcherController.mListView);
                    UserSwitcherController.UserRecord userRecord = (UserSwitcherController.UserRecord) keyguardUserDetailItemView.getTag();
                    if (userRecord.isCurrent) {
                        if (i != 0) {
                            Log.w("KeyguardUserSwitcherController", "Current user is not the first view in the list");
                        }
                        int i2 = userRecord.info.id;
                        keyguardUserDetailItemView.updateVisibilities(true, keyguardUserSwitcherController.mUserSwitcherOpen, false);
                        z = true;
                    } else {
                        keyguardUserDetailItemView.updateVisibilities(keyguardUserSwitcherController.mUserSwitcherOpen, true, false);
                    }
                    float f = keyguardUserSwitcherController.mDarkAmount;
                    if (keyguardUserDetailItemView.mDarkAmount != f) {
                        keyguardUserDetailItemView.mDarkAmount = f;
                        keyguardUserDetailItemView.mName.setTextColor(ColorUtils.blendARGB(keyguardUserDetailItemView.mTextColor, -1, f));
                    }
                    if (view == null) {
                        keyguardUserSwitcherController.mListView.addView(keyguardUserDetailItemView);
                    } else if (view != keyguardUserDetailItemView) {
                        KeyguardUserSwitcherListView keyguardUserSwitcherListView = keyguardUserSwitcherController.mListView;
                        Objects.requireNonNull(keyguardUserSwitcherListView);
                        keyguardUserSwitcherListView.removeViewAt(i);
                        keyguardUserSwitcherListView.addView(keyguardUserDetailItemView, i);
                    }
                } else {
                    KeyguardUserSwitcherListView keyguardUserSwitcherListView2 = keyguardUserSwitcherController.mListView;
                    Objects.requireNonNull(keyguardUserSwitcherListView2);
                    keyguardUserSwitcherListView2.removeViewAt(keyguardUserSwitcherListView2.getChildCount() - 1);
                }
            }
            if (!z) {
                Log.w("KeyguardUserSwitcherController", "Current user is not listed");
            }
        }
    };

    /* JADX WARN: Type inference failed for: r0v0, types: [com.android.systemui.statusbar.policy.KeyguardUserSwitcherController$1] */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.statusbar.policy.KeyguardUserSwitcherController$2] */
    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.statusbar.policy.KeyguardUserSwitcherController$3] */
    /* JADX WARN: Type inference failed for: r0v3, types: [com.android.systemui.statusbar.policy.KeyguardUserSwitcherController$4] */
    public KeyguardUserSwitcherController(KeyguardUserSwitcherView keyguardUserSwitcherView, Context context, Resources resources, LayoutInflater layoutInflater, ScreenLifecycle screenLifecycle, UserSwitcherController userSwitcherController, CommunalStateController communalStateController, KeyguardStateController keyguardStateController, SysuiStatusBarStateController sysuiStatusBarStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, ScreenOffAnimationController screenOffAnimationController) {
        super(keyguardUserSwitcherView);
        if (DEBUG) {
            Log.d("KeyguardUserSwitcherController", "New KeyguardUserSwitcherController");
        }
        this.mScreenLifecycle = screenLifecycle;
        this.mUserSwitcherController = userSwitcherController;
        this.mKeyguardStateController = keyguardStateController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mAdapter = new KeyguardUserAdapter(context, resources, layoutInflater, userSwitcherController, this);
        this.mKeyguardVisibilityHelper = new KeyguardVisibilityHelper(keyguardUserSwitcherView, communalStateController, keyguardStateController, screenOffAnimationController, false, false);
        this.mBackground = new KeyguardUserSwitcherScrim(context);
    }

    /* loaded from: classes.dex */
    public static class KeyguardUserAdapter extends UserSwitcherController.BaseUserAdapter implements View.OnClickListener {
        public final Context mContext;
        public KeyguardUserSwitcherController mKeyguardUserSwitcherController;
        public final LayoutInflater mLayoutInflater;
        public final Resources mResources;
        public ArrayList<UserSwitcherController.UserRecord> mUsersOrdered = new ArrayList<>();

        public KeyguardUserAdapter(Context context, Resources resources, LayoutInflater layoutInflater, UserSwitcherController userSwitcherController, KeyguardUserSwitcherController keyguardUserSwitcherController) {
            super(userSwitcherController);
            this.mContext = context;
            this.mResources = resources;
            this.mLayoutInflater = layoutInflater;
            this.mKeyguardUserSwitcherController = keyguardUserSwitcherController;
        }

        @Override // android.widget.Adapter
        public final View getView(int i, View view, ViewGroup viewGroup) {
            float f;
            ColorMatrixColorFilter colorMatrixColorFilter;
            Drawable drawable;
            int i2;
            UserSwitcherController.UserRecord item = getItem(i);
            int i3 = 0;
            if (!(view instanceof KeyguardUserDetailItemView) || !(view.getTag() instanceof UserSwitcherController.UserRecord)) {
                view = this.mLayoutInflater.inflate(2131624174, viewGroup, false);
            }
            KeyguardUserDetailItemView keyguardUserDetailItemView = (KeyguardUserDetailItemView) view;
            keyguardUserDetailItemView.setOnClickListener(this);
            String name = getName(this.mContext, item);
            if (item.picture == null) {
                if (!item.isCurrent || !item.isGuest) {
                    drawable = UserSwitcherController.BaseUserAdapter.getIconDrawable(this.mContext, item);
                } else {
                    drawable = this.mContext.getDrawable(2131231760);
                }
                if (item.isSwitchToEnabled) {
                    i2 = 2131099893;
                } else {
                    i2 = 2131099894;
                }
                drawable.setTint(this.mResources.getColor(i2, this.mContext.getTheme()));
                Drawable mutate = new LayerDrawable(new Drawable[]{this.mContext.getDrawable(2131232349), drawable}).mutate();
                int resolveId = item.resolveId();
                keyguardUserDetailItemView.mName.setText(name);
                keyguardUserDetailItemView.mAvatar.setDrawableWithBadge(mutate, resolveId);
            } else {
                CircleFramedDrawable circleFramedDrawable = new CircleFramedDrawable(item.picture, (int) this.mResources.getDimension(2131165866));
                if (item.isSwitchToEnabled) {
                    colorMatrixColorFilter = null;
                } else {
                    ColorMatrix colorMatrix = new ColorMatrix();
                    colorMatrix.setSaturation(0.0f);
                    colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);
                }
                circleFramedDrawable.setColorFilter(colorMatrixColorFilter);
                int i4 = item.info.id;
                keyguardUserDetailItemView.mName.setText(name);
                keyguardUserDetailItemView.mAvatar.setDrawableWithBadge(circleFramedDrawable, i4);
            }
            keyguardUserDetailItemView.setActivated(item.isCurrent);
            boolean z = item.isDisabledByAdmin;
            View view2 = keyguardUserDetailItemView.mRestrictedPadlock;
            if (view2 != null) {
                if (!z) {
                    i3 = 8;
                }
                view2.setVisibility(i3);
            }
            keyguardUserDetailItemView.setEnabled(!z);
            keyguardUserDetailItemView.setEnabled(item.isSwitchToEnabled);
            if (keyguardUserDetailItemView.isEnabled()) {
                f = 1.0f;
            } else {
                f = 0.38f;
            }
            keyguardUserDetailItemView.setAlpha(f);
            keyguardUserDetailItemView.setTag(item);
            return keyguardUserDetailItemView;
        }

        @Override // android.widget.BaseAdapter
        public final void notifyDataSetChanged() {
            ArrayList<UserSwitcherController.UserRecord> users = super.getUsers();
            this.mUsersOrdered = new ArrayList<>(users.size());
            for (int i = 0; i < users.size(); i++) {
                UserSwitcherController.UserRecord userRecord = users.get(i);
                if (userRecord.isCurrent) {
                    this.mUsersOrdered.add(0, userRecord);
                } else {
                    this.mUsersOrdered.add(userRecord);
                }
            }
            super.notifyDataSetChanged();
        }

        /* JADX WARN: Removed duplicated region for block: B:10:0x0025  */
        /* JADX WARN: Removed duplicated region for block: B:9:0x0024 A[RETURN] */
        @Override // android.view.View.OnClickListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final void onClick(android.view.View r4) {
            /*
                r3 = this;
                java.lang.Object r4 = r4.getTag()
                com.android.systemui.statusbar.policy.UserSwitcherController$UserRecord r4 = (com.android.systemui.statusbar.policy.UserSwitcherController.UserRecord) r4
                com.android.systemui.statusbar.policy.KeyguardUserSwitcherController r0 = r3.mKeyguardUserSwitcherController
                java.util.Objects.requireNonNull(r0)
                com.android.keyguard.KeyguardVisibilityHelper r1 = r0.mKeyguardVisibilityHelper
                java.util.Objects.requireNonNull(r1)
                boolean r1 = r1.mKeyguardViewVisibilityAnimating
                r2 = 1
                if (r1 != 0) goto L_0x0021
                com.android.systemui.statusbar.policy.KeyguardUserSwitcherListView r0 = r0.mListView
                java.util.Objects.requireNonNull(r0)
                boolean r0 = r0.mAnimating
                if (r0 == 0) goto L_0x001f
                goto L_0x0021
            L_0x001f:
                r0 = 0
                goto L_0x0022
            L_0x0021:
                r0 = r2
            L_0x0022:
                if (r0 == 0) goto L_0x0025
                return
            L_0x0025:
                com.android.systemui.statusbar.policy.KeyguardUserSwitcherController r0 = r3.mKeyguardUserSwitcherController
                java.util.Objects.requireNonNull(r0)
                boolean r0 = r0.mUserSwitcherOpen
                if (r0 == 0) goto L_0x0042
                boolean r0 = r4.isCurrent
                if (r0 == 0) goto L_0x003d
                boolean r0 = r4.isGuest
                if (r0 == 0) goto L_0x0037
                goto L_0x003d
            L_0x0037:
                com.android.systemui.statusbar.policy.KeyguardUserSwitcherController r3 = r3.mKeyguardUserSwitcherController
                r3.closeSwitcherIfOpenAndNotSimple(r2)
                goto L_0x0047
            L_0x003d:
                r0 = 0
                r3.onUserListItemClicked(r4, r0)
                goto L_0x0047
            L_0x0042:
                com.android.systemui.statusbar.policy.KeyguardUserSwitcherController r3 = r3.mKeyguardUserSwitcherController
                r3.setUserSwitcherOpened(r2, r2)
            L_0x0047:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.KeyguardUserSwitcherController.KeyguardUserAdapter.onClick(android.view.View):void");
        }

        @Override // com.android.systemui.statusbar.policy.UserSwitcherController.BaseUserAdapter
        public final ArrayList<UserSwitcherController.UserRecord> getUsers() {
            return this.mUsersOrdered;
        }
    }

    static {
        AnimationProperties animationProperties = new AnimationProperties();
        animationProperties.duration = 360L;
        ANIMATION_PROPERTIES = animationProperties;
    }

    public final boolean closeSwitcherIfOpenAndNotSimple(boolean z) {
        if (this.mUserSwitcherOpen) {
            UserSwitcherController userSwitcherController = this.mUserSwitcherController;
            Objects.requireNonNull(userSwitcherController);
            if (!userSwitcherController.mSimpleUserSwitcher) {
                setUserSwitcherOpened(false, z);
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        if (DEBUG) {
            Log.d("KeyguardUserSwitcherController", "onInit");
        }
        this.mListView = (KeyguardUserSwitcherListView) ((KeyguardUserSwitcherView) this.mView).findViewById(2131428196);
        ((KeyguardUserSwitcherView) this.mView).setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.statusbar.policy.KeyguardUserSwitcherController$$ExternalSyntheticLambda0
            /* JADX WARN: Removed duplicated region for block: B:11:? A[RETURN, SYNTHETIC] */
            /* JADX WARN: Removed duplicated region for block: B:9:0x001f  */
            @Override // android.view.View.OnTouchListener
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            public final boolean onTouch(android.view.View r2, android.view.MotionEvent r3) {
                /*
                    r1 = this;
                    com.android.systemui.statusbar.policy.KeyguardUserSwitcherController r1 = com.android.systemui.statusbar.policy.KeyguardUserSwitcherController.this
                    java.util.Objects.requireNonNull(r1)
                    com.android.keyguard.KeyguardVisibilityHelper r2 = r1.mKeyguardVisibilityHelper
                    java.util.Objects.requireNonNull(r2)
                    boolean r2 = r2.mKeyguardViewVisibilityAnimating
                    r3 = 0
                    r0 = 1
                    if (r2 != 0) goto L_0x001c
                    com.android.systemui.statusbar.policy.KeyguardUserSwitcherListView r2 = r1.mListView
                    java.util.Objects.requireNonNull(r2)
                    boolean r2 = r2.mAnimating
                    if (r2 == 0) goto L_0x001a
                    goto L_0x001c
                L_0x001a:
                    r2 = r3
                    goto L_0x001d
                L_0x001c:
                    r2 = r0
                L_0x001d:
                    if (r2 != 0) goto L_0x0023
                    boolean r3 = r1.closeSwitcherIfOpenAndNotSimple(r0)
                L_0x0023:
                    return r3
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.policy.KeyguardUserSwitcherController$$ExternalSyntheticLambda0.onTouch(android.view.View, android.view.MotionEvent):boolean");
            }
        });
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        if (DEBUG) {
            Log.d("KeyguardUserSwitcherController", "onViewAttached");
        }
        this.mAdapter.registerDataSetObserver(this.mDataSetObserver);
        this.mAdapter.notifyDataSetChanged();
        this.mKeyguardUpdateMonitor.registerCallback(this.mInfoCallback);
        this.mStatusBarStateController.addCallback(this.mStatusBarStateListener);
        this.mScreenLifecycle.addObserver(this.mScreenObserver);
        UserSwitcherController userSwitcherController = this.mUserSwitcherController;
        Objects.requireNonNull(userSwitcherController);
        if (userSwitcherController.mSimpleUserSwitcher) {
            setUserSwitcherOpened(true, true);
            return;
        }
        ((KeyguardUserSwitcherView) this.mView).addOnLayoutChangeListener(this.mBackground);
        ((KeyguardUserSwitcherView) this.mView).setBackground(this.mBackground);
        this.mBackground.setAlpha(0);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        if (DEBUG) {
            Log.d("KeyguardUserSwitcherController", "onViewDetached");
        }
        closeSwitcherIfOpenAndNotSimple(false);
        this.mAdapter.unregisterDataSetObserver(this.mDataSetObserver);
        this.mKeyguardUpdateMonitor.removeCallback(this.mInfoCallback);
        this.mStatusBarStateController.removeCallback(this.mStatusBarStateListener);
        ScreenLifecycle screenLifecycle = this.mScreenLifecycle;
        AnonymousClass2 r2 = this.mScreenObserver;
        Objects.requireNonNull(screenLifecycle);
        screenLifecycle.mObservers.remove(r2);
        ((KeyguardUserSwitcherView) this.mView).removeOnLayoutChangeListener(this.mBackground);
        ((KeyguardUserSwitcherView) this.mView).setBackground(null);
        this.mBackground.setAlpha(0);
    }

    public final void setUserSwitcherOpened(boolean z, boolean z2) {
        AppearAnimationUtils appearAnimationUtils;
        Runnable runnable;
        float f;
        int length;
        boolean z3 = DEBUG;
        if (z3) {
            Log.d("KeyguardUserSwitcherController", String.format("setUserSwitcherOpened: %b -> %b (animate=%b)", Boolean.valueOf(this.mUserSwitcherOpen), Boolean.valueOf(z), Boolean.valueOf(z2)));
        }
        this.mUserSwitcherOpen = z;
        if (z3) {
            Log.d("KeyguardUserSwitcherController", String.format("updateVisibilities: animate=%b", Boolean.valueOf(z2)));
        }
        ObjectAnimator objectAnimator = this.mBgAnimator;
        if (objectAnimator != null) {
            objectAnimator.cancel();
        }
        if (this.mUserSwitcherOpen) {
            ObjectAnimator ofInt = ObjectAnimator.ofInt(this.mBackground, "alpha", 0, 255);
            this.mBgAnimator = ofInt;
            ofInt.setDuration(400L);
            this.mBgAnimator.setInterpolator(Interpolators.ALPHA_IN);
            this.mBgAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.policy.KeyguardUserSwitcherController.5
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    KeyguardUserSwitcherController.this.mBgAnimator = null;
                }
            });
            this.mBgAnimator.start();
        } else {
            ObjectAnimator ofInt2 = ObjectAnimator.ofInt(this.mBackground, "alpha", 255, 0);
            this.mBgAnimator = ofInt2;
            ofInt2.setDuration(400L);
            this.mBgAnimator.setInterpolator(Interpolators.ALPHA_OUT);
            this.mBgAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.policy.KeyguardUserSwitcherController.6
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    KeyguardUserSwitcherController.this.mBgAnimator = null;
                }
            });
            this.mBgAnimator.start();
        }
        final KeyguardUserSwitcherListView keyguardUserSwitcherListView = this.mListView;
        final boolean z4 = this.mUserSwitcherOpen;
        if (KeyguardUserSwitcherListView.DEBUG) {
            Objects.requireNonNull(keyguardUserSwitcherListView);
            Log.d("KeyguardUserSwitcherListView", String.format("updateVisibilities: open=%b animate=%b childCount=%d", Boolean.valueOf(z4), Boolean.valueOf(z2), Integer.valueOf(keyguardUserSwitcherListView.getChildCount())));
        }
        keyguardUserSwitcherListView.mAnimating = false;
        int childCount = keyguardUserSwitcherListView.getChildCount();
        final KeyguardUserDetailItemView[] keyguardUserDetailItemViewArr = new KeyguardUserDetailItemView[childCount];
        for (int i = 0; i < childCount; i++) {
            keyguardUserDetailItemViewArr[i] = (KeyguardUserDetailItemView) keyguardUserSwitcherListView.getChildAt(i);
            keyguardUserDetailItemViewArr[i].clearAnimation();
            if (i == 0) {
                keyguardUserDetailItemViewArr[i].updateVisibilities(true, z4, z2);
                keyguardUserDetailItemViewArr[i].setClickable(true);
            } else {
                keyguardUserDetailItemViewArr[i].setClickable(z4);
                if (z4) {
                    keyguardUserDetailItemViewArr[i].updateVisibilities(true, true, false);
                }
            }
        }
        if (z2 && childCount > 1) {
            keyguardUserDetailItemViewArr[0] = null;
            keyguardUserSwitcherListView.setClipChildren(false);
            keyguardUserSwitcherListView.setClipToPadding(false);
            keyguardUserSwitcherListView.mAnimating = true;
            if (z4) {
                appearAnimationUtils = keyguardUserSwitcherListView.mAppearAnimationUtils;
            } else {
                appearAnimationUtils = keyguardUserSwitcherListView.mDisappearAnimationUtils;
            }
            Runnable keyguardUserSwitcherListView$$ExternalSyntheticLambda0 = new Runnable() { // from class: com.android.systemui.statusbar.policy.KeyguardUserSwitcherListView$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    KeyguardUserSwitcherListView keyguardUserSwitcherListView2 = KeyguardUserSwitcherListView.this;
                    boolean z5 = z4;
                    KeyguardUserDetailItemView[] keyguardUserDetailItemViewArr2 = keyguardUserDetailItemViewArr;
                    boolean z6 = KeyguardUserSwitcherListView.DEBUG;
                    Objects.requireNonNull(keyguardUserSwitcherListView2);
                    keyguardUserSwitcherListView2.setClipChildren(true);
                    keyguardUserSwitcherListView2.setClipToPadding(true);
                    keyguardUserSwitcherListView2.mAnimating = false;
                    if (!z5) {
                        for (int i2 = 1; i2 < keyguardUserDetailItemViewArr2.length; i2++) {
                            keyguardUserDetailItemViewArr2[i2].updateVisibilities(false, true, false);
                        }
                    }
                }
            };
            Objects.requireNonNull(appearAnimationUtils);
            AppearAnimationUtils.AppearAnimationProperties appearAnimationProperties = appearAnimationUtils.mProperties;
            appearAnimationProperties.maxDelayColIndex = -1;
            appearAnimationProperties.maxDelayRowIndex = -1;
            appearAnimationProperties.delays = new long[childCount];
            long j = -1;
            for (int i2 = 0; i2 < childCount; i2++) {
                appearAnimationUtils.mProperties.delays[i2] = new long[1];
                long calculateDelay = appearAnimationUtils.calculateDelay(i2, 0);
                AppearAnimationUtils.AppearAnimationProperties appearAnimationProperties2 = appearAnimationUtils.mProperties;
                appearAnimationProperties2.delays[i2][0] = calculateDelay;
                if (keyguardUserDetailItemViewArr[i2] != null && calculateDelay > j) {
                    appearAnimationProperties2.maxDelayColIndex = 0;
                    appearAnimationProperties2.maxDelayRowIndex = i2;
                    j = calculateDelay;
                }
            }
            AppearAnimationUtils.AppearAnimationProperties appearAnimationProperties3 = appearAnimationUtils.mProperties;
            if (appearAnimationProperties3.maxDelayRowIndex == -1 || appearAnimationProperties3.maxDelayColIndex == -1) {
                keyguardUserSwitcherListView$$ExternalSyntheticLambda0.run();
                return;
            }
            int i3 = 0;
            while (true) {
                long[][] jArr = appearAnimationProperties3.delays;
                if (i3 < jArr.length) {
                    long j2 = jArr[i3][0];
                    if (appearAnimationProperties3.maxDelayRowIndex == i3 && appearAnimationProperties3.maxDelayColIndex == 0) {
                        runnable = keyguardUserSwitcherListView$$ExternalSyntheticLambda0;
                    } else {
                        runnable = null;
                    }
                    if (appearAnimationUtils.mRowTranslationScaler != null) {
                        f = (float) (Math.pow(length - i3, 2.0d) / jArr.length);
                    } else {
                        f = 1.0f;
                    }
                    float f2 = f * appearAnimationUtils.mStartTranslation;
                    KeyguardUserDetailItemView keyguardUserDetailItemView = keyguardUserDetailItemViewArr[i3];
                    long j3 = appearAnimationUtils.mDuration;
                    boolean z5 = appearAnimationUtils.mAppearing;
                    if (!z5) {
                        f2 = -f2;
                    }
                    appearAnimationUtils.createAnimation((View) keyguardUserDetailItemView, j2, j3, f2, z5, appearAnimationUtils.mInterpolator, runnable);
                    i3++;
                    keyguardUserSwitcherListView$$ExternalSyntheticLambda0 = keyguardUserSwitcherListView$$ExternalSyntheticLambda0;
                } else {
                    return;
                }
            }
        }
    }
}
