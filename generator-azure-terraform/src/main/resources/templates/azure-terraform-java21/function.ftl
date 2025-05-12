package ${handlerInfo.handlerPackage};

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.cloudhopper.mc.provider.azure.AzureBaseFunctionWrapper;
import ${handlerInfo.inputTypeImport};
import ${handlerInfo.outputTypeImport};
import ${handlerInfo.handlerFullyQualifiedName};
import com.google.gson.reflect.TypeToken;

public class Azure${handlerInfo.handlerClassName}Function extends AzureBaseFunctionWrapper<${handlerInfo.inputType}, ${handlerInfo.outputType}> {

    public Azure${handlerInfo.handlerClassName}Function() {
        super(new ${handlerInfo.handlerClassName}(), new TypeToken<${handlerInfo.inputType}>(){}.getType());
    }

    @FunctionName("${handlerInfo.functionId}")
    public HttpResponseMessage run(
            @HttpTrigger(name = "req", 
                         methods = {HttpMethod.POST, HttpMethod.GET}, 
                         authLevel = AuthorizationLevel.ANONYMOUS,
                         route = "${handlerInfo.functionId}")
            HttpRequestMessage<String> request,
            final ExecutionContext context) {
        return handleRequest(request, context, "/${handlerInfo.functionId}_http_function_trigger");
    }
}