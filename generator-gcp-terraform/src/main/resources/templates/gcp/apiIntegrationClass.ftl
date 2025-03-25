package ${packageName};

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import com.cloudhopper.mc.deployment.config.api.CloudRequestHandler;
import com.cloudhopper.mc.deployment.config.api.HandlerContext;
import io.swagger.v3.oas.annotations.extensions.ExtensionProperty;

@Path("${path}")
public class ${className} implements CloudRequestHandler<${inputType}, ${outputType}> {

    ${handlerClassName} handler = new ${handlerClassName}();

    @Operation(
        summary = "${summary}",
        description = "${description}",
        operationId = "${operationId}",
        parameters = {
            <#list parameters as param>
            @Parameter(
                in = ParameterIn.${param.in},
                name = "${param.name}"<#if param.description??>, description = "${param.description}"</#if><#if param.example??>, example = "${param.example}"</#if>
            )<#if param_has_next>,</#if>
            </#list>
        }, 
        extensions = {
            @io.swagger.v3.oas.annotations.extensions.Extension(
                name = "x-google-backend",
                properties = {
                    @ExtensionProperty(name = "address", value = "https://${"$"}{region}-${"$"}{project}.cloudfunctions.net/${operationId}"),
                    @ExtensionProperty(name = "protocol", value = "h2")
                }
            )
        }
    )
    @${httpMethod}
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public ${outputType} ${methodName}(${inputType} input, HandlerContext context) {
        return handler.handleRequest(input, context);
    }


}
