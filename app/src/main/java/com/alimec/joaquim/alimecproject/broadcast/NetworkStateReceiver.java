package com.alimec.joaquim.alimecproject.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by joaquim on 23/04/15.
 */
public class NetworkStateReceiver extends BroadcastReceiver{


    @Override
    public void onReceive(Context context, Intent intent) {
//        ConnectivityManager commManager =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo wifiInfo = commManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//
//
//        if(wifiInfo.isAvailable() && ServerServices.isServerVisivel()){
//            try {
//                VendaController.getInstance().enviarVendasPendentes();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (JSONException e) {
//                e.printStackTrace();
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
//
//
//

    }
}
