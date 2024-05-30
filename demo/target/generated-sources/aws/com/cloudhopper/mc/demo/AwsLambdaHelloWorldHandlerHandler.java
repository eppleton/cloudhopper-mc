package com.cloudhopper.mc.demo;

import java.lang.String;
import java.lang.String;
import com.cloudhopper.mc.demo.HelloWorldHandler;
import com.cloudhopper.mc.provider.aws.AwsLambdaRequestHandler;


public class AwsLambdaHelloWorldHandlerHandler extends AwsLambdaRequestHandler<java.lang.String, java.lang.String> {

    public AwsLambdaHelloWorldHandlerHandler() {
        super(new HelloWorldHandler());
    }
}


