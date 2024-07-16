package fpoly.nhanhhph47395.weather.models;

public class Current {
    public double temp_c;
    public double temp_f;
    public String last_updated;
    public Condition condition;

    public class Condition {
        public String text;
        public String icon;
    }
}
