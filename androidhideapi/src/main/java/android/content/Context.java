package android.content;

import android.os.Handler;
import android.os.IBinder;
import android.os.UserHandle;

public class Context {
    /**
     * Constant for the internal network management service, not really a Context service.
     *
     * @hide
     */
    public static final String NETWORKMANAGEMENT_SERVICE = "network_management";

    public IBinder getActivityToken() {
        throw new UnsupportedOperationException("STUB");
    }

    public Intent registerReceiverAsUser(BroadcastReceiver receiver, UserHandle user,
                                         IntentFilter filter, String broadcastPermission, Handler scheduler) {
        throw new UnsupportedOperationException("STUB");
    }
}
