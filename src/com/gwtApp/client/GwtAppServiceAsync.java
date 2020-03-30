package com.gwtApp.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GwtAppServiceAsync {
    void getString(String s, AsyncCallback<String> async);
}
