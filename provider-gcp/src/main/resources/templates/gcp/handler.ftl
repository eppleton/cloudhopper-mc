package ${package};

import ${inputType};
import ${outputType};
import ${handlerImport};
import com.cloudhopper.mc.provider.gcp.GcpHttpRequestHandler;


public class GcpFunctions${handler}Handler extends GcpHttpRequestHandler<${inputType}, ${outputType}> {

    public GcpFunctions${handler}Handler() {
        super(new ${handler}());
    }
}

