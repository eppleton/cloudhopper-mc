resource "aws_apigatewayv2_api" "main" {
  name          = "${"$"}{var.environment}_${"$"}{var.project_name}_public_api"
  protocol_type = "HTTP"
  target        = null
  route_selection_expression = "${"$"}request.method ${"$"}request.path"
}
