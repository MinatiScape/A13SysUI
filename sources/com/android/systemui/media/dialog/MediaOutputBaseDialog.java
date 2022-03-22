package com.android.systemui.media.dialog;

import android.app.WallpaperColors;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.session.MediaController;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.graphics.drawable.IconCompat;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import androidx.mediarouter.media.MediaRouter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.android.internal.graphics.cam.Cam;
import com.android.settingslib.media.LocalMediaManager;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.systemui.monet.ColorScheme;
import com.android.systemui.monet.CoreSpec;
import com.android.systemui.monet.Style;
import com.android.systemui.screenshot.ScreenshotView$$ExternalSyntheticLambda9;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import com.android.systemui.statusbar.phone.SystemUIDialogManager;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda2;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda3;
import com.android.wm.shell.pip.phone.PipMenuView$$ExternalSyntheticLambda4;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class MediaOutputBaseDialog extends SystemUIDialog implements MediaOutputController.Callback {
    public static final /* synthetic */ int $r8$clinit = 0;
    public MediaOutputAdapter mAdapter;
    public Button mAppButton;
    public ImageView mAppResourceIcon;
    public LinearLayout mCastAppLayout;
    public LinearLayout mDeviceListLayout;
    public RecyclerView mDevicesRecyclerView;
    public View mDialogView;
    public Button mDoneButton;
    public ImageView mHeaderIcon;
    public TextView mHeaderSubtitle;
    public TextView mHeaderTitle;
    public int mListMaxHeight;
    public final MediaOutputController mMediaOutputController;
    public Button mStopButton;
    public final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    public final MediaOutputBaseDialog$$ExternalSyntheticLambda0 mDeviceListLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() { // from class: com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda0
        @Override // android.view.ViewTreeObserver.OnGlobalLayoutListener
        public final void onGlobalLayout() {
            MediaOutputBaseDialog mediaOutputBaseDialog = MediaOutputBaseDialog.this;
            Objects.requireNonNull(mediaOutputBaseDialog);
            if (mediaOutputBaseDialog.mDeviceListLayout.getHeight() > mediaOutputBaseDialog.mListMaxHeight) {
                ViewGroup.LayoutParams layoutParams = mediaOutputBaseDialog.mDeviceListLayout.getLayoutParams();
                layoutParams.height = mediaOutputBaseDialog.mListMaxHeight;
                mediaOutputBaseDialog.mDeviceListLayout.setLayoutParams(layoutParams);
            }
        }
    };
    public final Context mContext = getContext();
    public final LinearLayoutManager mLayoutManager = new LinearLayoutManager(1);

    public abstract Drawable getAppSourceIcon();

    public abstract IconCompat getHeaderIcon();

    public abstract void getHeaderIconRes();

    public abstract int getHeaderIconSize();

    public abstract CharSequence getHeaderSubtitle();

    public abstract CharSequence getHeaderText();

    public abstract int getStopButtonVisibility();

    public void refresh() {
        refresh(false);
    }

    public final void refresh(boolean z) {
        boolean z2;
        CharSequence charSequence;
        getHeaderIconRes();
        IconCompat headerIcon = getHeaderIcon();
        Drawable appSourceIcon = getAppSourceIcon();
        LinearLayout linearLayout = this.mCastAppLayout;
        MediaOutputController mediaOutputController = this.mMediaOutputController;
        Objects.requireNonNull(mediaOutputController);
        MediaRouter.getInstance(mediaOutputController.mContext);
        MediaRouter.checkCallingThread();
        MediaRouter.getGlobalRouter();
        Log.d("MediaOutputController", "try to get routerParams: null");
        linearLayout.setVisibility(8);
        String str = null;
        ApplicationInfo applicationInfo = null;
        if (appSourceIcon != null) {
            this.mAppResourceIcon.setImageDrawable(appSourceIcon);
            Button button = this.mAppButton;
            int dimensionPixelSize = this.mContext.getResources().getDimensionPixelSize(2131166340);
            int intrinsicWidth = appSourceIcon.getIntrinsicWidth();
            int intrinsicHeight = appSourceIcon.getIntrinsicHeight();
            Bitmap createBitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, appSourceIcon.getOpacity() != -1 ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
            Canvas canvas = new Canvas(createBitmap);
            appSourceIcon.setBounds(0, 0, intrinsicWidth, intrinsicHeight);
            appSourceIcon.draw(canvas);
            button.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(this.mContext.getResources(), Bitmap.createScaledBitmap(createBitmap, dimensionPixelSize, dimensionPixelSize, false)), (Drawable) null, (Drawable) null, (Drawable) null);
        } else {
            this.mAppResourceIcon.setVisibility(8);
        }
        if (headerIcon != null) {
            Icon icon$1 = headerIcon.toIcon$1();
            Object[] objArr = (this.mContext.getResources().getConfiguration().uiMode & 48) == 32 ? 1 : null;
            WallpaperColors fromBitmap = WallpaperColors.fromBitmap(icon$1.getBitmap());
            z2 = true ^ fromBitmap.equals(null);
            if (z2) {
                MediaOutputAdapter mediaOutputAdapter = this.mAdapter;
                Objects.requireNonNull(mediaOutputAdapter);
                MediaOutputController mediaOutputController2 = mediaOutputAdapter.mController;
                Objects.requireNonNull(mediaOutputController2);
                List seedColors = ColorScheme.Companion.getSeedColors(fromBitmap);
                if (!seedColors.isEmpty()) {
                    int intValue = ((Number) seedColors.get(0)).intValue();
                    Style style = Style.TONAL_SPOT;
                    Cam fromInt = Cam.fromInt(intValue);
                    intValue = -14979341;
                    if (intValue == 0 || fromInt.getChroma() < 5.0f) {
                    }
                    Cam fromInt2 = Cam.fromInt(intValue);
                    CoreSpec coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet = style.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet();
                    Objects.requireNonNull(coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet);
                    ArrayList shades = coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet.a1.shades(fromInt2);
                    CoreSpec coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet2 = style.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet();
                    Objects.requireNonNull(coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet2);
                    ArrayList shades2 = coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet2.a2.shades(fromInt2);
                    CoreSpec coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet3 = style.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet();
                    Objects.requireNonNull(coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet3);
                    coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet3.a3.shades(fromInt2);
                    CoreSpec coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet4 = style.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet();
                    Objects.requireNonNull(coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet4);
                    ArrayList shades3 = coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet4.n1.shades(fromInt2);
                    CoreSpec coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet5 = style.getCoreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet();
                    Objects.requireNonNull(coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet5);
                    coreSpec$frameworks__base__packages__SystemUI__monet__android_common__monet5.n2.shades(fromInt2);
                    if (objArr != null) {
                        mediaOutputController2.mColorActiveItem = ((Integer) shades3.get(10)).intValue();
                        mediaOutputController2.mColorInactiveItem = ((Integer) shades3.get(10)).intValue();
                        mediaOutputController2.mColorSeekbarProgress = ((Integer) shades.get(2)).intValue();
                        mediaOutputController2.mColorButtonBackground = ((Integer) shades.get(2)).intValue();
                        mediaOutputController2.mColorItemBackground = ((Integer) shades2.get(0)).intValue();
                    } else {
                        mediaOutputController2.mColorActiveItem = ((Integer) shades3.get(10)).intValue();
                        mediaOutputController2.mColorInactiveItem = ((Integer) shades.get(7)).intValue();
                        mediaOutputController2.mColorSeekbarProgress = ((Integer) shades.get(3)).intValue();
                        mediaOutputController2.mColorButtonBackground = ((Integer) shades.get(3)).intValue();
                        mediaOutputController2.mColorItemBackground = ((Integer) shades2.get(0)).intValue();
                    }
                    MediaOutputAdapter mediaOutputAdapter2 = this.mAdapter;
                    Objects.requireNonNull(mediaOutputAdapter2);
                    MediaOutputController mediaOutputController3 = mediaOutputAdapter2.mController;
                    Objects.requireNonNull(mediaOutputController3);
                    PorterDuffColorFilter porterDuffColorFilter = new PorterDuffColorFilter(mediaOutputController3.mColorButtonBackground, PorterDuff.Mode.SRC_IN);
                    MediaOutputAdapter mediaOutputAdapter3 = this.mAdapter;
                    Objects.requireNonNull(mediaOutputAdapter3);
                    MediaOutputController mediaOutputController4 = mediaOutputAdapter3.mController;
                    Objects.requireNonNull(mediaOutputController4);
                    PorterDuffColorFilter porterDuffColorFilter2 = new PorterDuffColorFilter(mediaOutputController4.mColorInactiveItem, PorterDuff.Mode.SRC_IN);
                    this.mDoneButton.getBackground().setColorFilter(porterDuffColorFilter);
                    this.mStopButton.getBackground().setColorFilter(porterDuffColorFilter2);
                } else {
                    throw new NoSuchElementException("List is empty.");
                }
            }
            this.mHeaderIcon.setVisibility(0);
            this.mHeaderIcon.setImageIcon(icon$1);
        } else {
            this.mHeaderIcon.setVisibility(8);
            z2 = false;
        }
        if (this.mHeaderIcon.getVisibility() == 0) {
            int headerIconSize = getHeaderIconSize();
            this.mHeaderIcon.setLayoutParams(new LinearLayout.LayoutParams(this.mContext.getResources().getDimensionPixelSize(2131166343) + headerIconSize, headerIconSize));
        }
        Button button2 = this.mAppButton;
        MediaOutputController mediaOutputController5 = this.mMediaOutputController;
        Objects.requireNonNull(mediaOutputController5);
        if (!mediaOutputController5.mPackageName.isEmpty()) {
            PackageManager packageManager = mediaOutputController5.mContext.getPackageManager();
            try {
                applicationInfo = packageManager.getApplicationInfo(mediaOutputController5.mPackageName, PackageManager.ApplicationInfoFlags.of(0L));
            } catch (PackageManager.NameNotFoundException unused) {
            }
            if (applicationInfo != null) {
                charSequence = packageManager.getApplicationLabel(applicationInfo);
            } else {
                charSequence = mediaOutputController5.mContext.getString(2131952742);
            }
            str = (String) charSequence;
        }
        button2.setText(str);
        this.mHeaderTitle.setText(getHeaderText());
        CharSequence headerSubtitle = getHeaderSubtitle();
        if (TextUtils.isEmpty(headerSubtitle)) {
            this.mHeaderSubtitle.setVisibility(8);
            this.mHeaderTitle.setGravity(8388627);
        } else {
            this.mHeaderSubtitle.setVisibility(0);
            this.mHeaderSubtitle.setText(headerSubtitle);
            this.mHeaderTitle.setGravity(0);
        }
        MediaOutputAdapter mediaOutputAdapter4 = this.mAdapter;
        Objects.requireNonNull(mediaOutputAdapter4);
        if (!mediaOutputAdapter4.mIsDragging) {
            Objects.requireNonNull(this.mAdapter);
            MediaOutputAdapter mediaOutputAdapter5 = this.mAdapter;
            Objects.requireNonNull(mediaOutputAdapter5);
            int i = mediaOutputAdapter5.mCurrentActivePosition;
            if (z2 || z || i < 0 || i >= this.mAdapter.getItemCount()) {
                this.mAdapter.notifyDataSetChanged();
            } else {
                this.mAdapter.notifyItemChanged(i);
            }
        }
        this.mStopButton.setVisibility(getStopButtonVisibility());
    }

    /* JADX WARN: Type inference failed for: r5v2, types: [com.android.systemui.media.dialog.MediaOutputBaseDialog$$ExternalSyntheticLambda0] */
    public MediaOutputBaseDialog(Context context, MediaOutputController mediaOutputController, SystemUIDialogManager systemUIDialogManager) {
        super(context, 2132018188, true, systemUIDialogManager);
        this.mMediaOutputController = mediaOutputController;
        this.mListMaxHeight = context.getResources().getDimensionPixelSize(2131166346);
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.AlertDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mDialogView = LayoutInflater.from(this.mContext).inflate(2131624260, (ViewGroup) null);
        Window window = getWindow();
        WindowManager.LayoutParams attributes = window.getAttributes();
        attributes.gravity = 17;
        attributes.setFitInsetsTypes(WindowInsets.Type.statusBars() | WindowInsets.Type.navigationBars());
        attributes.setFitInsetsSides(WindowInsets.Side.all());
        attributes.setFitInsetsIgnoringVisibility(true);
        window.setAttributes(attributes);
        window.setContentView(this.mDialogView);
        window.setTitle(" ");
        this.mHeaderTitle = (TextView) this.mDialogView.requireViewById(2131428086);
        this.mHeaderSubtitle = (TextView) this.mDialogView.requireViewById(2131428084);
        this.mHeaderIcon = (ImageView) this.mDialogView.requireViewById(2131428082);
        this.mDevicesRecyclerView = (RecyclerView) this.mDialogView.requireViewById(2131428268);
        this.mDeviceListLayout = (LinearLayout) this.mDialogView.requireViewById(2131427835);
        this.mDoneButton = (Button) this.mDialogView.requireViewById(2131427865);
        this.mStopButton = (Button) this.mDialogView.requireViewById(2131428936);
        this.mAppButton = (Button) this.mDialogView.requireViewById(2131428206);
        this.mAppResourceIcon = (ImageView) this.mDialogView.requireViewById(2131427508);
        this.mCastAppLayout = (LinearLayout) this.mDialogView.requireViewById(2131427675);
        this.mDeviceListLayout.getViewTreeObserver().addOnGlobalLayoutListener(this.mDeviceListLayoutListener);
        this.mDevicesRecyclerView.setLayoutManager(this.mLayoutManager);
        this.mDevicesRecyclerView.setAdapter(this.mAdapter);
        this.mHeaderIcon.setOnClickListener(new ScreenshotView$$ExternalSyntheticLambda9(this, 1));
        this.mDoneButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda2(this, 1));
        this.mStopButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda3(this, 1));
        this.mAppButton.setOnClickListener(new PipMenuView$$ExternalSyntheticLambda4(this, 1));
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public final void onStart() {
        super.onStart();
        MediaOutputController mediaOutputController = this.mMediaOutputController;
        Objects.requireNonNull(mediaOutputController);
        mediaOutputController.mMediaDevices.clear();
        if (!TextUtils.isEmpty(mediaOutputController.mPackageName)) {
            Iterator<MediaController> it = mediaOutputController.mMediaSessionManager.getActiveSessions(null).iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                MediaController next = it.next();
                if (TextUtils.equals(next.getPackageName(), mediaOutputController.mPackageName)) {
                    mediaOutputController.mMediaController = next;
                    next.unregisterCallback(mediaOutputController.mCb);
                    mediaOutputController.mMediaController.registerCallback(mediaOutputController.mCb);
                    break;
                }
            }
        }
        if (mediaOutputController.mMediaController == null && MediaOutputController.DEBUG) {
            ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("No media controller for "), mediaOutputController.mPackageName, "MediaOutputController");
        }
        LocalMediaManager localMediaManager = mediaOutputController.mLocalMediaManager;
        if (localMediaManager != null) {
            mediaOutputController.mCallback = this;
            localMediaManager.mCallbacks.remove(mediaOutputController);
            mediaOutputController.mLocalMediaManager.stopScan();
            LocalMediaManager localMediaManager2 = mediaOutputController.mLocalMediaManager;
            Objects.requireNonNull(localMediaManager2);
            localMediaManager2.mCallbacks.add(mediaOutputController);
            mediaOutputController.mLocalMediaManager.startScan();
        } else if (MediaOutputController.DEBUG) {
            ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("No local media manager "), mediaOutputController.mPackageName, "MediaOutputController");
        }
    }

    @Override // com.android.systemui.statusbar.phone.SystemUIDialog, android.app.Dialog
    public final void onStop() {
        super.onStop();
        MediaOutputController mediaOutputController = this.mMediaOutputController;
        Objects.requireNonNull(mediaOutputController);
        MediaController mediaController = mediaOutputController.mMediaController;
        if (mediaController != null) {
            mediaController.unregisterCallback(mediaOutputController.mCb);
        }
        LocalMediaManager localMediaManager = mediaOutputController.mLocalMediaManager;
        if (localMediaManager != null) {
            localMediaManager.mCallbacks.remove(mediaOutputController);
            mediaOutputController.mLocalMediaManager.stopScan();
        }
        mediaOutputController.mMediaDevices.clear();
    }
}
