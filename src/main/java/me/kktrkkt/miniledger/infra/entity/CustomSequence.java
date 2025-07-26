package me.kktrkkt.miniledger.infra.entity;

import org.hibernate.annotations.IdGeneratorType;

import java.lang.annotation.*;

@IdGeneratorType(PrefixSequenceGenerator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface CustomSequence {
    String prefix() default "ID";
    String numberFormat() default "%05d";
}
