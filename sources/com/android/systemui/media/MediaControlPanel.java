package com.android.systemui.media;

import android.app.PendingIntent;
import android.app.WallpaperColors;
import android.app.smartspace.SmartspaceAction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintSet;
import com.android.keyguard.KeyguardPINView$$ExternalSyntheticLambda0;
import com.android.settingslib.Utils;
import com.android.settingslib.widget.AdaptiveIcon;
import com.android.systemui.animation.GhostedViewLaunchAnimatorController;
import com.android.systemui.flags.Flags;
import com.android.systemui.media.MediaViewController;
import com.android.systemui.media.SeekBarViewModel;
import com.android.systemui.media.dialog.MediaOutputDialogFactory;
import com.android.systemui.monet.ColorScheme;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.util.animation.TransitionLayout;
import com.android.systemui.util.time.SystemClock;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
/* loaded from: classes.dex */
public final class MediaControlPanel {
    public final ActivityStarter mActivityStarter;
    public int mAlbumArtSize;
    public int mBackgroundColor;
    public final Executor mBackgroundExecutor;
    public Context mContext;
    public MediaController mController;
    public int mDevicePadding;
    public final FalsingManager mFalsingManager;
    public String mKey;
    public MediaCarouselController mMediaCarouselController;
    public Lazy<MediaDataManager> mMediaDataManagerLazy;
    public final MediaFlags mMediaFlags;
    public final MediaOutputDialogFactory mMediaOutputDialogFactory;
    public MediaViewController mMediaViewController;
    public MediaViewHolder mMediaViewHolder;
    public RecommendationViewHolder mRecommendationViewHolder;
    public SeekBarObserver mSeekBarObserver;
    public final SeekBarViewModel mSeekBarViewModel;
    public int mSmartspaceMediaItemsCount;
    public SystemClock mSystemClock;
    public MediaSession.Token mToken;
    public static final Intent SETTINGS_INTENT = new Intent("android.settings.ACTION_MEDIA_CONTROLS_SETTINGS");
    public static final int[] ACTION_IDS = {2131427401, 2131427402, 2131427403, 2131427404, 2131427405};
    public int mInstanceId = -1;
    public int mUid = -1;
    public boolean mIsImpressed = false;

    public static void setVisibleAndAlpha(ConstraintSet constraintSet, int i, boolean z) {
        int i2;
        float f;
        if (z) {
            i2 = 0;
        } else {
            i2 = 8;
        }
        Objects.requireNonNull(constraintSet);
        constraintSet.get(i).propertySet.visibility = i2;
        if (z) {
            f = 1.0f;
        } else {
            f = 0.0f;
        }
        constraintSet.get(i).propertySet.alpha = f;
    }

