resource "aws_lambda_function" "hello_world_2_function" {
  function_name = "hello_world_2"
  role          = aws_iam_role.lambda_exec_role.arn
  handler       = "HelloWorld2AWSLambdaHandler"
  runtime       = "java11"

  filename      = "lambda.zip"
  source_code_hash = filebase64sha256("lambda.zip")

}