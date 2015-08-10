package code.snippet

import java.util.Date

import code.lib._
import code.model.Job
import net.liftweb.common._
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http._
import net.liftweb.mapper.{MaxRows, StartAt}
import net.liftweb.util.Helpers._

import scala.xml.{Group, NodeSeq, Text}

class JobRender extends PaginatorSnippet[Job] {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  private object selectedJob extends RequestVar[Box[Job]](Empty)

  override def count: Long = Job.count()

  override def page: Seq[Job] =  Job.findAll(StartAt(curPage*itemsPerPage), MaxRows(itemsPerPage))

  def jobs: NodeSeq = {




    // get and display each of the jobs
    page.flatMap(u => <tr>

      <td>{u.mainClassName.get}</td>
      <td>
        {link("/components/job/edit", () => selectedJob(Full(u)),  <span class="glyphicon glyphicon-edit"></span> )}
        {link("/components/job/delete", () => selectedJob(Full(u)), <span class="glyphicon glyphicon-remove"></span> )}
      </td>
    </tr> )

  }
  /**
   * Confirm deleting a user
   */
  def confirmDelete = {
    (for (job <- selectedJob.is) // find the job
      yield {
        def deleteJob() {
          notice("Job " + (job.mainClassName) + " deleted")
          job.delete_!
          redirectTo("/components/job/index.html")
        }

        // bind the incoming XHTML to a "delete" button.
        // when the delete button is pressed, call the "deleteJob"
        // function (which is a closure and bound the "job" object
        // in the current content)
        ".job" #> (job.mainClassName.get ) &
          ".delete" #> submit("Delete", deleteJob _, "class"-> "btn btn-primary")

        // if the was no ID or the job couldn't be found,
        // display an error and redirect
      }) openOr {
      error("Job not found"); redirectTo("/components/job/index.html")
    }
  }
  // called when the form is submitted
  private def saveJob(job: Job) = job.validate match {
    // no validation errors, save the job, and go
    // back to the "list" page
    case Nil => job.save; redirectTo("/components/job/index.html")

    // oops... validation errors
    // display the errors and make sure our selected job is still the same
    case x => error(x); selectedJob(Full(job))
  }

  def add(xhtml: Group): NodeSeq =
    selectedJob.is.openOr(new Job).toForm(Empty, saveJob _) ++ <div class="span3">
      <button type="submit" class="btn btn-primary">
        <span class="glyphicon glyphicon-new" aria-hidden="true"></span> Create
      </button>
      <a href='/components/job/index.html' class="btn btn-default btn-sm">
        Cancel
      </a>
    </div>

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
      toForm(Empty, saveJob _) ++ <div class="span3">
      <button type="submit" class="btn btn-primary">
        <span class="glyphicon glyphicon-save" aria-hidden="true"></span> Save
      </button>
      <a href='/components/job/index.html' class="btn btn-default btn-sm">
        Cancel
      </a>
    </div>


      // bail out if the ID is not supplied or the job's not found
    ) openOr {
      error("Job not found"); redirectTo("/components/job/index.html")
    }

}

