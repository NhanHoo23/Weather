package fpoly.nhanhhph47395.weather.models;

public class Current {
    public double temp_c;
    public double temp_f;
    public String last_updated;
    public Condition condition;
    public double precip_mm;
    public int uv;
    public int humidity;
    public int vis_km;
    public double feelslike_c;
    public double feelslike_f;
    public double dewpoint_c;
    public double dewpoint_f;

    public class Condition {
        public String text;
        public String icon;
    }
}

