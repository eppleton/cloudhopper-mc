package ${package};

import ${inputType};
import ${outputType};
import ${handlerImport};
import com.cloudhopper.mc.provider.azure.AzureFunctionRequestHandler;


public class AzureFunction${handler}Handler extends AzureFunctionRequestHandler<${inputType}, ${outputType}> {

    public AzureFunction${handler}Handler() {
        super(new ${handler}());
    }
}

