<#list lambdaMap?keys as key>
output "${lambdaMap[key]}_url" {
  value = "https://${"$"}{google_api_gateway_gateway.main.default_hostname}/${lambdaMap[key]}"
}
</#list>

<#list lambdaMap?keys as key>
output "${lambdaMap[key]}_direct_url" {
  value = google_cloudfunctions_function.${lambdaMap[key]}.https_trigger_url
}
</#list>