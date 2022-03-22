package com.android.systemui.privacy.television;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.util.Log;
import android.view.IWindowManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.systemui.CoreStartable;
import com.android.systemui.ImageWallpaper$GLEngine$$ExternalSyntheticLambda0;
import com.android.systemui.doze.DozeUi$$ExternalSyntheticLambda1;
import com.android.systemui.privacy.PrivacyChipBuilder;
import com.android.systemui.privacy.PrivacyItem;
import com.android.systemui.privacy.PrivacyItemController;
import com.android.systemui.privacy.PrivacyType;
import com.android.systemui.privacy.television.PrivacyChipDrawable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class TvOngoingPrivacyChip extends CoreStartable implements PrivacyItemController.Callback, PrivacyChipDrawable.PrivacyChipDrawableListener {
    public boolean mAllIndicatorsEnabled;
    public final int mAnimationDurationMs;
    public ObjectAnimator mAnimator;
    public PrivacyChipDrawable mChipDrawable;
    public final Context mContext;
    public final IWindowManager mIWindowManager;
    public final int mIconMarginStart;
    public final int mIconSize;
    public LinearLayout mIconsContainer;
    public ViewGroup mIndicatorView;
    public boolean mIsRtl;
    public boolean mMicCameraIndicatorFlagEnabled;
    public final PrivacyItemController mPrivacyItemController;
    public boolean mViewAndWindowAdded;
    public final Rect[] mBounds = new Rect[4];
    public List<PrivacyItem> mPrivacyItems = Collections.emptyList();
    public final Handler mUiThreadHandler = new Handler(Looper.getMainLooper());
    public final DozeUi$$ExternalSyntheticLambda1 mCollapseRunnable = new DozeUi$$ExternalSyntheticLambda1(this, 4);
    public final ImageWallpaper$GLEngine$$ExternalSyntheticLambda0 mAccessibilityRunnable = new ImageWallpaper$GLEngine$$ExternalSyntheticLambda0(this, 3);
    public final LinkedList mItemsBeforeLastAnnouncement = new LinkedList();
    public int mState = 0;

    public final void createAndShowIndicator() {
        int i;
        this.mState = 1;
        if (this.mIndicatorView != null || this.mViewAndWindowAdded) {
            removeIndicatorView();
        }
        ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this.mContext).inflate(2131624632, (ViewGroup) null);
        this.mIndicatorView = viewGroup;
        viewGroup.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.systemui.privacy.television.TvOngoingPrivacyChip.1
            @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
            public final void onGlobalLayout() {
                TvOngoingPrivacyChip tvOngoingPrivacyChip = TvOngoingPrivacyChip.this;
                if (tvOngoingPrivacyChip.mState == 1) {
                    tvOngoingPrivacyChip.mViewAndWindowAdded = true;
                    tvOngoingPrivacyChip.mIndicatorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    TvOngoingPrivacyChip.this.postAccessibilityAnnouncement();
                    TvOngoingPrivacyChip tvOngoingPrivacyChip2 = TvOngoingPrivacyChip.this;
                    Objects.requireNonNull(tvOngoingPrivacyChip2);
                    tvOngoingPrivacyChip2.animateIconAlphaTo(1.0f);
                    PrivacyChipDrawable privacyChipDrawable = TvOngoingPrivacyChip.this.mChipDrawable;
                    Objects.requireNonNull(privacyChipDrawable);
                    privacyChipDrawable.mFadeIn.start();
                }
            }
        });
        PrivacyChipDrawable privacyChipDrawable = new PrivacyChipDrawable(this.mContext);
        this.mChipDrawable = privacyChipDrawable;
        Objects.requireNonNull(privacyChipDrawable);
        privacyChipDrawable.mListener = this;
        PrivacyChipDrawable privacyChipDrawable2 = this.mChipDrawable;
        boolean z = this.mIsRtl;
        Objects.requireNonNull(privacyChipDrawable2);
        privacyChipDrawable2.mIsRtl = z;
        ImageView imageView = (ImageView) this.mIndicatorView.findViewById(2131427699);
        if (imageView != null) {
            imageView.setImageDrawable(this.mChipDrawable);
        }
        LinearLayout linearLayout = (LinearLayout) this.mIndicatorView.findViewById(2131428110);
        this.mIconsContainer = linearLayout;
        linearLayout.setAlpha(0.0f);
        updateIcons();
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        ViewGroup viewGroup2 = this.mIndicatorView;
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, -2, 2006, 8, -3);
        if (this.mIsRtl) {
            i = 3;
        } else {
            i = 5;
        }
        layoutParams.gravity = i | 48;
        layoutParams.setTitle("MicrophoneCaptureIndicator");
        layoutParams.packageName = this.mContext.getPackageName();
        windowManager.addView(viewGroup2, layoutParams);
    }

    public final void animateIconAlphaTo(float f) {
        ObjectAnimator objectAnimator = this.mAnimator;
        if (objectAnimator == null) {
            ObjectAnimator objectAnimator2 = new ObjectAnimator();
            this.mAnimator = objectAnimator2;
            objectAnimator2.setTarget(this.mIconsContainer);
            this.mAnimator.setProperty(View.ALPHA);
            this.mAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.privacy.television.TvOngoingPrivacyChip.2
                public boolean mCancelled;

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationCancel(Animator animator) {
                    this.mCancelled = true;
                }

                @Override // android.animation.Animator.AnimatorListener
                public final void onAnimationStart(Animator animator, boolean z) {
                    this.mCancelled = false;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    if (!this.mCancelled) {
                        TvOngoingPrivacyChip tvOngoingPrivacyChip = TvOngoingPrivacyChip.this;
                        Objects.requireNonNull(tvOngoingPrivacyChip);
                        int i = tvOngoingPrivacyChip.mState;
                        if (i == 1 || i == 2) {
                            tvOngoingPrivacyChip.mUiThreadHandler.removeCallbacks(tvOngoingPrivacyChip.mCollapseRunnable);
                            tvOngoingPrivacyChip.mUiThreadHandler.postDelayed(tvOngoingPrivacyChip.mCollapseRunnable, 4000L);
                        }
                        int i2 = tvOngoingPrivacyChip.mState;
                        if (i2 == 1) {
                            tvOngoingPrivacyChip.mState = 2;
                        } else if (i2 == 4) {
                            tvOngoingPrivacyChip.removeIndicatorView();
                            tvOngoingPrivacyChip.mState = 0;
                        }
                    }
                }
            });
        } else if (objectAnimator.isRunning()) {
            this.mAnimator.cancel();
        }
        if (this.mIconsContainer.getAlpha() != f) {
            this.mAnimator.setDuration(this.mAnimationDurationMs);
            this.mAnimator.setFloatValues(f);
            this.mAnimator.start();
        }
    }

    public final void fadeOutIndicator() {
        int i = this.mState;
        if (i != 0 && i != 4) {
            this.mUiThreadHandler.removeCallbacks(this.mCollapseRunnable);
            if (this.mViewAndWindowAdded) {
                this.mState = 4;
                animateIconAlphaTo(0.0f);
            } else {
                this.mState = 0;
                removeIndicatorView();
            }
            PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
            if (privacyChipDrawable != null) {
                privacyChipDrawable.updateIcons(0);
            }
        }
    }

    public final void makeAccessibilityAnnouncement() {
        int i;
        if (this.mIndicatorView != null) {
            LinkedList linkedList = this.mItemsBeforeLastAnnouncement;
            PrivacyType privacyType = PrivacyType.TYPE_CAMERA;
            boolean listContainsPrivacyType = listContainsPrivacyType(linkedList, privacyType);
            boolean listContainsPrivacyType2 = listContainsPrivacyType(this.mPrivacyItems, privacyType);
            LinkedList linkedList2 = this.mItemsBeforeLastAnnouncement;
            PrivacyType privacyType2 = PrivacyType.TYPE_MICROPHONE;
            boolean listContainsPrivacyType3 = listContainsPrivacyType(linkedList2, privacyType2);
            boolean listContainsPrivacyType4 = listContainsPrivacyType(this.mPrivacyItems, privacyType2);
            if (!listContainsPrivacyType && listContainsPrivacyType2 && !listContainsPrivacyType3 && listContainsPrivacyType4) {
                i = 2131952758;
            } else if (!listContainsPrivacyType || listContainsPrivacyType2 || !listContainsPrivacyType3 || listContainsPrivacyType4) {
                if (listContainsPrivacyType && !listContainsPrivacyType2) {
                    i = 2131952093;
                } else if (listContainsPrivacyType || !listContainsPrivacyType2) {
                    i = 0;
                } else {
                    i = 2131952092;
                }
                if (i != 0) {
                    this.mIndicatorView.announceForAccessibility(this.mContext.getString(i));
                    i = 0;
                }
                if (listContainsPrivacyType3 && !listContainsPrivacyType4) {
                    i = 2131952761;
                } else if (!listContainsPrivacyType3 && listContainsPrivacyType4) {
                    i = 2131952760;
                }
            } else {
                i = 2131952759;
            }
            if (i != 0) {
                this.mIndicatorView.announceForAccessibility(this.mContext.getString(i));
            }
            this.mItemsBeforeLastAnnouncement.clear();
            this.mItemsBeforeLastAnnouncement.addAll(this.mPrivacyItems);
        }
    }

    @Override // com.android.systemui.privacy.PrivacyItemController.Callback
    public final void onFlagMicCameraChanged(boolean z) {
        boolean z2;
        this.mMicCameraIndicatorFlagEnabled = z;
        if (z || this.mAllIndicatorsEnabled) {
            z2 = false;
        } else {
            z2 = true;
        }
        if (z2) {
            fadeOutIndicator();
        } else {
            updateChip();
        }
    }

    @Override // com.android.systemui.privacy.PrivacyItemController.Callback
    public final void onPrivacyItemsChanged(List<PrivacyItem> list) {
        boolean z;
        ArrayList arrayList = new ArrayList(list);
        arrayList.removeIf(TvOngoingPrivacyChip$$ExternalSyntheticLambda0.INSTANCE);
        if (this.mMicCameraIndicatorFlagEnabled || this.mAllIndicatorsEnabled) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            fadeOutIndicator();
            this.mPrivacyItems = arrayList;
        } else if (arrayList.size() != this.mPrivacyItems.size() || !this.mPrivacyItems.containsAll(arrayList)) {
            this.mPrivacyItems = arrayList;
            postAccessibilityAnnouncement();
            updateChip();
        }
    }

    public final void postAccessibilityAnnouncement() {
        this.mUiThreadHandler.removeCallbacks(this.mAccessibilityRunnable);
        if (this.mPrivacyItems.size() == 0) {
            makeAccessibilityAnnouncement();
        } else {
            this.mUiThreadHandler.postDelayed(this.mAccessibilityRunnable, 500L);
        }
    }

    public final void removeIndicatorView() {
        ViewGroup viewGroup;
        WindowManager windowManager = (WindowManager) this.mContext.getSystemService(WindowManager.class);
        if (!(windowManager == null || (viewGroup = this.mIndicatorView) == null)) {
            windowManager.removeView(viewGroup);
        }
        this.mIndicatorView = null;
        this.mAnimator = null;
        PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
        if (privacyChipDrawable != null) {
            Objects.requireNonNull(privacyChipDrawable);
            privacyChipDrawable.mListener = null;
            this.mChipDrawable = null;
        }
        this.mViewAndWindowAdded = false;
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        this.mPrivacyItemController.addCallback(this);
    }

    public final void updateChip() {
        if (this.mPrivacyItems.isEmpty()) {
            fadeOutIndicator();
            return;
        }
        int i = this.mState;
        if (i == 0) {
            createAndShowIndicator();
        } else if (i == 1 || i == 2) {
            updateIcons();
            this.mUiThreadHandler.removeCallbacks(this.mCollapseRunnable);
            this.mUiThreadHandler.postDelayed(this.mCollapseRunnable, 4000L);
        } else if (i == 3 || i == 4) {
            this.mState = 2;
            updateIcons();
            animateIconAlphaTo(1.0f);
        }
    }

    public final void updateIcons() {
        ArrayList generateIcons = new PrivacyChipBuilder(this.mContext, this.mPrivacyItems).generateIcons();
        this.mIconsContainer.removeAllViews();
        for (int i = 0; i < generateIcons.size(); i++) {
            Drawable drawable = (Drawable) generateIcons.get(i);
            drawable.mutate().setTint(this.mContext.getColor(2131100508));
            ImageView imageView = new ImageView(this.mContext);
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            LinearLayout linearLayout = this.mIconsContainer;
            int i2 = this.mIconSize;
            linearLayout.addView(imageView, i2, i2);
            if (i != 0) {
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
                marginLayoutParams.setMarginStart(this.mIconMarginStart);
                imageView.setLayoutParams(marginLayoutParams);
            }
        }
        PrivacyChipDrawable privacyChipDrawable = this.mChipDrawable;
        if (privacyChipDrawable != null) {
            privacyChipDrawable.updateIcons(generateIcons.size());
        }
    }

    public final void updateStaticPrivacyIndicatorBounds() {
        int i;
        int i2;
        Resources resources = this.mContext.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(2131166825);
        int dimensionPixelSize2 = resources.getDimensionPixelSize(2131166821);
        int dimensionPixelSize3 = resources.getDimensionPixelSize(2131166824) * 2;
        Rect bounds = ((WindowManager) this.mContext.getSystemService(WindowManager.class)).getCurrentWindowMetrics().getBounds();
        Rect[] rectArr = this.mBounds;
        boolean z = this.mIsRtl;
        if (z) {
            i = bounds.left;
        } else {
            i = (bounds.right - dimensionPixelSize3) - dimensionPixelSize;
        }
        int i3 = bounds.top;
        if (z) {
            i2 = bounds.left + dimensionPixelSize3 + dimensionPixelSize;
        } else {
            i2 = bounds.right;
        }
        rectArr[0] = new Rect(i, i3, i2, dimensionPixelSize3 + i3 + dimensionPixelSize2);
        try {
            this.mIWindowManager.updateStaticPrivacyIndicatorBounds(this.mContext.getDisplayId(), this.mBounds);
        } catch (RemoteException unused) {
            Log.w("TvOngoingPrivacyChip", "could not update privacy indicator bounds");
        }
    }

    public TvOngoingPrivacyChip(Context context, PrivacyItemController privacyItemController, IWindowManager iWindowManager) {
        super(context);
        boolean z = false;
        this.mContext = context;
        this.mPrivacyItemController = privacyItemController;
        this.mIWindowManager = iWindowManager;
        Resources resources = context.getResources();
        this.mIconMarginStart = Math.round(resources.getDimension(2131166822));
        this.mIconSize = resources.getDimensionPixelSize(2131166823);
        this.mIsRtl = context.getResources().getConfiguration().getLayoutDirection() == 1 ? true : z;
        updateStaticPrivacyIndicatorBounds();
        this.mAnimationDurationMs = resources.getInteger(2131493026);
        Objects.requireNonNull(privacyItemController);
        this.mMicCameraIndicatorFlagEnabled = privacyItemController.micCameraAvailable;
        this.mAllIndicatorsEnabled = privacyItemController.allIndicatorsAvailable;
    }

    public static boolean listContainsPrivacyType(List list, PrivacyType privacyType) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            PrivacyItem privacyItem = (PrivacyItem) it.next();
            Objects.requireNonNull(privacyItem);
            if (privacyItem.privacyType == privacyType) {
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.CoreStartable
    public final void onConfigurationChanged(Configuration configuration) {
        boolean z = true;
        if (configuration.getLayoutDirection() != 1) {
            z = false;
        }
        if (this.mIsRtl != z) {
            this.mIsRtl = z;
            updateStaticPrivacyIndicatorBounds();
            if (this.mState != 0 && this.mIndicatorView != null) {
                fadeOutIndicator();
                createAndShowIndicator();
            }
        }
    }
}
