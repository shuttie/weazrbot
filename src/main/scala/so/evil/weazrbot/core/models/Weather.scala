package so.evil.weazrbot.core.models

import org.joda.time.DateTime

/**
 * Created by shutty on 4/15/14.
 */
class Weather(val zone:Int,
              val supplier:String,
              val runAt:DateTime,
              val forecastFor:DateTime,
              val lat:Float,
              val lon:Float,
              val tmp:Float,
              val rh:Float,
              val precip:Float )
