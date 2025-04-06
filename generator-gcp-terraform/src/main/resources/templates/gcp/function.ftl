# Function-specific resources

resource "google_cloudfunctions_function" "${handlerInfo.functionId}" {
  name        = "${handlerInfo.functionId}"
  description = "Cloud function for ${handlerInfo.functionId}"
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

# IAM entry for all users to invoke the function
resource "google_cloudfunctions_function_iam_member" "${handlerInfo.functionId}_invoker" {
  project        = google_cloudfunctions_function.${handlerInfo.functionId}.project
  region         = google_cloudfunctions_function.${handlerInfo.functionId}.region
  cloud_function = google_cloudfunctions_function.${handlerInfo.functionId}.name

  role   = "roles/cloudfunctions.invoker"
  member = "allUsers"
}