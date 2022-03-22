package com.android.systemui.qs.user;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.animation.AnimatedDialog;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.animation.DialogLaunchAnimator$createActivityLaunchController$1;
import com.android.systemui.animation.LaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.PseudoGridView;
import com.android.systemui.qs.QSUserSwitcherEvent;
import com.android.systemui.qs.tiles.UserDetailView;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.util.Iterator;
import java.util.Objects;
import javax.inject.Provider;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: UserSwitchDialogController.kt */
/* loaded from: classes.dex */
public final class UserSwitchDialogController {
    public static final Intent USER_SETTINGS_INTENT = new Intent("android.settings.USER_SETTINGS");
    public final ActivityStarter activityStarter;
    public final Function1<Context, SystemUIDialog> dialogFactory;
    public final DialogLaunchAnimator dialogLaunchAnimator;
    public final FalsingManager falsingManager;
    public final UiEventLogger uiEventLogger;
    public final Provider<UserDetailView.Adapter> userDetailViewAdapterProvider;

    /* compiled from: UserSwitchDialogController.kt */
    /* renamed from: com.android.systemui.qs.user.UserSwitchDialogController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    final class AnonymousClass1 extends Lambda implements Function1<Context, SystemUIDialog> {
        public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

        public AnonymousClass1() {
            super(1);
        }

        @Override // kotlin.jvm.functions.Function1
        public final SystemUIDialog invoke(Context context) {
            return new SystemUIDialog(context);
        }
    }

    /* compiled from: UserSwitchDialogController.kt */
    /* loaded from: classes.dex */
    public interface DialogShower extends DialogInterface {
        void showDialog(SystemUIDialog systemUIDialog);
    }

    /* compiled from: UserSwitchDialogController.kt */
    /* loaded from: classes.dex */
    public static final class DialogShowerImpl implements DialogInterface, DialogShower {
        public final Dialog animateFrom;
        public final DialogLaunchAnimator dialogLaunchAnimator;

        @Override // android.content.DialogInterface
        public final void cancel() {
            this.animateFrom.cancel();
        }

        @Override // android.content.DialogInterface
        public final void dismiss() {
            this.animateFrom.dismiss();
        }

        @Override // com.android.systemui.qs.user.UserSwitchDialogController.DialogShower
        public final void showDialog(SystemUIDialog systemUIDialog) {
            ViewGroup viewGroup;
            AnimatedDialog animatedDialog;
            DialogLaunchAnimator dialogLaunchAnimator = this.dialogLaunchAnimator;
            Dialog dialog = this.animateFrom;
            LaunchAnimator.Timings timings = DialogLaunchAnimator.TIMINGS;
            Objects.requireNonNull(dialogLaunchAnimator);
            Iterator<AnimatedDialog> it = dialogLaunchAnimator.openedDialogs.iterator();
            while (true) {
                viewGroup = null;
                if (!it.hasNext()) {
                    animatedDialog = null;
                    break;
                }
                animatedDialog = it.next();
                AnimatedDialog animatedDialog2 = animatedDialog;
                Objects.requireNonNull(animatedDialog2);
                if (Intrinsics.areEqual(animatedDialog2.dialog, dialog)) {
                    break;
                }
            }
            AnimatedDialog animatedDialog3 = animatedDialog;
            if (animatedDialog3 != null) {
                viewGroup = animatedDialog3.dialogContentWithBackground;
            }
            if (viewGroup != null) {
                dialogLaunchAnimator.showFromView(systemUIDialog, viewGroup, false);
                return;
            }
            throw new IllegalStateException("The animateFrom dialog was not animated using DialogLaunchAnimator.showFrom(View|Dialog)");
        }

        public DialogShowerImpl(SystemUIDialog systemUIDialog, DialogLaunchAnimator dialogLaunchAnimator) {
            this.animateFrom = systemUIDialog;
            this.dialogLaunchAnimator = dialogLaunchAnimator;
        }
    }

    public UserSwitchDialogController() {
        throw null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public UserSwitchDialogController(Provider<UserDetailView.Adapter> provider, ActivityStarter activityStarter, FalsingManager falsingManager, DialogLaunchAnimator dialogLaunchAnimator, UiEventLogger uiEventLogger, Function1<? super Context, ? extends SystemUIDialog> function1) {
        this.userDetailViewAdapterProvider = provider;
        this.activityStarter = activityStarter;
        this.falsingManager = falsingManager;
        this.dialogLaunchAnimator = dialogLaunchAnimator;
        this.uiEventLogger = uiEventLogger;
        this.dialogFactory = function1;
    }

    public final void showDialog(View view) {
        final SystemUIDialog invoke = this.dialogFactory.invoke(view.getContext());
        Objects.requireNonNull(invoke);
        SystemUIDialog.setShowForAllUsers(invoke);
        invoke.setCanceledOnTouchOutside(true);
        invoke.setTitle(2131953052);
        invoke.setPositiveButton(2131953108, new DialogInterface.OnClickListener() { // from class: com.android.systemui.qs.user.UserSwitchDialogController$showDialog$1$1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                UserSwitchDialogController.this.uiEventLogger.log(QSUserSwitcherEvent.QS_USER_DETAIL_CLOSE);
            }
        });
        invoke.setButton(-3, 2131953120, new DialogInterface.OnClickListener() { // from class: com.android.systemui.qs.user.UserSwitchDialogController$showDialog$1$2
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                if (!UserSwitchDialogController.this.falsingManager.isFalseTap(1)) {
                    UserSwitchDialogController.this.uiEventLogger.log(QSUserSwitcherEvent.QS_USER_MORE_SETTINGS);
                    DialogLaunchAnimator$createActivityLaunchController$1 createActivityLaunchController$default = DialogLaunchAnimator.createActivityLaunchController$default(UserSwitchDialogController.this.dialogLaunchAnimator, invoke.getButton(-3));
                    if (createActivityLaunchController$default == null) {
                        invoke.dismiss();
                    }
                    UserSwitchDialogController.this.activityStarter.postStartActivityDismissingKeyguard(UserSwitchDialogController.USER_SETTINGS_INTENT, 0, createActivityLaunchController$default);
                }
            }
        }, false);
        View inflate = LayoutInflater.from(invoke.getContext()).inflate(2131624435, (ViewGroup) null);
        invoke.setView(inflate);
        UserDetailView.Adapter adapter = this.userDetailViewAdapterProvider.mo144get();
        Objects.requireNonNull(adapter);
        new PseudoGridView.ViewGroupAdapterBridge((ViewGroup) inflate.findViewById(2131428041), adapter);
        DialogLaunchAnimator dialogLaunchAnimator = this.dialogLaunchAnimator;
        LaunchAnimator.Timings timings = DialogLaunchAnimator.TIMINGS;
        dialogLaunchAnimator.showFromView(invoke, view, false);
        this.uiEventLogger.log(QSUserSwitcherEvent.QS_USER_DETAIL_OPEN);
        adapter.mDialogShower = new DialogShowerImpl(invoke, this.dialogLaunchAnimator);
    }
}
