package com.zhufucdev.motion_emulator.provider;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import androidx.constraintlayout.core.motion.utils.TypedValues;
import com.zhufucdev.me.stub.AgentState;
import com.zhufucdev.me.stub.Emulation;
import com.zhufucdev.me.stub.EmulationInfo;
import com.zhufucdev.me.stub.Intermediate;
import com.zhufucdev.motion_emulator.extension.PreferencesKt;
import com.zhufucdev.motion_emulator.plugin.Plugins;
import io.ktor.server.application.Application;
import io.ktor.server.engine.ApplicationEngine;
import io.ktor.server.engine.ApplicationEngineEnvironmentBuilder;
import io.ktor.server.engine.ApplicationEngineEnvironmentKt;
import io.ktor.server.engine.EmbeddedServerKt;
import io.ktor.server.netty.Netty;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import kotlin.Metadata;
import kotlin.Result;
import kotlin.Unit;
import kotlin.collections.CollectionsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugProbesKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt;
import kotlinx.coroutines.CancellableContinuation;
import kotlinx.coroutines.CancellableContinuationImpl;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineScopeKt;
import kotlinx.coroutines.channels.Channel;
import kotlinx.coroutines.channels.ChannelKt;
import kotlinx.coroutines.channels.ReceiveChannel;

