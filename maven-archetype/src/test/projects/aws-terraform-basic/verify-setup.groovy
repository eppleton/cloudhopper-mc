println ">> Running Terraform apply for testing"
def dir = new File(basedir, "aws-demo")
def process = ["mvn", "package", "-Pdeploy-with-terraform", "-Dterraform.action=apply"].execute(null, dir)
process.in.eachLine { println it }
int result = process.waitFor()
if (result != 0) {
  throw new RuntimeException("Terraform apply failed")
}
