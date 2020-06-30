package com.lening.excel;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public  @interface ExcelAttribute {
    //对应的列名称
    String name() default ""; // default java8新特性
    //列序号
    int sort() default -1;
    //字段类型对应的格式
    String format() default "";
}