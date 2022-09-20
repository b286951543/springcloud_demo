package com.nacos.util;

import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.csp.sentinel.command.handler.ModifyParamFlowRulesCommandHandler;
import com.alibaba.csp.sentinel.datasource.FileRefreshableDataSource;
import com.alibaba.csp.sentinel.datasource.FileWritableDataSource;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.WritableDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRuleManager;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.alibaba.csp.sentinel.slots.system.SystemRuleManager;
import com.alibaba.csp.sentinel.transport.util.WritableDataSourceRegistry;

/**
 * 具体pull模式操作类
 */
public class PullModeLocalFileDataSource implements InitFunc {

    private static final Logger logger = LoggerFactory.getLogger(PullModeLocalFileDataSource.class);

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    @Override
    public void init() throws Exception {
        logger.info("time:{}读取配置", sdf.format(new Date()));
        try {
            // 创建文件存储目录(若路径不存在就创建路径)
            RuleFileUtils.mkdirIfNotExits(PersistenceRuleConstant.storePath);
            // 创建规则文件()
            RuleFileUtils.createFileIfNotExits(PersistenceRuleConstant.rulesMap);
            // 处理流控规则逻辑
            dealFlowRules();
            // 处理降级规则
            dealDegradeRules();
            // 处理系统规则
            dealSystemRules();
            // 热点参数规则
            dealParamFlowRules();
            // 授权规则
            dealAuthRules();
        } catch (Exception e) {
            logger.error("错误原因:{}", e);
        }

    }

    /**
     * 方法实现说明:处理流控规则逻辑
     */
    private void dealFlowRules() throws FileNotFoundException {
        String ruleFilePath = PersistenceRuleConstant.rulesMap.get(PersistenceRuleConstant.FLOW_RULE_PATH).toString();
        // 创建流控规则的可读数据源
        ReadableDataSource<String, List<FlowRule>> flowRuleRDS = new FileRefreshableDataSource<>(ruleFilePath,
                RuleListParserUtils.flowRuleListParser);
        // 将可读数据源注册至FlowRuleManager 这样当规则文件发生变化时，就会更新规则到内存
        FlowRuleManager.register2Property(flowRuleRDS.getProperty());
        WritableDataSource<List<FlowRule>> flowRuleWDS = new FileWritableDataSource<List<FlowRule>>(ruleFilePath,
                RuleListParserUtils.flowRuleEnCoding);

        // 将可写数据源注册至 transport 模块的 WritableDataSourceRegistry 中.
        // 这样收到控制台推送的规则时，Sentinel 会先更新到内存，然后将规则写入到文件中.
        WritableDataSourceRegistry.registerFlowDataSource(flowRuleWDS);
    }

    // 处理降级规则
    private void dealDegradeRules() throws FileNotFoundException {
        String degradeRulePath = PersistenceRuleConstant.rulesMap.get(PersistenceRuleConstant.DEGRAGE_RULE_PATH).toString();
        // 降级规则
        ReadableDataSource<String, List<DegradeRule>> degradeRuleRDS = new FileRefreshableDataSource<>(degradeRulePath,
                RuleListParserUtils.degradeRuleListParser);
        DegradeRuleManager.register2Property(degradeRuleRDS.getProperty());
        WritableDataSource<List<DegradeRule>> degradeRuleWDS = new FileWritableDataSource<>(degradeRulePath,
                RuleListParserUtils.degradeRuleEnCoding);
        WritableDataSourceRegistry.registerDegradeDataSource(degradeRuleWDS);

    }

    // 处理系统规则
    private void dealSystemRules() throws FileNotFoundException {
        String systemRulePath = PersistenceRuleConstant.rulesMap.get(PersistenceRuleConstant.SYSTEM_RULE_PATH).toString();
        // 系统规则
        ReadableDataSource<String, List<SystemRule>> systemRuleRDS = new FileRefreshableDataSource<>(systemRulePath,
                RuleListParserUtils.systemRuleListParser);
        SystemRuleManager.register2Property(systemRuleRDS.getProperty());
        WritableDataSource<List<SystemRule>> systemRuleWDS = new FileWritableDataSource<>(systemRulePath,
                RuleListParserUtils.systemRuleEnCoding);
        WritableDataSourceRegistry.registerSystemDataSource(systemRuleWDS);
    }

    // 热点参数规则
    private void dealParamFlowRules() throws FileNotFoundException {
        String paramFlowRulePath = PersistenceRuleConstant.rulesMap.get(PersistenceRuleConstant.HOT_PARAM_RULE).toString();
        // 热点参数规则
        ReadableDataSource<String, List<ParamFlowRule>> paramFlowRuleRDS = new FileRefreshableDataSource<>(
                paramFlowRulePath, RuleListParserUtils.paramFlowRuleListParser);
        ParamFlowRuleManager.register2Property(paramFlowRuleRDS.getProperty());
        WritableDataSource<List<ParamFlowRule>> paramFlowRuleWDS = new FileWritableDataSource<>(paramFlowRulePath,
                RuleListParserUtils.paramFlowRuleEnCoding);
        ModifyParamFlowRulesCommandHandler.setWritableDataSource(paramFlowRuleWDS);
    }

    private void dealAuthRules() throws FileNotFoundException {
        String authFlowRulePath = PersistenceRuleConstant.rulesMap.get(PersistenceRuleConstant.AUTH_RULE_PATH).toString();
        // 授权规则
        ReadableDataSource<String, List<AuthorityRule>> authorityRuleRDS = new FileRefreshableDataSource<>(authFlowRulePath,
                RuleListParserUtils.authorityRuleListParser);
        AuthorityRuleManager.register2Property(authorityRuleRDS.getProperty());
        WritableDataSource<List<AuthorityRule>> authorityRuleWDS = new FileWritableDataSource<>(authFlowRulePath,
                RuleListParserUtils.authorityRuleEnCoding);
        WritableDataSourceRegistry.registerAuthorityDataSource(authorityRuleWDS);
    }
}
