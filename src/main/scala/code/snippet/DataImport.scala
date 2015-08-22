package code
package snippet

import code.model.{Cluster, Job, Project, Template, User}
import com.recipegrace.biglegos.data.{BigBricksImport, ScriptDBImport}
import com.recipegrace.biglegos.data.ScriptDBImport.ScriptDBJob
import net.liftweb.json.Serialization._
import net.liftweb.http._
import net.liftweb.common.{Box,Empty,Full}
import net.liftweb.util.Helpers._
import net.liftweb.mapper.By
import net.liftweb.json._
/**
 * A snippet that binds behavior, functions,
 * to HTML elements
 */
object DataImport {
  implicit val formats = DefaultFormats
  case class Argument(name:String, value:String, valType:String)

  def createArguments(job: ScriptDBJob):String = {
   val arguments:List[Argument]= (job.vmArguments.map(f=> Argument(f._1, f._2,"vm")) ++
      job.programArguments.map(f=> Argument(f._1, f._2,"pm")) ).toList

    write(arguments)
  }

  def createJob(job: ScriptDBJob)= {

    val templates =Template.findAll(By(Template.templateName, job.templateName))
    val projects =Project.findAll(By(Project.projectName, job.projectName))

      Job
        .create
        .project(projects.head)
        .template(templates.head)
        .mainClassName(job.mainClassName)
        .arguments(createArguments(job))
        .save
   

  }

  def getValidJobs(jobs: List[ScriptDBJob])=  {
 val oKJobs=
    jobs.filter(job=> {
      val templates =Template.findAll(By(Template.templateName, job.templateName))
      val projects =Project.findAll(By(Project.projectName, job.projectName))
      projects.size ==1 && templates.size==1
    })
    (jobs.size-oKJobs.size, oKJobs)
  }

  def render = {
    // define some variables to put our values into
    var fileHolder : Box[FileParamHolder] = Empty

    var submitType=""
    // process the form
    def process() {

        // otherwise give the user feedback and
        // redirect to the home page
val message=
   fileHolder.map(f=> f.file) match {

     case Full(x) => {
       val content = new String(x,"UTF-8")
         submitType match {
         case "project" => {
           val projects =ScriptDBImport.extractProjects(content)
           projects.foreach(f=>
             Project
               .create
               .projectName(f.projectName)
               .projectLocation(f.projectLocation)
               .gitBranch("")
               .jarName(f.jarName)
               .save
           )
           s"added ${projects.size} projects"
         }
         case "template" => {
           val templates =ScriptDBImport.extractTemplates(content)
           templates.foreach(f=>
             Template
               .create
               .templateName(f.templateName)
               .template(f.template)
               .save
           )
           s"added ${templates.size} templates"
         }
         case "job" => {
           val jobs =ScriptDBImport.extractJobs(content)
           val result= getValidJobs(jobs)
           result._2 .foreach(f=> createJob(f))
             s"added ${result._2.size} jobs, skipped ${result._1}"



         }
         case "bigbricks" => {

           BigBricksImport.importBigBricks(content)
           s"uploaded bigbricks data"
         }


         case _ =>{
           "Invalid type"
         }
       }
     }
     case _ => {
       "Looks like file is invalid"
     }
   }



     S.notice(message)

        S.redirectTo("/")

    }
    // associate each of the form elements
    // with a function... behavior to perform when the
    // for element is submitted
   ("name=file_upload" #>  SHtml.fileUpload(ul=>  fileHolder=Full(ul) ) )& // set the name
      "name=contentType" #> SHtml.onSubmit(submitType =_) &
      "type=submit" #> SHtml.onSubmitUnit(process)
  }
}