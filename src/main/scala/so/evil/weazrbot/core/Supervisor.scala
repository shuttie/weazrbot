package so.evil.weazrbot.core

import so.evil.weazrbot.modules.Module
import akka.actor.{Actor, Props, ActorSystem}
import so.evil.weazrbot.core.messages.{Disconnect, Download, Connect, CheckAvailability}
import org.apache.commons.net.ftp.FTPClient
import java.net.URI
import scala.concurrent.duration._
import scala.util.Random

/**
 * Created by shutty on 3/1/14.
 */

class Supervisor(val module:Module) extends Actor {
  import context.dispatcher
  context.system.scheduler.schedule(0.seconds, 10.minutes, self, CheckAvailability)
  def receive = {
    case CheckAvailability => {
      val ftp = new FTPClient()
      val dir = new URI(module.dir)
      try {
        ftp.connect(dir.getHost)
        ftp.login("anonymous", "mail@evil.so")
        val names = ftp.listNames(dir.getPath)
        val workers = (for (i <- 0 to 5) yield context.actorOf(Props(classOf[Worker], module), name = s"worker$i")).toList
        workers.foreach { _ ! Connect(dir.getHost, "anonymous", "mail@evil.so") }
        names.map {name => {
          val worker = workers(Random.nextInt(workers.size))
          worker ! Download(name)
        }}
        workers.foreach { _ ! Disconnect }
      }

    }
  }
}
