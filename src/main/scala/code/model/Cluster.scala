package code.model

import net.liftweb.mapper._

/**
 * The singleton that has methods for accessing the database
 */
object Cluster extends Cluster with LongKeyedMetaMapper[Cluster] {

  override def fieldOrder = List(clusterName,userName,password)
  override def dbTableName = "clusters"

}
class Cluster extends LongKeyedMapper[Cluster]  with IdPK {
  def getSingleton = Cluster // what's the "meta" server
  object clusterName extends MappedString(this, 100)
  object userName extends MappedString(this, 25)
  object password extends MappedString(this, 25)


  }




