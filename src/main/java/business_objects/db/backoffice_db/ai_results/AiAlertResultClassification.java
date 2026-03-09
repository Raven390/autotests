package business_objects.db.backoffice_db.ai_results;

import java.sql.Timestamp;
import lombok.Data;

@Data
public class AiAlertResultClassification {
    private Long id;
    private Long aiAlertResultId;
    private String category;
    private String fraudLabel;
    private Integer confidenceScore;
    private String reasoningCn;
    private String manualReviewCn;
    private String reasoningEn;
    private String manualReviewEn;
    private Timestamp createdAt;
}
