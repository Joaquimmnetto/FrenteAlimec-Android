package joaquimneto.com.alimec.serverio;

import joaquimneto.com.alimec.model.ModuleException;

/**
 * Created by KithLenovo on 03/02/2016.
 */
public class ServerModuleException extends ModuleException {

    public ServerModuleException(String message) {
        super(message);
    }

    public ServerModuleException(String message, Exception exp){
        super(message,exp);
    }

    public ServerModuleException(Exception e) {
        super(e);
    }

}
