package com.test.miao;

import java.util.Arrays;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.webapp.utils.builder.MybatisBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class Test {
	
    
  
    
	public static void main(String[] args) {
	    
	    MybatisBuilder.buildByTable("com.ucf.staging.dao.user", Arrays.asList("user"));
	    
		/*MybatisConfiguration my = new MybatisConfiguration();
		//AutoGeneratorTest test = new AutoGeneratorTest();
		ConfigGenerator config = new ConfigGenerator();
		config.setDbDriverName("com.mysql.jdbc.Driver");
		config.setDbPassword("62386997");
		config.setDbUrl("jdbc:mysql://localhost:3306/jeeshop");
		config.setDbUser("root");
		config.setMapperPackage("D:/d");
		config.setSaveDir("D:/d");
		AutoGenerator ag = new AutoGenerator(config);
		ag.run(config);
		//ag.run(config);
*/	
	    
	}
}
