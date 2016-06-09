package com.alimec.joaquim.alimecproject.activities.fragment.dialog;

import android.app.DialogFragment;
import android.widget.Toast;

/**
 * Created by KithLenovo on 19/08/2015.
 */
public abstract class RetDialogFragment extends DialogFragment{

    public static interface DialogSuccessListener {

        void onDialogSuccess(Object... obj);

    }

    public static interface DialogFailListener{

        void onDialogFail(Exception e, Object... obj);
    }

    private DialogSuccessListener dialogSuccess;
    private DialogFailListener dialogFail = new DialogFailListener() {
        @Override
        public void onDialogFail(Exception e, Object... obj) {
            if(obj != null){
                Toast.makeText(getActivity(),obj[0].toString(),Toast.LENGTH_LONG).show();
            }
        }
    };


    protected void dialogSuccess(Object... obj){
        if(dialogSuccess != null){
            dialogSuccess.onDialogSuccess(obj);
        }

    }

    protected void dialogFail(Exception e, Object... obj){
        if(dialogFail != null){
            dialogFail.onDialogFail(e, obj);
        }
    }

    public void setDialogSuccessListener(DialogSuccessListener dialogSuccess){
        this.dialogSuccess = dialogSuccess;
    }

    public void setDialogFailListener(DialogFailListener dialogFail) {
        this.dialogFail = dialogFail;
    }



}
