package service

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives
import akka.stream.ActorMaterializer
import io.RequestHandler
import org.slf4j.{Logger, LoggerFactory}

import scala.concurrent.ExecutionContextExecutor
import scala.util.{Failure, Success}


class QueryService(requestHandler : RequestHandler[QueryToFileRequest, Unit]) extends QueryRequestJsonSupport with Directives {

  val log :Logger = LoggerFactory.getLogger(this.getClass)
  def start(address : String, port: Int): Unit ={
    implicit val system: ActorSystem = ActorSystem("my-system")
    implicit val materializer: ActorMaterializer = ActorMaterializer()
    implicit val executionContext: ExecutionContextExecutor = system.dispatcher

    val route = path("query"){
      concat(
        post {

          entity(as[QueryToFileRequest]) { queryRequest =>
            requestHandler.executeRequest(queryRequest)
            log.debug(s"got the request ${queryRequest.query} and file name: ${queryRequest.fileName}")
            complete(s"query: ${queryRequest.query} - file name: ${queryRequest.fileName}")
          }
        },
        get{
          complete("hello this is a  connection test for the get query path ! :)")
        })
    }

    val bindingFuture = Http().bindAndHandle(route, address, port)
    bindingFuture.onComplete({
      case Success(value) => log.info("The service is up !")
      case Failure(e : Throwable) => log.error(s"There was a problem while starting service ${e.getMessage}" +
        s" ${e.getStackTrace.mkString("", ",\n ", ")")}")
    })
  }
}
