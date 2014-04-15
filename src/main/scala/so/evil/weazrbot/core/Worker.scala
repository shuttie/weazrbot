package so.evil.weazrbot.core

import so.evil.weazrbot.core.messages.{Disconnect, Connect, Download}
import akka.actor.{PoisonPill, Actor}
import org.apache.commons.net.ftp.{FTP, FTPClient}
import java.io._
import so.evil.weazrbot.modules.Module
import so.evil.weazrbot.core.messages.Connect
import so.evil.weazrbot.core.messages.Download

/**
 * Created by shutty on 3/21/14.
 */
class Worker(val module:Module) extends Actor {
  val ftp = new FTPClient()

  def receive = {
    case Connect(host:String, login:String, password:String) => {
      ftp.connect(host)
      ftp.login(login, password)
    }
    case Download(fileName:String) => {
      val f = new File("/tmp/file2.grib2")
      val stream = new BufferedOutputStream(new FileOutputStream(f))
      ftp.setFileType(FTP.BINARY_FILE_TYPE)
      ftp.retrieveFile(fileName, stream)
      stream.close()
      val snapshot = module.parse("/tmp/file2.grib2")
    }
    case Disconnect => {
      ftp.disconnect()
      self ! PoisonPill
    }
  }
}
