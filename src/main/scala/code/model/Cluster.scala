package code.model

import code.lib.BootstrapCodeGenerator._
import net.liftweb.mapper._

/**
 * The singleton that has methods for accessing the database
 */
object Cluster extends Cluster with LongKeyedMetaMapper[Cluster] {


  override def fieldOrder = List(clusterName,userName,password)
  override def dbTableName = "clusters"

  formatFormElement = bsformFormElement






}
class Cluster extends LongKeyedMapper[Cluster]  with IdPK {
  def getSingleton = Cluster // what's the "meta" server
  object clusterName extends MappedString(this, 100) {

    override def displayName = "Cluster name"
  override def toForm = addClassAttribute(super.toForm)


  }


  object userName extends MappedString(this, 25) {
    override def toForm = addClassAttribute(super.toForm)
    override def displayName = "User name"
  }
  object password extends MappedPassword(this) {
    override def toForm = addPasswordConfirm(super.toForm)

  }



  }




