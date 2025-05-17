package ${package};


import com.cloudhopper.mc.annotations.ApiOperation;
import com.cloudhopper.mc.annotations.Function;
import com.cloudhopper.mc.runtime.CloudRequestHandler;
import com.cloudhopper.mc.runtime.HandlerContext;

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

