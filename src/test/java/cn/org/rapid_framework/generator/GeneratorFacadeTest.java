package cn.org.rapid_framework.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.webapp.utils.builder.MybatisBuilder;
import com.webapp.utils.builder.MybatisBuilder.TemplatType;
import com.webapp.utils.string.Utils;

/**
 * Created with IntelliJ IDEA.
 * User: chenyuan
 * Date: 7/11/14
 * Time: 15:44
 * To change this template use File | Settings | File Templates.
 */
public class GeneratorFacadeTest {
    private Logger logger = LoggerFactory.getLogger(GeneratorFacadeTest.class);

    private GeneratorFacade generatorFacade;

    @Before
    public void create(){
        generatorFacade = new GeneratorFacade();
    }

    @Test
    public void testPrintAllTableNames() throws Exception {
        logger.info("###print all table names start");
        generatorFacade.printAllTableNames();
        //Arrays.asList("order_main")
     //   generatorFacade.generateByTable( );
        logger.info("###print all table names end");
    }

    @Test
    public void testDeleteOutRootDir() throws Exception {

    }

    @Test
    public void testGenerateByMap() throws Exception {

    }

    @Test
    public void testDeleteByMap() throws Exception {

    }

    @Test
    public void testGenerateByAllTable() throws Exception {

    }

    @Test
    public void testDeleteByAllTable() throws Exception {

    }

    @Test
    public void testGenerateByTable() throws Exception {
         
            Generator generator = new Generator();
            GeneratorControl gc = new GeneratorControl();
            String basePkg = "com.ucf.staging.dao.order";
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
            if (basePkg != null) {
                GeneratorProperties.setProperty((String)"basepackage", (String)basePkg);
            }
            gc.setOverride(true);
            
            
            GeneratorFacade gf = new GeneratorFacade();
            gf.getGenerator().addTemplateRootDir(TemplatType.simple.getTemplate());
            
            try {
                gf.deleteOutRootDir();
                gf.generateByTable(new String[]{Utils.toSnake((String)"orderMain")});
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }

    @Test
    public void testDeleteByTable() throws Exception {

    }

    @Test
    public void testGenerateByClass() throws Exception {

    }

    @Test
    public void testDeleteByClass() throws Exception {

    }

    @Test
    public void testGenerateBySql() throws Exception {

    }

    @Test
    public void testDeleteBySql() throws Exception {

    }
}
