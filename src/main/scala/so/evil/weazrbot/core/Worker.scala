package so.evil.weazrbot.core

import akka.actor.Actor
import so.evil.weazrbot.core.messages.Import
import org.apache.commons.net.ftp.{FTP, FTPClient}
import java.net.URI
import java.io.{BufferedOutputStream, ByteArrayOutputStream}
import so.evil.weazrbot.core.db.Cassandra
import org.apache.commons.io.IOUtils

/**
 * Created by shutty on 4/15/14.
 */
class Worker(supplierName:String) extends Actor {
  val db = Cassandra()
  val ftp = new FTPClient()
  val supplier = SupplierFactory.build(db, supplierName)

  override def postStop() = {
    db.close()
    if (ftp.isConnected) ftp.disconnect()
    println("worker closed")
  }

  def receive = {
    case Import(target) => {
      val uri = new URI(target)
      val fileName = if (GribStore.exists(uri.getPath)) {
        GribStore.getFileName(uri.getPath)
      } else {
        if (!ftp.isConnected) {
          ftp.connect(uri.getHost)
          ftp.login("anonymous","mail@evil.so")
        }
        ftp.setFileType(FTP.BINARY_FILE_TYPE)
        val stream = ftp.retrieveFileStream(uri.getPath)
        GribStore.store(uri.getPath, IOUtils.toByteArray(stream))
        GribStore.getFileName(uri.getPath)
      }
      val data = supplier.parse(uri.getPath, fileName)
      data.foreach(db.addWeather)
      println("import done")
    }
  }
}
