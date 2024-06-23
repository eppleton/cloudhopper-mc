package ${package};

import ${inputType};
import ${outputType};
import ${handlerImport};
import com.cloudhopper.mc.provider.aws.AwsLambdaRequestHandler;


public class AwsLambda${handler}Handler2 extends AwsLambdaRequestHandler<${inputType}, ${outputType}> {

    public AwsLambda${handler}Handler2() {
        super(new ${handler}());
    }
}

