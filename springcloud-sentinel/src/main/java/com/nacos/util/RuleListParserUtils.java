package com.nacos.util;

import java.util.List;

import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 规则的编码和解码操作类
 */
public class RuleListParserUtils {

    /**
     * 流控列表解析器
     */
    public static final Converter<String, List<FlowRule>> flowRuleListParser = new Converter<String, List<FlowRule>>() {
        @Override
        public List<FlowRule> convert(String source) {
            return JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
            });
        }
    };

    /**
     * 流控列表 编码器
     */
    public static final Converter<List<FlowRule>, String> flowRuleEnCoding = new Converter<List<FlowRule>, String>() {
        @Override
        public String convert(List<FlowRule> source) {
            return JSON.toJSONString(source);
        }
    };

    public static final Converter<String, List<DegradeRule>> degradeRuleListParser = source -> JSON.parseObject(source,
            new TypeReference<List<DegradeRule>>() {
            });

    public static final Converter<List<DegradeRule>, String> degradeRuleEnCoding = new Converter<List<DegradeRule>, String>() {
        @Override
        public String convert(List<DegradeRule> source) {
            return JSON.toJSONString(source);
        }
    };

    public static final Converter<String, List<SystemRule>> systemRuleListParser = source -> JSON.parseObject(source,
            new TypeReference<List<SystemRule>>() {
            });

    public static final Converter<List<SystemRule>, String> systemRuleEnCoding = new Converter<List<SystemRule>, String>() {
        @Override
        public String convert(List<SystemRule> source) {
            return JSON.toJSONString(source);
        }
    };

    public static final Converter<String, List<AuthorityRule>> authorityRuleListParser = source -> JSON
            .parseObject(source, new TypeReference<List<AuthorityRule>>() {
            });


    public static final Converter<List<AuthorityRule>, String> authorityRuleEnCoding = new Converter<List<AuthorityRule>, String>() {
        @Override
        public String convert(List<AuthorityRule> source) {
            return JSON.toJSONString(source);
        }
    };

    public static final Converter<String, List<ParamFlowRule>> paramFlowRuleListParser = source -> JSON
            .parseObject(source, new TypeReference<List<ParamFlowRule>>() {
            });


//    public static final Converter<List<ParamFlowRule>, String> paramFlowRuleEnCoding = new Converter<List<ParamFlowRule>, String>() {
//        @Override
//        public String convert(List<ParamFlowRule> source) {
//            return JSON.toJSONString(source);
//        }
//    };

    public static final Converter<List<ParamFlowRule>, String> paramFlowRuleEnCoding = source -> encodeJson(source);

    private static <T> String encodeJson(T t) {
        return JSON.toJSONString(t);
    }

}
