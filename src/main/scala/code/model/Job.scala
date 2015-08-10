package code
package model


import code.lib.BootstrapCodeGenerator._
import code.snippet.DataImport.Argument
import net.liftweb.json.DefaultFormats
import net.liftweb.mapper._
import net.liftweb.util.FieldError
import scala.xml.Text
import net.liftweb.common.{Box, Full}
/**
 * The singleton that has methods for accessing the database
 */
object Job extends Job with LongKeyedMetaMapper[Job] {

  override def fieldOrder = List(project,mainClassName,template)
  override def dbTableName = "jobs"
  formatFormElement = bsformFormElement


}
/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class Job extends LongKeyedMapper[Job]  with IdPK {
  def getSingleton = Job // what's the "meta" server
  object project extends MappedLongForeignKey(this, Project){
    override def displayName = "Project"
    override def asHtml = {
      <span>{Project.find(By(Project.id, this.get)).map(u => (u.projectName.get )).openOr(Text("PM"))}</span>
    }
    override def validSelectValues: Box[List[(Long, String)]] =
      Full(Project.findAll(OrderBy(Project.projectName, Ascending)).map(c =>
        (c.id.get, c.projectName.get)))

    override def toForm = addClassAttribute(super.toForm)
        }
  object mainClassName extends   MappedString(this, 100) {
    override def toForm = addClassAttribute(super.toForm)
    override def displayName = "Main class name"
  }
  object template extends MappedLongForeignKey(this, Template) {
    override def displayName = "Template"
    override def asHtml = {
      <span>{Template.find(By(Template.id, this.get)).map(u => (u.templateName.get )).openOr(Text("PM"))}</span>
    }
    override def validSelectValues: Box[List[(Long, String)]] =
      Full(Template.findAll(OrderBy(Template.templateName, Ascending)).map(c =>
        (c.id.get, c.templateName.get)))
    override def toForm = addClassAttribute(super.toForm)
  }

  object arguments extends MappedTextarea(this, 2048) {
    override def textareaRows  = 10
    override def textareaCols = 100
    override def displayName = "Arguments"
    implicit val formats = DefaultFormats
    import net.liftweb.json._

    override def toForm = addClassAttribute(super.toForm)
    override def validations =  validJSON _ :: super.validations


    def validJSON(argText : String) = {

      try {
        val arguments=

        for (project <- parse(argText).asInstanceOf[JArray].children)
          yield project.extract[Argument]

        println(arguments)
        List[FieldError]()
      } catch {
        case _:Throwable => List(FieldError(this, "Invalid json"))
      }




    }

  }
}



