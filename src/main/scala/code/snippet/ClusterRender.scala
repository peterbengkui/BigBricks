package code.snippet

import java.util.Date

import code.lib._
import code.model.Cluster
import net.liftweb.common._
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http._
import net.liftweb.mapper.{MaxRows, StartAt}
import net.liftweb.util.Helpers._

import scala.xml.{Group, NodeSeq, Text}

class ClusterRender extends PaginatorSnippet[Cluster] {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  private object selectedCluster extends RequestVar[Box[Cluster]](Empty)

  override def count: Long = Cluster.count()

  override def page: Seq[Cluster] =  Cluster.findAll(StartAt(curPage*itemsPerPage), MaxRows(itemsPerPage))

  def clusters: NodeSeq = {




    // get and display each of the clusters
    page.flatMap(u => <tr>

      <td>{u.clusterName.get}</td>
      <td>
        {link("/components/cluster/edit", () => selectedCluster(Full(u)), Text("Edit"))}
      </td>
      <td>
        {link("/components/cluster/delete", () => selectedCluster(Full(u)), Text("Delete"))}
      </td>
    </tr> )

  }
  /**
   * Confirm deleting a user
   */
  def confirmDelete = {
    (for (cluster <- selectedCluster.is) // find the cluster
      yield {
        def deleteCluster() {
          notice("Cluster " + (cluster.clusterName ) + " deleted")
          cluster.delete_!
          redirectTo("/components/cluster/index.html")
        }

        // bind the incoming XHTML to a "delete" button.
        // when the delete button is pressed, call the "deleteCluster"
        // function (which is a closure and bound the "cluster" object
        // in the current content)
        ".clustername" #> (cluster.clusterName.get ) &
          ".delete" #> submit("Delete", deleteCluster _)

        // if the was no ID or the cluster couldn't be found,
        // display an error and redirect
      }) openOr {
      error("Cluster not found"); redirectTo("/components/cluster/index.html")
    }
  }
  // called when the form is submitted
  private def saveCluster(cluster: Cluster) = cluster.validate match {
    // no validation errors, save the cluster, and go
    // back to the "list" page
    case Nil => cluster.save; redirectTo("/components/cluster/index.html")

    // oops... validation errors
    // display the errors and make sure our selected cluster is still the same
    case x => error(x); selectedCluster(Full(cluster))
  }

  def add(xhtml: Group): NodeSeq =
    selectedCluster.is.openOr(new Cluster).toForm(Empty, saveCluster _) ++ <tr>
      <td>
        <a href="/components/cluster/index.html">Cancel</a>
      </td>
      <td>
        <input type="submit" value="Create"/>
      </td>
    </tr>

  /**
   * Edit a cluster
   */


  def edit(xhtml: Group): NodeSeq =
    selectedCluster.map(_.
      // get the form data for the cluster and when the form
      // is submitted, call the passed function.
      // That means, when the cluster submits the form,
      // the fields that were typed into will be populated into
      // "cluster" and "saveCluster" will be called.  The
      // form fields are bound to the model's fields by this
      // call.
      toForm(Empty, saveCluster _) ++ <tr>
      <td>
        <a href="/components/cluster/index.html">Cancel</a>
      </td>
      <td>
        <input type="submit" value="Save"/>
      </td>
    </tr>

      // bail out if the ID is not supplied or the cluster's not found
    ) openOr {
      error("Cluster not found"); redirectTo("/components/cluster/index.html")
    }

}

