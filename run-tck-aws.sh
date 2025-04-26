#!/bin/bash
set -e

echo "📦 Building Docker image..."
docker build -f test/system-tests/generator-aws-terraform/Dockerfile.dockerfile \
  -t cloudhopper-tck-aws .

echo "🚀 Running containerized TCK for AWS"
docker run --rm \
  -v "$PWD":/workspace \
  -w /workspace \
  -e AWS_ACCESS_KEY_ID \
  -e AWS_SECRET_ACCESS_KEY \
  -e AWS_REGION \
  cloudhopper-tck-aws bash -c "
    echo '🔧 Installing all modules (no tests)...'
    mvn -f /workspace/pom.xml clean install -DskipTests &&
    echo '▶ Executing TCK test launcher...'
    cd /workspace/test/system-tests/generator-aws-terraform/
    mvn -f pom.xml \
      exec:java \
      -Dexec.mainClass=com.cloudhopper.mc.test.system.tests.generator.aws.terraform.TckLauncher \
      -Dexec.classpathScope=test
  "