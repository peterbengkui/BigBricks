package code
package model

import net.liftweb.mapper._
/**
 * The singleton that has methods for accessing the database
 */
object Template extends Template with LongKeyedMetaMapper[Template] {

  override def fieldOrder = List(templateName,template)
  override def dbTableName = "templates"

}
/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class Template extends LongKeyedMapper[Template]  with IdPK {
  def getSingleton = Template // what's the "meta" server
  object templateName extends MappedString(this, 25)
  // define an additional field for a personal essay
  object template extends MappedTextarea(this, 2048) {
    override def textareaRows  = 20
    override def textareaCols = 100
    override def displayName = "Template"
  }


}



