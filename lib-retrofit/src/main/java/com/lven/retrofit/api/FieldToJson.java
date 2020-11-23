package com.lven.retrofit.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记的字段转成Json
 */
@Target(ElementType.FIELD)// 注解作用在字段上
@Retention(RetentionPolicy.RUNTIME)// 在类运行的时候注解
public @interface FieldToJson {
}
