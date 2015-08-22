package bootstrap.liftweb

import code.model._
import net.liftmodules.FoBo
import net.liftweb._
import net.liftweb.common._
import net.liftweb.http._
import net.liftweb.mapper._
import net.liftweb.sitemap.Loc._
import net.liftweb.sitemap._
import net.liftweb.util.Helpers._
import net.liftweb.util._

import scala.language.postfixOps

/**
 * A class that's instantiated early and run.  It allows the application
 * to modify lift's environment
 */
class Boot {
  def boot {
    if (!DB.jndiJdbcConnAvailable_?) {
      val vendor =
	new StandardDBVendor(Props.get("db.driver") openOr "org.h2.Driver",
			     Props.get("db.url") openOr
			     "jdbc:h2:lift_proto.db;AUTO_SERVER=TRUE",
			     Props.get("db.user"), Props.get("db.password"))

      LiftRules.unloadHooks.append(vendor.closeAllConnections_! _)

      DB.defineConnectionManager(util.DefaultConnectionIdentifier, vendor)

    }

    // Use Lift's Mapper ORM to populate the database
    // you don't need to use Mapper to use Lift... use
    // any ORM you want
    Schemifier.schemify(true, Schemifier.infoF _, User)
    Schemifier.schemify(true, Schemifier.infoF _, Project)
    Schemifier.schemify(true, Schemifier.infoF _, code.model.Template)
    Schemifier.schemify(true, Schemifier.infoF _, Job)
    Schemifier.schemify(true, Schemifier.infoF _, Cluster)

    // where to search snippet
    LiftRules.addToPackages("code")


    def sitemapMutators = User.sitemapMutator
    //The SiteMap is built in the Site object bellow 
    LiftRules.setSiteMapFunc(() => sitemapMutators(Site.sitemap))

    //Init the FoBo - Front-End Toolkit module, 
    //see http://liftweb.net/lift_modules for more info
    FoBo.InitParam.JQuery=FoBo.JQuery1102
    FoBo.InitParam.ToolKit=FoBo.Bootstrap320
    FoBo.init()

    //Show the spinny image when an Ajax call starts
    LiftRules.ajaxStart =
      Full(() => LiftRules.jsArtifacts.show("ajax-loader").cmd)

    // Make the spinny image go away when it ends
    LiftRules.ajaxEnd =
      Full(() => LiftRules.jsArtifacts.hide("ajax-loader").cmd)

    // Force the request to be UTF-8
    LiftRules.early.append(_.setCharacterEncoding("UTF-8"))

    // What is the function to test if a user is logged in?
    LiftRules.loggedInTest = Full(() => User.loggedIn_?)

    // Use HTML5 for rendering
    LiftRules.htmlProperties.default.set((r: Req) =>
      new Html5Properties(r.userAgent))

    LiftRules.noticesAutoFadeOut.default.set( (notices: NoticeType.Value) => {
        notices match {
          case NoticeType.Notice => Full((8 seconds, 4 seconds))
          case _ => Empty
        }
     }
    )


    // Make a transaction span the whole HTTP request
    S.addAround(DB.buildLoanWrapper)
  }

  object Site {

    val loggedIn = If(() => User.loggedIn_?,
      () => RedirectResponse("/user_mgt/login"))
    val divider1   = Menu("divider1") / "divider1"
    val ddLabel1   = Menu.i("UserDDLabel") / "ddlabel1"
    val ddLabel2   = Menu.i("Components") / "ddlabel2"
    val home       = Menu.i("Home") / "index"
    val userMenu   = User.AddUserMenusHere

    val dataMenu    = Menu.i("Data") / "data" / "index"


    val jobMenu    = (Menu.i("Jobs") / "components"/"job" / "index").rule(loggedIn)
    val editJobMenu =  Menu.i("Edit Job") / "components"/"job" /"edit" >> Hidden
    val addJobMenu =  Menu.i("Add Job") / "components"/"job" /"add" >> Hidden
    val deleteJobMenu =  Menu.i("Delete Job") / "components"/"job" /"delete" >> Hidden

    val projectMenu    = (Menu.i("Projects") / "components"/"project" / "index").rule(loggedIn)
    val editProjectMenu =  Menu.i("Edit Project") / "components"/"project" /"edit" >> Hidden
    val addProjectMenu =  Menu.i("Add Project") / "components"/"project" /"add" >> Hidden
    val deleteProjectMenu =  Menu.i("Delete Project") / "components"/"project" /"delete" >> Hidden

    val templateMenu    = (Menu.i("Templates") / "components"/"template" / "index").rule(loggedIn)
    val editTemplateMenu =  Menu.i("Edit Template") / "components"/"template" /"edit" >> Hidden
    val addTemplateMenu =  Menu.i("Add Template") / "components"/"template" /"add" >> Hidden
    val deleteTemplateMenu =  Menu.i("Delete Template") / "components"/"template" /"delete" >> Hidden

    val clusterMenu    = (Menu.i("Clusters") / "components"/"cluster" / "index").rule(loggedIn)
    val editClusterMenu =  Menu.i("Edit Cluster") / "components"/"cluster" /"edit" >> Hidden
    val addClusterMenu =  Menu.i("Add Cluster") / "components"/"cluster" /"add" >> Hidden
    val deleteClusterMenu =  Menu.i("Delete Cluster") / "components"/"cluster" /"delete" >> Hidden





    def sitemap = SiteMap(


      home          >> LocGroup("lg1")
      ,editJobMenu,addJobMenu,deleteJobMenu
      ,editProjectMenu,addProjectMenu,deleteProjectMenu
      ,editTemplateMenu,addTemplateMenu,deleteTemplateMenu
      ,editClusterMenu,addClusterMenu,deleteClusterMenu,

    dataMenu >>LocGroup("lg2"),
        ddLabel1      >> LocGroup("topRight") >> PlaceHolder submenus (

            divider1  >> FoBo.TBLocInfo.Divider >> userMenu
            ),
      ddLabel2      >> LocGroup("topRight") >> PlaceHolder submenus (
           projectMenu,
           templateMenu,
           clusterMenu,
           jobMenu
          )
    )
  }

}
