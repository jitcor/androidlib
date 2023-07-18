package android.os;

public interface IServiceCallback extends IInterface {
    abstract class Stub extends Binder implements IServiceCallback {
    }

    /**
     * Called when a service is registered.
     *
     * @param name   the service name that has been registered with
     * @param binder the binder that is registered
     */
    void onRegistration(String name, IBinder binder) throws RemoteException;
}