/* JADX INFO: compiled from: Scheduler.kt */
/* JADX INFO: loaded from: classes12.dex */
@Metadata(d1 = {"\u0000\u0082\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0010$\n\u0002\u0010\u000e\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0010#\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010%\n\u0002\b\u0003\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u000e\n\u0002\u0018\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\bÇ\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002¢\u0006\u0002\u0010\u0002J \u0010(\u001a\u00020)2\u0018\u0010*\u001a\u0014\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u001a0\u0019J\u000e\u0010+\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\u0010J\u0006\u0010-\u001a\u00020\u001aJ\u000e\u0010.\u001a\u00020\u00042\u0006\u0010/\u001a\u00020\u0010J\u000e\u00100\u001a\u00020\u001a2\u0006\u00101\u001a\u000202J\u001a\u00103\u001a\u00020\u00042\n\b\u0002\u0010/\u001a\u0004\u0018\u00010\u0010H\u0086@¢\u0006\u0002\u00104J\u0010\u00105\u001a\u00020\u001a2\u0006\u00106\u001a\u00020\u0004H\u0002J\u0015\u00107\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\u0010H\u0000¢\u0006\u0002\b8J\u0015\u00109\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\u0010H\u0000¢\u0006\u0002\b:J\u001d\u0010;\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\u00102\u0006\u0010<\u001a\u00020\u0011H\u0000¢\u0006\u0002\b=J\u0018\u0010>\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\u00102\u0006\u0010?\u001a\u00020\u0004H\u0002J>\u0010@\u001a\u00020)26\u0010*\u001a2\u0012\u0013\u0012\u00110\u0010¢\u0006\f\bA\u0012\b\bB\u0012\u0004\b\b(,\u0012\u0013\u0012\u00110\u0004¢\u0006\f\bA\u0012\b\bB\u0012\u0004\b\b(?\u0012\u0004\u0012\u00020\u001a0\u0019J\u000e\u0010C\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\u0010J\u0016\u0010D\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\u00102\u0006\u0010<\u001a\u00020\u0015J\u000e\u0010E\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\u0010J\u0006\u0010F\u001a\u00020\u001aJ\u000e\u0010G\u001a\u00020\u001a2\u0006\u00101\u001a\u000202J\u0018\u0010H\u001a\u00020\u001a2\u0006\u0010,\u001a\u00020\u00102\u0006\u0010?\u001a\u00020\u0004H\u0002J\u001c\u0010I\u001a\b\u0012\u0004\u0012\u00020\u00040J*\u00020K2\n\b\u0002\u0010/\u001a\u0004\u0018\u00010\u0010R\u0011\u0010\u0003\u001a\u00020\u00048F¢\u0006\u0006\u001a\u0004\b\u0005\u0010\u0006R(\u0010\t\u001a\u0004\u0018\u00010\b2\b\u0010\u0007\u001a\u0004\u0018\u00010\b8F@FX\u0086\u000e¢\u0006\f\u001a\u0004\b\n\u0010\u000b\"\u0004\b\f\u0010\rR\u001d\u0010\u000e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00110\u000f8F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u001d\u0010\u0014\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00150\u000f8F¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0013R&\u0010\u0017\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0015\u0012\u0004\u0012\u00020\u001a0\u00190\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R\u0010\u0010\u001b\u001a\u0004\u0018\u00010\bX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u001c\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00110\u001dX\u0082\u000e¢\u0006\u0002\n\u0000R\u001a\u0010\u001e\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00150\u001dX\u0082\u0004¢\u0006\u0002\n\u0000R\u001a\u0010\u001f\u001a\u000e\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u00040\u001dX\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010 \u001a\u00020!X\u0082\u000e¢\u0006\u0002\n\u0000R\u000e\u0010\"\u001a\u00020#X\u0082.¢\u0006\u0002\n\u0000R\u000e\u0010$\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000R&\u0010&\u001a\u001a\u0012\u0016\u0012\u0014\u0012\u0004\u0012\u00020\u0010\u0012\u0004\u0012\u00020\u0004\u0012\u0004\u0012\u00020%0\u00190\u0018X\u0082\u0004¢\u0006\u0002\n\u0000R\u000e\u0010'\u001a\u00020%X\u0082\u000e¢\u0006\u0002\n\u0000¨\u0006L"}, d2 = {"Lcom/zhufucdev/motion_emulator/provider/Scheduler;", "", "()V", "controllerState", "Lcom/zhufucdev/me/stub/AgentState;", "getControllerState", "()Lcom/zhufucdev/me/stub/AgentState;", "value", "Lcom/zhufucdev/me/stub/Emulation;", "emulation", "getEmulation", "()Lcom/zhufucdev/me/stub/Emulation;", "setEmulation", "(Lcom/zhufucdev/me/stub/Emulation;)V", "instance", "", "", "Lcom/zhufucdev/me/stub/EmulationInfo;", "getInstance", "()Ljava/util/Map;", "intermediate", "Lcom/zhufucdev/me/stub/Intermediate;", "getIntermediate", "intermediateListeners", "", "Lkotlin/Function2;", "", "mEmulation", "mInfo", "", "mIntermediate", "mState", RtspHeaders.Values.PORT, "", "server", "Lio/ktor/server/engine/ApplicationEngine;", "serverRunning", "", "stateListeners", "tls", "addIntermediateListener", "Lcom/zhufucdev/motion_emulator/provider/ListenCallback;", "l", "cancelAgent", "id", "cancelAll", "currentEmulationState", "targetId", "init", "context", "Landroid/content/Context;", "nextStateChange", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "notifyAll", TypedValues.AttributesType.S_TARGET, "notifyEmulationCompleted", "notifyEmulationCompleted$app_debug", "notifyEmulationFailed", "notifyEmulationFailed$app_debug", "notifyEmulationStarted", "info", "notifyEmulationStarted$app_debug", "notifyStateChanged", "state", "onAgentStateChanged", "Lkotlin/ParameterName;", "name", "pauseAgent", "setIntermediate", "startAgent", "startAll", "stop", "stopAgent", "incomingAgentStateOf", "Lkotlinx/coroutines/channels/ReceiveChannel;", "Lkotlinx/coroutines/CoroutineScope;", "app_debug"}, k = 1, mv = {1, 9, 0}, xi = 48)
public final class Scheduler {
    private static Emulation mEmulation;
    private static ApplicationEngine server;
    private static boolean serverRunning;
    private static boolean tls;
    public static final Scheduler INSTANCE = new Scheduler();
    private static final Set<Function2<String, AgentState, Boolean>> stateListeners = new LinkedHashSet();
    private static final Set<Function2<String, Intermediate, Unit>> intermediateListeners = new LinkedHashSet();
    private static Map<String, EmulationInfo> mInfo = new HashMap();
    private static final Map<String, AgentState> mState = new HashMap();
    private static final Map<String, Intermediate> mIntermediate = new HashMap();
    private static int port = 20230;
    public static final int $stable = 8;

    private Scheduler() {
    }

