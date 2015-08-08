package code
package snippet

import net.liftweb._
import http._
import net.liftweb.util._
import net.liftweb.common._
import Helpers._

import org.specs2.mutable.Specification



object SnippetTestExample extends Specification {
  val session = new LiftSession("", randomString(20), Empty)
  val stableTime = now



  "Example Snippet" should {
    "Put the time in the node" in {
      val hello = new JobRender
      Thread.sleep(1000) // make sure the time changes

      val str = hello.time( <span>This<span id="time">Time goes here</span> </span>).toString

      println(str, stableTime)

      str must startWith("<span>This")
    }
  }
}
