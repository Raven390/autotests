package helpers.data.enums;

import java.security.SecureRandom;
import java.util.Arrays;

public enum NbdComment {
    PROMO_NDB_CREDIT_IN("Promo-NDB-Credit In"),
    CREDIT_IN_NO_DEPOSIT_BONUS("Credit In - No Deposit Bonus"),
    CREDIT_IN_APAC_NO_DEP_BONUS("Credit in - APAC No Dep. Bonus"),
    CREDIT_IN_JAP_NO_DEP_BONUS("Credit in - JAP No Dep. Bonus"),
    CREDIT_IN_PMT103_NDB_JP("credit in-PMT103 -NDB-JP"),
    CREDIT_IN_VN_30_NDB_IBS("Credit In - VN $30 NDB IBs"),
    CREDIT_IN_JP_NDB("credit in-JP NDB"),
    CREDIT_OUT_APAC_NO_DEP_BONUS("Credit out - APAC No Dep. Bonus"),
    CREDIT_IN_JP_NDB_2406("credit in-JP NDB2406");

    private final String displayName;

    NbdComment(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    private static final SecureRandom random = new SecureRandom();

    public static NbdComment getRandomNbdComment() {
        NbdComment[] comment = values();

        return comment[random.nextInt(comment.length)];
    }

    public static NbdComment getRandomNbdComment(NbdComment... fraudType) {
        NbdComment comment;
        do {
            NbdComment[] comments = values();

            comment = comments[random.nextInt(comments.length)];
        } while (Arrays.stream(fraudType).toList().contains(comment));
        return comment;
    }
}
