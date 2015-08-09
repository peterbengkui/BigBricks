package code
package model


import code.lib.BootstrapCodeGenerator._
import net.liftweb.mapper._
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
  object project extends MappedLongForeignKey(this, Project)
  object mainClassName extends   MappedString(this, 100)
  object template extends MappedLongForeignKey(this, Template)


}



