resource "aws_lambda_function" "${functionId}" {
  filename      = "${targetDir}/${artifactId}-${version}-${classifier}.jar"
  function_name = "${functionId}"
  role          = aws_iam_role.lambda_exec.arn
  handler       = "${handlerWrapperFullyQualifiedName}::handleRequest"
  
  source_code_hash = filebase64sha256("${targetDir}/${artifactId}-${version}-${classifier}.jar")
  timeout = 30
  runtime = "java21"

  environment {
    variables = {
      # Add any environment variables your function needs
    }
  }
}

resource "aws_lambda_permission" "${functionId}_invoke" {
  statement_id  = "AllowAPIGatewayInvoke"
  action        = "lambda:InvokeFunction"
  function_name = aws_lambda_function.${functionId}.function_name
  principal     = "apigateway.amazonaws.com"

  source_arn = "${"$"}{aws_api_gateway_rest_api.public-api.execution_arn}/*/*"
}