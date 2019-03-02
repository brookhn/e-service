package com.pp.server.HystrixTest;

import org.springframework.cloud.netflix.hystrix.HystrixCommands;

public class ThirdPartyRequest extends HystrixCommands {



    public ThirdPartyRequest()
    {

    }

    protected  String run()
    {
        return "";
    }
}
