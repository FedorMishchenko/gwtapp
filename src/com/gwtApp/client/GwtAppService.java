package com.gwtApp.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("service/gwtservice")
public interface GwtAppService extends RemoteService {

    String getString(String s);
}
