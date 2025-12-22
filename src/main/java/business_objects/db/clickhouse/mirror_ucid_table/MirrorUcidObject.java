package business_objects.db.clickhouse.mirror_ucid_table;

import java.util.Objects;

public class MirrorUcidObject {
    public String symbol;
    public String ucid_1;
    public Double lots_1;
    public Integer cmd_1;
    public String ucid_2;
    public Double lots_2;
    public Integer cmd_2;
    public Float similarity_exp;
    public Float similarity_exp_time;
    public Float median_volume_1;
    public Float sum_volume_1;
    public Float median_volume_2;
    public Float sum_volume_2;
    public String date;

    public MirrorUcidObject() {}

    public MirrorUcidObject(
            String symbol,
            String ucid_1,
            Double lots_1,
            Integer cmd_1,
            String ucid_2,
            Double lots_2,
            Integer cmd_2,
            Float similarity_exp,
            Float similarity_exp_time,
            Float median_volume_1,
            Float sum_volume_1,
            Float median_volume_2,
            Float sum_volume_2,
            String date) {
        this.symbol = symbol;
        this.ucid_1 = ucid_1;
        this.lots_1 = lots_1;
        this.cmd_1 = cmd_1;
        this.ucid_2 = ucid_2;
        this.lots_2 = lots_2;
        this.cmd_2 = cmd_2;
        this.similarity_exp = similarity_exp;
        this.similarity_exp_time = similarity_exp_time;
        this.median_volume_1 = median_volume_1;
        this.sum_volume_1 = sum_volume_1;
        this.median_volume_2 = median_volume_2;
        this.sum_volume_2 = sum_volume_2;
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MirrorUcidObject that = (MirrorUcidObject) o;
        return Objects.equals(symbol, that.symbol)
                && Objects.equals(ucid_1, that.ucid_1)
                && Objects.equals(lots_1, that.lots_1)
                && Objects.equals(cmd_1, that.cmd_1)
                && Objects.equals(ucid_2, that.ucid_2)
                && Objects.equals(lots_2, that.lots_2)
                && Objects.equals(cmd_2, that.cmd_2)
                && Objects.equals(similarity_exp, that.similarity_exp)
                && Objects.equals(similarity_exp_time, that.similarity_exp_time)
                && Objects.equals(median_volume_1, that.median_volume_1)
                && Objects.equals(sum_volume_1, that.sum_volume_1)
                && Objects.equals(median_volume_2, that.median_volume_2)
                && Objects.equals(sum_volume_2, that.sum_volume_2)
                && Objects.equals(date, that.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
                symbol,
                ucid_1,
                lots_1,
                cmd_1,
                ucid_2,
                lots_2,
                cmd_2,
                similarity_exp,
                similarity_exp_time,
                median_volume_1,
                sum_volume_1,
                median_volume_2,
                sum_volume_2,
                date);
    }

    @Override
    public String toString() {
        return "MirrorUcidObject{" + "symbol='" + symbol + '\'' + ", ucid_1='" + ucid_1 + '\'' + ", lots_1=" + lots_1
                + ", cmd_1=" + cmd_1 + ", ucid_2='" + ucid_2 + '\'' + ", lots_2=" + lots_2 + ", cmd_2=" + cmd_2
                + ", similarity_exp=" + similarity_exp + ", similarity_exp_time=" + similarity_exp_time
                + ", median_volume_1=" + median_volume_1 + ", sum_volume_1=" + sum_volume_1 + ", median_volume_2="
                + median_volume_2 + ", sum_volume_2=" + sum_volume_2 + ", date='" + date + '\'' + '}';
    }
}
