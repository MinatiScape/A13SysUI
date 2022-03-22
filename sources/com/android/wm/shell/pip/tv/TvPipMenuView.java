package com.android.wm.shell.pip.tv;

import android.app.PendingIntent;
import android.app.RemoteAction;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Handler;
import android.os.IBinder;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.PathInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class TvPipMenuView extends FrameLayout implements View.OnClickListener {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final LinearLayout mActionButtonsContainer;
    public Rect mCurrentBounds;
    public final TvPipMenuActionButton mExpandButton;
    public Listener mListener;
    public final ArrayList mAdditionalButtons = new ArrayList();
    public IBinder mFocusGrantToken = null;
    public final ViewGroup mScrollView = (ViewGroup) findViewById(2131429131);
    public final ViewGroup mHorizontalScrollView = (ViewGroup) findViewById(2131429129);
    public final View mMenuFrameView = findViewById(2131429127);
    public final ImageView mArrowUp = (ImageView) findViewById(2131429124);
    public final ImageView mArrowRight = (ImageView) findViewById(2131429123);
    public final ImageView mArrowDown = (ImageView) findViewById(2131429121);
    public final ImageView mArrowLeft = (ImageView) findViewById(2131429122);

    /* loaded from: classes.dex */
    public interface Listener {
    }

    public TvPipMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0, 0);
        View.inflate(context, 2131624633, this);
        LinearLayout linearLayout = (LinearLayout) findViewById(2131429120);
        this.mActionButtonsContainer = linearLayout;
        linearLayout.findViewById(2131429128).setOnClickListener(this);
        linearLayout.findViewById(2131429125).setOnClickListener(this);
        linearLayout.findViewById(2131429130).setOnClickListener(this);
        TvPipMenuActionButton tvPipMenuActionButton = (TvPipMenuActionButton) findViewById(2131429126);
        this.mExpandButton = tvPipMenuActionButton;
        tvPipMenuActionButton.setOnClickListener(this);
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0052, code lost:
        if (r2 == 20) goto L_0x00a2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x005d, code lost:
        if (r2 == 21) goto L_0x00a2;
     */
    /* JADX WARN: Removed duplicated region for block: B:48:0x00a5  */
    @Override // android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean dispatchKeyEvent(android.view.KeyEvent r9) {
        /*
            Method dump skipped, instructions count: 360
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.pip.tv.TvPipMenuView.dispatchKeyEvent(android.view.KeyEvent):boolean");
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        if (this.mListener != null) {
            int id = view.getId();
            if (id == 2131429128) {
                TvPipMenuController tvPipMenuController = (TvPipMenuController) this.mListener;
                Objects.requireNonNull(tvPipMenuController);
                TvPipController tvPipController = (TvPipController) tvPipMenuController.mDelegate;
                Objects.requireNonNull(tvPipController);
                tvPipController.mPipTaskOrganizer.exitPip(tvPipController.mResizeAnimationDuration, false);
                tvPipController.onPipDisappeared();
            } else if (id == 2131429130) {
                TvPipMenuController tvPipMenuController2 = (TvPipMenuController) this.mListener;
                Objects.requireNonNull(tvPipMenuController2);
                tvPipMenuController2.mInMoveMode = true;
                TvPipMenuView tvPipMenuView = tvPipMenuController2.mPipMenuView;
                Objects.requireNonNull(tvPipMenuView);
                animateAlphaTo(0.0f, tvPipMenuView.mActionButtonsContainer);
                TvPipMenuView tvPipMenuView2 = tvPipMenuController2.mPipMenuView;
                TvPipController tvPipController2 = (TvPipController) tvPipMenuController2.mDelegate;
                Objects.requireNonNull(tvPipController2);
                TvPipBoundsState tvPipBoundsState = tvPipController2.mTvPipBoundsState;
                Objects.requireNonNull(tvPipBoundsState);
                tvPipMenuView2.showMovementHints(tvPipBoundsState.mTvPipGravity);
            } else if (id == 2131429125) {
                TvPipMenuController tvPipMenuController3 = (TvPipMenuController) this.mListener;
                Objects.requireNonNull(tvPipMenuController3);
                ((TvPipController) tvPipMenuController3.mDelegate).closePip();
            } else if (id == 2131429126) {
                TvPipMenuController tvPipMenuController4 = (TvPipMenuController) this.mListener;
                Objects.requireNonNull(tvPipMenuController4);
                TvPipController tvPipController3 = (TvPipController) tvPipMenuController4.mDelegate;
                Objects.requireNonNull(tvPipController3);
                TvPipBoundsState tvPipBoundsState2 = tvPipController3.mTvPipBoundsState;
                Objects.requireNonNull(tvPipBoundsState2);
                boolean z = !tvPipBoundsState2.mIsTvPipExpanded;
                int updatePositionOnExpandToggled = tvPipController3.mTvPipBoundsAlgorithm.updatePositionOnExpandToggled(tvPipController3.mPreviousGravity, z);
                if (updatePositionOnExpandToggled != 0) {
                    tvPipController3.mPreviousGravity = updatePositionOnExpandToggled;
                }
                TvPipBoundsState tvPipBoundsState3 = tvPipController3.mTvPipBoundsState;
                Objects.requireNonNull(tvPipBoundsState3);
                tvPipBoundsState3.mTvPipManuallyCollapsed = !z;
                TvPipBoundsState tvPipBoundsState4 = tvPipController3.mTvPipBoundsState;
                Objects.requireNonNull(tvPipBoundsState4);
                tvPipBoundsState4.mIsTvPipExpanded = z;
                tvPipController3.movePinnedStack();
            } else {
                RemoteAction remoteAction = (RemoteAction) view.getTag();
                if (remoteAction != null) {
                    try {
                        remoteAction.getActionIntent().send();
                    } catch (PendingIntent.CanceledException e) {
                        Log.w("TvPipMenuView", "Failed to send action", e);
                    }
                } else {
                    Log.w("TvPipMenuView", "RemoteAction is null");
                }
            }
        }
    }

    public final void showMovementHints(int i) {
        boolean z;
        float f;
        boolean z2;
        float f2;
        boolean z3;
        float f3;
        boolean z4 = true;
        if ((i & 80) == 80) {
            z = true;
        } else {
            z = false;
        }
        float f4 = 1.0f;
        if (z) {
            f = 1.0f;
        } else {
            f = 0.0f;
        }
        animateAlphaTo(f, this.mArrowUp);
        if ((i & 48) == 48) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            f2 = 1.0f;
        } else {
            f2 = 0.0f;
        }
        animateAlphaTo(f2, this.mArrowDown);
        if ((i & 5) == 5) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (z3) {
            f3 = 1.0f;
        } else {
            f3 = 0.0f;
        }
        animateAlphaTo(f3, this.mArrowLeft);
        if ((i & 3) != 3) {
            z4 = false;
        }
        if (!z4) {
            f4 = 0.0f;
        }
        animateAlphaTo(f4, this.mArrowRight);
    }

    public static void animateAlphaTo(final float f, final View view) {
        PathInterpolator pathInterpolator;
        ViewPropertyAnimator alpha = view.animate().alpha(f);
        if (f == 0.0f) {
            pathInterpolator = TvPipInterpolators.EXIT;
        } else {
            pathInterpolator = TvPipInterpolators.ENTER;
        }
        alpha.setInterpolator(pathInterpolator).setDuration(500L).withStartAction(new Runnable() { // from class: com.android.wm.shell.pip.tv.TvPipMenuView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                float f2 = f;
                View view2 = view;
                int i = TvPipMenuView.$r8$clinit;
                if (f2 != 0.0f) {
                    view2.setVisibility(0);
                }
            }
        }).withEndAction(new Runnable() { // from class: com.android.wm.shell.pip.tv.TvPipMenuView$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                float f2 = f;
                View view2 = view;
                int i = TvPipMenuView.$r8$clinit;
                if (f2 == 0.0f) {
                    view2.setVisibility(8);
                }
            }
        });
    }

    public final void setAdditionalActions(List<RemoteAction> list, Handler handler) {
        LinearLayout linearLayout;
        int size = list.size();
        int size2 = this.mAdditionalButtons.size();
        if (size > size2) {
            while (size > size2) {
                TvPipMenuActionButton tvPipMenuActionButton = new TvPipMenuActionButton(((FrameLayout) this).mContext);
                tvPipMenuActionButton.setOnClickListener(this);
                this.mActionButtonsContainer.addView(tvPipMenuActionButton, linearLayout.getChildCount() - 1);
                this.mAdditionalButtons.add(tvPipMenuActionButton);
                size2++;
            }
        } else if (size < size2) {
            while (size < size2) {
                size2--;
                View view = (View) this.mAdditionalButtons.get(size2);
                view.setVisibility(8);
                view.setTag(null);
            }
        }
        for (int i = 0; i < size; i++) {
            RemoteAction remoteAction = list.get(i);
            final TvPipMenuActionButton tvPipMenuActionButton2 = (TvPipMenuActionButton) this.mAdditionalButtons.get(i);
            tvPipMenuActionButton2.setVisibility(0);
            tvPipMenuActionButton2.mButtonView.setContentDescription(remoteAction.getContentDescription());
            tvPipMenuActionButton2.setEnabled(remoteAction.isEnabled());
            tvPipMenuActionButton2.setTag(remoteAction);
            remoteAction.getIcon().loadDrawableAsync(((FrameLayout) this).mContext, new Icon.OnDrawableLoadedListener() { // from class: com.android.wm.shell.pip.tv.TvPipMenuView$$ExternalSyntheticLambda0
                @Override // android.graphics.drawable.Icon.OnDrawableLoadedListener
                public final void onDrawableLoaded(Drawable drawable) {
                    TvPipMenuActionButton tvPipMenuActionButton3 = TvPipMenuActionButton.this;
                    Objects.requireNonNull(tvPipMenuActionButton3);
                    tvPipMenuActionButton3.mIconImageView.setImageDrawable(drawable);
                }
            }, handler);
        }
    }
}
