package com.jnu.bookstore;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextOptions;
import com.baidu.mapapi.model.LatLng;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MapFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MapFragment extends Fragment {


    private MapView mapView;

    public MapFragment() {
        // Required empty public constructor
    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = rootView.findViewById(R.id.baidu_map_view);

        LatLng centerPoint = new LatLng(22.254895,113.539458);

        MapStatus.Builder builder = new MapStatus.Builder();
        builder.zoom(18.0f).target(centerPoint);
        mapView.getMap().setMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));

        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.jinan);
        MarkerOptions markerOption = new MarkerOptions().icon(bitmap).position(centerPoint);
        Marker marker = (Marker) mapView.getMap().addOverlay(markerOption);
        OverlayOptions textOption = new TextOptions().bgColor(0xAAFFFF00).fontSize(50)
                .fontColor(0xFFFF00FF).text("我的学校").rotate(0).position(centerPoint);
        mapView.getMap().addOverlay(textOption);
        mapView.getMap().setOnMarkerClickListener(maker -> {
            Toast.makeText(getContext(), "Marker被点击了！", Toast.LENGTH_SHORT).show();
            return false;
        });
        return rootView;
    }
    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }
    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}