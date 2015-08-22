package code.snippet

import com.recipegrace.biglegos.data.BigBricksExport
import net.liftweb.http._
import net.liftweb.util.Helpers._
/**
 * Created by fjacob on 8/16/15.
 */
object DataExport {


  def content = {

      BigBricksExport.exportBigBricks()


  }


  def render =

    "type=submit" #> SHtml.onSubmitUnit( () => throw new ResponseShortcutException(poemTextFile))


  def poemTextFile : LiftResponse = {
    val name:String = s"bigbricks${System.currentTimeMillis().toString}.xml"

    InMemoryResponse(
      content.getBytes("UTF-8"),
      "Content-Type" -> "text/plain; charset=utf8" ::
        "Content-Disposition" -> ("attachment; filename=\""+name+"\"") :: Nil,
      cookies = Nil, 200)
  }
}
