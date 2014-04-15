package so.evil.weazrbot.modules

import ucar.nc2.NetcdfFile
import scala.List
import so.evil.weazrbot.core.db.Cassandra
import org.apache.commons.net.ftp.FTPClient
import java.net.URI
import org.joda.time.{DateTimeZone, DateTime}
import org.joda.time.format.DateTimeFormat
import java.nio.ByteBuffer
import so.evil.weazrbot.core.models.Weather
import ucar.ma2.ArrayFloat
import ucar.ma2.ArrayFloat.D4

/**
 * Created by shutty on 3/1/14.
 */
class GFSSupplier(db:Cassandra) extends Supplier("gfs", db) {
  val dir:String = "ftp://localhost/pub/data/nccf/com/gfs/prod/"
  val dirFormat = DateTimeFormat.forPattern("yyyyMMddHHmm")

  def shouldImport:Boolean = {
    db.getLatestRun(name) match {
      case None => true
      case Some(latestImport:DateTime) => {
        val gfsDirName = "gfs."+latestImport.toString(dirFormat)
        val ftp = new FTPClient()
        val dirUri = new URI(dir)
        try {
          ftp.connect(dirUri.getHost)
          ftp.login("anonymous", "mail@evil.so")
          val names = ftp.listNames(dirUri.getPath)
          val x=1
        }
        true
      }
    }
  }
  def targetURLs:List[String] = {
    List(dir + "gfs.2014041500/" + "gfs.t00z.pgrb2f03")
  }


  val datePattern = """.*/gfs\.(\d{4})(\d{2})(\d{2})(\d{2})/gfs\.t\d{2}z.pgrb2f(\d{2})""".r
  override def parse(target:String, fileName:String):List[Weather] = {

    def toList[T](array:ucar.ma2.Array) = {
      array.copyTo1DJavaArray().asInstanceOf[Array[T]].toList
    }

    val tzone = DateTimeZone.forID("UTC")
    val dates = target match {
      case datePattern(year,month,day,hour,offset) => {
        val runDate = new DateTime(year.toInt, month.toInt, day.toInt, hour.toInt, 0, tzone)
        val forecastDate= runDate.plusHours(offset.toInt)
        (runDate, forecastDate)
      }
    }

    val cdf = NetcdfFile.open(fileName)
    val allVars = cdf.getVariables
    val lats = toList[Float](cdf.findVariable("lat").read())
    val lons = toList[Float](cdf.findVariable("lon").read())
    val heights = toList[Float](cdf.findVariable("isobaric").read())
    val heights1 = toList[Float](cdf.findVariable("isobaric1").read())

    val TMP = cdf.findVariable("Temperature_isobaric").read.asInstanceOf[ArrayFloat.D4]
    val TMPS = cdf.findVariable("Temperature_surface").read.asInstanceOf[ArrayFloat.D3]
    val RH = cdf.findVariable("Relative_humidity_isobaric").read.asInstanceOf[ArrayFloat.D4]
    val PRECIP = cdf.findVariable("Total_precipitation_surface_3_Hour_Accumulation").read.asInstanceOf[ArrayFloat.D3]
    val result = (for (
      lat <- lats.zipWithIndex;
      lon <- lons.zipWithIndex;
      height <- heights.zipWithIndex.filter(tuple => Math.abs(tuple._1 - 5000) < 1)
    ) yield {
      val zone = Math.round(lon._1)*360 + Math.round(lat._1)
      //val isobaricIndex = heights.size*height._2 + lats.size*lat._2 + lon._2
      //val surfaceIndex = lats.size*lat._2 + lon._2
      val tmp = TMP.get(0,height._2, lat._2, lon._2) - 273.15f
      val tmps = TMPS.get(0, lat._2, lon._2) - 273.15f
      val rh = RH.get(0,height._2, lat._2, lon._2)
      val precip = PRECIP.get(0, lat._2, lon._2)
      new Weather(zone,"gfs", dates._1, dates._2, lat._1,lon._1,tmp,rh,precip)
    }).toList
    result
  }

}
