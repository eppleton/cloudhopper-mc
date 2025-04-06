package ${handlerInfo.handlerPackage};

import ${handlerInfo.inputTypeImport};
import ${handlerInfo.outputTypeImport};
import ${handlerInfo.handlerFullyQualifiedName};
import com.cloudhopper.mc.provider.aws.AwsLambdaRequestHandler;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayV2HTTPResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;

/**
 * This class contains both a plain Lambda integration and an API proxy integration.
 */
public class AwsLambda${handlerInfo.handlerClassName}Handler {

    /**
     * Plain Lambda integration.
     */
    public static class Plain extends AwsLambdaRequestHandler<${handlerInfo.inputType}, ${handlerInfo.outputType}> {
        public Plain() {
            super(new ${handlerInfo.handlerClassName}());
        }
    }

    /**
     * API Gateway Proxy integration.
     */
    public static class ApiProxy implements RequestHandler<APIGatewayV2HTTPEvent, APIGatewayV2HTTPResponse> {

        // Delegate to the plain integration
        private final AwsLambdaRequestHandler<${handlerInfo.inputType}, ${handlerInfo.outputType}> delegate = new Plain();
        private final Gson gson = new Gson();

        @Override
        public APIGatewayV2HTTPResponse handleRequest(APIGatewayV2HTTPEvent event, Context awsContext) {
            String body = event.getBody();
            // Decode the body if it is base64 encoded.
            if (event.getIsBase64Encoded() && body != null) {
                body = new String(Base64.getDecoder().decode(body), StandardCharsets.UTF_8);
            }
            // Convert the event body from JSON into the expected input type.
            <#if handlerInfo.inputType == "java.util.Map<java.lang.String,java.lang.Object>">
            Type mapType = new TypeToken<java.util.Map<java.lang.String,java.lang.Object>>(){}.getType();
            java.util.Map<java.lang.String,java.lang.Object> input = gson.fromJson(body, mapType);
            <#else>
            ${handlerInfo.inputType} input = gson.fromJson(body, ${handlerInfo.inputType}.class);
            </#if>            
            // Call the provider-agnostic handler.
            ${handlerInfo.outputType} result = delegate.handleRequest(input, awsContext);

            // Build the API Gateway response.
            APIGatewayV2HTTPResponse response = new APIGatewayV2HTTPResponse();
            response.setStatusCode(200);
            response.setHeaders(Collections.singletonMap("Content-Type", "application/json"));
            // If result is not already a JSON string, you may need to convert it.
            response.setBody(result.toString());
            return response;
        }
    }
}


