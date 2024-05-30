resource "aws_lambda_function" "${functionId}_function" {
  function_name = "${functionId}"
  role          = aws_iam_role.lambda_exec_role.arn
  handler       = "${handler}AWSLambdaHandler"
  runtime       = "java11"

  filename      = "lambda.zip"
  source_code_hash = filebase64sha256("lambda.zip")

}