package code.model

import code.lib.BootstrapCodeGenerator._
import net.liftweb.common.{Empty, Full}
import net.liftweb.mapper._

import scala.xml.{Null, Text, Attribute}

/**
 * The singleton that has methods for accessing the database
 */
object Project extends Project with LongKeyedMetaMapper[Project] {

  override def fieldOrder = List(projectName, projectLocation,jarName,gitBranch)
  override def dbTableName = "projects"
  formatFormElement = bsformFormElement
}
/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class Project extends LongKeyedMapper[Project]  with IdPK {
  def getSingleton = Project // what's the "meta" server
  object projectName extends MappedString(this, 50){

    override def displayName = "Project name"
    override def toForm = addClassAttribute(super.toForm)


  }
  object projectLocation extends MappedString(this, 100){

    override def displayName = "Project location"
    override def toForm = addClassAttribute(super.toForm)


  }
  object jarName extends MappedString(this, 100){

    override def displayName = "Jar name"
    override def toForm = addClassAttribute(super.toForm)


  }
  object gitBranch extends MappedString(this,50){

    override def displayName = "Git branch"
    override def toForm = addClassAttribute(super.toForm)


  }


}



