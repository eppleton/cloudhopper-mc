package ${package};

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.cloudhopper.mc.provider.azure.AzureBaseFunctionWrapper;
import ${inputTypeImport};
import ${outputTypeImport};
import ${handlerFullyQualifiedName};
import com.google.gson.reflect.TypeToken;


public class Azure${handler}ApiFunction extends AzureBaseFunctionWrapper<${inputType}, ${outputType}> {

    public Azure${handler}ApiFunction() {
        super(new ${handler}(), new TypeToken<${inputType}>(){}.getType());
    }

    @FunctionName("${functionId}_api")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", 
                        methods = {HttpMethod.GET, HttpMethod.POST},
                        authLevel = AuthorizationLevel.ANONYMOUS,
                        route = "${path}"
                        ) 
            HttpRequestMessage<String> request,
            final ExecutionContext context) {
        return handleRequest(request, context);
    }
}
