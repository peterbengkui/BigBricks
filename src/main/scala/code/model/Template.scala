package code
package model

import code.lib.BootstrapCodeGenerator._
import code.model.Job._
import code.model.Project._
import net.liftweb.common.{Empty, Full}
import net.liftweb.mapper._

import scala.xml.{Null, Text, Attribute}

/**
 * The singleton that has methods for accessing the database
 */
object Template extends Template with LongKeyedMetaMapper[Template] {

  override def fieldOrder = List(templateName,template)
  override def dbTableName = "templates"
  formatFormElement = bsformFormElement
}
/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class Template extends LongKeyedMapper[Template]  with IdPK {
  def getSingleton = Template // what's the "meta" server
  object templateName extends MappedString(this, 25){

    override def displayName = "Template name"
    override def toForm = addClassAttribute(super.toForm)

  }
  // define an additional field for a personal essay
  object template extends MappedTextarea(this, 2048) {
    override def textareaRows  = 20
    override def textareaCols = 100
    override def displayName = "Template"
    override def toForm = addClassAttribute(super.toForm)

  }


}



