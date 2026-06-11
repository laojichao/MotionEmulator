package com.zhufucdev.motion_emulator.provider;

import com.amap.api.maps.utils.SpatialRelationUtil;
import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.zhufucdev.me.stub.AgentState;
import com.zhufucdev.me.stub.Emulation;
import com.zhufucdev.me.stub.EmulationInfo;
import com.zhufucdev.me.stub.Intermediate;
import io.ktor.http.HttpStatusCode;
import io.ktor.http.Parameters;
import io.ktor.http.content.OutgoingContent;
import io.ktor.serialization.WebsocketContentConverter;
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter;
import io.ktor.serialization.kotlinx.protobuf.ProtoBufSupportKt;
import io.ktor.server.application.Application;
import io.ktor.server.application.ApplicationCall;
import io.ktor.server.application.ApplicationPluginKt;
import io.ktor.server.engine.ApplicationEngineEnvironmentBuilder;
import io.ktor.server.engine.EngineConnectorBuilder;
import io.ktor.server.engine.EngineConnectorConfig;
import io.ktor.server.engine.EngineSSLConnectorBuilder;
import io.ktor.server.plugins.MissingRequestParameterException;
import io.ktor.server.plugins.contentnegotiation.ContentNegotiationConfig;
import io.ktor.server.plugins.contentnegotiation.ContentNegotiationKt;
import io.ktor.server.response.ApplicationResponse;
import io.ktor.server.response.ApplicationSendPipeline;
import io.ktor.server.response.ResponseTypeKt;
import io.ktor.server.routing.Routing;
import io.ktor.server.routing.RoutingBuilderKt;
import io.ktor.server.routing.RoutingKt;
import io.ktor.server.websocket.WebSocketServerSession;
import io.ktor.server.websocket.WebSocketServerSessionKt;
import io.ktor.server.websocket.WebSockets;
import io.ktor.util.pipeline.PipelineContext;
import io.ktor.util.reflect.TypeInfoJvmKt;
import io.ktor.websocket.Frame;
import io.ktor.websocket.WebSocketSession;
import io.ktor.websocket.WebSocketSessionKt;
import io.netty.handler.codec.rtsp.RtspHeaders;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.security.KeyStore;
import java.util.List;
import kotlin.Metadata;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.intrinsics.IntrinsicsKt;
import kotlin.coroutines.jvm.internal.DebugMetadata;
import kotlin.coroutines.jvm.internal.SuspendLambda;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.functions.Function3;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Reflection;
import kotlin.reflect.KType;
import kotlin.reflect.TypesJVMKt;
import kotlin.text.Charsets;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.JobKt;
import kotlinx.coroutines.channels.ChannelIterator;
import kotlinx.serialization.protobuf.ProtoBuf;

