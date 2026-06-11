package com.zhufucdev.motion_emulator.provider;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import androidx.core.app.NotificationCompat;
import androidx.core.view.accessibility.AccessibilityEventCompat;
import androidx.work.CoroutineWorker;
import androidx.work.ForegroundInfo;
import androidx.work.WorkerParameters;
import com.zhufucdev.motion_emulator.R;
import com.zhufucdev.motion_emulator.ui.EmulateActivity;
import kotlin.Metadata;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.jvm.internal.Intrinsics;
import kotlin.math.MathKt;

/* JADX INFO: compiled from: EmulationMonitorWorker.kt */
/* JADX INFO: loaded from: classes12.dex */
@Metadata(d1 = {"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0007\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0007\u0018\u0000 \u000f2\u00020\u0001:\u0001\u000fB\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005¢\u0006\u0002\u0010\u0006J\u0010\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\nH\u0002J\u000e\u0010\u000b\u001a\u00020\fH\u0096@¢\u0006\u0002\u0010\rJ\u000e\u0010\u000e\u001a\u00020\bH\u0096@¢\u0006\u0002\u0010\r¨\u0006\u0010"}, d2 = {"Lcom/zhufucdev/motion_emulator/provider/EmulationMonitorWorker;", "Landroidx/work/CoroutineWorker;", "appContext", "Landroid/content/Context;", "workerParameters", "Landroidx/work/WorkerParameters;", "(Landroid/content/Context;Landroidx/work/WorkerParameters;)V", "createForegroundInfo", "Landroidx/work/ForegroundInfo;", "progress", "", "doWork", "Landroidx/work/ListenableWorker$Result;", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "getForegroundInfo", "Companion", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class EmulationMonitorWorker extends CoroutineWorker {
    public static final int $stable = 0;
    public static final String CHANNEL_ID = "emulation_activity";
    public static final int NOTIFICATION_ID = 0;

    /* JADX INFO: renamed from: com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker$doWork$1, reason: invalid class name */
    /* JADX INFO: compiled from: EmulationMonitorWorker.kt */
    @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
    @DebugMetadata(c = "com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker", f = "EmulationMonitorWorker.kt", i = {0, 1}, l = {29, 30}, m = "doWork", n = {"this", "this"}, s = {"L$0", "L$0"})
    static final class AnonymousClass1 extends ContinuationImpl {
        Object L$0;
        int label;
        /* synthetic */ Object result;

        AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
            super(continuation);
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Object invokeSuspend(Object obj) {
            this.result = obj;
            this.label |= Integer.MIN_VALUE;
            return EmulationMonitorWorker.this.doWork(this);
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public EmulationMonitorWorker(Context appContext, WorkerParameters workerParameters) {
        super(appContext, workerParameters);
        Intrinsics.checkNotNullParameter(appContext, "appContext");
        Intrinsics.checkNotNullParameter(workerParameters, "workerParameters");
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x004a  */
    /* JADX WARN: Removed duplicated region for block: B:26:0x0096 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:28:0x0098  */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0014  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:25:0x0094 -> B:15:0x0041). Please report as a decompilation issue!!! */
    @Override // androidx.work.CoroutineWorker
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.Object doWork(kotlin.coroutines.Continuation<? super androidx.work.ListenableWorker.Result> r7) {
        /*
            r6 = this;
            boolean r0 = r7 instanceof com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker.AnonymousClass1
            if (r0 == 0) goto L14
            r0 = r7
            com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker$doWork$1 r0 = (com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker.AnonymousClass1) r0
            int r1 = r0.label
            r2 = -2147483648(0xffffffff80000000, float:-0.0)
            r1 = r1 & r2
            if (r1 == 0) goto L14
            int r7 = r0.label
            int r7 = r7 - r2
            r0.label = r7
            goto L19
        L14:
            com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker$doWork$1 r0 = new com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker$doWork$1
            r0.<init>(r7)
        L19:
            r7 = r0
            java.lang.Object r0 = r7.result
            java.lang.Object r1 = kotlin.coroutines.intrinsics.IntrinsicsKt.getCOROUTINE_SUSPENDED()
            int r2 = r7.label
            switch(r2) {
                case 0: goto L3d;
                case 1: goto L35;
                case 2: goto L2d;
                default: goto L25;
            }
        L25:
            java.lang.IllegalStateException r7 = new java.lang.IllegalStateException
            java.lang.String r0 = "call to 'resume' before 'invoke' with coroutine"
            r7.<init>(r0)
            throw r7
        L2d:
            java.lang.Object r2 = r7.L$0
            com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker r2 = (com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker) r2
            kotlin.ResultKt.throwOnFailure(r0)
            goto L97
        L35:
            java.lang.Object r2 = r7.L$0
            com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker r2 = (com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker) r2
            kotlin.ResultKt.throwOnFailure(r0)
            goto L81
        L3d:
            kotlin.ResultKt.throwOnFailure(r0)
            r2 = r6
        L41:
            com.zhufucdev.motion_emulator.provider.Scheduler r3 = com.zhufucdev.motion_emulator.provider.Scheduler.INSTANCE
            com.zhufucdev.me.stub.Emulation r3 = r3.getEmulation()
            if (r3 == 0) goto L98
            com.zhufucdev.motion_emulator.provider.Scheduler r3 = com.zhufucdev.motion_emulator.provider.Scheduler.INSTANCE
            java.util.Map r3 = r3.getIntermediate()
            int r3 = r3.size()
            r4 = 1
            if (r3 != r4) goto L6e
            com.zhufucdev.motion_emulator.provider.Scheduler r3 = com.zhufucdev.motion_emulator.provider.Scheduler.INSTANCE
            java.util.Map r3 = r3.getIntermediate()
            java.util.Collection r3 = r3.values()
            java.lang.Iterable r3 = (java.lang.Iterable) r3
            java.lang.Object r3 = kotlin.collections.CollectionsKt.first(r3)
            com.zhufucdev.me.stub.Intermediate r3 = (com.zhufucdev.me.stub.Intermediate) r3
            float r3 = r3.getProgress()
            goto L70
        L6e:
            r3 = -1082130432(0xffffffffbf800000, float:-1.0)
        L70:
            androidx.work.ForegroundInfo r5 = r2.createForegroundInfo(r3)
            r7.L$0 = r2
            r7.label = r4
            java.lang.Object r3 = r2.setForeground(r5, r7)
            if (r3 != r1) goto L81
            return r1
        L81:
            kotlin.time.Duration$Companion r3 = kotlin.time.Duration.INSTANCE
            r3 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            kotlin.time.DurationUnit r5 = kotlin.time.DurationUnit.SECONDS
            long r3 = kotlin.time.DurationKt.toDuration(r3, r5)
            r7.L$0 = r2
            r5 = 2
            r7.label = r5
            java.lang.Object r3 = kotlinx.coroutines.DelayKt.m8677delayVtjQ1oo(r3, r7)
            if (r3 != r1) goto L97
            return r1
        L97:
            goto L41
        L98:
            androidx.work.ListenableWorker$Result r1 = androidx.work.ListenableWorker.Result.success()
            java.lang.String r3 = "success(...)"
            kotlin.jvm.internal.Intrinsics.checkNotNullExpressionValue(r1, r3)
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.zhufucdev.motion_emulator.provider.EmulationMonitorWorker.doWork(kotlin.coroutines.Continuation):java.lang.Object");
    }

    @Override // androidx.work.CoroutineWorker
    public Object getForegroundInfo(Continuation<? super ForegroundInfo> continuation) {
        return createForegroundInfo(-1.0f);
    }

    private final ForegroundInfo createForegroundInfo(float progress) {
        Intent determineIntent = new Intent(getApplicationContext(), (Class<?>) EmulationMonitorReceiver.class);
        determineIntent.setAction(EmulationMonitorReceiverKt.INTENT_ACTION_DETERMINE);
        PendingIntent determinePendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, determineIntent, AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL);
        Intent contentIntend = new Intent(getApplicationContext(), (Class<?>) EmulateActivity.class);
        PendingIntent contentPendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, contentIntend, AccessibilityEventCompat.TYPE_VIEW_TARGETED_BY_SCROLL);
        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID).setContentTitle("Emulation Ongoing").setTicker("Emulation Ongoing").setContentText("Click here to see more detail").setContentIntent(contentPendingIntent).setProgress(1000, MathKt.roundToInt(1000 * progress), progress < 0.0f).setSmallIcon(R.drawable.ic_baseline_auto_fix_high_24).addAction(R.drawable.ic_baseline_stop_24, "Determine", determinePendingIntent).build();
        Intrinsics.checkNotNullExpressionValue(notification, "build(...)");
        return new ForegroundInfo(0, notification);
    }
}
