package code
package snippet

import com.recipegrace.biglegos.data.ScriptDBImport
import net.liftweb._
import net.liftweb.http._
import net.liftweb.common.{Box,Empty,Full}
import net.liftweb.util.Helpers._
import scala.xml.NodeSeq
/**
 * A snippet that binds behavior, functions,
 * to HTML elements
 */
object DataImport {
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
         case "Project" => {
           val projects =ScriptDBImport.extractProjects(content)
           s"Ready to upload ${projects.size} projects"
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
      "type=hidden" #> SHtml.onSubmit(submitType =_) &
      "type=submit" #> SHtml.onSubmitUnit(process)
  }
}