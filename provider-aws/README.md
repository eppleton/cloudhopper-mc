# provider-aws

This module contains a single utility class: `AwsLambdaRequestHandler`.

It provides the glue between Cloudhopper’s cloud-neutral function interface and the AWS Lambda runtime. This class is especially important for **developers implementing custom AWS-based generators**, such as in the `generator-aws-terraform` module.

---

## 🚀 Purpose

The `AwsLambdaRequestHandler<I, O>` class allows generated AWS handler classes to:

- Bridge between AWS Lambda’s `RequestHandler<I, O>` interface and the generic `CloudRequestHandler<I, O>`
- Translate AWS-specific `Context` into Cloudhopper’s `HandlerContext`
- Stay vendor-neutral in the function logic, while enabling platform-specific entrypoints

---

## 🔧 How It's Used

Generated handler classes in `generator-aws-terraform` extend this class and delegate to your user-defined function:

```java
public class MyGeneratedHandler extends AwsLambdaRequestHandler<InputType, OutputType> {
    public MyGeneratedHandler() {
        super(new MyCloudhopperFunction());
    }
}
```

This allows the developer to write platform-agnostic business logic and let Cloudhopper handle:

- AWS integration
- Context adaptation
- Handler wiring

---

## 📦 Packaging

This class is included in the shaded JAR produced for AWS deployment. It **must** be available in the deployment runtime to ensure the generated handler can be invoked.

---

## 💡 For Generator Authors

If you're building your own AWS generator (e.g. a custom version of `generator-aws-terraform`), you should:

- Use this class in your generated AWS Lambda handler templates
- Ensure it is either:
  - Included in your runtime dependency tree, or
  - Generated into the function JAR via annotation processor output

---

## 📚 Related Modules

| Module | Purpose |
|--------|---------|
| `generator-aws-terraform` | Uses this base class to generate AWS Lambda handler classes |
| `deployment-config-api` | Provides `CloudRequestHandler` and `HandlerContext` interfaces |

---

```java title="AwsLambdaRequestHandler.java"
public abstract class AwsLambdaRequestHandler<I, O> implements RequestHandler<I, O> {
    // ...
}
```

This class acts as the AWS-specific adapter between infrastructure and logic — making it a central building block for serverless interoperability in Cloudhopper.
