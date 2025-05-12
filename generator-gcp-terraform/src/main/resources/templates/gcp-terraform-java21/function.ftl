# Function-specific resources

resource "google_cloudfunctions2_function" "${handlerInfo.functionId}" {
  name        = "${handlerInfo.functionId}"
  location    = var.gcp_region
  description = "Cloud function for ${handlerInfo.functionId}"

  build_config {
    runtime     = "java21"
    entry_point = "${handlerWrapperFullyQualifiedName}"
    source {
      storage_source {
        bucket = google_storage_bucket.function_bucket.name
        object = google_storage_bucket_object.shared_function_archive.name
      }
    }
  }

  service_config {
    available_memory   = "${handlerInfo.memory}Mi"
    timeout_seconds    = ${handlerInfo.timeout}
    environment_variables = {
      FUNCTION_MEMORY_MB       = ${handlerInfo.memory}
      FUNCTION_TIMEOUT_SECONDS = ${handlerInfo.timeout}
    }
  }
}

# IAM entry for all users to invoke the function
resource "google_cloudfunctions2_function_iam_member" "${handlerInfo.functionId}_invoker" {
  project        = google_cloudfunctions2_function.${handlerInfo.functionId}.project
  location       = google_cloudfunctions2_function.scheduledfunction.location
  cloud_function = google_cloudfunctions2_function.${handlerInfo.functionId}.name

  role   = "roles/cloudfunctions.invoker"
  member = "allUsers"
}