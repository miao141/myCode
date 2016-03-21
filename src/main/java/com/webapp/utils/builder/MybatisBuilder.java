/*
 * Decompiled with CFR 0_115.
 * 
 * Could not load the following classes:
 *  cn.org.rapid_framework.generator.Generator
 *  cn.org.rapid_framework.generator.GeneratorControl
 *  cn.org.rapid_framework.generator.GeneratorFacade
 *  cn.org.rapid_framework.generator.GeneratorProperties
 *  com.webapp.utils.string.Utils
 *  org.slf4j.Logger
 *  org.slf4j.LoggerFactory
 */
package com.webapp.utils.builder;

import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.GeneratorControl;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;
import com.webapp.utils.string.Utils;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class MybatisBuilder {
    private static final Logger logger = LoggerFactory.getLogger((Class)MybatisBuilder.class);
    private static final String PASSWORD = "password";
    private static final String USERNAME = "username";
    private static final String DRIVER = "driver";
    private static final String URL = "url";

    public static void viewProp(String jdbcCfg) {
        MybatisBuilder.setCfg(jdbcCfg);
        Properties props = GeneratorProperties.getProperties();
        TreeMap<String, String> map = new TreeMap<String, String>();
        Enumeration keys = props.keys();
        while (keys.hasMoreElements()) {
            Object next = keys.nextElement();
            map.put(next.toString(), props.getProperty(next.toString()));
        }
        for (String next : map.keySet()) {
            logger.info("{} --> {}", (Object)next, map.get(next));
        }
    }

    public static void buildByTable(String jdbcCfg, String table) {
        MybatisBuilder.buildByTable(jdbcCfg, null, table);
    }

    public static void buildByTable(String jdbcCfg, List<String> tables) {
        MybatisBuilder.buildByTable(jdbcCfg, null, tables);
    }

    public static void buildByTable(String jdbcCfg, String basePkg, List<String> tables) {
        GeneratorFacade gf = MybatisBuilder.build(jdbcCfg, basePkg);
        tables.forEach(x -> {
            Utils.toSnake((String)x);
        }
        );
        try {
            gf.deleteOutRootDir();
            gf.generateByTable(tables.toArray(new String[0]));
        }
        catch (Exception e) {
            logger.info(" error buildByTable ", (Throwable)e);
        }
    }

    public static void buildByTable(String jdbcCfg, String basePkg, String table) {
        GeneratorFacade gf = MybatisBuilder.build(jdbcCfg, basePkg);
        try {
            gf.deleteOutRootDir();
            gf.generateByTable(new String[]{Utils.toSnake((String)table)});
        }
        catch (Exception e) {
            logger.info(" error buildByTable ", (Throwable)e);
        }
    }

    public static void buildAll(String jdbcCfg, String basePkg) {
        GeneratorFacade gf = MybatisBuilder.build(jdbcCfg, basePkg);
        try {
            gf.deleteOutRootDir();
            gf.generateByAllTable();
        }
        catch (Exception e) {
            logger.info(" error buildAll ", (Throwable)e);
        }
    }

    public static void buildAll(String jdbcCfg) {
        MybatisBuilder.buildAll(jdbcCfg, null);
    }

    private static GeneratorFacade build(String jdbcCfg, String basePkg) {
        MybatisBuilder.setCfg(jdbcCfg);
        if (basePkg != null) {
            GeneratorProperties.setProperty((String)"basepackage", (String)basePkg);
        }
        GeneratorControl gc = new GeneratorControl();
        gc.setOverride(true);
        GeneratorFacade gf = new GeneratorFacade();
        gf.getGenerator().addTemplateRootDir(TemplatType.simple.getTemplate());
        return gf;
    }

    private static void setCfg(String jdbcCfg) {
        GeneratorProperties.setProperty((String)"outRoot", (String)(System.getProperty("user.dir") + "/codes/"));
        Properties jdbc = new Properties();
        try {
            jdbc.load(MybatisBuilder.class.getResourceAsStream("/" + jdbcCfg));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        MybatisBuilder.setJdbc(jdbc, "url");
        MybatisBuilder.setJdbc(jdbc, "driver");
        MybatisBuilder.setJdbc(jdbc, "username");
        MybatisBuilder.setJdbc(jdbc, "password");
    }

    private static void setJdbc(Properties jdbc, String key) {
        String jdbc_key = "jdbc_" + key;
        if (jdbc.containsKey(jdbc_key)) {
            GeneratorProperties.setProperty((String)jdbc_key, (String)jdbc.getProperty(jdbc_key));
        } else if (jdbc.containsKey(key)) {
            GeneratorProperties.setProperty((String)jdbc_key, (String)jdbc.getProperty(key));
        } else {
            logger.error("Must contain {} or {} configuration", (Object)jdbc_key, (Object)key);
        }
    }

    public static enum TemplatType {
        simple("classpath:/template/simple"),
        view("classpath:/template/view"),
        my("classpath:/template/my");
        
        private String template;

        private TemplatType(String template) {
            this.template = template;
        }

        public String getTemplate() {
            return this.template;
        }
    }

}
