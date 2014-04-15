package so.evil.weazrbot

import akka.actor.{Props, ActorSystem}
import so.evil.weazrbot.core.Supervisor
import so.evil.weazrbot.modules.GFSModule

/**
 * Created by shutty on 3/1/14.
 */
object Weazrbot {
  def main(args:Array[String]) = {
    val system = ActorSystem.create("weazr")
    system.actorOf(Props(classOf[Supervisor], new GFSModule()), name = "supervisor")
    system.awaitTermination()
  }
}
