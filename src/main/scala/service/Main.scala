package service

import com.typesafe.config.ConfigFactory
import io.PostgreSQLHandler

object Main {

  /*
  * Hello this is the starting point of this service.
  * because this is an exercise and not a real production project there are
  * some things I would do a bit different and I will write comments
  * to explain some of them.
  *
  * the general structure of this service is:
  * you got The akka service that will listen to requests,
  * it will send the request to a RequestHandler (in this case PostgreSQLHandler)
  * and will process the request accordingly.
  *
  * if it was a real project
  * 1) I would ask to send in the request another field
  * called requestType and according to the request type I will have something like
  * static factory that will know which request handler to use according to the request type.
  *
  * 2) I would probably also create abstraction like a QueryBase and some query types...
  *
  * of course what I wrote is really situation dependent
  * so there needs to a balance between over design and generic code to the
  * purpose of this service and it's future plans
  * */

  def main(args: Array[String]): Unit ={
    val config = ConfigFactory.load("application.conf").getConfig("connection_info")
    val postgreSQLHandler = new PostgreSQLHandler()
    postgreSQLHandler.initConnection("databaseUrl")
    val service = new QueryService(postgreSQLHandler)
    service.start(config.getString("address"), config.getInt("port"))
  }

}
