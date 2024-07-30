package fpoly.nhanhhph47395.weather.models;

import android.content.Context;

import fpoly.nhanhhph47395.weather.R;

public class WeatherEvaluate {
    public enum UVLevel {
        LOW("Thấp", "Thấp đến hết ngày"),
        MODERATE("Trung bình", "Trung bình vào buổi trưa"),
        HIGH("Cao", "Cao, hãy sử dụng kem chống nắng"),
        VERY_HIGH("Rất cao", "Rất cao, hạn chế ra ngoài"),
        EXTREME("Cực cao", "Cực cao, hãy ở trong nhà");

        private String displayName;
        private String detailedDescription;

        UVLevel(String displayName, String detailedDescription) {
            this.displayName = displayName;
            this.detailedDescription = detailedDescription;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getDetailedDescription() {
            return detailedDescription;
        }

        public static UVLevel getUVLevel(double uvIndex) {
            if (uvIndex >= 0 && uvIndex <= 2.9) {
                return LOW;
            } else if (uvIndex >= 3 && uvIndex <= 5.9) {
                return MODERATE;
            } else if (uvIndex >= 6 && uvIndex <= 7.9) {
                return HIGH;
            } else if (uvIndex >= 8 && uvIndex <= 10.9) {
                return VERY_HIGH;
            } else {
                return EXTREME;
            }
        }

        public static void updateDisplayNames(Context context) {
            LOW.displayName = context.getString(R.string.UVLowLevel);
            LOW.detailedDescription = context.getString(R.string.UVLowLevelDes);
            MODERATE.displayName = context.getString(R.string.UVModerateLevel);
            MODERATE.detailedDescription = context.getString(R.string.UVModerateLevelDes);
            HIGH.displayName = context.getString(R.string.UVHighLevel);
            HIGH.detailedDescription = context.getString(R.string.UVHighLevelDes);
            VERY_HIGH.displayName = context.getString(R.string.UVVeryHighLevel);
            VERY_HIGH.detailedDescription = context.getString(R.string.UVVeryHighLevelDes);
            EXTREME.displayName = context.getString(R.string.UVExtremeLevel);
            EXTREME.detailedDescription = context.getString(R.string.UVExtremeLevelDes);
        }
    }

    public enum VisibilityLevel {
        EXCELLENT("Hoàn toàn nhìn rõ ngay lúc này"),
        GOOD("Tầm nhìn tốt"),
        MODERATE("Tầm nhìn vừa phải"),
        POOR("Tầm nhìn kém"),
        VERY_POOR("Tầm nhìn rất kém");

        private String displayName;

        VisibilityLevel(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static VisibilityLevel getVisibilityLevel(int visKm) {
            if (visKm >= 10) {
                return EXCELLENT;
            } else if (visKm >= 6 && visKm < 10) {
                return GOOD;
            } else if (visKm >= 3 && visKm < 6) {
                return MODERATE;
            } else if (visKm >= 1 && visKm < 3) {
                return POOR;
            } else {
                return VERY_POOR;
            }
        }

        public static void updateDisplayNames(Context context) {
            EXCELLENT.displayName = context.getString(R.string.visionExcellentLevelDes);
            GOOD.displayName = context.getString(R.string.visionGoodLevelDes);
            MODERATE.displayName = context.getString(R.string.visionModerateLevelDes);
            POOR.displayName = context.getString(R.string.visionPoorLevelDes);
            VERY_POOR.displayName = context.getString(R.string.visionVeryPoorLevelDes);
        }
    }
}






