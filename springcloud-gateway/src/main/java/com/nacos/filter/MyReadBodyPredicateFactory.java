//package com.nacos.filter;
//
//import com.alibaba.nacos.common.utils.ArrayUtils;
//import org.reactivestreams.Publisher;
//import org.springframework.cloud.gateway.handler.AsyncPredicate;
//import org.springframework.cloud.gateway.handler.predicate.AbstractRoutePredicateFactory;
//import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
//import org.springframework.http.codec.HttpMessageReader;
//import org.springframework.stereotype.Component;
//import org.springframework.web.reactive.function.server.HandlerStrategies;
//import org.springframework.web.reactive.function.server.ServerRequest;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.util.List;
//import java.util.function.Predicate;
//
//@Component()
//public class MyReadBodyPredicateFactory extends AbstractRoutePredicateFactory<MyReadBodyPredicateFactory.Config> {
//
//
//    private static final List<HttpMessageReader<?>> messageReaders = HandlerStrategies
//            .withDefaults().messageReaders();
//
//    public MyReadBodyPredicateFactory() {
//        super(MyReadBodyPredicateFactory.Config.class);
//    }
//
//    public MyReadBodyPredicateFactory(Class<MyReadBodyPredicateFactory.Config> configClass) {
//        super(configClass);
//    }
//
//    @Override
//    public AsyncPredicate<ServerWebExchange> applyAsync(MyReadBodyPredicateFactory.Config config) {
//        return new AsyncPredicate<ServerWebExchange>() {
//            @Override
//            public Publisher<Boolean> apply(ServerWebExchange exchange) {
//                Object cachedBody = exchange.getAttribute("cachedRequestBodyObject");
////                if (cachedBody != null) {
////                    try {
////                        boolean test = match(config.sceneIds, (MycRequest) cachedBody);
////                        return Mono.just(test);
////                    } catch (ClassCastException e) {
////                        if (log.isDebugEnabled()) {
////                            log.debug("Predicate test failed because class in predicate "
////                                    + "does not match the cached body object", e);
////                        }
////                    }
////                    return Mono.just(false);
////                } else {
////                    return ServerWebExchangeUtils.cacheRequestBodyAndRequest(exchange,
////                            (serverHttpRequest) -> ServerRequest.create(exchange.mutate().request(serverHttpRequest).build(), messageReaders)
////                                    .bodyToMono(MycRequest.class)
////                                    .doOnNext(objectValue -> exchange.getAttributes().put("cachedRequestBodyObject",objectValue))
////                                    .map(objectValue -> { return match(config.sceneIds, objectValue);}));
////                }
//
//                return Mono.just(true);
//            }
//        };
//    }
//
////    private boolean match(String params, MycRequest mycRequest) {
////        if("others".equals(params)){
////            return true;
////        }
////        String[] paramArray = params.split(",");
////        if (ArrayUtils.contains(paramArray, mycRequest.getRouteId)) {
////            return true;
////        } else {
////            return false;
////        }
////    }
//
//    @Override
//    public Predicate<ServerWebExchange> apply(MyReadBodyPredicateFactory.Config config) {
//        throw new UnsupportedOperationException(
//                "MyReadBodyPredicateFactory is only async.");
//    }
//
//    public static class Config {
//
//        private String params;
//
//        public MyReadBodyPredicateFactory.Config setParams(String params) {
//            this.params = params;
//            return this;
//        }
//
//        public String getParams() {
//            return params;
//        }
//    }
//}
