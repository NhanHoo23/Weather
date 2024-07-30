package fpoly.nhanhhph47395.weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.models.settingModels.DefaultLocationModel;
import fpoly.nhanhhph47395.weather.models.settingModels.DistanceSettingModel;
import fpoly.nhanhhph47395.weather.models.settingModels.LanguageModel;
import fpoly.nhanhhph47395.weather.models.settingModels.PrecipitationSettingModel;
import fpoly.nhanhhph47395.weather.models.settingModels.SettingModel;
import fpoly.nhanhhph47395.weather.models.settingModels.TempSettingModel;
import fpoly.nhanhhph47395.weather.models.settingModels.WindSpeedSettingModel;
import fpoly.nhanhhph47395.weather.utils.AppManager;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.SettingViewHolder> {
    private Context mContext;
    private List<SettingModel> list;
    private OnClickListener onClickListener;
    private OnSwitchListener onSwitchListener;
    private boolean isDarkMode = false;

    public SettingAdapter(Context mContext, List<SettingModel> list, OnClickListener onClickListener, OnSwitchListener onSwitchListener) {
        this.mContext = mContext;
        this.list = list;
        this.onClickListener = onClickListener;
        this.onSwitchListener = onSwitchListener;
    }

    public SettingAdapter(Context mContext, List<SettingModel> list, OnClickListener onClickListener) {
        this.mContext = mContext;
        this.list = list;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_rc_setting, parent, false);
        return new SettingViewHolder(view, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        SettingModel item = list.get(position);

        holder.tvSettingName.setText(item.settingName);
        holder.imgIcon.setImageResource(item.trailingIcon);
        holder.btnSwitch.setVisibility(item.showTrailingIcon ? View.INVISIBLE : View.VISIBLE);
        holder.imgIcon.setVisibility(item.showTrailingIcon ? View.VISIBLE : View.GONE);

        if (item.getClass() == SettingModel.class) {
            holder.btnSwitch.setVisibility(item.showTrailingIcon ? View.INVISIBLE : View.VISIBLE);
            holder.imgIcon.setVisibility(item.showTrailingIcon ? View.VISIBLE : View.GONE);
        } else {
            holder.btnSwitch.setVisibility(View.INVISIBLE);

            if (item.getClass() == TempSettingModel.class) {
                updateIcon(holder.imgIcon, AppManager.shared(mContext).getSelectedTempIndex() == position);
            } else if (item.getClass() == WindSpeedSettingModel.class) {
                updateIcon(holder.imgIcon, AppManager.shared(mContext).getSelectedWindSpeedIndex() == position);
            } else if (item.getClass() == DistanceSettingModel.class) {
                updateIcon(holder.imgIcon, AppManager.shared(mContext).getSelectedDistanceIndex() == position);
            } else if (item.getClass() == PrecipitationSettingModel.class) {
                updateIcon(holder.imgIcon, AppManager.shared(mContext).getSelectedPrecipitationIndex() == position);
            } else if (item.getClass() == LanguageModel.class) {
                updateIcon(holder.imgIcon, AppManager.shared(mContext).getSelectedLanguageIndex() == position);
            } else if (item.getClass() == DefaultLocationModel.class) {
                updateIcon(holder.imgIcon, AppManager.shared(mContext).getSelectedDefaultLocationIndex() == position);
            }
        }

        isDarkMode = AppManager.shared(mContext).getDarkModeStatus();
        holder.btnSwitch.setChecked(isDarkMode);

        holder.btnSwitch.setOnClickListener(v -> {
            isDarkMode = !isDarkMode;
            onSwitchListener.onClick(isDarkMode);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class SettingViewHolder extends RecyclerView.ViewHolder {
        private TextView tvSettingName;
        private ImageView imgIcon;
        private Switch btnSwitch;
        public SettingViewHolder(@NonNull View itemView, final OnClickListener listener) {
            super(itemView);

            tvSettingName = itemView.findViewById(R.id.tvSettingName);
            imgIcon = itemView.findViewById(R.id.imgIcon);
            btnSwitch = itemView.findViewById(R.id.btnSwitch);

            itemView.setOnClickListener(v -> {
                listener.onItemClick(getBindingAdapterPosition());
            });
        }
    }

    public void updateIcon(View view, boolean flag) {
        if (flag) {
            view.setVisibility(View.VISIBLE);
        } else {
            view.setVisibility(View.GONE);
        }
    }

    public interface OnClickListener {
        void onItemClick(int position);
    }

    public interface OnSwitchListener {
        void onClick(boolean isDarkMode);
    }

}
