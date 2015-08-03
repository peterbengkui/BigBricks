package code.model

import net.liftweb.mapper._
/**
 * The singleton that has methods for accessing the database
 */
object Project extends Project with LongKeyedMetaMapper[Project] {

  override def fieldOrder = List(projectName, projectLocation,jarName,gitBranch)
  override def dbTableName = "projects"

}
/**
 * An O-R mapped "User" class that includes first name, last name, password and we add a "Personal Essay" to it
 */
class Project extends LongKeyedMapper[Project]  with IdPK {
  def getSingleton = Project // what's the "meta" server
  object projectName extends MappedString(this, 25)
  object projectLocation extends MappedString(this, 50)
  object jarName extends MappedString(this, 50)
  object gitBranch extends MappedString(this,50)


}



