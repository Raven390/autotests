package businessObjects.db.mtTbUser;

public class MtTbUserObjectFactory {
    public static MtTbUserObject generateMtTbUserData(String id, String ucid, Integer account, Integer serverId) {
        return new MtTbUserObject(
                id, ucid,
                account, "server1", "MT4", "Standart", serverId,
                "2023-02-10 07:00:04.408", "Active", 0.00, "USD", 0.00,
                0.00, 0.00, 100, "S_VFX_EUR", 0.00, 0.00,
                "2023-02-10 07:00:04.408", "2023-02-10 07:00:04.408");
    }
}
