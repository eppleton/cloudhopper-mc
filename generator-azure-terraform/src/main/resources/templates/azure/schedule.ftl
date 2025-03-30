package ${package};

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import com.cloudhopper.mc.provider.azure.AzureBaseFunctionWrapper;
import ${inputTypeImport};
import ${outputTypeImport};
import ${handlerFullyQualifiedName};
import com.google.gson.reflect.TypeToken;


public class Azure${handler}ScheduledFunction extends AzureBaseFunctionWrapper<${inputType}, ${outputType}> {

    public Azure${handler}ScheduledFunction() {
        super(new ${handler}(), new TypeToken<${inputType}>(){}.getType());
    }

    @FunctionName("${functionId}_scheduled")
    public void runTimer(
            @TimerTrigger(name = "timerInfo", schedule = "${scheduleExpression}") String timerInfo,
            final ExecutionContext context) {
        handleScheduledRequest(context);
    }   
}