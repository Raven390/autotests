package business_objects.db.rule_engine_db.rule_deployment;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.UUID;
import utils.Utils;

public class RuleDeploymentObjectFactory {
    public static RuleDeploymentObject generateRuleDeploymentObject(UUID uuid) {

        String ruleBody = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";
        byte[] ruleBodyBytes = ruleBody.getBytes(StandardCharsets.UTF_8);

        RuleDeploymentObject ruleDeployment = new RuleDeploymentObject();
        ruleDeployment.setUuid(uuid);
        ruleDeployment.setAuthorName("test_automation");
        ruleDeployment.setVersion("1");
        ruleDeployment.setZeebeRevision(1);
        ruleDeployment.setProcessId(Utils.getRandomIntPositive().toString());
        ruleDeployment.setRuleName("rule_name");
        ruleDeployment.setRuleBody(ruleBodyBytes); // Set BYTEA (binary)
        ruleDeployment.setLastUpdate(LocalDateTime.now());
        ruleDeployment.setStatus(1);
        ruleDeployment.setComment("Comment");

        return ruleDeployment;
    }
}
