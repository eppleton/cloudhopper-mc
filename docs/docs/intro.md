---
sidebar_position: 1
---

# Cloudhopper

**Cloudhopper** is an open-source toolkit for building truly vendor-neutral serverless Java applications. By leveraging annotations, templates, and generated code, Cloudhopper makes it painless to switch between various cloud providers (or in the future maybe on-premises Kubernetes clusters) without tying your code to proprietary services.

## Motivation

Many serverless frameworks and cloud ecosystems still impose hidden dependencies on specific platforms. Cloudhopper tackles this challenge by providing common abstractions (e.g., for persistence or messaging) directly at the code level. Differences in infrastructure are handled through configurable templates and generated deployment descriptors, allowing teams to avoid vendor lock-in.

## How It Works

1. **Annotation-Driven**
   - Mark serverless functions in Java with custom annotations.
   - An annotation processor then generates integration classes, deployment descriptors (e.g., Terraform, openTofu), and documentation (e.g., OpenAPI).

2. **Template-Based**
   - Standard templates are available for popular platforms (e.g., AWS).
   - Users can add or override templates to accommodate custom requirements.

3. **Build-Tool Integration**
   - Compatible with Maven or Gradle.
   - Configure the generator via your build settings to produce the required code for your target platform.

4. **Genuinely Vendor-Neutral**
   - Use platform-specific artifacts only through generated descriptors, making provider switching effortless.

## Features

- **Automatic Generation** of deployment scripts and documentation.
- **Extensibility** with your own code generators or templates.
- **Seamless Platform Switching** 

## Installation

1. **Include the Dependency**
   - In your `pom.xml` (Maven) or `build.gradle` (Gradle), add the Cloudhopper dependency.
2. **Enable Annotation Processing**
   - Ensure annotation processing is activated in your project build settings.
3. **Configuration**
   - Specify which templates or deployment targets to use in your build plugin configuration.