    public final void init(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        if (serverRunning) {
            Log.w("Scheduler", "Reinitialize a running instance");
            return;
        }
        SharedPreferences prefs = PreferencesKt.sharedPreferences(context);
        String string = prefs.getString("provider_port", "");
        Intrinsics.checkNotNull(string);
        Integer intOrNull = StringsKt.toIntOrNull(string);
        port = intOrNull != null ? intOrNull.intValue() : 20230;
        tls = prefs.getBoolean("provider_tls", true);
        ApplicationEngine applicationEngine = null;
        server = EmbeddedServerKt.embeddedServer$default(Netty.INSTANCE, ApplicationEngineEnvironmentKt.applicationEngineEnvironment(new Function1<ApplicationEngineEnvironmentBuilder, Unit>() { // from class: com.zhufucdev.motion_emulator.provider.Scheduler.init.1
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(ApplicationEngineEnvironmentBuilder applicationEngineEnvironmentBuilder) {
                invoke2(applicationEngineEnvironmentBuilder);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(ApplicationEngineEnvironmentBuilder applicationEngineEnvironment) {
                Intrinsics.checkNotNullParameter(applicationEngineEnvironment, "$this$applicationEngineEnvironment");
                SchedulerKt.configure(applicationEngineEnvironment, Scheduler.port);
                applicationEngineEnvironment.module(C02141.INSTANCE);
            }

            /* JADX INFO: renamed from: com.zhufucdev.motion_emulator.provider.Scheduler$init$1$1, reason: invalid class name and collision with other inner class name */
            /* JADX INFO: compiled from: Scheduler.kt */
            @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
            /* synthetic */ class C02141 extends FunctionReferenceImpl implements Function1<Application, Unit> {
                public static final C02141 INSTANCE = new C02141();

                C02141() {
                    super(1, SchedulerKt.class, "eventServer", "eventServer(Lio/ktor/server/application/Application;)V", 1);
                }

                @Override // kotlin.jvm.functions.Function1
                public /* bridge */ /* synthetic */ Unit invoke(Application application) {
                    invoke2(application);
                    return Unit.INSTANCE;
                }

                /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
                public final void invoke2(Application p0) {
                    Intrinsics.checkNotNullParameter(p0, "p0");
                    SchedulerKt.eventServer(p0);
                }
            }
        }), null, 4, null);
        ApplicationEngine applicationEngine2 = server;
        if (applicationEngine2 == null) {
            Intrinsics.throwUninitializedPropertyAccessException("server");
        } else {
            applicationEngine = applicationEngine2;
        }
        applicationEngine.start(false);
        serverRunning = true;
        Plugins.INSTANCE.notifyStart(context);
    }

    public final void setIntermediate(String id, Intermediate info) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(info, "info");
        mIntermediate.put(id, info);
        Iterable $this$forEach$iv = intermediateListeners;
        for (Object element$iv : $this$forEach$iv) {
            Function2 it = (Function2) element$iv;
            try {
                it.invoke(id, info);
            } catch (Exception e) {
                Log.w("Scheduler", "Error while notifying intermediate");
                e.printStackTrace();
            }
        }
    }

    public final Map<String, Intermediate> getIntermediate() {
        return mIntermediate;
    }

    public final void startAgent(String id) {
        Intrinsics.checkNotNullParameter(id, "id");
        notifyStateChanged(id, AgentState.PENDING);
    }

    public final void cancelAgent(String id) {
        Intrinsics.checkNotNullParameter(id, "id");
        stopAgent(id, AgentState.CANCELED);
    }

    public final void pauseAgent(String id) {
        Intrinsics.checkNotNullParameter(id, "id");
        stopAgent(id, AgentState.PAUSED);
    }

    private final void stopAgent(String id, AgentState state) {
        if (!mInfo.containsKey(id)) {
            throw new IllegalStateException("No agent: " + id);
        }
        mIntermediate.remove(id);
        notifyStateChanged(id, state);
    }

    public final void cancelAll() {
        notifyAll(AgentState.CANCELED);
    }

    public final void startAll() {
        notifyAll(AgentState.PENDING);
    }

    private final void notifyAll(AgentState target) {
        for (Map.Entry<String, AgentState> entry : mState.entrySet()) {
            String id = entry.getKey();
            AgentState state = entry.getValue();
            if (state != target) {
                INSTANCE.notifyStateChanged(id, target);
            }
        }
    }

    public final void notifyEmulationStarted$app_debug(String id, EmulationInfo info) {
        Intrinsics.checkNotNullParameter(id, "id");
        Intrinsics.checkNotNullParameter(info, "info");
        mInfo.put(id, info);
        notifyStateChanged(id, AgentState.RUNNING);
    }

    public final void notifyEmulationCompleted$app_debug(String id) {
        Intrinsics.checkNotNullParameter(id, "id");
        notifyStateChanged(id, AgentState.COMPLETED);
    }

    public final void notifyEmulationFailed$app_debug(String id) {
        Intrinsics.checkNotNullParameter(id, "id");
        notifyStateChanged(id, AgentState.FAILURE);
    }

