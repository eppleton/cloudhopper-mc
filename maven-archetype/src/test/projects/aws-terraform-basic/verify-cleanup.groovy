println ">> Running Terraform destroy for cleanup"
def dir = new File(basedir, "aws-demo")
def process = ["mvn", "package", "-Pterraform", "-Dterraform.action=destroy"].execute(null, dir)
process.in.eachLine { println it }
process.waitFor()
