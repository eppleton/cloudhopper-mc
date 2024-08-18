resource "google_storage_bucket" "${functionId}_bucket" {
  name     = "${functionId}-bucket"
  location = "US"  # Adjust as needed
}

resource "google_storage_bucket_object" "${functionId}_archive" {
  name   = "${artifactId}-${version}-${classifier}.jar"
  bucket = google_storage_bucket.${functionId}_bucket.name
  source = "${targetDir}/${artifactId}-${version}-${classifier}.jar"
}

resource "google_cloudfunctions_function" "${functionId}" {
  name        = "${functionId}"
  description = "Cloud function for ${functionId}"
  runtime     = "java21"

  available_memory_mb   = 256
  source_archive_bucket = google_storage_bucket.${functionId}_bucket.name
  source_archive_object = google_storage_bucket_object.${functionId}_archive.name
  trigger_http          = true
  entry_point           = "${handlerWrapperFullyQualifiedName}"
  
  environment_variables = {
    # Add any environment variables your function needs
  }
}

# IAM entry for all users to invoke the function
resource "google_cloudfunctions_function_iam_member" "${functionId}_invoker" {
  project        = google_cloudfunctions_function.${functionId}.project
  region         = google_cloudfunctions_function.${functionId}.region
  cloud_function = google_cloudfunctions_function.${functionId}.name

  role   = "roles/cloudfunctions.invoker"
  member = "allUsers"
}