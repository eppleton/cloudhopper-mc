resource "aws_lambda_function" "${handlerInfo.functionId}" {
  filename      = "${handlerInfo.targetDir}/${handlerInfo.artifactId}-${handlerInfo.version}-${handlerInfo.classifier}.jar"
  function_name = "${handlerInfo.functionId}"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "${handlerWrapperFullyQualifiedName}$Auto::handleRequest"
  
  source_code_hash = filebase64sha256("${handlerInfo.targetDir}/${handlerInfo.artifactId}-${handlerInfo.version}-${handlerInfo.classifier}.jar")
  timeout = 30
  runtime = "java21"

  environment {
    variables = {
      # Add any environment variables your function needs
    }
  }
}

