package com.recipegrace.biglegos.db

import java.sql.ResultSet

import code.model.{Project, Job}
import com.recipegrace.biglegos.data.BigBricksExport._
import com.recipegrace.biglegos.data.BigBricksImport._
import net.liftweb.db.{H2Driver, StandardDBVendor}
import net.liftweb.mapper.DB
import net.liftweb.util
import net.liftweb.util.Props
import org.specs2.mutable.Specification

/**
 * Created by fjacob on 8/21/15.
 */
import net.liftweb.json.Serialization._
class DBExportTest extends Specification {



  "Parser test" should {
    "jobs" in {


      val vendor =
        new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
          Props.get("db.url") openOr
            "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
          Props.get("db.user"), Props.get("db.password"))
      vendor.closeAllConnections_!()
      DB.defineConnectionManager(util.DefaultConnectionIdentifier, vendor)

      val content = exportBigBricks()
      importBigBricks(content)
      content.startsWith("{\"clusters\":[],") shouldEqual true

    }
  }


    }