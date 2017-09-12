package com.til.prime.timesSubscription.pojo;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamOmitField;
import com.thoughtworks.xstream.mapper.CannotResolveClassException;
import com.thoughtworks.xstream.mapper.MapperWrapper;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.usertype.UserType;
import org.springframework.beans.BeanUtils;
import org.springframework.util.StringUtils;

import java.beans.PropertyDescriptor;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class XMLUserType implements UserType {

    private static final Logger LOG = Logger.getLogger(XMLUserType.class);
    private static final int[] SQL_TYPES = {Types.CLOB};
    public static final XStream XSTREAM = new GracefulXStream();

    private static class GracefulXStream extends XStream {

        @Override
        protected MapperWrapper wrapMapper(MapperWrapper next) {

            return new MapperWrapper(next) {
                @Override
                public boolean shouldSerializeMember(Class definedIn, String fieldName) {

                    try {
                        Class studiedClass = definedIn;
                        while (studiedClass != null) {
                            List<String> fieldNames = collect(Arrays.asList(studiedClass.getDeclaredFields()), "name");
                            if (fieldNames.contains(fieldName)) {
                                try {
                                    return studiedClass.getDeclaredField(fieldName).getAnnotation(XStreamOmitField.class) == null && super.shouldSerializeMember(definedIn, fieldName) && (definedIn != Object.class || realClass(fieldName) != null);
                                } catch (CannotResolveClassException e) {
                                    LOG.warn("[Graceful XSTREAM] - Unknown element handled!!", e);
                                    return false;
                                }
                            } else {
                                studiedClass = studiedClass.getSuperclass();
                            }
                        }
                        return false;
                    } catch (NoSuchFieldException e) {
                        LOG.error("[Graceful XSTREAM] - NoSuchFieldException Error Encountered ", e);
                        return true;
                    }
                }
            }

                    ;
        }
    }

    public static <T, V> List<V> collect(List<T> col, String property) {

        if (col == null || col.isEmpty() || col.get(0) == null) {
            return Collections.emptyList();
        }

        final PropertyDescriptor pd = BeanUtils.getPropertyDescriptor(col.get(0).getClass(), property);
        if (pd == null) {
            return Collections.emptyList();
        }

        final List<V> result = new ArrayList<V>(col.size());
        final Method rm = pd.getReadMethod();

        for (T item : col) {
            try {
                //noinspection unchecked
                result.add((V) rm.invoke(item));
            } catch (Exception e) {
                LOG.error("Error while collecting the property from list. Ignoring this error and proceeding", e);
                // eat it
            }
        }
        return Collections.unmodifiableList(result);
    }

    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class returnedClass() {
        return List.class;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor si, Object owner) throws HibernateException, SQLException {
        String json = rs.getString(names[0]);
        if (json == null || !StringUtils.hasText(json)) {
            return null;
        }

        return XSTREAM.fromXML(json);
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor si) throws HibernateException, SQLException {
        st.setString(index, XSTREAM.toXML(value));
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }
}

