package business_objects.db.rule_engine_db.rule;

import business_objects.api.rule_engine_api.post_rules.RuleObject;
import org.postgresql.jdbc.PgArray;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import static helpers.database.DbHelper.createPostgresConnectionRuleEngine;


public class RuleDbObjectFactory {
    public static RuleDbObjectPgArray generateRuleDbObjectByRulePgArray(RuleObject rule) throws SQLException {
        Connection connection = createPostgresConnectionRuleEngine();
        // Convert Java List to SQL Array
        List<String> brands = List.of(rule.getValue().getBrands().getFirst(), rule.getValue().getBrands().getLast());
        Array sqlArray = connection.createArrayOf("text", brands.toArray());
        RuleDbObjectPgArray ruleFinal = new RuleDbObjectPgArray();
        ruleFinal.setId(rule.getId());
        ruleFinal.setEventType(rule.getEventType());
        ruleFinal.setRuleName(rule.getValue().getName());
        ruleFinal.setBrands((PgArray) sqlArray);

        return ruleFinal;
    }
}