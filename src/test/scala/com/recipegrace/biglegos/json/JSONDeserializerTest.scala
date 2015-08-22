package com.recipegrace.biglegos.json

import code.model.{Project, Job}
import net.liftweb.json.DefaultFormats
import net.liftweb.json._
import org.specs2.mutable.Specification
import net.liftweb.json.JsonDSL._
import net.liftweb.json.Extraction._
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
