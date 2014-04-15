package so.evil.weazrbot.core

import java.io._
import org.apache.commons.io.IOUtils

/**
 * Created by shutty on 4/15/14.
 */
object GribStore {
  val DIR = "/home/shutty/tmp/gribs/"

  def exists(target:String):Boolean = {
    getFile(target).exists()
  }

  def store(target:String, bytes:Array[Byte]) = {
    val f = getFile(target)
    val out = new BufferedOutputStream(new FileOutputStream(f))
    out.write(bytes)
    out.close()
  }

  def getFileName(target:String) = {
    DIR+target.replaceAll("/","~")
  }

  def getFile(target:String) = {
    new File(getFileName(target))
  }
}
