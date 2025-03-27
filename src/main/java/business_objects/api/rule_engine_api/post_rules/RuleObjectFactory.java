package business_objects.api.rule_engine_api.post_rules;

import java.util.List;

import static utils.Utils.getRandomIntPositive;

public class RuleObjectFactory {

    public static RuleObject.Value generateRuleObjectValue() {
        return new RuleObject.Value(getRandomIntPositive().toString(), List.of(getRandomIntPositive().toString(), getRandomIntPositive().toString()));
    }

    public static RuleObject generateRule() {
        return new RuleObject(getRandomIntPositive().toString(), getRandomIntPositive().toString(), generateRuleObjectValue()
        );
    }
}
