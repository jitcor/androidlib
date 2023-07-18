package android.os;

public interface INetworkManagementService extends IInterface {
    void setFirewallEnabled(boolean enabled);

    boolean isFirewallEnabled();

    void setFirewallUidRule(int chain, int uid, int rule);

    void setFirewallUidRules(int chain, int[] uids, int[] rules);

    void setFirewallChainEnabled(int chain, boolean enable);

    boolean isNetworkActive();

    abstract class Stub extends Binder implements INetworkManagementService {

        public static INetworkManagementService asInterface(IBinder obj) {
            throw new UnsupportedOperationException();
        }
    }
}
