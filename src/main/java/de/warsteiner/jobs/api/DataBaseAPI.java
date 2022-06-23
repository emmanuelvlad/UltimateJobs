package de.warsteiner.jobs.api;

import de.warsteiner.jobs.utils.database.statements.SQLStatementAPI;

public class DataBaseAPI {
 
    private static SQLStatementAPI sqlStatementAPI = null;
 
    public static SQLStatementAPI getSQLStatementAPI() {
        if (sqlStatementAPI == null) sqlStatementAPI = new SQLStatementAPI();
        return sqlStatementAPI;
    }
     

}
