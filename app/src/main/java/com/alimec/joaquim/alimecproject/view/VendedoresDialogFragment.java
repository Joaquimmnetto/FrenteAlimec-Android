package com.alimec.joaquim.alimecproject.view;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.View;
import android.widget.GridLayout;

import com.alimec.joaquim.alimecproject.R;

/**
 * Created by KithLenovo on 02/05/2015.
 */
public class VendedoresDialogFragment extends DialogFragment {


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        GridLayout dialogView = (GridLayout) View.inflate(getActivity(), R.layout.fragment_selecionar_vendedor,null);

        //TODO: porra toda de vendedores.

        return super.onCreateDialog(savedInstanceState);
    }
}
