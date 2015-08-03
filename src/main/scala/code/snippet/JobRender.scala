package code 
package snippet

import code.model.Job

import scala.xml.{NodeSeq, Text, Group}
import net.liftweb.util._

import net.liftweb.common._
import java.util.Date
import code.lib._
import Helpers._
import _root_.net.liftweb._


import mapper.{Ascending, OrderBy}
import http._
import S._
import SHtml._
import util._

import _root_.java.util.Locale
import common._
import net.liftweb.mapper.{StartAt, MaxRows}

class JobRender extends PaginatorSnippet[Job] {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  private object selectedJob extends RequestVar[Box[Job]](Empty)
  // replace the contents of the element with id "time" with the date
  def time = "#time *" #> date.map(_.toString)



  override def count: Long = Job.count()

  override def page: Seq[Job] =  Job.findAll(StartAt(curPage*itemsPerPage), MaxRows(itemsPerPage))

  def jobs: NodeSeq = {

    // the header


    // get and display each of the jobs
    page.flatMap(u => <tr>

      <td>{u.project.get}</td>
      <td>{u.template.get}</td>
      <td>
        {link("/job/edit", () => selectedJob(Full(u)), Text("Edit"))}
      </td>
      <td>
        {link("/job/delete", () => selectedJob(Full(u)), Text("Delete"))}
      </td>
    </tr> )

  }
  // called when the form is submitted
  private def saveJob(job: Job) = job.validate match {
    // no validation errors, save the job, and go
    // back to the "list" page
    case Nil => job.save; redirectTo("/job/index.html")

    // oops... validation errors
    // display the errors and make sure our selected job is still the same
    case x => error(x); selectedJob(Full(job))
  }

  def add(xhtml: Group): NodeSeq =
    selectedJob.is.openOr(new Job).toForm(Empty, saveJob _) ++ <tr>
      <td>
        <a href="/job/index.html">Cancel</a>
      </td>
      <td>
        <input type="submit" value="Create"/>
      </td>
    </tr>

  /**
   * Edit a job
   */


  def edit(xhtml: Group): NodeSeq =
    selectedJob.map(_.
      // get the form data for the job and when the form
      // is submitted, call the passed function.
      // That means, when the job submits the form,
      // the fields that were typed into will be populated into
      // "job" and "saveJob" will be called.  The
      // form fields are bound to the model's fields by this
      // call.
      toForm(Empty, saveJob _) ++ <tr>
      <td>
        <a href="/job/index.html">Cancel</a>
      </td>
      <td>
        <input type="submit" value="Save"/>
      </td>
    </tr>

      // bail out if the ID is not supplied or the job's not found
    ) openOr {
      error("Job not found"); redirectTo("/job/index.html")
    }

}

