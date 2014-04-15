package so.evil.weazrbot.modules

import ucar.nc2.NetcdfFile
import scala.List
import so.evil.weazrbot.core.{WeatherPoint, WeatherSnapshot}

/**
 * Created by shutty on 3/1/14.
 */
class GFSModule extends Module {

  def dir:String = "ftp://localhost/pub/data/nccf/com/gfs/prod/gfs.2014031606"
  def files:List[String] = List("gfs.t06z.pgrb2bf00")
  override def parse(fileName:String):WeatherSnapshot = {
    val cdf = NetcdfFile.open(fileName)
    val allVars = cdf.getVariables
    val lats = toList[Float](cdf.findVariable("lat").read())
    val lons = toList[Float](cdf.findVariable("lon").read())
    val heights = toList[Float](cdf.findVariable("isobaric").read())
    val TMP = cdf.findVariable("Temperature_isobaric").read
    val RH = cdf.findVariable("Relative_humidity_isobaric").read
    val CLWMR = cdf.findVariable("Cloud_mixing_ratio_isobaric").read
    val UGRD = cdf.findVariable("u-component_of_wind_isobaric").read
    val VGRD = cdf.findVariable("v-component_of_wind_isobaric").read
    val MOIST = cdf.findVariable("Liquid_Volumetric_Soil_Moisture_non_Frozen_depth_below_surface_layer").read
    for (
      lat <- lats.zipWithIndex;
      lon <- lons.zipWithIndex;
      height <- heights.zipWithIndex.filter(tuple => Math.abs(tuple._1 - 500) < 0.1)
    ) yield {
      val isobaricIndex = lats.size*lat._2 + lons.size*lon._2 + height._2
      val surfaceIndex = lats.size*lat._2 + lon._2
      val temp = TMP.getFloat(isobaricIndex)
      val hum = RH.getFloat(isobaricIndex)
      val cloud = RH.getFloat(isobaricIndex)
      val uwind = UGRD.getFloat(isobaricIndex)
      val vwind = VGRD.getFloat(isobaricIndex)
      val rain = MOIST.getFloat(isobaricIndex)
      WeatherPoint(lat._1, lon._1, temp, rain, 0, 0, 0, 0, 0, 0, 0)
    }
    WeatherSnapshot(null,null)
  }

  private def toList[T](array:ucar.ma2.Array) = {
    array.copyTo1DJavaArray().asInstanceOf[Array[T]].toList
  }
}
