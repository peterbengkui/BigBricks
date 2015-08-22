package com.recipegrace.bigbricks.json

import code.model.Project
import net.liftweb.json.Extraction._
import net.liftweb.json.{DefaultFormats, _}
import org.specs2.mutable.Specification
/**
 * Created by fjacob on 8/16/15.
 */
object JSONDeserializerTest extends Specification {
  implicit val formats = DefaultFormats
  "Parser test" should {
    "project" in {

      val project:Project = Project.create.projectName("projectName")
       render(decompose(project)) shouldEqual project.projectName.get
    }
    }

}
