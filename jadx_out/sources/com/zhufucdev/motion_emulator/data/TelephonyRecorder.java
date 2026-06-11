package com.zhufucdev.motion_emulator.data;

import android.content.Context;
import android.os.Build;
import android.telephony.CellInfo;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.zhufucdev.me.stub.CellMoment;
import com.zhufucdev.me.stub.CellTimeline;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import kotlin.Deprecated;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.concurrent.TimersKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KFunction;
import kotlin.reflect.full.KClasses;
import kotlin.text.StringsKt;

/* JADX INFO: compiled from: TelephonyRecorder.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u0000L\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000*\u0002\u0007\u0016\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\u000b\u001a\u00020\f2\u0006\u0010\r\u001a\u00020\u000eJ(\u0010\u000f\u001a\u00020\u00102\u0016\u0010\u0011\u001a\u0012\u0012\u0004\u0012\u00020\u00100\u0012j\b\u0012\u0004\u0012\u00020\u0010`\u00132\u0006\u0010\u0014\u001a\u00020\u0010H\u0002J\r\u0010\u0015\u001a\u00020\u0016H\u0002¢\u0006\u0002\u0010\u0017J\u0006\u0010\u0018\u001a\u00020\u0019R\u0014\u0010\u0003\u001a\b\u0012\u0004\u0012\u00020\u00050\u0004X\u0082.¢\u0006\u0002\n\u0000R\u0010\u0010\u0006\u001a\u00020\u0007X\u0082\u0004¢\u0006\u0004\n\u0002\u0010\bR\u000e\u0010\t\u001a\u00020\nX\u0082.¢\u0006\u0002\n\u0000¨\u0006\u001a"}, d2 = {"Lcom/zhufucdev/motion_emulator/data/TelephonyRecorder;", "", "()V", "checkPermission", "Lkotlin/Function0;", "", "mainExecutor", "com/zhufucdev/motion_emulator/data/TelephonyRecorder$mainExecutor$1", "Lcom/zhufucdev/motion_emulator/data/TelephonyRecorder$mainExecutor$1;", "manager", "Landroid/telephony/TelephonyManager;", "init", "", "context", "Landroid/content/Context;", "mergeIfPossible", "Lcom/zhufucdev/me/stub/CellMoment;", "timeline", "Ljava/util/ArrayList;", "Lkotlin/collections/ArrayList;", "moment", "noop", "com/zhufucdev/motion_emulator/data/TelephonyRecorder$noop$1", "()Lcom/zhufucdev/motion_emulator/data/TelephonyRecorder$noop$1;", "start", "Lcom/zhufucdev/motion_emulator/data/TelephonyRecordCallback;", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class TelephonyRecorder {
    private static Function0<Boolean> checkPermission;
    private static TelephonyManager manager;
    public static final TelephonyRecorder INSTANCE = new TelephonyRecorder();
    private static final TelephonyRecorder$mainExecutor$1 mainExecutor = new TelephonyRecorder$mainExecutor$1();
    public static final int $stable = 8;

    private TelephonyRecorder() {
    }

    public final void init(final Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Object systemService = context.getSystemService((Class<Object>) TelephonyManager.class);
        Intrinsics.checkNotNullExpressionValue(systemService, "getSystemService(...)");
        manager = (TelephonyManager) systemService;
        checkPermission = new Function0<Boolean>() { // from class: com.zhufucdev.motion_emulator.data.TelephonyRecorder.init.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(0);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // kotlin.jvm.functions.Function0
            public final Boolean invoke() {
                return Boolean.valueOf(context.checkCallingOrSelfPermission("android.permission.READ_PHONE_STATE") == 0 && context.checkCallingOrSelfPermission("android.permission.ACCESS_FINE_LOCATION") == 0);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public final CellMoment mergeIfPossible(ArrayList<CellMoment> timeline, CellMoment moment) {
        synchronized (TelephonyRecorder.class) {
            if ((!timeline.isEmpty()) && Math.abs(moment.getElapsed() - ((CellMoment) CollectionsKt.last((List) timeline)).getElapsed()) <= 0.05f) {
                CellMoment last = (CellMoment) CollectionsKt.last((List) timeline);
                if (!TelephonyRecorderKt.isSameTypeOf(last, moment)) {
                    CollectionsKt.removeLast(timeline);
                    CellMoment merged = TelephonyRecorderKt.merge(last, moment);
                    timeline.add(merged);
                    return merged;
                }
            }
            timeline.add(moment);
            return moment;
        }
    }

    /* JADX WARN: Type inference failed for: r15v0, types: [T, java.util.Timer] */
    /* JADX WARN: Type inference failed for: r2v2, types: [com.zhufucdev.motion_emulator.data.TelephonyRecorder$start$listener$1] */
    public final TelephonyRecordCallback start() {
        TelephonyManager telephonyManager;
        Function0<Unit> function0;
        Object element$iv;
        TelephonyManager telephonyManager2;
        Function0<Boolean> function02 = checkPermission;
        if (function02 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("checkPermission");
            function02 = null;
        }
        if (!function02.invoke().booleanValue()) {
            return noop();
        }
        final long start = System.currentTimeMillis();
        final ArrayList timeline = new ArrayList();
        final Ref.ObjectRef updateListener = new Ref.ObjectRef();
        Function0 cancel = null;
        if (Build.VERSION.SDK_INT >= 31) {
            final TelephonyRecorder$start$telephonyCallback$1 telephonyCallback = new TelephonyRecorder$start$telephonyCallback$1(timeline, updateListener, start);
            TelephonyManager telephonyManager3 = manager;
            if (telephonyManager3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("manager");
                telephonyManager2 = null;
            } else {
                telephonyManager2 = telephonyManager3;
            }
            telephonyManager2.registerTelephonyCallback(mainExecutor, telephonyCallback);
            function0 = new Function0<Unit>() { // from class: com.zhufucdev.motion_emulator.data.TelephonyRecorder.start.1
                {
                    super(0);
                }

                @Override // kotlin.jvm.functions.Function0
                public /* bridge */ /* synthetic */ Unit invoke() {
                    invoke2();
                    return Unit.INSTANCE;
                }

                /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2() {
                    TelephonyManager telephonyManager4 = TelephonyRecorder.manager;
                    if (telephonyManager4 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("manager");
                        telephonyManager4 = null;
                    }
                    telephonyManager4.unregisterTelephonyCallback(telephonyCallback);
                }
            };
        } else {
            final ?? r2 = new PhoneStateListener() { // from class: com.zhufucdev.motion_emulator.data.TelephonyRecorder$start$listener$1
                @Override // android.telephony.PhoneStateListener
                @Deprecated(message = "Deprecated in Java")
                public void onCellInfoChanged(List<CellInfo> cellInfo) {
                    if (cellInfo != null) {
                        CellMoment original = new CellMoment(TelephonyRecorder.start$elapsed(start), cellInfo, null, null, 12, null);
                        TelephonyRecorder.INSTANCE.mergeIfPossible(timeline, original);
                        Function1<CellMoment, Unit> function1 = updateListener.element;
                        if (function1 != null) {
                            function1.invoke(original);
                        }
                    }
                }

                @Override // android.telephony.PhoneStateListener
                @Deprecated(message = "Deprecated in Java")
                public void onCellLocationChanged(CellLocation location) {
                    if (location != null) {
                        CellMoment moment = TelephonyRecorder.INSTANCE.mergeIfPossible(timeline, new CellMoment(TelephonyRecorder.start$elapsed(start), null, null, location, 6, null));
                        Function1<CellMoment, Unit> function1 = updateListener.element;
                        if (function1 != null) {
                            function1.invoke(moment);
                        }
                    }
                }
            };
            final Ref.ObjectRef timer = new Ref.ObjectRef();
            if (Build.VERSION.SDK_INT < 29) {
                Iterable $this$firstOrNull$iv = KClasses.getMemberFunctions(Reflection.getOrCreateKotlinClass(TelephonyManager.class));
                int $i$f$firstOrNull = 0;
                Iterator it = $this$firstOrNull$iv.iterator();
                while (true) {
                    if (!it.hasNext()) {
                        telephonyManager = null;
                        element$iv = null;
                        break;
                    }
                    element$iv = it.next();
                    KFunction it2 = (KFunction) element$iv;
                    Iterable $this$firstOrNull$iv2 = $this$firstOrNull$iv;
                    int $i$f$firstOrNull2 = $i$f$firstOrNull;
                    Function0 cancel2 = cancel;
                    telephonyManager = null;
                    if (StringsKt.startsWith$default(it2.getName(), "getNeighboringCellInfo", false, 2, (Object) null)) {
                        break;
                    }
                    $this$firstOrNull$iv = $this$firstOrNull$iv2;
                    $i$f$firstOrNull = $i$f$firstOrNull2;
                    cancel = cancel2;
                }
                final KFunction method = (KFunction) element$iv;
                if (method == null) {
                    Log.w("telephony recorder", "method to get neighboring cell info isn't available");
                } else {
                    ?? Timer = TimersKt.timer("neighboring daemon", false);
                    Timer.schedule(new TimerTask() { // from class: com.zhufucdev.motion_emulator.data.TelephonyRecorder$start$$inlined$timer$default$1
                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            KFunction kFunction = method;
                            TelephonyManager telephonyManager4 = TelephonyRecorder.manager;
                            if (telephonyManager4 == null) {
                                Intrinsics.throwUninitializedPropertyAccessException("manager");
                                telephonyManager4 = null;
                            }
                            List infos = (List) kFunction.call(telephonyManager4);
                            if (infos != null) {
                                CellMoment moment = TelephonyRecorder.INSTANCE.mergeIfPossible(timeline, new CellMoment(TelephonyRecorder.start$elapsed(start), null, infos, null, 10, null));
                                Function1 function1 = (Function1) updateListener.element;
                                if (function1 != null) {
                                    function1.invoke(moment);
                                }
                            }
                        }
                    }, 0L, 1500L);
                    timer.element = Timer;
                }
            } else {
                telephonyManager = null;
            }
            TelephonyManager telephonyManager4 = manager;
            if (telephonyManager4 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("manager");
                telephonyManager4 = telephonyManager;
            }
            telephonyManager4.listen((PhoneStateListener) r2, 1040);
            function0 = new Function0<Unit>() { // from class: com.zhufucdev.motion_emulator.data.TelephonyRecorder.start.3
                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(0);
                }

                @Override // kotlin.jvm.functions.Function0
                public /* bridge */ /* synthetic */ Unit invoke() {
                    invoke2();
                    return Unit.INSTANCE;
                }

                /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2() {
                    TelephonyManager telephonyManager5 = TelephonyRecorder.manager;
                    if (telephonyManager5 == null) {
                        Intrinsics.throwUninitializedPropertyAccessException("manager");
                        telephonyManager5 = null;
                    }
                    telephonyManager5.listen(r2, 0);
                    Timer timer2 = timer.element;
                    if (timer2 != null) {
                        timer2.cancel();
                    }
                }
            };
        }
        final Function0<Unit> function03 = function0;
        return new TelephonyRecordCallback() { // from class: com.zhufucdev.motion_emulator.data.TelephonyRecorder.start.4
            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.zhufucdev.motion_emulator.data.TelephonyRecordCallback
            public void onUpdate(Function1<? super CellMoment, Unit> l) {
                Intrinsics.checkNotNullParameter(l, "l");
                updateListener.element = l;
            }

            @Override // com.zhufucdev.motion_emulator.data.TelephonyRecordCallback
            public CellTimeline summarize() {
                function03.invoke();
                String strRandomNanoId = NanoIdUtils.randomNanoId();
                Intrinsics.checkNotNullExpressionValue(strRandomNanoId, "randomNanoId(...)");
                return new CellTimeline(strRandomNanoId, null, start, timeline);
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final float start$elapsed(long start) {
        return (System.currentTimeMillis() - start) / 1000.0f;
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [com.zhufucdev.motion_emulator.data.TelephonyRecorder$noop$1] */
    private final C08421 noop() {
        return new TelephonyRecordCallback() { // from class: com.zhufucdev.motion_emulator.data.TelephonyRecorder.noop.1
            @Override // com.zhufucdev.motion_emulator.data.TelephonyRecordCallback
            public void onUpdate(Function1<? super CellMoment, Unit> l) {
                Intrinsics.checkNotNullParameter(l, "l");
            }

            @Override // com.zhufucdev.motion_emulator.data.TelephonyRecordCallback
            public CellTimeline summarize() {
                String strRandomNanoId = NanoIdUtils.randomNanoId();
                Intrinsics.checkNotNullExpressionValue(strRandomNanoId, "randomNanoId(...)");
                return new CellTimeline(strRandomNanoId, null, 0L, CollectionsKt.emptyList());
            }
        };
    }
}
