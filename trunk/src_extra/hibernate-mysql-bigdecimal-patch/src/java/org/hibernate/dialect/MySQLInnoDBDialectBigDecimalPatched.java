package org.hibernate.dialect;

import java.sql.Types;

public class MySQLInnoDBDialectBigDecimalPatched extends MySQLInnoDBDialect {

    public MySQLInnoDBDialectBigDecimalPatched() {
        super();
        registerColumnType( Types.NUMERIC, "decimal($p,$s)" );
        // MySQL converts numeric to decimal
	}
	
}