/* JADX INFO: compiled from: Scheduler.kt */
/* JADX INFO: loaded from: classes12.dex */
@Metadata(d1 = {"\u0000:\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000e\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0012\u0010\u0000\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\u0012\u0010\u0005\u001a\u00020\u0001*\u00020\u00022\u0006\u0010\u0003\u001a\u00020\u0004\u001a\n\u0010\u0006\u001a\u00020\u0001*\u00020\u0007\u001a\u001a\u0010\b\u001a\u00020\t*\u00020\n2\u0006\u0010\u000b\u001a\u00020\fH\u0082@¢\u0006\u0002\u0010\r\u001a\u001a\u0010\u000e\u001a\u00020\u0001*\u00020\u000f2\u0006\u0010\u0010\u001a\u00020\u0011H\u0082@¢\u0006\u0002\u0010\u0012¨\u0006\u0013"}, d2 = {"configure", "", "Lio/ktor/server/engine/ApplicationEngineEnvironmentBuilder;", RtspHeaders.Values.PORT, "", "configureSsl", "eventServer", "Lio/ktor/server/application/Application;", "launchWorker", "Lkotlinx/coroutines/Job;", "Lio/ktor/server/websocket/WebSocketServerSession;", "id", "", "(Lio/ktor/server/websocket/WebSocketServerSession;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "sendCommand", "Lio/ktor/websocket/WebSocketSession;", "state", "Lcom/zhufucdev/me/stub/AgentState;", "(Lio/ktor/websocket/WebSocketSession;Lcom/zhufucdev/me/stub/AgentState;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "app_debug"}, k = 2, mv = {1, 9, 0}, xi = 48)
public final class SchedulerKt {
    public static final void configureSsl(ApplicationEngineEnvironmentBuilder $this$configureSsl, int port) {
        Intrinsics.checkNotNullParameter($this$configureSsl, "<this>");
        String strRandomNanoId = NanoIdUtils.randomNanoId();
        Intrinsics.checkNotNullExpressionValue(strRandomNanoId, "randomNanoId(...)");
        final char[] keyPassword = strRandomNanoId.toCharArray();
        Intrinsics.checkNotNullExpressionValue(keyPassword, "toCharArray(...)");
        KeyStore keyStore = SelfSignedCertificateKt.generateSelfSignedKeyStore$default("motion_provider", keyPassword, null, 4, null);
        Function0<char[]> function0 = new Function0<char[]>() { // from class: com.zhufucdev.motion_emulator.provider.SchedulerKt.configureSsl.1
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public final char[] invoke() {
                return keyPassword;
            }
        };
        Function0<char[]> function02 = new Function0<char[]>() { // from class: com.zhufucdev.motion_emulator.provider.SchedulerKt.configureSsl.2
            /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
            {
                super(0);
            }

            @Override // kotlin.jvm.functions.Function0
            public final char[] invoke() {
                return keyPassword;
            }
        };
        List<EngineConnectorConfig> connectors = $this$configureSsl.getConnectors();
        EngineSSLConnectorBuilder $this$configureSsl_u24lambda_u240 = new EngineSSLConnectorBuilder(keyStore, "motion_provider", function0, function02);
        $this$configureSsl_u24lambda_u240.setHost("127.0.0.1");
        $this$configureSsl_u24lambda_u240.setPort(port);
        connectors.add($this$configureSsl_u24lambda_u240);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static final void configure(ApplicationEngineEnvironmentBuilder applicationEngineEnvironmentBuilder, int i) {
        Intrinsics.checkNotNullParameter(applicationEngineEnvironmentBuilder, "<this>");
        List<EngineConnectorConfig> connectors = applicationEngineEnvironmentBuilder.getConnectors();
        EngineConnectorBuilder engineConnectorBuilder = new EngineConnectorBuilder(null, 1, 0 == true ? 1 : 0);
        engineConnectorBuilder.setHost("127.0.0.1");
        engineConnectorBuilder.setPort(i);
        connectors.add(engineConnectorBuilder);
    }

    public static final void eventServer(Application $this$eventServer) {
        Intrinsics.checkNotNullParameter($this$eventServer, "<this>");
        ApplicationPluginKt.install($this$eventServer, ContentNegotiationKt.getContentNegotiation(), new Function1<ContentNegotiationConfig, Unit>() { // from class: com.zhufucdev.motion_emulator.provider.SchedulerKt.eventServer.1
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(ContentNegotiationConfig contentNegotiationConfig) {
                invoke2(contentNegotiationConfig);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(ContentNegotiationConfig install) {
                Intrinsics.checkNotNullParameter(install, "$this$install");
                ProtoBufSupportKt.protobuf$default(install, null, null, 3, null);
            }
        });
        ApplicationPluginKt.install($this$eventServer, WebSockets.INSTANCE, new Function1<WebSockets.WebSocketOptions, Unit>() { // from class: com.zhufucdev.motion_emulator.provider.SchedulerKt.eventServer.2
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(WebSockets.WebSocketOptions webSocketOptions) {
                invoke2(webSocketOptions);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(WebSockets.WebSocketOptions install) {
                Intrinsics.checkNotNullParameter(install, "$this$install");
                install.setMaxFrameSize(Long.MAX_VALUE);
                install.setMasking(false);
                install.setContentConverter(new KotlinxWebsocketSerializationConverter(ProtoBuf.INSTANCE));
            }
        });
        RoutingKt.routing($this$eventServer, new Function1<Routing, Unit>() { // from class: com.zhufucdev.motion_emulator.provider.SchedulerKt.eventServer.3

            /* JADX INFO: renamed from: com.zhufucdev.motion_emulator.provider.SchedulerKt$eventServer$3$1, reason: invalid class name */
            /* JADX INFO: compiled from: Scheduler.kt */
            @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lio/ktor/server/websocket/WebSocketServerSession;"}, k = 3, mv = {1, 9, 0}, xi = 48)
            @DebugMetadata(c = "com.zhufucdev.motion_emulator.provider.SchedulerKt$eventServer$3$1", f = "Scheduler.kt", i = {0, 0, 1, 1, 1, 2, 4, 4, 5, 5, 6, 6, 6, 7, 7, 7, 8, 8, 8}, l = {349, 351, 354, 355, SpatialRelationUtil.A_CIRCLE_DEGREE, 361, 362, 369, 371}, m = "invokeSuspend", n = {"$this$webSocketRaw", "id", "$this$webSocketRaw", "id", "worker", "worker", "$this$webSocketRaw", "id", "$this$webSocketRaw", "id", "$this$webSocketRaw", "id", "worker", "$this$webSocketRaw", "id", "worker", "$this$webSocketRaw", "id", "worker"}, s = {"L$0", "L$1", "L$0", "L$1", "L$2", "L$0", "L$0", "L$1", "L$0", "L$1", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2", "L$0", "L$1", "L$2"})
            static final class AnonymousClass1 extends SuspendLambda implements Function2<WebSocketServerSession, Continuation<? super Unit>, Object> {
                private /* synthetic */ Object L$0;
                Object L$1;
                Object L$2;
                Object L$3;
                int label;

                /* JADX INFO: renamed from: com.zhufucdev.motion_emulator.provider.SchedulerKt$eventServer$3$1$WhenMappings */
                /* JADX INFO: compiled from: Scheduler.kt */
                @Metadata(k = 3, mv = {1, 9, 0}, xi = 48)
                public /* synthetic */ class WhenMappings {
                    public static final /* synthetic */ int[] $EnumSwitchMapping$0;

                    static {
                        int[] iArr = new int[AgentState.values().length];
                        try {
                            iArr[AgentState.NOT_JOINED.ordinal()] = 1;
                        } catch (NoSuchFieldError e) {
                        }
                        try {
                            iArr[AgentState.PENDING.ordinal()] = 2;
                        } catch (NoSuchFieldError e2) {
                        }
                        try {
                            iArr[AgentState.RUNNING.ordinal()] = 3;
                        } catch (NoSuchFieldError e3) {
                        }
                        $EnumSwitchMapping$0 = iArr;
                    }
                }

                AnonymousClass1(Continuation<? super AnonymousClass1> continuation) {
                    super(2, continuation);
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
                    AnonymousClass1 anonymousClass1 = new AnonymousClass1(continuation);
                    anonymousClass1.L$0 = obj;
                    return anonymousClass1;
                }

                @Override // kotlin.jvm.functions.Function2
                public final Object invoke(WebSocketServerSession webSocketServerSession, Continuation<? super Unit> continuation) {
                    return ((AnonymousClass1) create(webSocketServerSession, continuation)).invokeSuspend(Unit.INSTANCE);
                }

                /* JADX WARN: Removed duplicated region for block: B:24:0x0123 A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:25:0x0124  */
                /* JADX WARN: Removed duplicated region for block: B:28:0x0132  */
                /* JADX WARN: Removed duplicated region for block: B:42:0x0193 A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:43:0x0194  */
                /* JADX WARN: Removed duplicated region for block: B:46:0x01b7 A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:47:0x01b8  */
                /* JADX WARN: Removed duplicated region for block: B:55:0x01ed A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:62:0x020d A[RETURN] */
                /* JADX WARN: Removed duplicated region for block: B:63:0x020e  */
                /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:35:0x0162 -> B:22:0x010f). Please report as a decompilation issue!!! */
                /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:47:0x01b8 -> B:22:0x010f). Please report as a decompilation issue!!! */
                /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:63:0x020e -> B:22:0x010f). Please report as a decompilation issue!!! */
                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                */
                public final Object invokeSuspend(Object $result) throws MissingRequestParameterException {
                    Object $result2;
                    WebSocketServerSession $this$webSocketRaw;
                    String id;
                    AnonymousClass1 anonymousClass1;
                    Job worker;
                    ChannelIterator<AgentState> it;
                    Job worker2;
                    String id2;
                    WebSocketServerSession $this$webSocketRaw2;
                    ChannelIterator<AgentState> channelIterator;
                    Job worker3;
                    Object $result3;
                    AnonymousClass1 anonymousClass12;
                    AnonymousClass1 anonymousClass13;
                    ChannelIterator<AgentState> channelIterator2;
                    String id3;
                    WebSocketServerSession $this$webSocketRaw3;
                    WebSocketServerSession $this$webSocketRaw4;
                    String id4;
                    ChannelIterator<AgentState> channelIterator3;
                    Object obj;
                    Object $result4;
                    AnonymousClass1 anonymousClass14;
                    ChannelIterator<AgentState> channelIterator4;
                    Object objLaunchWorker;
                    Object objHasNext;
                    Object $result5 = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    switch (this.label) {
                        case 0:
                            ResultKt.throwOnFailure($result);
                            WebSocketServerSession $this$webSocketRaw5 = (WebSocketServerSession) this.L$0;
                            Parameters $this$getOrFail$iv = $this$webSocketRaw5.getCall().getParameters();
                            String id5 = $this$getOrFail$iv.get("id");
                            if (id5 == null) {
                                throw new MissingRequestParameterException("id");
                            }
                            Scheduler.INSTANCE.startAgent(id5);
                            this.L$0 = $this$webSocketRaw5;
                            this.L$1 = id5;
                            this.label = 1;
                            Object objLaunchWorker2 = SchedulerKt.launchWorker($this$webSocketRaw5, id5, this);
                            if (objLaunchWorker2 == $result5) {
                                return $result5;
                            }
                            $result2 = $result;
                            $result = objLaunchWorker2;
                            $this$webSocketRaw = $this$webSocketRaw5;
                            id = id5;
                            anonymousClass1 = this;
                            worker = (Job) $result;
                            it = Scheduler.INSTANCE.incomingAgentStateOf($this$webSocketRaw, id).iterator();
                            anonymousClass1.L$0 = $this$webSocketRaw;
                            anonymousClass1.L$1 = id;
                            anonymousClass1.L$2 = worker;
                            anonymousClass1.L$3 = it;
                            anonymousClass1.label = 2;
                            objHasNext = it.hasNext(anonymousClass1);
                            if (objHasNext == $result5) {
                                return $result5;
                            }
                            String str = id;
                            worker2 = worker;
                            $result = objHasNext;
                            channelIterator = it;
                            $this$webSocketRaw2 = $this$webSocketRaw;
                            id2 = str;
                            if (((Boolean) $result).booleanValue()) {
                                AgentState req = channelIterator.next();
                                switch (WhenMappings.$EnumSwitchMapping$0[req.ordinal()]) {
                                    case 1:
                                        anonymousClass1.L$0 = worker2;
                                        anonymousClass1.L$1 = null;
                                        anonymousClass1.L$2 = null;
                                        anonymousClass1.L$3 = null;
                                        anonymousClass1.label = 3;
                                        if (SchedulerKt.sendCommand($this$webSocketRaw2, AgentState.NOT_JOINED, anonymousClass1) == $result5) {
                                            return $result5;
                                        }
                                        $result = $result2;
                                        $result3 = $result5;
                                        anonymousClass12 = anonymousClass1;
                                        worker3 = worker2;
                                        anonymousClass12.L$0 = null;
                                        anonymousClass12.label = 4;
                                        if (JobKt.cancelAndJoin(worker3, anonymousClass12) == $result3) {
                                            return $result3;
                                        }
                                        break;
                                    case 2:
                                        anonymousClass1.L$0 = $this$webSocketRaw2;
                                        anonymousClass1.L$1 = id2;
                                        anonymousClass1.L$2 = channelIterator;
                                        anonymousClass1.L$3 = null;
                                        anonymousClass1.label = 5;
                                        if (JobKt.cancelAndJoin(worker2, anonymousClass1) == $result5) {
                                            return $result5;
                                        }
                                        $result = $result2;
                                        anonymousClass13 = anonymousClass1;
                                        id3 = id2;
                                        $this$webSocketRaw3 = $this$webSocketRaw2;
                                        channelIterator2 = channelIterator;
                                        anonymousClass13.L$0 = $this$webSocketRaw3;
                                        anonymousClass13.L$1 = id3;
                                        anonymousClass13.L$2 = channelIterator2;
                                        anonymousClass13.label = 6;
                                        objLaunchWorker = SchedulerKt.launchWorker($this$webSocketRaw3, id3, anonymousClass13);
                                        if (objLaunchWorker != $result5) {
                                            return $result5;
                                        }
                                        Object obj2 = $result5;
                                        $result4 = $result;
                                        $result = objLaunchWorker;
                                        $this$webSocketRaw4 = $this$webSocketRaw3;
                                        id4 = id3;
                                        channelIterator3 = channelIterator2;
                                        anonymousClass1 = anonymousClass13;
                                        obj = obj2;
                                        worker = (Job) $result;
                                        anonymousClass1.L$0 = $this$webSocketRaw4;
                                        anonymousClass1.L$1 = id4;
                                        anonymousClass1.L$2 = worker;
                                        anonymousClass1.L$3 = channelIterator3;
                                        anonymousClass1.label = 7;
                                        if (SchedulerKt.sendCommand($this$webSocketRaw4, AgentState.PENDING, anonymousClass1) != obj) {
                                            return obj;
                                        }
                                        Object obj3 = obj;
                                        $result2 = $result4;
                                        $result5 = obj3;
                                        WebSocketServerSession webSocketServerSession = $this$webSocketRaw4;
                                        it = channelIterator3;
                                        id = id4;
                                        $this$webSocketRaw = webSocketServerSession;
                                        anonymousClass1.L$0 = $this$webSocketRaw;
                                        anonymousClass1.L$1 = id;
                                        anonymousClass1.L$2 = worker;
                                        anonymousClass1.L$3 = it;
                                        anonymousClass1.label = 2;
                                        objHasNext = it.hasNext(anonymousClass1);
                                        if (objHasNext == $result5) {
                                        }
                                        break;
                                    case 3:
                                        worker = worker2;
                                        id = id2;
                                        $this$webSocketRaw = $this$webSocketRaw2;
                                        it = channelIterator;
                                        anonymousClass1.L$0 = $this$webSocketRaw;
                                        anonymousClass1.L$1 = id;
                                        anonymousClass1.L$2 = worker;
                                        anonymousClass1.L$3 = it;
                                        anonymousClass1.label = 2;
                                        objHasNext = it.hasNext(anonymousClass1);
                                        if (objHasNext == $result5) {
                                        }
                                        break;
                                    default:
                                        if (req.getFin()) {
                                            $result = $result2;
                                            anonymousClass14 = anonymousClass1;
                                            channelIterator4 = channelIterator;
                                        } else {
                                            anonymousClass1.L$0 = $this$webSocketRaw2;
                                            anonymousClass1.L$1 = id2;
                                            anonymousClass1.L$2 = worker2;
                                            anonymousClass1.L$3 = channelIterator;
                                            anonymousClass1.label = 8;
                                            if (SchedulerKt.sendCommand($this$webSocketRaw2, req, anonymousClass1) == $result5) {
                                                return $result5;
                                            }
                                            $result = $result2;
                                            anonymousClass14 = anonymousClass1;
                                            channelIterator4 = channelIterator;
                                        }
                                        anonymousClass14.L$0 = $this$webSocketRaw2;
                                        anonymousClass14.L$1 = id2;
                                        anonymousClass14.L$2 = worker2;
                                        anonymousClass14.L$3 = channelIterator4;
                                        anonymousClass14.label = 9;
                                        if (JobKt.cancelAndJoin(worker2, anonymousClass14) != $result5) {
                                            return $result5;
                                        }
                                        AnonymousClass1 anonymousClass15 = anonymousClass14;
                                        $result2 = $result;
                                        worker = worker2;
                                        id = id2;
                                        $this$webSocketRaw = $this$webSocketRaw2;
                                        it = channelIterator4;
                                        anonymousClass1 = anonymousClass15;
                                        anonymousClass1.L$0 = $this$webSocketRaw;
                                        anonymousClass1.L$1 = id;
                                        anonymousClass1.L$2 = worker;
                                        anonymousClass1.L$3 = it;
                                        anonymousClass1.label = 2;
                                        objHasNext = it.hasNext(anonymousClass1);
                                        if (objHasNext == $result5) {
                                        }
                                        break;
                                }
                            }
                            return Unit.INSTANCE;
                        case 1:
                            String id6 = (String) this.L$1;
                            WebSocketServerSession $this$webSocketRaw6 = (WebSocketServerSession) this.L$0;
                            ResultKt.throwOnFailure($result);
                            $this$webSocketRaw = $this$webSocketRaw6;
                            id = id6;
                            anonymousClass1 = this;
                            $result2 = $result;
                            worker = (Job) $result;
                            it = Scheduler.INSTANCE.incomingAgentStateOf($this$webSocketRaw, id).iterator();
                            anonymousClass1.L$0 = $this$webSocketRaw;
                            anonymousClass1.L$1 = id;
                            anonymousClass1.L$2 = worker;
                            anonymousClass1.L$3 = it;
                            anonymousClass1.label = 2;
                            objHasNext = it.hasNext(anonymousClass1);
                            if (objHasNext == $result5) {
                            }
                            break;
                        case 2:
                            ChannelIterator<AgentState> channelIterator5 = (ChannelIterator) this.L$3;
                            worker2 = (Job) this.L$2;
                            id2 = (String) this.L$1;
                            $this$webSocketRaw2 = (WebSocketServerSession) this.L$0;
                            ResultKt.throwOnFailure($result);
                            channelIterator = channelIterator5;
                            anonymousClass1 = this;
                            $result2 = $result;
                            if (((Boolean) $result).booleanValue()) {
                            }
                            return Unit.INSTANCE;
                        case 3:
                            worker3 = (Job) this.L$0;
                            ResultKt.throwOnFailure($result);
                            $result3 = $result5;
                            anonymousClass12 = this;
                            anonymousClass12.L$0 = null;
                            anonymousClass12.label = 4;
                            if (JobKt.cancelAndJoin(worker3, anonymousClass12) == $result3) {
                            }
                            return Unit.INSTANCE;
                        case 4:
                            anonymousClass12 = this;
                            ResultKt.throwOnFailure($result);
                            return Unit.INSTANCE;
                        case 5:
                            anonymousClass13 = this;
                            channelIterator2 = (ChannelIterator) anonymousClass13.L$2;
                            id3 = (String) anonymousClass13.L$1;
                            $this$webSocketRaw3 = (WebSocketServerSession) anonymousClass13.L$0;
                            ResultKt.throwOnFailure($result);
                            anonymousClass13.L$0 = $this$webSocketRaw3;
                            anonymousClass13.L$1 = id3;
                            anonymousClass13.L$2 = channelIterator2;
                            anonymousClass13.label = 6;
                            objLaunchWorker = SchedulerKt.launchWorker($this$webSocketRaw3, id3, anonymousClass13);
                            if (objLaunchWorker != $result5) {
                            }
                            break;
                        case 6:
                            ChannelIterator<AgentState> channelIterator6 = (ChannelIterator) this.L$2;
                            String id7 = (String) this.L$1;
                            WebSocketServerSession $this$webSocketRaw7 = (WebSocketServerSession) this.L$0;
                            ResultKt.throwOnFailure($result);
                            $this$webSocketRaw4 = $this$webSocketRaw7;
                            id4 = id7;
                            channelIterator3 = channelIterator6;
                            anonymousClass1 = this;
                            obj = $result5;
                            $result4 = $result;
                            worker = (Job) $result;
                            anonymousClass1.L$0 = $this$webSocketRaw4;
                            anonymousClass1.L$1 = id4;
                            anonymousClass1.L$2 = worker;
                            anonymousClass1.L$3 = channelIterator3;
                            anonymousClass1.label = 7;
                            if (SchedulerKt.sendCommand($this$webSocketRaw4, AgentState.PENDING, anonymousClass1) != obj) {
                            }
                            break;
                        case 7:
                            ChannelIterator<AgentState> channelIterator7 = (ChannelIterator) this.L$3;
                            Job worker4 = (Job) this.L$2;
                            String id8 = (String) this.L$1;
                            WebSocketServerSession $this$webSocketRaw8 = (WebSocketServerSession) this.L$0;
                            ResultKt.throwOnFailure($result);
                            $result2 = $result;
                            worker = worker4;
                            id = id8;
                            $this$webSocketRaw = $this$webSocketRaw8;
                            it = channelIterator7;
                            anonymousClass1 = this;
                            anonymousClass1.L$0 = $this$webSocketRaw;
                            anonymousClass1.L$1 = id;
                            anonymousClass1.L$2 = worker;
                            anonymousClass1.L$3 = it;
                            anonymousClass1.label = 2;
                            objHasNext = it.hasNext(anonymousClass1);
                            if (objHasNext == $result5) {
                            }
                            break;
                        case 8:
                            anonymousClass14 = this;
                            channelIterator4 = (ChannelIterator) anonymousClass14.L$3;
                            worker2 = (Job) anonymousClass14.L$2;
                            id2 = (String) anonymousClass14.L$1;
                            $this$webSocketRaw2 = (WebSocketServerSession) anonymousClass14.L$0;
                            ResultKt.throwOnFailure($result);
                            anonymousClass14.L$0 = $this$webSocketRaw2;
                            anonymousClass14.L$1 = id2;
                            anonymousClass14.L$2 = worker2;
                            anonymousClass14.L$3 = channelIterator4;
                            anonymousClass14.label = 9;
                            if (JobKt.cancelAndJoin(worker2, anonymousClass14) != $result5) {
                            }
                            break;
                        case 9:
                            ChannelIterator<AgentState> channelIterator8 = (ChannelIterator) this.L$3;
                            Job worker5 = (Job) this.L$2;
                            String id9 = (String) this.L$1;
                            WebSocketServerSession $this$webSocketRaw9 = (WebSocketServerSession) this.L$0;
                            ResultKt.throwOnFailure($result);
                            $result2 = $result;
                            worker = worker5;
                            id = id9;
                            $this$webSocketRaw = $this$webSocketRaw9;
                            it = channelIterator8;
                            anonymousClass1 = this;
                            anonymousClass1.L$0 = $this$webSocketRaw;
                            anonymousClass1.L$1 = id;
                            anonymousClass1.L$2 = worker;
                            anonymousClass1.L$3 = it;
                            anonymousClass1.label = 2;
                            objHasNext = it.hasNext(anonymousClass1);
                            if (objHasNext == $result5) {
                            }
                            break;
                        default:
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
            }

            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Routing routing) {
                invoke2(routing);
                return Unit.INSTANCE;
            }

            /* JADX INFO: renamed from: invoke, reason: avoid collision after fix types in other method */
            public final void invoke2(Routing routing) {
                Intrinsics.checkNotNullParameter(routing, "$this$routing");
                io.ktor.server.websocket.RoutingKt.webSocketRaw$default(routing, "/join/{id}", (String) null, new AnonymousClass1(null), 2, (Object) null);
                RoutingBuilderKt.get(routing, "/current/{id?}", new AnonymousClass2(null));
            }

            /* JADX INFO: renamed from: com.zhufucdev.motion_emulator.provider.SchedulerKt$eventServer$3$2, reason: invalid class name */
            /* JADX INFO: compiled from: Scheduler.kt */
            @Metadata(d1 = {"\u0000\u0010\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0010\u0000\u001a\u00020\u0001*\u000e\u0012\u0004\u0012\u00020\u0001\u0012\u0004\u0012\u00020\u00030\u00022\u0006\u0010\u0004\u001a\u00020\u0001H\u008a@"}, d2 = {"<anonymous>", "", "Lio/ktor/util/pipeline/PipelineContext;", "Lio/ktor/server/application/ApplicationCall;", "it"}, k = 3, mv = {1, 9, 0}, xi = 48)
            @DebugMetadata(c = "com.zhufucdev.motion_emulator.provider.SchedulerKt$eventServer$3$2", f = "Scheduler.kt", i = {}, l = {427, 435, 443}, m = "invokeSuspend", n = {}, s = {})
            static final class AnonymousClass2 extends SuspendLambda implements Function3<PipelineContext<Unit, ApplicationCall>, Unit, Continuation<? super Unit>, Object> {
                private /* synthetic */ Object L$0;
                int label;

                AnonymousClass2(Continuation<? super AnonymousClass2> continuation) {
                    super(3, continuation);
                }

                @Override // kotlin.jvm.functions.Function3
                public final Object invoke(PipelineContext<Unit, ApplicationCall> pipelineContext, Unit unit, Continuation<? super Unit> continuation) {
                    AnonymousClass2 anonymousClass2 = new AnonymousClass2(continuation);
                    anonymousClass2.L$0 = pipelineContext;
                    return anonymousClass2.invokeSuspend(Unit.INSTANCE);
                }

                @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
                public final Object invokeSuspend(Object $result) {
                    Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
                    switch (this.label) {
                        case 0:
                            ResultKt.throwOnFailure($result);
                            PipelineContext $this$call$iv = (PipelineContext) this.L$0;
                            String id = ((ApplicationCall) $this$call$iv.getContext()).getParameters().get("id");
                            if (id != null && Scheduler.INSTANCE.currentEmulationState(id) != AgentState.PENDING) {
                                ApplicationCall $this$respond$iv = (ApplicationCall) $this$call$iv.getContext();
                                Object message$iv = HttpStatusCode.INSTANCE.getForbidden();
                                if (!(message$iv instanceof OutgoingContent) && !(message$iv instanceof byte[])) {
                                    ApplicationResponse response = $this$respond$iv.getResponse();
                                    KType kType$iv$iv = Reflection.typeOf(HttpStatusCode.class);
                                    Type reifiedType$iv$iv = TypesJVMKt.getJavaType(kType$iv$iv);
                                    ResponseTypeKt.setResponseType(response, TypeInfoJvmKt.typeInfoImpl(reifiedType$iv$iv, Reflection.getOrCreateKotlinClass(HttpStatusCode.class), kType$iv$iv));
                                }
                                ApplicationSendPipeline pipeline = $this$respond$iv.getResponse().getPipeline();
                                Intrinsics.checkNotNull(message$iv, "null cannot be cast to non-null type kotlin.Any");
                                this.label = 1;
                                return pipeline.execute($this$respond$iv, message$iv, this) == coroutine_suspended ? coroutine_suspended : Unit.INSTANCE;
                            }
                            Emulation emulation = Scheduler.INSTANCE.getEmulation();
                            if (emulation == null) {
                                ApplicationCall $this$respond$iv2 = (ApplicationCall) $this$call$iv.getContext();
                                Object message$iv2 = HttpStatusCode.INSTANCE.getNotFound();
                                if (!(message$iv2 instanceof OutgoingContent) && !(message$iv2 instanceof byte[])) {
                                    ApplicationResponse response2 = $this$respond$iv2.getResponse();
                                    KType kType$iv$iv2 = Reflection.typeOf(HttpStatusCode.class);
                                    Type reifiedType$iv$iv2 = TypesJVMKt.getJavaType(kType$iv$iv2);
                                    ResponseTypeKt.setResponseType(response2, TypeInfoJvmKt.typeInfoImpl(reifiedType$iv$iv2, Reflection.getOrCreateKotlinClass(HttpStatusCode.class), kType$iv$iv2));
                                }
                                ApplicationSendPipeline pipeline2 = $this$respond$iv2.getResponse().getPipeline();
                                Intrinsics.checkNotNull(message$iv2, "null cannot be cast to non-null type kotlin.Any");
                                this.label = 2;
                                if (pipeline2.execute($this$respond$iv2, message$iv2, this) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                            } else {
                                ApplicationCall $this$respond$iv3 = (ApplicationCall) $this$call$iv.getContext();
                                if (!(emulation instanceof OutgoingContent) && !(emulation instanceof byte[])) {
                                    ApplicationResponse response3 = $this$respond$iv3.getResponse();
                                    KType kType$iv$iv3 = Reflection.typeOf(Emulation.class);
                                    Type reifiedType$iv$iv3 = TypesJVMKt.getJavaType(kType$iv$iv3);
                                    ResponseTypeKt.setResponseType(response3, TypeInfoJvmKt.typeInfoImpl(reifiedType$iv$iv3, Reflection.getOrCreateKotlinClass(Emulation.class), kType$iv$iv3));
                                }
                                this.label = 3;
                                if ($this$respond$iv3.getResponse().getPipeline().execute($this$respond$iv3, emulation, this) == coroutine_suspended) {
                                    return coroutine_suspended;
                                }
                            }
                            return Unit.INSTANCE;
                        case 1:
                            ResultKt.throwOnFailure($result);
                        case 2:
                            ResultKt.throwOnFailure($result);
                            return Unit.INSTANCE;
                        case 3:
                            ResultKt.throwOnFailure($result);
                            return Unit.INSTANCE;
                        default:
                            throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
                    }
                }
            }
        });
    }

    /* JADX INFO: renamed from: com.zhufucdev.motion_emulator.provider.SchedulerKt$launchWorker$2, reason: invalid class name and case insensitive filesystem */
    /* JADX INFO: compiled from: Scheduler.kt */
    @Metadata(d1 = {"\u0000\n\n\u0000\n\u0002\u0010\u0002\n\u0002\u0018\u0002\u0010\u0000\u001a\u00020\u0001*\u00020\u0002H\u008a@"}, d2 = {"<anonymous>", "", "Lkotlinx/coroutines/CoroutineScope;"}, k = 3, mv = {1, 9, 0}, xi = 48)
    @DebugMetadata(c = "com.zhufucdev.motion_emulator.provider.SchedulerKt$launchWorker$2", f = "Scheduler.kt", i = {}, l = {420, 397, 399, 427}, m = "invokeSuspend", n = {}, s = {})
    static final class C08502 extends SuspendLambda implements Function2<CoroutineScope, Continuation<? super Unit>, Object> {
        final /* synthetic */ String $id;
        final /* synthetic */ WebSocketServerSession $this_launchWorker;
        Object L$0;
        int label;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C08502(WebSocketServerSession webSocketServerSession, String str, Continuation<? super C08502> continuation) {
            super(2, continuation);
            this.$this_launchWorker = webSocketServerSession;
            this.$id = str;
        }

        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        public final Continuation<Unit> create(Object obj, Continuation<?> continuation) {
            return new C08502(this.$this_launchWorker, this.$id, continuation);
        }

        @Override // kotlin.jvm.functions.Function2
        public final Object invoke(CoroutineScope coroutineScope, Continuation<? super Unit> continuation) {
            return ((C08502) create(coroutineScope, continuation)).invokeSuspend(Unit.INSTANCE);
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Removed duplicated region for block: B:25:0x008d A[RETURN] */
        /* JADX WARN: Removed duplicated region for block: B:26:0x008e  */
        /* JADX WARN: Removed duplicated region for block: B:29:0x0099 A[Catch: Exception -> 0x014c, TryCatch #1 {Exception -> 0x014c, blocks: (B:23:0x007f, B:27:0x0091, B:29:0x0099, B:31:0x00a3, B:36:0x00bd, B:38:0x00c8, B:43:0x00d5, B:44:0x00de, B:46:0x00e8, B:50:0x00f3, B:51:0x00fb, B:22:0x006c), top: B:68:0x006c }] */
        /* JADX WARN: Removed duplicated region for block: B:59:0x0144 A[Catch: Exception -> 0x0140, TryCatch #0 {Exception -> 0x0140, blocks: (B:56:0x0132, B:59:0x0144, B:60:0x014b), top: B:66:0x0132 }] */
        /* JADX WARN: Removed duplicated region for block: B:66:0x0132 A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /* JADX WARN: Type inference failed for: r1v0, types: [int] */
        /* JADX WARN: Type inference failed for: r1v1 */
        /* JADX WARN: Type inference failed for: r1v18 */
        /* JADX WARN: Type inference failed for: r1v6 */
        /* JADX WARN: Type inference failed for: r3v0 */
        /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:54:0x012d -> B:55:0x0130). Please report as a decompilation issue!!! */
        @Override // kotlin.coroutines.jvm.internal.BaseContinuationImpl
        /*
            Code decompiled incorrectly, please refer to instructions dump.
        */
        public final Object invokeSuspend(Object $result) {
            Object $result2;
            C08502 c08502;
            ChannelIterator<Frame> it;
            ChannelIterator<Frame> channelIterator;
            Object $result3;
            C08502 c085022;
            Object objHasNext;
            Object coroutine_suspended = IntrinsicsKt.getCOROUTINE_SUSPENDED();
            ?? r1 = this.label;
            try {
            } catch (Exception e) {
                e = e;
            }
            switch (r1) {
                case 0:
                    ResultKt.throwOnFailure($result);
                    WebSocketServerSession webSocketServerSession = this.$this_launchWorker;
                    KType kType$iv$iv = Reflection.typeOf(EmulationInfo.class);
                    Type reifiedType$iv$iv = TypesJVMKt.getJavaType(kType$iv$iv);
                    this.label = 1;
                    Object objReceiveDeserialized = WebSocketServerSessionKt.receiveDeserialized(webSocketServerSession, TypeInfoJvmKt.typeInfoImpl(reifiedType$iv$iv, Reflection.getOrCreateKotlinClass(EmulationInfo.class), kType$iv$iv), this);
                    if (objReceiveDeserialized == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    $result2 = $result;
                    $result = objReceiveDeserialized;
                    c08502 = this;
                    try {
                        EmulationInfo info = (EmulationInfo) $result;
                        Scheduler.INSTANCE.notifyEmulationStarted$app_debug(c08502.$id, info);
                        it = c08502.$this_launchWorker.getIncoming().iterator();
                        c08502.L$0 = it;
                        c08502.label = 2;
                        objHasNext = it.hasNext(c08502);
                    } catch (Exception e2) {
                        e = e2;
                        $result = $result2;
                        r1 = c08502;
                    }
                    if (objHasNext == coroutine_suspended) {
                        return coroutine_suspended;
                    }
                    channelIterator = it;
                    $result = objHasNext;
                    if (((Boolean) $result).booleanValue()) {
                        Frame frame = channelIterator.next();
                        if (frame instanceof Frame.Close) {
                            c08502.L$0 = null;
                            c08502.label = 3;
                            if (WebSocketSessionKt.close$default(c08502.$this_launchWorker, null, c08502, 1, null) == coroutine_suspended) {
                                return coroutine_suspended;
                            }
                            $result = $result2;
                            $result3 = c08502;
                        } else {
                            Byte bSingleOrNull = ArraysKt.singleOrNull(frame.getData());
                            boolean z = false;
                            if (bSingleOrNull != null && bSingleOrNull.byteValue() == 127) {
                                Scheduler.INSTANCE.notifyEmulationCompleted$app_debug(c08502.$id);
                            } else {
                                Byte bSingleOrNull2 = ArraysKt.singleOrNull(frame.getData());
                                if (bSingleOrNull2 != null && bSingleOrNull2.byteValue() == -127) {
                                    z = true;
                                }
                                if (z) {
                                    Scheduler.INSTANCE.notifyEmulationFailed$app_debug(c08502.$id);
                                } else {
                                    WebsocketContentConverter converter = WebSocketServerSessionKt.getConverter(c08502.$this_launchWorker);
                                    Intrinsics.checkNotNull(converter);
                                    Charset charset = Charsets.UTF_8;
                                    KType kType$iv$iv2 = Reflection.typeOf(Intermediate.class);
                                    Type reifiedType$iv$iv2 = TypesJVMKt.getJavaType(kType$iv$iv2);
                                    c08502.L$0 = channelIterator;
                                    c08502.label = 4;
                                    Object objDeserialize = converter.deserialize(charset, TypeInfoJvmKt.typeInfoImpl(reifiedType$iv$iv2, Reflection.getOrCreateKotlinClass(Intermediate.class), kType$iv$iv2), frame, c08502);
                                    if (objDeserialize == coroutine_suspended) {
                                        return coroutine_suspended;
                                    }
                                    $result = objDeserialize;
                                    c085022 = c08502;
                                    if ($result != null) {
                                        throw new NullPointerException("null cannot be cast to non-null type com.zhufucdev.me.stub.Intermediate");
                                    }
                                    try {
                                        Intermediate data = (Intermediate) $result;
                                        Scheduler.INSTANCE.setIntermediate(c085022.$id, data);
                                        it = channelIterator;
                                        c08502 = c085022;
                                        c08502.L$0 = it;
                                        c08502.label = 2;
                                        objHasNext = it.hasNext(c08502);
                                        if (objHasNext == coroutine_suspended) {
                                        }
                                    } catch (Exception e3) {
                                        e = e3;
                                        $result = $result2;
                                        r1 = c085022;
                                        e.printStackTrace();
                                    }
                                    e = e3;
                                    $result = $result2;
                                    r1 = c085022;
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                    return Unit.INSTANCE;
                case 1:
                    ResultKt.throwOnFailure($result);
                    c08502 = this;
                    $result2 = $result;
                    EmulationInfo info2 = (EmulationInfo) $result;
                    Scheduler.INSTANCE.notifyEmulationStarted$app_debug(c08502.$id, info2);
                    it = c08502.$this_launchWorker.getIncoming().iterator();
                    c08502.L$0 = it;
                    c08502.label = 2;
                    objHasNext = it.hasNext(c08502);
                    if (objHasNext == coroutine_suspended) {
                    }
                    break;
                case 2:
                    ChannelIterator<Frame> channelIterator2 = (ChannelIterator) this.L$0;
                    ResultKt.throwOnFailure($result);
                    channelIterator = channelIterator2;
                    c08502 = this;
                    $result2 = $result;
                    if (((Boolean) $result).booleanValue()) {
                    }
                    return Unit.INSTANCE;
                case 3:
                    $result3 = this;
                    ResultKt.throwOnFailure($result);
                    return Unit.INSTANCE;
                case 4:
                    channelIterator = (ChannelIterator) this.L$0;
                    ResultKt.throwOnFailure($result);
                    c085022 = this;
                    $result2 = $result;
                    if ($result != null) {
                    }
                    e = e3;
                    $result = $result2;
                    r1 = c085022;
                    e.printStackTrace();
                    return Unit.INSTANCE;
                default:
                    throw new IllegalStateException("call to 'resume' before 'invoke' with coroutine");
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object launchWorker(WebSocketServerSession $this$launchWorker, String id, Continuation<? super Job> continuation) {
        return BuildersKt__Builders_commonKt.launch$default($this$launchWorker, null, null, new C08502($this$launchWorker, id, null), 3, null);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object sendCommand(WebSocketSession $this$sendCommand, AgentState state, Continuation<? super Unit> continuation) {
        Object objSend = WebSocketSessionKt.send($this$sendCommand, new byte[]{(byte) state.ordinal()}, continuation);
        return objSend == IntrinsicsKt.getCOROUTINE_SUSPENDED() ? objSend : Unit.INSTANCE;
    }
}
