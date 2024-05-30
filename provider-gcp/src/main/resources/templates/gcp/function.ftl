resource "aws_lambda_function" "${operationId}_function" {
  function_name = "${operationId}"
  role          = aws_iam_role.lambda_exec_role.arn
  handler       = "GcpFunctions${handler}Handler"
  runtime       = "java11"

  filename      = "lambda.zip"
  source_code_hash = filebase64sha256("lambda.zip")

}