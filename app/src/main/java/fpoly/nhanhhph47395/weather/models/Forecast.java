package fpoly.nhanhhph47395.weather.models;

import java.util.List;

public class Forecast {
    public List<ForecastDay> forecastday;

    public class ForecastDay {
        public String date;
        public Day day;
        public List<Hour> hour;

        public class Day {
            public double maxtemp_c;
            public double mintemp_c;
            public double maxtemp_f;
            public double mintemp_f;
            public int avghumidity;
            public Current.Condition condition;
        }

        public class Hour {
            public String time;
            public double temp_c;
            public double temp_f;
            public int humidity;
            public Current.Condition condition;
        }
    }
}
