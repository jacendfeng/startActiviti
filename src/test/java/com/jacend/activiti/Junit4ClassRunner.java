package com.jacend.activiti;

import org.junit.runners.model.InitializationError;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Log4jConfigurer;

import java.io.FileNotFoundException;

/**
 * @author fengxf
 * @since 2016-05-21
 */
public class Junit4ClassRunner extends SpringJUnit4ClassRunner{

    static {
        try {
            Log4jConfigurer.initLogging("classpath:log4j.properties");
        }catch (FileNotFoundException e){
            System.err.println("Cannot init log4j");
        }
    }

    public Junit4ClassRunner(Class<?> clazz) throws InitializationError {
        super(clazz);
    }
}
