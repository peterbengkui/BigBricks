package com.recipegrace.bigbricks

/**
 * Created by fjacob on 8/22/15.
 */
package object data {
  case class ClusterWrapper(clusterName:String, userName:String)
  case class ProjectWrapper(gitBranch:String, jarName:String, projectLocation:String, projectName:String )
  case class TemplateWrapper(template:String, templateName:String )
  case class JobWrapper(arguments:String, mainClassName:String, project:Long, template: Long)
  case class UserWrapper(email:String, firstName:String, lastName:String, locale:String, timezone:String)

  case class BigBricksData(clusters:List[ClusterWrapper], projects:List[ProjectWrapper], templates:List[TemplateWrapper],
                           jobs:List[JobWrapper], users:List[UserWrapper])
}
