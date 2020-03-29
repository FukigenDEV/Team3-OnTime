package fkg.dev.geofencingdemo.Interface;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import fkg.dev.geofencingdemo.MyLatLng;

public interface IOnLoadLocationListener {
    void onLoadLocationSuccess(List<MyLatLng> latLngs);
    void onLoadLocationFailed(String message);
}
