package com.android.settingslib.wifi;

import android.content.Context;
import android.graphics.Typeface;
import android.os.RemoteException;
import android.os.Trace;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.graphics.TypefaceCompatUtil;
import androidx.core.provider.FontsContractCompat$FontInfo;
import androidx.emoji2.text.EmojiCompat;
import androidx.emoji2.text.FontRequestEmojiCompatConfig;
import androidx.emoji2.text.MetadataListReader;
import androidx.emoji2.text.MetadataRepo;
import com.android.keyguard.KeyguardClockSwitchController;
import com.android.systemui.dreams.DreamOverlayContainerViewController;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qrcodescanner.controller.QRCodeScannerController;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.shared.rotation.FloatingRotationButton;
import com.android.systemui.shared.rotation.RotationButton;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wm.shell.pip.phone.PipDismissTargetHandler;
import com.android.wm.shell.splitscreen.MainStage;
import com.android.wm.shell.splitscreen.StageCoordinator;
import java.nio.MappedByteBuffer;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AccessPoint$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ AccessPoint$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback;
        switch (this.$r8$classId) {
            case 0:
                Objects.requireNonNull((AccessPoint) this.f$0);
                return;
            case 1:
                FontRequestEmojiCompatConfig.FontRequestMetadataLoader fontRequestMetadataLoader = (FontRequestEmojiCompatConfig.FontRequestMetadataLoader) this.f$0;
                Objects.requireNonNull(fontRequestMetadataLoader);
                synchronized (fontRequestMetadataLoader.mLock) {
                    if (fontRequestMetadataLoader.mCallback != null) {
                        try {
                            FontsContractCompat$FontInfo retrieveFontInfo = fontRequestMetadataLoader.retrieveFontInfo();
                            Objects.requireNonNull(retrieveFontInfo);
                            int i = retrieveFontInfo.mResultCode;
                            if (i == 2) {
                                synchronized (fontRequestMetadataLoader.mLock) {
                                }
                            }
                            if (i == 0) {
                                Trace.beginSection("EmojiCompat.FontRequestEmojiCompatConfig.buildTypeface");
                                FontRequestEmojiCompatConfig.FontProviderHelper fontProviderHelper = fontRequestMetadataLoader.mFontProviderHelper;
                                Context context = fontRequestMetadataLoader.mContext;
                                Objects.requireNonNull(fontProviderHelper);
                                Typeface createFromFontInfo = TypefaceCompat.createFromFontInfo(context, new FontsContractCompat$FontInfo[]{retrieveFontInfo}, 0);
                                MappedByteBuffer mmap = TypefaceCompatUtil.mmap(fontRequestMetadataLoader.mContext, retrieveFontInfo.mUri);
                                if (mmap == null || createFromFontInfo == null) {
                                    throw new RuntimeException("Unable to open file.");
                                }
                                Trace.beginSection("EmojiCompat.MetadataRepo.create");
                                MetadataRepo metadataRepo = new MetadataRepo(createFromFontInfo, MetadataListReader.read(mmap));
                                Trace.endSection();
                                Trace.endSection();
                                synchronized (fontRequestMetadataLoader.mLock) {
                                    EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback = fontRequestMetadataLoader.mCallback;
                                    if (metadataRepoLoaderCallback != null) {
                                        metadataRepoLoaderCallback.onLoaded(metadataRepo);
                                    }
                                }
                                fontRequestMetadataLoader.cleanUp();
                                return;
                            }
                            throw new RuntimeException("fetchFonts result is not OK. (" + i + ")");
                        } catch (Throwable th) {
                            synchronized (fontRequestMetadataLoader.mLock) {
                                EmojiCompat.MetadataRepoLoaderCallback metadataRepoLoaderCallback2 = fontRequestMetadataLoader.mCallback;
                                if (metadataRepoLoaderCallback2 != null) {
                                    metadataRepoLoaderCallback2.onFailed(th);
                                }
                                fontRequestMetadataLoader.cleanUp();
                                return;
                            }
                        }
                    } else {
                        return;
                    }
                }
            case 2:
                KeyguardClockSwitchController keyguardClockSwitchController = (KeyguardClockSwitchController) this.f$0;
                Objects.requireNonNull(keyguardClockSwitchController);
                keyguardClockSwitchController.displayClock(1, true);
                return;
            case 3:
                DreamOverlayContainerViewController.$r8$lambda$Oxvj_GJUc06UJC_m7GrRwIKFrUA((DreamOverlayContainerViewController) this.f$0);
                return;
            case 4:
                QRCodeScannerController qRCodeScannerController = (QRCodeScannerController) this.f$0;
                Objects.requireNonNull(qRCodeScannerController);
                qRCodeScannerController.updateQRCodeScannerPreferenceDetails(true);
                return;
            case 5:
                CustomTile customTile = (CustomTile) this.f$0;
                int i2 = CustomTile.$r8$clinit;
                Objects.requireNonNull(customTile);
                try {
                    customTile.mService.onUnlockComplete();
                    return;
                } catch (RemoteException unused) {
                    return;
                }
            case FalsingManager.VERSION /* 6 */:
                FloatingRotationButton floatingRotationButton = (FloatingRotationButton) this.f$0;
                Objects.requireNonNull(floatingRotationButton);
                if (floatingRotationButton.mIsShowing && (rotationButtonUpdatesCallback = floatingRotationButton.mUpdatesCallback) != null) {
                    ((NavigationBarView.AnonymousClass2) rotationButtonUpdatesCallback).onVisibilityChanged(true);
                    return;
                }
                return;
            case 7:
                NetworkControllerImpl networkControllerImpl = (NetworkControllerImpl) this.f$0;
                boolean z = NetworkControllerImpl.DEBUG;
                Objects.requireNonNull(networkControllerImpl);
                if (networkControllerImpl.mLastServiceState == null) {
                    networkControllerImpl.mLastServiceState = networkControllerImpl.mPhone.getServiceState();
                    if (networkControllerImpl.mMobileSignalControllers.size() == 0) {
                        networkControllerImpl.recalculateEmergency();
                        return;
                    }
                    return;
                }
                return;
            case 8:
                PhoneStatusBarPolicy.AnonymousClass1 r4 = (PhoneStatusBarPolicy.AnonymousClass1) this.f$0;
                int i3 = PhoneStatusBarPolicy.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r4);
                PhoneStatusBarPolicy.this.updateAlarm();
                PhoneStatusBarPolicy.this.updateManagedProfile();
                return;
            case 9:
                int i4 = LocationControllerImpl.$r8$clinit;
                ((LocationControllerImpl) this.f$0).updateActiveLocationRequests();
                return;
            case 10:
                WifiEntry.ConnectActionListener connectActionListener = (WifiEntry.ConnectActionListener) this.f$0;
                Objects.requireNonNull(connectActionListener);
                WifiEntry.ConnectCallback connectCallback = WifiEntry.this.mConnectCallback;
                if (connectCallback != null) {
                    ((InternetDialogController.WifiEntryConnectCallback) connectCallback).onConnectResult(2);
                    return;
                }
                return;
            case QSTileImpl.H.STALE /* 11 */:
                PipDismissTargetHandler pipDismissTargetHandler = (PipDismissTargetHandler) this.f$0;
                Objects.requireNonNull(pipDismissTargetHandler);
                pipDismissTargetHandler.mTargetViewContainer.setVisibility(8);
                return;
            default:
                StageCoordinator stageCoordinator = (StageCoordinator) this.f$0;
                Objects.requireNonNull(stageCoordinator);
                MainStage mainStage = stageCoordinator.mMainStage;
                Objects.requireNonNull(mainStage);
                if (!mainStage.mIsActive) {
                    stageCoordinator.mSplitLayout.release();
                    stageCoordinator.mSplitLayout.resetDividerPosition();
                    stageCoordinator.mTopStageAfterFoldDismiss = -1;
                    return;
                }
                return;
        }
    }
}
