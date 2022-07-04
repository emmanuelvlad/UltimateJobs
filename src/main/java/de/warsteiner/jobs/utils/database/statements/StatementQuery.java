package de.warsteiner.jobs.utils.database.statements;

import java.util.function.Consumer;

@SuppressWarnings("hiding")
public interface StatementQuery<SQLStatementAPI> extends Consumer<SQLStatementAPI> {

    @Override
    void accept(SQLStatementAPI query);
}
