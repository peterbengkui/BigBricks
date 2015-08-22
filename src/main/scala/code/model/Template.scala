package code
package model

import code.lib.BootstrapCodeGenerator._
import net.liftweb.mapper._

/**
 * The singleton that has methods for accessing the database
 */
object Template extends Template with LongKeyedMetaMapper[Template] {

  override def fieldOrder = List(templateName,template)
  override def dbTableName = "templates"
  formatFormElement = bsformFormElement
}

class Template extends LongKeyedMapper[Template]  with IdPK {
  def getSingleton = Template // what's the "meta" server
  object templateName extends MappedString(this, 50){

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



