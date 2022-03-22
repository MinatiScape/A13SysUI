package com.android.keyguard;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.ServiceInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.CompositionSamplingListener;
import android.view.SurfaceControl;
import android.view.View;
import com.android.settingslib.drawable.CircleFramedDrawable;
import com.android.settingslib.users.EditUserPhotoController;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSSecurityFooter;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.customize.TileQueryHelper;
import com.android.systemui.qs.customize.TileQueryHelper$$ExternalSyntheticLambda0;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.shared.navigationbar.RegionSamplingHelper;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardPatternView$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ KeyguardPatternView$$ExternalSyntheticLambda0(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        QSTile.State state;
        String str;
        String str2 = null;
        switch (this.$r8$classId) {
            case 0:
                KeyguardPatternView keyguardPatternView = (KeyguardPatternView) this.f$0;
                Runnable runnable = (Runnable) this.f$1;
                int i = KeyguardPatternView.$r8$clinit;
                Objects.requireNonNull(keyguardPatternView);
                keyguardPatternView.enableClipping(true);
                if (runnable != null) {
                    runnable.run();
                    return;
                }
                return;
            case 1:
                EditUserPhotoController editUserPhotoController = (EditUserPhotoController) this.f$0;
                Bitmap bitmap = (Bitmap) this.f$1;
                Objects.requireNonNull(editUserPhotoController);
                if (bitmap != null) {
                    editUserPhotoController.mNewUserPhotoBitmap = bitmap;
                    Context context = editUserPhotoController.mImageView.getContext();
                    Bitmap bitmap2 = editUserPhotoController.mNewUserPhotoBitmap;
                    int i2 = CircleFramedDrawable.$r8$clinit;
                    CircleFramedDrawable circleFramedDrawable = new CircleFramedDrawable(bitmap2, context.getResources().getDimensionPixelSize(17105621));
                    editUserPhotoController.mNewUserPhotoDrawable = circleFramedDrawable;
                    editUserPhotoController.mImageView.setImageDrawable(circleFramedDrawable);
                    return;
                }
                return;
            case 2:
                QSSecurityFooter qSSecurityFooter = (QSSecurityFooter) this.f$0;
                View view = (View) this.f$1;
                int i3 = QSSecurityFooter.$r8$clinit;
                Objects.requireNonNull(qSSecurityFooter);
                SystemUIDialog systemUIDialog = new SystemUIDialog(qSSecurityFooter.mContext, 0);
                qSSecurityFooter.mDialog = systemUIDialog;
                systemUIDialog.requestWindowFeature(1);
                qSSecurityFooter.mDialog.setButton(-1, qSSecurityFooter.mContext.getString(2131952934), qSSecurityFooter);
                SystemUIDialog systemUIDialog2 = qSSecurityFooter.mDialog;
                if (qSSecurityFooter.mShouldUseSettingsButton.get()) {
                    str2 = qSSecurityFooter.getSettingsButton();
                } else if (qSSecurityFooter.mSecurityController.isParentalControlsEnabled()) {
                    str2 = qSSecurityFooter.mContext.getString(2131952778);
                }
                systemUIDialog2.setButton(-2, str2, qSSecurityFooter);
                qSSecurityFooter.mDialog.setView(view);
                DialogLaunchAnimator dialogLaunchAnimator = qSSecurityFooter.mDialogLaunchAnimator;
                SystemUIDialog systemUIDialog3 = qSSecurityFooter.mDialog;
                View view2 = qSSecurityFooter.mRootView;
                Objects.requireNonNull(dialogLaunchAnimator);
                dialogLaunchAnimator.showFromView(systemUIDialog3, view2, false);
                return;
            case 3:
                TileQueryHelper tileQueryHelper = (TileQueryHelper) this.f$0;
                QSTileHost qSTileHost = (QSTileHost) this.f$1;
                Objects.requireNonNull(tileQueryHelper);
                Objects.requireNonNull(qSTileHost);
                Collection<QSTile> values = qSTileHost.mTiles.values();
                PackageManager packageManager = tileQueryHelper.mContext.getPackageManager();
                List<ResolveInfo> queryIntentServicesAsUser = packageManager.queryIntentServicesAsUser(new Intent("android.service.quicksettings.action.QS_TILE"), 0, tileQueryHelper.mUserTracker.getUserId());
                String string = tileQueryHelper.mContext.getString(2131953141);
                for (ResolveInfo resolveInfo : queryIntentServicesAsUser) {
                    ComponentName componentName = new ComponentName(resolveInfo.serviceInfo.packageName, resolveInfo.serviceInfo.name);
                    if (!string.contains(componentName.flattenToString())) {
                        CharSequence loadLabel = resolveInfo.serviceInfo.applicationInfo.loadLabel(packageManager);
                        String spec = CustomTile.toSpec(componentName);
                        Iterator<QSTile> it = values.iterator();
                        while (true) {
                            if (it.hasNext()) {
                                QSTile next = it.next();
                                if (spec.equals(next.getTileSpec())) {
                                    state = next.getState().copy();
                                }
                            } else {
                                state = null;
                            }
                        }
                        if (state != null) {
                            tileQueryHelper.addTile(spec, loadLabel, state, false);
                        } else {
                            ServiceInfo serviceInfo = resolveInfo.serviceInfo;
                            if (serviceInfo.icon != 0 || serviceInfo.applicationInfo.icon != 0) {
                                Drawable loadIcon = serviceInfo.loadIcon(packageManager);
                                if ("android.permission.BIND_QUICK_SETTINGS_TILE".equals(resolveInfo.serviceInfo.permission) && loadIcon != null) {
                                    loadIcon.mutate();
                                    loadIcon.setTint(tileQueryHelper.mContext.getColor(17170443));
                                    CharSequence loadLabel2 = resolveInfo.serviceInfo.loadLabel(packageManager);
                                    if (loadLabel2 != null) {
                                        str = loadLabel2.toString();
                                    } else {
                                        str = "null";
                                    }
                                    QSTile.State state2 = new QSTile.State();
                                    state2.state = 1;
                                    state2.label = str;
                                    state2.contentDescription = str;
                                    state2.icon = new QSTileImpl.DrawableIcon(loadIcon);
                                    tileQueryHelper.addTile(spec, loadLabel, state2, false);
                                }
                            }
                        }
                    }
                }
                tileQueryHelper.mMainExecutor.execute(new TileQueryHelper$$ExternalSyntheticLambda0(tileQueryHelper, new ArrayList(tileQueryHelper.mTiles), true));
                return;
            default:
                RegionSamplingHelper regionSamplingHelper = (RegionSamplingHelper) this.f$0;
                SurfaceControl surfaceControl = (SurfaceControl) this.f$1;
                Objects.requireNonNull(regionSamplingHelper);
                RegionSamplingHelper.SysuiCompositionSamplingListener sysuiCompositionSamplingListener = regionSamplingHelper.mCompositionSamplingListener;
                RegionSamplingHelper.AnonymousClass3 r0 = regionSamplingHelper.mSamplingListener;
                Objects.requireNonNull(sysuiCompositionSamplingListener);
                CompositionSamplingListener.unregister(r0);
                if (surfaceControl != null && surfaceControl.isValid()) {
                    surfaceControl.release();
                    return;
                }
                return;
        }
    }
}
