# Shared resources for Azure Functions

resource "azurerm_resource_group" "function_rg" {
  name     = "${r"${var.project_name}-${var.environment}"}-rg"
  location = var.azure_region
}

resource "azurerm_storage_account" "function_storage" {
  name                     = "${r'${lower(replace(var.project_name, "-", ""))}${var.environment}'}"
  resource_group_name      = azurerm_resource_group.function_rg.name
  location                 = azurerm_resource_group.function_rg.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_service_plan" "function_plan" {
  name                = "${r"${var.project_name}-${var.environment}"}-plan"
  resource_group_name = azurerm_resource_group.function_rg.name
  location            = azurerm_resource_group.function_rg.location
  os_type             = "Linux"
  sku_name            = "Y1" # This is the consumption plan
}

resource "azurerm_storage_container" "function_container" {
  name                  = "function-container"
  storage_account_id    = azurerm_storage_account.function_storage.id
  container_access_type = "private"
}

# Shared JAR file for all functions
resource "azurerm_storage_blob" "function_code" {
  name                   = "${handlerInfo.artifactId}-${handlerInfo.version}-${handlerInfo.classifier}.zip"
  storage_account_name   = azurerm_storage_account.function_storage.name
  storage_container_name = azurerm_storage_container.function_container.name
  type                   = "Block"
  source                 = "${handlerInfo.targetDir}/${handlerInfo.artifactId}-${handlerInfo.version}-${handlerInfo.classifier}.zip"
}

resource "azurerm_linux_function_app" "shared_function_app" {
  name                = "my-shared-function-app"
  resource_group_name = azurerm_resource_group.function_rg.name
  location            = azurerm_resource_group.function_rg.location
  
  service_plan_id            = azurerm_service_plan.function_plan.id
  storage_account_name       = azurerm_storage_account.function_storage.name
  storage_account_access_key = azurerm_storage_account.function_storage.primary_access_key

  site_config {
    application_stack {
      java_version = "11"  # or "17" if you're using Java 17
    }
  }

  app_settings = {
    FUNCTIONS_WORKER_RUNTIME    = "java"
    AzureWebJobsDisableHomepage = "true"
    WEBSITE_RUN_FROM_PACKAGE    = azurerm_storage_blob.function_code.url
  }
}

output "function_storage_name" {
  value = azurerm_storage_account.function_storage.name
}

output "function_storage_primary_access_key" {
  value     = azurerm_storage_account.function_storage.primary_access_key
  sensitive = true
}

output "function_plan_id" {
  value = azurerm_service_plan.function_plan.id
}

output "shared_zip_url" {
  value = azurerm_storage_blob.function_code.url
}

