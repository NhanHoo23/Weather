package fpoly.nhanhhph47395.weather.models;

import android.util.Log;

public enum WeatherCondition {
    SUNNY(1000, "Sunny", "Clear", 113),
    PARTLY_CLOUDY(1003, "Partly cloudy", "Partly cloudy", 116),
    CLOUDY(1006, "Cloudy", "Cloudy", 119),
    OVERCAST(1009, "Overcast", "Overcast", 122),
    MIST(1030, "Mist", "Mist", 143),
    PATCHY_RAIN_POSSIBLE(1063, "Patchy rain possible", "Patchy rain possible", 176),
    PATCHY_SNOW_POSSIBLE(1066, "Patchy snow possible", "Patchy snow possible", 179),
    PATCHY_SLEET_POSSIBLE(1069, "Patchy sleet possible", "Patchy sleet possible", 182),
    PATCHY_FREEZING_DRIZZLE_POSSIBLE(1072, "Patchy freezing drizzle possible", "Patchy freezing drizzle possible", 185),
    THUNDERY_OUTBREAKS_POSSIBLE(1087, "Thundery outbreaks possible", "Thundery outbreaks possible", 200),
    BLOWING_SNOW(1114, "Blowing snow", "Blowing snow", 227),
    BLIZZARD(1117, "Blizzard", "Blizzard", 230),
    FOG(1135, "Fog", "Fog", 248),
    FREEZING_FOG(1147, "Freezing fog", "Freezing fog", 260),
    PATCHY_LIGHT_DRIZZLE(1150, "Patchy light drizzle", "Patchy light drizzle", 263),
    LIGHT_DRIZZLE(1153, "Light drizzle", "Light drizzle", 266),
    FREEZING_DRIZZLE(1168, "Freezing drizzle", "Freezing drizzle", 281),
    HEAVY_FREEZING_DRIZZLE(1171, "Heavy freezing drizzle", "Heavy freezing drizzle", 284),
    PATCHY_LIGHT_RAIN(1180, "Patchy light rain", "Patchy light rain", 293),
    LIGHT_RAIN(1183, "Light rain", "Light rain", 296),
    MODERATE_RAIN_AT_TIMES(1186, "Moderate rain at times", "Moderate rain at times", 299),
    MODERATE_RAIN(1189, "Moderate rain", "Moderate rain", 302),
    HEAVY_RAIN_AT_TIMES(1192, "Heavy rain at times", "Heavy rain at times", 305),
    HEAVY_RAIN(1195, "Heavy rain", "Heavy rain", 308),
    LIGHT_FREEZING_RAIN(1198, "Light freezing rain", "Light freezing rain", 311),
    MODERATE_OR_HEAVY_FREEZING_RAIN(1201, "Moderate or heavy freezing rain", "Moderate or heavy freezing rain", 314),
    LIGHT_SLEET(1204, "Light sleet", "Light sleet", 317),
    MODERATE_OR_HEAVY_SLEET(1207, "Moderate or heavy sleet", "Moderate or heavy sleet", 320),
    PATCHY_LIGHT_SNOW(1210, "Patchy light snow", "Patchy light snow", 323),
    LIGHT_SNOW(1213, "Light snow", "Light snow", 326),
    PATCHY_MODERATE_SNOW(1216, "Patchy moderate snow", "Patchy moderate snow", 329),
    MODERATE_SNOW(1219, "Moderate snow", "Moderate snow", 332),
    PATCHY_HEAVY_SNOW(1222, "Patchy heavy snow", "Patchy heavy snow", 335),
    HEAVY_SNOW(1225, "Heavy snow", "Heavy snow", 338),
    ICE_PELLETS(1237, "Ice pellets", "Ice pellets", 350),
    LIGHT_RAIN_SHOWER(1240, "Light rain shower", "Light rain shower", 353),
    MODERATE_OR_HEAVY_RAIN_SHOWER(1243, "Moderate or heavy rain shower", "Moderate or heavy rain shower", 356),
    TORRENTIAL_RAIN_SHOWER(1246, "Torrential rain shower", "Torrential rain shower", 359),
    LIGHT_SLEET_SHOWERS(1249, "Light sleet showers", "Light sleet showers", 362),
    MODERATE_OR_HEAVY_SLEET_SHOWERS(1252, "Moderate or heavy sleet showers", "Moderate or heavy sleet showers", 365),
    LIGHT_SNOW_SHOWERS(1255, "Light snow showers", "Light snow showers", 368),
    MODERATE_OR_HEAVY_SNOW_SHOWERS(1258, "Moderate or heavy snow showers", "Moderate or heavy snow showers", 371),
    LIGHT_SHOWERS_OF_ICE_PELLETS(1261, "Light showers of ice pellets", "Light showers of ice pellets", 374),
    MODERATE_OR_HEAVY_SHOWERS_OF_ICE_PELLETS(1264, "Moderate or heavy showers of ice pellets", "Moderate or heavy showers of ice pellets", 377),
    PATCHY_LIGHT_RAIN_WITH_THUNDER(1273, "Patchy light rain with thunder", "Patchy light rain with thunder", 386),
    MODERATE_OR_HEAVY_RAIN_WITH_THUNDER(1276, "Moderate or heavy rain with thunder", "Moderate or heavy rain with thunder", 389),
    PATCHY_LIGHT_SNOW_WITH_THUNDER(1279, "Patchy light snow with thunder", "Patchy light snow with thunder", 392),
    MODERATE_OR_HEAVY_SNOW_WITH_THUNDER(1282, "Moderate or heavy snow with thunder", "Moderate or heavy snow with thunder", 395);

    private final int code;
    private final String day;
    private final String night;
    private final int icon;

    WeatherCondition(int code, String day, String night, int icon) {
        this.code = code;
        this.day = day;
        this.night = night;
        this.icon = icon;
    }

    public int getCode() {
        return code;
    }

    public String getDay() {
        return day;
    }

    public String getNight() {
        return night;
    }

    public int getIcon() {
        return icon;
    }

    public static WeatherCondition fromCode(int code) {
        for (WeatherCondition condition : values()) {
            if (condition.code == code) {
                return condition;
            }
        }

        throw new IllegalArgumentException("Invalid weather condition code: " + code);
    }
}

