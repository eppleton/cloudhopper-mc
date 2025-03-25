"${path}": {
  "${httpMethod?lower_case}": {
    "operationId": "${operationId}",
    "responses": {
      "default": {
        "description": "default"
      }
    },
    "x-google-backend": {
      "address": "https://${"$"}{region}-${"$"}{project}.cloudfunctions.net/${functionId}",
      "protocol": "h2"
    }
  }
}
