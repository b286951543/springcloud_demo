package com.nacos.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 常量类，主要是定义规则文件的目录和名字
 */
public class PersistenceRuleConstant {

    /**
     * 存储文件路径
     */
    public static final String storePath = "./sentinel/rules/";

    /**
     * 各种存储sentinel规则映射map
     */
    public static final Map<String,String> rulesMap = new HashMap<>();

    //流控规则文件
    public static final String FLOW_RULE_PATH = "flowRulePath";

    //降级规则文件
    public static final String DEGRAGE_RULE_PATH = "degradeRulePath";

    //授权规则文件
    public static final String AUTH_RULE_PATH = "authRulePath";

    //系统规则文件
    public static final String SYSTEM_RULE_PATH = "systemRulePath";

    //热点参数文件
    public static final String HOT_PARAM_RULE = "hotParamRulePath";

    static {
        rulesMap.put(FLOW_RULE_PATH,storePath+"flowRule.json");
        rulesMap.put(DEGRAGE_RULE_PATH,storePath+"degradeRule.json");
        rulesMap.put(SYSTEM_RULE_PATH,storePath+"systemRule.json");
        rulesMap.put(AUTH_RULE_PATH,storePath+"authRule.json");
        rulesMap.put(HOT_PARAM_RULE,storePath+"hotParamRule.json");
    }
}
