package ${package};

import ${inputTypeImport};
import ${outputTypeImport};
import ${handlerFullyQualifiedName};

import com.cloudhopper.mc.provider.gcp.GcpCloudFunctionRequestHandler;
import com.google.cloud.functions.HttpFunction;
import com.google.cloud.functions.HttpRequest;
import com.google.cloud.functions.HttpResponse;
import com.google.gson.reflect.TypeToken;

public class Gcp${handler}Function extends GcpCloudFunctionRequestHandler<${inputType}, ${outputType}> {

    public Gcp${handler}Function() {
        super(new ${handler}(), new TypeToken<${inputType}>() {});
    }

 
}