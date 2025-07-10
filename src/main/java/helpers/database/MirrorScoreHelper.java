package helpers.database;

import io.qameta.allure.Allure;

import java.util.logging.Logger;

import static helpers.database.DbHelper.deleteEntryFromDb;
import static utils.Constants.*;

public class MirrorScoreHelper {

    protected static Logger logger = Logger.getLogger("MirrorScoreHelper");

    public static void cleanUserMirrorScoreDataDb(String ucid) throws Exception {
        Allure.step("delete user's mirror score data from DB");

        deleteEntryFromDb(DATA_SCIENCE_FEATURE_STORE_SERVICE_TABLE_NAME, "ucid = '" + ucid + "'");
        logger.info("source mirror score table is cleared");
        deleteEntryFromDb(DATA_SCIENCE_UCID_MIRROR_SCORE_TABLE_NAME, "ucid = '" + ucid + "'");
        logger.info("result mirror score table is cleared");
        Thread.sleep(100);
    }
}
