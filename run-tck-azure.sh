#!/bin/bash
set -e

if [ -z "$AZURE_SUBSCRIPTION_ID" ] || [ -z "$AZURE_TENANT_ID" ] || [ -z "$AZURE_CLIENT_ID" ] || [ -z "$AZURE_CLIENT_SECRET" ] || [ -z "$AZURE_REGION" ]; then
  echo "❌ Azure credentials not set"
  exit 1
fi

echo "✅ Azure credentials detected."

if [[ "$1" == "--rebuild" ]]; then
  echo "🔄 Rebuilding Docker image..."
  docker build -f test/system-tests/generator-azure-terraform/Dockerfile.dockerfile \
    -t cloudhopper-tck-azure .
else
  echo "📦 Skipping Docker rebuild (pass --rebuild to force)"
fi

echo "🚀 Running containerized TCK for Azure"
docker run --rm \
  -v "$PWD":/workspace \
  -v "$HOME/.m2":/root/.m2 \
  -w /workspace \
  -e ARM_SUBSCRIPTION_ID \
  -e ARM_CLIENT_ID \
  -e ARM_CLIENT_SECRET \
  -e ARM_TENANT_ID \
  cloudhopper-tck-azure bash -c "
    echo '🔍 Recompiling only system tests...'
    mvn -f /workspace/pom.xml install -DskipTests -pl test/system-tests/generator-azure-terraform &&
    echo '▶ Running TCK...'
    cd /workspace/test/system-tests/generator-azure-terraform/
    mvn exec:java \
      -Dexec.mainClass=com.cloudhopper.mc.test.system.tests.generator.azure.terraform.TckLauncher \
      -Dexec.classpathScope=test
  "