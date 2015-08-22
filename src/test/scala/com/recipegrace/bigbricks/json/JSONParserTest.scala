package com.recipegrace.bigbricks.json

import com.recipegrace.bigbricks.data.ScriptDBImport._
import org.specs2.mutable.Specification
import net.liftweb.json._

/**
 * Created by fjacob on 8/2/15.
 */
object JSONParserTest  extends Specification{



  "Parser test" should {
    "jobs" in {

      val jobsText="[\n  {\n    \"projectName\": \"Edge\",\n    \"mainClassName\": \"com.careerbuilder.edge.common.MergeFiles\",\n    \"vmArguments\": {\n      \n    },\n    \"programArguments\": {\n      \"output\": \"exampleout\",\n      \"input\": \"countout\"\n    },\n    \"templateName\": \"GIT\\rPULL\\rAND\\rSCALDING\\rJOB\\rSUBMIT\"\n  },\n  {\n    \"projectName\": \"Edge\",\n    \"mainClassName\": \"com.careerbuilder.edge.matching.common.HeuristicRuleDistribution\",\n    \"vmArguments\": {\n      \n    },\n    \"programArguments\": {\n      \"output\": \"tempout\",\n      \"input\": \"finalmatcherout\"\n    },\n    \"templateName\": \"EDGE\\rMATCHER\\rPROJECT\"\n  },\n  {\n    \"projectName\": \"EdgeMatcher\",\n    \"mainClassName\": \"com.careerbuilder.edge.    matching.common.HeuristicRuleDistribution\",\n    \"vmArguments\": {\n      \n    },\n    \"programArguments\": {\n      \"output\": \"tempout\",\n      \"input\": \"finalmatcherout\"\n    },\n    \"templateName\": \"EDGE\\rMATCHER\\rPROJECT\"\n  }]"
      val jobs: List[ScriptDBJob] = extractJobs(jobsText)

      jobs.size mustEqual(3)
      jobs(0).projectName mustEqual("Edge")

    }
    "projects" in {
      val projectText = "[\n  {\n    \"projectLocation\": \"\\/home\\/fjacob.site\\/Recruitment-Edge\\/scalding\\/Edge\",\n    \"projectName\": \"Edge\",\n    \"jarName\": \"target\\/scala-2.11\\/EdgeHadoop-job.jar\"\n  },\n  {\n    \"projectLocation\": \"\\/home\\/fjacob.site\\/Recruitment-Edge\\/scala\",\n    \"projectName\": \"EdgeMatcher\",\n    \"jarName\": \"scalding\\/matcher\\/target\\/scala-2.11\\/EdgeHadoop-job.jar\"\n  }]"
      val projects = extractProjects(projectText)

      projects.size mustEqual(2)
      projects(0).projectName mustEqual("Edge")
    }
    "templates" in {
      val templateText ="[\n  {\n    \"templateName\": \"GIT PULL AND SCALDING JOB SUBMIT\",\n    \"template\": \"template1(mainclass,arguments,jarname,options, projectname,projectlocation) ::=<<\\n #!\\/bin\\/          bash\\nJAR_NAME=<projectlocation>\\/<jarname>\\nRUNNER=com.careerbuilder.edge.main.JobRunner\\nMAIN_CLASS=<mainclass>\\ncd                   <projectlocation>\\n\\/usr\\/bin\\/git pull origin matcher-validation\\n\\/home\\/fjacob.site\\/sbt\\/bin\\/sbt clean assembly\\nsudo -u dataservices kinit -k -t \\/home\\/dataservices\\/krb5.   keytab svcDataServicesJobs@ATL.CAREERBUILDER.COM\\nchmod a+r <jarname>\\nsudo -u dataservices sh -c \\\"hadoop jar $JAR_NAME  $RUNNER $MAIN_CLASS --hdfs <arguments:{u|--<u.key> <u.value> }>  2> \\/tmp\\/error >\\/tmp\\/out &\\\"\\n>>\"\n  },\n  {\n    \"templateName\": \"EMSIPROJECT\",\n    \"template\": \"template1(mainclass,arguments,  jarname,options, projectname,projectlocation) ::=<<\\n #!\\/bin\\/bash\\nJAR_NAME=<projectlocation>\\/<jarname>\\nRUNNER=com.careerbuilder.edge.scaldingcore.JobRunner\\nMAIN_CLASS=<mainclass>\\ncd <projectlocation>\\n\\/usr\\/bin\\/git checkout master\\n\\/usr\\/bin\\/git pull\\n\\/usr\\/bin\\/git pull origin ml-based-  matcher\\n\\/home\\/fjacob.site\\/sbt\\/bin\\/sbt 'project emsi' clean assembly\\nsudo -u dataservices kinit -k -t \\/home\\/dataservices\\/krb5.keytab svcDataServicesJobs@ATL.            CAREERBUILDER.COM\\nchmod a+r $JAR_NAME\\nARGUMENTS=\\\"<arguments:{u| --<u.key> <u.value> }>\\\"\\nsudo -u dataservices sh -c \\\"hadoop jar $JAR_NAME   $RUNNER $MAIN_CLASS --hdfs $ARGUMENTS  2> \\/tmp\\/error >\\/tmp\\/out &\\\"\\n>>\"\n  }]"
      val templates =extractTemplates(templateText)
      templates.size mustEqual(2)
      templates(0).templateName mustEqual("GIT PULL AND SCALDING JOB SUBMIT")
    }
  }


}
