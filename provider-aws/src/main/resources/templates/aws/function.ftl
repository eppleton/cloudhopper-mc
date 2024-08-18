resource "aws_lambda_function" "${functionId}" {
  filename      = "${targetDir}/${artifactId}-${version}-${classifier}.jar"
  function_name = "${functionId}"
  role          = aws_iam_role.${functionId}_lambda_exec.arn
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

resource "aws_iam_role" "${functionId}_lambda_exec" {
  name = "${functionId}_lambda_role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [{
      Action = "sts:AssumeRole"
      Effect = "Allow"
      Principal = {
        Service = "lambda.amazonaws.com"
      }
    }]
  })
}

resource "aws_iam_role_policy_attachment" "${functionId}_lambda_policy" {
  role       = aws_iam_role.${functionId}_lambda_exec.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}

