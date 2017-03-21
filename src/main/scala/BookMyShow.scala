import akka.actor.{Actor, ActorSystem, Props}
import akka.routing.FromConfig
import com.typesafe.config.ConfigFactory


class BookMyShow extends Actor{

  var status=false

  override def receive = {

    case bookSeat if(bookSeat.equals("Booking Seat "))=>{
      if(status==false)
        {
          status=true
          println(s"Thanks for $bookSeat !!")
        }
      else {

        println("Seat already booked !!")
      }

    }

    case cancelSeat if(cancelSeat.equals("Canceling Seat "))=>{

      if(status==true) {
        status = false
        println(s"$cancelSeat Successful")
      }
      else
        println("Seat Canceled Already !!")
    }
  }
}

object BookMyShowApp extends App{

  val config = ConfigFactory.parseString(
    """
      |akka.actor.deployment {
      | /poolRouter {
      |   router = balancing-pool
      |   nr-of-instances = 5
      | }
      |}
    """.stripMargin
  )

  val system = ActorSystem("RouterSystem", config)
  val router = system.actorOf(FromConfig.props(Props[BookMyShow]), "poolRouter")

  router ! "Booking Seat "
  Thread.sleep(1000)
  router ! "Booking Seat "
  Thread.sleep(1000)
  router ! "Canceling Seat "
  Thread.sleep(1000)
  router ! "Canceling Seat "
  Thread.sleep(1000)
  router ! "Booking Seat "

}
