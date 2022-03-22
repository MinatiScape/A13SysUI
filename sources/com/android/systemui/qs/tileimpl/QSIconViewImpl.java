package com.android.systemui.qs.tileimpl;

import android.content.Context;
import android.content.res.Configuration;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.settingslib.Utils;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
/* loaded from: classes.dex */
public class QSIconViewImpl extends QSIconView {
    public final View mIcon;
    public int mIconSizePx;
    public QSTile.Icon mLastIcon;
    public int mTint;
    public boolean mAnimationEnabled = true;
    public int mState = -1;

    @Override // com.android.systemui.plugins.qs.QSIconView
    public final void disableAnimation() {
        this.mAnimationEnabled = false;
    }

    public int getIconMeasureMode() {
        return 1073741824;
    }

    @Override // com.android.systemui.plugins.qs.QSIconView
    public void setIcon(QSTile.State state, boolean z) {
        setIcon((ImageView) this.mIcon, state, z);
    }

    public View createIcon() {
        SlashImageView slashImageView = new SlashImageView(((ViewGroup) this).mContext);
        slashImageView.setId(16908294);
        slashImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        return slashImageView;
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda1] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setIcon(final android.widget.ImageView r11, final com.android.systemui.plugins.qs.QSTile.State r12, final boolean r13) {
        /*
            r10 = this;
            boolean r0 = r12.disabledByPolicy
            if (r0 == 0) goto L_0x0013
            android.content.Context r0 = r10.getContext()
            r1 = 2131100510(0x7f06035e, float:1.7813403E38)
            int r0 = r0.getColor(r1)
            r11.setColorFilter(r0)
            goto L_0x0016
        L_0x0013:
            r11.clearColorFilter()
        L_0x0016:
            int r0 = r12.state
            int r1 = r10.mState
            if (r0 == r1) goto L_0x00dc
            android.content.Context r1 = r10.getContext()
            int r0 = getIconColorForState(r1, r0)
            int r1 = r12.state
            r10.mState = r1
            int r1 = r10.mTint
            if (r1 == 0) goto L_0x00b4
            if (r13 == 0) goto L_0x00b4
            boolean r1 = r10.mAnimationEnabled
            if (r1 == 0) goto L_0x0040
            boolean r1 = r11.isShown()
            if (r1 == 0) goto L_0x0040
            android.graphics.drawable.Drawable r1 = r11.getDrawable()
            if (r1 == 0) goto L_0x0040
            r1 = 1
            goto L_0x0041
        L_0x0040:
            r1 = 0
        L_0x0041:
            if (r1 == 0) goto L_0x00b4
            int r1 = r10.mTint
            com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda1 r2 = new com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda1
            r2.<init>()
            boolean r12 = r11 instanceof com.android.systemui.qs.AlphaControlledSignalTileView.AlphaControlledSlashImageView
            if (r12 == 0) goto L_0x0064
            r12 = r11
            com.android.systemui.qs.AlphaControlledSignalTileView$AlphaControlledSlashImageView r12 = (com.android.systemui.qs.AlphaControlledSignalTileView.AlphaControlledSlashImageView) r12
            android.content.res.ColorStateList r13 = android.content.res.ColorStateList.valueOf(r0)
            java.util.Objects.requireNonNull(r12)
            r12.setImageTintList(r13)
            com.android.systemui.qs.SlashDrawable r12 = r12.mSlash
            if (r12 == 0) goto L_0x0064
            com.android.systemui.qs.AlphaControlledSignalTileView$AlphaControlledSlashDrawable r12 = (com.android.systemui.qs.AlphaControlledSignalTileView.AlphaControlledSlashDrawable) r12
            r12.setFinalTintList(r13)
        L_0x0064:
            boolean r12 = r10.mAnimationEnabled
            if (r12 == 0) goto L_0x00a7
            boolean r12 = android.animation.ValueAnimator.areAnimatorsEnabled()
            if (r12 == 0) goto L_0x00a7
            int r12 = android.graphics.Color.alpha(r1)
            float r4 = (float) r12
            int r12 = android.graphics.Color.alpha(r0)
            float r5 = (float) r12
            int r12 = android.graphics.Color.red(r1)
            float r6 = (float) r12
            int r12 = android.graphics.Color.red(r0)
            float r7 = (float) r12
            r12 = 2
            float[] r12 = new float[r12]
            r12 = {x00e0: FILL_ARRAY_DATA  , data: [0, 1065353216} // fill-array
            android.animation.ValueAnimator r12 = android.animation.ValueAnimator.ofFloat(r12)
            r8 = 350(0x15e, double:1.73E-321)
            r12.setDuration(r8)
            com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda0 r13 = new com.android.systemui.qs.tileimpl.QSIconViewImpl$$ExternalSyntheticLambda0
            r3 = r13
            r8 = r11
            r3.<init>()
            r12.addUpdateListener(r13)
            com.android.systemui.qs.tileimpl.QSIconViewImpl$2 r11 = new com.android.systemui.qs.tileimpl.QSIconViewImpl$2
            r11.<init>()
            r12.addListener(r11)
            r12.start()
            goto L_0x00b1
        L_0x00a7:
            android.content.res.ColorStateList r12 = android.content.res.ColorStateList.valueOf(r0)
            r11.setImageTintList(r12)
            r2.run()
        L_0x00b1:
            r10.mTint = r0
            goto L_0x00df
        L_0x00b4:
            boolean r1 = r11 instanceof com.android.systemui.qs.AlphaControlledSignalTileView.AlphaControlledSlashImageView
            if (r1 == 0) goto L_0x00cf
            r1 = r11
            com.android.systemui.qs.AlphaControlledSignalTileView$AlphaControlledSlashImageView r1 = (com.android.systemui.qs.AlphaControlledSignalTileView.AlphaControlledSlashImageView) r1
            android.content.res.ColorStateList r2 = android.content.res.ColorStateList.valueOf(r0)
            java.util.Objects.requireNonNull(r1)
            r1.setImageTintList(r2)
            com.android.systemui.qs.SlashDrawable r1 = r1.mSlash
            if (r1 == 0) goto L_0x00d6
            com.android.systemui.qs.AlphaControlledSignalTileView$AlphaControlledSlashDrawable r1 = (com.android.systemui.qs.AlphaControlledSignalTileView.AlphaControlledSlashDrawable) r1
            r1.setFinalTintList(r2)
            goto L_0x00d6
        L_0x00cf:
            android.content.res.ColorStateList r1 = android.content.res.ColorStateList.valueOf(r0)
            r11.setImageTintList(r1)
        L_0x00d6:
            r10.mTint = r0
            r10.updateIcon(r11, r12, r13)
            goto L_0x00df
        L_0x00dc:
            r10.updateIcon(r11, r12, r13)
        L_0x00df:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tileimpl.QSIconViewImpl.setIcon(android.widget.ImageView, com.android.systemui.plugins.qs.QSTile$State, boolean):void");
    }

    @Override // android.view.View
    public final String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append('[');
        sb.append("state=" + this.mState);
        sb.append(", tint=" + this.mTint);
        if (this.mLastIcon != null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(", lastIcon=");
            m.append(this.mLastIcon.toString());
            sb.append(m.toString());
        }
        sb.append("]");
        return sb.toString();
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0040, code lost:
        if (r11 != false) goto L_0x0044;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateIcon(android.widget.ImageView r9, com.android.systemui.plugins.qs.QSTile.State r10, boolean r11) {
        /*
            r8 = this;
            java.util.function.Supplier<com.android.systemui.plugins.qs.QSTile$Icon> r0 = r10.iconSupplier
            if (r0 == 0) goto L_0x000b
            java.lang.Object r0 = r0.get()
            com.android.systemui.plugins.qs.QSTile$Icon r0 = (com.android.systemui.plugins.qs.QSTile.Icon) r0
            goto L_0x000d
        L_0x000b:
            com.android.systemui.plugins.qs.QSTile$Icon r0 = r10.icon
        L_0x000d:
            r1 = 2131428654(0x7f0b052e, float:1.8478959E38)
            java.lang.Object r2 = r9.getTag(r1)
            boolean r2 = java.util.Objects.equals(r0, r2)
            r3 = 2131428658(0x7f0b0532, float:1.8478967E38)
            if (r2 == 0) goto L_0x0029
            com.android.systemui.plugins.qs.QSTile$SlashState r2 = r10.slash
            java.lang.Object r4 = r9.getTag(r3)
            boolean r2 = java.util.Objects.equals(r2, r4)
            if (r2 != 0) goto L_0x00ae
        L_0x0029:
            r2 = 1
            r4 = 0
            if (r11 == 0) goto L_0x0043
            boolean r11 = r8.mAnimationEnabled
            if (r11 == 0) goto L_0x003f
            boolean r11 = r9.isShown()
            if (r11 == 0) goto L_0x003f
            android.graphics.drawable.Drawable r11 = r9.getDrawable()
            if (r11 == 0) goto L_0x003f
            r11 = r2
            goto L_0x0040
        L_0x003f:
            r11 = r4
        L_0x0040:
            if (r11 == 0) goto L_0x0043
            goto L_0x0044
        L_0x0043:
            r2 = r4
        L_0x0044:
            r8.mLastIcon = r0
            r11 = 0
            if (r0 == 0) goto L_0x0059
            if (r2 == 0) goto L_0x0052
            android.content.Context r5 = r8.mContext
            android.graphics.drawable.Drawable r5 = r0.getDrawable(r5)
            goto L_0x005a
        L_0x0052:
            android.content.Context r5 = r8.mContext
            android.graphics.drawable.Drawable r5 = r0.getInvisibleDrawable(r5)
            goto L_0x005a
        L_0x0059:
            r5 = r11
        L_0x005a:
            if (r0 == 0) goto L_0x0061
            int r6 = r0.getPadding()
            goto L_0x0062
        L_0x0061:
            r6 = r4
        L_0x0062:
            if (r5 == 0) goto L_0x007c
            android.graphics.drawable.Drawable$ConstantState r7 = r5.getConstantState()
            if (r7 == 0) goto L_0x0072
            android.graphics.drawable.Drawable$ConstantState r5 = r5.getConstantState()
            android.graphics.drawable.Drawable r5 = r5.newDrawable()
        L_0x0072:
            r5.setAutoMirrored(r4)
            int r8 = r8.getLayoutDirection()
            r5.setLayoutDirection(r8)
        L_0x007c:
            boolean r8 = r9 instanceof com.android.systemui.qs.tileimpl.SlashImageView
            if (r8 == 0) goto L_0x008b
            r8 = r9
            com.android.systemui.qs.tileimpl.SlashImageView r8 = (com.android.systemui.qs.tileimpl.SlashImageView) r8
            r8.mAnimationEnabled = r2
            r8.mSlash = r11
            r8.setImageDrawable(r5)
            goto L_0x008e
        L_0x008b:
            r9.setImageDrawable(r5)
        L_0x008e:
            r9.setTag(r1, r0)
            com.android.systemui.plugins.qs.QSTile$SlashState r8 = r10.slash
            r9.setTag(r3, r8)
            r9.setPadding(r4, r6, r4, r6)
            boolean r8 = r5 instanceof android.graphics.drawable.Animatable2
            if (r8 == 0) goto L_0x00ae
            android.graphics.drawable.Animatable2 r5 = (android.graphics.drawable.Animatable2) r5
            r5.start()
            boolean r8 = r10.isTransient
            if (r8 == 0) goto L_0x00ae
            com.android.systemui.qs.tileimpl.QSIconViewImpl$1 r8 = new com.android.systemui.qs.tileimpl.QSIconViewImpl$1
            r8.<init>()
            r5.registerAnimationCallback(r8)
        L_0x00ae:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.tileimpl.QSIconViewImpl.updateIcon(android.widget.ImageView, com.android.systemui.plugins.qs.QSTile$State, boolean):void");
    }

    public QSIconViewImpl(Context context) {
        super(context);
        this.mIconSizePx = context.getResources().getDimensionPixelSize(2131166873);
        View createIcon = createIcon();
        this.mIcon = createIcon;
        addView(createIcon);
    }

    public static int getIconColorForState(Context context, int i) {
        if (i == 0) {
            return Utils.applyAlpha(Utils.getColorAttrDefaultColor(context, 16842806));
        }
        if (i == 1) {
            return Utils.getColorAttrDefaultColor(context, 16842806);
        }
        if (i == 2) {
            return Utils.getColorAttrDefaultColor(context, 17957103);
        }
        KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Invalid state ", i, "QSIconView");
        return 0;
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mIconSizePx = getContext().getResources().getDimensionPixelSize(2131166873);
    }

    @Override // android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = (getMeasuredWidth() - this.mIcon.getMeasuredWidth()) / 2;
        View view = this.mIcon;
        view.layout(measuredWidth, 0, view.getMeasuredWidth() + measuredWidth, view.getMeasuredHeight() + 0);
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int size = View.MeasureSpec.getSize(i);
        this.mIcon.measure(View.MeasureSpec.makeMeasureSpec(size, getIconMeasureMode()), View.MeasureSpec.makeMeasureSpec(this.mIconSizePx, 1073741824));
        setMeasuredDimension(size, this.mIcon.getMeasuredHeight());
    }

    @Override // com.android.systemui.plugins.qs.QSIconView
    public final View getIconView() {
        return this.mIcon;
    }
}
