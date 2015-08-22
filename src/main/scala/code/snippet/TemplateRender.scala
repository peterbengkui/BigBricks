package code.snippet

import java.util.Date

import code.lib._
import code.model.Template
import net.liftweb.common._
import net.liftweb.http.S._
import net.liftweb.http.SHtml._
import net.liftweb.http._
import net.liftweb.mapper.{MaxRows, StartAt}
import net.liftweb.util.Helpers._

import scala.xml.{Group, NodeSeq}

class TemplateRender extends PaginatorSnippet[Template] {
  lazy val date: Box[Date] = DependencyFactory.inject[Date] // inject the date

  private object selectedTemplate extends RequestVar[Box[Template]](Empty)

  override def count: Long = Template.count()

  override def page: Seq[Template] =  Template.findAll(StartAt(curPage*itemsPerPage), MaxRows(itemsPerPage))

  def templates: NodeSeq = {




    // get and display each of the templates
    page.flatMap(u => <tr>

      <td>{u.templateName.get}</td>
      <td>
        {link("/components/template/edit", () => selectedTemplate(Full(u)),  <span class="glyphicon glyphicon-edit"></span> )}
        {link("/components/template/delete", () => selectedTemplate(Full(u)), <span class="glyphicon glyphicon-remove"></span> )}
      </td>
    </tr> )

  }
  /**
   * Confirm deleting a user
   */
  def confirmDelete = {
    (for (template <- selectedTemplate.is) // find the template
      yield {
        def deleteTemplate() {
          notice("Template " + (template.templateName ) + " deleted")
          template.delete_!
          redirectTo("/components/template/index.html")
        }

        // bind the incoming XHTML to a "delete" button.
        // when the delete button is pressed, call the "deleteTemplate"
        // function (which is a closure and bound the "template" object
        // in the current content)
        ".template" #> (template.templateName.get ) &
          ".delete" #> submit("Delete", deleteTemplate _, "class"-> "btn btn-primary")

        // if the was no ID or the template couldn't be found,
        // display an error and redirect
      }) openOr {
      error("Template not found"); redirectTo("/components/template/index.html")
    }
  }
  // called when the form is submitted
  private def saveTemplate(template: Template) = template.validate match {
    // no validation errors, save the template, and go
    // back to the "list" page
    case Nil => template.save; redirectTo("/components/template/index.html")

    // oops... validation errors
    // display the errors and make sure our selected template is still the same
    case x => error(x); selectedTemplate(Full(template))
  }

  def add(xhtml: Group): NodeSeq =
    selectedTemplate.is.openOr(new Template).toForm(Empty, saveTemplate _) ++ <div class="span3">
      <button type="submit" class="btn btn-primary">
        <span class="glyphicon glyphicon-new" aria-hidden="true"></span> Create
      </button>
      <a href='/components/template/index.html' class="btn btn-default btn-sm">
        Cancel
      </a>
    </div>

  /**
   * Edit a template
   */


  def edit(xhtml: Group): NodeSeq =
    selectedTemplate.map(_.
      // get the form data for the template and when the form
      // is submitted, call the passed function.
      // That means, when the template submits the form,
      // the fields that were typed into will be populated into
      // "template" and "saveTemplate" will be called.  The
      // form fields are bound to the model's fields by this
      // call.
      toForm(Empty, saveTemplate _) ++ <div class="span3">
      <button type="submit" class="btn btn-primary">
        <span class="glyphicon glyphicon-save" aria-hidden="true"></span> Save
      </button>
      <a href='/components/template/index.html' class="btn btn-default btn-sm">
        Cancel
      </a>
    </div>


      // bail out if the ID is not supplied or the template's not found
    ) openOr {
      error("Template not found"); redirectTo("/components/template/index.html")
    }

}

