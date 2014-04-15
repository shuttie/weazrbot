package so.evil.weazrbot.core

/**
 * Created by shutty on 3/1/14.
 */
case class PointMetadata(forecastName:String,
                    dateAt:String,
                    dateFor:String)

case class WeatherPoint(lat:Float,
                   lon:Float,
                   temperature:Float,
                   preciperation:Float,
                   cloudsHigh:Int,
                   cloudsMed:Int,
                   cloudsLow:Int,
                   windDirection:Int,
                   windSpeed:Int,
                   windGusts:Int,
                   humidity:Int ) {

}
case class WeatherSnapshot(key:PointMetadata, points:List[WeatherPoint]) {

}
