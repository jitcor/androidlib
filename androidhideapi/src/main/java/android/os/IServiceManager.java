package android.os;

public interface IServiceManager extends IInterface {

    void tryUnregisterService(String name, IBinder service);

    IBinder getService(String name);

    void registerForNotifications(String name, IServiceCallback cb);

    abstract class Stub extends Binder implements IServiceManager {
        public static IServiceManager asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}
