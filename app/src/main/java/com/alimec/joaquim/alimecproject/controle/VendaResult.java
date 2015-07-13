package com.alimec.joaquim.alimecproject.controle;

/**
 * Created by KithLenovo on 19/05/2015.
 */
public class VendaResult {


    private boolean success;
    private boolean sent;
    private String message;


//    public VendaResult(boolean success, boolean sent){
//        this.success = success;
//        this.sent = sent;
//        this.message = "";
//    }

    public VendaResult(boolean success, boolean sent, String message){
        this.success = success;
        this.sent = sent;
        this.message = message;
    }




    public boolean isSuccess() {
        return success;
    }

    public boolean isSent() {
        return sent;
    }

    public String getMessage() {
        return message;
    }
}
