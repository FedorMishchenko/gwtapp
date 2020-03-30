package com.gwtApp.server;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.gwtApp.client.GwtAppService;

public class GwtAppServiceImpl extends RemoteServiceServlet implements GwtAppService {

    public String getString(String s) {
        return s;
    }
}
