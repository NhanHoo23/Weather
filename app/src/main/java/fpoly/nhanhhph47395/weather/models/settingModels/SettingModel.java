package fpoly.nhanhhph47395.weather.models.settingModels;

public class SettingModel {
    public String settingName;
    public int trailingIcon;
    public boolean showTrailingIcon;

    public SettingModel(String settingName, int trailingIcon, boolean showTrailingIcon) {
        this.settingName = settingName;
        this.trailingIcon = trailingIcon;
        this.showTrailingIcon = showTrailingIcon;
    }
}
