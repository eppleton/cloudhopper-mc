# ☁️ provider-aws

This module provides AWS-specific base classes for Cloudhopper functions. These classes act as bridges between the AWS Lambda runtime and the cloud-neutral `CloudRequestHandler<I, O>` interface.

They are used by generated handler classes in the `generator-aws-terraform` module to support:

- Plain Lambda invocations
- Scheduled executions (e.g., EventBridge)
- API Gateway HTTP APIs (v2), including both proxy and typed events

---

## 🚀 Purpose

These base classes enable Cloudhopper functions to:

- Stay **platform-neutral** in their logic
- Be executed in **multiple AWS contexts**
- Use a consistent interface for path/query parameters and runtime metadata

---

## 🔧 Available Base Classes

| Class | Trigger Type | Input | Output | Used for |
|-------|--------------|-------|--------|----------|
| `AwsLambdaRequestHandler<I, O>` | Lambda direct or scheduled | POJO or Map | POJO | `Plain`, `PlainSchedule` |
| `ApiGatewayV2ProxyRequestHandler<I, O>` | API Gateway v2 (proxy) | `APIGatewayV2ProxyRequestEvent` | `APIGatewayV2ProxyResponseEvent` | `ApiProxyV2` |
| `ApiGatewayV2HttpEventHandler<I, O>` | API Gateway v2 (typed) | `APIGatewayV2HTTPEvent` | `APIGatewayV2HTTPResponse` | `ApiHttpV2` |
| `ApiGatewayEventRouter<I, O>` | Dynamic | `Object` | `Object` | `Auto` router that delegates to the correct handler |

---

## 🧪 Example Usage

Generated handlers typically look like this:

```java
public class AwsLambdaMyFunctionHandler {

    public static class Plain extends AwsLambdaRequestHandler<InputType, OutputType> {
        public Plain() {
            super(new MyFunction());
        }
    }

    public static class PlainSchedule extends AwsLambdaRequestHandler<Map<String, Object>, OutputType> {
        public PlainSchedule() {
            super(new MyFunction());
        }
    }

    public static class ApiProxyV2 extends ApiGatewayV2ProxyRequestHandler<InputType, OutputType> {
        public ApiProxyV2() {
            super(new MyFunction(), InputType.class);
        }
    }

    public static class ApiHttpV2 extends ApiGatewayV2HttpEventHandler<InputType, OutputType> {
        public ApiHttpV2() {
            super(new MyFunction(), InputType.class);
        }
    }

    public static class Auto extends ApiGatewayEventRouter<InputType, OutputType> {
        public Auto() {
            super(new Plain(), new PlainSchedule(), new ApiProxyV2(), new ApiHttpV2());
        }
    }
}
```

In AWS deployments, the `Auto` handler is typically registered to dynamically route events to the appropriate handler class.

---

## 📦 Packaging

All classes in this module are runtime dependencies and **must** be included in the shaded JAR used for deployment.

They are referenced by the generated handlers and are required for correct dispatch and integration with the AWS Lambda platform.

---

## 💡 For Generator Authors

If you're building your own AWS generator module (like `generator-aws-terraform`), you should:

- Use the correct base class for each handler type
- Route schedule and direct invocations to `AwsLambdaRequestHandler`
- Route HTTP API events to one of the two API Gateway handler base classes
- Optionally register `Auto` as the main handler to allow flexible dispatch

---

## 📚 Related Modules

| Module | Purpose |
|--------|---------|
| `generator-aws-terraform` | Uses these base classes to generate AWS Lambda handler classes |
| `deployment-config-api` | Provides `CloudRequestHandler` and `HandlerContext` interfaces |

---

```java title="AwsLambdaRequestHandler.java"
public abstract class AwsLambdaRequestHandler<I, O> implements RequestHandler<I, O> {
    // ...
}
```

These base classes form the backbone of Cloudhopper’s AWS Lambda support, enabling a clean separation of infrastructure, runtime integration, and application logic.