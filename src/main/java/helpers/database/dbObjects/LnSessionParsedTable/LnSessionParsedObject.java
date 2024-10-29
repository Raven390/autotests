package helpers.database.dbObjects.LnSessionParsedTable;

public class LnSessionParsedObject {
    public String uid;
    public int id;
    public String brand;
    public String session_id;
    public int user_id;
    public String email;
    public String mobile_code;
    public String mobile;
    public String event_type;
    public boolean is_from_app;
    public String create_time;
    public int policy_score;
    public String risk_rating;
    public String device_id;
    public String digital_id;
    public String event_datetime;
    public int event_id;
    public String proxy_ip;
    public String proxy_ip_activities;
    public String[] proxy_ip_attributes;
    public String proxy_ip_city;
    public String proxy_ip_connection_type;
    public String proxy_ip_first_seen;
    public String proxy_ip_geo;
    public String proxy_ip_home;
    public String proxy_ip_isp;
    public double proxy_ip_latitude;
    public double proxy_ip_longitude;
    public String proxy_ip_organization;
    public String proxy_ip_organization_type;
    public String proxy_ip_postal_code;
    public String proxy_ip_region;
    public String proxy_ip_result;
    public String proxy_ip_routing_type;
    public int proxy_ip_score;
    public int proxy_ip_worst_score;
    public String proxy_ipv6;
    public String proxy_name;
    public double proxy_score;
    public String proxy_type;
    public String true_ip;
    public String true_ip_activities;
    public String[] true_ip_attributes;
    public String true_ip_city;
    public int true_ip_country_confidence;
    public String true_ip_first_seen;
    public String true_ip_geo;
    public String true_ip_isp;
    public String true_ip_last_event;
    public String true_ip_organization;
    public String true_ip_organization_type;
    public String true_ip_postal_code;
    public String true_ip_region;
    public String true_ip_result;
    public String true_ip_routing_type;
    public int true_ip_score;
    public int true_ip_worst_score;
    public String true_ipv6;
    public int vpn_score;

    // Constructor to initialize all fields
    public LnSessionParsedObject(
            String uid,
            int id,
            String brand,
            String session_id,
            int user_id,
            String email,
            String mobile_code,
            String mobile,
            String event_type,
            boolean is_from_app,
            String create_time,
            int policy_score,
            String risk_rating,
            String device_id,
            String digital_id,
            String event_datetime,
            int event_id,
            String proxy_ip,
            String proxy_ip_activities,
            String[] proxy_ip_attributes,
            String proxy_ip_city,
            String proxy_ip_connection_type,
            String proxy_ip_first_seen,
            String proxy_ip_geo,
            String proxy_ip_home,
            String proxy_ip_isp,
            double proxy_ip_latitude,
            double proxy_ip_longitude,
            String proxy_ip_organization,
            String proxy_ip_organization_type,
            String proxy_ip_postal_code,
            String proxy_ip_region,
            String proxy_ip_result,
            String proxy_ip_routing_type,
            int proxy_ip_score,
            int proxy_ip_worst_score,
            String proxy_ipv6,
            String proxy_name,
            double proxy_score,
            String proxy_type,
            String true_ip,
            String true_ip_activities,
            String[] true_ip_attributes,
            String true_ip_city,
            int true_ip_country_confidence,
            String true_ip_first_seen,
            String true_ip_geo,
            String true_ip_isp,
            String true_ip_last_event,
            String true_ip_organization,
            String true_ip_organization_type,
            String true_ip_postal_code,
            String true_ip_region,
            String true_ip_result,
            String true_ip_routing_type,
            int true_ip_score,
            int true_ip_worst_score,
            String true_ipv6,
            int vpn_score
    ) {
        this.uid = uid;
        this.id = id;
        this.brand = brand;
        this.session_id = session_id;
        this.user_id = user_id;
        this.email = email;
        this.mobile_code = mobile_code;
        this.mobile = mobile;
        this.event_type = event_type;
        this.is_from_app = is_from_app;
        this.create_time = create_time;
        this.policy_score = policy_score;
        this.risk_rating = risk_rating;
        this.device_id = device_id;
        this.digital_id = digital_id;
        this.event_datetime = event_datetime;
        this.event_id = event_id;
        this.proxy_ip = proxy_ip;
        this.proxy_ip_activities = proxy_ip_activities;
        this.proxy_ip_attributes = proxy_ip_attributes;
        this.proxy_ip_city = proxy_ip_city;
        this.proxy_ip_connection_type = proxy_ip_connection_type;
        this.proxy_ip_first_seen = proxy_ip_first_seen;
        this.proxy_ip_geo = proxy_ip_geo;
        this.proxy_ip_home = proxy_ip_home;
        this.proxy_ip_isp = proxy_ip_isp;
        this.proxy_ip_latitude = proxy_ip_latitude;
        this.proxy_ip_longitude = proxy_ip_longitude;
        this.proxy_ip_organization = proxy_ip_organization;
        this.proxy_ip_organization_type = proxy_ip_organization_type;
        this.proxy_ip_postal_code = proxy_ip_postal_code;
        this.proxy_ip_region = proxy_ip_region;
        this.proxy_ip_result = proxy_ip_result;
        this.proxy_ip_routing_type = proxy_ip_routing_type;
        this.proxy_ip_score = proxy_ip_score;
        this.proxy_ip_worst_score = proxy_ip_worst_score;
        this.proxy_ipv6 = proxy_ipv6;
        this.proxy_name = proxy_name;
        this.proxy_score = proxy_score;
        this.proxy_type = proxy_type;
        this.true_ip = true_ip;
        this.true_ip_activities = true_ip_activities;
        this.true_ip_attributes = true_ip_attributes;
        this.true_ip_city = true_ip_city;
        this.true_ip_country_confidence = true_ip_country_confidence;
        this.true_ip_first_seen = true_ip_first_seen;
        this.true_ip_geo = true_ip_geo;
        this.true_ip_isp = true_ip_isp;
        this.true_ip_last_event = true_ip_last_event;
        this.true_ip_organization = true_ip_organization;
        this.true_ip_organization_type = true_ip_organization_type;
        this.true_ip_postal_code = true_ip_postal_code;
        this.true_ip_region = true_ip_region;
        this.true_ip_result = true_ip_result;
        this.true_ip_routing_type = true_ip_routing_type;
        this.true_ip_score = true_ip_score;
        this.true_ip_worst_score = true_ip_worst_score;
        this.true_ipv6 = true_ipv6;
        this.vpn_score = vpn_score;
    }
}