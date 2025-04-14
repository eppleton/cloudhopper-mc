"use strict";(self.webpackChunkdocs=self.webpackChunkdocs||[]).push([[44],{8453:(e,n,r)=>{r.d(n,{R:()=>o,x:()=>i});var a=r(6540);const t={},s=a.createContext(t);function o(e){const n=a.useContext(s);return a.useMemo((function(){return"function"==typeof e?e(n):{...n,...e}}),[n,e])}function i(e){let n;return n=e.disableParentContext?"function"==typeof e.components?e.components(t):e.components||t:o(e.components),a.createElement(s.Provider,{value:n},e.children)}},9533:(e,n,r)=>{r.r(n),r.d(n,{assets:()=>d,contentTitle:()=>i,default:()=>u,frontMatter:()=>o,metadata:()=>a,toc:()=>l});const a=JSON.parse('{"id":"modules/generator-azure-terraform","title":"\u2699\ufe0f Azure Terraform Generator","description":"This module provides a Cloudhopper generator that enables the deployment of annotated Java functions as Azure Functions using Terraform and Java handler code generation.","source":"@site/docs/modules/generator-azure-terraform.md","sourceDirName":"modules","slug":"/modules/generator-azure-terraform","permalink":"/cloudhopper-mc/docs/modules/generator-azure-terraform","draft":false,"unlisted":false,"tags":[],"version":"current","frontMatter":{},"sidebar":"documentationSidebar","previous":{"title":"provider-aws","permalink":"/cloudhopper-mc/docs/modules/provider-aws"},"next":{"title":"provider-azure","permalink":"/cloudhopper-mc/docs/modules/provider-azure"}}');var t=r(4848),s=r(8453);const o={},i="\u2699\ufe0f Azure Terraform Generator",d={},l=[{value:"\ud83d\udce6 How to Use",id:"-how-to-use",level:2},{value:"1. Add the dependency",id:"1-add-the-dependency",level:3},{value:"2. Configure annotation processing",id:"2-configure-annotation-processing",level:3},{value:"3. Add Azure Functions Maven Plugin",id:"3-add-azure-functions-maven-plugin",level:3},{value:"4. Optional: ZIP Packaging for Terraform Deployment",id:"4-optional-zip-packaging-for-terraform-deployment",level:3},{value:"\ud83d\udcc1 Templates",id:"-templates",level:2},{value:"\ud83d\udd0c Template Registration",id:"-template-registration",level:2},{value:"\ud83d\udcc2 Output",id:"-output",level:2},{value:"\ud83d\udcda Related Modules",id:"-related-modules",level:2}];function c(e){const n={code:"code",em:"em",h1:"h1",h2:"h2",h3:"h3",header:"header",hr:"hr",p:"p",pre:"pre",strong:"strong",table:"table",tbody:"tbody",td:"td",th:"th",thead:"thead",tr:"tr",...(0,s.R)(),...e.components};return(0,t.jsxs)(t.Fragment,{children:[(0,t.jsx)(n.header,{children:(0,t.jsx)(n.h1,{id:"\ufe0f-azure-terraform-generator",children:"\u2699\ufe0f Azure Terraform Generator"})}),"\n",(0,t.jsxs)(n.p,{children:["This module provides a Cloudhopper generator that enables the deployment of annotated Java functions as ",(0,t.jsx)(n.strong,{children:"Azure Functions"})," using ",(0,t.jsx)(n.strong,{children:"Terraform"})," and ",(0,t.jsx)(n.strong,{children:"Java handler code generation"}),"."]}),"\n",(0,t.jsxs)(n.p,{children:["Azure is unique in that Cloudhopper generates most of the glue code as Java classes. Only shared Terraform infrastructure (like the Function App and Storage Account) is generated as ",(0,t.jsx)(n.code,{children:".tf"})," files."]}),"\n",(0,t.jsx)(n.hr,{}),"\n",(0,t.jsx)(n.h2,{id:"-how-to-use",children:"\ud83d\udce6 How to Use"}),"\n",(0,t.jsx)(n.p,{children:"To use this generator in your own project, configure the following:"}),"\n",(0,t.jsx)(n.hr,{}),"\n",(0,t.jsx)(n.h3,{id:"1-add-the-dependency",children:"1. Add the dependency"}),"\n",(0,t.jsx)(n.pre,{children:(0,t.jsx)(n.code,{className:"language-xml",children:"<dependency>\n  <groupId>com.cloudhopper.mc</groupId>\n  <artifactId>generator-azure-terraform</artifactId>\n  <version>${cloudhopper.version}</version>\n</dependency>\n"})}),"\n",(0,t.jsx)(n.hr,{}),"\n",(0,t.jsx)(n.h3,{id:"2-configure-annotation-processing",children:"2. Configure annotation processing"}),"\n",(0,t.jsx)(n.pre,{children:(0,t.jsx)(n.code,{className:"language-xml",children:"<plugin>\n  <artifactId>maven-compiler-plugin</artifactId>\n  <version>3.8.1</version>\n  <configuration>\n    <source>11</source>\n    <target>11</target>\n    <encoding>UTF-8</encoding>\n    <annotationProcessors>\n      <annotationProcessor>\n        com.cloudhopper.mc.deployment.config.generator.ServerlessFunctionProcessor\n      </annotationProcessor>\n    </annotationProcessors>\n    <compilerArgs>\n      <arg>-Acloudprovider=azure</arg>\n      <arg>-AgeneratorId=azure-terraform-java21</arg>\n      <arg>-AconfigOutputDir=${project.build.directory}/deployment/azure</arg>\n      <arg>-AtargetDir=${project.build.directory}</arg>\n      <arg>-AartifactId=${project.artifactId}</arg>\n      <arg>-Aversion=${project.version}</arg>\n      <arg>-Aclassifier=azure</arg>\n    </compilerArgs>\n  </configuration>\n</plugin>\n"})}),"\n",(0,t.jsx)(n.hr,{}),"\n",(0,t.jsx)(n.h3,{id:"3-add-azure-functions-maven-plugin",children:"3. Add Azure Functions Maven Plugin"}),"\n",(0,t.jsxs)(n.p,{children:["This plugin is used to ",(0,t.jsx)(n.strong,{children:"package and deploy"})," the generated Azure function handler classes."]}),"\n",(0,t.jsx)(n.pre,{children:(0,t.jsx)(n.code,{className:"language-xml",children:"<plugin>\n  <groupId>com.microsoft.azure</groupId>\n  <artifactId>azure-functions-maven-plugin</artifactId>\n  <version>${azure.functions.maven.plugin.version}</version>\n  <configuration>\n    <appName>${functionAppName}</appName>\n    <resourceGroup>java-functions-group</resourceGroup>\n    <appServicePlanName>java-functions-app-service-plan</appServicePlanName>\n    <region>westus</region>\n    <runtime>\n      <os>linux</os>\n      <javaVersion>11</javaVersion>\n    </runtime>\n    <appSettings>\n      <property>\n        <name>FUNCTIONS_EXTENSION_VERSION</name>\n        <value>~4</value>\n      </property>\n    </appSettings>\n    <skipInstallExtensions>true</skipInstallExtensions>\n  </configuration>\n  <executions>\n    <execution>\n      <id>package-functions</id>\n      <goals>\n        <goal>package</goal>\n      </goals>\n      <phase>package</phase>\n    </execution>\n  </executions>\n</plugin>\n"})}),"\n",(0,t.jsx)(n.hr,{}),"\n",(0,t.jsx)(n.h3,{id:"4-optional-zip-packaging-for-terraform-deployment",children:"4. Optional: ZIP Packaging for Terraform Deployment"}),"\n",(0,t.jsx)(n.pre,{children:(0,t.jsx)(n.code,{className:"language-xml",children:"<plugin>\n  <groupId>org.apache.maven.plugins</groupId>\n  <artifactId>maven-assembly-plugin</artifactId>\n  <version>3.3.0</version>\n  <executions>\n    <execution>\n      <id>make-zip</id>\n      <phase>package</phase>\n      <goals>\n        <goal>single</goal>\n      </goals>\n      <configuration>\n        <descriptors>\n          <descriptor>src/assembly/azure-zip.xml</descriptor>\n        </descriptors>\n        <finalName>${project.artifactId}-${project.version}-${jar.classifier}</finalName>\n        <appendAssemblyId>false</appendAssemblyId>\n      </configuration>\n    </execution>\n  </executions>\n</plugin>\n"})}),"\n",(0,t.jsx)(n.hr,{}),"\n",(0,t.jsx)(n.h2,{id:"-templates",children:"\ud83d\udcc1 Templates"}),"\n",(0,t.jsx)(n.p,{children:"Templates are located under:"}),"\n",(0,t.jsx)(n.pre,{children:(0,t.jsx)(n.code,{children:"src/main/resources/templates/azure/\n"})}),"\n",(0,t.jsxs)(n.table,{children:[(0,t.jsx)(n.thead,{children:(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.th,{children:"Template"}),(0,t.jsx)(n.th,{children:"Output Type"}),(0,t.jsx)(n.th,{children:"Purpose"})]})}),(0,t.jsxs)(n.tbody,{children:[(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.td,{children:(0,t.jsx)(n.code,{children:"apiIntegrationClass.ftl"})}),(0,t.jsx)(n.td,{children:"Java"}),(0,t.jsx)(n.td,{children:"Entry class for API functions"})]}),(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.td,{children:(0,t.jsx)(n.code,{children:"function.ftl"})}),(0,t.jsx)(n.td,{children:"Java"}),(0,t.jsx)(n.td,{children:"HTTP function handler"})]}),(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.td,{children:(0,t.jsx)(n.code,{children:"schedule.ftl"})}),(0,t.jsx)(n.td,{children:"Java"}),(0,t.jsx)(n.td,{children:"Scheduled function handler"})]}),(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.td,{children:(0,t.jsx)(n.code,{children:"shared.ftl"})}),(0,t.jsx)(n.td,{children:"Terraform"}),(0,t.jsx)(n.td,{children:"Base infrastructure setup (Function App, etc.)"})]})]})]}),"\n",(0,t.jsx)(n.hr,{}),"\n",(0,t.jsx)(n.h2,{id:"-template-registration",children:"\ud83d\udd0c Template Registration"}),"\n",(0,t.jsx)(n.pre,{children:(0,t.jsx)(n.code,{className:"language-java",children:'@TemplateRegistration(\n  generatorId = "azure-terraform-java21",\n  templates = {\n    @Template(name = "function", phase = GenerationPhase.SOURCES),\n    @Template(name = "schedule", phase = GenerationPhase.SOURCES),\n    @Template(name = "apiIntegrationClass", phase = GenerationPhase.SOURCES),\n    @Template(name = "shared", phase = GenerationPhase.DEPLOYMENT)\n  }\n)\npublic class AzureTerraformJava21TemplateRegistration { ... }\n'})}),"\n",(0,t.jsx)(n.hr,{}),"\n",(0,t.jsx)(n.h2,{id:"-output",children:"\ud83d\udcc2 Output"}),"\n",(0,t.jsxs)(n.table,{children:[(0,t.jsx)(n.thead,{children:(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.th,{children:"Artifact"}),(0,t.jsx)(n.th,{children:"Location"})]})}),(0,t.jsxs)(n.tbody,{children:[(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.td,{children:"Java handler classes"}),(0,t.jsx)(n.td,{children:(0,t.jsx)(n.code,{children:"target/generated-sources/azure/"})})]}),(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.td,{children:"Terraform infrastructure"}),(0,t.jsx)(n.td,{children:(0,t.jsx)(n.code,{children:"target/deployment/azure/shared.tf"})})]}),(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.td,{children:"Deployment metadata"}),(0,t.jsx)(n.td,{children:(0,t.jsx)(n.code,{children:"target/classes/META-INF/cloudhopper/"})})]}),(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.td,{children:"Deployment ZIP (optional)"}),(0,t.jsx)(n.td,{children:(0,t.jsx)(n.code,{children:"target/${artifactId}-${version}-azure.zip"})})]})]})]}),"\n",(0,t.jsx)(n.hr,{}),"\n",(0,t.jsx)(n.h2,{id:"-related-modules",children:"\ud83d\udcda Related Modules"}),"\n",(0,t.jsxs)(n.table,{children:[(0,t.jsx)(n.thead,{children:(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.th,{children:"Module"}),(0,t.jsx)(n.th,{children:"Description"})]})}),(0,t.jsxs)(n.tbody,{children:[(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.td,{children:(0,t.jsx)(n.code,{children:"provider-azure"})}),(0,t.jsx)(n.td,{children:"Runtime adapter used in generated Java classes"})]}),(0,t.jsxs)(n.tr,{children:[(0,t.jsx)(n.td,{children:(0,t.jsx)(n.code,{children:"deployment-config-api"})}),(0,t.jsx)(n.td,{children:"Annotations and interfaces for vendor-neutral functions"})]})]})]}),"\n",(0,t.jsx)(n.hr,{}),"\n",(0,t.jsxs)(n.p,{children:["\ud83d\udcdd ",(0,t.jsx)(n.em,{children:"This module is required if you're targeting Azure Functions with Cloudhopper. It provides the logic and templates to generate handlers, infrastructure, and deployable artifacts."})]})]})}function u(e={}){const{wrapper:n}={...(0,s.R)(),...e.components};return n?(0,t.jsx)(n,{...e,children:(0,t.jsx)(c,{...e})}):c(e)}}}]);