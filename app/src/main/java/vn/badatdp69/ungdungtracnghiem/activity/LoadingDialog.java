package vn.badatdp69.ungdungtracnghiem.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import vn.badatdp69.ungdungtracnghiem.R;

class LoadingDialog {

    private Activity activity;
    private AlertDialog dialog;
    LoadingDialog(Activity myActivity){
        activity = myActivity;
    }
    void startLoadingDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.loading_dialog,null));
        builder.setCancelable(true);

        dialog = builder.create();
        dialog.show();
    }
    void dimissDialog(){
        dialog.dismiss();
    }
}
