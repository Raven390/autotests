package business_objects.db.data_science.ucid_general_score;

import java.time.OffsetDateTime;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UcidGeneralScore {
    String ucid;
    String id;
    Integer action;
    OffsetDateTime timeUtc;
    OffsetDateTime insertTimeUtc;
    Double modelScore;
    Integer countAction;
    Double cumSumScore;
    Double ucidScore;
}
