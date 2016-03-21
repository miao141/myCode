package com.test.miao;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.webapp.utils.builder.MybatisBuilder;

 

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath*:applicationContext.xml")
public class LocalTest {
    @Autowired
    protected WebApplicationContext wac;
    protected MockMvc mvc;
    
    @Before
    public void runBefore() {
        mvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    @Test
    public void ATestA(){
    //    MybatisBuilder.buildByTable("com.ucf.staging.dao.order", Arrays.asList("order_item"));
        
      /*  MybatisBuilder.buildByTable("build_dev.properties", "com.ucf.staging.dao.order",
                Arrays.asList("order_main"));*/
        //buildAll
        MybatisBuilder.buildByTable("build_dev.properties", "com.ucf.staging.dao.order",Arrays.asList("order_main"));
    }
}
