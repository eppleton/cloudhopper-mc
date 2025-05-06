<#list lambdaMap?keys as key>
output "${lambdaMap[key]}_url" {
  value = "https://${"$"}{azurerm_linux_function_app.shared_function_app.default_hostname}/api/${lambdaMap[key]}"
}
</#list>
