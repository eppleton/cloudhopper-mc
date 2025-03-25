# Shared resources for GCP functions

# Shared storage bucket for all function JARs
resource "google_storage_bucket" "function_bucket" {
  name     = "${r"${var.project_name}-${var.environment}-functions"}"
  location = var.gcp_region
}

# Shared JAR file for all functions
resource "google_storage_bucket_object" "shared_function_archive" {
  name   = "${artifactId}-${version}-${classifier}.zip"
  bucket = google_storage_bucket.function_bucket.name
  source = "${targetDir}/${artifactId}-${version}-${classifier}.zip"
}
