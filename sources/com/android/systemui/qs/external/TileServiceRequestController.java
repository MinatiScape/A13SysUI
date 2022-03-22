package com.android.systemui.qs.external;

import android.content.ComponentName;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.RemoteException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.android.internal.logging.InstanceId;
import com.android.internal.statusbar.IAddTileResultCallback;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.tileimpl.QSIconViewImpl;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import com.android.systemui.qs.tileimpl.QSTileViewImpl;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.commandline.Command;
import com.android.systemui.statusbar.commandline.CommandRegistry;
import com.android.systemui.statusbar.phone.SystemUIDialog;
import java.io.PrintWriter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: TileServiceRequestController.kt */
/* loaded from: classes.dex */
public final class TileServiceRequestController {
    public final CommandQueue commandQueue;
    public final TileServiceRequestController$commandQueueCallback$1 commandQueueCallback;
    public final CommandRegistry commandRegistry;
    public Function1<? super String, Unit> dialogCanceller;
    public final Function0<TileRequestDialog> dialogCreator;
    public final TileRequestDialogEventLogger eventLogger;
    public final QSTileHost qsTileHost;

    /* compiled from: TileServiceRequestController.kt */
    /* renamed from: com.android.systemui.qs.external.TileServiceRequestController$1  reason: invalid class name */
    /* loaded from: classes.dex */
    final class AnonymousClass1 extends Lambda implements Function0<TileRequestDialog> {
        public final /* synthetic */ QSTileHost $qsTileHost;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        public AnonymousClass1(QSTileHost qSTileHost) {
            super(0);
            this.$qsTileHost = qSTileHost;
        }

        @Override // kotlin.jvm.functions.Function0
        public final TileRequestDialog invoke() {
            QSTileHost qSTileHost = this.$qsTileHost;
            Objects.requireNonNull(qSTileHost);
            return new TileRequestDialog(qSTileHost.mContext);
        }
    }

    /* compiled from: TileServiceRequestController.kt */
    /* loaded from: classes.dex */
    public static final class SingleShotConsumer<T> implements Consumer<T> {
        public final Consumer<T> consumer;
        public final AtomicBoolean dispatched = new AtomicBoolean(false);

        @Override // java.util.function.Consumer
        public final void accept(T t) {
            if (this.dispatched.compareAndSet(false, true)) {
                this.consumer.accept(t);
            }
        }

        public SingleShotConsumer(TileServiceRequestController$requestTileAdd$dialogResponse$1 tileServiceRequestController$requestTileAdd$dialogResponse$1) {
            this.consumer = tileServiceRequestController$requestTileAdd$dialogResponse$1;
        }
    }

    /* compiled from: TileServiceRequestController.kt */
    /* loaded from: classes.dex */
    public final class TileServiceRequestCommand implements Command {
        @Override // com.android.systemui.statusbar.commandline.Command
        public final void execute(PrintWriter printWriter, List<String> list) {
            ComponentName unflattenFromString = ComponentName.unflattenFromString(list.get(0));
            if (unflattenFromString == null) {
                Log.w("TileServiceRequestController", Intrinsics.stringPlus("Malformed componentName ", list.get(0)));
            } else {
                TileServiceRequestController.this.requestTileAdd$frameworks__base__packages__SystemUI__android_common__SystemUI_core(unflattenFromString, list.get(1), list.get(2), null, TileServiceRequestController$TileServiceRequestCommand$execute$1.INSTANCE);
            }
        }

        public TileServiceRequestCommand() {
        }
    }

