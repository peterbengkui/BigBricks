package code.snippet

import java.util.Date

import code.lib._
import code.model.Project
import net.liftweb.common._
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http._
import net.liftweb.mapper.{MaxRows, StartAt}
import net.liftweb.util.Helpers._

import scala.xml.{Group, NodeSeq}

class ProjectRender extends PaginatorSnippet[Project] {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  private object selectedProject extends RequestVar[Box[Project]](Empty)

  override def count: Long = Project.count()

  override def page: Seq[Project] =  Project.findAll(StartAt(curPage*itemsPerPage), MaxRows(itemsPerPage))

  def projects: NodeSeq = {




    // get and display each of the projects
    page.flatMap(u => <tr>

      <td>{u.projectName.get}</td>
      <td>
        {link("/components/project/edit", () => selectedProject(Full(u)),  <span class="glyphicon glyphicon-edit"></span> )}
        {link("/components/project/delete", () => selectedProject(Full(u)), <span class="glyphicon glyphicon-remove"></span> )}
      </td>
    </tr> )

  }
  /**
   * Confirm deleting a user
   */
  def confirmDelete = {
    (for (project <- selectedProject.is) // find the project
      yield {
        def deleteProject() {
          notice("Project " + (project.projectName ) + " deleted")
          project.delete_!
          redirectTo("/components/project/index.html")
        }

        // bind the incoming XHTML to a "delete" button.
        // when the delete button is pressed, call the "deleteProject"
        // function (which is a closure and bound the "project" object
        // in the current content)
        ".project" #> (project.projectName.get ) &
          ".delete" #> submit("Delete", deleteProject _, "class"-> "btn btn-primary")

        // if the was no ID or the project couldn't be found,
        // display an error and redirect
      }) openOr {
      error("Project not found"); redirectTo("/components/project/index.html")
    }
  }
  // called when the form is submitted
  private def saveProject(project: Project) = project.validate match {
    // no validation errors, save the project, and go
    // back to the "list" page
    case Nil => project.save; redirectTo("/components/project/index.html")

    // oops... validation errors
    // display the errors and make sure our selected project is still the same
    case x => error(x); selectedProject(Full(project))
  }

  def add(xhtml: Group): NodeSeq =
    selectedProject.is.openOr(new Project).toForm(Empty, saveProject _) ++ <div class="span3">
      <button type="submit" class="btn btn-primary">
        <span class="glyphicon glyphicon-new" aria-hidden="true"></span> Create
      </button>
      <a href='/components/project/index.html' class="btn btn-default btn-sm">
        Cancel
      </a>
    </div>

  /**
   * Edit a project
   */


  def edit(xhtml: Group): NodeSeq =
    selectedProject.map(_.
      // get the form data for the project and when the form
      // is submitted, call the passed function.
      // That means, when the project submits the form,
      // the fields that were typed into will be populated into
      // "project" and "saveProject" will be called.  The
      // form fields are bound to the model's fields by this
      // call.
      toForm(Empty, saveProject _) ++ <div class="span3">
      <button type="submit" class="btn btn-primary">
        <span class="glyphicon glyphicon-save" aria-hidden="true"></span> Save
      </button>
      <a href='/components/project/index.html' class="btn btn-default btn-sm">
        Cancel
      </a>
    </div>


      // bail out if the ID is not supplied or the project's not found
    ) openOr {
      error("Project not found"); redirectTo("/components/project/index.html")
    }

}

