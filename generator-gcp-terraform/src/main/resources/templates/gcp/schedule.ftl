resource "google_cloud_scheduler_job" "${functionId}_schedule" {
  name     = "${functionId}-schedule"
  schedule = "${scheduleExpression}"
  time_zone = "${scheduleTimezone}"

  http_target {
    http_method = "POST"
    uri         = "https://${"$"}{var.gcp_region}-${"$"}{var.gcp_project_id}.cloudfunctions.net/${functionId}"

    oidc_token {
      service_account_email = var.cloudscheduler_service_account_email
    }
  }
}
