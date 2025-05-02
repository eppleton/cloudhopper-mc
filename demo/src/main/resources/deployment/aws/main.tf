terraform {
  backend "s3" {
    bucket = "cloudhopper-demo-terraform-state-bucket"
    key    = "state/terraform.tfstate"
    region = "eu-central-1"
  }
}

# Configure the AWS Provider
provider "aws" {
  region = var.aws_region
  
  # Uncomment and fill these if you want to specify credentials in the Terraform file
  # access_key = var.aws_access_key
  # secret_key = var.aws_secret_key
  
  # Alternatively, you can specify a profile
  profile = var.aws_profile
}

# Variables
variable "aws_region" {
  description = "The AWS region to deploy to"
  type        = string
  default     = "eu-central-1"  # Change this to your preferred default region
}

#variable "aws_access_key" {
#  description = "AWS access key"
#  type        = string
#  default     = ""  # Do not put actual credentials here
#}
#
#variable "aws_secret_key" {
#  description = "AWS secret key"
#  type        = string
#  default     = ""  # Do not put actual credentials here
#}

variable "aws_profile" {
  description = "AWS profile to use"
  type        = string
  default     = "dukehoff"
}

variable "project_name" {
  description = "The name of the project"
  type        = string
  default     = "my-cloudhopper-project"
}

variable "environment" {
  description = "The deployment environment (e.g., dev, staging, prod)"
  type        = string
  default     = "dev"
}


#
#resource "aws_cognito_user_pool" "main" {
#  name = "cloudhopper-user-pool"
#}
#
#resource "aws_cognito_user_pool_client" "main" {
#  name         = "cloudhopper-app-client"
#  user_pool_id = aws_cognito_user_pool.main.id
#
#  generate_secret = false
#  allowed_oauth_flows_user_pool_client = true
#
#  explicit_auth_flows = [
#    "ALLOW_USER_PASSWORD_AUTH",
#    "ALLOW_REFRESH_TOKEN_AUTH",
#    "ALLOW_USER_SRP_AUTH"
#  ]
#
#  allowed_oauth_flows = ["code"]
#  allowed_oauth_scopes = ["email", "openid"]
#  callback_urls         = ["https://example.com/callback"]
#  logout_urls           = ["https://example.com/logout"]
#  supported_identity_providers = ["COGNITO"]
#}
#
#resource "aws_apigatewayv2_authorizer" "cognito_jwt" {
#  name                       = "CognitoJWT"
#  api_id                     = aws_apigatewayv2_api.main.id
#  authorizer_type            = "JWT"
#  identity_sources           = ["$request.header.Authorization"]
#
#  jwt_configuration {
#    audience = [aws_cognito_user_pool_client.main.id]
#    issuer   = "https://${aws_cognito_user_pool.main.endpoint}"
#  }
#}
#
