package business_objects.api.rule_engine_api.post_rules;

import helpers.data.enums.Brand;

import java.util.List;

import static utils.Utils.getRandomIntPositive;

public class RuleObjectFactory {

    public static RuleObject.Value generateRuleObjectValue() {
        return new RuleObject.Value(getRandomIntPositive().toString(), List.of(Brand.VANTAGE.getUcidBrand(), Brand.PU_PRIME.getUcidBrand()));
    }

    public static RuleObject generateRule() {
        return new RuleObject(getRandomIntPositive().toString(), "registration", generateRuleObjectValue()
        );
    }
}
