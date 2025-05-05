locals {
  openapi_paths_fragments = [
  <#list lambdaMap?keys as key>
    templatefile("${"$"}{path.module}/api-routes/${lambdaMap[key]}_api_apiIntegration.json", {
      region  = var.gcp_region,
      project = var.gcp_project_id
    })<#if key_has_next>,</#if>
  </#list>
  ]

  openapi_description = <<EOF
{
  "swagger": "2.0",
  "info": {
    "title": "Cloudhopper Generated API",
    "version": "1.0"
  },
  "paths": {
    ${"$"}{join(",\n", local.openapi_paths_fragments)}
  }
}
EOF
}

resource "google_api_gateway_api" "api" {
  provider = google-beta
  api_id = "${"$"}{var.gcp_project_id}-api"
  project = var.gcp_project_id
  display_name = "${"$"}{var.gcp_project_id}_api"
}

resource "google_api_gateway_api_config" "config" {
  provider = google-beta
  api          = google_api_gateway_api.api.api_id
  project = var.gcp_project_id

  api_config_id = "v1"
  openapi_documents {
    document {
      path     = "openapi.json"
      contents = base64encode(local.openapi_description)
    }
  }
  lifecycle {
    create_before_destroy = true
  }
}

resource "google_api_gateway_gateway" "main" {
  provider     = google-beta
  region       = var.gcp_region
  project = var.gcp_project_id

  api_config   = google_api_gateway_api_config.config.id
  gateway_id   = "${"$"}{var.project_name}-${"$"}{var.environment}-gateway"

  depends_on   = [google_api_gateway_api_config.config]
}
