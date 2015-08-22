package com.recipegrace.biglegos.data

import code.model._
import net.liftweb.common.Logger
import net.liftweb.mapper.By
import net.liftweb.json._
/**
 * Created by fjacob on 8/16/15.
 */
object BigBricksImport {
  val logger = Logger(this.getClass.getName)
  val TEMP_PASSWORD= "helloworld"

  def importBigBricks(content:String) = {
    implicit val formats = net.liftweb.json.DefaultFormats
    val bigBricksData = parse(content).extract[BigBricksData]
    bigBricksData.projects.foreach(saveProject)
    bigBricksData.templates.foreach(saveTemplate)
    bigBricksData.jobs.foreach(saveJob)
    bigBricksData.clusters.foreach(saveCluster)
    bigBricksData.users.foreach(saveUser)

  }
  def saveJob(job:JobWrapper)= {

    if(Job.findAll(
      By(Job.project, job.project),
      By(Job.template, job.template),
      By(Job.mainClassName, job.mainClassName)).size==0) {
      Job.create
        .arguments(job.arguments)
        .mainClassName(job.mainClassName)
        .template(job.template)
        .project(job.project)
        .save
    } else {
      logger.warn("skipped" + job)
    }
  }

  def saveTemplate(template:TemplateWrapper)= {
    if(Template.findAll(
      By(Template.templateName, template.templateName)).size==0) {
      Template.create
        .templateName(template.templateName)
        .template(template.template)
        .save
    }else {
      logger.warn("skipped" + template)
    }

  }

  def saveCluster(cluster:ClusterWrapper)= {
    if(Cluster.findAll(
      By(Cluster.clusterName, cluster.clusterName)).size==0) {
      Cluster.create
        .clusterName(cluster.clusterName)
        .userName(cluster.userName)
        .password(TEMP_PASSWORD)
        .save
    }else {
      logger.warn("skipped" + cluster)
    }

  }

  def saveProject(project:ProjectWrapper)= {
    if(Project.findAll(
      By(Project.projectName, project.projectName)).size==0) {
      Project.create
        .gitBranch(project.gitBranch)
        .jarName(project.jarName)
        .projectLocation(project.projectLocation)
        .projectName(project.projectName)
        .save
    }else {
      logger.warn("skipped" + project)
    }
  }
  def saveUser(user:UserWrapper)= {

    if(User.findAll(
      By(User.email, user.email)).size==0) {
      User.create
        .email(user.email)
        .firstName(user.firstName)
        .lastName(user.lastName)
        .locale(user.locale)
        .timezone(user.timezone)
        .password(TEMP_PASSWORD)
        .validated(true)
        .save
    }else {

      logger.warn("skipped" + user)
    }
  }
}
