package code.lib

import net.liftweb.common.{Box, Empty, Full}

import scala.xml.{Attribute, Elem, NodeSeq, Null, Text}


/**
 * Created by fjacob on 8/9/15.
 */
object BootstrapCodeGenerator {
  val  bsformFormElement:(NodeSeq, NodeSeq) => NodeSeq =
    (name, form) =>
      <div class="form-group">
        <label>{name}</label>

        {form}
      </div>

  def addClassAttribute(input:Any) = {
    input match {
      case Full(x:Elem) => {
        Full({x  % Attribute(None, "class", Text("form-control"), Null)})
      }
      case Full(x:NodeSeq)=>{
      Full(<span>  {x.map(f=> f.asInstanceOf[Elem]  % Attribute(None, "class", Text("form-control"), Null))}</span>)
      }
      case _=>{

        Empty
      }
    }
  }
  def addPasswordConfirm(input:Box[NodeSeq]) = {
    input match {

      case Full(node:NodeSeq) => {
        val textBoxes =  (node \ "input" ).map(f=> f.asInstanceOf[Elem]  % Attribute(None, "class", Text("form-control"), Null))
        Full(
          <span>
            <div class="form-group">
              { textBoxes(0)}
            </div>
            <div class="form-group">
              <label>Confirm</label>
              { textBoxes(1)}
            </div>
          </span>
        )
      }
      case _ => input
    }
  }
}
