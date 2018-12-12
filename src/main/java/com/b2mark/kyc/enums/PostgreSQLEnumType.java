/**
 * <h1>JPA and Postgresql</h1>
 * <p>This class use for maping postgresql enum in jpa.
 *
 * @author b2mark
 * @version 1.0
 * @since 2018
 *
 */

package com.b2mark.kyc.enums;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;

public class PostgreSQLEnumType extends org.hibernate.type.EnumType {

    public void nullSafeSet
            (PreparedStatement st,
             Object value,
             int index,
             SharedSessionContractImplementor session) throws HibernateException, SQLException {

        if (value == null) {
            st.setNull(index, Types.OTHER);
        } else {
            st.setObject(index, value.toString(), Types.OTHER);
        }
    }
}
