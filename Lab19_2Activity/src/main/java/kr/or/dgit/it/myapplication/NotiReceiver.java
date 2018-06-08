package kr.or.dgit.it.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotiReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast toast = Toast.makeText(context, "I am NotiReceiver...", Toast.LENGTH_LONG);
        toast.show();
    }
}
