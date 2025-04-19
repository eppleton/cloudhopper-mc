# Function for API Gateway HTTP trigger (path parameter extraction)
resource "google_cloudfunctions_function" "${handlerInfo.functionId}_api" {
  name        = "${handlerInfo.functionId}-api"
  description = "API Cloud function for ${handlerInfo.functionId}"
  runtime     = "java21"

  available_memory_mb   = ${handlerInfo.memory}
  timeout               = ${handlerInfo.timeout}
  source_archive_bucket = google_storage_bucket.function_bucket.name
  source_archive_object = google_storage_bucket_object.shared_function_archive.name
  trigger_http          = true
  entry_point           = "${handlerWrapperFullyQualifiedName}"

  environment_variables = {
    # Add any environment variables your function needs
  }
}

# IAM permission for the API function
resource "google_cloudfunctions_function_iam_member" "${handlerInfo.functionId}_api_invoker" {
  project        = google_cloudfunctions_function.${handlerInfo.functionId}_api.project
  region         = google_cloudfunctions_function.${handlerInfo.functionId}_api.region
  cloud_function = google_cloudfunctions_function.${handlerInfo.functionId}_api.name

  role   = "roles/cloudfunctions.invoker"
  member = "allUsers"
}
