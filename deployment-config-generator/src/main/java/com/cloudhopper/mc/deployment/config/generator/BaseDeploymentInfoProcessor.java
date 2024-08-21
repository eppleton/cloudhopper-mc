/*
 * Copyright (C) 2024 antonepple
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.cloudhopper.mc.deployment.config.generator;

/*-
 * #%L
 * deployment-config-generator - a library from the "Cloudhopper" project.
 * 
 * Eppleton IT Consulting designates this particular file as subject to the "Classpath"
 * exception as provided in the README.md file that accompanies this code.
 * %%
 * Copyright (C) 2024 Eppleton IT Consulting
 * %%
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 * #L%
 */
import com.cloudhopper.mc.deployment.config.api.GenericDeploymentConfigGenerator;
import com.cloudhopper.mc.deployment.config.spi.DeploymentConfigGenerator;
import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

/**
 *
 * @author antonepple
 */
public abstract class BaseDeploymentInfoProcessor extends AbstractProcessor {

    protected List<DeploymentConfigGenerator> generators;
    protected DeploymentConfigGenerator deploymentGenerator;
    protected String providerName;
    protected String configOutputDir = "target/generated-config";
    protected String artifactId;
    protected String classifier;
    protected String version;
    protected String targetDir;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        Messager messager = processingEnv.getMessager();
        messager.printMessage(Diagnostic.Kind.NOTE, "Initializing BaseDeploymentInfoProcessor");

        super.init(processingEnv);
        ServiceLoader<DeploymentConfigGenerator> loader = ServiceLoader.load(DeploymentConfigGenerator.class, ServerlessFunctionProcessor.class.getClassLoader());
        generators = new ArrayList<>();
        for (DeploymentConfigGenerator generator : loader) {
            generators.add(generator);
        }

        providerName = processingEnv.getOptions().getOrDefault("cloudprovider", "aws");
        configOutputDir = processingEnv.getOptions().getOrDefault("configOutputDir", "target/generated-config");
        artifactId = processingEnv.getOptions().getOrDefault("artifactId", "default-artifactId");
        classifier = processingEnv.getOptions().getOrDefault("classifier", "default-classifier");
        version = processingEnv.getOptions().getOrDefault("version", "default-version");
        targetDir = processingEnv.getOptions().getOrDefault("targetDir", "./target");

        if ("default-artifactId".equals(artifactId) || "default-classifier".equals(classifier) || "default-version".equals(version)) {
            messager.printMessage(Diagnostic.Kind.WARNING, "One or more required compiler arguments (artifactId, classifier, version) are using default values. Please ensure these are provided.");
        }
        deploymentGenerator = getDeploymentGenerator();
    }

    protected DeploymentConfigGenerator getDeploymentGenerator() {
        DeploymentConfigGenerator deploymentGenerator = null;
        for (DeploymentConfigGenerator generator : generators) {
            if (generator.supportsProvider(providerName)) {
                deploymentGenerator = generator;
                break;
            }
        }
        // If no registered generator is found, use the GenericDeploymentConfigGenerator
        if (deploymentGenerator == null) {
            deploymentGenerator = new GenericDeploymentConfigGenerator(processingEnv);
        }
        return deploymentGenerator;
    }

    protected boolean implementsInterface(TypeElement classElement, String interfaceName) {
        Types typeUtils = processingEnv.getTypeUtils();
        Elements elementUtils = processingEnv.getElementUtils();

        TypeElement requiredInterfaceElement = elementUtils.getTypeElement(interfaceName);
        if (requiredInterfaceElement == null) {
            System.err.println("Required interface not found: " + interfaceName);
            return false;
        }

        TypeMirror requiredInterface = requiredInterfaceElement.asType();
        TypeMirror rawRequiredInterface = typeUtils.erasure(requiredInterface);
        System.err.println("Created raw TypeMirror " + rawRequiredInterface.toString());

        for (TypeMirror implementedInterface : classElement.getInterfaces()) {
            TypeMirror rawImplementedInterface = typeUtils.erasure(implementedInterface);
            if (typeUtils.isSameType(rawImplementedInterface, rawRequiredInterface)) {
                return true;
            }
        }

        // Recursively check superclasses
        TypeMirror superclass = classElement.getSuperclass();
        if (superclass.getKind() != javax.lang.model.type.TypeKind.NONE) {
            return implementsInterface((TypeElement) typeUtils.asElement(superclass), interfaceName);
        }
        return false;
    }

    protected String getPackageName(TypeElement classElement) {
        String qualifiedName = classElement.getQualifiedName().toString();
        return qualifiedName.substring(0, qualifiedName.lastIndexOf('.'));
    }

}
