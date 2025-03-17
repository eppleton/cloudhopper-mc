
resource "aws_api_gateway_rest_api" "public-api" {
  name        = "${"$"}{var.environment}_${"$"}{var.project_name}_public_api"
  description = "Public API for ${"$"}{var.project_name}"
  endpoint_configuration {
    types = ["REGIONAL"]
  }
  binary_media_types = [
    "application/json"
  ]
  body = local.apidescription
  disable_execute_api_endpoint = false

  tags = {
    applicationRole = "api"
  }
}

resource "aws_iam_role" "lambda_exec" {
  name = "lambda_exec_role"

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
  role       = aws_iam_role.lambda_exec.name
  policy_arn = "arn:aws:iam::aws:policy/service-role/AWSLambdaBasicExecutionRole"
}


