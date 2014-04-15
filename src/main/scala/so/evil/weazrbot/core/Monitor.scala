package so.evil.weazrbot.core

import so.evil.weazrbot.modules.Supplier
import akka.actor.{PoisonPill, Actor, Props}
import so.evil.weazrbot.core.messages.{Import, CheckAvailability}
import org.apache.commons.net.ftp.FTPClient
import java.net.URI
import scala.concurrent.duration._
import scala.util.Random
import so.evil.weazrbot.core.db.Cassandra

/**
 * Created by shutty on 3/1/14.
 */

class Monitor(val supplierName:String) extends Actor {
  val CHECK_EVERY = 10.minutes
  val WORKER_COUNT = 1

  import context.dispatcher
  context.system.scheduler.schedule(0.seconds, CHECK_EVERY, self, CheckAvailability)

  val db = Cassandra()

  def receive = {
    case CheckAvailability => {
      val supplier = SupplierFactory.build(db, supplierName)
      if (supplier.shouldImport) {
        val workers = for (i <- 0 until WORKER_COUNT) yield { context.actorOf(Props(classOf[Worker], supplierName)) }
        supplier.targetURLs.zipWithIndex.foreach { case (url,index) => {
          val workerIndex = index % WORKER_COUNT
          workers(workerIndex) ! Import(url)
        }}
        workers.foreach(_ ! PoisonPill)
      }
    }
  }
}
