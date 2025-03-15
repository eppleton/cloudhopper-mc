locals {
  apidescription = templatefile("openapi.json", {
<#-- 'lambdaMap' is what we'll pass in from Java. It will map, e.g. "AddDeviceActionFunction_Arn" -> "add_device_action_function" -->
<#list lambdaMap?keys as key>
  # DEBUG: ${key} -> ${lambdaMap[key]}
</#list>
<#list lambdaMap?keys as key>
    ${key} = aws_lambda_function.${lambdaMap[key]}.arn<#if key_has_next>,</#if>
</#list>
  })
}