    private final void notifyStateChanged(final String id, final AgentState state) {
        mState.put(id, state);
        CollectionsKt.removeAll(stateListeners, new Function1<Function2<? super String, ? super AgentState, ? extends Boolean>, Boolean>() { // from class: com.zhufucdev.motion_emulator.provider.Scheduler.notifyStateChanged.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Boolean invoke(Function2<? super String, ? super AgentState, ? extends Boolean> function2) {
                return invoke2((Function2<? super String, ? super AgentState, Boolean>) function2);
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final Boolean invoke2(Function2<? super String, ? super AgentState, Boolean> it) {
                boolean zBooleanValue;
                Intrinsics.checkNotNullParameter(it, "it");
                try {
                    zBooleanValue = it.invoke(id, state).booleanValue();
                } catch (Exception e) {
                    Log.w("Scheduler", "Error while notifying changes to emulation info");
                    e.printStackTrace();
                    zBooleanValue = false;
                }
                return Boolean.valueOf(zBooleanValue);
            }
        });
    }

    public final Map<String, EmulationInfo> getInstance() {
        return mInfo;
    }

    public final void setEmulation(Emulation value) {
        if (!Intrinsics.areEqual(mEmulation, value)) {
            if (value == null) {
                Map<String, EmulationInfo> map = mInfo;
                mInfo = new LinkedHashMap();
                Iterator<Map.Entry<String, EmulationInfo>> it = map.entrySet().iterator();
                while (it.hasNext()) {
                    String id = it.next().getKey();
                    INSTANCE.notifyStateChanged(id, AgentState.CANCELED);
                }
            }
            mState.clear();
        }
        mEmulation = value;
    }

    public final Emulation getEmulation() {
        return mEmulation;
    }

    public final AgentState getControllerState() {
        boolean z;
        if (getEmulation() == null) {
            return AgentState.NOT_JOINED;
        }
        if (!getInstance().isEmpty()) {
            boolean z2 = true;
            if (mState.size() != 1 || !mState.containsValue(AgentState.PENDING)) {
                if (mState.containsValue(AgentState.RUNNING)) {
                    return AgentState.RUNNING;
                }
                Iterable $this$all$iv = mState.values();
                if (!($this$all$iv instanceof Collection) || !((Collection) $this$all$iv).isEmpty()) {
                    Iterator it = $this$all$iv.iterator();
                    while (true) {
                        if (it.hasNext()) {
                            Object element$iv = it.next();
                            AgentState it2 = (AgentState) element$iv;
                            if (!(it2 == AgentState.PAUSED)) {
                                z = false;
                                break;
                            }
                        } else {
                            z = true;
                            break;
                        }
                    }
                } else {
                    z = true;
                }
                if (z) {
                    return AgentState.PAUSED;
                }
                Iterable $this$all$iv2 = mState.values();
                if (!($this$all$iv2 instanceof Collection) || !((Collection) $this$all$iv2).isEmpty()) {
                    Iterator it3 = $this$all$iv2.iterator();
                    while (true) {
                        if (!it3.hasNext()) {
                            break;
                        }
                        Object element$iv2 = it3.next();
                        AgentState it4 = (AgentState) element$iv2;
                        if (!it4.getFin()) {
                            z2 = false;
                            break;
                        }
                    }
                }
                return z2 ? AgentState.COMPLETED : AgentState.CANCELED;
            }
        }
        return AgentState.PENDING;
    }

