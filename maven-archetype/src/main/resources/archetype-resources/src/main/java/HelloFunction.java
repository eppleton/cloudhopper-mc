package ${package};


import eu.cloudhopper.mc.annotations.ApiOperation;
import eu.cloudhopper.mc.annotations.Function;
import eu.cloudhopper.mc.runtime.CloudRequestHandler;
import eu.cloudhopper.mc.runtime.HandlerContext;

import java.util.Map;

public class HelloFunction implements CloudRequestHandler<Void, String> {

    @Function(name = "hello")
    @ApiOperation(
        method      = "GET",
        path        = "/hello",
        summary     = "Say Hello",
        description = "Returns a friendly greeting"
    )
    @Override
    public String handleRequest(Void input, Map<String, String> pathParams, Map<String, String> queryParams, HandlerContext context) {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}