    public TileServiceRequestController() {
        throw null;
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [com.android.systemui.qs.external.TileServiceRequestController$commandQueueCallback$1] */
    public TileServiceRequestController(QSTileHost qSTileHost, CommandQueue commandQueue, CommandRegistry commandRegistry, TileRequestDialogEventLogger tileRequestDialogEventLogger) {
        AnonymousClass1 r0 = new AnonymousClass1(qSTileHost);
        this.qsTileHost = qSTileHost;
        this.commandQueue = commandQueue;
        this.commandRegistry = commandRegistry;
        this.eventLogger = tileRequestDialogEventLogger;
        this.dialogCreator = r0;
        this.commandQueueCallback = new CommandQueue.Callbacks() { // from class: com.android.systemui.qs.external.TileServiceRequestController$commandQueueCallback$1
            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public final void cancelRequestAddTile(String str) {
                Function1<? super String, Unit> function1 = TileServiceRequestController.this.dialogCanceller;
                if (function1 != null) {
                    function1.invoke(str);
                }
            }

            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public final void requestAddTile(ComponentName componentName, CharSequence charSequence, CharSequence charSequence2, Icon icon, final IAddTileResultCallback iAddTileResultCallback) {
                TileServiceRequestController.this.requestTileAdd$frameworks__base__packages__SystemUI__android_common__SystemUI_core(componentName, charSequence, charSequence2, icon, new Consumer() { // from class: com.android.systemui.qs.external.TileServiceRequestController$commandQueueCallback$1$requestAddTile$1
                    @Override // java.util.function.Consumer
                    public final void accept(Object obj) {
                        try {
                            iAddTileResultCallback.onTileRequest(((Number) obj).intValue());
                        } catch (RemoteException e) {
                            Log.e("TileServiceRequestController", "Couldn't respond to request", e);
                        }
                    }
                });
            }
        };
    }

    /* compiled from: TileServiceRequestController.kt */
    /* loaded from: classes.dex */
    public static final class Builder {
        public final CommandQueue commandQueue;
        public final CommandRegistry commandRegistry;

        public Builder(CommandQueue commandQueue, CommandRegistry commandRegistry) {
            this.commandQueue = commandQueue;
            this.commandRegistry = commandRegistry;
        }
    }

    public final void init() {
        this.commandRegistry.registerCommand("tile-service-add", new TileServiceRequestController$init$1(this));
        this.commandQueue.addCallback((CommandQueue.Callbacks) this.commandQueueCallback);
    }

    /* JADX WARN: Type inference failed for: r13v0, types: [com.android.systemui.qs.external.TileServiceRequestController$requestTileAdd$dialogResponse$1] */
    public final void requestTileAdd$frameworks__base__packages__SystemUI__android_common__SystemUI_core(final ComponentName componentName, CharSequence charSequence, CharSequence charSequence2, Icon icon, final Consumer<Integer> consumer) {
        boolean z;
        Drawable loadDrawable;
        TileRequestDialogEventLogger tileRequestDialogEventLogger = this.eventLogger;
        Objects.requireNonNull(tileRequestDialogEventLogger);
        final InstanceId newInstanceId = tileRequestDialogEventLogger.instanceIdSequence.newInstanceId();
        final String packageName = componentName.getPackageName();
        if (this.qsTileHost.indexOf(CustomTile.toSpec(componentName)) != -1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            consumer.accept(1);
            TileRequestDialogEventLogger tileRequestDialogEventLogger2 = this.eventLogger;
            Objects.requireNonNull(tileRequestDialogEventLogger2);
            tileRequestDialogEventLogger2.uiEventLogger.logWithInstanceId(TileRequestDialogEvent.TILE_REQUEST_DIALOG_TILE_ALREADY_ADDED, 0, packageName, newInstanceId);
            return;
        }
        final SingleShotConsumer singleShotConsumer = new SingleShotConsumer(new Consumer() { // from class: com.android.systemui.qs.external.TileServiceRequestController$requestTileAdd$dialogResponse$1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                TileRequestDialogEvent tileRequestDialogEvent;
                int intValue = ((Number) obj).intValue();
                if (intValue == 2) {
                    TileServiceRequestController tileServiceRequestController = TileServiceRequestController.this;
                    ComponentName componentName2 = componentName;
                    Objects.requireNonNull(tileServiceRequestController);
                    tileServiceRequestController.qsTileHost.addTile(componentName2, true);
                }
                TileServiceRequestController tileServiceRequestController2 = TileServiceRequestController.this;
                tileServiceRequestController2.dialogCanceller = null;
                TileRequestDialogEventLogger tileRequestDialogEventLogger3 = tileServiceRequestController2.eventLogger;
                String str = packageName;
                InstanceId instanceId = newInstanceId;
                if (intValue == 0) {
                    tileRequestDialogEvent = TileRequestDialogEvent.TILE_REQUEST_DIALOG_TILE_NOT_ADDED;
                } else if (intValue == 2) {
                    tileRequestDialogEvent = TileRequestDialogEvent.TILE_REQUEST_DIALOG_TILE_ADDED;
                } else if (intValue == 3) {
                    tileRequestDialogEvent = TileRequestDialogEvent.TILE_REQUEST_DIALOG_DISMISSED;
                } else {
                    Objects.requireNonNull(tileRequestDialogEventLogger3);
                    throw new IllegalArgumentException(Intrinsics.stringPlus("User response not valid: ", Integer.valueOf(intValue)));
                }
                tileRequestDialogEventLogger3.uiEventLogger.logWithInstanceId(tileRequestDialogEvent, 0, str, instanceId);
                consumer.accept(Integer.valueOf(intValue));
            }
        });
        DialogInterface.OnClickListener tileServiceRequestController$createDialog$dialogClickListener$1 = new DialogInterface.OnClickListener() { // from class: com.android.systemui.qs.external.TileServiceRequestController$createDialog$dialogClickListener$1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                if (i == -1) {
                    singleShotConsumer.accept(2);
                } else {
                    singleShotConsumer.accept(0);
                }
            }
        };
        TileRequestDialog invoke = this.dialogCreator.invoke();
        TileRequestDialog tileRequestDialog = invoke;
        Objects.requireNonNull(tileRequestDialog);
        QSTile.Icon icon2 = null;
        View inflate = LayoutInflater.from(tileRequestDialog.getContext()).inflate(2131624623, (ViewGroup) null);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        ViewGroup viewGroup = (ViewGroup) inflate;
        TextView textView = (TextView) viewGroup.requireViewById(2131429024);
        textView.setText(textView.getContext().getString(2131953051, charSequence));
        final QSTileViewImpl qSTileViewImpl = new QSTileViewImpl(tileRequestDialog.getContext(), new QSIconViewImpl(tileRequestDialog.getContext()), true);
        QSTile.BooleanState booleanState = new QSTile.BooleanState();
        booleanState.label = charSequence2;
        booleanState.handlesLongClick = false;
        if (!(icon == null || (loadDrawable = icon.loadDrawable(tileRequestDialog.getContext())) == null)) {
            icon2 = new QSTileImpl.DrawableIcon(loadDrawable);
        }
        if (icon2 == null) {
            icon2 = QSTileImpl.ResourceIcon.get(2131231597);
        }
        booleanState.icon = icon2;
        qSTileViewImpl.onStateChanged(booleanState);
        qSTileViewImpl.post(new Runnable() { // from class: com.android.systemui.qs.external.TileRequestDialog$createTileView$1
            @Override // java.lang.Runnable
            public final void run() {
                QSTileViewImpl.this.setStateDescription("");
                QSTileViewImpl.this.setClickable(false);
                QSTileViewImpl.this.setSelected(true);
            }
        });
        viewGroup.addView(qSTileViewImpl, viewGroup.getContext().getResources().getDimensionPixelSize(2131166918), viewGroup.getContext().getResources().getDimensionPixelSize(2131166903));
        viewGroup.setSelected(true);
        tileRequestDialog.setView(viewGroup, 0, 0, 0, 0);
        SystemUIDialog.setShowForAllUsers(tileRequestDialog);
        tileRequestDialog.setCanceledOnTouchOutside(true);
        tileRequestDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.android.systemui.qs.external.TileServiceRequestController$createDialog$1$1
            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                singleShotConsumer.accept(3);
            }
        });
        tileRequestDialog.setOnDismissListener(new DialogInterface.OnDismissListener() { // from class: com.android.systemui.qs.external.TileServiceRequestController$createDialog$1$2
            @Override // android.content.DialogInterface.OnDismissListener
            public final void onDismiss(DialogInterface dialogInterface) {
                singleShotConsumer.accept(3);
            }
        });
        tileRequestDialog.setPositiveButton(2131953049, tileServiceRequestController$createDialog$dialogClickListener$1);
        tileRequestDialog.setNegativeButton(2131953050, tileServiceRequestController$createDialog$dialogClickListener$1);
        TileRequestDialog tileRequestDialog2 = invoke;
        this.dialogCanceller = new TileServiceRequestController$requestTileAdd$1$1(packageName, tileRequestDialog2, this);
        tileRequestDialog2.show();
        TileRequestDialogEventLogger tileRequestDialogEventLogger3 = this.eventLogger;
        Objects.requireNonNull(tileRequestDialogEventLogger3);
        tileRequestDialogEventLogger3.uiEventLogger.logWithInstanceId(TileRequestDialogEvent.TILE_REQUEST_DIALOG_SHOWN, 0, packageName, newInstanceId);
    }
}