    public final ListenCallback onAgentStateChanged(final Function2<? super String, ? super AgentState, Unit> l) {
        Intrinsics.checkNotNullParameter(l, "l");
        final Function2<String, AgentState, Boolean> function2 = new Function2<String, AgentState, Boolean>() { // from class: com.zhufucdev.motion_emulator.provider.Scheduler$onAgentStateChanged$actual$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Boolean invoke(String id, AgentState state) {
                Intrinsics.checkNotNullParameter(id, "id");
                Intrinsics.checkNotNullParameter(state, "state");
                l.invoke(id, state);
                return false;
            }
        };
        stateListeners.add(function2);
        return new ListenCallback() { // from class: com.zhufucdev.motion_emulator.provider.Scheduler.onAgentStateChanged.1
            @Override // com.zhufucdev.motion_emulator.provider.ListenCallback
            public void pause() {
                if (Scheduler.stateListeners.contains(function2)) {
                    Scheduler.stateListeners.remove(function2);
                    return;
                }
                throw new IllegalStateException("Already paused");
            }

            @Override // com.zhufucdev.motion_emulator.provider.ListenCallback
            public void resume() {
                if (!Scheduler.stateListeners.contains(function2)) {
                    Scheduler.stateListeners.add(function2);
                    return;
                }
                throw new IllegalStateException("Already resumed");
            }
        };
    }

    public final AgentState currentEmulationState(String targetId) {
        Intrinsics.checkNotNullParameter(targetId, "targetId");
        AgentState agentState = mState.get(targetId);
        return agentState == null ? AgentState.NOT_JOINED : agentState;
    }

    public static /* synthetic */ Object nextStateChange$default(Scheduler scheduler, String str, Continuation continuation, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        return scheduler.nextStateChange(str, continuation);
    }

    public final Object nextStateChange(final String targetId, Continuation<? super AgentState> continuation) throws Throwable {
        CancellableContinuationImpl cancellable$iv = new CancellableContinuationImpl(IntrinsicsKt.intercepted(continuation), 1);
        cancellable$iv.initCancellability();
        final CancellableContinuationImpl it = cancellable$iv;
        stateListeners.add(new Function2<String, AgentState, Boolean>() { // from class: com.zhufucdev.motion_emulator.provider.Scheduler$nextStateChange$2$1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            /* JADX WARN: Multi-variable type inference failed */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Boolean invoke(String target, AgentState state) {
                Intrinsics.checkNotNullParameter(target, "target");
                Intrinsics.checkNotNullParameter(state, "state");
                if (targetId == null || Intrinsics.areEqual(target, targetId)) {
                    if (!it.isCancelled()) {
                        CancellableContinuation<AgentState> cancellableContinuation = it;
                        Result.Companion companion = Result.Companion;
                        cancellableContinuation.resumeWith(Result.m7172constructorimpl(state));
                    }
                    return true;
                }
                return false;
            }
        });
        Object result = cancellable$iv.getResult();
        if (result == IntrinsicsKt.getCOROUTINE_SUSPENDED()) {
            DebugProbesKt.probeCoroutineSuspended(continuation);
        }
        return result;
    }

    public static /* synthetic */ ReceiveChannel incomingAgentStateOf$default(Scheduler scheduler, CoroutineScope coroutineScope, String str, int i, Object obj) {
        if ((i & 1) != 0) {
            str = null;
        }
        return scheduler.incomingAgentStateOf(coroutineScope, str);
    }

    public final ReceiveChannel<AgentState> incomingAgentStateOf(final CoroutineScope $this$incomingAgentStateOf, final String targetId) {
        Intrinsics.checkNotNullParameter($this$incomingAgentStateOf, "<this>");
        final Channel channel = ChannelKt.Channel$default(0, null, null, 7, null);
        stateListeners.add(new Function2<String, AgentState, Boolean>() { // from class: com.zhufucdev.motion_emulator.provider.Scheduler.incomingAgentStateOf.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(2);
            }

            @Override // kotlin.jvm.functions.Function2
            public final Boolean invoke(String target, AgentState state) {
                Intrinsics.checkNotNullParameter(target, "target");
                Intrinsics.checkNotNullParameter(state, "state");
                if (targetId == null || Intrinsics.areEqual(target, targetId)) {
                    channel.mo6839trySendJP2dKIU(state);
                }
                return Boolean.valueOf(!CoroutineScopeKt.isActive($this$incomingAgentStateOf));
            }
        });
        return channel;
    }

    public final ListenCallback addIntermediateListener(final Function2<? super String, ? super Intermediate, Unit> l) {
        Intrinsics.checkNotNullParameter(l, "l");
        intermediateListeners.add(l);
        return new ListenCallback() { // from class: com.zhufucdev.motion_emulator.provider.Scheduler.addIntermediateListener.1
            @Override // com.zhufucdev.motion_emulator.provider.ListenCallback
            public void pause() {
                if (Scheduler.intermediateListeners.contains(l)) {
                    Scheduler.intermediateListeners.remove(l);
                    return;
                }
                throw new IllegalStateException("Already paused");
            }

            @Override // com.zhufucdev.motion_emulator.provider.ListenCallback
            public void resume() {
                if (!Scheduler.intermediateListeners.contains(l)) {
                    Scheduler.intermediateListeners.add(l);
                    return;
                }
                throw new IllegalStateException("Already resumed");
            }
        };
    }

    public final void stop(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        Plugins.INSTANCE.notifyStop(context);
        notifyAll(AgentState.NOT_JOINED);
        ApplicationEngine applicationEngine = server;
        if (applicationEngine == null) {
            Intrinsics.throwUninitializedPropertyAccessException("server");
            applicationEngine = null;
        }
        ApplicationEngine.DefaultImpls.stop$default(applicationEngine, 0L, 0L, 3, null);
        serverRunning = false;
    }
}
