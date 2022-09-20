package com.nacos.util;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 文件操作类，如果规则文件不存在就创建对应的目录和对应的规则文件
 */
public class RuleFileUtils {

    private static final Logger logger = LoggerFactory.getLogger(RuleFileUtils.class);

    /**
     * 方法实现说明:若路径不存在就创建路径
     * @param filePath:文件存储路径
     */
    public static void mkdirIfNotExits(String filePath) throws IOException {
        File file = new File(filePath);
        if(!file.exists()) {
            logger.info("创建Sentinel规则目录:{}",filePath);
            file.mkdirs();
        }
    }


    /**
     * 方法实现说明:若文件不存在就创建路径
     * @param ruleFileMap 规则存储文件
     */
    public static void createFileIfNotExits(Map<String,String> ruleFileMap) throws IOException {

        Set<String> ruleFilePathSet = ruleFileMap.keySet();

        Iterator<String> ruleFilePathIter = ruleFilePathSet.iterator();

        while (ruleFilePathIter.hasNext()) {
            String ruleFilePathKey = ruleFilePathIter.next();
            String ruleFilePath  = PersistenceRuleConstant.rulesMap.get(ruleFilePathKey).toString();
            File ruleFile = new File(ruleFilePath);
            if(!ruleFile.exists()) {
                logger.info("创建 Sentinel 规则文件:{}",ruleFile);
                ruleFile.createNewFile();
            }
        }
    }
}
