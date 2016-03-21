package com.webapp.utils.builder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.org.rapid_framework.generator.Generator;
import cn.org.rapid_framework.generator.GeneratorControl;
import cn.org.rapid_framework.generator.GeneratorFacade;
import cn.org.rapid_framework.generator.GeneratorProperties;

import com.webapp.utils.builder.MybatisBuilder.TemplatType;
import com.webapp.utils.string.Utils;

public class MyTest {
    private static final Logger logger = LoggerFactory.getLogger(MyTest.class);
    
    public static void main(String[] args) {
        
        GeneratorFacade gf = new GeneratorFacade();
        Generator generator = new Generator();
        GeneratorControl gc = new GeneratorControl();
        
        GeneratorProperties.setProperty((String)"outRoot", (String)(System.getProperty("user.dir") + "/codes/"));
        Properties jdbc = new Properties();
        try {
            jdbc.load(MybatisBuilder.class.getResourceAsStream("/" + "build_dev.properties"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        List list = new ArrayList();
        list.add("url");
        list.add("driver");
        list.add("username");
        list.add("password");
        for (int i = 0; i < list.size(); i++) {
            String key = list.get(i).toString();
            String jdbc_key = "jdbc_" + key;
            if (jdbc.containsKey(jdbc_key)) {
                GeneratorProperties.setProperty((String)jdbc_key, (String)jdbc.getProperty(jdbc_key));
            } else if (jdbc.containsKey(key)) {
                GeneratorProperties.setProperty((String)jdbc_key, (String)jdbc.getProperty(key));
            } else {
                logger.error("Must contain {} or {} configuration", (Object)jdbc_key, (Object)key);
            }
        }
  
        gc.setOverride(true);
       
        gf.getGenerator().addTemplateRootDir(TemplatType.simple.getTemplate());
        
        try {
            gf.deleteOutRootDir();
            gf.generateByTable(new String[]{Utils.toSnake((String)"order_main")});
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
     
        
       /* try {
          
            gf.printAllTableNames();
            generator.setTemplateRootDir(TemplatType.simple.getTemplate());
            generator.setOutRootDir("D:/ddd");
            gf.setGenerator(generator);
            
            String[] tables = {"region"};
         
            gf.generateByTable(tables);
          
            
          //  generatorFacade.generateByTable("region", "G:/javaSpaceFive/rapidTest/templateMy");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
         
    }

}
