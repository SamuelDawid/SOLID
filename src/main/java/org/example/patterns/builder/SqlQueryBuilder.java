package org.example.patterns.builder;

import java.util.ArrayList;
import java.util.List;

/**
 * Drugi przykład Builder — generator SQL.
 * Pokazuje, że Builder jest naturalny dla obiektów,
 * które buduje się stopniowo z wielu opcjonalnych fragmentów.
 */
public class SqlQueryBuilder {

    private String table;
    private final List<String> columns = new ArrayList<>();
    private final List<String> wheres  = new ArrayList<>();
    private String orderBy;
    private Integer limit;

    public SqlQueryBuilder select(String... cols) {
        for (String c : cols) columns.add(c);
        return this;
    }

    public SqlQueryBuilder from(String table) {
        this.table = table;
        return this;
    }

    public SqlQueryBuilder where(String condition) {
        wheres.add(condition);
        return this;
    }

    public SqlQueryBuilder orderBy(String column) {
        this.orderBy = column;
        return this;
    }

    public SqlQueryBuilder limit(int n) {
        if (n <= 0) throw new IllegalArgumentException("limit > 0");
        this.limit = n;
        return this;
    }

    public String build() {
        if (table == null) {
            throw new IllegalStateException("Brak FROM — wywołaj from(...)");
        }
        StringBuilder sb = new StringBuilder("SELECT ");
        sb.append(columns.isEmpty() ? "*" : String.join(", ", columns));
        sb.append(" FROM ").append(table);
        if (!wheres.isEmpty()) {
            sb.append(" WHERE ").append(String.join(" AND ", wheres));
        }
        if (orderBy != null) sb.append(" ORDER BY ").append(orderBy);
        if (limit   != null) sb.append(" LIMIT ").append(limit);
        return sb.toString();
    }
}