package com.zhufucdev.motion_emulator.data;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.zhufucdev.me.stub.Motion;
import com.zhufucdev.me.stub.MotionMoment;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Ref;

/* JADX INFO: compiled from: MotionRecorder.kt */
/* JADX INFO: loaded from: classes7.dex */
@Metadata(d1 = {"\u00008\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0002\u0010\b\n\u0000\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J\u000e\u0010\t\u001a\u00020\n2\u0006\u0010\u000b\u001a\u00020\fJ\u0014\u0010\r\u001a\u00020\u00052\f\u0010\u000e\u001a\b\u0012\u0004\u0012\u00020\u00100\u000fR\u001e\u0010\u0003\u001a\u0012\u0012\u0004\u0012\u00020\u00050\u0004j\b\u0012\u0004\u0012\u00020\u0005`\u0006X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010\u0007\u001a\u00020\bX\u0082.¢\u0006\u0002\n\u0000¨\u0006\u0011"}, d2 = {"Lcom/zhufucdev/motion_emulator/data/MotionRecorder;", "", "()V", "callbacks", "Ljava/util/ArrayList;", "Lcom/zhufucdev/motion_emulator/data/MotionCallback;", "Lkotlin/collections/ArrayList;", "sensors", "Landroid/hardware/SensorManager;", "init", "", "context", "Landroid/content/Context;", "start", "sensorsRequired", "", "", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class MotionRecorder {
    private static SensorManager sensors;
    public static final MotionRecorder INSTANCE = new MotionRecorder();
    private static final ArrayList<MotionCallback> callbacks = new ArrayList<>();
    public static final int $stable = 8;

    private MotionRecorder() {
    }

    public final void init(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Object systemService = context.getSystemService((Class<Object>) SensorManager.class);
        Intrinsics.checkNotNullExpressionValue(systemService, "getSystemService(...)");
        sensors = (SensorManager) systemService;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [com.zhufucdev.motion_emulator.data.MotionRecorder$start$listener$1] */
    public final MotionCallback start(List<Integer> sensorsRequired) {
        Intrinsics.checkNotNullParameter(sensorsRequired, "sensorsRequired");
        final long start = System.currentTimeMillis();
        final ArrayList moments = new ArrayList();
        final Ref.ObjectRef callbackListener = new Ref.ObjectRef();
        final HashMap typedListeners = new HashMap();
        final List sensorsInvolved = CollectionsKt.toList(sensorsRequired);
        final int sensorCount = sensorsInvolved.size();
        final ?? r0 = new SensorEventListener() { // from class: com.zhufucdev.motion_emulator.data.MotionRecorder$start$listener$1
            /* JADX WARN: Multi-variable type inference failed */
            @Override // android.hardware.SensorEventListener
            public void onSensorChanged(SensorEvent event) {
                Function1<MotionMoment, Unit> function1;
                Function1<MotionMoment, Unit> function12;
                Intrinsics.checkNotNullParameter(event, "event");
                float elapsed = (System.currentTimeMillis() - start) / 1000.0f;
                if (!moments.isEmpty()) {
                    MotionMoment lastMoment = (MotionMoment) CollectionsKt.last((List) moments);
                    if (Math.abs(lastMoment.getElapsed() - elapsed) < 0.05f && !lastMoment.getData().containsKey(Integer.valueOf(event.sensor.getType()))) {
                        lastMoment.getData().put(Integer.valueOf(event.sensor.getType()), event.values.clone());
                        Function1<MotionMoment, Unit> function13 = typedListeners.get(Integer.valueOf(event.sensor.getType()));
                        if (function13 != null) {
                            function13.invoke(lastMoment);
                        }
                        if (lastMoment.getData().size() != sensorCount || (function12 = callbackListener.element) == null) {
                            return;
                        }
                        function12.invoke(lastMoment);
                        return;
                    }
                }
                Map data = new LinkedHashMap();
                data.put(Integer.valueOf(event.sensor.getType()), event.values.clone());
                MotionMoment moment = new MotionMoment(elapsed, data);
                moments.add(moment);
                Function1<MotionMoment, Unit> function14 = typedListeners.get(Integer.valueOf(event.sensor.getType()));
                if (function14 != null) {
                    function14.invoke(moment);
                }
                if (sensorCount != 1 || (function1 = callbackListener.element) == null) {
                    return;
                }
                function1.invoke(moment);
            }

            @Override // android.hardware.SensorEventListener
            public void onAccuracyChanged(Sensor sensor, int accuracy) {
            }
        };
        List<Integer> $this$forEach$iv = sensorsRequired;
        for (Object element$iv : $this$forEach$iv) {
            int it = ((Number) element$iv).intValue();
            SensorManager sensorManager = sensors;
            SensorManager sensorManager2 = null;
            if (sensorManager == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sensors");
                sensorManager = null;
            }
            Sensor sensor = sensorManager.getDefaultSensor(it);
            SensorManager sensorManager3 = sensors;
            if (sensorManager3 == null) {
                Intrinsics.throwUninitializedPropertyAccessException("sensors");
            } else {
                sensorManager2 = sensorManager3;
            }
            sensorManager2.registerListener((SensorEventListener) r0, sensor, 2);
        }
        MotionCallback motionCallback = new MotionCallback() { // from class: com.zhufucdev.motion_emulator.data.MotionRecorder$start$result$1
            @Override // com.zhufucdev.motion_emulator.data.MotionCallback
            public Motion summarize() {
                SensorManager sensorManager4 = MotionRecorder.sensors;
                if (sensorManager4 == null) {
                    Intrinsics.throwUninitializedPropertyAccessException("sensors");
                    sensorManager4 = null;
                }
                sensorManager4.unregisterListener(r0);
                synchronized (MotionRecorder.INSTANCE) {
                    MotionRecorder.callbacks.remove(this);
                }
                String strRandomNanoId = NanoIdUtils.randomNanoId();
                Intrinsics.checkNotNullExpressionValue(strRandomNanoId, "randomNanoId(...)");
                return new Motion(strRandomNanoId, null, start, moments, sensorsInvolved);
            }

            /* JADX WARN: Multi-variable type inference failed */
            @Override // com.zhufucdev.motion_emulator.data.MotionCallback
            public void onUpdate(Function1<? super MotionMoment, Unit> l) {
                Intrinsics.checkNotNullParameter(l, "l");
                callbackListener.element = l;
            }

            @Override // com.zhufucdev.motion_emulator.data.MotionCallback
            public void onUpdate(int type, Function1<? super MotionMoment, Unit> l) {
                Intrinsics.checkNotNullParameter(l, "l");
                typedListeners.put(Integer.valueOf(type), l);
            }
        };
        synchronized (this) {
            callbacks.add(motionCallback);
        }
        return motionCallback;
    }
}
