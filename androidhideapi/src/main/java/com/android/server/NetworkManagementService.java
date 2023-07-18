package com.android.server;

public class NetworkManagementService {
     public static final String TAG = NetworkManagementService.class.getSimpleName();

     public void setFirewallEnabled(boolean enabled) {
          throw new UnsupportedOperationException();
     }

     public boolean isFirewallEnabled() {
          throw new UnsupportedOperationException();
     }

     public void setFirewallUidRule(int chain, int uid, int rule) {
          throw new UnsupportedOperationException();
     }

     public void setFirewallUidRules(int chain, int[] uids, int[] rules) {
          throw new UnsupportedOperationException();
     }

     public void setFirewallChainEnabled(int chain, boolean enable) {
          throw new UnsupportedOperationException();
     }

     public boolean isNetworkActive() {
          throw new UnsupportedOperationException();
     }
}
