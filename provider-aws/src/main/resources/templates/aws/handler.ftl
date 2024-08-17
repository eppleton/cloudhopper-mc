package ${package};

import ${inputType};
import ${outputType};
import ${handlerFullyQualifiedName};
import com.cloudhopper.mc.provider.aws.AwsLambdaRequestHandler;


public class AwsLambda${handler}Handler extends AwsLambdaRequestHandler<${inputType}, ${outputType}> {

    public AwsLambda${handler}Handler() {
        super(new ${handler}());
    }
}

