package com.cloudhopper.mc.demo;

import java.lang.String;
import java.lang.String;
import com.cloudhopper.mc.demo.HelloWorld2;
import com.cloudhopper.mc.provider.aws.AwsLambdaRequestHandler;


public class AwsLambdaHelloWorld2Handler extends AwsLambdaRequestHandler<java.lang.String, java.lang.String> {

    public AwsLambdaHelloWorld2Handler() {
        super(new HelloWorld2());
    }
}


