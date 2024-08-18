package com.cloudhopper.mc.deployment.config.api;

/*-
 * #%L
 * deployment-config-api - a library from the "Cloudhopper" project.
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
/**
 *
 * @author antonepple
 */
public class HandlerInfo {

    private final String functionId;
    private final String handlerClassName;
    private final String handlerFullyQualifiedName;
    private final String handlerPackage;
    private final String handlerMethod;
    private final String inputType;
    private final String outputType;
    private final String artifactId;
    private final String version;
    private final String classifier;
    private final String targetDir;
    private String wrapperFullyQualifiedName;

    public String getWrapperFullyQualifiedName() {
        return wrapperFullyQualifiedName;
    }

    public String getInputTypeImport() {
        return stripGenericArguments(inputType);
    }

    public String getOutputTypeImport() {
        return stripGenericArguments(outputType);
    }
    
    private String stripGenericArguments(String type) {
        int genericStart = type.indexOf('<');
        if (genericStart != -1) {
            return type.substring(0, genericStart);
        }
        return type;
    }

    public void setWrapperClassName(String wrapperFullyQualifiedName) {
        this.wrapperFullyQualifiedName = handlerPackage + "." + wrapperFullyQualifiedName;
    }

    public String getHandlerClassName() {
        return handlerClassName;
    }

    public String getHandlerFullyQualifiedName() {
        return handlerFullyQualifiedName;
    }

    public String getHandlerPackage() {
        return handlerPackage;
    }

    public String getHandlerMethod() {
        return handlerMethod;
    }

    public String getInputType() {
        return inputType;
    }

    public String getOutputType() {
        return outputType;
    }

    public String getFunctionId() {
        return functionId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public String getVersion() {
        return version;
    }

    public String getClassifier() {
        return classifier;
    }

    public String getTargetDir() {
        return targetDir;
    }

    public HandlerInfo(String functionId, String handlerClassName, String handlerFullyQualifiedName,
            String handlerPackage, String handlerMethod, String inputType, String outputType,
            String artifactId, String version, String classifier, String targetDir) {
        this.targetDir = targetDir;
        this.artifactId = artifactId;
        this.version = version;
        this.classifier = classifier;
        this.functionId = functionId;
        this.handlerClassName = handlerClassName;
        this.handlerFullyQualifiedName = handlerFullyQualifiedName;
        this.handlerPackage = handlerPackage;
        this.handlerMethod = handlerMethod;
        this.inputType = inputType;
        this.outputType = outputType;
    }

}
