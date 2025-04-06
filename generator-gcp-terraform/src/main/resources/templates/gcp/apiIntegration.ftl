"${path}": {
  "${httpMethod?lower_case}": {
    "operationId": "${operationId}",
    "responses": {
      "default": {
        "description": "default"
      }
    },
    "x-google-backend": {
      "address": "https://${"$"}{var.region}-${"$"}{var.project}.cloudfunctions.net/${handlerInfo.functionId}",
      "protocol": "h2"
    }
  }
}
