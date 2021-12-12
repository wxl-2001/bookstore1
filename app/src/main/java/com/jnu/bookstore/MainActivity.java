package com.jnu.bookstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TableLayout;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewPager2 viewPagerFragments=findViewById(R.id.viewpager2_content);
        viewPagerFragments.setAdapter(new MyFragmentAdpater(this));

        TabLayout tabLayoutHeader=findViewById(R.id.tablayout_header);
        TabLayoutMediator tabLayoutMediator=new TabLayoutMediator(tabLayoutHeader, viewPagerFragments, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch(position){
                    case 0:
                        tab.setText("图书");
                        break;
                    case 1:
                        tab.setText("新闻");
                        break;
                    case 2:
                        tab.setText("卖家");
                        break;
                    case 3:
                        tab.setText("游戏");
                        break;
                }
            }
        });
        tabLayoutMediator.attach();
    }

    private class MyFragmentAdpater extends FragmentStateAdapter {


        public MyFragmentAdpater(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position) {
                case 0:
                    return bookFragment.newInstance();
                case 1:
                    return WebViewFragment.newInstance();
                case 2:
                    return MapFragment.newInstance();
                default:
                    return GameFragment.newInstance();
            }
        }
        @Override
        public int getItemCount() {
            return 4;
        }

    }
}