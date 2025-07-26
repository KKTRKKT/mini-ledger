package me.kktrkkt.miniledger.infra.entity;

import org.hibernate.HibernateException;
import org.hibernate.MappingException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.enhanced.SequenceStyleGenerator;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.type.BasicType;
import org.hibernate.type.Type;
import org.hibernate.type.descriptor.java.JavaType;
import org.hibernate.type.descriptor.java.LongJavaType;
import org.hibernate.type.descriptor.jdbc.BigIntJdbcType;
import org.hibernate.type.internal.BasicTypeImpl;

import java.io.Serializable;
import java.util.Properties;

public class PrefixSequenceGenerator extends SequenceStyleGenerator {

    private final String prefix;
    private final String numberFormat;

    // 생성자 - 어노테이션 값과 연동됨!
    public PrefixSequenceGenerator(CustomSequence annotation) {
        this.prefix = annotation.prefix();
        this.numberFormat = annotation.numberFormat();
    }

    @Override
    public void configure(Type type, Properties params, ServiceRegistry serviceRegistry) throws MappingException {
        JavaType<Long> javaType = LongJavaType.INSTANCE;
        BasicType<Long> longType = new BasicTypeImpl<>(javaType, BigIntJdbcType.INSTANCE);
        super.configure(longType, params, serviceRegistry);
    }

    @Override
    public Serializable generate(SharedSessionContractImplementor session,
                                 Object object) throws HibernateException {
        return this.prefix + String.format(this.numberFormat, super.generate(session, object));
    }
}
