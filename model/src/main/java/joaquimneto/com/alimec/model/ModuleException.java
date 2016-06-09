package joaquimneto.com.alimec.model;

/**
 * Created by KithLenovo on 04/03/2016.
 */
public class ModuleException extends Exception {

    public ModuleException(String message) {
        super(message);
    }

    public ModuleException(String message,Exception exp){
        super(message,exp);
    }

    public ModuleException(Exception e) {
        super(e);
    }

}
