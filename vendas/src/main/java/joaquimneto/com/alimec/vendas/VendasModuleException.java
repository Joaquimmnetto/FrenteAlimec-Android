package joaquimneto.com.alimec.vendas;

import joaquimneto.com.alimec.model.ModuleException;

/**
 * Created by KithLenovo on 03/02/2016.
 */
public class VendasModuleException extends ModuleException {

    public VendasModuleException(String message) {
        super(message);
    }

    public VendasModuleException(String message, Exception exp){
        super(message,exp);
    }

    public VendasModuleException(Exception e) {
        super(e);
    }

}
