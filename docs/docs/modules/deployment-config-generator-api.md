---
title: Deployment Config Generator API
---

# deployment-config-generator-api

This module provides the public extension API for implementing custom deployment configuration generators in Cloudhopper.

It defines the **SPI**, annotations, and support types used by integrators to declare and implement generators that transform annotated Java functions into provider-specific deployment artifacts (e.g., Terraform, CloudFormation, etc.).

---

## 💡 Purpose

This module is intended for:
- Platform integrators building custom code generators
- Template-based generators declaring supported features
- The Cloudhopper annotation processor, which uses this metadata to validate annotations and orchestrate generation

---

## 🔌 Key Components

### SPI

- [`DeploymentConfigGenerator`](../deployment-config-generator-api/spi/DeploymentConfigGenerator.java):  
  Main interface to be implemented by generator modules.

### Annotations

- [`@GeneratorFeatures`](../deployment-config-generator-api/annotations/GeneratorFeatures.java):  
  Declares the generator ID and supported features.
- [`@GeneratorFeature`](../deployment-config-generator-api/annotations/GeneratorFeature.java):  
  Describes which annotations and attributes a generator supports.

### Support Types

- [`HandlerInfo`](../deployment-config-generator-api/api/HandlerInfo.java):  
  Encapsulates function metadata passed to generators.
- [`GenerationPhase`](../deployment-config-generator-api/api/GenerationPhase.java):  
  Enum describing the stages of generation (e.g. FUNCTION, API, etc.)
- [`ConfigGenerationException`](../deployment-config-generator-api/api/ConfigGenerationException.java):  
  Used to signal errors during generation.

---

## 🧩 Example

~~~java
@GeneratorFeatures(
    generatorId = "aws-terraform-java21",
    supportedFeatures = {
        @GeneratorFeature(
            supportedAnnotation = Function.class,
            supportedAttributes = {
                FunctionAttribute.NAME,
                FunctionAttribute.MEMORY,
                FunctionAttribute.TIMEOUT
            }
        )
    }
)
public class AwsTemplateRegistration implements TemplateRegistration {
    ...
}
~~~

---

## 📦 Package Overview

| Package                                      | Description                                  |
|---------------------------------------------|----------------------------------------------|
| `com.cloudhopper.mc.generator.api`          | Support types and enums                      |
| `com.cloudhopper.mc.generator.api.spi`      | SPI interface to be implemented by generators|
| `com.cloudhopper.mc.generator.api.annotations` | Metadata annotations for declaring generator features |

---

## 🚀 Related Modules

- `deployment-config-processor`: consumes this SPI to perform annotation processing
- `deployment-config-generator-generic`: default implementation using templates
