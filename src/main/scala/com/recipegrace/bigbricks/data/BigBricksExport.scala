package com.recipegrace.bigbricks.data

import code.model.{Cluster, Job, Project, Template, User}
import net.liftweb.json.Serialization._
/**
 * Created by fjacob on 8/21/15.
 */



object BigBricksExport {



  def fromCluster(cluster:Cluster) = {
      ClusterWrapper(cluster.clusterName.get, cluster.userName.get)
    }

   def fromProject(project:Project) = {
      ProjectWrapper(project.gitBranch.get, project.jarName.get, project.projectLocation.get, project.projectName.get)
    }


    def fromTemplate(template:Template) = {
      TemplateWrapper(template.template.get, template.templateName.get)
    }


    def fromJob(job:Job) = {
      JobWrapper(job.arguments.get, job.mainClassName.get, job.project.get, job.template.get)
    }

  def fromUser(user:User) = {
    UserWrapper(user.email.get, user.firstName.get, user.lastName.get, user.locale.get, user.timezone.get)
  }

  def exportBigBricks() = {
    implicit val formats = net.liftweb.json.DefaultFormats
   val data= BigBricksData(Cluster.findAll().map(fromCluster)
    ,Project.findAll().map(fromProject)
    ,Template.findAll().map(fromTemplate)
    ,Job.findAll().map(fromJob)
    ,User.findAll().map(fromUser))

    write(data)



  }

}
