package so.evil.weazrbot

import akka.actor.{Props, ActorSystem}
import so.evil.weazrbot.modules.GFSSupplier
import so.evil.weazrbot.core.Monitor

/**
 * Created by shutty on 3/1/14.
 */
object Weazrbot {
  def main(args:Array[String]) = {
    val system = ActorSystem.create("weazr")
    system.actorOf(Props(classOf[Monitor], "gfs"), name = "gfs_monitor")
    system.awaitTermination()
  }
}
