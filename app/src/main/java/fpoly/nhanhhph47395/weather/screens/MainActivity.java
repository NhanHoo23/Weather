package fpoly.nhanhhph47395.weather.screens;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;
import fpoly.nhanhhph47395.weather.R;
import fpoly.nhanhhph47395.weather.databinding.ActivityMainBinding;
import fpoly.nhanhhph47395.weather.fragments.HomeFragment;
import fpoly.nhanhhph47395.weather.fragments.SearchFragment;
import fpoly.nhanhhph47395.weather.fragments.SettingFragment;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    private BlurView blurView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        replaceFragment(new HomeFragment());

        binding.nvView.setOnItemSelectedListener(menuItem -> {
            if (menuItem.getItemId() == R.id.nav_home) {
                replaceFragment(new HomeFragment());
            } else if (menuItem.getItemId() == R.id.nav_search) {
                replaceFragment(new SearchFragment());
            } else {
                replaceFragment(new SettingFragment());
            }
            return true;
        });

//        float radius = 20f;
//
//        // Lấy decorView và rootView
//        View decorView = getWindow().getDecorView();
//        ViewGroup rootView = (ViewGroup) decorView.findViewById(android.R.id.content);
//
//        // Lấy drawable mặc định của cửa sổ
//        Drawable windowBackground = decorView.getBackground();
//
//        // Cấu hình BlurView
//        binding.blurView.setupWith(rootView, new RenderScriptBlur(this)) // or RenderEffectBlur
//                .setFrameClearDrawable(windowBackground) // Optional
//                .setBlurRadius(radius);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.flContent, fragment);
        transaction.commit();
    }

}