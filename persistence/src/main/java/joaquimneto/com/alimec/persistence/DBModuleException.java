package joaquimneto.com.alimec.persistence;

import joaquimneto.com.alimec.model.ModuleException;

/**
 * Created by KithLenovo on 03/02/2016.
 */
public class DBModuleException extends ModuleException {

    public DBModuleException(String message) {
        super(message);
    }

    public DBModuleException(String message,Exception exp){
        super(message,exp);
    }

    public DBModuleException(Exception e) {
        super(e);
    }

}