    public final void attachPlayer(MediaViewHolder mediaViewHolder, MediaViewController.TYPE type) {
        boolean z;
        this.mMediaViewHolder = mediaViewHolder;
        TransitionLayout transitionLayout = mediaViewHolder.player;
        if (type == MediaViewController.TYPE.PLAYER_SESSION) {
            z = true;
        } else {
            z = false;
        }
        this.mSeekBarObserver = new SeekBarObserver(mediaViewHolder, z);
        SeekBarViewModel seekBarViewModel = this.mSeekBarViewModel;
        Objects.requireNonNull(seekBarViewModel);
        seekBarViewModel._progress.observeForever(this.mSeekBarObserver);
        SeekBarViewModel seekBarViewModel2 = this.mSeekBarViewModel;
        SeekBar seekBar = mediaViewHolder.seekBar;
        Objects.requireNonNull(seekBarViewModel2);
        seekBar.setOnSeekBarChangeListener(new SeekBarViewModel.SeekBarChangeListener(seekBarViewModel2));
        seekBar.setOnTouchListener(new SeekBarViewModel.SeekBarTouchListener(seekBarViewModel2, seekBar));
        this.mMediaViewController.attach(transitionLayout, type);
        mediaViewHolder.player.setOnLongClickListener(new View.OnLongClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda11
            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                MediaControlPanel mediaControlPanel = MediaControlPanel.this;
                Objects.requireNonNull(mediaControlPanel);
                MediaViewController mediaViewController = mediaControlPanel.mMediaViewController;
                Objects.requireNonNull(mediaViewController);
                if (!mediaViewController.isGutsVisible) {
                    mediaControlPanel.openGuts();
                    return true;
                }
                mediaControlPanel.closeGuts(false);
                return true;
            }
        });
        mediaViewHolder.cancel.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda3(this, 0));
        mediaViewHolder.settings.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda0(this, 0));
    }

    public final void bindPlayer(final MediaData mediaData, String str) {
        int i;
        boolean z;
        float f;
        boolean z2;
        ColorScheme colorScheme;
        Drawable drawable;
        int i2;
        int[] iArr;
        boolean z3;
        Drawable drawable2;
        Drawable drawable3;
        Drawable drawable4;
        Rect rect;
        if (this.mMediaViewHolder != null) {
            this.mKey = str;
            Objects.requireNonNull(mediaData);
            MediaSession.Token token = mediaData.token;
            try {
                this.mUid = this.mContext.getPackageManager().getApplicationInfo(mediaData.packageName, 0).uid;
            } catch (PackageManager.NameNotFoundException e) {
                Log.e("MediaControlPanel", "Unable to look up package name", e);
            }
            if (this.mInstanceId == -1) {
                this.mInstanceId = Math.abs(Math.floorMod(this.mUid + ((int) this.mSystemClock.currentTimeMillis()), 8192));
            }
            this.mBackgroundColor = mediaData.backgroundColor;
            MediaSession.Token token2 = this.mToken;
            if (token2 == null || !token2.equals(token)) {
                this.mToken = token;
            }
            if (this.mToken != null) {
                this.mController = new MediaController(this.mContext, this.mToken);
            } else {
                this.mController = null;
            }
            final PendingIntent pendingIntent = mediaData.clickIntent;
            if (pendingIntent != null) {
                MediaViewHolder mediaViewHolder = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder);
                mediaViewHolder.player.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda8
                    @Override // android.view.View.OnClickListener
                    public final void onClick(View view) {
                        MediaControlPanel mediaControlPanel = MediaControlPanel.this;
                        PendingIntent pendingIntent2 = pendingIntent;
                        Objects.requireNonNull(mediaControlPanel);
                        if (!mediaControlPanel.mFalsingManager.isFalseTap(1)) {
                            MediaViewController mediaViewController = mediaControlPanel.mMediaViewController;
                            Objects.requireNonNull(mediaViewController);
                            if (!mediaViewController.isGutsVisible) {
                                mediaControlPanel.logSmartspaceCardReported(760, false, 0, 0);
                                ActivityStarter activityStarter = mediaControlPanel.mActivityStarter;
                                MediaViewHolder mediaViewHolder2 = mediaControlPanel.mMediaViewHolder;
                                Objects.requireNonNull(mediaViewHolder2);
                                activityStarter.postStartActivityDismissingKeyguard(pendingIntent2, MediaControlPanel.buildLaunchAnimatorController(mediaViewHolder2.player));
                            }
                        }
                    }
                });
            }
            MediaViewHolder mediaViewHolder2 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder2);
            boolean z4 = true;
            mediaViewHolder2.player.setContentDescription(this.mContext.getString(2131952185, mediaData.song, mediaData.artist, mediaData.app));
            MediaViewHolder mediaViewHolder3 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder3);
            mediaViewHolder3.titleText.setText(mediaData.song);
            MediaViewHolder mediaViewHolder4 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder4);
            mediaViewHolder4.artistText.setText(mediaData.artist);
            this.mBackgroundExecutor.execute(new KeyguardPINView$$ExternalSyntheticLambda0(this, this.mController, 1));
            boolean z5 = mediaData.isClearable;
            MediaViewHolder mediaViewHolder5 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder5);
            TextView textView = mediaViewHolder5.longPressText;
            if (z5) {
                i = 2131952183;
            } else {
                i = 2131952178;
            }
            textView.setText(i);
            MediaViewHolder mediaViewHolder6 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder6);
            ViewGroup viewGroup = mediaViewHolder6.seamless;
            viewGroup.setVisibility(0);
            MediaViewHolder mediaViewHolder7 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder7);
            ImageView imageView = mediaViewHolder7.seamlessIcon;
            MediaViewHolder mediaViewHolder8 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder8);
            TextView textView2 = mediaViewHolder8.seamlessText;
            final MediaDeviceData mediaDeviceData = mediaData.device;
            if ((mediaDeviceData == null || mediaDeviceData.enabled) && !mediaData.resumption) {
                z = false;
            } else {
                z = true;
            }
            float f2 = 0.38f;
            if (z) {
                f = 0.38f;
            } else {
                f = 1.0f;
            }
            MediaViewHolder mediaViewHolder9 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder9);
            mediaViewHolder9.seamlessButton.setAlpha(f);
            viewGroup.setEnabled(!z);
            CharSequence string = this.mContext.getString(2131952748);
            if (mediaDeviceData != null) {
                Drawable drawable5 = mediaDeviceData.icon;
                if (drawable5 instanceof AdaptiveIcon) {
                    AdaptiveIcon adaptiveIcon = (AdaptiveIcon) drawable5;
                    adaptiveIcon.setBackgroundColor(this.mBackgroundColor);
                    imageView.setImageDrawable(adaptiveIcon);
                } else {
                    imageView.setImageDrawable(drawable5);
                }
                string = mediaDeviceData.name;
            } else {
                imageView.setImageResource(2131232038);
            }
            textView2.setText(string);
            viewGroup.setContentDescription(string);
            viewGroup.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda9
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    MediaControlPanel mediaControlPanel = MediaControlPanel.this;
                    MediaDeviceData mediaDeviceData2 = mediaDeviceData;
                    MediaData mediaData2 = mediaData;
                    Objects.requireNonNull(mediaControlPanel);
                    if (!mediaControlPanel.mFalsingManager.isFalseTap(1)) {
                        Objects.requireNonNull(mediaDeviceData2);
                        PendingIntent pendingIntent2 = mediaDeviceData2.intent;
                        if (pendingIntent2 == null) {
                            MediaOutputDialogFactory mediaOutputDialogFactory = mediaControlPanel.mMediaOutputDialogFactory;
                            Objects.requireNonNull(mediaData2);
                            String str2 = mediaData2.packageName;
                            MediaViewHolder mediaViewHolder10 = mediaControlPanel.mMediaViewHolder;
                            Objects.requireNonNull(mediaViewHolder10);
                            mediaOutputDialogFactory.create(str2, true, mediaViewHolder10.seamlessButton);
                        } else if (pendingIntent2.isActivity()) {
                            mediaControlPanel.mActivityStarter.startActivity(mediaDeviceData2.intent.getIntent(), true);
                        } else {
                            try {
                                mediaDeviceData2.intent.send();
                            } catch (PendingIntent.CanceledException unused) {
                                Log.e("MediaControlPanel", "Device pending intent was canceled");
                            }
                        }
                    }
                }
            });
            MediaViewHolder mediaViewHolder10 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder10);
            TextView textView3 = mediaViewHolder10.dismissText;
            if (z5) {
                f2 = 1.0f;
            }
            textView3.setAlpha(f2);
            MediaViewHolder mediaViewHolder11 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder11);
            mediaViewHolder11.dismiss.setEnabled(z5);
            MediaViewHolder mediaViewHolder12 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder12);
            mediaViewHolder12.dismiss.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda6(this, str, mediaData, 0));
            this.mMediaViewController.refreshState();
            MediaViewHolder mediaViewHolder13 = this.mMediaViewHolder;
            if (mediaViewHolder13 instanceof PlayerViewHolder) {
                MediaViewController mediaViewController = this.mMediaViewController;
                Objects.requireNonNull(mediaViewController);
                ConstraintSet constraintSet = mediaViewController.expandedLayout;
                MediaViewController mediaViewController2 = this.mMediaViewController;
                Objects.requireNonNull(mediaViewController2);
                ConstraintSet constraintSet2 = mediaViewController2.collapsedLayout;
                MediaViewHolder mediaViewHolder14 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder14);
                ImageView imageView2 = mediaViewHolder14.albumView;
                Icon icon = mediaData.artwork;
                if (icon == null) {
                    z4 = false;
                }
                if (z4) {
                    if (icon == null) {
                        drawable4 = null;
                    } else {
                        Drawable loadDrawable = icon.loadDrawable(this.mContext);
                        float intrinsicHeight = loadDrawable.getIntrinsicHeight() / loadDrawable.getIntrinsicWidth();
                        if (intrinsicHeight > 1.0f) {
                            int i3 = this.mAlbumArtSize;
                            rect = new Rect(0, 0, i3, (int) (i3 * intrinsicHeight));
                        } else {
                            int i4 = this.mAlbumArtSize;
                            rect = new Rect(0, 0, (int) (i4 / intrinsicHeight), i4);
                        }
                        if (rect.width() > this.mAlbumArtSize || rect.height() > this.mAlbumArtSize) {
                            rect.offset((int) (-((rect.width() - this.mAlbumArtSize) / 2.0f)), (int) (-((rect.height() - this.mAlbumArtSize) / 2.0f)));
                        }
                        loadDrawable.setBounds(rect);
                        drawable4 = loadDrawable;
                    }
                    imageView2.setPadding(0, 0, 0, 0);
                    imageView2.setImageDrawable(drawable4);
                } else {
                    MediaDeviceData mediaDeviceData2 = mediaData.device;
                    if (mediaDeviceData2 == null || (drawable3 = mediaDeviceData2.icon) == null) {
                        drawable2 = this.mContext.getDrawable(2131232001);
                    } else {
                        drawable2 = drawable3.getConstantState().newDrawable().mutate();
                    }
                    drawable2.setTintList(ColorStateList.valueOf(this.mBackgroundColor));
                    int i5 = this.mDevicePadding;
                    imageView2.setPadding(i5, i5, i5, i5);
                    imageView2.setImageDrawable(drawable2);
                }
                MediaViewHolder mediaViewHolder15 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder15);
                ImageView imageView3 = mediaViewHolder15.appIcon;
                imageView3.clearColorFilter();
                Icon icon2 = mediaData.appIcon;
                if (icon2 == null || mediaData.resumption) {
                    ColorMatrix colorMatrix = new ColorMatrix();
                    colorMatrix.setSaturation(0.0f);
                    imageView3.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
                    try {
                        imageView3.setImageDrawable(this.mContext.getPackageManager().getApplicationIcon(mediaData.packageName));
                    } catch (PackageManager.NameNotFoundException e2) {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Cannot find icon for package ");
                        m.append(mediaData.packageName);
                        Log.w("MediaControlPanel", m.toString(), e2);
                        imageView3.setImageResource(2131232196);
                    }
                } else {
                    imageView3.setImageIcon(icon2);
                    imageView3.setColorFilter(this.mContext.getColor(2131100327));
                }
                List<MediaAction> list = mediaData.actions;
                List<Integer> list2 = mediaData.actionsToShowInCompact;
                MediaFlags mediaFlags = this.mMediaFlags;
                Objects.requireNonNull(mediaFlags);
                ArrayList arrayList = list;
                ArrayList arrayList2 = list2;
                if (mediaFlags.featureFlags.isEnabled(Flags.MEDIA_SESSION_ACTIONS)) {
                    MediaButton mediaButton = mediaData.semanticActions;
                    arrayList = list;
                    arrayList2 = list2;
                    if (mediaButton != null) {
                        ArrayList arrayList3 = new ArrayList();
                        arrayList3.add(mediaButton.startCustom);
                        arrayList3.add(mediaButton.prevOrCustom);
                        arrayList3.add(mediaButton.playOrPause);
                        arrayList3.add(mediaButton.nextOrCustom);
                        arrayList3.add(mediaButton.endCustom);
                        ArrayList arrayList4 = new ArrayList();
                        arrayList4.add(1);
                        arrayList4.add(2);
                        arrayList4.add(3);
                        arrayList = arrayList3;
                        arrayList2 = arrayList4;
                    }
                }
                int i6 = 0;
                while (i6 < arrayList.size()) {
                    int[] iArr2 = ACTION_IDS;
                    if (i6 >= iArr2.length) {
                        break;
                    }
                    int i7 = iArr2[i6];
                    boolean contains = arrayList2.contains(Integer.valueOf(i6));
                    ImageButton action = this.mMediaViewHolder.getAction(i7);
                    MediaAction mediaAction = arrayList.get(i6);
                    if (mediaAction != null) {
                        action.setImageIcon(mediaAction.icon);
                        action.setContentDescription(mediaAction.contentDescription);
                        final Runnable runnable = mediaAction.action;
                        if (runnable == null) {
                            action.setEnabled(false);
                            z3 = true;
                        } else {
                            action.setEnabled(true);
                            action.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda10
                                @Override // android.view.View.OnClickListener
                                public final void onClick(View view) {
                                    MediaControlPanel mediaControlPanel = MediaControlPanel.this;
                                    Runnable runnable2 = runnable;
                                    Objects.requireNonNull(mediaControlPanel);
                                    if (!mediaControlPanel.mFalsingManager.isFalseTap(1)) {
                                        mediaControlPanel.logSmartspaceCardReported(760, false, 0, 0);
                                        runnable2.run();
                                    }
                                }
                            });
                            z3 = true;
                        }
                        setVisibleAndAlpha(constraintSet2, i7, contains);
                        setVisibleAndAlpha(constraintSet, i7, z3);
                    } else {
                        action.setImageIcon(null);
                        action.setContentDescription(null);
                        action.setEnabled(false);
                        setVisibleAndAlpha(constraintSet2, i7, contains);
                        Objects.requireNonNull(constraintSet);
                        constraintSet.get(i7).propertySet.visibility = 4;
                        constraintSet.get(i7).propertySet.alpha = 0.0f;
                    }
                    i6++;
                }
                while (true) {
                    iArr = ACTION_IDS;
                    if (i6 >= iArr.length) {
                        break;
                    }
                    setVisibleAndAlpha(constraintSet2, iArr[i6], false);
                    setVisibleAndAlpha(constraintSet, iArr[i6], false);
                    i6++;
                }
                if (arrayList.size() == 0) {
                    int i8 = iArr[0];
                    Objects.requireNonNull(constraintSet);
                    constraintSet.get(i8).propertySet.visibility = 4;
                }
            } else if (mediaViewHolder13 instanceof PlayerSessionViewHolder) {
                int i9 = this.mBackgroundColor;
                int defaultColor = Utils.getColorAttr(this.mContext, 16842806).getDefaultColor();
                int defaultColor2 = Utils.getColorAttr(this.mContext, 16842806).getDefaultColor();
                int defaultColor3 = Utils.getColorAttr(this.mContext, 16842809).getDefaultColor();
                int defaultColor4 = Utils.getColorAttr(this.mContext, 16842808).getDefaultColor();
                int defaultColor5 = Utils.getColorAttr(this.mContext, 16843282).getDefaultColor();
                MediaViewHolder mediaViewHolder16 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder16);
                ImageView imageView4 = mediaViewHolder16.appIcon;
                imageView4.clearColorFilter();
                try {
                    imageView4.setImageDrawable(this.mContext.getPackageManager().getApplicationIcon(mediaData.packageName));
                } catch (PackageManager.NameNotFoundException e3) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Cannot find icon for package ");
                    m2.append(mediaData.packageName);
                    Log.w("MediaControlPanel", m2.toString(), e3);
                    Icon icon3 = mediaData.appIcon;
                    if (icon3 != null) {
                        imageView4.setImageIcon(icon3);
                    } else {
                        imageView4.setImageResource(2131232196);
                    }
                    imageView4.setColorFilter(this.mContext.getColor(2131100327));
                }
                MediaViewHolder mediaViewHolder17 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder17);
                ImageView imageView5 = mediaViewHolder17.albumView;
                Icon icon4 = mediaData.artwork;
                if (icon4 != null) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                if (z2) {
                    colorScheme = new ColorScheme(WallpaperColors.fromBitmap(icon4.getBitmap()));
                    MediaViewHolder mediaViewHolder18 = this.mMediaViewHolder;
                    Objects.requireNonNull(mediaViewHolder18);
                    int width = mediaViewHolder18.player.getWidth();
                    MediaViewHolder mediaViewHolder19 = this.mMediaViewHolder;
                    Objects.requireNonNull(mediaViewHolder19);
                    int height = mediaViewHolder19.player.getHeight();
                    Icon icon5 = mediaData.artwork;
                    if (icon5 == null) {
                        i2 = 0;
                        drawable = null;
                    } else {
                        Drawable loadDrawable2 = icon5.loadDrawable(this.mContext);
                        Rect rect2 = new Rect(0, 0, width, height);
                        if (rect2.width() > width || rect2.height() > height) {
                            rect2.offset((int) (-((rect2.width() - width) / 2.0f)), (int) (-((rect2.height() - height) / 2.0f)));
                        }
                        loadDrawable2.setBounds(rect2);
                        i2 = 0;
                        drawable = loadDrawable2;
                    }
                    imageView5.setPadding(i2, i2, i2, i2);
                    imageView5.setImageDrawable(drawable);
                    imageView5.setClipToOutline(true);
                } else {
                    try {
                        colorScheme = new ColorScheme(WallpaperColors.fromDrawable(this.mContext.getPackageManager().getApplicationIcon(mediaData.packageName)));
                    } catch (PackageManager.NameNotFoundException e4) {
                        StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("Cannot find icon for package ");
                        m3.append(mediaData.packageName);
                        Log.w("MediaControlPanel", m3.toString(), e4);
                        colorScheme = null;
                    }
                }
                if (colorScheme != null) {
                    i9 = ((Integer) colorScheme.accent2.get(9)).intValue();
                    defaultColor = ((Integer) colorScheme.accent1.get(2)).intValue();
                    defaultColor2 = ((Integer) colorScheme.neutral1.get(1)).intValue();
                    defaultColor3 = ((Integer) colorScheme.neutral1.get(10)).intValue();
                    defaultColor4 = ((Integer) colorScheme.neutral2.get(3)).intValue();
                    defaultColor5 = ((Integer) colorScheme.neutral2.get(5)).intValue();
                }
                ColorStateList valueOf = ColorStateList.valueOf(i9);
                ColorStateList valueOf2 = ColorStateList.valueOf(defaultColor);
                ColorStateList valueOf3 = ColorStateList.valueOf(defaultColor2);
                imageView5.setForegroundTintList(ColorStateList.valueOf(i9));
                imageView5.setBackgroundTintList(ColorStateList.valueOf(i9));
                MediaViewHolder mediaViewHolder20 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder20);
                mediaViewHolder20.player.setBackgroundTintList(valueOf);
                MediaViewHolder mediaViewHolder21 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder21);
                mediaViewHolder21.titleText.setTextColor(defaultColor2);
                MediaViewHolder mediaViewHolder22 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder22);
                mediaViewHolder22.artistText.setTextColor(defaultColor4);
                MediaViewHolder mediaViewHolder23 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder23);
                SeekBar seekBar = mediaViewHolder23.seekBar;
                seekBar.getThumb().setTintList(valueOf3);
                seekBar.setProgressTintList(valueOf3);
                seekBar.setProgressBackgroundTintList(ColorStateList.valueOf(defaultColor5));
                MediaViewHolder mediaViewHolder24 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder24);
                mediaViewHolder24.seamlessButton.setBackgroundTintList(valueOf2);
                MediaViewHolder mediaViewHolder25 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder25);
                mediaViewHolder25.seamlessIcon.setImageTintList(valueOf);
                MediaViewHolder mediaViewHolder26 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder26);
                mediaViewHolder26.seamlessText.setTextColor(i9);
                MediaButton mediaButton2 = mediaData.semanticActions;
                if (mediaButton2 != null) {
                    PlayerSessionViewHolder playerSessionViewHolder = (PlayerSessionViewHolder) this.mMediaViewHolder;
                    Objects.requireNonNull(playerSessionViewHolder);
                    playerSessionViewHolder.actionPlayPause.setBackgroundTintList(valueOf2);
                    setSemanticButton(playerSessionViewHolder.actionPlayPause, mediaButton2.playOrPause, ColorStateList.valueOf(defaultColor3));
                    setSemanticButton(playerSessionViewHolder.actionNext, mediaButton2.nextOrCustom, valueOf3);
                    setSemanticButton(playerSessionViewHolder.actionPrev, mediaButton2.prevOrCustom, valueOf3);
                    setSemanticButton(playerSessionViewHolder.actionStart, mediaButton2.startCustom, valueOf3);
                    setSemanticButton(playerSessionViewHolder.actionEnd, mediaButton2.endCustom, valueOf3);
                } else {
                    Log.w("MediaControlPanel", "Using semantic player, but did not get buttons");
                }
                MediaViewHolder mediaViewHolder27 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder27);
                mediaViewHolder27.longPressText.setTextColor(valueOf3);
                MediaViewHolder mediaViewHolder28 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder28);
                mediaViewHolder28.settingsText.setTextColor(valueOf3);
                MediaViewHolder mediaViewHolder29 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder29);
                mediaViewHolder29.settingsText.setBackgroundTintList(valueOf2);
                MediaViewHolder mediaViewHolder30 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder30);
                mediaViewHolder30.cancelText.setTextColor(valueOf3);
                MediaViewHolder mediaViewHolder31 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder31);
                mediaViewHolder31.cancelText.setBackgroundTintList(valueOf2);
                MediaViewHolder mediaViewHolder32 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder32);
                mediaViewHolder32.dismissText.setTextColor(valueOf3);
                MediaViewHolder mediaViewHolder33 = this.mMediaViewHolder;
                Objects.requireNonNull(mediaViewHolder33);
                mediaViewHolder33.dismissText.setBackgroundTintList(valueOf2);
            }
        }
    }

    public final void closeGuts(boolean z) {
        MediaViewHolder mediaViewHolder = this.mMediaViewHolder;
        if (mediaViewHolder != null) {
            mediaViewHolder.marquee(false);
        } else {
            RecommendationViewHolder recommendationViewHolder = this.mRecommendationViewHolder;
            if (recommendationViewHolder != null) {
                recommendationViewHolder.longPressText.getHandler().postDelayed(new RecommendationViewHolder$marquee$1(recommendationViewHolder, false), 500L);
            }
        }
        MediaViewController mediaViewController = this.mMediaViewController;
        Objects.requireNonNull(mediaViewController);
        if (mediaViewController.isGutsVisible) {
            mediaViewController.isGutsVisible = false;
            if (!z) {
                mediaViewController.animateNextStateChange = true;
                mediaViewController.animationDuration = 500L;
                mediaViewController.animationDelay = 0L;
            }
            mediaViewController.setCurrentState(mediaViewController.currentStartLocation, mediaViewController.currentEndLocation, mediaViewController.currentTransitionProgress, z);
        }
    }

    public final int getSurfaceForSmartspaceLogging() {
        MediaViewController mediaViewController = this.mMediaViewController;
        Objects.requireNonNull(mediaViewController);
        int i = mediaViewController.currentEndLocation;
        if (i == 1 || i == 0) {
            return 4;
        }
        if (i == 2) {
            return 2;
        }
        return 0;
    }

    public final void logSmartspaceCardReported(int i, boolean z, int i2, int i3) {
        MediaCarouselController mediaCarouselController = this.mMediaCarouselController;
        int i4 = this.mInstanceId;
        int i5 = this.mUid;
        int[] iArr = {getSurfaceForSmartspaceLogging()};
        Objects.requireNonNull(mediaCarouselController);
        MediaCarouselController.logSmartspaceCardReported$default(mediaCarouselController, i, i4, i5, z, iArr, i2, i3, 0, 0, 384);
    }

    public final void openGuts() {
        Layout layout;
        boolean z;
        MediaViewController mediaViewController = this.mMediaViewController;
        Objects.requireNonNull(mediaViewController);
        ConstraintSet constraintSet = mediaViewController.expandedLayout;
        MediaViewController mediaViewController2 = this.mMediaViewController;
        Objects.requireNonNull(mediaViewController2);
        ConstraintSet constraintSet2 = mediaViewController2.collapsedLayout;
        MediaViewHolder mediaViewHolder = this.mMediaViewHolder;
        if (mediaViewHolder != null) {
            mediaViewHolder.marquee(true);
            MediaViewHolder mediaViewHolder2 = this.mMediaViewHolder;
            Objects.requireNonNull(mediaViewHolder2);
            layout = mediaViewHolder2.settingsText.getLayout();
        } else {
            RecommendationViewHolder recommendationViewHolder = this.mRecommendationViewHolder;
            if (recommendationViewHolder != null) {
                recommendationViewHolder.longPressText.getHandler().postDelayed(new RecommendationViewHolder$marquee$1(recommendationViewHolder, true), 500L);
                RecommendationViewHolder recommendationViewHolder2 = this.mRecommendationViewHolder;
                Objects.requireNonNull(recommendationViewHolder2);
                layout = recommendationViewHolder2.settingsText.getLayout();
            } else {
                layout = null;
            }
        }
        if (layout == null || layout.getEllipsisCount(0) <= 0) {
            z = false;
        } else {
            z = true;
        }
        MediaViewController mediaViewController3 = this.mMediaViewController;
        Objects.requireNonNull(mediaViewController3);
        mediaViewController3.shouldHideGutsSettings = z;
        if (z) {
            Objects.requireNonNull(constraintSet);
            constraintSet.get(2131428837).layout.widthMax = 0;
            Objects.requireNonNull(constraintSet2);
            constraintSet2.get(2131428837).layout.widthMax = 0;
        }
        MediaViewController mediaViewController4 = this.mMediaViewController;
        Objects.requireNonNull(mediaViewController4);
        if (!mediaViewController4.isGutsVisible) {
            mediaViewController4.isGutsVisible = true;
            mediaViewController4.animateNextStateChange = true;
            mediaViewController4.animationDuration = 500L;
            mediaViewController4.animationDelay = 0L;
            mediaViewController4.setCurrentState(mediaViewController4.currentStartLocation, mediaViewController4.currentEndLocation, mediaViewController4.currentTransitionProgress, false);
        }
    }

    public final void setSmartspaceRecItemOnClickListener(final ViewGroup viewGroup, final SmartspaceAction smartspaceAction, final int i) {
        if (viewGroup == null || smartspaceAction == null || smartspaceAction.getIntent() == null || smartspaceAction.getIntent().getExtras() == null) {
            Log.e("MediaControlPanel", "No tap action can be set up");
        } else {
            viewGroup.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda7
                /* JADX WARN: Code restructure failed: missing block: B:15:0x0045, code lost:
                    r2 = r1.getIntent().getExtras().getString("com.google.android.apps.gsa.smartspace.extra.SMARTSPACE_INTENT");
                 */
                /* JADX WARN: Code restructure failed: missing block: B:7:0x0029, code lost:
                    if (r8.mSmartspaceMediaItemsCount > 3) goto L_0x002e;
                 */
                /* JADX WARN: Removed duplicated region for block: B:22:0x007e  */
                /* JADX WARN: Removed duplicated region for block: B:23:0x0093  */
                @Override // android.view.View.OnClickListener
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final void onClick(android.view.View r8) {
                    /*
                        r7 = this;
                        com.android.systemui.media.MediaControlPanel r8 = com.android.systemui.media.MediaControlPanel.this
                        int r0 = r2
                        android.app.smartspace.SmartspaceAction r1 = r3
                        android.view.View r7 = r4
                        java.util.Objects.requireNonNull(r8)
                        com.android.systemui.plugins.FalsingManager r2 = r8.mFalsingManager
                        r3 = 1
                        boolean r2 = r2.isFalseTap(r3)
                        if (r2 == 0) goto L_0x0016
                        goto L_0x00a5
                    L_0x0016:
                        r2 = 760(0x2f8, float:1.065E-42)
                        com.android.systemui.media.MediaCarouselController r4 = r8.mMediaCarouselController
                        java.util.Objects.requireNonNull(r4)
                        com.android.systemui.media.MediaCarouselScrollHandler r4 = r4.mediaCarouselScrollHandler
                        java.util.Objects.requireNonNull(r4)
                        boolean r4 = r4.qsExpanded
                        if (r4 != 0) goto L_0x002c
                        int r4 = r8.mSmartspaceMediaItemsCount
                        r5 = 3
                        if (r4 <= r5) goto L_0x002c
                        goto L_0x002e
                    L_0x002c:
                        int r5 = r8.mSmartspaceMediaItemsCount
                    L_0x002e:
                        r8.logSmartspaceCardReported(r2, r3, r0, r5)
                        r0 = 0
                        if (r1 == 0) goto L_0x007b
                        android.content.Intent r2 = r1.getIntent()
                        if (r2 == 0) goto L_0x007b
                        android.content.Intent r2 = r1.getIntent()
                        android.os.Bundle r2 = r2.getExtras()
                        if (r2 != 0) goto L_0x0045
                        goto L_0x007b
                    L_0x0045:
                        android.content.Intent r2 = r1.getIntent()
                        android.os.Bundle r2 = r2.getExtras()
                        java.lang.String r4 = "com.google.android.apps.gsa.smartspace.extra.SMARTSPACE_INTENT"
                        java.lang.String r2 = r2.getString(r4)
                        if (r2 != 0) goto L_0x0056
                        goto L_0x007b
                    L_0x0056:
                        android.content.Intent r4 = android.content.Intent.parseUri(r2, r3)     // Catch: URISyntaxException -> 0x0061
                        java.lang.String r5 = "KEY_OPEN_IN_FOREGROUND"
                        boolean r2 = r4.getBooleanExtra(r5, r0)     // Catch: URISyntaxException -> 0x0061
                        goto L_0x007c
                    L_0x0061:
                        r4 = move-exception
                        java.lang.StringBuilder r5 = new java.lang.StringBuilder
                        r5.<init>()
                        java.lang.String r6 = "Failed to create intent from URI: "
                        r5.append(r6)
                        r5.append(r2)
                        java.lang.String r2 = r5.toString()
                        java.lang.String r5 = "MediaControlPanel"
                        android.util.Log.wtf(r5, r2)
                        r4.printStackTrace()
                    L_0x007b:
                        r2 = r0
                    L_0x007c:
                        if (r2 == 0) goto L_0x0093
                        com.android.systemui.plugins.ActivityStarter r7 = r8.mActivityStarter
                        android.content.Intent r1 = r1.getIntent()
                        com.android.systemui.media.RecommendationViewHolder r2 = r8.mRecommendationViewHolder
                        java.util.Objects.requireNonNull(r2)
                        com.android.systemui.util.animation.TransitionLayout r2 = r2.recommendations
                        com.android.systemui.media.MediaControlPanel$1 r2 = com.android.systemui.media.MediaControlPanel.buildLaunchAnimatorController(r2)
                        r7.postStartActivityDismissingKeyguard(r1, r0, r2)
                        goto L_0x009e
                    L_0x0093:
                        android.content.Context r7 = r7.getContext()
                        android.content.Intent r0 = r1.getIntent()
                        r7.startActivity(r0)
                    L_0x009e:
                        com.android.systemui.media.MediaCarouselController r7 = r8.mMediaCarouselController
                        java.util.Objects.requireNonNull(r7)
                        r7.shouldScrollToActivePlayer = r3
                    L_0x00a5:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda7.onClick(android.view.View):void");
                }
            });
        }
    }

    public MediaControlPanel(Context context, Executor executor, ActivityStarter activityStarter, MediaViewController mediaViewController, SeekBarViewModel seekBarViewModel, Lazy<MediaDataManager> lazy, MediaOutputDialogFactory mediaOutputDialogFactory, MediaCarouselController mediaCarouselController, FalsingManager falsingManager, MediaFlags mediaFlags, SystemClock systemClock) {
        this.mContext = context;
        this.mBackgroundExecutor = executor;
        this.mActivityStarter = activityStarter;
        this.mSeekBarViewModel = seekBarViewModel;
        this.mMediaViewController = mediaViewController;
        this.mMediaDataManagerLazy = lazy;
        this.mMediaOutputDialogFactory = mediaOutputDialogFactory;
        this.mMediaCarouselController = mediaCarouselController;
        this.mFalsingManager = falsingManager;
        this.mMediaFlags = mediaFlags;
        this.mSystemClock = systemClock;
        this.mAlbumArtSize = context.getResources().getDimensionPixelSize(2131166879);
        this.mDevicePadding = this.mContext.getResources().getDimensionPixelSize(2131166877);
        Function0<Unit> mediaControlPanel$$ExternalSyntheticLambda14 = new Function0() { // from class: com.android.systemui.media.MediaControlPanel$$ExternalSyntheticLambda14
            @Override // kotlin.jvm.functions.Function0
            public final Object invoke() {
                MediaControlPanel mediaControlPanel = MediaControlPanel.this;
                Objects.requireNonNull(mediaControlPanel);
                mediaControlPanel.logSmartspaceCardReported(760, false, 0, 0);
                return Unit.INSTANCE;
            }
        };
        Objects.requireNonNull(seekBarViewModel);
        seekBarViewModel.logSmartspaceClick = mediaControlPanel$$ExternalSyntheticLambda14;
    }

    /* JADX WARN: Type inference failed for: r0v2, types: [com.android.systemui.media.MediaControlPanel$1] */
    public static AnonymousClass1 buildLaunchAnimatorController(final TransitionLayout transitionLayout) {
        if (transitionLayout.getParent() instanceof ViewGroup) {
            return new GhostedViewLaunchAnimatorController(transitionLayout) { // from class: com.android.systemui.media.MediaControlPanel.1
                @Override // com.android.systemui.animation.GhostedViewLaunchAnimatorController
                public final float getCurrentTopCornerRadius() {
                    return ((IlluminationDrawable) transitionLayout.getBackground()).getCornerRadius();
                }

                @Override // com.android.systemui.animation.GhostedViewLaunchAnimatorController
                public final float getCurrentBottomCornerRadius() {
                    return getCurrentTopCornerRadius();
                }

                @Override // com.android.systemui.animation.GhostedViewLaunchAnimatorController, com.android.systemui.animation.LaunchAnimator.Controller
                public final void onLaunchAnimationEnd(boolean z) {
                    super.onLaunchAnimationEnd(z);
                    ((IlluminationDrawable) transitionLayout.getBackground()).setCornerRadiusOverride(null);
                }

                @Override // com.android.systemui.animation.GhostedViewLaunchAnimatorController
                public final void setBackgroundCornerRadius(Drawable drawable, float f, float f2) {
                    ((IlluminationDrawable) drawable).setCornerRadiusOverride(Float.valueOf(Math.min(f, f2)));
                }
            };
        }
        Log.wtf("MediaControlPanel", "Skipping player animation as it is not attached to a ViewGroup", new Exception());
        return null;
    }

    public final void setSemanticButton(ImageButton imageButton, MediaAction mediaAction, ColorStateList colorStateList) {
        imageButton.setImageTintList(colorStateList);
        if (mediaAction != null) {
            imageButton.setImageIcon(mediaAction.icon);
            imageButton.setContentDescription(mediaAction.contentDescription);
            Runnable runnable = mediaAction.action;
            if (runnable == null) {
                imageButton.setEnabled(false);
                return;
            }
            imageButton.setEnabled(true);
            imageButton.setOnClickListener(new MediaControlPanel$$ExternalSyntheticLambda5(this, runnable, 0));
            return;
        }
        imageButton.setImageIcon(null);
        imageButton.setContentDescription(null);
        imageButton.setEnabled(false);
    }
}
