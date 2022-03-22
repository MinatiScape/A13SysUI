package com.android.systemui.theme;

import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.om.FabricatedOverlay;
import android.content.om.OverlayIdentifier;
import android.content.om.OverlayManagerTransaction;
import android.content.pm.UserInfo;
import android.database.ContentObserver;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Color;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.net.Uri;
import android.os.Handler;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.util.SparseArray;
import android.util.SparseIntArray;
import androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import androidx.fragment.app.DialogFragment$$ExternalSyntheticOutline0;
import com.android.internal.graphics.ColorUtils;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline3;
import com.android.systemui.CoreStartable;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.monet.ColorScheme;
import com.android.systemui.monet.Style;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.util.settings.SecureSettings;
import com.android.wifitrackerlib.WifiEntry$$ExternalSyntheticLambda2;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public class ThemeOverlayController extends CoreStartable {
    public final Executor mBgExecutor;
    public final Handler mBgHandler;
    public final BroadcastDispatcher mBroadcastDispatcher;
    public ColorScheme mColorScheme;
    public boolean mDeferredThemeEvaluation;
    public final DeviceProvisionedController mDeviceProvisionedController;
    public final boolean mIsMonetEnabled;
    public final Executor mMainExecutor;
    public boolean mNeedsOverlayCreation;
    public FabricatedOverlay mNeutralOverlay;
    public FabricatedOverlay mSecondaryOverlay;
    public final SecureSettings mSecureSettings;
    public boolean mSkipSettingChange;
    public final ThemeOverlayApplier mThemeManager;
    public final UserManager mUserManager;
    public final UserTracker mUserTracker;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final WallpaperManager mWallpaperManager;
    public final SparseArray<WallpaperColors> mCurrentColors = new SparseArray<>();
    public int mMainWallpaperColor = 0;
    public Style mThemeStyle = Style.TONAL_SPOT;
    public boolean mAcceptColorEvents = true;
    public final SparseArray<WallpaperColors> mDeferredWallpaperColors = new SparseArray<>();
    public final SparseIntArray mDeferredWallpaperColorsFlags = new SparseIntArray();
    public final AnonymousClass1 mDeviceProvisionedListener = new DeviceProvisionedController.DeviceProvisionedListener() { // from class: com.android.systemui.theme.ThemeOverlayController.1
        @Override // com.android.systemui.statusbar.policy.DeviceProvisionedController.DeviceProvisionedListener
        public final void onUserSetupChanged() {
            if (ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() && ThemeOverlayController.this.mDeferredThemeEvaluation) {
                Log.i("ThemeOverlayController", "Applying deferred theme");
                ThemeOverlayController themeOverlayController = ThemeOverlayController.this;
                themeOverlayController.mDeferredThemeEvaluation = false;
                themeOverlayController.reevaluateSystemTheme(true);
            }
        }
    };
    public final AnonymousClass2 mOnColorsChangedListener = new WallpaperManager.OnColorsChangedListener() { // from class: com.android.systemui.theme.ThemeOverlayController.2
        @Override // android.app.WallpaperManager.OnColorsChangedListener
        public final void onColorsChanged(WallpaperColors wallpaperColors, int i) {
            throw new IllegalStateException("This should never be invoked, all messages should arrive on the overload that has a user id");
        }

        public final void onColorsChanged(WallpaperColors wallpaperColors, int i, int i2) {
            boolean z = i2 == ThemeOverlayController.this.mUserTracker.getUserId();
            if (z) {
                ThemeOverlayController themeOverlayController = ThemeOverlayController.this;
                if (!themeOverlayController.mAcceptColorEvents) {
                    WakefulnessLifecycle wakefulnessLifecycle = themeOverlayController.mWakefulnessLifecycle;
                    Objects.requireNonNull(wakefulnessLifecycle);
                    if (wakefulnessLifecycle.mWakefulness != 0) {
                        ThemeOverlayController.this.mDeferredWallpaperColors.put(i2, wallpaperColors);
                        ThemeOverlayController.this.mDeferredWallpaperColorsFlags.put(i2, i);
                        Log.i("ThemeOverlayController", "colors received; processing deferred until screen off: " + wallpaperColors + " user: " + i2);
                        return;
                    }
                }
            }
            if (z && wallpaperColors != null) {
                ThemeOverlayController themeOverlayController2 = ThemeOverlayController.this;
                themeOverlayController2.mAcceptColorEvents = false;
                themeOverlayController2.mDeferredWallpaperColors.put(i2, null);
                ThemeOverlayController.this.mDeferredWallpaperColorsFlags.put(i2, 0);
            }
            ThemeOverlayController.m116$$Nest$mhandleWallpaperColors(ThemeOverlayController.this, wallpaperColors, i, i2);
        }
    };
    public final AnonymousClass3 mUserTrackerCallback = new UserTracker.Callback() { // from class: com.android.systemui.theme.ThemeOverlayController.3
        @Override // com.android.systemui.settings.UserTracker.Callback
        public final void onUserChanged(int i) {
            boolean isManagedProfile = ThemeOverlayController.this.mUserManager.isManagedProfile(i);
            if (ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() || !isManagedProfile) {
                Log.d("ThemeOverlayController", "Updating overlays for user switch / profile added.");
                ThemeOverlayController.this.reevaluateSystemTheme(true);
                return;
            }
            Log.i("ThemeOverlayController", "User setup not finished when new user event was received. Deferring... Managed profile? " + isManagedProfile);
        }
    };
    public final AnonymousClass4 mBroadcastReceiver = new BroadcastReceiver() { // from class: com.android.systemui.theme.ThemeOverlayController.4
        @Override // android.content.BroadcastReceiver
        public final void onReceive(Context context, Intent intent) {
            boolean equals = "android.intent.action.MANAGED_PROFILE_ADDED".equals(intent.getAction());
            boolean isManagedProfile = ThemeOverlayController.this.mUserManager.isManagedProfile(intent.getIntExtra("android.intent.extra.user_handle", 0));
            if (equals) {
                if (ThemeOverlayController.this.mDeviceProvisionedController.isCurrentUserSetup() || !isManagedProfile) {
                    Log.d("ThemeOverlayController", "Updating overlays for user switch / profile added.");
                    ThemeOverlayController.this.reevaluateSystemTheme(true);
                    return;
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("User setup not finished when ");
                m.append(intent.getAction());
                m.append(" was received. Deferring... Managed profile? ");
                m.append(isManagedProfile);
                Log.i("ThemeOverlayController", m.toString());
            } else if (!"android.intent.action.WALLPAPER_CHANGED".equals(intent.getAction())) {
            } else {
                if (intent.getBooleanExtra("android.service.wallpaper.extra.FROM_FOREGROUND_APP", false)) {
                    ThemeOverlayController.this.mAcceptColorEvents = true;
                    Log.i("ThemeOverlayController", "Wallpaper changed, allowing color events again");
                    return;
                }
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Wallpaper changed from background app, keep deferring color events. Accepting: ");
                m2.append(ThemeOverlayController.this.mAcceptColorEvents);
                Log.i("ThemeOverlayController", m2.toString());
            }
        }
    };

    public static boolean isSeedColorSet(JSONObject jSONObject, WallpaperColors wallpaperColors) {
        String str;
        if (wallpaperColors == null || (str = (String) jSONObject.opt("android.theme.customization.system_palette")) == null) {
            return false;
        }
        if (!str.startsWith("#")) {
            str = SupportMenuInflater$$ExternalSyntheticOutline0.m("#", str);
        }
        int parseColor = Color.parseColor(str);
        for (Integer num : ColorScheme.Companion.getSeedColors(wallpaperColors)) {
            if (num.intValue() == parseColor) {
                DialogFragment$$ExternalSyntheticOutline0.m("Same as previous set system palette: ", str, "ThemeOverlayController");
                return true;
            }
        }
        return false;
    }

    @Override // com.android.systemui.CoreStartable, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("mSystemColors=");
        m.append(this.mCurrentColors);
        printWriter.println(m.toString());
        printWriter.println("mMainWallpaperColor=" + Integer.toHexString(this.mMainWallpaperColor));
        printWriter.println("mSecondaryOverlay=" + this.mSecondaryOverlay);
        printWriter.println("mNeutralOverlay=" + this.mNeutralOverlay);
        StringBuilder sb = new StringBuilder();
        sb.append("mIsMonetEnabled=");
        StringBuilder m2 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb, this.mIsMonetEnabled, printWriter, "mColorScheme=");
        m2.append(this.mColorScheme);
        printWriter.println(m2.toString());
        StringBuilder sb2 = new StringBuilder();
        sb2.append("mNeedsOverlayCreation=");
        StringBuilder m3 = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(sb2, this.mNeedsOverlayCreation, printWriter, "mAcceptColorEvents="), this.mAcceptColorEvents, printWriter, "mDeferredThemeEvaluation="), this.mDeferredThemeEvaluation, printWriter, "mThemeStyle=");
        m3.append(this.mThemeStyle);
        printWriter.println(m3.toString());
    }

    public final FabricatedOverlay getOverlay(int i, int i2, Style style) {
        ArrayList arrayList;
        String str;
        String str2;
        int i3 = this.mContext.getResources().getConfiguration().uiMode;
        ColorScheme colorScheme = new ColorScheme(i, style);
        this.mColorScheme = colorScheme;
        if (i2 == 1) {
            arrayList = new ArrayList();
            arrayList.addAll(colorScheme.accent1);
            arrayList.addAll(colorScheme.accent2);
            arrayList.addAll(colorScheme.accent3);
        } else {
            arrayList = new ArrayList();
            arrayList.addAll(colorScheme.neutral1);
            arrayList.addAll(colorScheme.neutral2);
        }
        if (i2 == 1) {
            str = "accent";
        } else {
            str = "neutral";
        }
        ColorScheme colorScheme2 = this.mColorScheme;
        Objects.requireNonNull(colorScheme2);
        int size = colorScheme2.accent1.size();
        FabricatedOverlay.Builder builder = new FabricatedOverlay.Builder(ThemeOverlayApplier.SYSUI_PACKAGE, str, ThemeOverlayApplier.ANDROID_PACKAGE);
        for (int i4 = 0; i4 < arrayList.size(); i4++) {
            int i5 = i4 % size;
            int i6 = (i4 / size) + 1;
            if (i5 == 0) {
                str2 = "android:color/system_" + str + i6 + "_10";
            } else if (i5 != 1) {
                StringBuilder sb = new StringBuilder();
                sb.append("android:color/system_");
                sb.append(str);
                sb.append(i6);
                sb.append("_");
                sb.append(i5 - 1);
                sb.append("00");
                str2 = sb.toString();
            } else {
                str2 = "android:color/system_" + str + i6 + "_50";
            }
            builder.setResourceValue(str2, 28, ColorUtils.setAlphaComponent(((Integer) arrayList.get(i4)).intValue(), 255));
        }
        return builder.build();
    }

    public final void reevaluateSystemTheme(boolean z) {
        int i;
        Style style;
        FabricatedOverlay fabricatedOverlay;
        FabricatedOverlay fabricatedOverlay2;
        WallpaperColors wallpaperColors = this.mCurrentColors.get(this.mUserTracker.getUserId());
        if (wallpaperColors == null) {
            i = 0;
        } else {
            List seedColors = ColorScheme.Companion.getSeedColors(wallpaperColors);
            if (!seedColors.isEmpty()) {
                i = ((Number) seedColors.get(0)).intValue();
            } else {
                throw new NoSuchElementException("List is empty.");
            }
        }
        if (this.mMainWallpaperColor != i || z) {
            this.mMainWallpaperColor = i;
            if (this.mIsMonetEnabled) {
                this.mSecondaryOverlay = getOverlay(i, 1, this.mThemeStyle);
                this.mNeutralOverlay = getOverlay(this.mMainWallpaperColor, 0, this.mThemeStyle);
                this.mNeedsOverlayCreation = true;
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("fetched overlays. accent: ");
                m.append(this.mSecondaryOverlay);
                m.append(" neutral: ");
                m.append(this.mNeutralOverlay);
                Log.d("ThemeOverlayController", m.toString());
            }
            final int userId = this.mUserTracker.getUserId();
            String stringForUser = this.mSecureSettings.getStringForUser("theme_customization_overlay_packages", userId);
            Log.d("ThemeOverlayController", "updateThemeOverlays. Setting: " + stringForUser);
            final ArrayMap arrayMap = new ArrayMap();
            Style style2 = this.mThemeStyle;
            if (!TextUtils.isEmpty(stringForUser)) {
                try {
                    JSONObject jSONObject = new JSONObject(stringForUser);
                    for (String str : ThemeOverlayApplier.THEME_CATEGORIES) {
                        if (jSONObject.has(str)) {
                            arrayMap.put(str, new OverlayIdentifier(jSONObject.getString(str)));
                        }
                    }
                    try {
                        style = Style.valueOf(jSONObject.getString("android.theme.customization.theme_style"));
                    } catch (IllegalArgumentException unused) {
                        style = Style.TONAL_SPOT;
                    }
                    style2 = style;
                } catch (JSONException e) {
                    Log.i("ThemeOverlayController", "Failed to parse THEME_CUSTOMIZATION_OVERLAY_PACKAGES.", e);
                }
            }
            if (this.mIsMonetEnabled && style2 != this.mThemeStyle) {
                this.mThemeStyle = style2;
                this.mNeutralOverlay = getOverlay(this.mMainWallpaperColor, 0, style2);
                this.mSecondaryOverlay = getOverlay(this.mMainWallpaperColor, 1, this.mThemeStyle);
                this.mNeedsOverlayCreation = true;
            }
            OverlayIdentifier overlayIdentifier = (OverlayIdentifier) arrayMap.get("android.theme.customization.system_palette");
            if (this.mIsMonetEnabled && overlayIdentifier != null && overlayIdentifier.getPackageName() != null) {
                try {
                    String lowerCase = overlayIdentifier.getPackageName().toLowerCase();
                    if (!lowerCase.startsWith("#")) {
                        lowerCase = "#" + lowerCase;
                    }
                    int parseColor = Color.parseColor(lowerCase);
                    this.mNeutralOverlay = getOverlay(parseColor, 0, this.mThemeStyle);
                    this.mSecondaryOverlay = getOverlay(parseColor, 1, this.mThemeStyle);
                    this.mNeedsOverlayCreation = true;
                    arrayMap.remove("android.theme.customization.system_palette");
                    arrayMap.remove("android.theme.customization.accent_color");
                } catch (Exception e2) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid color definition: ");
                    m2.append(overlayIdentifier.getPackageName());
                    Log.w("ThemeOverlayController", m2.toString(), e2);
                }
            } else if (!this.mIsMonetEnabled && overlayIdentifier != null) {
                try {
                    arrayMap.remove("android.theme.customization.system_palette");
                    arrayMap.remove("android.theme.customization.accent_color");
                } catch (NumberFormatException unused2) {
                }
            }
            if (!arrayMap.containsKey("android.theme.customization.system_palette") && (fabricatedOverlay2 = this.mNeutralOverlay) != null) {
                arrayMap.put("android.theme.customization.system_palette", fabricatedOverlay2.getIdentifier());
            }
            if (!arrayMap.containsKey("android.theme.customization.accent_color") && (fabricatedOverlay = this.mSecondaryOverlay) != null) {
                arrayMap.put("android.theme.customization.accent_color", fabricatedOverlay.getIdentifier());
            }
            final HashSet hashSet = new HashSet();
            for (UserInfo userInfo : this.mUserManager.getEnabledProfiles(userId)) {
                if (userInfo.isManagedProfile()) {
                    hashSet.add(userInfo.getUserHandle());
                }
            }
            ExifInterface$$ExternalSyntheticOutline2.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Applying overlays: "), (String) arrayMap.keySet().stream().map(new Function() { // from class: com.android.systemui.theme.ThemeOverlayController$$ExternalSyntheticLambda0
                @Override // java.util.function.Function
                public final Object apply(Object obj) {
                    Map map = arrayMap;
                    String str2 = (String) obj;
                    StringBuilder m3 = DebugInfo$$ExternalSyntheticOutline0.m(str2, " -> ");
                    m3.append(map.get(str2));
                    return m3.toString();
                }
            }).collect(Collectors.joining(", ")), "ThemeOverlayController");
            if (this.mNeedsOverlayCreation) {
                this.mNeedsOverlayCreation = false;
                final ThemeOverlayApplier themeOverlayApplier = this.mThemeManager;
                final FabricatedOverlay[] fabricatedOverlayArr = {this.mSecondaryOverlay, this.mNeutralOverlay};
                Objects.requireNonNull(themeOverlayApplier);
                themeOverlayApplier.mBgExecutor.execute(new Runnable() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public final void run() {
                        final ThemeOverlayApplier themeOverlayApplier2 = ThemeOverlayApplier.this;
                        Map map = arrayMap;
                        FabricatedOverlay[] fabricatedOverlayArr2 = fabricatedOverlayArr;
                        int i2 = userId;
                        Set<UserHandle> set = hashSet;
                        Objects.requireNonNull(themeOverlayApplier2);
                        HashSet hashSet2 = new HashSet(ThemeOverlayApplier.THEME_CATEGORIES);
                        ArrayList arrayList = new ArrayList();
                        ((Set) hashSet2.stream().map(new Function() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda2
                            @Override // java.util.function.Function
                            public final Object apply(Object obj) {
                                ThemeOverlayApplier themeOverlayApplier3 = ThemeOverlayApplier.this;
                                Objects.requireNonNull(themeOverlayApplier3);
                                return (String) themeOverlayApplier3.mCategoryToTargetPackage.get((String) obj);
                            }
                        }).collect(Collectors.toSet())).forEach(new ThemeOverlayApplier$$ExternalSyntheticLambda1(themeOverlayApplier2, arrayList, 0));
                        List<Pair> list = (List) arrayList.stream().filter(new ThemeOverlayApplier$$ExternalSyntheticLambda5(themeOverlayApplier2, 0)).filter(new ThemeOverlayApplier$$ExternalSyntheticLambda6(hashSet2, 0)).filter(new ThemeOverlayApplier$$ExternalSyntheticLambda4(map, 0)).filter(ThemeOverlayApplier$$ExternalSyntheticLambda7.INSTANCE).map(ThemeOverlayApplier$$ExternalSyntheticLambda3.INSTANCE).collect(Collectors.toList());
                        OverlayManagerTransaction.Builder transactionBuilder = themeOverlayApplier2.getTransactionBuilder();
                        HashSet hashSet3 = new HashSet();
                        if (fabricatedOverlayArr2 != null) {
                            for (FabricatedOverlay fabricatedOverlay3 : fabricatedOverlayArr2) {
                                hashSet3.add(fabricatedOverlay3.getIdentifier());
                                transactionBuilder.registerFabricatedOverlay(fabricatedOverlay3);
                            }
                        }
                        for (Pair pair : list) {
                            OverlayIdentifier overlayIdentifier2 = new OverlayIdentifier((String) pair.second);
                            themeOverlayApplier2.setEnabled(transactionBuilder, overlayIdentifier2, (String) pair.first, i2, set, false, hashSet3.contains(overlayIdentifier2));
                        }
                        for (String str2 : ThemeOverlayApplier.THEME_CATEGORIES) {
                            if (map.containsKey(str2)) {
                                OverlayIdentifier overlayIdentifier3 = (OverlayIdentifier) map.get(str2);
                                themeOverlayApplier2.setEnabled(transactionBuilder, overlayIdentifier3, str2, i2, set, true, hashSet3.contains(overlayIdentifier3));
                            }
                        }
                        try {
                            themeOverlayApplier2.mOverlayManager.commit(transactionBuilder.build());
                        } catch (IllegalStateException | SecurityException e3) {
                            Log.e("ThemeOverlayApplier", "setEnabled failed", e3);
                        }
                    }
                });
                return;
            }
            final ThemeOverlayApplier themeOverlayApplier2 = this.mThemeManager;
            Objects.requireNonNull(themeOverlayApplier2);
            themeOverlayApplier2.mBgExecutor.execute(new Runnable() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    final ThemeOverlayApplier themeOverlayApplier22 = ThemeOverlayApplier.this;
                    Map map = arrayMap;
                    FabricatedOverlay[] fabricatedOverlayArr2 = fabricatedOverlayArr;
                    int i2 = userId;
                    Set<UserHandle> set = hashSet;
                    Objects.requireNonNull(themeOverlayApplier22);
                    HashSet hashSet2 = new HashSet(ThemeOverlayApplier.THEME_CATEGORIES);
                    ArrayList arrayList = new ArrayList();
                    ((Set) hashSet2.stream().map(new Function() { // from class: com.android.systemui.theme.ThemeOverlayApplier$$ExternalSyntheticLambda2
                        @Override // java.util.function.Function
                        public final Object apply(Object obj) {
                            ThemeOverlayApplier themeOverlayApplier3 = ThemeOverlayApplier.this;
                            Objects.requireNonNull(themeOverlayApplier3);
                            return (String) themeOverlayApplier3.mCategoryToTargetPackage.get((String) obj);
                        }
                    }).collect(Collectors.toSet())).forEach(new ThemeOverlayApplier$$ExternalSyntheticLambda1(themeOverlayApplier22, arrayList, 0));
                    List<Pair> list = (List) arrayList.stream().filter(new ThemeOverlayApplier$$ExternalSyntheticLambda5(themeOverlayApplier22, 0)).filter(new ThemeOverlayApplier$$ExternalSyntheticLambda6(hashSet2, 0)).filter(new ThemeOverlayApplier$$ExternalSyntheticLambda4(map, 0)).filter(ThemeOverlayApplier$$ExternalSyntheticLambda7.INSTANCE).map(ThemeOverlayApplier$$ExternalSyntheticLambda3.INSTANCE).collect(Collectors.toList());
                    OverlayManagerTransaction.Builder transactionBuilder = themeOverlayApplier22.getTransactionBuilder();
                    HashSet hashSet3 = new HashSet();
                    if (fabricatedOverlayArr2 != null) {
                        for (FabricatedOverlay fabricatedOverlay3 : fabricatedOverlayArr2) {
                            hashSet3.add(fabricatedOverlay3.getIdentifier());
                            transactionBuilder.registerFabricatedOverlay(fabricatedOverlay3);
                        }
                    }
                    for (Pair pair : list) {
                        OverlayIdentifier overlayIdentifier2 = new OverlayIdentifier((String) pair.second);
                        themeOverlayApplier22.setEnabled(transactionBuilder, overlayIdentifier2, (String) pair.first, i2, set, false, hashSet3.contains(overlayIdentifier2));
                    }
                    for (String str2 : ThemeOverlayApplier.THEME_CATEGORIES) {
                        if (map.containsKey(str2)) {
                            OverlayIdentifier overlayIdentifier3 = (OverlayIdentifier) map.get(str2);
                            themeOverlayApplier22.setEnabled(transactionBuilder, overlayIdentifier3, str2, i2, set, true, hashSet3.contains(overlayIdentifier3));
                        }
                    }
                    try {
                        themeOverlayApplier22.mOverlayManager.commit(transactionBuilder.build());
                    } catch (IllegalStateException | SecurityException e3) {
                        Log.e("ThemeOverlayApplier", "setEnabled failed", e3);
                    }
                }
            });
        }
    }

    @Override // com.android.systemui.CoreStartable
    public final void start() {
        Log.d("ThemeOverlayController", "Start");
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MANAGED_PROFILE_ADDED");
        intentFilter.addAction("android.intent.action.WALLPAPER_CHANGED");
        this.mBroadcastDispatcher.registerReceiver(this.mBroadcastReceiver, intentFilter, this.mMainExecutor, UserHandle.ALL);
        this.mSecureSettings.registerContentObserverForUser(Settings.Secure.getUriFor("theme_customization_overlay_packages"), false, new ContentObserver(this.mBgHandler) { // from class: com.android.systemui.theme.ThemeOverlayController.5
            public final void onChange(boolean z, Collection<Uri> collection, int i, int i2) {
                ExifInterface$$ExternalSyntheticOutline1.m("Overlay changed for user: ", i2, "ThemeOverlayController");
                if (ThemeOverlayController.this.mUserTracker.getUserId() == i2) {
                    if (!ThemeOverlayController.this.mDeviceProvisionedController.isUserSetup(i2)) {
                        Log.i("ThemeOverlayController", "Theme application deferred when setting changed.");
                        ThemeOverlayController.this.mDeferredThemeEvaluation = true;
                        return;
                    }
                    ThemeOverlayController themeOverlayController = ThemeOverlayController.this;
                    if (themeOverlayController.mSkipSettingChange) {
                        Log.d("ThemeOverlayController", "Skipping setting change");
                        ThemeOverlayController.this.mSkipSettingChange = false;
                        return;
                    }
                    themeOverlayController.reevaluateSystemTheme(true);
                }
            }
        }, -1);
        if (this.mIsMonetEnabled) {
            this.mUserTracker.addCallback(this.mUserTrackerCallback, this.mMainExecutor);
            this.mDeviceProvisionedController.addCallback(this.mDeviceProvisionedListener);
            if (this.mIsMonetEnabled) {
                WifiEntry$$ExternalSyntheticLambda2 wifiEntry$$ExternalSyntheticLambda2 = new WifiEntry$$ExternalSyntheticLambda2(this, 10);
                if (!this.mDeviceProvisionedController.isCurrentUserSetup()) {
                    wifiEntry$$ExternalSyntheticLambda2.run();
                } else {
                    this.mBgExecutor.execute(wifiEntry$$ExternalSyntheticLambda2);
                }
                this.mWallpaperManager.addOnColorsChangedListener(this.mOnColorsChangedListener, null, -1);
                WakefulnessLifecycle wakefulnessLifecycle = this.mWakefulnessLifecycle;
                WakefulnessLifecycle.Observer observer = new WakefulnessLifecycle.Observer() { // from class: com.android.systemui.theme.ThemeOverlayController.6
                    @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
                    public final void onFinishedGoingToSleep() {
                        int userId = ThemeOverlayController.this.mUserTracker.getUserId();
                        WallpaperColors wallpaperColors = ThemeOverlayController.this.mDeferredWallpaperColors.get(userId);
                        if (wallpaperColors != null) {
                            int i = ThemeOverlayController.this.mDeferredWallpaperColorsFlags.get(userId);
                            ThemeOverlayController.this.mDeferredWallpaperColors.put(userId, null);
                            ThemeOverlayController.this.mDeferredWallpaperColorsFlags.put(userId, 0);
                            ThemeOverlayController.m116$$Nest$mhandleWallpaperColors(ThemeOverlayController.this, wallpaperColors, i, userId);
                        }
                    }
                };
                Objects.requireNonNull(wakefulnessLifecycle);
                wakefulnessLifecycle.mObservers.add(observer);
            }
        }
    }

    /* renamed from: -$$Nest$mhandleWallpaperColors  reason: not valid java name */
    public static void m116$$Nest$mhandleWallpaperColors(ThemeOverlayController themeOverlayController, WallpaperColors wallpaperColors, int i, int i2) {
        boolean z;
        int i3;
        boolean z2;
        JSONObject jSONObject;
        String str;
        String str2;
        boolean z3;
        Objects.requireNonNull(themeOverlayController);
        int userId = themeOverlayController.mUserTracker.getUserId();
        if (themeOverlayController.mCurrentColors.get(i2) != null) {
            z = true;
        } else {
            z = false;
        }
        if (themeOverlayController.mWallpaperManager.getWallpaperIdForUser(2, i2) > themeOverlayController.mWallpaperManager.getWallpaperIdForUser(1, i2)) {
            i3 = 2;
        } else {
            i3 = 1;
        }
        int i4 = i3 & i;
        if (i4 != 0) {
            themeOverlayController.mCurrentColors.put(i2, wallpaperColors);
            Log.d("ThemeOverlayController", "got new colors: " + wallpaperColors + " where: " + i);
        }
        if (i2 != userId) {
            StringBuilder sb = new StringBuilder();
            sb.append("Colors ");
            sb.append(wallpaperColors);
            sb.append(" for user ");
            sb.append(i2);
            sb.append(". Not for current user: ");
            KeyguardUpdateMonitor$$ExternalSyntheticOutline3.m(sb, userId, "ThemeOverlayController");
            return;
        }
        DeviceProvisionedController deviceProvisionedController = themeOverlayController.mDeviceProvisionedController;
        if (deviceProvisionedController != null && !deviceProvisionedController.isCurrentUserSetup()) {
            if (z) {
                Log.i("ThemeOverlayController", "Wallpaper color event deferred until setup is finished: " + wallpaperColors);
                themeOverlayController.mDeferredThemeEvaluation = true;
                return;
            } else if (themeOverlayController.mDeferredThemeEvaluation) {
                Log.i("ThemeOverlayController", "Wallpaper color event received, but we already were deferring eval: " + wallpaperColors);
                return;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("During user setup, but allowing first color event: had? ");
                sb2.append(z);
                sb2.append(" has? ");
                if (themeOverlayController.mCurrentColors.get(i2) != null) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                sb2.append(z3);
                Log.i("ThemeOverlayController", sb2.toString());
            }
        }
        String stringForUser = themeOverlayController.mSecureSettings.getStringForUser("theme_customization_overlay_packages", userId);
        if (i == 3) {
            z2 = true;
        } else {
            z2 = false;
        }
        try {
            if (stringForUser == null) {
                jSONObject = new JSONObject();
            } else {
                jSONObject = new JSONObject(stringForUser);
            }
            if (!"preset".equals(jSONObject.optString("android.theme.customization.color_source")) && i4 != 0 && !isSeedColorSet(jSONObject, wallpaperColors)) {
                themeOverlayController.mSkipSettingChange = true;
                if (jSONObject.has("android.theme.customization.accent_color") || jSONObject.has("android.theme.customization.system_palette")) {
                    jSONObject.remove("android.theme.customization.accent_color");
                    jSONObject.remove("android.theme.customization.system_palette");
                    jSONObject.remove("android.theme.customization.color_index");
                }
                if (z2) {
                    str = "1";
                } else {
                    str = "0";
                }
                jSONObject.put("android.theme.customization.color_both", str);
                if (i == 2) {
                    str2 = "lock_wallpaper";
                } else {
                    str2 = "home_wallpaper";
                }
                jSONObject.put("android.theme.customization.color_source", str2);
                jSONObject.put("_applied_timestamp", System.currentTimeMillis());
                Log.d("ThemeOverlayController", "Updating theme setting from " + stringForUser + " to " + jSONObject.toString());
                themeOverlayController.mSecureSettings.putString("theme_customization_overlay_packages", jSONObject.toString());
            }
        } catch (JSONException e) {
            Log.i("ThemeOverlayController", "Failed to parse THEME_CUSTOMIZATION_OVERLAY_PACKAGES.", e);
        }
        themeOverlayController.reevaluateSystemTheme(false);
    }

    /* JADX WARN: Type inference failed for: r1v10, types: [com.android.systemui.theme.ThemeOverlayController$4] */
    /* JADX WARN: Type inference failed for: r1v7, types: [com.android.systemui.theme.ThemeOverlayController$1] */
    /* JADX WARN: Type inference failed for: r1v8, types: [com.android.systemui.theme.ThemeOverlayController$2] */
    /* JADX WARN: Type inference failed for: r1v9, types: [com.android.systemui.theme.ThemeOverlayController$3] */
    public ThemeOverlayController(Context context, BroadcastDispatcher broadcastDispatcher, Handler handler, Executor executor, Executor executor2, ThemeOverlayApplier themeOverlayApplier, SecureSettings secureSettings, WallpaperManager wallpaperManager, UserManager userManager, DeviceProvisionedController deviceProvisionedController, UserTracker userTracker, DumpManager dumpManager, FeatureFlags featureFlags, WakefulnessLifecycle wakefulnessLifecycle) {
        super(context);
        this.mIsMonetEnabled = featureFlags.isEnabled(Flags.MONET);
        this.mDeviceProvisionedController = deviceProvisionedController;
        this.mBroadcastDispatcher = broadcastDispatcher;
        this.mUserManager = userManager;
        this.mBgExecutor = executor2;
        this.mMainExecutor = executor;
        this.mBgHandler = handler;
        this.mThemeManager = themeOverlayApplier;
        this.mSecureSettings = secureSettings;
        this.mWallpaperManager = wallpaperManager;
        this.mUserTracker = userTracker;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        dumpManager.registerDumpable("ThemeOverlayController", this);
    }
}
